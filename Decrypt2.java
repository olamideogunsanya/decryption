import java.util.Scanner;
import java.util.Arrays;




public class Decrypt2 {
	

		// CS312 students, add your constants here
		final static int LOWASCIIVAL = 32;
		final static int HIGHASCIIVAL = 126;
		final static int ENCRYPTED = 1;
		final static int DECRYPTED = 2;
		final static int FINAL = 3;
		final static int TOTALASCII = 128;

		public static void main(String[] arg) {
			// CS312 Students, do not create any other Scanners connected to System.in
			Scanner keyboard = new Scanner(System.in);
			String fileName = getFileName(keyboard);
			String encryptedText = DecryptUtilities.convertFileToString(fileName);

			// The other method from DecryptUtilities you will have to use is
			// DecryptUtilities.getDecryptionKey(int[]), but first you need to
			// create an array with the frequency of all the ASCII characters in the
			// encrypted text. Count ALL characters from ASCII code 0 to ASCII code 127

			// CS312 students, add you code here.
			int[] ASCII = frequencyAnalysis(encryptedText);
			char[] key = DecryptUtilities.getDecryptionKey(ASCII);
			String decryptedText = decodeText(encryptedText, key);
			printInformation(ASCII, key, encryptedText);
			printText(decryptedText, DECRYPTED);
			decryptedText = reEncryptText(keyboard, encryptedText, key, decryptedText);
			printFinalizedInfo(decryptedText, key);

			keyboard.close();
		}

		// CS312 students, add your methods here
		/*
		 * This function analysizes the text char by char and counts them in an array
		 * 
		 * @param encryptedText this is the text that is analyzed
		 * 
		 * @returns array of ints that is counters for the each ascii character
		 */
		public static int[] frequencyAnalysis(String encryptedText) {
			int[] ASCII = new int[TOTALASCII];
			int length = encryptedText.length();
			int posValue;
			char posChar;
			for (int pos = 0; pos < length; pos++) {
				posChar = encryptedText.charAt(pos);

				posValue = (int) posChar;
				ASCII[posValue]++;
			}
			return ASCII;
		}

		/*
		 * This function takes an encrypted text and key and decrypts the texts
		 * 
		 * @param encryptedText this is the encrypted text that will be decoded
		 * 
		 * @param key this is the key that is used to be decoded
		 * 
		 * @returns a String that is the decrypted text based on the key given
		 */
		public static String decodeText(String encryptedText, char[] key) {
			String decryptedText = "";
			char charPos;
			int charVal;
			char deChar;
			for (int pos = 0; pos < encryptedText.length(); pos++) {
				charPos = encryptedText.charAt(pos);
				charVal = (int) charPos;
				deChar = key[charVal];
				decryptedText += deChar;

			}

			return decryptedText;
		}

		/*
		 * This function prints the frequency of different ASCII characters based on the
		 * text
		 * 
		 * @param ASCIIFrequency array that holds the frequencys of different characters
		 */
		public static void printFrequency(int[] ASCIIFrequency) {
			char ASCIIChar;
			System.out.println("Frequencies of characters.");
			System.out.println("Character - Frequency");
			for (int ASCIIVal = LOWASCIIVAL; ASCIIVal <= HIGHASCIIVAL; ASCIIVal++) {
				ASCIIChar = (char) ASCIIVal;
				System.out.println(ASCIIChar + " - " + ASCIIFrequency[ASCIIVal]);
			}
			System.out.println();
		}

		/*
		 * this function print the key in the format encrypted character #, decrypted
		 * character: #
		 * 
		 * @param key this is the key that was used for decoding and is printed here
		 */
		public static void printKey(char[] key) {
			char ASCIIChar;
			System.out.println("The current version of the key for ASCII characters 32 to 126 is: ");
			for (int ASCIIVal = LOWASCIIVAL; ASCIIVal <= HIGHASCIIVAL; ASCIIVal++) {
				ASCIIChar = (char) ASCIIVal;
				System.out.println("Encrypt character: " + ASCIIChar + ", decrypt character: " + key[ASCIIVal]);
			}
			System.out.println();
		}

		/*
		 * this function is used as a general text printer with the diff situations
		 * needed
		 * 
		 * @param text this is the text that is printed
		 * 
		 * @param version this number is used to tell the what text header to print
		 */
		public static void printText(String text, int version) {
			if (version == ENCRYPTED) {
				System.out.println("The encrypted text is: ");
			} else if (version == DECRYPTED) {
				System.out.println("The current version of the decrypted text is: ");
				System.out.println();
			} else if (version == FINAL) {
				System.out.println("The final version of the decrypted text is: ");
				System.out.println();
			}
			System.out.println(text);

		}

