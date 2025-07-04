create type user_role as enum ('ADMIN', 'TEACHER', 'STUDENT');

create table if not exists categories (
                            id serial primary key,
                            name varchar(25) not null unique,
                            description text not null,
                            created_at timestamp with time zone default current_timestamp
);

create table if not exists users (
                       id serial primary key,
                       username varchar(25) not null unique,
                       password varchar(255) not null,
                       email varchar(35) not null unique,
                       role user_role not null,
                       created_at timestamp with time zone default current_timestamp
);

create table if not exists courses (
                         id serial primary key,
                         header varchar(25) not null,
                         description text not null,
                         category_id integer not null references categories(id) on delete cascade,
                         entry_date timestamp with time zone not null,
                         exit_date timestamp with time zone not null,
                         progress decimal(5,2) default 0.00,
                         created_at timestamp with time zone default current_timestamp,
                         constraint valid_progress check (progress >= 0 and progress <= 100),
                         constraint valid_dates check (entry_date <= exit_date)
);

create table if not exists lessons (
                         id serial primary key,
                         lesson_order_num integer not null,
                         header varchar(25) not null,
                         description text not null,
                         url text not null,
                         course_id integer not null references courses(id) on delete cascade,
                         created_at timestamp with time zone default current_timestamp,
                         constraint unique_lesson_order unique (course_id, lesson_order_num)
);

create table if not exists entries (
                         id serial primary key,
                         user_id integer not null references users(id) on delete cascade,
                         course_id integer not null references courses(id) on delete cascade,
                         entry_date timestamp with time zone not null,
                         created_at timestamp with time zone default current_timestamp,
                         constraint unique_user_course unique (user_id, course_id)
);

create table if not exists flashcards (
                            id serial primary key,
                            user_id integer not null references users(id) on delete cascade,
                            front_text varchar(25) not null,
                            back_text varchar(35) not null,
                            created_at timestamp with time zone default current_timestamp
);

create index idx_courses_category on courses(category_id);
create index idx_lessons_course on lessons(course_id);
create index idx_entries_user on entries(user_id);
create index idx_entries_course on entries(course_id);
create index idx_flashcards_user on flashcards(user_id);
