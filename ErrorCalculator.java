import java.util.*;
import java.io.*;

public class ErrorCalculator {
	public static void main(String[] args) throws IOException  {
	    // Use BufferedReader rather than RandomAccessFile; it's much faster
	    BufferedReader f = new BufferedReader(new FileReader("./src/data/stripped_17.txt"));
	    BufferedReader f2 = new BufferedReader(new FileReader("./src/data/ocr17.testout"));
	                                                  // input file name goes above
	    // Use StringTokenizer vs. readLine/split -- lots faster
							  // Get line, break into tokens
	    double sum = 0;
	    int num = 0;
	    String temp1 = f.readLine();
	    String temp2 = f2.readLine();
	    //StringTokenizer st = new StringTokenizer(temp);
	    while (temp1 != null && temp2 != null) {
	    int x = Integer.parseInt(temp1);
	    int y = Integer.parseInt(temp2);
	    if (x != y)
	    	sum++;
	    num++;
	    temp1 = f.readLine();
	    temp2 = f2.readLine();
	    }
	    
	    System.out.println(sum/num);
	    
	  /*
	    int i1 = Integer.parseInt(st.nextToken());    // first integer
	    int i2 = Integer.parseInt(st.nextToken());    // second integer
	    out.println(i1+i2);                           // output result
	  */                    // close the output file
	    System.exit(0);                               // don't omit this!
	}

}