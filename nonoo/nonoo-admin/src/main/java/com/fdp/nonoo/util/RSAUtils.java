package com.fdp.nonoo.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

public final class RSAUtils {
	private static final Provider provider = new BouncyCastleProvider();
	private static final int size = 1024;

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator
					.getInstance("RSA", provider);
			generator.initialize(size, new SecureRandom());
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		Assert.notNull(publicKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA", provider);
			cipher.init(1, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte[] arrayOfByte = encrypt(publicKey, text.getBytes());
		return (arrayOfByte != null) ? Base64.encodeBase64String(arrayOfByte)
				: null;
	}

	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		Assert.notNull(privateKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",
					provider);
			cipher.init(2, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
		}
		return null;
	}

	public static String decrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		return (data != null) ? new String(data) : null;
	}
}