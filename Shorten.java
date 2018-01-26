import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Shorten {
	public static void main(String args[]) throws IOException
	{
		File bigger = new File("C:/Users/sinne/workspace/A4/words_text.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter("bignew.txt"));
		File stop = new File("C:/Users/sinne/workspace/A4/sw.txt");
		BufferedReader br = new BufferedReader(new FileReader(stop));
		ArrayList<String> words = new ArrayList<String>(); 
		String w;
		while ((w = br.readLine()) != null)
		{
			//System.out.println(w);
			words.add(w);
			//words.remove("");
		}
		words.removeAll(Collections.singleton(""));
		words.add("");
		System.out.println(words);
		br = new BufferedReader(new FileReader(bigger));
		while ((w = br.readLine()) != null)
		{
			w=w.toLowerCase();
			if (w.matches("[a-zA-Z]+"))
			{
				if (!words.contains(w) && w!="")
				{
					bw.write(w);
					bw.newLine();
					System.out.println(w);
				}
			}
		}
	}
}
