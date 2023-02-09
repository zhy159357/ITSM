package com.ruoyi.es.utils;

import java.text.DecimalFormat;

/**
 * 数学运算工具类
 * @author dayong_sun
 * @version 2021年04月12日
 */
public class MathUtil {

    /**
     * 获取百分比
     * num1分子，num2分母
     * @return 百分比
     */
    public static String numberRate(int num1, int num2) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        String rate = df.format(num1 * 100.00 / num2) + "%";
        return rate;
    }

    public static int stringLength(String str){
        int count=0;
        while(true)
        {
            try{
                str.charAt(count++);
            }catch (Exception e) {
                System.out.println("length= " + (--count));
                break;
            }
        }
        return count;
    }

}