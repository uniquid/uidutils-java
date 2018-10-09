package com.uniquid.encryption;

import org.spongycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.Key;

/**
 * Class to manage AES encryption
 */
public class AESUtils {

	private static final String KEY_ALGO = "AES";
	private static final String ENC_ALGO = "AES/CBC/PKCS5Padding";
	// private static final int SALT_BYTES = 24;
	private static final int HASH_BYTES = 24;
	private static final int PBKDF2_ITERATIONS = 1000;
	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static byte[] SALT = new String("458924034589240345892403").getBytes();
	// Compute a SALT from securerandom: this makes a good salt—which doesn't
	// need to be kept secret

	// https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption/992413#992413
	// https://gist.github.com/jtan189/3804290
	// https://gist.github.com/scotttam/874426

	/**
	 * Encrypt a string with AES algorithm.
	 *
	 * @param data
	 *            is a string
	 * @return the encrypted string
	 */
	public static String[] encrypt(String data, String password) throws Exception {

		String[] result = new String[2];

		Key key = getPasswordHash(password);

		Cipher cipher = Cipher.getInstance(ENC_ALGO);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

		byte[] encVal = cipher.doFinal(data.getBytes());

		result[0] = new String(Base64.encode(iv), Charset.forName("UTF-8"));
        result[1] = new String(Base64.encode(encVal), Charset.forName("UTF-8"));

		return result;
	}

	/**
	 * Decrypt a string with AES algorithm.
	 *
	 * @param encryptedData
	 *            is a string
	 * @return the decrypted string
	 */
	public static String decrypt(String encryptedData, String initialVector, String password) throws Exception {

		Key key = getPasswordHash(password);

		Cipher cipher = Cipher.getInstance(ENC_ALGO);

		byte[] iv = Base64.decode(initialVector);

		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		byte[] decordedValue = Base64.decode(encryptedData);
		byte[] decValue = cipher.doFinal(decordedValue);
		return new String(decValue);

	}

	/**
	 * Computes the PBKDF2 hash of a password.
	 *
	 * @param password
	 *            the password to hash.
	 * @return the PBDKF2 hash of the password
	 */
	private static SecretKey getPasswordHash(String password) throws Exception {
		// // Generate a random salt
		// SecureRandom random = new SecureRandom();
		// byte[] salt = new byte[SALT_BYTES];
		// random.nextBytes(salt);

		// Hash the password
		SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, PBKDF2_ITERATIONS, HASH_BYTES * 8);
		SecretKey tmp = skf.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), KEY_ALGO);
	}

}
