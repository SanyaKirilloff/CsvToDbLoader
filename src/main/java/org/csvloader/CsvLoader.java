package org.csvloader;

import com.opencsv.exceptions.CsvException;
import org.csvreader.CsvReader;
import org.csvrecordprocessor.CsvRecordProcessor;
import org.databasemanager.DatabaseManager;
import org.model.CsvRecord;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class CsvLoader {

    private final String filePath;
    private final DatabaseManager databaseManager;
    private final int numThreads;

    public CsvLoader(String filePath, DatabaseManager databaseManager, int numThreads) {
        this.filePath = filePath;
        this.databaseManager = databaseManager;
        this.numThreads = numThreads;
    }

    public void load() throws IOException, InterruptedException, CsvException {
        CsvReader csvReader = new CsvReader(filePath);
        List<CsvRecord> records = csvReader.readCsvFile();

        BlockingQueue<CsvRecord> queue = new LinkedBlockingQueue<>(1000);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            CsvRecordProcessor processor = new CsvRecordProcessor(queue, databaseManager);
            executorService.submit(processor);
        }

        for (CsvRecord csvRecord : records) {
            queue.put(csvRecord);
        }

        while (!queue.isEmpty()) {
            Thread.sleep(100);
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        databaseManager.closeConnection(null);
    }
}
