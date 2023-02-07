package com.edu.m7.feedback.util;

import java.util.Random;

public final class ShortCodeUtil {

    private static final Random random = new Random();

    private ShortCodeUtil() {
    }


    public static String generate(int length) {
        StringBuilder shortCode = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int type = random.nextInt(3);
            switch (type) {
                case 0:
                    shortCode.append((char) (random.nextInt(10) + 48));
                    break; // 0-9
                case 1:
                    shortCode.append((char) (random.nextInt(26) + 65));
                    break; // A-Z
                case 2:
                    shortCode.append((char) (random.nextInt(26) + 97));
                    break; // a-z
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
        }

        return shortCode.toString();
    }
}
