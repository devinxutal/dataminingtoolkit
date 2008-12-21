package dm.categorizer.util;

import java.text.NumberFormat;

public class PrintUtil {
	public static void print(String content, int length) {
		length = Math.max(length, 2) - 1;
		int strlen = content.length();
		if (strlen > length) {
			content = content.substring(0, length);
			strlen = length;
		}
		System.out.print(content);
		for (int i = 0; i < (length - strlen + 1); i++)
			System.out.print(" ");
	}

	public static void print(int num, int length) {
		length = Math.max(length, 2) - 1;
		int strlen = lengthOf(num);
		if (strlen > length) {
			for (int i = 0; i < (strlen - length); i++) {
				num = num / 10;
			}
			strlen = length;
		}
		System.out.print(num);
		for (int i = 0; i < (length - strlen + 1); i++)
			System.out.print(" ");
	}

	public static void print(double num, int nlength, int flength) {
		flength = Math.max(flength, 2) - 1;
		nlength = Math.min(flength, nlength);
		int intlen = lengthOf((int) num);
		int fraclen = nlength - intlen - 1;
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(fraclen);
		format.setMinimumFractionDigits(fraclen);
		System.out.print(format.format(num));
		for (int i = 0; i < (flength - nlength + 1); i++)
			System.out.print(" ");
	}

	private static int lengthOf(int num) {
		int len = 1;
		while ((num = num / 10) != 0)
			len++;
		return len;
	}
}
