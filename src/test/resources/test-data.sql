-- Удаление таблицы "newEmployee"
DROP TABLE IF EXISTS newEmployee CASCADE ;

-- Удаление таблицы "position"
DROP TABLE  IF EXISTS position CASCADE ;

-- Удаление таблицы "department"
DROP TABLE IF EXISTS departmen CASCADE ;

-- Создание таблицы "position"
CREATE TABLE IF NOT EXISTS position (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

-- Вставка данных в таблицу "position"
INSERT INTO position (id, name) VALUES (1, 'Software Developer');

-- Создание таблицы "department"
CREATE TABLE IF NOT EXISTS department (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

-- Вставка данных в таблицу "department"
INSERT INTO department (id, name) VALUES (1, 'IT');

-- Создание таблицы "newEmployee" с каскадным удалением
CREATE TABLE IF NOT EXISTS newEmployee (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             salary NUMERIC(10, 2),
                             position_id INT,
                             department_id INT,
                             CONSTRAINT fk_employee_position FOREIGN KEY (position_id) REFERENCES position (id) ON DELETE CASCADE,
                             CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES department (id) ON DELETE CASCADE
);

-- Вставка данных в таблицу "newEmployee"
INSERT INTO newEmployee (id, name, salary, position_id, department_id) VALUES (1, 'John Doe', 5000.0, 1, 1);