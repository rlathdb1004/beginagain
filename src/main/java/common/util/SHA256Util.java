package common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256Util {

	public static String encrypt(String rawText) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(rawText.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("SHA-256 암호화 실패", e);
		}
	}
}