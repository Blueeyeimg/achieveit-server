package com.ecnu.achieveit.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author 倪事通
 * 90%的验证都调用了Regular方法 但是本类也可删除大部分方法 涉及到正则的判断都直接穿参数和正则表达式
 * 但是为了方便业务类调用和有更直观的含义 建议不要这么做
 * Pattern的matcher已经被同步synchronized 所以 此类的任何使用正则验证的方法都不需要同步
 *
 */
public class Validate {

    //------------------常量定义
    /**
     * Email正则表达式=^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$
     */
    public static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 电话号码正则表达式= (^(\d{2,4}[-_－—]?)?\d{3,8}([-_－—]?\d{3,8})?([-_－—]?\d{1,7})?$)|(^0?1[35]\d{9}$)
     */
    public static final String PHONE = "(^(\\d{2,4}[-_－—]?)?\\d{3,8}([-_－—]?\\d{3,8})?([-_－—]?\\d{1,7})?$)|(^0?1[35]\\d{9}$)" ;
    /**
     * 手机号码正则表达式=^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8}$
     */
    public static final String MOBILE ="^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\\d{8}$";

    /**
     * IP地址正则表达式 ((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))
     */
    public static final String IPADDRESS = "((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))";

    /**
     * Integer正则表达式 ^-?(([1-9]\d*$)|0)
     */
    public static final String  INTEGER = "^-?(([1-9]\\d*$)|0)";
    /**
     * 正整数正则表达式 >=0 ^[1-9]\d*|0$
     */
    public static final String  INTEGER_NEGATIVE = "^[1-9]\\d*|0$";
    /**
     * 负整数正则表达式 <=0 ^-[1-9]\d*|0$
     */
    public static final String  INTEGER_POSITIVE = "^-[1-9]\\d*|0$";
    /**
     * Double正则表达式 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
     */
    public static final String  DOUBLE ="^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
    /**
     * 正Double正则表达式 >=0  ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$　
     */
    public static final String  DOUBLE_NEGATIVE ="^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$";
    /**
     * 负Double正则表达式 <= 0  ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
     */
    public static final String  DOUBLE_POSITIVE ="^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$";
    /**
     * 年龄正则表达式 ^(?:[1-9][0-9]?|1[01][0-9]|120)$ 匹配0-120岁
     */
    public static final String  AGE="^(?:[1-9][0-9]?|1[01][0-9]|120)$";
    /**
     * 邮编正则表达式  [1-9]\d{5}(?!\d) 国内6位邮编
     */
    public static final String  CODE="[1-9]\\d{5}(?!\\d)";
    /**
     * 匹配由数字、26个英文字母或者下划线组成的字符串 ^\w+$
     */
    public static final String STR_ENG_NUM_="^\\w+$";
    /**
     * 匹配由数字和26个英文字母组成的字符串 ^[A-Za-z0-9]+$
     */
    public static final String STR_ENG_NUM="^\\w+$";
    /**
     * 匹配由26个英文字母组成的字符串  ^[A-Za-z]+$
     */
    public static final String STR_ENG="^[A-Za-z]+$";
    /**
     * 匹配中文字符 ^[\u0391-\uFFE5]+$
     */
    public static final String STR_CHINA="^[\u0391-\uFFE5]+$";
    /**
     * 过滤特殊字符串正则
     * regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
     */
    public static final String STR_SPECIAL="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    /**
     *只能输英文 数字 中文 ^[a-zA-Z0-9\u4e00-\u9fa5]+$
     */
    public static final String STR_ENG_CHA_NUM="^[a-zA-Z0-9\u4e00-\u9fa5]+$";
    /**
     *
     */
    /***
     * 日期正则 支持：
     *  YYYY-MM-DD
     *  YYYY/MM/DD
     *  YYYY_MM_DD
     *  YYYYMMDD
     *  YYYY.MM.DD的形式
     */
    public static final String DATE_ALL="((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(10|12|0?[13578])([-\\/\\._]?)(3[01]|[12][0-9]|0?[1-9])$)" +
            "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(11|0?[469])([-\\/\\._]?)(30|[12][0-9]|0?[1-9])$)" +
            "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(0?2)([-\\/\\._]?)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([3579][26]00)" +
            "([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)" +
            "|(^([1][89][0][48])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][0][48])([-\\/\\._]?)" +
            "(0?2)([-\\/\\._]?)(29)$)" +
            "|(^([1][89][2468][048])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._]?)(0?2)" +
            "([-\\/\\._]?)(29)$)|(^([1][89][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|" +
            "(^([2-9][0-9][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$))";
    /***
     * 日期正则 支持：
     *  YYYY-MM-DD
     */
    public static final String DATE_FORMAT1="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
    //    1.验证日期的正则表达式加入闰年的判断以及思路分析
    //
    //    进入正题之前，我们需要澄清两个概念：
    //
    //    一，什么是合法的日期范围？对于不同的应用场景，这个问题有不同的解释。这里采纳MSDN中的约定：
    //
    //        DateTime 值类型表示值范围在公元（基督纪元）0001 年 1 月 1 日午夜 12:00:00 到公元 (C.E.) 9999 年 12 月 31 日晚上 11:59:59 之间的日期和时间。
    //
    //    二，关于闰年的阐释。人民教育出版社小学数学室的解释浅明易懂（mediawiki等都没说明为什么整百年份必须是400的倍数时才是闰年）：
    //
    //        关于公历闰年是这样规定的：地球绕太阳公转一周叫做一回归年，一回归年长365日5时48分 46秒。因此，公历规定有平年和闰年，平年一年有365日，比回归年短0.2422日，四年共短0.9688日，故每四年增加一日，这一年有366日，就是闰年。但四年增加一日比四个回归年又多0.0312日,400年后将多3.12日,故在400年中少设3个闰年,也就是在400年中只设97个闰年，这样公历年的平均长度与回归年就相近似了。由此规定：年份是整百数的必须是400的倍数才是闰年，例如1900年、2100年就不是闰年。
    //
    //    清楚了以上两个概念，我们进入正题。
    //    首先需要验证年份，显然，年份范围为 0001 - 9999，匹配YYYY的正则表达式为：
    //
    //        [0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}
    //
    //    其中 [0-9] 也可以表示为 \d，但 \d 不如 [0-9] 直观，因此下面我将一直采用 [0-9]
    //
    //    用正则表达式验证日期的难点有二：一是大小月份的天数不同，二是闰年的考虑。
    //    对于第一个难点，我们首先不考虑闰年，假设2月份都是28天，这样，月份和日期可以分成三种情况：
    //
    //    1、月份为 1, 3, 5, 7, 8, 10, 12，天数范围为 01 - 31，匹配MM-DD的正则表达式为：
    //
    //        (0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01])
    //
    //    2、月份为 4, 6, 9, 11，天数范围为 01-30，匹配MM-DD的正则表达式为：
    //
    //        (0[469]|11)-(0[1-9]|[12][0-9]|30)
    //
    //    3、月份为 2，考虑平年情况，匹配MM-DD的正则表达式为：
    //
    //        02-(0[1-9]|[1][0-9]|2[0-8])
    //
    //    根据上面的成果，我们可以得到匹配平年日期格式为YYYY-MM-DD的正则表达式：
    //
    //    ([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))
    //
    //    接着我们来解决第二个难点：闰年的考虑。根据闰年的定义，我们可以将闰年分为两类：
    //
    //    1、能被4整除但不能被100整除的年份。寻找后两位的变化规律，可以很快得到下面的正则匹配：
    //
    //        ([0-9]{2})(0[48]|[2468][048]|[13579][26])
    //
    //    2、能被400整除的年份。能被400整除的数肯定能被100整除，因此后两位肯定是00，我们只要保证前两位能被4整除即可，相应的正则表达式为：
    //
    //        (0[48]|[2468][048]|[3579][26])00
    //
    //    2.最强验证日期的正则表达式,添加了闰年的验证
    //
    //    这个日期正则表达式支持
    //    YYYY-MM-DD
    //    YYYY/MM/DD
    //    YYYY_MM_DD
    //    YYYY.MM.DD的形式
    //
    //    match : 2008-2-29 2008/02/29
    //
    //    not match : 2008-2-30   2007-2-29
    //
    //    完整的正则表达式如下:
    //    ((^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(10|12|0?[13578])([-\/\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(11|0?[469])([-\/\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(0?2)([-\/\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([3579][26]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][13579][26])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][13579][26])([-\/\._])(0?2)([-\/\._])(29)$))
    //    闰年的2月份有29天，因此匹配闰年日期格式为YYYY-MM-DD的正则表达式为：
    //
    //    (([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29
    //
    //    最后，将平年和闰年的日期验证表达式合并，我们得到最终的验证日期格式为YYYY-MM-DD的正则表达式为：
    //
    //    (([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)
    //
    //    DD/MM/YYYY格式的正则验证表达式为：
    //
    //    (((0[1-9]|[12][0-9]|3[01])/((0[13578]|1[02]))|((0[1-9]|[12][0-9]|30)/(0[469]|11))|(0[1-9]|[1][0-9]|2[0-8])/(02))/([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}))|(29/02/(([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00)))
    /**
     * URL正则表达式
     * 匹配 http www ftp
     */
    public static final String URL = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?" +
            "(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*" +
            "(\\w*:)*(\\w*\\+)*(\\w*\\.)*" +
            "(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

