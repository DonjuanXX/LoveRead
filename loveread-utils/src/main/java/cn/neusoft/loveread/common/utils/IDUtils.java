package cn.neusoft.loveread.common.utils;

import java.util.Random;

/**
 * 各种id生成策略
 */
public class IDUtils {
    /**
     * 图片生成
     */
    public static String genImageName(){
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //补0
        String str = millis + String.format("%03d",end3);
        return str;
    }
    /**
     * 商品id生成
     */
    public static long genItemId(){
        //取当前时间的长整形包含毫秒
        long millis = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //补0
        String str = millis + String.format("%02d",end2);
        return new Long(str);
    }
}
