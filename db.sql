CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE type order_status as enum('DELETED', 'VALID');

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    trade_date DATE NOT NULL,
    amount INTEGER NOT NULL,
    status order_status NOT NULL
);

CREATE TABLE orders_history (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    order_id INTEGER NOT NULL REFERENCES orders(id)
);

CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR not null
);

CREATE TABLE categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL
);

CREATE TABLE product_categories (
  id SERIAL PRIMARY KEY,
  product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE,
  category_id INTEGER NOT NULL REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE order_details (
  id SERIAL PRIMARY KEY,
  order_id INTEGER NOT NULL REFERENCES orders(id),
  product_id INTEGER NOT NULL REFERENCES products(id),
  amount INTEGER NOT NULL
);