-- Вставка данных в таблицу "position"
INSERT INTO position (id, name) VALUES (1, 'Software Developer');

-- Вставка данных в таблицу "department"
INSERT INTO department (id, name) VALUES (1, 'IT');

-- Вставка данных в таблицу "new_employee"
INSERT INTO new_employee (id, name, salary, position_id, department_id) VALUES (1, 'John Doe', 5000.0, 1, 1);
