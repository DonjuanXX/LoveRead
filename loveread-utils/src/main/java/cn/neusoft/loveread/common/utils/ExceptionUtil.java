package cn.neusoft.loveread.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    /**
     * 获取异常的堆栈信息
     *
     * @param t 异常
     */
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
