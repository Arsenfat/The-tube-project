package com.tubeproject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {
    private static final Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean checkEmail(String email) {
        Matcher m = pattern.matcher(email);
        return m.find();
    }
}
