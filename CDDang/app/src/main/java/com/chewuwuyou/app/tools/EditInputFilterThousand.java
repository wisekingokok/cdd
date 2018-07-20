package com.chewuwuyou.app.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @author zeng
 * 
 *         edittext输入最大值为50000 ,最小值为0.01
 */
public class EditInputFilterThousand implements InputFilter {

	/**
	 * 最大数字
	 */
	public int MAX_VALUE;
	/**
	 * 小数点后的数字的位数
	 */
	public int PONTINT_LENGTH;
	
	Pattern p;

	public EditInputFilterThousand(int MAX_VALUE, int PONTINT_LENGTH) {
		this.MAX_VALUE = MAX_VALUE;
		this.PONTINT_LENGTH = PONTINT_LENGTH;
		p = Pattern.compile("[0-9]*"); // 除数字外的其他的
	}

	/**
	 * source 新输入的字符串 start 新输入的字符串起始下标，一般为0 end 新输入的字符串终点下标，一般为source长度-1 dest
	 * 输入之前文本框内容 dstart 原内容起始坐标，一般为0 dend 原内容终点坐标，一般为dest长度-1
	 */

	@Override
	public CharSequence filter(CharSequence arg0, int arg1, int arg2,
			Spanned arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub

		String oldtext = arg3.toString();
		System.out.println(oldtext);
		// 验证删除等按键
		if ("".equals(arg0.toString())) {
			return null;
		}
		// 验证非数字或者小数点的情况
		Matcher m = p.matcher(arg0);
		if (oldtext.contains(".")) {
			// 已经存在小数点的情况下，只能输入数字
			if (!m.matches()) {
				return null;
			}
		} else {
			// 未输入小数点的情况下，可以输入小数点和数字
			if (!m.matches() && !arg0.equals(".")) {
				return null;
			}
		}
		// 验证输入金额的大小
		if (!arg0.toString().equals("") && !oldtext.toString().equals("")) {

			double dold = Double.parseDouble(oldtext + arg0.toString());
			if (dold > MAX_VALUE) {
				return arg3.subSequence(arg4, arg5);
			} else if (dold == MAX_VALUE) {
				if (arg0.toString().equals(".")) {
					return arg3.subSequence(arg4, arg5);
				}
			}
		}
		// 验证小数位精度是否正确
		if (oldtext.contains(".")) {
			int index = oldtext.indexOf(".");
			int len = arg5 - index;
			// 小数位只能2位
			if (len > PONTINT_LENGTH) {
				CharSequence newText = arg3.subSequence(arg4, arg5);
				return newText;
			}
		}
		return arg3.subSequence(arg4, arg5) + arg0.toString();
	}

}
