import java.util.*;
import java.io.*;

public class ErrorChecker {
	
	public static void main(String[] args) throws IOException  {
	    // Use BufferedReader rather than RandomAccessFile; it's much faster
	    BufferedReader f = new BufferedReader(new FileReader("./src/data/unstripper_17"));
	                                                  // input file name goes above
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./src/data/stripped_17.txt")));
	    PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("./src/data/ocr17.test")));
	    out1.print("Hello World!");
	    // Use StringTokenizer vs. readLine/split -- lots faster
							  // Get line, break into tokens
	    String temp = f.readLine();
	    //StringTokenizer st = new StringTokenizer(temp);
	    while (temp != null) {
	    StringTokenizer st = new StringTokenizer(temp);
	    for (int i = 0; i < 14*14; i++) {
	    	int x = Integer.parseInt(st.nextToken());
	    	out1.print(x);
	    	out1.print(" ");
	    }
	    out1.println();
	    int x = Integer.parseInt(st.nextToken());
	    out.println(x);
	    temp = f.readLine();
	    }
	  /*
	    int i1 = Integer.parseInt(st.nextToken());    // first integer
	    int i2 = Integer.parseInt(st.nextToken());    // second integer
	    out.println(i1+i2);                           // output result
	  */
	    out.close();                                  // close the output file
	    System.exit(0);                               // don't omit this!
	}

}
