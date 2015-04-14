/*
ID: your_id_here
LANG: JAVA
TASK: test
*/
import java.io.*;
import java.util.*;

class test {
  public static void main (String [] args) throws IOException {
    // Use BufferedReader rather than RandomAccessFile; it's much faster
    BufferedReader f = new BufferedReader(new FileReader("ocr49.train"));
                                                  // input file name goes above
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ocr49_stripped.txt")));
    // Use StringTokenizer vs. readLine/split -- lots faster
						  // Get line, break into tokens
    String temp = f.readLine();
    //StringTokenizer st = new StringTokenizer(temp);
    while (temp != null) {
    StringTokenizer st = new StringTokenizer(temp);
    while (st.hasMoreTokens()) {
    		int x = Integer.parseInt(st.nextToken());
		if (x == 4 || x == 9)
    			out.println(x);
    }
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