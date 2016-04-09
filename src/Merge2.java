import java.util.*;
import java.io.*;

public class Merge2{
	public static ArrayList<String> combined = new ArrayList<String>();
	public static void main(String[] args) throws Exception{
		File pos = new File("positive-words-combined.text");
		File neg = new File("negative-words-combined.text");


		Scanner in = new Scanner(pos);

		while(in.hasNext()){
			String word = in.next();
			if(!combined.contains(word.toLowerCase())){
				combined.add(word.toLowerCase());
			}
			else{
				System.out.println(word);
			}
		}

		Scanner ins = new Scanner(neg);

		while(ins.hasNext()){
			String word = ins.next();
			if(!combined.contains(word.toLowerCase())){
				combined.add(word.toLowerCase());
			}
			else {
				System.out.println(word);
			}
		}

		Collections.sort(combined);
		System.out.println(combined.size());




	}
}