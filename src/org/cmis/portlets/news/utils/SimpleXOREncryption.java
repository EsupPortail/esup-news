package org.cmis.portlets.news.utils;

/**
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 16 juin 2010
 * 
 * A very simple way to perform data encryption/decryption without using a
 * {@link Key} or a {@link Certificate}
 */
public final class SimpleXOREncryption {
    // The key to be used for both encryption and decryption
    private static final int KEY = 129;

    /** Define a private constructor. */
    private SimpleXOREncryption() {
	// nothing
    }

    /**
     * Return the string parameter encrypted.
     * @param textToEncrypt
     * @return String
     */
    public static String encryptDecrypt(final String textToEncrypt) {
	StringBuilder inSb = new StringBuilder(textToEncrypt);
	StringBuilder outSb = new StringBuilder(textToEncrypt.length());
	char c;
	for (int i = 0; i < textToEncrypt.length(); i++) {
	    c = inSb.charAt(i);
	    c = (char) (c ^ KEY);
	    outSb.append(c);
	}
	return outSb.toString();
    }
}
