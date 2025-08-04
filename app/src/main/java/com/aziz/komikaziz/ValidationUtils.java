package com.aziz.komikaziz;

import android.util.Patterns;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() >= 2;
    }

    public static String getPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return "Lemah";
        }

        int strength = 0;

        // Check length
        if (password.length() >= 8) strength++;

        // Check for uppercase
        if (Pattern.compile("[A-Z]").matcher(password).find()) strength++;

        // Check for lowercase
        if (Pattern.compile("[a-z]").matcher(password).find()) strength++;

        // Check for digits
        if (Pattern.compile("[0-9]").matcher(password).find()) strength++;

        // Check for special characters
        if (Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) strength++;

        switch (strength) {
            case 0:
            case 1:
                return "Lemah";
            case 2:
            case 3:
                return "Sedang";
            case 4:
            case 5:
                return "Kuat";
            default:
                return "Lemah";
        }
    }
}
