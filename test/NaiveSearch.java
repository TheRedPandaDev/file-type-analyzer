

public class NaiveSearch {
    public static long searchForPattern(String[] args) {
        String pattern = args[1];
        String fileContent = args[0];
        long timeStart = System.nanoTime();
        if (naiveSearch(fileContent, pattern)) {
            return System.nanoTime() - timeStart;
        }
        return 0;
    }


    public static boolean naiveSearch (String text, String pattern) {
        int textLength = text.length();
        int patternLength = pattern.length();
        for (int i = 0; i<textLength - patternLength + 1; i++) {
            for (int j = 0; j< patternLength; j++) {
                if (!(text.charAt(i+j) == pattern.charAt(j))) {
                    break;
                }
                else if (j == patternLength -1) return true;
            }
        }
        return false;
    }
}
