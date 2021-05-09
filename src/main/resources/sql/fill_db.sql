insert into user_role (role_id, role_name) values
(1, 'ROLE_NO_AUTH_CUSTOMER'),
(2, 'ROLE_CUSTOMER'),
(3, 'ROLE_COURIER'),
(4, 'ROLE_PRODUCT_MGR'),
(5, 'ROLE_ADMIN');

insert into permission (permission_id, permission_name) values
(1, 'USER_SELF_W'),
(2, 'ORDER_R'),
(3, 'DONT_MISS_TO_BUY_R'),
(4, 'BID_W'),
(5, 'DELIVERIES_R'),
(6, 'DELIVERIES_W'),
(7, 'USER_R'),
(8, 'PRODUCT_W'),
(9, 'PRODUCT_R'),
(10, 'AUCTION_W'),
(11, 'STAFF_W'),
(12, 'STAFF_R');

insert into user_status (status_name) values
('UNCONFIRMED'),
('ACTIVE'),
('INACTIVE'),
('TERMINATED');

insert into permission_role (role_id, permission_id) values
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



-- password for development
insert into auth_user (email, password, role_id, status_id, first_name, last_name) values
('netcracker_spring_2021_t4@yahoo.com', '{bcrypt}$2a$10$0nSlqxDAYjETHwDc.CNIieVv1erp3Hk6BhvA633LRJZDicfmv23HC', 5, 2,
'King', 'Vanovich');