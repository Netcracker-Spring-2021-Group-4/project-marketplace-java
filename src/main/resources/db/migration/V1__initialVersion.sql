create EXTENSION IF NOT EXISTS "uuid-ossp";
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO nc_course;

create TABLE user_status
(
    status_id INTEGER PRIMARY KEY,
    status_name      VARCHAR(32) NOT NULL UNIQUE
);

create TABLE user_role
(
    role_id   INTEGER PRIMARY KEY,
    role_name VARCHAR(32) NOT NULL UNIQUE
);

create TABLE user_permission
(
    permission_id   INTEGER PRIMARY KEY,
    permission_name VARCHAR(32) NOT NULL UNIQUE
);

create TABLE permission_role
(
    permission_id INTEGER,
    role_id       INTEGER,
    CONSTRAINT permission_role_pk PRIMARY KEY (permission_id, role_id),
    CONSTRAINT permission_role_role_id_fk
        FOREIGN KEY (role_id) REFERENCES user_role
            ON update RESTRICT ON delete RESTRICT,
    CONSTRAINT permission_role_permission_id_fk
        FOREIGN KEY (permission_id) REFERENCES user_permission
            ON update RESTRICT ON delete RESTRICT
);

create TABLE auth_user
(
    user_id      uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    email        VARCHAR(128) NOT NULL UNIQUE,
    password     VARCHAR(96)  NOT NULL,
    first_name   VARCHAR(32)  NOT NULL,
    last_name    VARCHAR(32)  NOT NULL,
    phone_number CHAR(16)     NULL,
    role_id      INTEGER      NOT NULL,
    status_id    INTEGER      NOT NULL,
    CONSTRAINT user_role_fk FOREIGN KEY (role_id) REFERENCES user_role (role_id)
        ON update CASCADE ON delete RESTRICT,
    CONSTRAINT user_status_fk FOREIGN KEY (status_id) REFERENCES user_status (status_id)
        ON update CASCADE ON delete RESTRICT
);

create INDEX idx_auth_user_email
    ON auth_user (email);

create TABLE confirmation_token
(
    token             uuid PRIMARY KEY,
    expired_at        TIMESTAMPTZ           NOT NULL,
    is_patch_password BOOLEAN DEFAULT FALSE NOT NULL,
    is_activated      BOOLEAN DEFAULT FALSE NOT NULL,
    user_email        VARCHAR(128)          NOT NULL,
    CONSTRAINT token_email_check FOREIGN KEY (user_email) REFERENCES auth_user (email)
        ON update RESTRICT ON delete CASCADE
);

create TABLE address
(
    address_id  uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    city        VARCHAR(64) NOT NULL,
    street      VARCHAR(64) NOT NULL,
    building    VARCHAR(8)  NOT NULL,
    flat        INTEGER     NULL,
    customer_id uuid        NULL,
    CONSTRAINT address_user_fk FOREIGN KEY (customer_id) REFERENCES auth_user (user_id)
        ON update CASCADE ON delete CASCADE
);

create TABLE timeslot
(
    timeslot_id INTEGER PRIMARY KEY,
    time_start  TIME NOT NULL UNIQUE,
    time_end    TIME NOT NULL UNIQUE
);

create TABLE order_status
(
    status_id INTEGER PRIMARY KEY,
    status_name      VARCHAR(32) NOT NULL UNIQUE
);

create TABLE prod_order
(
    order_id     uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    placed_at    timestamptz NOT NULL,
    phone_number CHAR(16)    NOT NULL,
    comment      TEXT        NULL,
    status_id    INTEGER     NOT NULL,
    address_id   uuid        NOT NULL,
    customer_id  uuid        NULL,
    CONSTRAINT order_status_fk FOREIGN KEY (status_id) REFERENCES order_status (status_id)
        ON update RESTRICT ON delete RESTRICT,
    CONSTRAINT order_address_fk FOREIGN KEY (address_id) REFERENCES address (address_id)
        ON update RESTRICT ON delete RESTRICT,
    CONSTRAINT order_customer_fk FOREIGN KEY (customer_id) REFERENCES auth_user (user_id)
        ON update RESTRICT ON delete SET NULL
);

