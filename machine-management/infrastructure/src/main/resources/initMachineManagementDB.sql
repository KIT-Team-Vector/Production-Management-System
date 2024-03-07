CREATE DATABASE IF NOT EXISTS machineManagementDB;
USE machineManagementDB;

CREATE TABLE IF NOT EXISTS one_to_one_machine
(
    id                   INT PRIMARY KEY,
    input_resource_id    INT,
    input_resource_name  VARCHAR(255),
    output_resource_id   INT,
    output_resource_name VARCHAR(255)
);

INSERT INTO one_to_one_machine (id, input_resource_id, input_resource_name, output_resource_id, output_resource_name)
VALUES (1, 1, 'steel', 2, 'wood'),
       (2, 1, 'steel', 3, 'copper'),
       (3, 2, 'wood', 4, 'paper'),
       (4, 3, 'copper', 5, 'plastic'),
       (5, 4, 'copper', 1, 'steel');