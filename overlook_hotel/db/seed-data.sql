-- Roles
INSERT INTO roles (name, description) VALUES
  ('GUEST', 'Hotel guest / customer'),
  ('EMPLOYEE', 'Hotel staff'),
  ('MANAGER', 'Hotel manager / admin');

-- Demo users (passwords: Password123! hashed with bcrypt)
-- Replace the password hash values with real bcrypt hashes generated in your environment.
INSERT INTO users (email, username, password_hash, first_name, last_name, role_id)
VALUES
  ('manager@overlook.test','manager','$2y$12$REPLACE_WITH_BCRYPT_HASH_MANAGER','Manager','Overlook', (SELECT id FROM roles WHERE name='MANAGER')),
  ('employee@overlook.test','employee','$2y$12$REPLACE_WITH_BCRYPT_HASH_EMP','Employee','Overlook', (SELECT id FROM roles WHERE name='EMPLOYEE')),
  ('guest1@overlook.test','guest1','$2y$12$REPLACE_WITH_BCRYPT_HASH_G1','Guest','One', (SELECT id FROM roles WHERE name='GUEST')),
  ('guest2@overlook.test','guest2','$2y$12$REPLACE_WITH_BCRYPT_HASH_G2','Guest','Two', (SELECT id FROM roles WHERE name='GUEST'));

-- Rooms
INSERT INTO rooms (room_number, room_type, capacity, base_price, description, floor_number, amenities, status, is_accessible)
VALUES
  ('101','Standard',2,100.00,'Comfortable double room',1,ARRAY['wifi','ensuite','tv'],'AVAILABLE',false),
  ('102','Delux',2,150.00,'Comfortable double room with balcony',1,ARRAY['wifi','ensuite','tv','balcony'],'AVAILABLE',false),
  ('201','Suite',4,250.00,'Spacious suite with living area',2,ARRAY['wifi','ensuite','tv','minibar','sofa'],'AVAILABLE',true);

-- Loyalty records for guests
INSERT INTO loyalty_points (user_id, total_points, lifetime_points, tier)
VALUES
  ((SELECT id FROM users WHERE email='guest1@overlook.test'), 100, 100, 'SILVER'),
  ((SELECT id FROM users WHERE email='guest2@overlook.test'), 0, 0, NULL);

-- Sample reservation (past)
INSERT INTO reservations (user_id, room_id, lead_guest_name, lead_guest_phone, check_in_date, check_out_date, guests_count, total_price, status)
VALUES
  (
    (SELECT id FROM users WHERE email='guest1@overlook.test'),
    (SELECT id FROM rooms WHERE room_number='101'),
    'Guest One',
    '+331234567890',
    CURRENT_DATE - INTERVAL '10 days',
    CURRENT_DATE - INTERVAL '7 days',
    1,
    300.00,
    'CHECKED_OUT'
  );

-- Sample feedback linked to that reservation
INSERT INTO guest_feedback (reservation_id, user_id, rating, feedback_text, categories, anonymous)
VALUES
  (
    (SELECT id FROM reservations WHERE user_id=(SELECT id FROM users WHERE email='guest1@overlook.test') ORDER BY id LIMIT 1),
    (SELECT id FROM users WHERE email='guest1@overlook.test'),
    5,
    'Wonderful stay, very clean and friendly staff.',
    ARRAY['cleanliness','staff'],
    FALSE
  );
