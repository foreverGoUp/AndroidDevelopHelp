package ckc.android.develophelp.lib.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DecimalUtil {

    public static int MONEY_SCALE = 2;//钱的小数位数
    public static int MONEY_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;//钱的小数取整方式：四舍五入

    /**
     * 将数据保留n个小数位
     * @param pattern 格式。如"#.00"：保留2位小数
     */
    public static String getDecimalFormated(double value, String pattern) {
        DecimalFormat dFormat = new DecimalFormat(pattern);
        String ret = dFormat.format(value);
        return '.' == ret.charAt(0) ? "0" + ret : ret;
    }

    /**
     * 获得经过取整的钱
     *
     * @param value float格式的计算结果
     */
    public static Float getMoneyWithRounded(Float value) {
        BigDecimal ret = new BigDecimal(String.valueOf(value));
        return ret.setScale(MONEY_SCALE, MONEY_ROUNDING_MODE).floatValue();
    }

}
