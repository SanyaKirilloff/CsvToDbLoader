package org.csvrecordprocessor;

import org.databasemanager.DatabaseManager;
import org.model.CsvRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CsvRecordProcessor implements Runnable {

    private final BlockingQueue<CsvRecord> queue;
    private final DatabaseManager databaseManager;

    public CsvRecordProcessor(BlockingQueue<CsvRecord> queue, DatabaseManager databaseManager) {
        this.queue = queue;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        List<CsvRecord> batch = new ArrayList<>();
        int batchSize = 1000;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                CsvRecord csvRecord = queue.take();
                batch.add(csvRecord);

                if (batch.size() >= batchSize) {
                    databaseManager.upsertRecords(batch);
                    batch.clear();

                    if (queue.isEmpty()) {
                        break;
                    }
                }
            } catch (InterruptedException | SQLException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (!batch.isEmpty()) {
            try {
                databaseManager.upsertRecords(batch);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
