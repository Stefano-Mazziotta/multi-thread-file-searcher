package multiThreadFileSearcher;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchTask implements Runnable {

	private static final Set<String> SKIP_DIRS = Set.of(
	        "/proc",        // Kernel pseudo-filesystem. Contains virtual files; can cause infinite reads or loops.
	        "/sys",         // Hardware/kernel pseudo-filesystem. Not meant for normal file traversal.
	        "/dev",         // Device files (disks, terminals, random). Reading them can block or be unsafe.
	        "/run",         // Volatile runtime data: sockets, PIDs, temp system state. Should not be scanned.
	        "/var/run",     // Symlink to /run. Excluded for the same reasons.
	        "/var/cache",   // Large cache directory. Slow and irrelevant for user file searches.
	        "/var/log",     // System logs. Huge, constantly changing files, not useful for file search.
	        "/tmp",         // Temporary files, often open or unstable. Produces noise and possible errors.
	        "/var/tmp",     // Persistent temp directory. Similar to /tmp; unnecessary to scan.
	        "/mnt",         // Mount points. May be slow network drives or external devices.
	        "/media"        // Removable drive mount points. Can cause delays or unavailable paths.
	);

    private final File directory;
    private final String targetFileName;
    private final AtomicBoolean found;
    private final ExecutorService executor;

    public SearchTask(File directory, String targetFileName, AtomicBoolean found, ExecutorService executor) {
        this.directory = directory;
        this.targetFileName = targetFileName;
        this.found = found;
        this.executor = executor;
    }

    @Override
    public void run() {
        if (found.get() || Thread.currentThread().isInterrupted()) return;

        if (shouldSkip(directory)) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (found.get() || Thread.currentThread().isInterrupted()) return;

            if (file.isDirectory()) {
            	executor.submit(new SearchTask(file, targetFileName, found, executor));
            }

            if (!file.getName().equals(targetFileName)) continue;

            if (found.compareAndSet(false, true)) {
                System.out.println("File found: " + file.getAbsolutePath());
                executor.shutdownNow();
                return;
            }
        }
    }

    private boolean shouldSkip(File directory) {
        Path path = directory.toPath();
        
        // Avoid symbolic links (they can create directory loops)
        if (Files.isSymbolicLink(path)) return true;

        String abs = directory.getAbsolutePath();

        // Exact matches
        if (SKIP_DIRS.contains(abs)) return true;

        return false;
    }
}
