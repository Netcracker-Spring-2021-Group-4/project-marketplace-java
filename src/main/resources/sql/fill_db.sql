INSERT INTO user_role (role_name) VALUES
('USER'),
('AUTH_USER'),
('COURIER'),
('PRODUCT_MANAGER'),
('ADMIN');

INSERT INTO permission (permission_name) VALUES
('user:self:w'),
('order:r'),
('dont_miss_to_buy:r'),
('bid:w'),
('deliveries:r'),
('deliveries:w'),
('user:r'),
('product:w'),
('product:r'),
('auction:w'),
('staff:w'),
('staff:r');

INSERT INTO permission_role (role_id, permission_id) VALUES
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(3, 1),
(3, 5),
(3, 6),
(3, 7),
(4, 1),
(4, 8),
(4, 9),
(4, 10),
(5, 8),
(5, 9),
(5, 10),
(5, 11),
(5, 12);

INSERT INTO auth_user (email, password, role_id) VALUES
('netcracker_spring_2021_t4@yahoo.com', '{bcrypt}$2a$10$0nSlqxDAYjETHwDc.CNIieVv1erp3Hk6BhvA633LRJZDicfmv23HC', 5);