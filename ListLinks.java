import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {
    public static void main(String[] args) throws IOException {
    	long startTime = System.currentTimeMillis();
    	System.out.println(System.currentTimeMillis()-startTime);
    	String line="";
    	File map =new File ("C:/Users/sinne/Desktop/LATimesData/mapLATimesDataFile.csv");
        BufferedReader br = new BufferedReader(new FileReader(map));
        PrintWriter writer = new PrintWriter("edgeList.txt","UTF-8");
        HashMap<String,String> fileUrlMap = new HashMap<String,String>();
        HashMap<String,String> urlFileMap = new HashMap<String,String>();
        while ((line=br.readLine())!=null)
        {
        	String temp[]=line.split(",");
        	fileUrlMap.put(temp[0], temp[1]);
        	urlFileMap.put(temp[1], temp[0]);
        }
        br.close();
        System.out.println(fileUrlMap.size()+" "+urlFileMap.size());
    	int count = 0;
    	Set<String> edges = new HashSet<String>();
    	String dirPath = "C:/Users/sinne/Desktop/LATimesData/LATimesDownloadData/";
        File dir=new File(dirPath);
    	for (File file:dir.listFiles()){
    		System.out.println(count+" "+file);
    	Document doc =Jsoup.parse(file,"UTF-8",fileUrlMap.get(file.getName()));
        Elements links = doc.select("a[href]");
   
        for (Element link : links) {
        	String url =link.attr("abs:href").trim();
            if (urlFileMap.containsKey(url))
            	edges.add(file.getName()+" "+urlFileMap.get(url));
        }
        }
        for (String s: edges){
        	writer.println(s);
        }
        writer.flush();
        writer.close();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
}