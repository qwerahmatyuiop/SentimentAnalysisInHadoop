/*Author: Rahmat Peter I. Dabalos
Program Description: This is the Naive Bayes Classifier */

import java.io.IOException;
import java.util.*;
import java.io.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import java.math.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.hadoop.fs.FSDataOutputStream;
public class WordCountBayes {
	
		public static double pSpam = 0;
		public static double pHam = 0;
		//This is the Mapper Class
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		private static HashMap<String, Integer> positiveWords =  new HashMap<String, Integer>(); 
		private static HashMap<String, Integer> negativeWords =  new HashMap<String, Integer>(); 
		private final static IntWritable one = new IntWritable(1);
		private static ArrayList<String> stopWords =  new ArrayList<String>(); 
		public static ArrayList<String> newWord = new ArrayList<String>();
		private Text word = new Text();
		public static int totalSpam = 0;
		public static int dictionarySizeSpam;
		public static int totalHam = 0;
		public static int dictionarySizeHam;
		public static int totalDicSize = 0;
		public static int totalWordsS = 0;
		public static int spamMessages = 0;
		public static int hamMessages = 0;
		public static int totalMessages;
		public static int lineCount =0;
		public static int K = 2;
		public static int newWords;

		//In setup the bag of words is populated from the text file that contains the initial dataset
		protected void setup(Context context) throws IOException, InterruptedException {
        	negativeWords.clear();
        	positiveWords.clear();
        	 try{
        	 	hamMessages = 0;
				spamMessages = 0;
		
			 positiveWords = getList("positive.text",true, new HashMap<String, Integer>());
			 negativeWords = getList("negative.text",false, new HashMap<String, Integer>());	
			 stopWords = getList2("stop-words.text", new ArrayList<String>());

			 } catch(Exception e){}	
        }
    
        //main algorithm
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	 dictionarySizeSpam = 0;
        	 dictionarySizeHam =0;
        	 totalSpam = 0;
        	 totalHam = 0;
        	 totalWordsS = 0;
        	 totalDicSize = 0;
        	 totalMessages = 0;
        	 pSpam = 0;
        	 pHam = 0;
			 dictionarySizeSpam = negativeWords.size();
			 for(Integer s: negativeWords.values()){
			 totalSpam += s;
			 }

			 dictionarySizeHam = positiveWords.size();
			 for(Integer s: positiveWords.values()){
			 totalHam += s;
			 }
			 totalWordsS = totalSpam + totalHam;
			 totalDicSize = dictionarySizeSpam + dictionarySizeHam;
			 totalMessages = spamMessages + hamMessages;

			 pSpam = ((double)spamMessages+ K)/((double)totalMessages + (2*K));

			 pHam = 1-pSpam;

