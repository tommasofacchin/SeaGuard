package com.seaguard;
import android.util.Patterns;

public class Utils {

    public static boolean validateEmail (String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return  password != null &&
                !password.isEmpty() &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[!@#$%^&*()_+\\-=;':\"\\\\|,.<>/?].*");
    }

}
