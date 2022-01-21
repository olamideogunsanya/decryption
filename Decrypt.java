import java.util.Arrays;
import java.util.Scanner;

public class Decrypt {

	final static int ALLVALUES = 128;
	final static int MIN = 32;
	final static int MAX = 126;
	final static int DECRYPTED = 2;
	final static int ENCRYPTED = 1;
	final static int FINAL_DECRYPTED = 3;
	final static int NONEXISTENT = -1;

	public static void main(String[] arg) {
		Scanner keyboard = new Scanner(System.in);
		String fileName = getFileName(keyboard);
		String encryptedText = DecryptUtilities.convertFileToString(fileName);
		executeProgram(keyboard, encryptedText);
		keyboard.close();
	}

	public static void executeProgram(Scanner keyboard, String encryptedText) {
		int[] ASCII = freqAnalysis(encryptedText);
		char[] key = DecryptUtilities.getDecryptionKey(ASCII);
		String decryptedText = decoder(encryptedText, key);
		printChangingInfo(ASCII, key, encryptedText);
		displayText(decryptedText, DECRYPTED);
		decryptedText = changeChars(keyboard, encryptedText, key, decryptedText);
		printFinalInfo(decryptedText, key);
	}

	public static int[] freqAnalysis(String encryptedText) {
		int ASCII[] = new int[ALLVALUES];
		int totalLength = encryptedText.length();
		char testChar;
		int testValue;
		for (int pos = 0; pos < totalLength; pos++) {
			testChar = encryptedText.charAt(pos);
			testValue = (int) testChar;
			ASCII[testValue]++;
		}
		return ASCII;
	}

	public static void displayFrequency(int[] frequency) {
		char character;
		System.out.println("Frequencies of characters.");
		System.out.println("Character - Frequency");
		for (int ASCIIVal = MIN; ASCIIVal <= MAX; ASCIIVal++) {
			character = (char) ASCIIVal;
			System.out.println(character + " - " + frequency[ASCIIVal]);
		}
		System.out.println();
	}

	public static String decoder(String encryptedText, char[] key) {
		String decryptedText = "";
		char charPos;
		char decChar;
		int charVal;
		for (int position = 0; position < encryptedText.length(); position++) {
			charPos = encryptedText.charAt(position);
			charVal = (int) charPos;
			decChar = key[charVal];
			decryptedText += decChar;
		}
		return decryptedText;
	}

	public static void displayKey(char[] key) {
		char ASCIICharacter;
		System.out.println("The current version of the key for ASCII characters 32 to 126 is: ");
		for (int ASCIIVal = MIN; ASCIIVal <= MAX; ASCIIVal++) {
			ASCIICharacter = (char) ASCIIVal;
			System.out.println("Encrypt character: " + ASCIICharacter + ", decrypt character: "
			+ key[ASCIIVal]);
		}
		System.out.println();
	}

	public static char[] changeKey(char key[], Scanner keyboard) {
		System.out.print("Enter the decrypt character you want to change: ");
		char decChar = keyboard.nextLine().charAt(0);
		System.out.print("Enter what the character " + decChar + " should decrypt to instead: ");
		char newChar = keyboard.nextLine().charAt(0);
		int indexOfNewChar = getIndex(key, newChar);
		int indexOfDecryptChar = getIndex(key, decChar);
		if (indexOfDecryptChar == -1 || indexOfNewChar == -1) {
			return key;
	}
		key[indexOfDecryptChar] = newChar;
		key[indexOfNewChar] = decChar;
		System.out.println(decChar + "'s will now decrypt to " + newChar + "'s and vice versa.");
		System.out.println();
		return key;
	}

	public static boolean makeAChange(Scanner keyboard) {
		System.out.println("Do you want to make a change to the key?");
		System.out.print("Enter 'Y' or 'y' to make change: ");
		String response = "" + keyboard.nextLine().charAt(0);
		response = response.toLowerCase();
		if (response.equals("y")) {
			return true;
		}
		return false;
	}

	public static int getIndex(char[] key, char goal) {
		for (int position = 0; position < key.length; position++) {
			if (key[position] == goal) {
				return position;
			}
		}
		return NONEXISTENT;
	}

	public static void displayText(String text, int type) {
		if (type == ENCRYPTED) {
			System.out.println("The encrypted text is:");
		} else if (type == DECRYPTED) {
			System.out.println("The current version of the decrypted text is: ");
			System.out.println();
		} else if (type == FINAL_DECRYPTED) {
			System.out.println("The final version of the decrypted text is: ");
			System.out.println();
		}
		System.out.println(text);
		
	}
	
	public static String changeChars(Scanner input, String encryptedText, char[] key,
			String decryptedText) {
		boolean update = makeAChange(input);
		while (update) {
			key = changeKey(key, input);
			decryptedText = decoder(encryptedText, key);
			displayText(decryptedText, DECRYPTED);
			update = makeAChange(input);
		}
		return decryptedText;
	}

	public static void printChangingInfo(int[] frequency, char[] key, String text) {
		displayText(text, ENCRYPTED);
		displayFrequency(frequency);
		displayKey(key);
	}

	public static void printFinalInfo(String finalText, char[] key) {
		System.out.println();
		displayKey(key);
		displayText(finalText, FINAL_DECRYPTED);
	}

	public static String getFileName(Scanner kbScanner) {
		System.out.print("Enter the name of the encrypted file: ");
		String fileName = kbScanner.nextLine().trim();
		System.out.println();
		return fileName;
	}
}
