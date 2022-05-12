package com.example.planetgreece.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.planetgreece.common.Helper;
import com.example.planetgreece.db.model.Article;
import com.example.planetgreece.db.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "PlanetGreece.db";

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ARTICLES = "articles";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // USERS
    private static final String USERS_FIRSTNAME = "firstname";
    private static final String USERS_LASTNAME = "lastname";
    private static final String USERS_EMAIL = "email";
    private static final String USERS_PASSWORD = "password";
    private static final String USERS_SALT = "salt";
    private static final String USERS_IS_ADMIN = "is_admin";

    // ARTICLES
    private static final String ARTICLES_TITLE = "title";
    private static final String ARTICLES_CONTENT = "content";
    private static final String ARTICLES_IMAGE = "image";
    private static final String ARTICLES_SITE_NAME = "site_name";
    private static final String ARTICLES_LINK = "link";

    // Create tables
    private static final String CREATE_TABLE_USERS =
            String.format("CREATE TABLE %s" +
                "(" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s INTEGER DEFAULT 0," +
                    "%s DATETIME" +
                ")",
                    TABLE_USERS,
                    KEY_ID,
                    USERS_FIRSTNAME,
                    USERS_LASTNAME,
                    USERS_EMAIL,
                    USERS_PASSWORD,
                    USERS_SALT,
                    USERS_IS_ADMIN,
                    KEY_CREATED_AT);

    private static final String CREATE_TABLE_ARTICLES =
            String.format("CREATE TABLE %s" +
                "(" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT" +
                    "%s TEXT" +
                    "%s TEXT" +
                    "%s TEXT" +
                    "%s TEXT" +
                ")",
                    TABLE_ARTICLES,
                    KEY_ID,
                    ARTICLES_TITLE,
                    ARTICLES_CONTENT,
                    ARTICLES_IMAGE,
                    ARTICLES_SITE_NAME,
                    ARTICLES_LINK);

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ARTICLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @SuppressLint("Range")
    public User getUser(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE %s = %d", TABLE_USERS, KEY_ID, id);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        if (c != null)
            c.moveToFirst();

        User user = new User();

        user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        user.setFirstName(c.getString(c.getColumnIndex(USERS_FIRSTNAME)));
        user.setLastName(c.getString(c.getColumnIndex(USERS_LASTNAME)));
        user.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
        user.setIsAdmin(c.getInt(c.getColumnIndex(USERS_IS_ADMIN)) == 1);
        user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return user;
    }

    public boolean userExists(String email) {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(Locale.getDefault(), "SELECT id FROM %s WHERE %s = %s",
                TABLE_USERS,
                USERS_EMAIL,
                DatabaseUtils.sqlEscapeString(email)
        );

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        int cursorCount = c.getCount();
        c.close();

        if (cursorCount > 0)
            return true;

        return false;
    }

    @SuppressLint("Range")
    public User getUser(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE %s = %s",
                TABLE_USERS,
                USERS_EMAIL,
                DatabaseUtils.sqlEscapeString(email)
        );

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        if (c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return null;

        User user = new User();
        user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        user.setFirstName(c.getString(c.getColumnIndex(USERS_FIRSTNAME)));
        user.setLastName(c.getString(c.getColumnIndex(USERS_LASTNAME)));
        user.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
//        user.setPassword(c.getString(c.getColumnIndex(USERS_PASSWORD)));
        user.setSalt(c.getString(c.getColumnIndex(USERS_SALT)));
        user.setIsAdmin(c.getInt(c.getColumnIndex(USERS_IS_ADMIN)) == 1);
        user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        c.close();

        return user;
    }

    @SuppressLint("Range")
    public List<User> getUsers() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s", TABLE_USERS);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        List<User> users = new ArrayList<User>();

        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                user.setFirstName(c.getString(c.getColumnIndex(USERS_FIRSTNAME)));
                user.setLastName(c.getString(c.getColumnIndex(USERS_LASTNAME)));
                user.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
//                user.setPassword(c.getString(c.getColumnIndex(USERS_PASSWORD)));
                user.setSalt(c.getString(c.getColumnIndex(USERS_SALT)));
                user.setIsAdmin(c.getInt(c.getColumnIndex(USERS_IS_ADMIN)) == 1);
                user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                users.add(user);
            } while (c.moveToNext());
        }

        c.close();

        return users;
    }

    public void deleteUser(long id) {
        SQLiteDatabase db = getWritableDatabase();

//        String query = String.format(Locale.getDefault(), "DELETE FROM %s WHERE %s = %d", TABLE_USERS, KEY_ID, id);
//        Cursor c = db.rawQuery(query, null);

        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public long createUser(User user) {
        if (userExists(user.getEmail())) {
            System.out.println("User already exists");
            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_FIRSTNAME, user.getFirstName());
        values.put(USERS_LASTNAME, user.getLastName());
        values.put(USERS_EMAIL, user.getEmail());
        values.put(USERS_PASSWORD, user.getPassword());
        values.put(USERS_SALT, user.getSalt());
        values.put(USERS_IS_ADMIN, user.getIsAdmin());
//        values.put(KEY_CREATED_AT, "datetime('now')");
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_USERS, null, values);

        return id;
    }

    @SuppressLint("Range")
    public Article getArticle(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE %s = %d", TABLE_ARTICLES, KEY_ID, id);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        if (c != null)
            c.moveToFirst();

        Article article = new Article();
        article.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        article.setTitle(c.getString(c.getColumnIndex(ARTICLES_TITLE)));
        article.setContent(c.getString(c.getColumnIndex(ARTICLES_CONTENT)));
        article.setImage(c.getString(c.getColumnIndex(ARTICLES_IMAGE)));
        article.setSiteName(c.getString(c.getColumnIndex(ARTICLES_SITE_NAME)));
        article.setLink(c.getString(c.getColumnIndex(ARTICLES_LINK)));
        article.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        c.close();

        return article;
    }

    @SuppressLint("Range")
    public List<Article> getArticles() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s", TABLE_ARTICLES);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        List<Article> articles = new ArrayList<Article>();

        if (c.moveToFirst()) {
            do {
                Article article = new Article();
                article.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                article.setTitle(c.getString(c.getColumnIndex(ARTICLES_TITLE)));
                article.setContent(c.getString(c.getColumnIndex(ARTICLES_CONTENT)));
                article.setImage(c.getString(c.getColumnIndex(ARTICLES_IMAGE)));
                article.setSiteName(c.getString(c.getColumnIndex(ARTICLES_SITE_NAME)));
                article.setLink(c.getString(c.getColumnIndex(ARTICLES_LINK)));
                article.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                articles.add(article);
            } while (c.moveToNext());
        }

        c.close();

        return articles;
    }

    public boolean checkLogin(String email, String password) {
        if (!userExists(email)) {
            System.out.println("User does not exist.");
            return false;
        }

        SQLiteDatabase db = getReadableDatabase();

        User u = getUser(email);

        String query = String.format(Locale.getDefault(), "SELECT id FROM %s WHERE %s = %s AND %s = '%s'",
                TABLE_USERS,
                USERS_EMAIL,
                DatabaseUtils.sqlEscapeString(email),
                USERS_PASSWORD,
                Helper.encryptPassword(password, u.getSalt())
        );

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        int cursorCount = c.getCount();
        c.close();

        if (cursorCount > 0)
            return true;

        return false;
    }

    public void closeDb() {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (instance == null)
            instance = new DatabaseHelper(ctx.getApplicationContext());
        return instance;
    }
}
