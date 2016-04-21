import java.io.IOException;
import java.util.*;
import java.io.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class WordCountBayes {
	
		public static float pSpam = 0;
		public static float pHam = 0;
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		//private static File Positive = new File("positive-words.text");
		//private static File Negative = new File("positive-wordsTagalog.text");
		private static HashMap<String, Integer> positiveWords =  new HashMap<String, Integer>(); 
		private static HashMap<String, Integer> negativeWords =  new HashMap<String, Integer>(); 
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

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			 try{
			 positiveWords = getList("positive.text",true, new HashMap<String, Integer>());
			 negativeWords = getList("negative.text",false, new HashMap<String, Integer>());	

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

			 pSpam = ((float)spamMessages+ K)/((float)totalMessages + (2*K));

			 pHam = 1-pSpam;

			for (String keys: negativeWords.keySet()) {
				if(positiveWords.containsKey(keys)){
					totalDicSize--;
				}
			}
			
			 }catch(Exception e){
			 	System.out.println("ERROR HERE!");
			 }
			 String line = value.toString();
			 String [] splitted = line.split(",",2);
         	 String lasttoken = null;
         	 float pwSpam = 1;
			 float pwHam = 1;
			 word.set(line);
         	 if(splitted.length > 1){
	         	 StringTokenizer s = new StringTokenizer(splitted[1]);
	         	 while(s.hasMoreTokens()){
	            	lasttoken=s.nextToken().toLowerCase().replaceAll("[^a-zA-Z]+","");
	            	if(!negativeWords.containsKey(word) && !positiveWords.containsKey(word) && !newWord.contains(word)){
						newWords++;
						newWord.add(lasttoken);
					}
					if(negativeWords.containsKey(word)){
						pwSpam = (float)(negativeWords.get(word)+K)/(float)(totalSpam+(K*(totalDicSize+newWords)));
					}else{
						pwSpam = (float)(0+K)/(float)(totalSpam+(K*(totalDicSize+newWords)));
					}

					if(positiveWords.containsKey(word)){
						pwHam = (float)(positiveWords.get(word)+K)/(float)(totalHam+(K*(totalDicSize+newWords)));;	
					}
					else{
						pwHam = (float)(0+K)/(float)(totalHam+(K*(totalDicSize+newWords)));
					}
					newWord.clear();
					newWords = 0;
					context.write(word, new Text(pwSpam+","+pwHam+","+pSpam+","+pHam));

	         	  }
         	}

		}
		public static HashMap<String, Integer> getList(String input, boolean flag, HashMap<String, Integer> AL) throws Exception{

 			try{
	            Path pt=new Path("hdfs://masternode:9000/user/ubuntu/dataset/"+input);//Lcocation of file in HDFS
	            FileSystem fs = FileSystem.get(new Configuration());
	            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
	            String line;
	            while ((line=br.readLine() )!= null){
	            	if(flag) hamMessages++;
	            	else spamMessages++;
	            	StringTokenizer st = new StringTokenizer(line, " ");
	            	while(st.hasMoreElements()){
	            		String word = st.nextElement().toString().replaceAll("[\\W]_","").toLowerCase();
	            		if(AL.containsKey(word)) AL.put(word, AL.get(word) + 1);
						else AL.put(word, 1);
	            	}
	         
	            }
	            br.close();
       		}catch(Exception e){
       		}
			//Scanner in = new Scanner(input);
			// while(in.hasNext()){
			// String word = in.next();
			// AL.add(word);
			// }
			return(AL);
		}
	}
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			float pwSpam = 1;
			 float pwHam = 1;
			 float pMessage= 1;
			 float pSpamMessage = 1;
			for(Text val: values){
				String[] v = val.toString().split(",",4);
				pwSpam *= Float.valueOf(v[0]);
				pwHam *= Float.valueOf(v[1]);
				pHam = Float.valueOf(v[2]);
				pSpam = Float.valueOf(v[3]);
			}
			
			 pMessage= (pwSpam*pSpam) + (pwHam*pHam);
			 pSpamMessage= (pwSpam*pSpam)/pMessage;
			 String classification = "";
			 if(pSpamMessage > (float)0.5){
			 	classification = "Negative";
			 	try{
			 	writeToFile(key.toString(),false);
			 	}catch(Exception e){}
			 }else{
			 	classification = "Positive";
			 	try{
			 	writeToFile(key.toString(),true);
			 	}catch(Exception es){}
			 }
			context.write(key, new Text(classification));
		}

		public static void writeToFile(String line, boolean flag) throws Exception{
 			try{
 				String input = "";
 				if(flag){
 					input = "positive.text";
 				}
 				else{
 					input = "negative.text";
 				}
	             Path pt=new Path("hdfs://masternode:9000/user/ubuntu/dataset/"+input);
	            FileSystem fs = FileSystem.get(new Configuration());
	            BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fs.append(pt)));
	            br.append(line.split(",",2)[1]+"\n");
	            br.close();

	            
       		}catch(Exception e){
       			System.out.println("ERROR!");
       		}
			//Scanner in = new Scanner(input);
			// while(in.hasNext()){
			// String word = in.next();
			// AL.add(word);
		}
	}
	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		System.out.println("fs.default.name : - " + conf.get("fs.default.name"));
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
