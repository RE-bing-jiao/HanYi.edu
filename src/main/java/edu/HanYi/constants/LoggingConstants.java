package edu.HanYi.constants;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LoggingConstants {
    private LoggingConstants() {}
    public static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");

    //COURSE
    public static final String COURSE_EXISTS_HEADER = "Course already exists with header: {}";
    public static final String CATEGORY_NOT_FOUND_ID = "Category not found with ID: {}";
    public static final String COURSE_CREATED = "Created course ID: {}, header: {}";
    public static final String COURSE_NOT_FOUND_ID = "Course not found with ID: {}";
    public static final String COURSES_FOUND_CATEGORY = "Found {} courses for category ID: {}";
    public static final String COURSE_UPDATE_FAILED_HEADER = "Update failed - header already exists: {}";
    public static final String COURSE_UPDATE_FAILED_CATEGORY = "Update failed - category not found with ID: {}";
    public static final String COURSE_UPDATED = "Updated course ID: {}, new header: {}";
    public static final String COURSE_DELETE_FAILED = "Delete failed - course not found with ID: {}";
    public static final String COURSE_DELETED = "Deleted course ID: {}";
    public static final String COURSE_UPDATE_FAILED = "Update failed - course not found with ID: {}";
    public static final String COURSE_CREATE = "Creating course with header: {}";
    public static final String COURSES_FETCHED = "Fetched {} courses";
    public static final String COURSE_FETCH = "Fetching course by ID: {}";
    public static final String COURSES_BY_CATEGORY = "Fetched {} courses for category ID: {}";
    public static final String COURSE_UPDATE = "Updating course ID: {}, new header: {}";
    public static final String COURSE_DELETE = "Deleting course ID: {}";

    //CATEGORY
    public static final String CATEGORY_EXISTS_NAME = "Category already exists: {}";
    public static final String CATEGORY_CREATED = "Created category ID: {}, name: {}";
    public static final String CATEGORY_UPDATE_FAILED_NAME = "Update failed - name already exists: {}";
    public static final String CATEGORY_UPDATED = "Updated category ID: {}, new name: {}";
    public static final String CATEGORY_DELETE_FAILED = "Delete failed - category not found with ID: {}";
    public static final String CATEGORY_DELETED = "Deleted category ID: {}";
    public static final String CATEGORY_CREATE = "Creating category with name: {}";
    public static final String CATEGORIES_FETCHED = "Fetched {} categories";
    public static final String CATEGORY_FETCH = "Fetching category by ID: {}";
    public static final String CATEGORY_UPDATE = "Updating category ID: {}, new name: {}";
    public static final String CATEGORY_DELETE = "Deleting category ID: {}";

    //ENTRY
    public static final String ENTRY_EXISTS = "Entry already exists for user ID: {} and course ID: {}";
    public static final String ENTRY_CREATED = "Created entry ID: {} for user ID: {} and course ID: {}";
    public static final String ENTRY_NOT_FOUND_ID = "Entry not found with ID: {}";
    public static final String ENTRY_DELETE_FAILED = "Delete failed - entry not found with ID: {}";
    public static final String ENTRY_DELETED = "Deleted entry ID: {}";
    public static final String ENTRY_CREATE = "Creating entry for user ID: {} and course ID: {}";
    public static final String ENTRIES_FETCHED = "Fetched {} entries";
    public static final String ENTRY_FETCH = "Fetching entry by ID: {}";
    public static final String ENTRIES_BY_USER = "Fetched {} entries for user ID: {}";
    public static final String ENTRIES_BY_COURSE = "Fetched {} entries for course ID: {}";
    public static final String ENTRY_DELETE = "Deleting entry ID: {}";

    //FLASHCARD
    public static final String FLASHCARD_CREATED = "Created flashcard ID: {} for user ID: {}";
    public static final String FLASHCARDS_FETCHED = "Fetched {} flashcards";
    public static final String FLASHCARD_NOT_FOUND_ID = "Flashcard not found with ID: {}";
    public static final String FLASHCARD_UPDATED = "Updated flashcard ID: {}";
    public static final String FLASHCARD_DELETED = "Deleted flashcard ID: {}";
    public static final String FLASHCARD_CREATE = "Creating flashcard for user ID: {}";
    public static final String FLASHCARD_FETCH = "Request received for flashcards by user ID: {}";
    public static final String FLASHCARD_UPDATE = "Updating flashcard ID: {}";
    public static final String FLASHCARD_DELETE = "Deleting flashcard ID: {}";

    //LESSON
    public static final String LESSON_EXISTS = "Lesson already exists: header={}, courseId={}";
    public static final String LESSON_CREATED = "Created lesson ID: {}, header: {}, courseId: {}";
    public static final String LESSONS_FETCHED = "Fetched {} lessons";
    public static final String LESSON_NOT_FOUND_ID = "Lesson not found: ID={}";
    public static final String LESSON_HEADER_CONFLICT = "Lesson header conflict: header={}, courseId={}";
    public static final String LESSON_UPDATED = "Updated lesson ID: {}";
    public static final String LESSON_DELETED = "Deleted lesson ID: {}";
    public static final String LESSON_CREATE = "Creating lesson for course ID: {}";
    public static final String LESSON_FETCH = "Fetching lesson ID: {}";
    public static final String LESSONS_BY_COURSE = "Fetching lessons for course ID: {}";
    public static final String LESSON_UPDATE = "Updating lesson ID: {}";
    public static final String LESSON_DELETE = "Deleting lesson ID: {}";

    //USER
    public static final String USERNAME_EXISTS = "Username already exists: {}";
    public static final String EMAIL_EXISTS = "Email already exists: {}";
    public static final String USER_CREATED = "Created user ID: {}, username: {}";
    public static final String USERS_FETCHED = "Fetched {} users";
    public static final String USER_NOT_FOUND_ID = "User not found: ID={}";
    public static final String USER_UPDATED = "Updated user ID: {}";
    public static final String USER_DELETED = "Deleted user ID: {}";
    public static final String USER_CREATE = "Creating new user with username: {}";
    public static final String USER_FETCH = "Fetching user ID: {}";
    public static final String USER_UPDATE = "Updating user ID: {}";
    public static final String USER_DELETE = "Deleting user ID: {}";

    //CONTACT
    public static final String CONTACT_NAME_BLANK = "Name cannot be blank";
    public static final String CONTACT_EMAIL_BLANK = "Email cannot be blank";
    public static final String CONTACT_REQUEST_SAVED = "Contact request saved from {}";
    public static final String CONTACT_ERROR = "Error saving contact request";

    //AUTHENTICATION
    public static final String SIGNUP_ATTEMPT = "Attempting to sign up user: {}";
    public static final String SIGNUP_EMAIL_EXISTS = "Signup failed - email already exists: {}";
    public static final String SIGNUP_USERNAME_EXISTS = "Signup failed - username already exists: {}";
    public static final String USER_CREATION_SUCCESS = "User created successfully: {}";
    public static final String TOKEN_GENERATED = "JWT token generated for user: {}";
    public static final String SIGNIN_ATTEMPT = "Attempting to sign in user: {}";
    public static final String USER_NOT_FOUND_AFTER_AUTH = "User not found after successful authentication: {}";
    public static final String SIGNIN_SUCCESS = "User successfully signed in: {}";
    public static final String AUTH_FAILED = "Authentication failed for user: {}";
    public static final String SIGNUP_ERROR = "Signup error for email: {}";
    public static final String UNEXPECTED_SIGNUP_ERROR = "Unexpected signup error";
    public static final String JWT_COOKIE_SET = "JWT cookie set for user: {}";
    public static final String INVALID_LOGIN_ATTEMPT = "Invalid login attempt for email: {}";
    public static final String LOGIN_ERROR = "Login error";

    //DEBUG
    public static final String DEBUG_CHECK_HEADER = "Checking if course exists with header: {}";
    public static final String DEBUG_FETCH_CATEGORY = "Fetching category with ID: {}";
    public static final String DEBUG_CREATE_COURSE = "Creating new course: {}";
    public static final String DEBUG_FETCH_COURSE = "Found course: {}";
    public static final String DEBUG_FETCH_COURSES_CATEGORY = "Fetching courses for category ID: {}";
    public static final String DEBUG_CATEGORY_EXISTS = "Category exists, fetching courses";
    public static final String DEBUG_FETCH_COURSE_UPDATE = "Fetching course for update ID: {}";
    public static final String DEBUG_CHECK_HEADER_AVAILABILITY = "Checking header availability: {}";
    public static final String DEBUG_CHECK_ENTRY_EXISTS = "Checking if entry exists for user ID: {} and course ID: {}";
    public static final String DEBUG_FETCH_USER = "Fetching user with ID: {}";
    public static final String DEBUG_FETCH_COURSE_ENTRY = "Fetching course with ID: {}";
    public static final String DEBUG_CREATE_ENTRY = "Creating new entry for user: {} and course: {}";
    public static final String DEBUG_FETCH_ENTRY = "Found entry ID: {} for user ID: {}";
    public static final String DEBUG_FETCH_ENTRIES_USER = "Fetching entries for user ID: {}";
    public static final String DEBUG_FETCH_ENTRIES_COURSE = "Fetching entries for course ID: {}";
    public static final String DEBUG_CHECK_ENTRY_EXISTENCE = "Checking if entry exists with ID: {}";
    public static final String DEBUG_CHECK_LESSON_EXISTENCE = "Checking lesson existence: header={}, courseId={}";
    public static final String DEBUG_FETCH_COURSE_LESSON = "Fetching course ID: {}";
    public static final String DEBUG_CHECK_COURSE_EXISTENCE = "Checking course existence ID: {}";
    public static final String DEBUG_CHECK_LESSON_HEADER = "Checking lesson header uniqueness: header={}, courseId={}";
    public static final String DEBUG_CHECK_USERNAME = "Checking username availability: {}";
    public static final String DEBUG_CHECK_EMAIL = "Checking email availability: {}";
    public static final String DEBUG_CHECK_NEW_USERNAME = "Checking new username availability: {}";
    public static final String DEBUG_CHECK_NEW_EMAIL = "Checking new email availability: {}";
    public static final String DEBUG_CHECK_USER_EXISTENCE = "Checking user existence ID: {}";
    public static final String DEBUG_CHECK_LESSON_EXISTENCE_ID = "Checking lesson existence ID: {}";
    public static final String DEBUG_FETCH_FLASHCARD = "Fetching flashcard ID: {}";
    public static final String DEBUG_CHECK_FLASHCARD_EXISTENCE = "Checking flashcard existence ID: {}";
    public static final String DEBUG_FETCH_FLASHCARDS_USER = "Fetching flashcards for user ID: {}";
    public static final String DEBUG_CREATE_CATEGORY = "Creating new category: {}";
    public static final String DEBUG_FOUND_CATEGORY = "Found category: {}";
    public static final String CATEGORY_UPDATE_FAILED = "Update failed - category not found with ID: {}";
    public static final String DEBUG_CHECK_CATEGORY_EXISTS = "Checking if category exists with name: {}";
}
