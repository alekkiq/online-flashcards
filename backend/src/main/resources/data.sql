-- seed subjects
-- English
INSERT IGNORE INTO subjects (code, name, language) VALUES ('math', 'Mathematics', 'en');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('science', 'Science', 'en');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('history', 'History', 'en');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('literature', 'Literature', 'en');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('programming', 'Programming', 'en');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('other', 'Others', 'en');

-- Finnish
INSERT IGNORE INTO subjects (code, name, language) VALUES ('math', 'Matematiikka', 'fi');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('science', 'Luonnontieteet', 'fi');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('history', 'Historia', 'fi');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('literature', 'Kirjallisuus', 'fi');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('programming', 'Ohjelmointi', 'fi');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('other', 'Muut', 'fi');

-- Persian
INSERT IGNORE INTO subjects (code, name, language) VALUES ('math', 'ریاضیات', 'fa');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('science', 'علوم', 'fa');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('history', 'تاریخ', 'fa');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('literature', 'ادبیات', 'fa');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('programming', 'برنامه‌نویسی', 'fa');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('other', 'سایر', 'fa');

-- Chinese
INSERT IGNORE INTO subjects (code, name, language) VALUES ('math', '数学', 'zh');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('science', '科学', 'zh');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('history', '历史', 'zh');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('literature', '文学', 'zh');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('programming', '编程', 'zh');
INSERT IGNORE INTO subjects (code, name, language) VALUES ('other', '其他', 'zh');

-- teacher FOR DEMO ONLY REMOVE LATER!!!!!!!!!!!!
-- password is just "password" hashed
INSERT IGNORE INTO users (username, email, password, role) VALUES ('DemoTeacher', 'demo@teacher.com', '$2a$12$T1.Zk/BCrNCt3/8zliv9m.xEgEwEgV0yMjQMpPYkpK1Dky1Rvanve', 'TEACHER');