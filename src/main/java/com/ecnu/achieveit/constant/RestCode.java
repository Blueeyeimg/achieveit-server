package com.ecnu.achieveit.constant;


public enum RestCode {

    /**
     * 操作成功
     */
    OK(0,"成功"),

    /**
     * 权限不足，失败
     */
    NO_PER(1,"权限不足"),

    /**
     * 其他原因，失败
     */
    FAIL(2,"失败");


    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
