package com.ecnu.achieveit.constant;

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

    ARCHIVED("已归档");


    private String state;

    public String getState() {
        return state;
    }

    ProjectState(String state){
        this.state = state;
    }
}
