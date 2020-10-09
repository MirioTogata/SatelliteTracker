DROP DATABASE IF EXISTS `satellite_tracker`;
CREATE DATABASE `satellite_tracker`;

USE `satellite_tracker`;

CREATE TABLE `accounts`(
	`id` INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(256) NOT NULL UNIQUE,
    `password_hash` VARCHAR(64) NOT NULL
);

CREATE TABLE `satellites`(
	`id` INT NOT NULL UNIQUE PRIMARY KEY,
    `name` VARCHAR(256) NOT NULL
);

CREATE TABLE `favourites`(
	`account_id` INT NOT NULL REFERENCES `accounts`(`id`),
    `satellite_id` INT NOT NULL REFERENCES `satellites`(`id`)
);

SELECT * FROM `accounts`;