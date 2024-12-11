package org.model;

import java.time.LocalDateTime;

public class CsvRecord {

    private String code;
    private String name;
    private String description;
    private LocalDateTime startDate;

    public CsvRecord(String code, String name, String description, LocalDateTime startDate) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
