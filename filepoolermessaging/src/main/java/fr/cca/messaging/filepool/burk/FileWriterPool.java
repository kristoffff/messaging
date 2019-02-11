package fr.cca.messaging.filepool.burk;

import fr.cca.messaging.filepool.IFileWriterPool;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class FileWriterPool implements IFileWriterPool{

    private ExecutorService executorService;
    private Set<FileWriterThread> threadSet = new HashSet<>();
    private AtomicInteger threadCounter;

    public FileWriterPool() {
        this.threadCounter = new AtomicInteger(0);
    }

    @Override
    public void start(final String basePath, final String fileName, final int nbThread) {
        executorService = Executors.newFixedThreadPool(nbThread, (Runnable r) -> {
            FileWriterThread thread = null;
            try {
                thread = new FileWriterThread(r, Paths.get(basePath, fileName + threadCounter.addAndGet(1)));
                threadSet.add(thread);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return thread;
        });
    }


    public void writeRow(String row) {
        executorService.submit(new AcceleratorRowWriter(row));
    }

    public void close() throws Exception {
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        executorService.shutdown();
        threadSet.stream().forEach(th -> {
            th.close();
        });

    }
}
