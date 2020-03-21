package com.ecnu.achieveit.constant;

public enum EmployeeTitle {

    /**
     * 员工类别
     */
    MANAGER("manager");

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    EmployeeTitle(String title){
        this.title = title;
    }
}
