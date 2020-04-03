package com.ecnu.achieveit.constant;


import java.util.Arrays;

public enum TimesheetState {
    /**
     * 项目工时的状态
     */
    DRAFT("草稿"),

    COMMITED("已提交"),

    CONFIRMED("已确认"),

    BACK("打回");

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    TimesheetState(String state) {
        this.state = state;
    }

    public boolean in(String state){
        long count = Arrays.stream(state.split(",")).filter(r -> this.state.equals(r)).count();
        return count != 0;
    }
}
