-- Overlook Hotel schema.sql
-- PostgreSQL 12+
-- Last updated: 2025-09-23

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Roles
CREATE TABLE roles (
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR(32) NOT NULL UNIQUE,
  description  TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Users
CREATE TABLE users (
  id                BIGSERIAL PRIMARY KEY,
  email             VARCHAR(255) NOT NULL UNIQUE,
  username          VARCHAR(100),
  password_hash     VARCHAR(255) NOT NULL, -- bcrypt hash expected
  first_name        VARCHAR(100),
  last_name         VARCHAR(100),
  phone_number      VARCHAR(30),
  date_of_birth     DATE,
  profile_photo_url TEXT,
  is_active         BOOLEAN NOT NULL DEFAULT true,
  role_id           BIGINT NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
  annual_leave_balance DECIMAL(8,2) DEFAULT 0,
  sick_leave_balance  DECIMAL(8,2) DEFAULT 0,
  created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  last_login        TIMESTAMPTZ,
  version           INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role_id);

-- Rooms
CREATE TABLE rooms (
  id              BIGSERIAL PRIMARY KEY,
  room_number     VARCHAR(20) NOT NULL UNIQUE,
  room_type       VARCHAR(50) NOT NULL,
  capacity        INT NOT NULL CHECK (capacity > 0),
  base_price      NUMERIC(10,2) NOT NULL CHECK (base_price >= 0),
  description     TEXT,
  floor_number    INT,
  amenities       TEXT[], -- simple array of amenity codes/names
  status          VARCHAR(32) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, OCCUPIED, CLEANING, MAINTENANCE
  is_accessible   BOOLEAN NOT NULL DEFAULT FALSE,
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  version         INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_rooms_room_number ON rooms(room_number);
CREATE INDEX idx_rooms_status ON rooms(status);
CREATE INDEX idx_rooms_room_type ON rooms(room_type);

-- Reservations
CREATE TABLE reservations (
  id                BIGSERIAL PRIMARY KEY,
  user_id           BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  room_id           BIGINT NOT NULL REFERENCES rooms(id) ON DELETE RESTRICT,
  lead_guest_name   VARCHAR(255) NOT NULL,
  lead_guest_phone  VARCHAR(30),
  check_in_date     DATE NOT NULL,
  check_out_date    DATE NOT NULL,
  guests_count      INT NOT NULL CHECK (guests_count > 0),
  total_price       NUMERIC(12,2) NOT NULL CHECK (total_price >= 0),
  status            VARCHAR(32) NOT NULL DEFAULT 'BOOKED', -- BOOKED, CHECKED_IN, CHECKED_OUT, CANCELLED, NO_SHOW
  special_requests  TEXT,
  cancellation_policy TEXT,
  created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  version           INT NOT NULL DEFAULT 0,
  CONSTRAINT chk_dates CHECK (check_in_date < check_out_date)
);

-- Prevent obvious overlapping bookings at DB level using exclusion constraint on daterange
ALTER TABLE reservations
  ADD COLUMN stay_period DATERANGE GENERATED ALWAYS AS (daterange(check_in_date, check_out_date, '[]')) STORED;

-- Exclusion constraint (requires btree_gist extension)
CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE reservations
  ADD CONSTRAINT no_room_overlap
    EXCLUDE USING GIST (room_id WITH =, stay_period WITH &&)
    WHERE (status <> 'CANCELLED'); -- allow overlaps for cancelled reservations

CREATE INDEX idx_reservations_user ON reservations(user_id);
CREATE INDEX idx_reservations_room ON reservations(room_id);
CREATE INDEX idx_reservations_checkin ON reservations(check_in_date);
CREATE INDEX idx_reservations_checkout ON reservations(check_out_date);
CREATE INDEX idx_reservations_status ON reservations(status);

-- Loyalty points ledger
CREATE TABLE loyalty_points (
  id               BIGSERIAL PRIMARY KEY,
  user_id          BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  total_points     INT NOT NULL DEFAULT 0,
  lifetime_points  INT NOT NULL DEFAULT 0,
  tier             VARCHAR(50),
  last_earned_at   TIMESTAMPTZ,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_loyalty_user ON loyalty_points(user_id);

CREATE TABLE loyalty_transactions (
  id               BIGSERIAL PRIMARY KEY,
  user_id          BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  reservation_id   BIGINT REFERENCES reservations(id) ON DELETE SET NULL,
  points_change    INT NOT NULL,
  balance_after    INT,
  transaction_type VARCHAR(50) NOT NULL, -- EARN, REDEEM, ADJUST
  description      TEXT,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_loyalty_tx_user ON loyalty_transactions(user_id);
CREATE INDEX idx_loyalty_tx_reservation ON loyalty_transactions(reservation_id);

-- Employee shifts
CREATE TABLE employee_shifts (
  id             BIGSERIAL PRIMARY KEY,
  user_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  shift_date     DATE NOT NULL,
  start_time     TIMESTAMPTZ NOT NULL,
  end_time       TIMESTAMPTZ NOT NULL,
  shift_type     VARCHAR(50),
  department     VARCHAR(100),
  status         VARCHAR(32) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, CANCELLED, COMPLETED
  notes          TEXT,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  version        INT NOT NULL DEFAULT 0,
  CONSTRAINT chk_shift_times CHECK (start_time < end_time)
);

CREATE INDEX idx_shifts_user ON employee_shifts(user_id);
CREATE INDEX idx_shifts_date ON employee_shifts(shift_date);

-- Shift swap requests
CREATE TABLE shift_swap_requests (
  id                  BIGSERIAL PRIMARY KEY,
  requester_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  target_shift_id     BIGINT NOT NULL REFERENCES employee_shifts(id) ON DELETE CASCADE,
  replacement_shift_id BIGINT REFERENCES employee_shifts(id) ON DELETE SET NULL,
  status              VARCHAR(32) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
  created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
  reviewed_at         TIMESTAMPTZ,
  reviewed_by         BIGINT REFERENCES users(id) ON DELETE SET NULL,
  review_notes        TEXT
);

CREATE INDEX idx_swap_request_requester ON shift_swap_requests(requester_id);

-- Holiday requests / time off
CREATE TABLE holiday_requests (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  start_date   DATE NOT NULL,
  end_date     DATE NOT NULL,
  request_type VARCHAR(50),
  status       VARCHAR(32) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
  reason       TEXT,
  reviewed_by  BIGINT REFERENCES users(id) ON DELETE SET NULL,
  reviewed_at  TIMESTAMPTZ,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT chk_holiday_dates CHECK (start_date <= end_date)
);

CREATE INDEX idx_holiday_user ON holiday_requests(user_id);

-- Guest feedback
CREATE TABLE guest_feedback (
  id             BIGSERIAL PRIMARY KEY,
  reservation_id  BIGINT REFERENCES reservations(id) ON DELETE SET NULL,
  user_id        BIGINT REFERENCES users(id) ON DELETE SET NULL,
  rating         INT CHECK (rating >= 1 AND rating <= 5),
  feedback_text  TEXT,
  categories     TEXT[],
  anonymous      BOOLEAN NOT NULL DEFAULT FALSE,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_feedback_reservation ON guest_feedback(reservation_id);
CREATE INDEX idx_feedback_user ON guest_feedback(user_id);

CREATE TABLE feedback_responses (
  id              BIGSERIAL PRIMARY KEY,
  feedback_id     BIGINT NOT NULL REFERENCES guest_feedback(id) ON DELETE CASCADE,
  responder_id    BIGINT REFERENCES users(id) ON DELETE SET NULL,
  responder_role  VARCHAR(50),
  response_text   TEXT,
  attachment_url  TEXT,
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Notification preferences + notifications (polymorphic target via entity_type/entity_id)
CREATE TABLE notification_preferences (
  id                 BIGSERIAL PRIMARY KEY,
  user_id            BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  email_enabled      BOOLEAN NOT NULL DEFAULT TRUE,
  sms_enabled        BOOLEAN NOT NULL DEFAULT FALSE,
  push_enabled       BOOLEAN NOT NULL DEFAULT FALSE,
  marketing_emails   BOOLEAN NOT NULL DEFAULT FALSE,
  reservation_updates BOOLEAN NOT NULL DEFAULT TRUE,
  feedback_notifications BOOLEAN NOT NULL DEFAULT TRUE,
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE notifications (
  id                    BIGSERIAL PRIMARY KEY,
  user_id               BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  notification_preferences_snapshot JSONB, -- snapshot of prefs at send time (nullable)
  type                  VARCHAR(100),
  title                 VARCHAR(255),
  message               TEXT,
  is_read               BOOLEAN NOT NULL DEFAULT FALSE,
  channel               VARCHAR(32), -- EMAIL, SMS, PUSH, IN_APP
  related_entity_type   VARCHAR(100),
  related_entity_id     BIGINT,
  channel_snapshot      JSONB,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_entity ON notifications(related_entity_type, related_entity_id);

-- Room status logs
CREATE TABLE room_status_logs (
  id           BIGSERIAL PRIMARY KEY,
  room_id      BIGINT NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  status       VARCHAR(32) NOT NULL,
  changed_by   BIGINT REFERENCES users(id) ON DELETE SET NULL,
  changed_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  notes        TEXT
);

CREATE INDEX idx_room_status_logs_room ON room_status_logs(room_id);

-- Audit logs (polymorphic)
CREATE TABLE audit_logs (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT REFERENCES users(id) ON DELETE SET NULL,
  action       VARCHAR(100) NOT NULL,
  entity_type  VARCHAR(100),
  entity_id    BIGINT,
  details      JSONB,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);

-- Simple lookup for room amenities (optional)
CREATE TABLE amenities (
  code    VARCHAR(50) PRIMARY KEY,
  name    VARCHAR(255) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Optional mapping table if you prefer normalized amenities per room
CREATE TABLE room_amenities (
  room_id BIGINT NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  amenity_code VARCHAR(50) NOT NULL REFERENCES amenities(code),
  PRIMARY KEY (room_id, amenity_code)
);

-- Triggers to maintain updated_at and increment version
CREATE OR REPLACE FUNCTION touch_updated_at() RETURNS TRIGGER LANGUAGE plpgsql AS $
BEGIN
  NEW.updated_at = now();
  NEW.version = COALESCE(NEW.version, 0) + 1;
  RETURN NEW;
END;
$;

-- Attach trigger to tables using optimistic locking
CREATE TRIGGER trg_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION touch_updated_at();
CREATE TRIGGER trg_rooms_updated_at BEFORE UPDATE ON rooms FOR EACH ROW EXECUTE FUNCTION touch_updated_at();
CREATE TRIGGER trg_reservations_updated_at BEFORE UPDATE ON reservations FOR EACH ROW EXECUTE FUNCTION touch_updated_at();
CREATE TRIGGER trg_employee_shifts_updated_at BEFORE UPDATE ON employee_shifts FOR EACH ROW EXECUTE FUNCTION touch_updated_at();


SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Roles
CREATE TABLE IF NOT EXISTS role (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(32) NOT NULL UNIQUE,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Users
CREATE TABLE IF NOT EXISTS "user" (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    first_name      VARCHAR(100),
    last_name       VARCHAR(100),
    phone           VARCHAR(50),
    enabled         BOOLEAN NOT NULL DEFAULT true,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- User <-> Role many-to-many
CREATE TABLE IF NOT EXISTS user_role (
    user_id         BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    role_id         BIGINT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Rooms
CREATE TYPE room_status AS ENUM ('AVAILABLE','OCCUPIED','CLEANING','MAINTENANCE');

CREATE TABLE IF NOT EXISTS room (
    id              BIGSERIAL PRIMARY KEY,
    number          VARCHAR(50) NOT NULL UNIQUE,
    type            VARCHAR(50) NOT NULL, -- e.g., STANDARD, DELUXE, SUITE
    capacity        INT NOT NULL DEFAULT 1,
    price_per_night NUMERIC(10,2) NOT NULL CHECK (price_per_night >= 0),
    amenities       TEXT, -- JSON/text listing amenities
    status          room_status NOT NULL DEFAULT 'AVAILABLE',
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Reservations
CREATE TYPE reservation_status AS ENUM ('BOOKED','CANCELLED','CHECKED_IN','CHECKED_OUT');

CREATE TABLE IF NOT EXISTS reservation (
    id              BIGSERIAL PRIMARY KEY,
    guest_id        BIGINT NOT NULL REFERENCES "user"(id) ON DELETE RESTRICT,
    room_id         BIGINT NOT NULL REFERENCES room(id) ON DELETE RESTRICT,
    start_date      DATE NOT NULL,
    end_date        DATE NOT NULL,
    status          reservation_status NOT NULL DEFAULT 'BOOKED',
    total_price     NUMERIC(12,2) NOT NULL CHECK (total_price >= 0),
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT now(),
    version         BIGINT NOT NULL DEFAULT 0
);

-- Trigger to keep updated_at and increment version on reservation updates
CREATE OR REPLACE FUNCTION touch_updated_at()
RETURNS TRIGGER AS $
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION incr_version()
RETURNS TRIGGER AS $
BEGIN
  IF TG_OP = 'UPDATE' THEN
    NEW.version = OLD.version + 1;
  END IF;
  RETURN NEW;
END;
$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS reservation_touch_updated_at ON reservation;
CREATE TRIGGER reservation_touch_updated_at
BEFORE UPDATE ON reservation
FOR EACH ROW
EXECUTE FUNCTION touch_updated_at();

DROP TRIGGER IF EXISTS reservation_incr_version ON reservation;
CREATE TRIGGER reservation_incr_version
BEFORE UPDATE ON reservation
FOR EACH ROW
EXECUTE FUNCTION incr_version();

-- Room status change log
CREATE TABLE IF NOT EXISTS room_status_log (
    id              BIGSERIAL PRIMARY KEY,
    room_id         BIGINT NOT NULL REFERENCES room(id) ON DELETE CASCADE,
    status          room_status NOT NULL,
    note            TEXT,
    updated_by_id   BIGINT REFERENCES "user"(id) ON DELETE SET NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Audit log (simple)
CREATE TABLE IF NOT EXISTS audit_log (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES "user"(id) ON DELETE SET NULL,
    operation       VARCHAR(100) NOT NULL,
    target_type     VARCHAR(100),
    target_id       VARCHAR(100),
    details         JSONB, -- optional structured details
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_reservation_room_dates ON reservation (room_id, start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_reservation_guest ON reservation (guest_id);
CREATE INDEX IF NOT EXISTS idx_room_type_capacity_price ON room (type, capacity, price_per_night);
CREATE INDEX IF NOT EXISTS idx_room_status ON room (status);
CREATE INDEX IF NOT EXISTS idx_audit_user ON audit_log (user_id);

-- Reservation conflict-check helper (optional view/function)
-- This function returns true if there exists a conflicting reservation for given room and date range
CREATE OR REPLACE FUNCTION exists_conflicting_reservation(p_room_id BIGINT, p_start DATE, p_end DATE)
RETURNS BOOLEAN LANGUAGE sql AS $
  SELECT EXISTS (
    SELECT 1 FROM reservation r
    WHERE r.room_id = p_room_id
      AND r.status IN ('BOOKED','CHECKED_IN')
      AND NOT (r.end_date <= p_start OR r.start_date >= p_end)
    LIMIT 1
  );
$;

-- Constraints / validations
ALTER TABLE reservation
  ADD CONSTRAINT reservation_dates_check CHECK (start_date < end_date);

-- (Optional) trigger to compute total_price if not provided — skipped here; business layer computes price.

-- Seed-safe: ensure roles exist if not present (use seed-data.sql for full demo data)
