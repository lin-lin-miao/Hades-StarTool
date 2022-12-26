//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.hadesstartool;

import java.util.Random;

public class Sentence {
    private static String[] eggString = new String[]{"喵~","泡泡?", "只要你需要我无处不在", "开始记录", "我叫靈凛", "已适应世界规律", "正在读取世界法则", "是谁惊动了深空", "我可以计算出未来", "星海是我最终的归宿", "》深空补给港欢迎您《"};
    private int hour;
    private static String[] lifeString = new String[]{"要好好吃饭(^▽^)b", "快去做作业怒`-з-)=3", "今天也是新的一天", "你那里是怎样的天气", "今天天气好就出门走走", "其实下雨也不错", "啦啦啦", "啊?", "咦!", "哼", "哇", "哈?", "不可以涩涩", "だめだ", "萌混过关(*^▽^)", "装傻", "阿巴阿巴", "qwq", "Qwq", "awa", "Wow"};
    private static String[] time1113 = new String[]{"中午好", "树阴满地日当午,梦觉流莺时一声", "谁知盘中餐,粒粒皆辛苦", "金碧上青空,花晴帘影红", "云淡风轻过午天,傍花随柳过前川", "午景帘栊静,薰风草木酣"};
    private static String[] time1416 = new String[]{"下午好", "白旗辉烈日,遥映一杯浓", "阳光和爱并行", "金色的阳光如同美酒", "萦绕在茶香中的温馨", "光透着些浪漫,夹杂着青春的幻想"};
    private static String[] time1719 = new String[]{"傍晚好", "夕阳无限好,只是近黄昏", "东篱把酒黄昏后,有暗香盈袖", "月上柳梢头,人约黄昏后", "雀桥边野草花,乌衣巷口夕阳斜", "夕阳西下,断肠人在天涯"};
    private static String[] time2022 = new String[]{"月转碧梧移鹊影,露低红草湿萤光", "晚上好", "月落乌啼霜满天,江枫渔火对愁眠", "独出门前望野田,月明荞麦花如雪", "月黑见渔灯,孤光一点萤", "怀君属秋夜,散步咏凉天"};
    private static String[] time231 = new String[]{"深夜好", "星稀河影转,霜重月华孤", "愁边动寒角,夜久意难平", "星垂平野阔,月涌大江流", "夜深知雪重,时闻折竹声", "微微风簇浪,散作满河星", "窗前明月光,疑是地上霜", "夜深人静了"};
    private static String[] time24 = new String[]{"凌晨好", "半欲天明半未明,醉闻花气睡闻莺", "你知道洛杉矶凌晨四点钟是什么样子吗?", "满天星星,寥落的灯光,行人很少", "夜幕轻纱犹在", "夜饮东坡醒复醉,归来仿佛三更", "梦好莫催醒,由他好处行", "雾交才洒地,风逆旋随云"};
    private static String[] time57 = new String[]{"复见林上月,娟娟犹未沉", "早上好", "画堂晨起,来报雪花坠", "晨起动征铎,客行悲故乡", "漏传初五点,鸡报第三声", "晨鸡初叫,昏鸦争噪,那个不去红尘闹", "清晨入古寺,初日照高林", "日轮擘水出,始觉江面宽"};
    private static String[] time810 = new String[]{"上午好", "金鸡三唱,东方既白", "清禽百啭似迎客,正在有情无思间", "日出雾露馀,青松如膏沐", "宿鸟动前林,晨光上东屋", "小雨晨光内,初来叶上闻", "初日破苍烟,零乱松竹影"};
    private static String[] tipsString = new String[]{"今日宜摸金", "今日宜打红", "今日宜打蓝", "小号要常喂哦", "不要偷懒", "tp是个好东西", "时间扭曲太棒啦", "白星需要团队合作", "后勤全家桶很正常", "不要太冒险", "经过计算就知道了", "计算器在右边", "长距离移动可能会驶向深渊", "你无法观测到未来"};

    public Sentence(int var1) {
        this.hour = var1;
    }

    private String life() {
        int var1 = (new Random()).nextInt(lifeString.length);
        return this.lifeString[var1];
    }

    private String time() {
        Random var1 = new Random();
        int var2;
        switch(this.hour) {
            case 0:
            case 1:
            case 23:
                var2 = var1.nextInt(this.time231.length);
                return this.time231[var2];
            case 2:
            case 3:
            case 4:
                var2 = var1.nextInt(this.time24.length);
                return this.time24[var2];
            case 5:
            case 6:
            case 7:
                var2 = var1.nextInt(this.time57.length);
                return this.time57[var2];
            case 8:
            case 9:
            case 10:
                var2 = var1.nextInt(this.time810.length);
                return this.time810[var2];
            case 11:
            case 12:
            case 13:
                var2 = var1.nextInt(this.time1113.length);
                return this.time1113[var2];
            case 14:
            case 15:
            case 16:
                var2 = var1.nextInt(this.time1416.length);
                return this.time1416[var2];
            case 17:
            case 18:
            case 19:
                var2 = var1.nextInt(this.time1719.length);
                return this.time1719[var2];
            case 20:
            case 21:
            case 22:
                var2 = var1.nextInt(this.time2022.length);
                return this.time2022[var2];
            default:
                return "时间读取失败啰";
        }
    }

    private String tips() {
        int var1 = (new Random()).nextInt(this.tipsString.length);
        return this.tipsString[var1];
    }

    public String egg() {
        int var1 = (new Random()).nextInt(this.eggString.length);
        return this.eggString[var1];
    }

    public String run() {
        int var1 = (new Random()).nextInt(4);
        String var2 = null;
        switch(var1) {
            case 0:
                var2 = this.egg();
                break;
            case 1:
                var2 = this.tips();
                break;
            case 2:
                var2 = this.life();
                break;
            case 3:
                var2 = this.time();
        }

        return var2;
    }

    public String toString() {
        return this.run();
    }
}
