CREATE TABLE users (
    user_id SERIAL PRIMARY KEY UNIQUE NOT NULL,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);