			for (String keys: negativeWords.keySet()) {
				if(positiveWords.containsKey(keys)){
					totalDicSize--;
				}
			}
			 String line = value.toString();
			 String [] splitted = line.split(",",3);
         	 String lasttoken = null;
         	 double pwSpam = 1;
			 double pwHam = 1;
			 newWord.clear();
			 newWords = 0;
         	 if(splitted.length > 2){
         	 	 word.set(splitted[2]);
	         	 StringTokenizer s = new StringTokenizer(splitted[2]);
	         	 //checks if there are words not present in the bag
	         	 while(s.hasMoreTokens()){
	            	lasttoken=s.nextToken().toLowerCase().replaceAll("[\\W]_","").trim();
	            	if(lasttoken != null && lasttoken.length() > 1 ){
	            	if(stopWords.contains(lasttoken)){
	            		continue;
	            	}
	            	if(!negativeWords.containsKey(lasttoken) && !positiveWords.containsKey(lasttoken) && !newWord.contains(lasttoken)){
						newWords++;
						newWord.add(lasttoken);
					}	
					}	

	         	  }
	         	  	//assigns the probability for each word through each word's occurence in the Bag-of-words
	         	  	s = new StringTokenizer(splitted[2]);
	         	   while(s.hasMoreTokens()){
	            	lasttoken=s.nextToken().toLowerCase().replaceAll("[\\W]_","").trim();
	            	if(lasttoken != null && lasttoken.length() > 1){
	            	if(stopWords.contains(lasttoken)){
	            		continue;
	            	}
	         	  	if(negativeWords.containsKey(lasttoken)){
						pwSpam = (double)(negativeWords.get(lasttoken)+K)/(double)(totalSpam+(K*(totalDicSize+newWords)));
					}else{
						pwSpam = ((double)(0+K)/(double)(totalSpam+(K*(totalDicSize+newWords))));
					}

					if(positiveWords.containsKey(lasttoken)){
						pwHam = (double)(positiveWords.get(lasttoken)+K)/(double)(totalHam+(K*(totalDicSize+newWords)));;	
					}
					else{
						pwHam = (double)(0+K)/(double)(totalHam+(K*(totalDicSize+newWords)));
					}
					context.write(word, new Text(pwSpam+";"+pwHam+";"+pSpam+";"+pHam));
				}	
			}
         	}

		}
		//Task is to read a file and put it into a hashmap
		public static HashMap<String, Integer> getList(String input, boolean flag, HashMap<String, Integer> AL) throws Exception{
					
 			try{
	            Path pt=new Path("hdfs://masternode:9000/user/ubuntu/dataset/"+input);//Lcocation of file in HDFS
	            FileSystem fs = FileSystem.get(new Configuration());
	            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
	            String line;
	            while ((line=br.readLine() )!= null){
	            	if(flag){hamMessages++;}
	            	else{spamMessages++;}
	            	StringTokenizer st = new StringTokenizer(line, " ");
	            	while(st.hasMoreElements()){
	            		String word = st.nextElement().toString().replaceAll("[\\W]_","").toLowerCase();
	            		if(word != null && word.trim().length() > 1){
	            			if(AL.containsKey(word)) AL.put(word, AL.get(word) + 1);
							else AL.put(word, 1);
						}
	            	}
	            }
	            br.close();
       		}catch(Exception e){
       		}
			return(AL);
		}
		//Getting the stop-words
		public static ArrayList<String> getList2(String input, ArrayList<String> AL) throws Exception{

 			try{
	            Path pt=new Path("hdfs://masternode:9000/user/ubuntu/dictionary/"+input);//Location of file in HDFS
	            FileSystem fs = FileSystem.get(new Configuration());
	            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
	            String line;
	            while ((line=br.readLine() )!= null){
	            	if(line != null && line.trim().length() > 1){
	            		if(!AL.contains(line.toLowerCase())){
	            			AL.add(line.toLowerCase());
	            		}
	            	}
	            }
       		}catch(Exception e){
       		}
			return(AL);
		}

	}
	//Reducer Function
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
				 int sum = 0;
				double pmSpam = 1;
				 double pmHam = 1;
				 BigDecimal pMessage= new BigDecimal(0);
				BigDecimal pSpamMessage = new BigDecimal(1);
				double probSpam = 1;
				double probHam =1;
			for(Text val: values){
				String[] v = val.toString().split(";",4);
				double nSpam = Double.parseDouble(v[0]);
				double nHam = Double.parseDouble(v[1]);
				double tesSpam = pmSpam;
				double tesHam = pmHam;
				if((tesSpam*nSpam)==(double)0.0){
				}
				else{
					pmSpam *= nSpam;
				}
				if((tesHam*nHam)==(double)0.0){
				}
				else{
					pmHam *= nHam;
				}
				probSpam = Double.parseDouble(v[2]);
				probHam = Double.parseDouble(v[3]);
		}
			 BigDecimal numerator1 = (new BigDecimal(pmSpam)).multiply(new BigDecimal(probSpam));
			 BigDecimal numerator2 = (new BigDecimal(pmHam)).multiply(new BigDecimal(probHam));
			 pMessage= numerator1.add(numerator2);
			 pSpamMessage= numerator1.divide(pMessage, MathContext.DECIMAL64); //this is where the probability of the whole message is calculated
			 String classification = "";
			 if((pSpamMessage.compareTo(BigDecimal.valueOf(0.5))) == 1){
			 	classification = "Negative Tweet("+pSpamMessage.toString()+") ";
			 }else if( (pSpamMessage.compareTo(BigDecimal.valueOf(0.5))) == 0){
			 	classification = "Neutral Tweet ("+pSpamMessage.toString()+") ";
			 }
			 else{
			 	classification = "Positive Tweet("+pSpamMessage+") ";
			 }
			context.write(new Text(classification), key);
		}
	}
	//main of the Naive Bayes Classifier
	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		//System.out.println("fs.default.name : - " + conf.get("fs.default.name"));
		Job job = new Job(conf, "WordCountJob");
		job.setJarByClass(WordCountBayes.class);
		

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}



}
