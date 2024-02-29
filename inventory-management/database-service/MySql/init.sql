CREATE DATABASE IF NOT EXISTS inventorydatabase;
USE inventorydatabase;

CREATE TABLE IF NOT EXISTS inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
	primary_name VARCHAR(50),
	amount INT
);
