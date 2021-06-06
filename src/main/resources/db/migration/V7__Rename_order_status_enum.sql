update order_status
SET status_name='SUBMITTED'
WHERE status_id=1;

update order_status
SET status_name='IN_DELIVERY'
WHERE status_id=2;

update order_status
SET status_name='DELIVERED'
WHERE status_id=3;

update order_status
SET status_name='CANCELLED'
WHERE status_id=4;

INSERT into order_status (status_id, status_name) values (5, 'FAILED');
