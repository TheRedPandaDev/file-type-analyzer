public class ContainsSearch {
    public static long searchForPattern(String[] args) {
        String pattern = args[1];
        String patternName = args[2];
        String fileContent = args[0];
        String unknownFileType = "Unknown file type";
        long timeStart = System.nanoTime();
        if (fileContent.contains(pattern) /*!kmpSearch(fileContent.toString(), pattern).isEmpty()*/) {
            return System.nanoTime() - timeStart;
        }
        return 0;
    }
}
