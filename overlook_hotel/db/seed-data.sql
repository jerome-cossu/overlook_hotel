-- Seed data for minimal booking-only schema (PostgreSQL)
-- Replace bcrypt hashes with real hashes in your environment before use.

-- Roles
INSERT INTO roles (name, description, created_at) VALUES
  ('GUEST', 'Hotel guest / customer', now()),
  ('EMPLOYEE', 'Hotel staff', now()),
  ('MANAGER', 'Hotel manager / admin', now());

-- Demo users (passwords: Password123! hashed with bcrypt)
-- Replace the password_hash values with real bcrypt hashes generated in your environment.
INSERT INTO users (email, username, password_hash, first_name, last_name, role_id, created_at, updated_at)
VALUES
  ('manager@overlook.test','manager','$2y$12$REPLACE_WITH_BCRYPT_HASH_MANAGER','Manager','Overlook',
     (SELECT id FROM roles WHERE name='MANAGER'), now(), now()),
  ('employee@overlook.test','employee','$2y$12$REPLACE_WITH_BCRYPT_HASH_EMP','Employee','Overlook',
     (SELECT id FROM roles WHERE name='EMPLOYEE'), now(), now()),
  ('guest1@overlook.test','guest1','$2y$12$REPLACE_WITH_BCRYPT_HASH_G1','Guest','One',
     (SELECT id FROM roles WHERE name='GUEST'), now(), now()),
  ('guest2@overlook.test','guest2','$2y$12$REPLACE_WITH_BCRYPT_HASH_G2','Guest','Two',
     (SELECT id FROM roles WHERE name='GUEST'), now(), now());

-- Rooms
INSERT INTO rooms (room_number, room_type, capacity, base_price, description, floor_number, status, is_accessible, created_at, updated_at)
VALUES
  ('101','Standard',2,100.00,'Comfortable double room',1,'AVAILABLE',FALSE, now(), now()),
  ('102','Deluxe',2,150.00,'Comfortable double room with balcony',1,'AVAILABLE',FALSE, now(), now()),
  ('201','Suite',4,250.00,'Spacious suite with living area',2,'AVAILABLE',TRUE, now(), now());

-- Features (amenities)
INSERT INTO features (code, name, description, category, is_active, created_at, updated_at) VALUES
  ('wifi','Wi‑Fi','High-speed wireless internet','connectivity', TRUE, now(), now()),
  ('ensuite','En-suite Bathroom','Private bathroom attached to room','room_feature', TRUE, now(), now()),
  ('tv','Television','Flat-screen TV','entertainment', TRUE, now(), now()),
  ('balcony','Balcony','Private balcony','room_feature', TRUE, now(), now()),
  ('minibar','Minibar','In-room minibar','amenity', TRUE, now(), now()),
  ('sofa','Sofa / Seating','Additional seating area','furnishing', TRUE, now(), now());

-- Map rooms to features via room_features
-- Room 101: wifi, ensuite, tv
INSERT INTO room_features (room_id, feature_id, added_at, source)
VALUES
  ((SELECT id FROM rooms WHERE room_number='101'), (SELECT id FROM features WHERE code='wifi'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='101'), (SELECT id FROM features WHERE code='ensuite'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='101'), (SELECT id FROM features WHERE code='tv'), now(), 'seed');

-- Room 102: wifi, ensuite, tv, balcony
INSERT INTO room_features (room_id, feature_id, added_at, source)
VALUES
  ((SELECT id FROM rooms WHERE room_number='102'), (SELECT id FROM features WHERE code='wifi'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='102'), (SELECT id FROM features WHERE code='ensuite'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='102'), (SELECT id FROM features WHERE code='tv'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='102'), (SELECT id FROM features WHERE code='balcony'), now(), 'seed');

-- Room 201: wifi, ensuite, tv, minibar, sofa
INSERT INTO room_features (room_id, feature_id, added_at, source)
VALUES
  ((SELECT id FROM rooms WHERE room_number='201'), (SELECT id FROM features WHERE code='wifi'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='201'), (SELECT id FROM features WHERE code='ensuite'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='201'), (SELECT id FROM features WHERE code='tv'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='201'), (SELECT id FROM features WHERE code='minibar'), now(), 'seed'),
  ((SELECT id FROM rooms WHERE room_number='201'), (SELECT id FROM features WHERE code='sofa'), now(), 'seed');

-- Optional demo reservations
INSERT INTO reservations (user_id, room_id, lead_guest_name, lead_guest_phone, check_in_date, check_out_date, guests_count, total_price, status, created_at, updated_at)
VALUES
  ((SELECT id FROM users WHERE email='guest1@overlook.test'),
   (SELECT id FROM rooms WHERE room_number='101'),
   'Guest One', '555-0101', CURRENT_DATE + INTERVAL '7 days', CURRENT_DATE + INTERVAL '10 days', 2, 300.00, 'BOOKED', now(), now()),
  ((SELECT id FROM users WHERE email='guest2@overlook.test'),
   (SELECT id FROM rooms WHERE room_number='201'),
   'Guest Two', '555-0202', CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '33 days', 3, 750.00, 'BOOKED', now(), now());
