insert into user_status (status_id, status_name)
values (1, 'UNCONFIRMED'),
       (2, 'ACTIVE'),
       (3, 'INACTIVE'),
       (4, 'TERMINATED');

insert into user_role (role_id, role_name)
values (1, 'ROLE_NO_AUTH_CUSTOMER'),
       (2, 'ROLE_CUSTOMER'),
       (3, 'ROLE_COURIER'),
       (4, 'ROLE_PRODUCT_MGR'),
       (5, 'ROLE_ADMIN');

insert into user_permission (permission_id, permission_name)
values (1, 'USER_SELF_W'),
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

insert into permission_role (role_id, permission_id)
values (2, 1),
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

insert into auth_user (email, password, first_name, last_name, phone_number, role_id, status_id)
values ('netcracker_spring_2021_t4@yahoo.com', '$2a$10$JQor2Lkc5wAN4hWUQQq.wudA2kb/vXUsGevKkmD.xOmCyqLnM.UHm', 'King', 'Yaropolkovich', '123456789012', 5, 2);

insert into timeslot (timeslot_id, time_start, time_end)
values (1, '10:00:00', '11:00:00'),
       (2, '11:00:00', '12:00:00'),
       (3, '12:00:00', '13:00:00'),
       (4, '13:00:00', '14:00:00'),
       (5, '14:00:00', '15:00:00'),
       (6, '15:00:00', '16:00:00'),
       (7, '16:00:00', '17:00:00'),
       (8, '17:00:00', '18:00:00'),
       (9, '18:00:00', '19:00:00'),
       (10, '19:00:00', '20:00:00');

insert into order_status (status_id, status_name)
values (1, 'ACCEPTED'),
       (2, 'IN_PROGRESS'),
       (3, 'FAILED'),
       (4, 'CANCELED');

insert into product_category (category_id, product_category_name)
values (1, 'RED_WINE'),
       (2, 'ROSE_WINE'),
       (3, 'WHITE_WINE'),
       (4, 'DESSERT_WINE'),
       (5, 'BLUE_CHEESE'),
       (6, 'HARD_CHEESE'),
       (7, 'PASTA_FILATA_CHEESE'),
       (8, 'PROCESSED_CHEESE'),
       (9, 'SEMI_HARD_CHEESE'),
       (10, 'SEMI_SOFT_CHEESE'),
       (11, 'SOFT_FRESH_CHEESE'),
       (12, 'SOFT_RIPENED_CHEESE');

insert into auction_type (type_id, auction_type_name)
values (1, 'ASCENDING'),
       (2, 'DESCENDING');