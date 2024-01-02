package scratch_compiler;

public class Utils {
    public static String generateID() {
        String id = randomString(20);
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

    public static String readFile(String path) {
        try {
            java.io.File file = new java.io.File(path);
            java.util.Scanner input = new java.util.Scanner(file);
            String text = "";
            while (input.hasNext()) {
                text += input.nextLine() + "\n";
            }
            input.close();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
