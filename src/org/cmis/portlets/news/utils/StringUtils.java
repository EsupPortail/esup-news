package org.cmis.portlets.news.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * created by Anyware Services - Delphine Gavalda.
 * 
 * 20 mai 2010
 */
public class StringUtils {

    /** index of the first character with accent. **/
    private static final int MIN = 192;
    
    /** index of the last character with accent. **/
    private static final int MAX = 255;
    
    /** Map of characters. **/
    private static final List<String> MAP = initMap();

    private StringUtils() {
	//nothing
    }
    
    /**
     * Initialize correspondence table for accents characters.
     */
    private static List<String> initMap() {
	List<String> result = new ArrayList<String>();
	java.lang.String car = null;

	car = new java.lang.String("A");
	/* '\u00C0' À alt-0192 */
	result.add(car); 
	/* '\u00C1' Á alt-0193 */
	result.add(car); 
	/* '\u00C2' Â alt-0194 */
	result.add(car); 
	/* '\u00C3' Ã alt-0195 */
	result.add(car); 
	/* '\u00C4' Ä alt-0196 */
	result.add(car);
	/* '\u00C5' Å alt-0197 */
	result.add(car); 
	car = new java.lang.String("AE");
	/* '\u00C6' Æ alt-0198 */
	result.add(car); 
	car = new java.lang.String("C");
	/* '\u00C7' Ç alt-0199 */
	result.add(car); 
	car = new java.lang.String("E");
	/* '\u00C8' È alt-0200 */
	result.add(car); 
	/* '\u00C9' É alt-0201 */
	result.add(car); 
	/* '\u00CA' Ê alt-0202 */
	result.add(car); 
	/* '\u00CB' Ë alt-0203 */
	result.add(car); 
	car = new java.lang.String("I");
	/* '\u00CC' Ì alt-0204 */
	result.add(car); 
	/* '\u00CD' Í alt-0205 */
	result.add(car); 
	/* '\u00CE' Î alt-0206 */
	result.add(car); 
	/* '\u00CF' Ï alt-0207 */
	result.add(car); 
	car = new java.lang.String("D");
	/* '\u00D0' Ð alt-0208 */
	result.add(car); 
	car = new java.lang.String("N");
	/* '\u00D1' Ñ alt-0209 */
	result.add(car); 
	car = new java.lang.String("O");
	/* '\u00D2' Ò alt-0210 */
	result.add(car); 
	/* '\u00D3' Ó alt-0211 */
	result.add(car); 
	 /* '\u00D4' Ô alt-0212 */
	result.add(car);
	/* '\u00D5' Õ alt-0213 */
	result.add(car); 
	/* '\u00D6' Ö alt-0214 */
	result.add(car); 
	car = new java.lang.String("*");
	/* '\u00D7' × alt-0215 */
	result.add(car); 
	car = new java.lang.String("0");
	/* '\u00D8' Ø alt-0216 */
	result.add(car); 
	car = new java.lang.String("U");
	 /* '\u00D9' Ù alt-0217 */
	result.add(car);
	/* '\u00DA' Ú alt-0218 */
	result.add(car); 
	/* '\u00DB' Û alt-0219 */
	result.add(car); 
	/* '\u00DC' Ü alt-0220 */
	result.add(car); 
	car = new java.lang.String("Y");
	/* '\u00DD' Ý alt-0221 */
	result.add(car); 
	car = new java.lang.String("Þ");
	/* '\u00DE' Þ alt-0222 */
	result.add(car); 
	car = new java.lang.String("B");
	/* '\u00DF' ß alt-0223 */
	result.add(car); 
	car = new java.lang.String("a");
	/* '\u00E0' à alt-0224 */
	result.add(car); 
	/* '\u00E1' á alt-0225 */
	result.add(car); 
	/* '\u00E2' â alt-0226 */
	result.add(car); 
	/* '\u00E3' ã alt-0227 */
	result.add(car); 
	/* '\u00E4' ä alt-0228 */
	result.add(car); 
	/* '\u00E5' å alt-0229 */
	result.add(car); 
	car = new java.lang.String("ae");
	/* '\u00E6' æ alt-0230 */
	result.add(car); 
	car = new java.lang.String("c");
	/* '\u00E7' ç alt-0231 */
	result.add(car); 
	car = new java.lang.String("e");
	/* '\u00E8' è alt-0232 */
	result.add(car); 
	/* '\u00E9' é alt-0233 */
	result.add(car); 
	/* '\u00EA' ê alt-0234 */
	result.add(car); 
	/* '\u00EB' ë alt-0235 */
	result.add(car); 
	car = new java.lang.String("i");
	/* '\u00EC' ì alt-0236 */
	result.add(car); 
	/* '\u00ED' í alt-0237 */
	result.add(car);
	/* '\u00EE' î alt-0238 */
	result.add(car); 
	/* '\u00EF' ï alt-0239 */
	result.add(car); 
	car = new java.lang.String("d");
	/* '\u00F0' ð alt-0240 */
	result.add(car); 
	car = new java.lang.String("n");
	/* '\u00F1' ñ alt-0241 */
	result.add(car); 
	car = new java.lang.String("o");
	/* '\u00F2' ò alt-0242 */
	result.add(car); 
	/* '\u00F3' ó alt-0243 */
	result.add(car); 
	/* '\u00F4' ô alt-0244 */
	result.add(car); 
	/* '\u00F5' õ alt-0245 */
	result.add(car); 
	/* '\u00F6' ö alt-0246 */
	result.add(car); 
	car = new java.lang.String("/");
	/* '\u00F7' ÷ alt-0247 */
	result.add(car); 
	car = new java.lang.String("0");
	 /* '\u00F8' ø alt-0248 */
	result.add(car);
	car = new java.lang.String("u");
	/* '\u00F9' ù alt-0249 */
	result.add(car); 
	/* '\u00FA' ú alt-0250 */
	result.add(car); 
	/* '\u00FB' û alt-0251 */
	result.add(car); 
	/* '\u00FC' ü alt-0252 */
	result.add(car); 
	car = new java.lang.String("y");
	/* '\u00FD' ý alt-0253 */
	result.add(car); 
	car = new java.lang.String("þ");
	/* '\u00FE' þ alt-0254 */
	result.add(car); 
	car = new java.lang.String("y");
	 /* '\u00FF' ÿ alt-0255 */
	result.add(car);
	/* '\u00FF' alt-0255 */
	result.add(car); 

	return result;
    }

    /**
     * Remove accents from a string and replace spaces.
     * 
     * @param str
     *            String to convert
     * @return String
     **/
    public static java.lang.String getValidFileName(final java.lang.String str) {
	java.lang.StringBuffer validName = new StringBuffer(str);

	for (int bcl = 0; bcl < validName.length(); bcl++) {
	    int carVal = str.charAt(bcl);
	    if (carVal >= MIN && carVal <= MAX) {
		java.lang.String newVal = MAP.get(carVal - MIN);
		validName.replace(bcl, bcl + 1, newVal);
	    }
	}

	String res = validName.toString();

	res = res.replace(" ", "_");
	res = res.replaceAll("[', `, :, ?, <, >, *, |, /]", "");

	return res;
    }
}
