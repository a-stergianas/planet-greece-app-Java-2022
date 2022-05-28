package com.example.planetgreece.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(generateRandomCharacter());
        }
        return stringBuilder.toString();
    }

    public static String generateRandomCharacter() {
        int randomInt = (int) (Math.random() * 26);
        char randomChar = (char) (randomInt + 65);
        return String.valueOf(randomChar);
    }

    public static String md5(String text) {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(text.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptPassword(String password, String salt) {
        return md5(md5(password) + salt);
    }

    public static boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    public static String capitalizeFirstLetter(String text) {
        if (text.isEmpty())
            return null;
        return text.substring(0, 1).toUpperCase() + text.substring(1, text.length());
    }

    public static String getTimestampByDateString(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date parsedDate = dateFormat.parse(dateString);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());

            return timestamp.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
