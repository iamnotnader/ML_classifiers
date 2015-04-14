	   import java.io.*;
	   import java.util.*;
	
	/**
	 * This is the class for an extremely simple learning algorithm that
	 * finds the most frequent class in the training data, and then
	 * predicts that each new test example belongs to this class.
	 */
	   public class AdaBoostClassifier implements Classifier {
	   
	     private String author = "Nader Al-Naji";
	     private String description = "Yay Adaboost algorithm using decision stumps as weak learners!";
	   
	   	 private static final int ENSEMBLE_SIZE = 1000;
	   	 private BinaryDataSet my_data;
	   	 private ArrayList<WeakLearner> ensemble;
	   	 private double[] z_weights;
	   
	    /**
	     * This constructor takes as input a dataset and computes and
	     * stores the most frequent class
	     */
	      public AdaBoostClassifier(DataSet d) {
	        my_data = (BinaryDataSet)d;
	        double[] temp_weights = new double[my_data.numTrainExs];
	        for (int i = 0; i < temp_weights.length; i++)
	        		temp_weights[i] = 1./temp_weights.length;
	        z_weights = new double[ENSEMBLE_SIZE];
	        ensemble = new ArrayList<WeakLearner>(ENSEMBLE_SIZE);
	         for (int m = 0; m < ENSEMBLE_SIZE; m++) {
	         		WeakLearner new_learner = new WeakLearner(temp_weights);
	         		ensemble.add(new_learner); 
	         		double error = 0;
	         		for (int j = 0; j < my_data.numTrainExs; j++) {
	         			if (new_learner.classify(my_data.trainEx[j]) != my_data.trainLabel[j])
	         				error  += temp_weights[j];
	         		}
	         		for (int j = 0; j < my_data.numTrainExs; j++) {
	         			if (new_learner.classify(my_data.trainEx[j]) == my_data.trainLabel[j])
	         				temp_weights[j] *= error/(1-error);
	         		}
	         		normalize(temp_weights);
	         		z_weights[m] = Math.log((1-error)/error);
	         }
	      }
	      
	   	 public void normalize(double[] x)
	   	 {
	   	 	double sum = 0;
	   		for (int i = 0; i < x.length; i++)
	   			sum += x[i];
	   		for (int i = 0; i < x.length; i++)
	   			x[i] /= sum;
	   	 }
	   
	    /** The prediction method ignores the given example and predicts
	     * with the most frequent class seen during training.
	     */
	      public int predict(int[] ex) {
	      	double[] votes = new double[2];
	         for (int i = 0; i < ensemble.size(); i++) {
	         		votes[ensemble.get(i).classify(ex)] += z_weights[i];
	         }
	         return (votes[1] > votes[0] ? 1 : 0);
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
	      
	         if (argv.length < 1) {
	            System.err.println("argument: filestem");
	            return;
	         }
	      
	         String filestem = argv[0];
	      
	         DataSet d = new BinaryDataSet(filestem);
	      
	         Classifier c = new AdaBoostClassifier(d);
	      
	         //System.out.println("funfunfunfun");
	         d.printTestPredictions(c, filestem);
	      }
	   
	   
	   	 private class WeakLearner 
	   	 {
	   	 	private int stump_index;
	   	 	private int constant;
	   	 
	   	 	public WeakLearner(double[] weights)
	   		{
	   			double min_err = 9999999;
	   			int best_attr = -1;
	   			int temp_const = 0;
	   			for (int i = 0; i < my_data.trainEx[0].length; i++) {
	   				double attr_val = measure_error(i, 1, weights);
	   				if (attr_val < min_err) {
	   					best_attr = i;
	   					min_err = attr_val;
	   					temp_const = 1;
	   				}
	   				attr_val = measure_error(i, -1, weights);
	   				if (attr_val < min_err) {
	   					best_attr = i;
	   					min_err = attr_val;
	   					temp_const = -1;
	   				}
	   			}
	   			stump_index = best_attr;
	   			constant = temp_const;
	   	 	}
	   		public int classify(int[] x)
	   		{
	   			if (constant == 1)
	   				return x[stump_index];
	   			else
	   				return x[stump_index] == 1 ? 0 : 1;
	   		}
	   		public double measure_error(int ind, int factor, double[] w)
	   		{
	   			double sum = 0;
	   			for (int i = 0; i < my_data.numTrainExs; i++) {
	   				if (my_data.trainEx[i][ind] != my_data.trainLabel[i])
	   					sum += w[i];
	   			}
	   			return sum/my_data.numTrainExs;
	   		}
	   	 }
	   }
