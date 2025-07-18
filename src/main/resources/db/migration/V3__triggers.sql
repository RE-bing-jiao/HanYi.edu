drop function if exists update_modified_at() cascade;
create or replace function update_modified_at()
returns trigger as $$
    begin
        new.modified_at = current_timestamp;
        return new;
    end;
    $$ language plpgsql;

create trigger update_categories_modified_at
    before update on categories
    for each row execute function update_modified_at();

create trigger update_courses_modified_at
    before update on courses
    for each row execute function update_modified_at();

create trigger update_entries_modified_at
    before update on entries
    for each row execute function update_modified_at();

create trigger update_flashcards_modified_at
    before update on flashcards
    for each row execute function update_modified_at();

create trigger update_lessons_modified_at
    before update on lessons
    for each row execute function update_modified_at();

create trigger update_users_modified_at
    before update on users
    for each row execute function update_modified_at();
