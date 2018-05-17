import java.io.*;
import java.util.*;

public class aclEmulator {

	public static void main(String[] args) {
		
		Scanner input 		= new Scanner(System.in);
		ArrayList<String> simple 	= new ArrayList<String>();
		ArrayList<String> extended 	= new ArrayList<String>();
		ArrayList<String> packets	= new ArrayList<String>();
        ArrayList<String> protocols = new ArrayList<String>();

        // Hardcoded protocols
        protocols.add("http tcp 80");
        protocols.add("ftp tcp 21");
        protocols.add("ssh tcp 22");
        protocols.add("snmp udp 161");

		System.out.println("Please enter ACL Rules file: ");
		String aclFile = input.nextLine();

		System.out.println("Please enter Packet File: ");
		String packetFile = input.nextLine();

		// Read ACL File and put it in an ArrayList
		readFile(aclFile, simple, extended, true, protocols);

		// Read Packet File and process it
		readFile(packetFile, simple, extended, false, protocols);

	}

	public static void readFile(String fileName, ArrayList<String> simple, ArrayList<String> extended, Boolean flag, ArrayList<String> protocols){
		try {

			// This will reference one line at a time
			String line 	= null;

            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

            	// Spliting the line based on the spaces.
           		String[] parts  = line.split(" ");
           		Boolean pass	= false;

            	// Reading rules files
            	if (flag) {
            		int id = Integer.parseInt(parts[1]);
            		// Check if this is a standard rule or extended
            		if (id < 100 && id > 0) {
            			
            			simple.add(line);

            		}else if (id > 100 && id < 200){

            			extended.add(line);

            		}else{
            			System.out.println("Invalid Rule "+ line);
            		}
            		// Reading packet file
            	}else{
            		if (parts.length == 1) {
            			// Go Standard
            			for (int i =0; i<simple.size(); i++) {

                            String rule         = simple.get(i);
                            String[] ruleParts  = rule.split(" ");
                            String result       = "";

                            if (ruleParts[3].equals("any")) {
                                result = ruleParts[2].equals("permit")? "permitted":"denied";
                                pass = true;
                                System.out.println(line + " " + result);
                                break;
                            }else {
                                if (packetHandler(parts[0], ruleParts[3], ruleParts[4])) {
                                    result = ruleParts[2].equals("permit")? "permitted":"denied";
                                    System.out.println(line + " " + result);
                                    pass = true;
                                    break;
                                }
                            }
            			}
            		}else if (parts.length >1 ) {
            			// Go extended
                        for (int i = 0; i<extended.size(); i++) {
                            
                            String rule         = extended.get(i);
                            String[] ruleParts  = rule.split(" ");
                            String result       = "";

                            if (ruleParts[5].equals("any") && ruleParts[3].equals("ip")) {
                                result = ruleParts[2].equals("permit")? "permitted":"denied";
                                pass = true;
                                System.out.println(line + " " + result);
                                break;
                            }else {                                
                                if (parts.length == 2) {
                                    if ((packetHandler(parts[0], ruleParts[4], ruleParts[5])) && (packetHandler(parts[1], ruleParts[6], ruleParts[7]))){
                                        result = ruleParts[2].equals("permit")? "permitted":"denied";
                                        pass = true;
                                        System.out.println(line + " " + result);
                                        break;
                                    }
                                }else{
                                    if ((packetHandler(parts[0], ruleParts[4], ruleParts[5])) && (packetHandler(parts[1], ruleParts[6], ruleParts[7])) && (checkProtocol(parts[2],ruleParts,protocols))) {
                                        result = ruleParts[2].equals("permit")? "permitted":"denied";
                                        pass = true;
                                        System.out.println(line + " " + result);
                                        break;
                                    }
                                }
                            }                            
                        }
            		}else {
            			System.out.println("Invalid Packet: "+line);
            		}
            		// Case there was no rule; Drop the packet
            		if (!pass) {
            			System.out.println(line + " denied");
            		}
            	}
            }   

            // Always close files.
            bufferedReader.close();      
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
	}

	public static Boolean packetHandler (String packet, String src, String mask){
        Boolean match          = true;
        String[] srcParts      = src.split("\\.");
        String[] maskParts     = mask.split("\\.");
        String[] pacParts      = packet.split("\\.");
            
        for (int j = 0; j<pacParts.length; j++) {
            if (Integer.parseInt(maskParts[j]) == 0 && Integer.parseInt(srcParts[j]) != Integer.parseInt(pacParts[j])) {
                match = false;
                break;
            }
        }

		return match;
	}

    public static Boolean checkProtocol (String packet, String[] parts, ArrayList<String> protocols){
        Boolean match = false;

        if (packet.equals("") && parts[3].equals("ip")) {
            match = true;
        }else {
            
            for (int k = 0; k < protocols.size(); k++) {
                
                String protocol         = protocols.get(k);
                String[] protocolParts  = protocol.split(" ");

                if (packet.equals(protocolParts[0]) && parts[3].equals(protocolParts[1]) && parts[9].equals(protocolParts[2])) {
                    match = true;
                    break;
                }
            }
        }

        return match;
    }
}