package me.hgo.smail;

public class SmailUtils {

    public static class STRING {
        public static boolean isBlank(CharSequence seq) {
            if (seq == null || seq.length() == 0) {
                return true;
            } else {
                boolean blank = true;
                for (int i = 0; i < seq.length() && blank; i++) {
                    blank = Character.isWhitespace(seq.charAt(i));
                }
                return blank;
            }
        }

        public static boolean isNotBlank(CharSequence seq) {
            return !isBlank(seq);
        }
    }
}
