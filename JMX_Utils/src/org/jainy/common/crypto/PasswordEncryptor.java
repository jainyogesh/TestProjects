package org.jainy.common.crypto;

//MOCK IMPLEMENTATION
public class PasswordEncryptor {

	public void setCipherPassword(String cipherPassword) {
		//System.out.println("[TRACE] PasswordEncryptor.setCipherPassword -> " + cipherPassword);
	}

	public String encrypt(String password) {
		return password;
	}

	public String decrypt(String encryptedPassword) {
		return encryptedPassword;
	}

}
