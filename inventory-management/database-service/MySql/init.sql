-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS inventorydatabase;

-- Use the inventorydatabase
USE inventorydatabase;

-- Create the table (unconditionally)
CREATE TABLE IF NOT EXISTS inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    primary_name VARCHAR(50),
    amount INT
);

-- Insert tuples into the table (ignoring duplicates)
INSERT IGNORE INTO inventory (id, primary_name, amount)
VALUES 
    (1, 'steel', 10),
    (2, 'wood', 10),
    (3, 'paper', 10),
    (4, 'copper', 10),
    (5, 'plastic', 10);