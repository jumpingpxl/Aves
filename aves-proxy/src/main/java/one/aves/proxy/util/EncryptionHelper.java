package one.aves.proxy.util;

import one.aves.api.console.ConsoleLogger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionHelper {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(EncryptionHelper.class);

	private EncryptionHelper() {
	}

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
			keypairgenerator.initialize(1024);
			return keypairgenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException nosuchalgorithmexception) {
			nosuchalgorithmexception.printStackTrace();
			LOGGER.printSevere("Key pair generation failed!");
			return null;
		}
	}

	public static PublicKey decodePublicKey(byte[] encodedKey) {
		try {
			EncodedKeySpec encodedkeyspec = new X509EncodedKeySpec(encodedKey);
			KeyFactory keyfactory = KeyFactory.getInstance("RSA");
			return keyfactory.generatePublic(encodedkeyspec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException var3) {
		}

		LOGGER.printSevere("Public key reconstitute failed!");
		return null;
	}

	public static SecretKey decryptSharedKey(PrivateKey key, byte[] secretKeyEncrypted) {
		return new SecretKeySpec(decryptData(key, secretKeyEncrypted), "AES");
	}

	public static byte[] decryptData(Key key, byte[] data) {
		return cipherOperation(2, key, data);
	}

	private static byte[] cipherOperation(int opMode, Key key, byte[] data) {
		try {
			return createTheCipherInstance(opMode, key.getAlgorithm(), key).doFinal(data);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		LOGGER.printSevere("Cipher data failed!");
		return null;
	}

	private static Cipher createTheCipherInstance(int opMode, String transformation, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(opMode, key);
			return cipher;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		LOGGER.printSevere("Cipher creation failed!");
		return null;
	}

	public static Cipher createNetCipherInstance(int opMode, Key key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			cipher.init(opMode, key, new IvParameterSpec(key.getEncoded()));
			return cipher;
		} catch (GeneralSecurityException generalsecurityexception) {
			throw new RuntimeException(generalsecurityexception);
		}
	}
}
