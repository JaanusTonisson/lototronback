-- roles
INSERT INTO lototron.role (id, name) VALUES (default, 'admin');
INSERT INTO lototron.role (id, name) VALUES (default, 'user');
-- base users
INSERT INTO lototron."user" (id, role_id, username, password, status) VALUES (default, 1, 'Kätlin', '123', 'A');
INSERT INTO lototron."user" (id, role_id, username, password, status) VALUES (default, 1, 'Jaanus', '123', 'A');
INSERT INTO lototron."user" (id, role_id, username, password, status) VALUES (default, 1, 'Stella', '123', 'A');
--base profiles
INSERT INTO lototron.profile (id, user_id, phone_number, first_name, last_name) VALUES (default, 3, '51234123', 'Stella', 'Sterilaid');
INSERT INTO lototron.profile (id, user_id, phone_number, first_name, last_name) VALUES (default, 2, '51515125', 'Jaanus', 'Jarilaid');
INSERT INTO lototron.profile (id, user_id, phone_number, first_name, last_name) VALUES (default, 1, '51515151', 'Kätlin', 'Kärilaid');

-- base restaurants
INSERT INTO lototron.restaurant (id, name, address) VALUES (default, 'Guru Restoran', 'Aia tänav 10a');
INSERT INTO lototron.restaurant (id, name, address) VALUES (default, 'Beer Garden', 'Inseneri tänav 1');
INSERT INTO lototron.restaurant (id, name, address) VALUES (default, 'Tallinn Pizza & Kebab', 'Aia tänav 10');
INSERT INTO lototron.restaurant (id, name, address) VALUES (default, 'Hesburger Viru', 'Viru 27a');
INSERT INTO lototron.restaurant (id, name, address) VALUES (default, 'Kanuti Ramen Bar', 'Aia tänav 10a-1');
INSERT INTO lototron.restaurant (id, name, address) VALUES (default, 'Restaurant Tai Boh', 'Viru 23');
-- base service
INSERT INTO lototron.dining_service (id, name) VALUES (default, 'Hommikusöök');
INSERT INTO lototron.dining_service (id, name) VALUES (default, 'Lõunapakkumine');
INSERT INTO lototron.dining_service (id, name) VALUES (default, 'À la carte');
INSERT INTO lototron.dining_service (id, name) VALUES (default, 'Kiirtoit');
INSERT INTO lototron.dining_service (id, name) VALUES (default, '24H');
-- base restaurant dining service
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 1, 2);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 1, 3);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 2, 2);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 2, 3);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 3, 4);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 3, 5);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 4, 1);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 4, 4);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 5, 2);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 5, 3);
INSERT INTO lototron.restaurant_dining_service (id, restaurant_id, dining_service_id) VALUES (default, 6, 3);
--base lunch events

INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 1, 1, 4, 3, '2025-03-07', '12:30:00', 'D', false);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 2, 2, 5, 4, '2025-03-06', '12:30:00', 'D', false);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 3, 3, 6, 5, '2025-03-05', '12:30:00', 'D', false);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 1, 4, 4, 3, '2025-03-14', '14:00:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 2, 5, 5, 4, '2025-03-13', '14:00:00', 'D', false);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 3, 6, 6, 5, '2025-03-12', '14:00:00', 'D', false);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 1, 1, 4, 3, '2025-03-21', '16:00:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 2, 2, 5, 4, '2025-03-20', '16:00:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 3, 3, 6, 5, '2025-03-19', '16:00:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 1, 4, 4, 3, '2025-03-27', '12:30:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 2, 6, 5, 4, '2025-03-28', '12:30:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 3, 1, 6, 5, '2025-03-26', '12:30:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 1, 2, 4, 3, '2025-03-19', '14:00:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 2, 3, 5, 4, '2025-03-26', '14:00:00', 'A', true);
INSERT INTO lototron.lunch_event (id, user_id, restaurant_id, pax_total, pax_available, date, time, status, is_available) VALUES (default, 3, 4, 6, 5, '2025-03-24', '14:00:00', 'A', true);

