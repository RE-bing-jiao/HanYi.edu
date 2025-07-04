insert into categories (name,
                        description) values
                                         ('Beginner', 'Courses for absolute beginners'),
                                         ('Intermediate', 'For learners with basic knowledge'),
                                         ('Advanced', 'Advanced level courses'),
                                         ('HSK 1', '汉语水平考试一级课程 - HSK Level 1 courses'),
                                         ('HSK 2', '汉语水平考试二级课程 - HSK Level 2 courses');

insert into users (username,
                   password,
                   email,
                   role) values
                             ('admin', 'p@ssw0rdD1234', 'admin@chinese.com', 'ADMIN'),
                             ('teacher1', 'p@ssw0rdD1234', 'teacher1@chinese.com', 'TEACHER'),
                             ('student1', 'p@ssw0rdD1234', 'student1@chinese.com', 'STUDENT'),
                             ('eva_li', 'p@ssw0rdD1234', 'evali@mail.com', 'STUDENT');

insert into courses (header,
                     description,
                     category_id,
                     entry_date,
                     exit_date,
                     progress) values
                                   ('Basic Chinese', 'Introduction to Mandarin Chinese', 1, '2027-09-01 00:00:00', '2027-12-31 23:59:59', 25.50),
                                   ('Daily Conversation', 'Common phrases for daily life', 2, '2027-09-01 00:00:00', '2027-02-28 23:59:59', 10.00),
                                   ('Business Chinese', 'Chinese for business communication', 3, '2027-10-01 00:00:00', '2027-03-31 23:59:59', 0.00),
                                   ('HSK 1 Preparation', 'HSK 1 考试准备课程', 4, '2027-09-15 00:00:00', '2027-12-15 23:59:59', 5.00);

insert into lessons (lesson_order_num,
                     header,
                     description,
                     url,
                     course_id) values
                                    (1, 'Greetings', 'Basic greetings in Chinese', 'https://hanyi.com/lessons/1', 1),
                                    (1, 'Shopping', 'Essential shopping phrases', 'https://hanyi.com/lessons/3', 2),
                                    (1, 'Introduction', '自我介绍 - Self introduction', 'https://hanyi.com/lessons/4', 4),
                                    (2, 'Family', '家庭成员 - Family members', 'https://hanyi.com/lessons/5', 4);

insert into entries (user_id,
                     course_id,
                     entry_date) values
                                     (3, 1, '2027-09-01 10:00:00'),
                                     (3, 2, '2027-09-02 11:00:00'),
                                     (4, 4, '2027-09-15 09:30:00');

insert into flashcards (user_id,
                        front_text,
                        back_text) values
                                       (3, '你好', 'Hello'),
                                       (3, '谢谢', 'Thank you'),
                                       (3, '再见', 'Goodbye'),
                                       (4, '早上好', 'Good morning'),
                                       (4, '晚上好', 'Good evening'),
                                       (4, '我叫伊娃', 'My name is Eva'),
                                       (4, '中国', 'China'),
                                       (4, '北京', 'Beijing');