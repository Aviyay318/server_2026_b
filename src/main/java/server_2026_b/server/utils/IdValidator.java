package server_2026_b.server.utils;

public class IdValidator {

    public static final int ID_LENGTH = 9;

    private IdValidator() {
    }

    public static boolean checkID(String id) {
        if (id == null) {
            return false;
        }
        id = id.trim();
        if (id.isEmpty() || id.length() > ID_LENGTH) {
            return false;
        }

        for (int i = 0; i < id.length(); i++) {
            if (id.charAt(i) > '9' || id.charAt(i) < '0') {
                return false;
            }
        }

        String padded = padWithLeadingZeros(id);
        return calculateID(padded) % 10 == 0;
    }

    public static String normalize(String id) {
        if (id == null) {
            return null;
        }
        return padWithLeadingZeros(id.trim());
    }

    private static String padWithLeadingZeros(String id) {
        if (id.length() >= ID_LENGTH) {
            return id;
        }
        StringBuilder sb = new StringBuilder(id);
        while (sb.length() < ID_LENGTH) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

    private static int calculateID(String id) {
        int sum = 0;
        for (int i = 0; i < id.length(); i++) {
            int digit = id.charAt(i) - '0';
            int temp = ((i % 2) + 1) * digit;

            if (temp > 9) {
                sum += temp / 10 + temp % 10;
            } else {
                sum += temp;
            }
        }
        return sum;
    }
}
