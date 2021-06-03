CREATE PROCEDURE add_product_reservation(_lock_id bigint, _add_quantity int, _product_id uuid)
LANGUAGE plpgsql
AS $$
DECLARE
_newReserved int;
_reserved int;
_in_stock int;
BEGIN
PERFORM pg_advisory_xact_lock(_lock_id);
SELECT reserved, in_stock
INTO _reserved, _in_stock
FROM product
WHERE product_id = _product_id;

SELECT _add_quantity + _reserved INTO _newReserved;

IF _newReserved <= _in_stock THEN
UPDATE product SET reserved = _newReserved WHERE product_id = _product_id;
ELSE
        RAISE EXCEPTION 'There is not so much in stock --> only %', _in_stock
        USING HINT = 'Try reserving less';
END IF;
END;
$$;


CREATE PROCEDURE delete_product_reservation(_lock_id bigint, _remove_quantity int, _product_id uuid)
LANGUAGE plpgsql
AS $$
DECLARE
_newReserved int;
_reserved int;
_in_stock int;
BEGIN
PERFORM pg_advisory_xact_lock(_lock_id);
SELECT reserved, in_stock
INTO _reserved, _in_stock
FROM product
WHERE product_id = _product_id;

SELECT _reserved - _remove_quantity INTO _newReserved;

IF _newReserved >= 0 THEN
UPDATE product SET reserved = _newReserved WHERE product_id = _product_id;
ELSE
        RAISE EXCEPTION 'Cannot set reserved to negative'
        USING HINT = 'Change the quantity to remove';
END IF;
END;
$$;