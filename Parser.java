import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
public class Parser {
	public static void main(String args[]) throws IOException
	{
		
		File folder = new File("C:/Users/sinne/Desktop/LATimesData/LATimesDownloadData/");
		File[] listOfFiles = folder.listFiles();
		BufferedWriter bw = new BufferedWriter(new FileWriter("big.txt"));
		for (File input:listOfFiles)
		{
			System.out.println(input.getName());
			//File input = new File("C:/Users/sinne/Desktop/LATimesData/LATimesDownloadData/fffc9db2-966a-4e88-b3a9-3f298bd58631.html");
			String doc = Jsoup.parse(input,"ISO-8859-1").select("body").text();
			String words[]=doc.split("\\s+");
			
			for (String w:words)
			{
				if (w.matches("[a-zA-Z]+"))
				{
					bw.write(w+"\n");
					bw.newLine();
				}
			}
		}
		//System.out.println(doc);
		bw.close();
	}
}
