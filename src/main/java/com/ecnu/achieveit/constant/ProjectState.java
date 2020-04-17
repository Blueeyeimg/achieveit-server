package com.ecnu.achieveit.constant;

import java.util.Arrays;

/**
 * @author 倪事通
 */

public enum ProjectState {

    /**
     * 项目生命周期中的各个状态
     */
    APPLIED("申请立项"),

    APPROVED("同意立项"),

    DISAPPROVED("驳回立项"),

    PROCESSING("进行中"),

    DELIVERED("已交付"),

    FINISHED("已结束"),

    APPLYINGARCHIVE("申请归档"),

    ARCHIVED("已归档");


    private String state;

    public String getState() {
        return state;
    }

    ProjectState(String state){
        this.state = state;
    }

    public boolean in(String state){
        long count = Arrays.stream(state.split(",")).filter(r -> this.state.equals(r)).count();
        return count != 0;
    }

    /**
     * 用于检查所要更改的state是否合法，按照业务逻辑只能改为以下五种state
     * @param state
     * @return
     */
    public static boolean checkState(String state){
        if(state.equals(PROCESSING.getState()) || state.equals(DELIVERED.getState())
        || state.equals(FINISHED.getState()) ||  state.equals(APPLYINGARCHIVE.getState())
                || state.equals(ARCHIVED.getState())) return true;
        return false;
    }

    public static boolean inProcess(String state){
        return state.equals(APPROVED.getState())
                || state.equals(PROCESSING.getState())
                || state.equals(DELIVERED.getState());
    }
}
