package fr.cca.messaging.filepool.ok;

import fr.cca.messaging.filepool.IFileWriterPool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class DataPooler implements IFileWriterPool {

    private final BlockingQueue<String> queue;
    private volatile boolean isActive = true;

    public DataPooler() {
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void start(final String basePath, final String fileName) throws IOException {
        ExecutorService es = Executors.newSingleThreadExecutor();

        es.submit(
                () -> {
                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(Paths.get(basePath, fileName).toFile()));
                        while (isActive) {
                            String message = queue.take();
                            if (isActive && message != null) {
                                bw.write(message);
                                bw.newLine();
                            }
                        }
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        es.shutdown();
    }


    @Override
    public void writeRow(String row) {
        this.queue.add(row);
    }

    @Override
    public void close() throws Exception {
        this.isActive = false;
        this.queue.add("");
    }
}
