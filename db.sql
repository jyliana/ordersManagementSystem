
CREATE database shop;

CREATE TABLE users (
    id SERIAL NOT NULL,
    name VARCHAR(50)
);

CREATE type order_status as enum('DELETED', 'VALID');

CREATE TABLE orders (
    id SERIAL NOT NULL,
    trade_date date NOT NULL,
    amount integer NOT NULL,
    status order_status NOT NULL
);

CREATE TABLE orders_history (
    id SERIAL NOT NULL,
    user_id integer NOT NULL,
    order_id integer NOT NULL
);

