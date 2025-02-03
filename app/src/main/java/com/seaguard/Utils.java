package com.seaguard;
import android.util.Patterns;

import com.seaguard.database.EntityModel;

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

    public static double getDistance (double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3; // metres
        double phi1 = Math.toRadians(lat1); // φ, λ in radians
        double phi2 = Math.toRadians(lat2); // φ, λ in radians
        double deltaPhi = Math.toRadians(lat2 - lat1); // difference in radians
        double deltaLambda = Math.toRadians(lon2 - lon1); // difference in radians

        // Haversine formula
        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                   Math.cos(phi1) * Math.cos(phi2) *
                   Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // in metres
    }

}
