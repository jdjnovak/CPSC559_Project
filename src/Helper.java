import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.net.InetAddress;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
  Helper.java
  - This class is responsible for defining all helper functions
    required to communicate with the registry and peers
 */
public class Helper {
    /*  no params.
        get current date and time
        returns: formatted date
    */
    public static String getFormattedDate() {
        // Generate the necessary format for the date YYYY-MM-DD HH:MM:SS
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        // Apply format to current date/time and return
        return fmt.format(date);
    }

    /* param: a path to the wanted directory
         Reads a directory and returns a list of files in that directory
       returns: An array of strings corresponding to files in the given directory
     */
    private static String[] listFiles(String path) {
        String[] files;
        File d = new File(path);
        files = d.list();
        return files;
    }

    /*  param: file path as a string
          Reads a file line by line and concatenates the contents to an empty string.
        returns: file contents as a long string OR an error string in case of an Exception.
    */
    private static String getFileContents(String path) {
        try {
            String returnString = "";
            File file = new File(path);                                         // open file using path
            Scanner reader = new Scanner(file);                                 // init file reader
            while (reader.hasNextLine()) {                                      // while there is content to read,
                returnString = returnString + reader.nextLine() + "\n";         // concat to returnString
            }
            return returnString;                                                // return resulting string
        } catch (FileNotFoundException ferr) {                                  // exception handling
            System.out.println("Error reading file " + path + ".");             // tell client there was an issue reading file
            return "Error: Cannot read file " + path + "\n";                    // return error string for server
        }
    }

    /* params: file path as a string
         Reads all files in a directory and returns their contents as a string
       returns: string containing all contents of the files in the given directory
     */
    public static String printAllFiles(String path) {
        String[] files = listFiles(path);
        String content = "";
        for (String file : files) {
            File f = new File(path + file);
            // If file is a directory, recurse 
            if (f.isDirectory()) {
                content = content + printAllFiles(path + file + "/");
            } else {
                content = content + "== " + file + " ==\n";
                content = content + getFileContents(path + file) + "\n";
            }
        }
        return content;
    }

	/* params: byte array
	     Returns a string from the given byte array
	   returns: string
	 */
	public static String data(byte[] a) {
		if (a == null) return null;
		StringBuilder returnString = new StringBuilder();
		int i = 0;
		while (a[i] != 0) {
			returnString.append((char) a[i]);
			i++;
		}
		return returnString.toString();
	}

	/* params: none
         Get the public IP
       returns: int
	 */
	public static String getPublicIP() {
		// Got this from: https://geeksforgeeks.org/java-program-find-ip-address-computer/
        String address = "";
		try {
			URL url = new URL("http://bot.whatismyipaddress.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			address = br.readLine().trim();
			return address;
		} catch (Exception e) {
			Main.log.Warn("Failed to retrieve public IP");
			return "";
		}
	}
}
