USE ecommerce_lp2_prod;

-- 👤 CREAR USUARIO ADMIN AUTOMÁTICO
INSERT INTO usuario (
    username,
    first_name,
    email,
    cellphone,
    password,
    date_created,
    type_user
) VALUES (
    'admin@gmail.com',
    'admin',
    'admin@gmail.com',
    '901521580',
    '$2a$10$xLFtBIXGtYvAbRqM95JhYeuNd/h6q5r6mhknU9t.ChkmY8b0F.Q0K',
    NOW(),
    'ADMIN'
);