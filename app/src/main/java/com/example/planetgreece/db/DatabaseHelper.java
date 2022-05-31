package com.example.planetgreece.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.example.planetgreece.common.Helper;
import com.example.planetgreece.db.model.Article;
import com.example.planetgreece.db.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private static final int DB_VERSION = 6;

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
    private static final String USERS_SAVED_ARTICLES = "saved_articles";
    private static final String USERS_LIKED_ARTICLES = "liked_articles";

    // ARTICLES
    private static final String ARTICLES_TITLE = "title";
    private static final String ARTICLES_IMAGE = "image";
    private static final String ARTICLES_SITE_NAME = "site_name";
    private static final String ARTICLES_LINK = "link";
    private static final String ARTICLES_LIKES = "likes";

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
                    "%s TEXT," +
                    "%s TEXT," +
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
                    USERS_SAVED_ARTICLES,
                    USERS_LIKED_ARTICLES,
                    KEY_CREATED_AT);

    private static final String CREATE_TABLE_ARTICLES =
            String.format("CREATE TABLE %s" +
                "(" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT," +
                    "%s INTEGER," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s INTEGER DEFAULT 0," +
                    "%s DATETIME" +
                ")",
                    TABLE_ARTICLES,
                    KEY_ID,
                    ARTICLES_TITLE,
                    ARTICLES_IMAGE,
                    ARTICLES_SITE_NAME,
                    ARTICLES_LINK,
                    ARTICLES_LIKES,
                    KEY_CREATED_AT);

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

    /**********************************************************************************************
     * USERS TABLE METHODS
     **********************************************************************************************/

    public boolean userExists(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(Locale.getDefault(), "SELECT id FROM %s WHERE %s = %s",
                TABLE_USERS,
                KEY_ID,
                id
        );

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        int cursorCount = c.getCount();
        c.close();

        if (cursorCount > 0)
            return true;

        return false;
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
    public User getUser(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE %s = %d", TABLE_USERS, KEY_ID, id);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        if (c == null)
            return null;

        if (!c.moveToFirst())
            return null;

        User user = new User();

        user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        user.setFirstName(c.getString(c.getColumnIndex(USERS_FIRSTNAME)));
        user.setLastName(c.getString(c.getColumnIndex(USERS_LASTNAME)));
        user.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
        user.setSalt(c.getString(c.getColumnIndex(USERS_SALT)));
        user.setIsAdmin(c.getInt(c.getColumnIndex(USERS_IS_ADMIN)) == 1);
        user.setSavedArticles(c.getString(c.getColumnIndex(USERS_SAVED_ARTICLES)));
        user.setLikedArticles(c.getString(c.getColumnIndex(USERS_LIKED_ARTICLES)));
        user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return user;
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

        if (c == null)
            return null;

        if (!c.moveToFirst())
            return null;

        if (c.getCount() == 0)
            return null;

        User user = new User();
        user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        user.setFirstName(c.getString(c.getColumnIndex(USERS_FIRSTNAME)));
        user.setLastName(c.getString(c.getColumnIndex(USERS_LASTNAME)));
        user.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
//        user.setPassword(c.getString(c.getColumnIndex(USERS_PASSWORD)));
        user.setSalt(c.getString(c.getColumnIndex(USERS_SALT)));
        user.setIsAdmin(c.getInt(c.getColumnIndex(USERS_IS_ADMIN)) > 0);
        user.setSavedArticles(c.getString(c.getColumnIndex(USERS_SAVED_ARTICLES)));
        user.setLikedArticles(c.getString(c.getColumnIndex(USERS_LIKED_ARTICLES)));
        user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        c.close();

        return user;
    }

    @SuppressLint("Range")
    public String getUserPassword(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT password FROM %s WHERE %s = %d", TABLE_USERS, KEY_ID, id);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        if (c == null)
            return null;

        if (!c.moveToFirst())
            return null;

        String password = c.getString(c.getColumnIndex(USERS_PASSWORD));

        return password;
    }

    @SuppressLint("Range")
    public List<User> getUsers() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s ORDER BY id DESC", TABLE_USERS);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        List<User> users = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                user.setFirstName(c.getString(c.getColumnIndex(USERS_FIRSTNAME)));
                user.setLastName(c.getString(c.getColumnIndex(USERS_LASTNAME)));
                user.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
//                user.setPassword(c.getString(c.getColumnIndex(USERS_PASSWORD)));
                user.setSalt(c.getString(c.getColumnIndex(USERS_SALT)));
                user.setIsAdmin(c.getInt(c.getColumnIndex(USERS_IS_ADMIN)) > 0);
                user.setSavedArticles(c.getString(c.getColumnIndex(USERS_SAVED_ARTICLES)));
                user.setLikedArticles(c.getString(c.getColumnIndex(USERS_LIKED_ARTICLES)));
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

        // When deleting a user, we should delete all likes for the articles that the user has liked.
        // Work on that if we have the time before the deadline.

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
        values.put(USERS_IS_ADMIN, user.getIsAdmin() ? 1 : 0);
//        values.put(KEY_CREATED_AT, "datetime('now')");
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_USERS, null, values);

        return id;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_FIRSTNAME, user.getFirstName());
        values.put(USERS_LASTNAME, user.getLastName());
        values.put(USERS_EMAIL, user.getEmail());
        values.put(USERS_IS_ADMIN, user.getIsAdmin());

        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(user.getId()) });
    }

    public boolean changePassword(long id, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();

        if (!userExists(id)) {
            System.out.println("User does not exist");
            return false;
        }

        String newSalt = Helper.generateRandomString(32);

        ContentValues values = new ContentValues();
        values.put(USERS_PASSWORD, Helper.encryptPassword(newPassword, newSalt));
        values.put(USERS_SALT, newSalt);

        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        return true;
    }

    public boolean isArticleInSaved(int userId, int articleId) {
//        SQLiteDatabase db = getReadableDatabase();

        if (!userExists(userId)) {
            System.out.println("User does not exist");
            return false;
        }

        if (!articleExists(articleId)) {
            System.out.println("Article does not exist");
            return false;
        }

        User user = getUser(userId);

        if (user.getSavedArticles() == null)
            return false;

        String[] split = user.getSavedArticles().split(",");

        for (String s : split) {
            if (s.equals(String.valueOf(articleId))) {
                System.out.println("Article found in saved articles");
                return true;
            }
        }

        return false;
    }

    public boolean isArticleInLiked(int userId, int articleId) {
        if (!userExists(userId)) {
            System.out.println("User does not exist");
            return false;
        }

        if (!articleExists(articleId)) {
            System.out.println("Article does not exist");
            return false;
        }

        User user = getUser(userId);

        if (user.getLikedArticles() == null)
            return false;

        String[] split = user.getLikedArticles().split(",");

        for (String s : split) {
            if (s.equals(String.valueOf(articleId))) {
                System.out.println("Article found in liked articles");
                return true;
            }
        }

        return false;
    }

    public void addArticleToSaved(int userId, int articleId) {
        SQLiteDatabase db = getWritableDatabase();

        if (!userExists(userId)) {
            System.out.println("User does not exist");
            return;
        }

        if (!articleExists(articleId)) {
            System.out.println("Article does not exist");
            return;
        }

        User user = getUser(userId);
        ArrayList<String> savedArticles = new ArrayList<>();

        if (user.getSavedArticles() != null) {
            String[] split = user.getSavedArticles().split(",");

            for (String s : split) {
                if (s.equals(String.valueOf(articleId))) {
                    System.out.println("Article already saved");
                    return;
                }

                savedArticles.add(s);
            }
        }

        savedArticles.add(String.valueOf(articleId));

        ContentValues values = new ContentValues();
        values.put(USERS_SAVED_ARTICLES, TextUtils.join(",", savedArticles));

        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(userId) });
    }

    public void addArticleToLiked(int userId, int articleId) {
        SQLiteDatabase db = getWritableDatabase();

        if (!userExists(userId)) {
            System.out.println("User does not exist");
            return;
        }

        if (!articleExists(articleId)) {
            System.out.println("Article does not exist");
            return;
        }

        User user = getUser(userId);
        ArrayList<String> likedArticles = new ArrayList<>();

        if (user.getLikedArticles() != null) {
            String[] split = user.getLikedArticles().split(",");

            for (String s : split) {
                if (s.equals(String.valueOf(articleId))) {
                    System.out.println("Article already saved");
                    return;
                }

                likedArticles.add(s);
            }
        }

        likedArticles.add(String.valueOf(articleId));

        ContentValues values = new ContentValues();
        values.put(USERS_LIKED_ARTICLES, TextUtils.join(",", likedArticles));

        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(userId) });

        // increment article likes
        Article a = getArticle(articleId);
        a.incrementLikes();
        updateArticle(a);
    }

    public void removeArticleFromSaved(int userId, int articleId) {
        SQLiteDatabase db = getWritableDatabase();

        if (!userExists(userId)) {
            System.out.println("User does not exist");
            return;
        }

        if (!articleExists(articleId)) {
            System.out.println("Article does not exist");
            return;
        }

        User user = getUser(userId);

        if (user.getSavedArticles() == null) {
            System.out.println("User does not have any saved articles");
            return;
        }

        ArrayList<String> savedArticles = new ArrayList<>();
        String[] split = user.getSavedArticles().split(",");

        for (String s : split) {
            if (s.equals(String.valueOf(articleId))) {
                continue;
            }

            if (s.isEmpty() || s.trim().isEmpty()) {
                continue;
            }

            savedArticles.add(String.valueOf(s));
        }

        ContentValues values = new ContentValues();

        if (savedArticles.size() == 0) {
            values.putNull(USERS_SAVED_ARTICLES);
        } else {
            values.put(USERS_SAVED_ARTICLES, TextUtils.join(",", savedArticles));
        }

        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(userId) });
    }

    public void removeArticleFromLiked(int userId, int articleId) {
        SQLiteDatabase db = getWritableDatabase();

        if (!userExists(userId)) {
            System.out.println("User does not exist");
            return;
        }

        if (!articleExists(articleId)) {
            System.out.println("Article does not exist");
            return;
        }

        User user = getUser(userId);

        if (user.getLikedArticles() == null) {
            System.out.println("User does not have any saved articles");
            return;
        }

        ArrayList<String> likedArticles = new ArrayList<>();
        String[] split = user.getLikedArticles().split(",");

        for (String s : split) {
            if (s.equals(String.valueOf(articleId))) {
                continue;
            }

            if (s.isEmpty() || s.trim().isEmpty()) {
                continue;
            }

            likedArticles.add(String.valueOf(s));
        }

        ContentValues values = new ContentValues();

        if (likedArticles.size() == 0) {
            values.putNull(USERS_LIKED_ARTICLES);
        } else {
            values.put(USERS_LIKED_ARTICLES, TextUtils.join(",", likedArticles));
        }

        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(userId) });

        // decrement article likes
        Article a = getArticle(articleId);
        a.decrementLikes();
        updateArticle(a);
    }

    @SuppressLint("Range")
    public List<Article> getSavedArticles(long id) {
        if (!userExists(id)) {
            System.out.println("User does not exist");
            return null;
        }

        User user = getUser(id);

        if (user.getSavedArticles() == null) {
            System.out.println("User does not have any saved articles");
            return null;
        }

        ArrayList<Article> savedArticles = new ArrayList<>();
        String[] split = user.getSavedArticles().split(",");

        System.out.println(user.getSavedArticles());
        System.out.println(split);

        for (String s : split) {
            Article article = getArticle(Integer.parseInt(s));
            savedArticles.add(article);
        }

        Collections.reverse(savedArticles);

        return savedArticles;
    }

    /**********************************************************************************************
     * ARTICLES TABLE METHODS
     **********************************************************************************************/

    public boolean articleExists(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(Locale.getDefault(), "SELECT id FROM %s WHERE %s = %s",
                TABLE_ARTICLES,
                KEY_ID,
                id
        );

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        int cursorCount = c.getCount();
        c.close();

        if (cursorCount > 0)
            return true;

        return false;
    }

    public boolean articleExists(String link) {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(Locale.getDefault(), "SELECT id FROM %s WHERE %s = %s",
                TABLE_ARTICLES,
                ARTICLES_LINK,
                DatabaseUtils.sqlEscapeString(link)
        );

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        int cursorCount = c.getCount();
        c.close();

        if (cursorCount > 0)
            return true;

        return false;
    }

    @SuppressLint("Range")
    public Article getArticle(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE %s = %d", TABLE_ARTICLES, KEY_ID, id);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        if (c == null)
            return null;

        if (!c.moveToFirst())
            return null;

        Article article = new Article();
        article.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        article.setTitle(c.getString(c.getColumnIndex(ARTICLES_TITLE)));
        article.setImage(c.getString(c.getColumnIndex(ARTICLES_IMAGE)));
        article.setSiteName(c.getString(c.getColumnIndex(ARTICLES_SITE_NAME)));
        article.setLink(c.getString(c.getColumnIndex(ARTICLES_LINK)));
        article.setLikes(c.getInt(c.getColumnIndex(ARTICLES_LIKES)));
        article.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        c.close();

        return article;
    }

    @SuppressLint("Range")
    public List<Article> getArticles() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s ORDER BY created_at DESC", TABLE_ARTICLES);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        List<Article> articles = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Article article = new Article();
                article.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                article.setTitle(c.getString(c.getColumnIndex(ARTICLES_TITLE)));
                article.setImage(c.getString(c.getColumnIndex(ARTICLES_IMAGE)));
                article.setSiteName(c.getString(c.getColumnIndex(ARTICLES_SITE_NAME)));
                article.setLink(c.getString(c.getColumnIndex(ARTICLES_LINK)));
                article.setLikes(c.getInt(c.getColumnIndex(ARTICLES_LIKES)));
                article.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                articles.add(article);
            } while (c.moveToNext());
        }

        c.close();

        return articles;
    }

    public void deleteArticle(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ARTICLES, KEY_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public long createArticle(Article article) {
        if (articleExists(article.getLink())) {
            System.out.println("Article link already exists");
            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ARTICLES_TITLE, article.getTitle());
        values.put(ARTICLES_IMAGE, article.getImage());
        values.put(ARTICLES_SITE_NAME, article.getSiteName());
        values.put(ARTICLES_LINK, article.getLink());
        values.put(KEY_CREATED_AT, Helper.getTimestampByDateString(article.getCreatedAt()));
//        values.put(KEY_CREATED_AT, getDate());

        long id = db.insert(TABLE_ARTICLES, null, values);

        return id;
    }

    public void updateArticle(Article article) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ARTICLES_TITLE, article.getTitle());
        values.put(ARTICLES_IMAGE, article.getImage());
        values.put(ARTICLES_SITE_NAME, article.getSiteName());
        values.put(ARTICLES_LINK, article.getLink());
        values.put(ARTICLES_LIKES, article.getLikes());

        db.update(TABLE_ARTICLES, values, KEY_ID + " = ?", new String[] { String.valueOf(article.getId()) });
    }

    /**********************************************************************************************
     * MISC FUNCTIONS
     **********************************************************************************************/

    public void closeDb() {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (instance == null)
            instance = new DatabaseHelper(ctx.getApplicationContext());
        return instance;
    }

    /**********************************************************************************************
     * DATABASE RELATED FUNCTIONS
     **********************************************************************************************/

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
}
