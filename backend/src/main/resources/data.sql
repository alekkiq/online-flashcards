-- seed subjects
INSERT IGNORE INTO subjects (name) VALUES ('Mathematics');
INSERT IGNORE INTO subjects (name) VALUES ('Science');
INSERT IGNORE INTO subjects (name) VALUES ('History');
INSERT IGNORE INTO subjects (name) VALUES ('Literature');
INSERT IGNORE INTO subjects (name) VALUES ('Programming');
INSERT IGNORE INTO subjects (name) VALUES ('Others');

-- teacher FOR DEMO ONLY REMOVE LATER!!!!!!!!!!!!
-- password is just "password" hashed
INSERT IGNORE INTO users (username, email, password, role) VALUES ('DemoTeacher', 'demo@teacher.com', '$2a$12$T1.Zk/BCrNCt3/8zliv9m.xEgEwEgV0yMjQMpPYkpK1Dky1Rvanve', 'TEACHER');