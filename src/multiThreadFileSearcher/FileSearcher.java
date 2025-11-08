package multiThreadFileSearcher;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileSearcher {

    private final String fileName;
    private final File startPath;

    public FileSearcher(String fileName, String startPath) {
        this.fileName = fileName;
        this.startPath = new File(startPath);
    }

    public void startSearch() {
        long startTime = System.currentTimeMillis();

        if (!startPath.exists()) {
            System.err.println("The specified path does not exist: " + startPath.getAbsolutePath());
            return;
        }

        // Create thread pool here — centralized control
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        AtomicBoolean found = new AtomicBoolean(false);

        System.out.println("Starting search using " + threads + " threads...");

        // Submit the initial directory task
        executor.submit(new SearchTask(startPath, fileName, found, executor));

        // Gracefully wait for all tasks to complete
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!found.get()) {
            System.out.println("File not found: " + fileName);
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Elapsed time: %.2f seconds%n", (endTime - startTime) / 1000.0);
    }
}