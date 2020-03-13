package com.ecnu.achieveit.constant;


/**
 * @author 倪事通
 */

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

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
