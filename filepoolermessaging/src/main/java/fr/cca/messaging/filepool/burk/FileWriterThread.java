package fr.cca.messaging.filepool.burk;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;


public class FileWriterThread extends Thread {

    private final FileWriter fileWriter;

    public FileWriterThread(Runnable r, Path filePath) throws IOException {
        super(r);
        this.fileWriter = new FileWriter(filePath.toFile());
    }

    public FileWriter getFileWriter() {
        return this.fileWriter;
    }


    public void close() {
        try {
            this.fileWriter.close();
        } catch (IOException ioe) {
            // log.error("Oroblem occured while closing file writer.", ioe);
        }
    }
}
