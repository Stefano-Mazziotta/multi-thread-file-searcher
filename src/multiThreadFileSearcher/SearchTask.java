package multiThreadFileSearcher;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchTask implements Runnable {

    private final File directory;
    private final String fileName;
    private final AtomicBoolean found;
    private final ExecutorService executor;

    public SearchTask(File directory, String fileName, AtomicBoolean found, ExecutorService executor) {
        this.directory = directory;
        this.fileName = fileName;
        this.found = found;
        this.executor = executor;
    }

    @Override
    public void run() {
        if (found.get() || Thread.currentThread().isInterrupted()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (found.get() || Thread.currentThread().isInterrupted()) return;

            if (file.isFile()) {
                if (file.getName().equals(fileName)) {
                    if (found.compareAndSet(false, true)) {
                        System.out.println("File found: " + file.getAbsolutePath());
                        executor.shutdownNow(); // stop all tasks
                        return;
                    }
                }
            } else if (file.isDirectory()) {
                executor.submit(new SearchTask(file, fileName, found, executor));
            }
        }
    }
}
