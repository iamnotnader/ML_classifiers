   import java.io.*;
   import java.util.*;
	
	/**
	 * This is the class for an extremely simple learning algorithm that
	 * finds the most frequent class in the training data, and then
	 * predicts that each new test example belongs to this class.
	 */
   public class DecisionTreeClassifier implements Classifier {
      
      private String author = "Nader Al-Naji";
      private String description = "Yay Adaboost algorithm using decision stumps as weak learners!";
      
      private BinaryDataSet my_data;
      boolean[] exampleBitmap;
      boolean[] attributeBitmap;
      DecisionTree tree;   
     
       /**
        * This constructor takes as input a dataset and computes and
        * stores the most frequent class
        */
      public DecisionTreeClassifier(DataSet d) {
         my_data = (BinaryDataSet)d;
         exampleBitmap = new boolean[my_data.numTrainExs];
         attributeBitmap = new boolean[my_data.numAttrs];    
      
         tree = constructTree(1, 0);
      }
   
      public DecisionTree constructTree(int defaultClass, int xxx) 
      {
         if (numExamplesLeft() == 0) {
		 	//System.out.println("no exs left");
            return new DecisionTree(null, null, -1, defaultClass);
         } 
         else if (numSameExamples() == numExamplesLeft()) {
		 	//System.out.println("all same");
            return new DecisionTree(null, null, -1, majorityExample());			
         } 
         else if (numAttrsLeft() == 0) {
		 	//System.out.println("no attrs left");
            return new DecisionTree(null, null, -1, majorityExample());			
         } 
         else {
            boolean[] oldExMap = new boolean[my_data.numTrainExs];
            int best = chooseBestAttributes();
			//System.out.println("X: " + xxx + " Best: " + best);
            DecisionTree tree = new DecisionTree(null, null, best, -1);
            int m = majorityExample();
            for (int i = 0; i < 2; i++) {
               for (int j = 0; j < my_data.numTrainExs; j++)
                  oldExMap[j] = exampleBitmap[j];
               for (int j = 0; j < my_data.numTrainExs; j++) {
                  attributeBitmap[best] = true;
                  if (my_data.trainEx[j][best] != i)
                     exampleBitmap[j] = true;
               }
               if (i == 0) {
			   	  //System.out.println("left");
                  tree.left = constructTree(m, xxx + 1);
               } 
               else {
			   	  //System.out.println("right");
                  tree.right = constructTree(m, xxx + 1);
               }
               attributeBitmap[best] = false;
               for (int j = 0; j < my_data.numTrainExs; j++) {
                  if (my_data.trainEx[j][best] != i
                  &&  !oldExMap[j])
                     exampleBitmap[j] = false;
               }
            }
            return tree;		
         }
      } 
    
      public int chooseBestAttributes()
      {
         double[][] p = new double[my_data.numAttrs][2];
         double[][] n = new double[my_data.numAttrs][2];
         for (int i = 0; i < my_data.numAttrs; i++) {
            if (!attributeBitmap[i]) {
               for (int j = 0; j < my_data.numTrainExs; j++) {
                  if (my_data.trainEx[j][i] == 0
                  &&  !exampleBitmap[j]) {
                     if (my_data.trainLabel[j] == 0) {
                        n[i][0]++;
                     } 
                     else {
                        p[i][0]++;
                     }               
                  } 
                  else if (my_data.trainEx[j][i] == 1
                   &&  !exampleBitmap[j]) {
                     if (my_data.trainLabel[j] == 0) {
                        n[i][1]++;
                     } 
                     else {
                        p[i][1]++;
                     }   
                  } 
               }
            }
         }
         double r = 0.0;
         double min = Double.MAX_VALUE;
         int index = -1;
         for (int i = 0; i < my_data.numAttrs; i++) {
            if (!attributeBitmap[i]) {
               r = (p[i][0] + n[i][0])*Info(p[i][0], n[i][0]) + (p[i][1] + n[i][1])*Info(p[i][1], n[i][1]);
            //System.out.println(p[i][0] + " " + n[i][0] + " -- " + p[i][1] + " " + n[i][1]);
            //System.out.println(r);
            //System.out.println();
               if (r < min) {
                  min = r;
                  index = i;
               }
            }
         }
       
         return index;
      }
   
      public double log2(double num)
      {
         return (Math.log(num)/Math.log(2));
      }
   	     
      public double Info(double p, double n)
      {
         if (p == 0 || n == 0)
            return 1;
         return -((p/(p+n))*log2(p/(p+n)) + (n/(p+n))*log2(n/(p+n)));
      }
     
    
      public int numAttrsLeft()
      {
         int total = 0;
         for (int i = 0; i < attributeBitmap.length; i++)
            if (!attributeBitmap[i])
               total++;
         return total;
      }
      public int numExamplesLeft()
      {
         int total = 0;
         for (int i = 0; i < exampleBitmap.length; i++)
            if (!exampleBitmap[i])
               total++;
         return total;
      }
      public int majorityExample()
      {
         int[] count = new int[2];
         for (int i = 0; i < exampleBitmap.length; i++) {
            if (!exampleBitmap[i]) {
               if (my_data.trainLabel[i] == 0)
                  count[0]++;
               else
                  count[1]++;
            }
         }
         return (count[0] > count[1])?0:1;
      
      }
      public int numSameExamples()
      {
         int[] count = new int[2];
         for (int i = 0; i < exampleBitmap.length; i++) {
            if (!exampleBitmap[i]) {
               if (my_data.trainLabel[i] == 0)
                  count[0]++;
               else
                  count[1]++;
            }
         }
         return (count[0] > count[1])?count[0]:count[1];
      }    
   
       /** The prediction method ignores the given example and predicts
        * with the most frequent class seen during training.
        */
      public int predict(int[] ex) {
	  	 DecisionTree temp = tree;
	  	 while (true)
		 {
		 	if (temp.attribute == -1) {
				return temp.label;
			}
			if (ex[temp.attribute] == 0) {
				if (temp.left == null)
					return temp.label;
				temp = temp.left;
			}
			else {
				if (temp.right == null)
					return temp.label;
				temp = temp.right;
			}
		 }
         //return 0;
      }
      
       /** This method returns a description of the learning algorithm. */
      public String algorithmDescription() {
         return description;
      }
      
       /** This method returns the author of this program. */
      public String author() {
         return author;
      }
      
       /** A simple main for testing this algorithm.  This main reads a
        * filestem from the command line, runs the learning algorithm on
        * this dataset, and prints the test predictions to filestem.testout.
        */
      public static void main(String argv[])
         throws FileNotFoundException, IOException {
         /*
         if (argv.length < 1) {
            System.err.println("argument: filestem");
            return;
         }
         */
         String filestem = "./data/ocr17";//argv[0];
         
         DataSet d = new BinaryDataSet(filestem);
         
         Classifier c = new DecisionTreeClassifier(d);
         
            //System.out.println("funfunfunfun");
         d.printTestPredictions(c, filestem);
      }
      
      
      private class DecisionTree
      {	
         int attribute;
         DecisionTree left;
         DecisionTree right;
         int label;
         public DecisionTree()
         {
            attribute = -1;
            left = right = null;
            label = -1;
         }
         public DecisionTree(DecisionTree l, DecisionTree r, int attr, int lab)
         {
            attribute = attr;
            left = l;
            right = r;
            label = lab;
         }
      
      }
   }
