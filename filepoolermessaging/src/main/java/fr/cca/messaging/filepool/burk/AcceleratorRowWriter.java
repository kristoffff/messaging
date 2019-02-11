package fr.cca.messaging.filepool.burk;

import fr.cca.messaging.filepool.burk.FileWriterThread;

import java.io.IOException;

public class AcceleratorRowWriter implements Runnable {

    private String str;

    public AcceleratorRowWriter(String str) {
        this.str = str;
    }

    public void run() {
        try {
            ((FileWriterThread) Thread.currentThread()).getFileWriter().append(this.str).append(System.getProperty("line.separator"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
