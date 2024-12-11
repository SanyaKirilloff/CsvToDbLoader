package org.csvreader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.model.CsvRecord;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvReader {

    private final String filePath;

    public CsvReader(String filePath) {
        this.filePath = filePath;
    }

    public List<CsvRecord> readCsvFile() throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext();
            return reader.readAll().stream()
                    .map(row -> new CsvRecord(
                            row[0],
                            row[1],
                            row[2],
                            LocalDateTime.parse(row[3], DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    ))
                    .toList();
        }
    }
}
