package fr.cca.messaging.filepool;

import java.io.IOException;

public interface IFileWriterPool {


    default void start(final String basePath, final String fileName) throws IOException {
    }

    default void start(final String basePath, final String fileName, final int nbThread) {
    }

    void writeRow(String row);

    void close() throws Exception;
}
