package com.example.les17.dto;

import java.util.ArrayList;
import java.util.List;

public class VacancyDto {
    private Integer id;

    private String description;

    private double hourlyRate;

    private double workingHour;

    private String type;

    private String location;

    private String status;

    List<ApplicationDto> applications = new ArrayList<>();

    public VacancyDto() {
    }

    public VacancyDto(Integer id, String description, double hourlyRate, double workingHour, String type, String location, String status) {
        this.id = id;
        this.description = description;
        this.hourlyRate = hourlyRate;
        this.workingHour = workingHour;
        this.type = type;
        this.location = location;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(double workingHour) {
        this.workingHour = workingHour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ApplicationDto> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationDto> applications) {
        this.applications = applications;
    }
}
