/*Author: Rahmat Peter I. Dabalos
Program Description: This is the Dictionary Based Classifier */
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
public class WordCountPro {
//This is the Mapper Class
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		private static ArrayList<String> positiveWords =  new ArrayList<String>(); 
		private static ArrayList<String> negativeWords =  new ArrayList<String>(); 
		private final static IntWritable one = new IntWritable(1);
		private final static IntWritable negone = new IntWritable(-1);
		private Text word = new Text();

		//In setup, the words in the positive and negative dictionary is populated
		protected void setup(Context context) throws IOException, InterruptedException {
        	 try{
			 positiveWords = getList("positive-words-combined.text", new ArrayList<String>());
			 negativeWords = getList("negative-words-combined.text", new ArrayList<String>());	
			
			 }catch(Exception e){
			 	System.out.println("ERROR HERE!");
			 }
        }
         //main algorithm
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			 String line = value.toString();
			 String [] splitted = line.split(",",3);
         	 String lasttoken = null;
         	 if(splitted.length > 2){
         	 word.set(splitted[2]);
         	 StringTokenizer s = new StringTokenizer(splitted[2]);
         	 //checks if word is in negative, or positive, or not in both the dictionaries
         	 while(s.hasMoreTokens()){
            	lasttoken=s.nextToken().toLowerCase().replaceAll("[^a-zA-Z]+","");
            	if( (positiveWords.contains(lasttoken) && (negativeWords.contains(lasttoken))) || ((!positiveWords.contains(lasttoken)) && (!negativeWords.contains(lasttoken))) ) {
            		context.write(word, new IntWritable(0));
            	}
            	else if(positiveWords.contains(lasttoken)){
            		context.write(word, one);
            	}
            	else{
            		context.write(word, negone);
            	}
         	  }
         	}

		}
		//puts the negative and positive words in a hash map
		public static ArrayList<String> getList(String input, ArrayList<String> AL) throws Exception{

 			try{
	            Path pt=new Path("hdfs://masternode:9000/user/ubuntu/dictionary/"+input);//Location of file in HDFS
	            FileSystem fs = FileSystem.get(new Configuration());
	            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
	            String line;
	            while ((line=br.readLine() )!= null){
	            	AL.add(line);
	            }
       		}catch(Exception e){
       		}
			return(AL);
		}
	}
	//adds all the polarity of each word to get the total sum of the message
	public static class Reduce extends Reducer<Text, IntWritable, Text, Text> {
		
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for(IntWritable val: values){
				sum += val.get();
			}
			context.write(new Text(Integer.toString(sum)),key);
		}
	}
	//main class
	public static void main(String[] args) throws Exception{
		Configuration conf = new Configuration();
		//System.out.println("fs.default.name : - " + conf.get("fs.default.name"));
		Job job = new Job(conf, "WordCountJob");
		job.setJarByClass(WordCountPro.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}



}
