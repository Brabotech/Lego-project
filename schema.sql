CREATE DATABASE IF NOT EXISTS legoDB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE legoDB;

CREATE TABLE IF NOT EXISTS users (
    userID INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    address_nr VARCHAR(20) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    telefoonnr VARCHAR(30) NOT NULL,
    email_address VARCHAR(254) NOT NULL,
    PRIMARY KEY (userID)
);