    /**
     * 身份证正则表达式
     */
    public static final String IDCARD="((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" +
            "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" +
            "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";
    /**
     * 1.匹配科学计数 e或者E必须出现有且只有一次 不含Dd
     * 正则 ^[-+]?(\d+(\.\d*)?|\.\d+)([eE]([-+]?([012]?\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))$
     */
    public final static String SCIENTIFIC_A ="^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))$";
    /**
     * 2.匹配科学计数 e或者E必须出现有且只有一次 结尾包含Dd
     * 正则 ^[-+]?(\d+(\.\d*)?|\.\d+)([eE]([-+]?([012]?\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))[dD]?$
     */
    public final static String SCIENTIFIC_B ="^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))[dD]?$";
    /**
     * 3.匹配科学计数 是否含有E或者e都通过 结尾含有Dd的也通过（针对Double类型）
     * 正则 ^[-+]?(\d+(\.\d*)?|\.\d+)([eE]([-+]?([012]?\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$
     */
    public final static String SCIENTIFIC_C ="^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$";
    /**
     * 4.匹配科学计数 是否含有E或者e都通过 结尾不含Dd
     * 正则 ^[-+]?(\d+(\.\d*)?|\.\d+)([eE]([-+]?([012]?\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?$
     */
    public final static String SCIENTIFIC_D ="^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?$";