create TABLE dateslot
(
    dateslot_id uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    datestamp   DATE    NOT NULL,
    timeslot_id INTEGER NOT NULL,
    courier_id  uuid    NOT NULL,
    order_id    uuid    NOT NULL,
    CONSTRAINT dateslot_timslot_fk FOREIGN KEY (timeslot_id) REFERENCES timeslot (timeslot_id)
        ON update RESTRICT ON delete RESTRICT,
    CONSTRAINT dateslot_courier_fk FOREIGN KEY (courier_id) REFERENCES auth_user (user_id)
        ON update RESTRICT ON delete CASCADE,
    CONSTRAINT dateslot_order_fk FOREIGN KEY (order_id) REFERENCES prod_order (order_id)
        ON update RESTRICT ON delete CASCADE
);

create TABLE product_category
(
    category_id INTEGER PRIMARY KEY,
    product_category_name        VARCHAR(64) NOT NULL UNIQUE
);

create TABLE product
(
    product_id        uuid    DEFAULT uuid_generate_v1() PRIMARY KEY,
    product_name      VARCHAR(32)          NOT NULL,
    image_url         VARCHAR(128)         NOT NULL,
    description       TEXT                 NULL,
    price             money                NOT NULL,
    in_stock          INTEGER              NOT NULL,
    reserved          INTEGER              NOT NULL,
    availability_date DATE                 NOT NULL,
    is_active         BOOLEAN DEFAULT TRUE NOT NULL,
    category_id       INTEGER              NOT NULL,
    CONSTRAINT product_category_fk FOREIGN KEY (category_id) REFERENCES product_category (category_id)
        ON update RESTRICT ON delete RESTRICT
);

create TABLE order_item
(
    order_item_id uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    quantity      INTEGER NOT NULL,
    price         money   NOT NULL,
    order_id      uuid    NOT NULL,
    product_id    uuid    NOT NULL,
    CONSTRAINT order_item_order_fk FOREIGN KEY (order_id) REFERENCES prod_order (order_id)
        ON update RESTRICT ON delete CASCADE,
    CONSTRAINT order_item_product_fk FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON update RESTRICT ON delete CASCADE
);

create TABLE cart_item
(
    cart_item_id uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    quantity     INTEGER NOT NULL,
    date_added   DATE    NOT NULL,
    customer_id  uuid    NOT NULL,
    product_id   uuid    NOT NULL,
    CONSTRAINT cart_item_auth_user_fk FOREIGN KEY (customer_id) REFERENCES auth_user (user_id)
        ON update RESTRICT ON delete CASCADE,
    CONSTRAINT cart_item_product_fk FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON update RESTRICT ON delete RESTRICT
);

create TABLE discount
(
    discount_id   uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    offered_price money       NOT NULL,
    starts_at     timestamptz NOT NULL,
    ends_at       timestamptz NOT NULL,
    product_id    uuid        NOT NULL REFERENCES product (product_id)
        ON update RESTRICT ON delete CASCADE
);

create TABLE auction_type
(
    type_id INTEGER PRIMARY KEY,
    auction_type_name    VARCHAR(32) NOT NULL UNIQUE
);

create TABLE auction
(
    auction_id       uuid    DEFAULT uuid_generate_v1() PRIMARY KEY,
    stars_at         timestamptz          NOT NULL,
    start_price      money                NOT NULL,
    product_quantity INTEGER              NOT NULL,
    product_id       uuid                 NOT NULL,
    json_details     JSON                 NOT NULL,
    is_active        BOOLEAN DEFAULT TRUE NOT NULL, -- is not finished yet
    type_id          INTEGER              NOT NULL,
    CONSTRAINT auction_product_fk FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON update RESTRICT ON delete RESTRICT,
    CONSTRAINT auction_auction_type_fk FOREIGN KEY (type_id) REFERENCES auction_type (type_id)
        ON update RESTRICT ON delete RESTRICT
);

create TABLE bid
(
    bid_id      uuid DEFAULT uuid_generate_v1() PRIMARY KEY,
    made_at     timestamptz NOT NULL,
    price       money       NOT NULL,
    auction_id  uuid        NOT NULL,
    customer_id uuid        NOT NULL,
    CONSTRAINT bid_auth_user_fk FOREIGN KEY (customer_id) REFERENCES auth_user (user_id)
        ON update RESTRICT ON delete RESTRICT,
    CONSTRAINT bid_auction_fk FOREIGN KEY (auction_id) REFERENCES auction (auction_id)
        ON update RESTRICT ON delete CASCADE
);