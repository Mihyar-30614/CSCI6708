import java.io.*;
import java.util.*;


public class Encrypt{
	
	public static void main(String[] args) {
		
		Scanner input 		= new Scanner(System.in);
		Boolean validCipher = false;
		Boolean validOption	= false;
		String text, secret;
		StringBuilder sb 	= new StringBuilder();
		int cipher = 0, option = 0;

		while(!validCipher){

			System.out.println("1) Cesar Cipher");
			System.out.println("2) Vigenere Cipher");
			System.out.println("3) Matrix Transportation Cipher");
			System.out.print("Choose a Cipher: ");
			try{
				cipher = input.nextInt();
				if (cipher == 1 || cipher == 2 || cipher == 3) {
					validCipher = true;
				}else {
					System.out.println("Invalid Option, please try again ...");
					System.out.println("____________________________________");
				}
			}catch(java.util.InputMismatchException e){
				System.out.println("Please enter numbers only");
				System.out.println("__________________________");
				input.nextLine();
			}
		}
		// Skip the new line
		input.nextLine();

		while(!validOption){

			System.out.println("__________________________");
			System.out.println("1) Encrypt a Text");
			System.out.println("2) Decrypt a Text");
			System.out.print("Choose an Option: ");
			try{
				option = input.nextInt();
				if (option == 1 || option == 2) {
					validOption = true;
				}else{
					System.out.println("Invalid option, please try again ...");
					System.out.println("____________________________________");
				}
			}catch (java.util.InputMismatchException e) {
				System.out.println("Please enter numbers only");
				System.out.println("__________________________");
				input.nextLine();
			}
		}

		// Consume the rest of the line
		input.nextLine();

		System.out.println("__________________________");
		System.out.print("Please Enter Your Text: ");
		text = input.nextLine();

		
		switch (cipher) {
			
			// Cesar Cipher
			case 1:
				System.out.println("__________________________");
				System.out.print("Please Enter The Key: ");
				int key = input.nextInt();

				for (int i = 0; i < text.length(); i++) {
		        	if (Character.toLowerCase(text.charAt(i)) - 'a' >= 0 && Character.toLowerCase(text.charAt(i)) - 'a' <= 25) {
		        		if (option == 1) {
		        			// Encrypt
		            		sb.append(cesarCharEncrypt(text.charAt(i), key));
		        		}else{
		        			// Decrypt
		        			sb.append(cesarCharEncrypt(text.charAt(i), -key));
		        		}
		        	}else{
		        		sb.append(text.charAt(i));
		        	}
		        }
			break;

			// Vigenere Cipher
			case 2 :
				System.out.println("__________________________");
				System.out.print("Please Enter The Key: ");
				secret = input.nextLine();

				int keyChar		 = 0;
				int keyLength	 = secret.length();

		        for (int i = 0; i < text.length(); i++) {
		        	if (Character.toLowerCase(text.charAt(i)) - 'a' >= 0 && Character.toLowerCase(text.charAt(i)) - 'a' <= 25) {
		        		if (option == 1) {
		        			// Encrypt
		            		sb.append(vigenereCharEncrypt(text.charAt(i), secret, true, keyChar));
		        		}else{
		        			// Decrypt
		        			sb.append(vigenereCharEncrypt(text.charAt(i), secret, false, keyChar));
		        		}
		            }else{
		            	sb.append(text.charAt(i));
		            }
		            keyChar = (keyChar + 1) % keyLength;
		        }
			break;

			// Matrix Transportation Cipher
			case 3:
				System.out.println("__________________________");
				System.out.print("Please Enter The Key ex.(1,3,2,4): ");
				secret = input.nextLine();

				// Remove '(' and ')'
				secret = secret.replace("(","");
				secret = secret.replace(")","");
				// Parse the array to int and find number of columns and number of rows
				String[] secrets = secret.split(",");
				int[] columns	 = new int[secrets.length];
				for (int i = 0; i< secrets.length; i++) {
					columns[i] = Integer.parseInt(secrets[i]);
				}

				int colNo	 = Arrays.stream(columns).max().getAsInt();
				int reminder = text.length()%colNo == 0? 0 : 1;
				int rowNo	 = (text.length()/colNo) + reminder;

				if (option == 1) {
					// Encrypt
					sb.append(transportCipher(text, columns, colNo, rowNo, true));
				}else{
					// Decrypt
					sb.append(transportCipher(text, columns, colNo, rowNo, false));
				}
			break;

		}

		if (option == 1) {
        	System.out.println("Your Encrypted Texted is: ");
        }else{
        	System.out.println("Your Decrypted Texted is: ");
        }

        System.out.print(sb);
	}

	// Cesar Character Cipher
	public static char cesarCharEncrypt(char c, int key){
		final int alphabet  	= 26;
		final int shift			= key % alphabet;
		final char firstLetter 	= 'a';

		// Find the location of the character
		char position = (char) (Character.toLowerCase(c) - firstLetter);
		// Deal with wrap-around both positive and negative
		position = (char) ((position + shift + alphabet) % alphabet);
		return (char) (position + firstLetter);
	}

	// Vigenere Character Cipher
	public static char vigenereCharEncrypt (char c, String key, Boolean encrypt, int keyChar){
		final int alphabet		= 26;
		final char firstLetter 	= 'a';
		int ciphered;

		// Find the location of the character
		int position 		= (int) (Character.toLowerCase(c) - firstLetter);
		// Find the location of the key character
		int keyPosition	= (int) (Character.toLowerCase(key.charAt(keyChar)) - firstLetter); 

		if(encrypt){
			ciphered	= (position + keyPosition)% alphabet;
		}else{
			ciphered	= (position - keyPosition)% alphabet;
			// wrap-around negative values
			if (ciphered < 0) {
				ciphered += 26;
			}
		}
		return (char) (ciphered + firstLetter);
	}

	// Matrix Transportation Cipher
	public static String transportCipher (String text, int[] columns, int colNo, int rowNo,  Boolean encrypt){
		StringBuilder output	= new StringBuilder();
		int index 				= 0;
		// Create 2D Arraylist to map the text to
		char[][] table = new char[rowNo][colNo];

		if (encrypt) {
			// Encrypt
			for (int i = 0; i < rowNo; i++) {
				for (int k = 0; k < colNo; k++) {
					// fill the 2D array with the text
					if (index < text.length()) {
						table[i][k]		= text.charAt(index);
					}else{
						// fill the extra positions with '%'
						table[i][k] 	= '%';
					}
					// keep track on which character are we reading
					index		+= 1;
				}
			}
			// Build the ciphered text
			for (int i = 0; i < columns.length; i++) {
				int col = columns[i] - 1;
				for (int k = 0; k < rowNo; k++) {
					output.append(table[k][col]);
				}
			}
		}else{
			// Decrypt
			for (int i = 0; i < columns.length; i++) {
				int col = columns[i] - 1;
				for (int k = 0; k < rowNo; k++) {
					table[k][col] = text.charAt(index);
					index		 += 1;
				}
			}
			// Build the Decrypted text
			for (int i = 0; i < rowNo; i++) {
				for (int k = 0; k < colNo; k++) {
					output.append(table[i][k]);
				}
			}
		}

		return output.toString();
	}
}