    ////------------------验证方法
    /**
     * 判断字段是否为空 符合返回ture
     * @param str
     * @return boolean
     */
    public static synchronized boolean StrisNull(String str) {
        return null == str || str.trim().length() <= 0 ? true : false ;
    }
    /**
     * 判断字段是非空 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean StrNotNull(String str) {
        return !StrisNull(str) ;
    }
    /**
     * 字符串null转空
     * @param str
     * @return boolean
     */
    public static  String nulltoStr(String str) {
        return StrisNull(str)?"":str;
    }
    /**
     * 字符串null赋值默认值
     * @param str    目标字符串
     * @param defaut 默认值
     * @return String
     */
    public static  String nulltoStr(String str,String defaut) {
        return StrisNull(str)?defaut:str;
    }
    /**
     * 判断字段是否为Email 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isEmail(String str) {
        return Regular(str,EMAIL);
    }
    /**
     * 判断是否为电话号码 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isPhone(String str) {
        return Regular(str,PHONE);
    }
    /**
     * 判断是否为手机号码 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isMobile(String str) {
        return Regular(str,MOBILE);
    }
    /**
     * 判断是否为Url 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isUrl(String str) {
        return Regular(str,URL);
    }
    /**
     * 判断是否为IP地址 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isIpaddress(String str) {
        return Regular(str,IPADDRESS);
    }
    /**
     * 判断字段是否为数字 正负整数 正负浮点数 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isNumber(String str) {
        return Regular(str,DOUBLE);
    }
    /**
     * 判断字段是否为INTEGER  符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isInteger(String str) {
        return Regular(str,INTEGER);
    }
    /**
     * 判断字段是否为正整数正则表达式 >=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isINTEGER_NEGATIVE(String str) {
        return Regular(str,INTEGER_NEGATIVE);
    }
    /**
     * 判断字段是否为负整数正则表达式 <=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isINTEGER_POSITIVE(String str) {
        return Regular(str,INTEGER_POSITIVE);
    }
    /**
     * 判断字段是否为DOUBLE 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDouble(String str) {
        return Regular(str,DOUBLE);
    }
    /**
     * 判断字段是否为正浮点数正则表达式 >=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDOUBLE_NEGATIVE(String str) {
        return Regular(str,DOUBLE_NEGATIVE);
    }
    /**
     * 判断字段是否为负浮点数正则表达式 <=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDOUBLE_POSITIVE(String str) {
        return Regular(str,DOUBLE_POSITIVE);
    }
    /**
     * 判断字段是否为日期 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDate(String str) {
        return Regular(str,DATE_ALL);
    }
    /**
     * 验证2010-12-10
     * @param str
     * @return
     */
    public static  boolean isDate1(String str) {
        return Regular(str,DATE_FORMAT1);
    }
    /**
     * 判断字段是否为年龄 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isAge(String str) {
        return Regular(str,AGE) ;
    }
    /**
     * 判断字段是否超长
     * 字串为空返回fasle, 超过长度{leng}返回ture 反之返回false
     * @param str
     * @param leng
     * @return boolean
     */
    public static  boolean isLengOut(String str,int leng) {
        return StrisNull(str)?false:str.trim().length() > leng ;
    }
    /**
     * 判断字段是否为身份证 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isIdCard(String str) {
        if(StrisNull(str)) {
            return false;
        }
        if(str.trim().length() == 15 || str.trim().length() == 18) {
            return Regular(str,IDCARD);
        }else {
            return false ;
        }

    }
    /**
     * 判断字段是否为邮编 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isCode(String str) {
        return Regular(str,CODE) ;
    }
    /**
     * 判断字符串是不是全部是汉字
     * @param str
     * @return boolean
     */
    public static boolean isChina(String str) {
        return Regular(str,STR_CHINA) ;
    }
    /**
     * 判断字符串是不是全部是英文字母
     * @param str
     * @return boolean
     */
    public static boolean isEnglish(String str) {
        return Regular(str,STR_ENG) ;
    }
    /**
     * 判断字符串是不是全部是英文字母+数字
     * @param str
     * @return boolean
     */
    public static boolean isENG_NUM(String str) {
        return Regular(str,STR_ENG_NUM) ;
    }
    /**
     * 判断字符串是不是全部是英文字母+数字+下划线
     * @param str
     * @return boolean
     */
    public static boolean isENG_NUM_(String str) {
        return Regular(str,STR_ENG_NUM_) ;
    }
    /**
     * 过滤特殊字符串 返回过滤后的字符串
     * @param str
     * @return boolean
     */
    public static  String filterStr(String str) {
        Pattern p = Pattern.compile(STR_SPECIAL);
        Matcher m = p.matcher(str);
        return   m.replaceAll("").trim();
    }
    /**
     * 匹配是否符合正则表达式pattern 匹配返回true
     * @param str 匹配的字符串
     * @param pattern 匹配模式
     * @return boolean
     */
    private static  boolean Regular(String str,String pattern){
        //        System.out.println("pattern="+pattern);
        if(null == str || str.trim().length()<=0) {
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /**
     * 判断是不是科学计数法 如果是 返回true
     * 匹配科学计数 e或者E必须出现有且只有一次 结尾不含D
     * 匹配模式可参考本类定义的 SCIENTIFIC_A，SCIENTIFIC_B,SCIENTIFIC_C,SCIENTIFIC_D
     * 若判断为其他模式可调用 Regular(String str,String pattern)方法
     * @param str 科学计数字符串
     * @return boolean
     */
    public static  boolean isScientific(String str) {
        return !StrisNull(str) && Regular(str, SCIENTIFIC_A);
    }

}