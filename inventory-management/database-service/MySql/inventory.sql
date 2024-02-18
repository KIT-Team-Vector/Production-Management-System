DROP DATABASE IF EXISTS `Inventory`;
CREATE DATABASE `Inventory`;
USE `Inventory`;






CREATE TABLE inventory (
  id INT AUTO_INCREMENT PRIMARY KEY,
  primary_name VARCHAR(50),
  amount INT
);



INSERT INTO inventory (id, primary_name, amount)
VALUES
(1,'Stahl', 20),
(2,'Auto', 2),
(3,'ECU', 1);














