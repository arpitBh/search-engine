import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Example program to list links from a URL.
 */
public class ParseLinks {
    public static void main(String[] args) throws IOException {
        File dir = new File("C:/Users/sinne/Desktop/LATimesData/LATimesDownloadData/");
        Set<String> edges = new HashSet<String>();
        //hashmap called fileurlmap and urlfilemap
        HashMap<String,String> fileurlmap = new HashMap<String,String>();
        HashMap<String,String> urlfilemap = new HashMap<String,String>();
        PrintWriter writer = new PrintWriter("edges_output_test.txt", "UTF-8");
        BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sinne/Desktop/LATimesData/mapLATimesDataFile.csv"));
        String line = "";
        while((line=reader.readLine())!=null){
            String [] data =line.trim().split(",");
            
            urlfilemap.put(data[1],data[0]);
            fileurlmap.put(data[0],data[1]);
        }
        for(File file: dir.listFiles())
        	{
        	System.out.println(file.getName());
        	Document doc = Jsoup.parse(file,"UTF-8",fileurlmap.get(file.getName()));
        	Elements links = doc.select("a[href]");
        	for (Element link : links) 
        		{
        		 String url = link.attr("abs:href").trim();
        		 if(urlfilemap.containsKey(url))
        			 edges.add(file.getName() + " " + urlfilemap.get(url));                 
        		}
        	}
        for(String ele:edges)
        	writer.println(ele);
        reader.close();
        writer.flush();
        writer.close();
    }
}