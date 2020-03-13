package com.ecnu.achieveit.util;

import com.ecnu.achieveit.constant.RestCode;

/**
 * @author 倪事通
 */
public class RestResponse<T> {

    private int code;
    private String msg;
    private T data;


    /**
     * 分别提供返回成功和失败的不同的方法
     * 也就是说返回的数据形式是RestResponse所包含的
     * d第一个T ：泛型方法的标示，没有实际的意义
     * 第二个返回的数据类型的一种规范
          *这也就是所谓的工厂模式的应用,
     */

    public static <T> RestResponse<T> success() {
        return new RestResponse<>();
    }

    public static <T> RestResponse<T> success(T data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setData(data);
        return restResponse;
    }

    public static <T> RestResponse<T> error() {
        RestResponse<T> restResponse = new RestResponse<>(RestCode.FAIL.getCode(), RestCode.FAIL.getMsg());
        return restResponse;
    }

    public static <T> RestResponse<T> error(RestCode restCode) {
        RestResponse<T> restResponse = new RestResponse<>(restCode.getCode(), restCode.getMsg());
        return restResponse;
    }

    public static <T> RestResponse<T> error(String msg) {
        RestResponse<T> restResponse = new RestResponse<>(3, msg);
        return restResponse;
    }

    public RestResponse() {
        //默认会调用有参的构造函数,默认是成功的
        this(RestCode.OK.getCode(), RestCode.OK.getMsg());
    }

    public RestResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public RestResponse(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
