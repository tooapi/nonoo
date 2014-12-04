package com.fdp.nonoo.util;

public class WordUtils {

	public static String toPropertyName(String s) {
		return toName(s, '.');
	}

	private static String toName(String s, char ch) {
		if (s == null || s.length() == 0)
			return s;

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;
			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i >= 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					if (i > 0)
						sb.append(ch);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}
			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

}
