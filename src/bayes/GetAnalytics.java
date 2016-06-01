/*Author: Rahmat Peter I. Dabalos
Program Description: This is gets the total negative and positive messages then writes it into a file*/
import java.util.*;
import java.io.*;
import java.math.*;
public class GetAnalytics {
	
	public static void main (String [] args){
		int negative=0;
		int positive=0;
		int total=0;
		int neutral=0;
		File f = new File("/home/ubuntu/bayes/Results");// your folder path
		String[] fileList = f.list(); // It gives list of all files in the folder.

		for(String str : fileList){
			 Scanner in = null;
			 PrintWriter outNegative = null;
			 PrintWriter outPositive = null;
			 PrintWriter outNeutral= null;
	    try {
	        // init input, output
	        in = new Scanner(new File("/home/ubuntu/bayes/Results/"+str));
	        outPositive=new PrintWriter(new File("/home/ubuntu/bayes/positiveResults.text"));
	        outNegative=new PrintWriter(new File("/home/ubuntu/bayes/negativeResults.text"));
	       	outNeutral=new PrintWriter(new File("/home/ubuntu/bayes/neutralResults.text"));

	        ArrayList<BigDecimal> list = new ArrayList<BigDecimal>();
	        while (in.hasNextLine()) {
	        	 total++;
	        	 String [] splitted = in.nextLine().split("\t",2);
	        	 BigDecimal value = new BigDecimal (splitted[0].substring(splitted[0].indexOf('(')+1,splitted[0].indexOf(')')-1));
	        	// double value = Double.parseDouble(splitted[0]);

	        	System.out.println(value);
	        	 if((value.compareTo(BigDecimal.valueOf(0.5))) == -1){
	        	 	positive++;
	        	 	outPositive.println(splitted[1].trim());
	        	 	outPositive.flush();
	        	 }
	        	 else if ((value.compareTo(BigDecimal.valueOf(0.5))) == 0){
	        	 	neutral++;
	        	 	outNeutral.println(splitted[1].trim());
	        	    outNeutral.flush();
	        	 }else{
	        	 	negative++;
	        	 	outNegative.println(splitted[1].trim());
	        	 	outNegative.flush();
	        	 }
	        	

	        }
	        System.out.println("Total:"+total);
	        System.out.println("Negative:"+negative+",%="+((double)(negative/total)*100)+"%");
	        System.out.println("Neutral:"+neutral+",%="+((double)(neutral/total)*100)+"%");
	        System.out.println("Positive:"+positive+",%="+((double)(positive/total)*100)+"%");
	        in.close();
	        outPositive.close();
	        outNeutral.close();
	        outNegative.close();
	    } catch(Exception e){

	    }
	}
	}
}