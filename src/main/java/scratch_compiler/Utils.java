package scratch_compiler;

import java.util.ArrayList;

public class Utils {
    private static ArrayList<String> ids = new ArrayList<>();

    public static String generateID() {
        String id = randomString(20);

        if (ids.contains(id))
            return generateID();

        ids.add(id);
        return id;
    }

    private static String id_chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static char randomChar() {
        return id_chars.charAt((int) (Math.random() * id_chars.length()));
    }

    private static String randomString(int length) {
        String str = "";
        for (int i = 0; i < length; i++) {
            str += randomChar();
        }
        return str;
    }
}
