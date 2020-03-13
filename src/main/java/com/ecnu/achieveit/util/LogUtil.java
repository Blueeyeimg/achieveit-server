package com.ecnu.achieveit.util;

/**
 * 管理Log的工具类：可通过设置mCurrentLevel，控制Log输出级别。
 * 项目上线时应将mCurrentLevel设置为LEVEL_NONE，禁止Log输出。
 */
public class LogUtil {

    public static final boolean LOG_VALID = true;
    //日志输出级别NONE
    public static final int LEVEL_NONE = 0;
    //日志输出级别E
    public static final int LEVEL_ERROR = 1;
    //日志输出级别W
    public static final int LEVEL_WARN = 2;
    //日志输出级别I
    public static final int LEVEL_INFO = 3;
    //日志输出级别D
    public static final int LEVEL_DEBUG = 4;
    //日志输出级别V
    public static final int LEVEL_VERBOSE = 5;

    //日志输出时的Tag
    private static String mTag = "LogUtil";
    //当前日志输出级别（是否允许输出log）
    private static int mCurrentLevel = LEVEL_VERBOSE;

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String msg) {
        if (mCurrentLevel >= LEVEL_INFO) {
            Log.i("Information", msg);
        }
    }
    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(String tag, String msg) {
        if (mCurrentLevel >= LEVEL_INFO) {
            Log.i(tag, msg);
        }
    }

    public static class Log{

        public static void i(String tag, String msg){
            if(LOG_VALID) {
                System.out.println(tag + ": " + msg);
            }
        }
    }
}
