package com.asb.snapshot.utils;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-12
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
public class Syslog {



    private static Logger log = Logger.getLogger(Syslog.class);

    /**
     * @param logstr
     */
    public static void syslog(String logstr) {
        log.info(logstr);
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//				.format(new Date()) + logstr);
    }

    /**
     * @param logstr
     */
    public static void syslogonlyprint(String logstr) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()) + logstr);
    }

    /**
     * @param logstr
     */
    public static void syslogonlylog(String logstr) {
        log.info(logstr);
    }

    /**
     * 对字节数组进行监控
     *
     * @param info
     * @param ba
     */
    public static void syslogBytes(String info, byte[] ba) {
        String logstr = "";
        for (byte b : ba) {
            logstr += formatStr(Integer.toHexString((b + 256) % 256))
                    .toUpperCase() + " ";
        }
        log.info(info + ":" + logstr);
    }

    /**
     * 对字节数组进行监控
     *
     * @param info
     * @param ba
     */
    public static void syslogBytesOnlyPrint(String info, byte[] ba) {
        String logstr = "";
        for (byte b : ba) {
            logstr += formatStr(Integer.toHexString((b + 256) % 256))
                    .toUpperCase() + " ";
        }
        System.out.println(info + ":" + logstr);
    }

    /**
     * @param info
     * @param ba
     */
    public static void syslogBytesOnlyLog(String info, byte[] ba) {
        String logstr = "";
        for (byte b : ba) {
            logstr += formatStr(Integer.toHexString((b + 256) % 256))
                    .toUpperCase() + " ";
        }
        log.info(info + ":" + logstr);
//		System.out.println(info + ":" + logstr);
    }

    /**
     * 格式化输出将一位String格式化为两位的十六进制格式输出
     *
     * @param inputStr
     * @return
     */
    public static String formatStr(String inputStr) {
        int len = inputStr.length();
        switch (len) {
            case 0:
                inputStr = "00";
                break;
            case 1:
                inputStr = "0" + inputStr.toUpperCase();
                break;
            case 2:
                break;
        }
        return inputStr;
    }


    public static void main(String args[]){
       Syslog.syslog("hel");
    }
}
