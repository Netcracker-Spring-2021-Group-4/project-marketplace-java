alter table cart_item
alter COLUMN date_added TYPE timestamptz;

alter table cart_item
    rename COLUMN date_added TO timestamp_added;