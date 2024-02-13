DROP DATABASE IF EXISTS `Inventory`;
CREATE DATABASE `Inventory`;
USE `Inventory`;






CREATE TABLE inventory (
  item_id INT NOT NULL,
  primary_name VARCHAR(50),
  amount INT,
  PRIMARY KEY (item_id)
);



INSERT INTO inventory (item_id, primary_name, amount)
VALUES
(1,'Stahl', 20),
(2,'Auto', 2),
(3,'ECU', 1);














