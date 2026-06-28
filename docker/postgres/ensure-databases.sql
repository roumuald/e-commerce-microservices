SELECT 'CREATE DATABASE customer_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'customer_db')\gexec
SELECT 'CREATE DATABASE product_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'product_db')\gexec
SELECT 'CREATE DATABASE order_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'order_db')\gexec
SELECT 'CREATE DATABASE payment_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'payment_db')\gexec
SELECT 'CREATE DATABASE keycloak_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak_db')\gexec
