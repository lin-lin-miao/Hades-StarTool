package Utils;

// import java.util.Date;
import java.util.Random;

/**
 * 数学操作工具类
 * @author 靈凛
 * @version jdk:18.0.2
 * <p>版本:1.0
 *
 */
public class MathUtils {



    // /**秒差 */
    // public static long secondDvalue(Date start,Date end){
    //     return secondDvalue(start.getTime(),end.getTime());
    // }
    /**秒差 */
    public static long secondDvalue(long start,long end){
        if(start>end){
            return ((start - end) / 1000);
        }
        return ((end - start) / 1000);
    }

    /**获取 int 类型的位数 */
    public static int getdigits(int i){
        return Integer.toString(i).length();
    }
    /**获取 long 类型的位数 */
    public static int getdigits(long i){
        return String.valueOf(i).length();
    }

    /**
     * 产生一个随机数
     *
     * @max 最大值
     * @min 最小值
     * @return max到min的随机数(包含max和min)
     */
    public static int getRandomNum(int max,int min){
        if(max<min){
            int a = min;
            min = max;
            max = a;
        }
        if(max == min){
            return max;
        }
        Random rand = new Random();
        return rand.nextInt((max-min)+1) + min;
    }

}
