import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A class with static methods to help decrypt a substitution cipher.
 * 
 * @author scottm
 *
 */
public class DecryptUtilities {

    private static final int NUM_ASCII_CHARS = 128; 
    private static final int SPACE_CODE = ' ';

    // List of ASCII code for frequencies of occurrence in 
    // normal text from most frequent to least frequent
    private static String  STANDARD_FREQS_STRING = 
                    " etaonisrhdlucmfwgy,pb.vkIT-AE\"SNORH'CLMBP_DGWF1x;jUYq:"
                    + "*V2J0z!?K83X94567)([]/|Q=Z#&+}{$~^`%@><\\";

    private static final char[] STANDARD_FREQS 
            = new char[STANDARD_FREQS_STRING.length()];
    
    private static final int ASSUMED_ENCRYTPED_CHARS 
            = STANDARD_FREQS_STRING.length();

    static {
        // fill STANDARD FREQS array
        assert STANDARD_FREQS.length == STANDARD_FREQS_STRING.length() 
                        : "char array and String not same length";
        for (int i = 0 ; i < STANDARD_FREQS.length; i++) {
            STANDARD_FREQS[i] = STANDARD_FREQS_STRING.charAt(i);
        }
    }
    
    
    // Like the Math class, so no one can create objects of this type.
    // We only want them to call the static methods.
    private DecryptUtilities() {

    }

    /**
     * A method to convert a file to a String.
     * When this method is called a File chooser will open 
     * to allow selection of the file. The file will be read and 
     * converted to a String. Any IO exceptions will result in
     * an empty String being returned.
     * @param fileName The name of the file to read.
     * @return A String version of the selected file. 
     * If there is a problem reading the file then an empty String is returned.
     */
    public static String convertFileToString(String fileName){
        StringBuilder sb = new StringBuilder();
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
                sb.append("\n");
            }
            sc.close();
        } catch (IOException e) {
            // if any problems occur reset sb.
            // The returned String will now be an empty String
            System.out.println("\n***** ERROR IN READING FILE ***** ");
            System.out.println("Can't find this file " 
                    + fileName + " in the current directory.");
            System.out.println("Error: " + e);
            String currentDir = System.getProperty("user.dir");
            System.out.println("Be sure " + fileName 
                    + " is in this directory: ");
            System.out.println(currentDir);
            System.out.println("Returning an empty String.");
            sb = new StringBuilder();
        }
        return sb.toString();
    }



    /**
     * Determine the initial key to decrypt a message.
     * This assumes a message was encrypted with a substitution
     * cipher and and the 128 ASCII characters. 
     * @param freqs the frequencies of characters in the 
     * encrypted text. freq[0] is the frequency of ASCII 0,
     * freq[1] is the frequency of ASCII 1 and so forth. freqs may
     * not be null and freqs.length must equal 128.
     * @return the decryption key based on the frequency 
     * analysis. The index of an element in the returned array
     * is the ASCII code for the encrypted characters and the
     * element is the probable decryption character.
     */
    public static char[] getDecryptionKey(int[] freqs){
        if (freqs == null) {
            throw new IllegalArgumentException("The parameter freqs "
                    + "cannot be null");
        } else if(freqs.length != NUM_ASCII_CHARS) {
            throw new IllegalArgumentException("The length of the "
                    + "parameter freqs must be " + NUM_ASCII_CHARS 
                    +  ". The length of the parameter you sent: " 
                    + freqs.length);
        }
        // Create a temp array with the frequencies of the chars assumed
        // to be encrypted.
        int[] tempFreqs = new int[ASSUMED_ENCRYTPED_CHARS];
        System.arraycopy(freqs, SPACE_CODE, tempFreqs, 0, tempFreqs.length);
        CharFreqPair[] rawData = new CharFreqPair[tempFreqs.length];
        for (int i = 0; i < rawData.length; i++) {
            rawData[i] = new CharFreqPair((char)(i + SPACE_CODE), 
                    freqs[i + SPACE_CODE]);
        }
        Arrays.sort(rawData);
        reverse(rawData);
        return makeResult(rawData);
    }

    // make the key to decrypt the text
    private static char[] makeResult(CharFreqPair[] rawData) {
        char[] result = new char[NUM_ASCII_CHARS];
        // Copy over the chars with codes 0 to 31. 
        // We assume those are not encrypted.
        for (int i = 0; i < SPACE_CODE; i++) {
            result[i] = (char) i;
        }
        // Create the key based on standard distribution of ASCII characters.
        // 
        // The encrypt character is rawData[i].ch.
        // The decrypt character is STANDARD_FREQS[i].
        for (int i = 0; i < rawData.length; i++) {
            result[(rawData[i].ch)] = STANDARD_FREQS[i];
        }
        // take care of ASCII code 127, DEL. Assume not used
        result[NUM_ASCII_CHARS - 1] = (char) (NUM_ASCII_CHARS - 1);
        return result;
    }

    // reverse the array
    private static void reverse(CharFreqPair[] data){
        int limit = data.length / 2;
        for (int i = 0, j = data.length - 1; i < limit; i++, j--) {
            CharFreqPair temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }
    }

    // A class to store a character and its frequency
    private static class CharFreqPair implements Comparable<CharFreqPair> {

        private char ch;
        private int freq;

        public CharFreqPair(char c, int f){
            ch = c;
            freq = f;
        }

        public int compareTo(CharFreqPair other){
            return freq - other.freq;
        }

        public boolean equals(Object other){
            if (other == null || other.getClass() != getClass()) {
                return false;
            }
            return freq == ((CharFreqPair) other).freq;
        }

        public int hashCode(){
            return freq * 1337;
        }

        public String toString() {
            return ch + ": " + freq;
        }
    }


}
