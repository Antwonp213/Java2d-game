package debug;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Encrypter
{	
	public static final String password = "92440641caf7fda1f6d85ff62ecd41db4c89de1b69ca2421dd312d074bde1623";

	//Using SHA-512 Encryption
	private static byte[] encrypt(String password) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");  
		
		byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
		
		System.out.println("Encrypted.");
		
		return digest;
	}
	
	private static String toHex(byte[] hash)
	{
		BigInteger num = new BigInteger(1, hash);
		
		//Hex is 16 bits in legnth, convert it to so.
		StringBuilder hex = new StringBuilder(num.toString(16));
		
		while(hex.length() < 32)
		{
			hex.insert(0, '0');
		}
		
		System.out.println("Making hex string");
		
		return hex.toString();
	}
	
	public static boolean checkPassword(String userPassword)
	{
		try
		{
			userPassword = toHex(encrypt(userPassword));
			
			if(userPassword.length() != password.length())
			{
				return false;
			}
			
			return password.equals(userPassword);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	//Below does work, but it keeps changing the input.
//	private byte[] encrypt(String password) throws Exception
//	{
//		Signature sign = Signature.getInstance("SHA256withRSA");
//		
//		keyPairGen.initialize(2048);
//		
//		KeyPair pair = keyPairGen.generateKeyPair();
//		
//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//		
//		cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
//		
//		byte[] input = password.getBytes();
//		cipher.update(input);
//		
//		byte[] cipherText = cipher.doFinal();
//				
//		return cipherText;
//	}
//	
//	public boolean checkEncryption(String password)
//	{
//		byte[] checkPassword;
//		
//		try 
//		{
//			checkPassword = encrypt(password);
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//			return false;
//		}
//		
//		System.out.println("Entered password: " + Arrays.toString(checkPassword));
//		System.err.println("Current passwrod: " + Arrays.toString(Encrypter.password));
//		
//		//Check password lengths, if they match, check index by index.
//		if(checkPassword.length != Encrypter.password.length)
//		{
//			System.out.println("Don't even need to check lmao");
//			return false;
//		}
//		
//		for(int i = 0; i < checkPassword.length; i++)
//		{
//			if(checkPassword[i] != Encrypter.password[i])
//			{
//				return false;
//			}
//		}
//		
//		return true;
//	}
}
