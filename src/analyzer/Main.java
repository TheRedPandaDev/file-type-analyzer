package analyzer;

import analyzer.strategies.KMPStrategy;
import analyzer.strategies.Strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

//        Temporarily not needed
//        if (args.length != 4) {
//            System.out.println("Please provide checking strategy, relative file path, the pattern and type");
//            System.exit(0);
//        }

        if (args.length != 3) {
            System.out.println("Please provide checking strategy, relative file path, the pattern and type");
            System.exit(0);
        }

//        String checkingStrategy = args[0];
        String inputFilesPath = args[0];
        String patternString = args[1];
        String resultString = args[2];


//        switch (checkingStrategy) {
//            case "--naive":
//                patternChecker.setStrategy(new NaiveStrategy());
//                break;
//            case "--KMP":
//                patternChecker.setStrategy(new KMPStrategy());
//                break;
//            default:
//                System.out.println("Invalid checking strategy");
//                System.exit(0);
//        }

        Strategy strategyToUse = new KMPStrategy();

        List<Path> filePaths = null;

        try(Stream<Path> walk = Files.walk(Paths.get(inputFilesPath))) {
            filePaths = walk
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        List<Callable<Boolean>> checkerList = new ArrayList<>();


        for (Path fileToCheck : filePaths) {
            checkerList.add(new FileChecker(strategyToUse, fileToCheck, patternString));
        }

        List<Future<Boolean>> checkerResults = new ArrayList<>();

        for (Callable<Boolean> fileChecker : checkerList) {
            checkerResults.add(executorService.submit(fileChecker));
        }

        for (int i = 0; i < filePaths.size(); i++) {
            System.out.print(filePaths.get(i).getFileName() + ": ");
            try {
                if (checkerResults.get(i).get()) {
                    System.out.println(resultString);
                } else {
                    System.out.println("Unknown file type");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
