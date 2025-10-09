-- 1. Create the database
CREATE DATABASE IF NOT EXISTS StudentDB;

-- 2. Use the database
USE StudentDB;

-- 3. Create students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- 4. Create courses table
CREATE TABLE IF NOT EXISTS courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) UNIQUE NOT NULL,
    duration VARCHAR(50)
);

-- 5. Create enrollments table (no enrollment date needed)
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- 6. Insert sample courses
INSERT INTO courses (course_name, duration) VALUES
('Mathematics', '6 months'),
('Physics', '1 year'),
('Computer Science', '1 year'),
('English', '6 months');
