
-- with jwt-1
-- =========================
-- USERS (with password + role)
-- =========================

INSERT INTO users (name, email, password, role)
SELECT 'Alice', 'alice@example.com',
       '$2a$10$YdCQ/614ns60lXDfXx8ANew5KAfub71Z16k2lwske/PF9lL1MhLxS',
       'USER'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'alice@example.com'
);

INSERT INTO users (name, email, password, role)
SELECT 'Bob', 'bob@example.com',
       '$2a$10$YdCQ/614ns60lXDfXx8ANew5KAfub71Z16k2lwske/PF9lL1MhLxS',
       'USER'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'bob@example.com'
);

INSERT INTO users (name, email, password, role)
SELECT 'Charlie', 'charlie@example.com',
       '$2a$10$YdCQ/614ns60lXDfXx8ANew5KAfub71Z16k2lwske/PF9lL1MhLxS',
       'USER'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'charlie@example.com'
);

-- =========================
-- ADMIN USER
-- =========================

INSERT INTO users (name, email, password, role)
SELECT 'Admin', 'admin@example.com',
       '$2a$10$ypLJwxP9mgrocbK4E/34COnLiZQqPH7BOz1y0z4yDlj60uCZuyc4W',
       'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@example.com'
);



-- =========================
-- ORDERS
-- =========================

-- Alice -> Keyboard
INSERT INTO orders (product_name, user_id)
SELECT 'Keyboard', u.id
FROM users u
WHERE u.email = 'alice@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.product_name = 'Keyboard'
      AND o.user_id = u.id
);


-- Alice -> Mouse
INSERT INTO orders (product_name, user_id)
SELECT 'Mouse', u.id
FROM users u
WHERE u.email = 'alice@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.product_name = 'Mouse'
      AND o.user_id = u.id
);


-- Bob -> Laptop
INSERT INTO orders (product_name, user_id)
SELECT 'Laptop', u.id
FROM users u
WHERE u.email = 'bob@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.product_name = 'Laptop'
      AND o.user_id = u.id
);


-- Bob -> Monitor
INSERT INTO orders (product_name, user_id)
SELECT 'Monitor', u.id
FROM users u
WHERE u.email = 'bob@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.product_name = 'Monitor'
      AND o.user_id = u.id
);


-- Charlie -> Headphones
INSERT INTO orders (product_name, user_id)
SELECT 'Headphones', u.id
FROM users u
WHERE u.email = 'charlie@example.com'
  AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.product_name = 'Headphones'
      AND o.user_id = u.id
);



-- -- =========================
--     without jwt
-- -- USERS
-- -- =========================
--
-- INSERT INTO users (name, email)
-- SELECT 'Alice', 'alice@example.com'
--     WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE email = 'alice@example.com'
-- );
--
-- INSERT INTO users (name, email)
-- SELECT 'Bob', 'bob@example.com'
--     WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE email = 'bob@example.com'
-- );
--
-- INSERT INTO users (name, email)
-- SELECT 'Charlie', 'charlie@example.com'
--     WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE email = 'charlie@example.com'
-- );
--
--
-- -- =========================
-- -- ORDERS
-- -- =========================
--
-- -- Alice -> Keyboard
-- INSERT INTO orders (product_name, user_id)
-- SELECT 'Keyboard', u.id
-- FROM users u
-- WHERE u.email = 'alice@example.com'
--   AND NOT EXISTS (
--     SELECT 1
--     FROM orders o
--     WHERE o.product_name = 'Keyboard'
--       AND o.user_id = u.id
-- );
--
--
-- -- Alice -> Mouse
-- INSERT INTO orders (product_name, user_id)
-- SELECT 'Mouse', u.id
-- FROM users u
-- WHERE u.email = 'alice@example.com'
--   AND NOT EXISTS (
--     SELECT 1
--     FROM orders o
--     WHERE o.product_name = 'Mouse'
--       AND o.user_id = u.id
-- );
--
--
-- -- Bob -> Laptop
-- INSERT INTO orders (product_name, user_id)
-- SELECT 'Laptop', u.id
-- FROM users u
-- WHERE u.email = 'bob@example.com'
--   AND NOT EXISTS (
--     SELECT 1
--     FROM orders o
--     WHERE o.product_name = 'Laptop'
--       AND o.user_id = u.id
-- );
--
--
-- -- Bob -> Monitor
-- INSERT INTO orders (product_name, user_id)
-- SELECT 'Monitor', u.id
-- FROM users u
-- WHERE u.email = 'bob@example.com'
--   AND NOT EXISTS (
--     SELECT 1
--     FROM orders o
--     WHERE o.product_name = 'Monitor'
--       AND o.user_id = u.id
-- );
--
--
-- -- Charlie -> Headphones
-- INSERT INTO orders (product_name, user_id)
-- SELECT 'Headphones', u.id
-- FROM users u
-- WHERE u.email = 'charlie@example.com'
--   AND NOT EXISTS (
--     SELECT 1
--     FROM orders o
--     WHERE o.product_name = 'Headphones'
--       AND o.user_id = u.id
-- );