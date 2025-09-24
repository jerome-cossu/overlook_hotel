-- Overlook Hotel schema.sql - (Minimal booking-only schema)
-- PostgreSQL
-- Last updated: 2025-09-23

CREATE TABLE roles (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT,
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email TEXT NOT NULL UNIQUE,
  username TEXT,
  password_hash TEXT NOT NULL,
  first_name TEXT,
  last_name TEXT,
  phone_number TEXT,
  date_of_birth DATE,
  profile_photo_url TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  role_id BIGINT REFERENCES roles(id),
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now(),
  last_login TIMESTAMPTZ,
  version INTEGER DEFAULT 1
);

CREATE TABLE rooms (
  id BIGSERIAL PRIMARY KEY,
  room_number TEXT NOT NULL UNIQUE,
  room_type TEXT,
  capacity INTEGER DEFAULT 1,
  base_price NUMERIC(10,2) DEFAULT 0,
  description TEXT,
  floor_number INTEGER,
  status TEXT DEFAULT 'available',
  is_accessible BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now(),
  version INTEGER DEFAULT 1
);

CREATE TABLE features (
  id BIGSERIAL PRIMARY KEY,
  code TEXT UNIQUE,
  name TEXT NOT NULL,
  description TEXT,
  icon_url TEXT,
  category TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE room_features (
  id BIGSERIAL PRIMARY KEY,
  room_id BIGINT NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  feature_id BIGINT NOT NULL REFERENCES features(id) ON DELETE CASCADE,
  added_at TIMESTAMPTZ DEFAULT now(),
  source TEXT,
  UNIQUE (room_id, feature_id)
);

CREATE TABLE reservations (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE SET NULL,
  room_id BIGINT NOT NULL REFERENCES rooms(id) ON DELETE RESTRICT,
  lead_guest_name TEXT,
  lead_guest_phone TEXT,
  check_in_date DATE NOT NULL,
  check_out_date DATE NOT NULL,
  guests_count INTEGER DEFAULT 1,
  total_price NUMERIC(10,2) DEFAULT 0,
  status TEXT DEFAULT 'booked',
  special_requests TEXT,
  cancellation_policy TEXT,
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now(),
  version INTEGER DEFAULT 1,
  CONSTRAINT chk_dates CHECK (check_out_date > check_in_date)
);

-- Helpful indexes
CREATE INDEX idx_reservations_room_dates ON reservations(room_id, check_in_date, check_out_date);
CREATE INDEX idx_room_features_room_id ON room_features(room_id);
CREATE INDEX idx_room_features_feature_id ON room_features(feature_id);
CREATE INDEX idx_users_email ON users(email);

-- Simple trigger to bump version and updated_at on update 
-- for optimistic locking (prevent double booking) and audit
-- Applies to users, rooms, reservations
-- Note: version starts at 1, increments by 1 on each update
CREATE OR REPLACE FUNCTION bump_version_and_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.version := COALESCE(OLD.version, 0) + 1;
  NEW.updated_at := now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bump_users BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION bump_version_and_timestamp();
CREATE TRIGGER trg_bump_rooms BEFORE UPDATE ON rooms
FOR EACH ROW EXECUTE FUNCTION bump_version_and_timestamp();
CREATE TRIGGER trg_bump_reservations BEFORE UPDATE ON reservations
FOR EACH ROW EXECUTE FUNCTION bump_version_and_timestamp();
