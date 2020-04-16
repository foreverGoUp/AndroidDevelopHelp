package ckc.android.develophelp.lib.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class InputLimitUtils {

    /**
     * 功能
     * 1、第一个字符输入“.”时，返回“0.”
     * 2、限制金额输入小数点后2位
     * 3、限制整数位数
     */
    public static void limitMoney(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                //如果第一个字符输入.则转换为输入0.
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    //限制小数位数，小数点+后面字符数的长度
                    int length = dest.toString().substring(index).length();
                    if (length == 3) {
                        return "";
                    }
                    //限制整数位数，小数点前数字的长度
                    length = dest.toString().substring(0, index).length();
                    if (length == 15) {
                        return "";
                    }
                } else {
                    //限制整数位数
                    if (dest.toString().length() == 15) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }
}
