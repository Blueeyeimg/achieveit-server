package com.ecnu.achieveit.util;

import com.ecnu.achieveit.constant.RestCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*@ExceptionHandler(value = BizException.class)
    @ResponseBody
    public  ResultBody bizExceptionHandler(HttpServletRequest req, BizException e){
        logger.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(),e.getErrorMsg());
    }*/

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public Object exceptionHandler(HttpServletRequest req, NullPointerException e){
        LogUtil.i("发生空指针异常！原因是:",e.toString());
        return RestResponse.fail("空指针异常");
    }


    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Object exceptionHandler(HttpServletRequest req, Exception e){
       LogUtil.i("异常:",e.toString());
        return RestResponse.fail(e.getMessage());
    }
}