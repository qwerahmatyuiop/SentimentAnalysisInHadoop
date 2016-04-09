import java.util.*;
import java.io.*;

public class Merge{
	public static ArrayList<String> combined = new ArrayList<String>();
	public static void main(String[] args) throws Exception{
		File English = new File("positive-words.txt");
		File Tagalog = new File("positive-wordsTagalog.txt");


		Scanner in = new Scanner(English);

		while(in.hasNext()){
			String word = in.next();
			if(!combined.contains(word.toLowerCase())){
				combined.add(word.toLowerCase());
			}
		}

		Scanner ins = new Scanner(Tagalog);

		while(ins.hasNext()){
			String word = ins.next();
			if(!combined.contains(word.toLowerCase())){
				combined.add(word.toLowerCase());
			}
		}

		Collections.sort(combined);
		System.out.println(combined.size());
		
			PrintWriter writer
   = new PrintWriter(new BufferedWriter(new FileWriter("positive-words-combined.text")));
			int i = 0;
			for(String w: combined){
				writer.println(w);
				System.out.print(++i+" ");

			}
			writer.close();



	}
}