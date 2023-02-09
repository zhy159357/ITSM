package thread;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: ruoyi
 * @description:
 * @author: ma zy
 * @date: 2022-10-01 15:04
 **/
public class Test {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String day = String.format("%te", date);
        System.out.println("现在时间：" + day);
    }
}
