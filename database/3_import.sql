-- roles
INSERT INTO lototron.role (id, name) VALUES (default, 'admin');
INSERT INTO lototron.role (id, name) VALUES (default, 'user');
-- base users
INSERT INTO lototron."user" (id, role_id, username, password, status) VALUES (default, 1, 'Kätlin', '123', 'A');
INSERT INTO lototron."user" (id, role_id, username, password, status) VALUES (default, 1, 'Jaanus', '123', 'A');
INSERT INTO lototron."user" (id, role_id, username, password, status) VALUES (default, 1, 'Stella', '123', 'A');
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
