package org.main;

import com.opencsv.exceptions.CsvException;
import org.csvloader.CsvLoader;
import org.databasemanager.DatabaseManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        String filePath = "C:/Users/kirillov.a/IdeaProjects/LeetCodeTasks/large_data.csv"; //указать путь к своему файлу
        DatabaseManager databaseManager = new DatabaseManager(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "postgres"
        ); // указать свои креды подключения к БД
        int numThreads = 16;

        CsvLoader csvLoader = new CsvLoader(filePath, databaseManager, numThreads);
        try {
            csvLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | CsvException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Время выполнения приложения: " + executionTime + " миллисекунд");
    }
}
