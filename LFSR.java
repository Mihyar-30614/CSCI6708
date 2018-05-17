import java.util.*;
import java.io.*;

public class LFSR{
	
	public static void main(String[] args) {
		
		Scanner input		= new Scanner(System.in);
		StringBuilder sb 	= new StringBuilder();
		int registerLen, clockCycles;

		System.out.print("Please Enter Length of the shift register: ");
		registerLen		= input.nextInt();
		int[] register = new int[registerLen];
		// Consume the rest of the line
		input.nextLine();

		System.out.print("Please Enter Tap Positions: ");
		String tap 		= input.nextLine();
		String[] taps   = tap.split(" ");
		int[] tapPos 	= new int[taps.length];

		for (int i = 0; i < taps.length; i++) {
			tapPos[i] = Integer.parseInt(taps[i]);
		}

		System.out.print("Please Enter Seed Value: ");
		String seedStr 		= input.nextLine();
		String[]seedsplit 	= seedStr.split(" ");

		int revIndex = registerLen - 1;
		for (int i = 0; i < registerLen; i++) {
			register[i] = Integer.parseInt(seedsplit[revIndex]);
			revIndex--;
		}

		System.out.print("Please Enter Number of Clock Cycles: ");
		clockCycles		= input.nextInt();
		// Consume the rest of the line
		input.nextLine();

		// Loop to the number of cycles
		for (int t = 0; t < clockCycles; t++) {
			int last   = register[registerLen - 1];
			int next   = register[registerLen - 1];
			// Compute the new bit
			for (int k = taps.length -1; k >= 0; k--) {
				int pos = tapPos[k];
				// Ignore if position is at the end
				if (!(pos == registerLen - 1)) {
					// In case there was more than one tap
					next = (next ^ register[pos]);
				}
			}

			// Shift to the right
			// System.out.print(register);
			for (int j = registerLen -1 ; j > 0; j--) {
				register[j] = register[j-1];
			}
			// Put the next bit in the first position
			register[0]	= next;
			sb.append(last);
		}

		System.out.print("The output is: "+ sb);
	}
}