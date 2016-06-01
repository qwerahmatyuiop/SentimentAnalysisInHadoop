/*Author: Rahmat Peter I. Dabalos
Program Description: This gets the total negative and positive messages */
import java.util.*;
import java.io.*;
public class GetAnalytics {
	
	public static void main (String [] args){
		int negative=0;
		int positive=0;
		int neutral=0;
		int total=0;
		File f = new File("/home/ubuntu/wordcountprof/Results");// your folder path
		String[] fileList = f.list(); // It gives list of all files in the folder.

		for(String str : fileList){
			 Scanner in = null;
	    try {
	        // init input, output
	        in = new Scanner(new File("/home/ubuntu/wordcountprof/Results/"+str));
	        // read input file line by line
	        while (in.hasNextLine()) {
	        	 total++;
	        	 String [] splitted = in.nextLine().split("\t",2);
	        	 double value = Double.parseDouble(splitted[0]);
	        	 if(value == 0){
	        	 	neutral++;
	        	 }
	        	 else if(value < 0){ 
	        	 	negative++;
	        	 }
	        	 else{
	        	 	positive++;
	        	 } 
	        }
	        System.out.println("Total:"+total);

	        System.out.println("Negative:"+negative+",%="+((float)(negative/total)*100)+"%");
	        System.out.println("Neutral:"+neutral+",%="+((float)(neutral/total)*100)+"%");
	        System.out.println("Positive:"+positive+",%="+((float)(positive/total)*100)+"%");
	        in.close();
	    } catch(Exception e){

	    }
	}
	}
}
