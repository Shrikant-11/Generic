-- Create the user_db database
CREATE DATABASE user_db;

-- Connect to auth_db (default database)
\c auth_db;

-- Create any necessary tables or initial data for auth_db
-- (This will be handled by Spring Boot's JPA/Hibernate)

-- Connect to user_db
\c user_db;

-- Create any necessary tables or initial data for user_db
-- (This will be handled by Spring Boot's JPA/Hibernate)