		/*
		 * this function is used to change the key based on the what the user wants to
		 * change
		 * 
		 * @param key this is the key that is change in this function
		 * 
		 * @param input this is used to get user input
		 * 
		 * @return the new key with the changes made
		 */
		public static char[] changeDecryptionKey(char key[], Scanner input) {
			char decryptChar = getDecryptChar(input);
			char newKeyChar = getCharChange(decryptChar, input);
			int indexDCChar = getIndexOf(key, decryptChar);
			int indexNKChar = getIndexOf(key, newKeyChar);
			if(indexDCChar == -1 || indexNKChar == -1)
				return key;
			key[indexDCChar] = newKeyChar;
			key[indexNKChar] = decryptChar;
			System.out.println(decryptChar + "'s will now decrypt to " + newKeyChar + "'s and vice versa.");
			System.out.println();

			return key;
		}

		/*
		 * This function is used to get the decrypt character the user wants to change
		 * 
		 * @param input this is used to get user input
		 * 
		 * @returns character the user wants to change
		 */
		public static char getDecryptChar(Scanner input) {
			System.out.print("Enter the decrypt character you want to change: ");
			return input.nextLine().charAt(0);
		}

		/*
		 * this function is used to get the character that will be replacing the char in
		 * the key
		 * 
		 * @param decryptChar this is the char that will be initially changed
		 * 
		 * @param input this function is used to get user input
		 * 
		 * @returns character the user that is going to be change with
		 */
		public static char getCharChange(char decryptChar, Scanner input) {
			System.out.print("Enter what the character " + decryptChar + " should decrypt to instead: ");
			return input.nextLine().charAt(0);
		}

		/*
		 * This function returns the index of target in char array
		 * 
		 * @param key this is where the target is searched for
		 * 
		 * @param target this is what is searched for
		 * 
		 * @returns the index of the target
		 */
		public static int getIndexOf(char[] key, char target) {
			for (int pos = 0; pos < key.length; pos++) {
				if (key[pos] == target) {
					return pos;
				}
			}
			return -1;
		}

		/*
		 * This function is used to ask if the user wants to change the key
		 * 
		 * @param input this is to get user input
		 * 
		 * @returns true or false based on if want to change something
		 */
		public static boolean getChangeKey(Scanner input) {
			System.out.println("Do you want to make a change to the key?");
			System.out.print("Enter 'Y' or 'y' to make change: ");
			String answer = "" + input.nextLine().charAt(0);
			answer = answer.toLowerCase();
			if (answer.equals("y")) {
				return true;
			}
			return false;
		}

		/*
		 * This function is used to ask if user wants to change key and changes and
		 * decryptes
		 * 
		 * @param input this is used to get user input
		 * 
		 * @param encryptedText this is the text that is encrypted
		 * 
		 * @param key this is the key that is changed
		 * 
		 * @param decryptedText This is the text that is decrypted
		 * 
		 * @returns decrypted text
		 */
		public static String reEncryptText(Scanner input, String encryptedText, char[] key, String decryptedText) {
			boolean continueToChangeKey = getChangeKey(input);
			
			while (continueToChangeKey) {
				key = changeDecryptionKey(key, input);
				decryptedText = decodeText(encryptedText, key);
				printText(decryptedText, DECRYPTED);
				continueToChangeKey = getChangeKey(input);
			}
			return decryptedText;
		}

		/*
		 * This function is used to print the info the first time the program is ran
		 * 
		 * @param frequency this is the frequency that is used print
		 * 
		 * @param key this is the key that is print here
		 * 
		 * @param text this text is printed here
		 */
		public static void printInformation(int[] frequency, char[] key, String text) {
			printText(text, ENCRYPTED);
			printFrequency(frequency);
			printKey(key);
		}

		/*
		 * This function is used to print the finalized decrypted text
		 * 
		 * @param finalText this is final decrypted text that is printed here
		 * 
		 * @param key this key is printed here
		 */
		public static void printFinalizedInfo(String finalText, char[] key) {
			System.out.println();
			printKey(key);
			printText(finalText, FINAL);
		}

		// get the name of file to use
		public static String getFileName(Scanner kbScanner) {
			System.out.print("Enter the name of the encrypted file: ");
			String fileName = kbScanner.nextLine().trim();
			System.out.println();
			return fileName;
		}

}

