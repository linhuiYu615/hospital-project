package com.example.pojo.DTO;

import java.util.List;

public class ChartsDTO {
    private String date;
    private List<String> departmentNames;
    private List<Long> registrationCounts;

    // 构造方法
    public ChartsDTO() {}

    public ChartsDTO(String date, List<String> departmentNames, List<Long> registrationCounts) {
        this.date = date;
        this.departmentNames = departmentNames;
        this.registrationCounts = registrationCounts;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getDepartmentNames() {
        return departmentNames;
    }

    public void setDepartmentNames(List<String> departmentNames) {
        this.departmentNames = departmentNames;
    }

    public List<Long> getRegistrationCounts() {
        return registrationCounts;
    }

    public void setRegistrationCounts(List<Long> registrationCounts) {
        this.registrationCounts = registrationCounts;
    }
}
