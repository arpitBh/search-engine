package edu.uci.ics.crawler4j.examples.localdata;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css.*|js|xml|" + "mp3|zip|gz))$");
	private static int counters = 0;
	private static FileWriter fileWriter;
	protected String contentType;
	private ArrayList<String> file1 =new ArrayList<>();
	private ArrayList<String> file2 =new ArrayList<>();
	private ArrayList<String> file3 =new ArrayList<>();

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) 
		{
		String href = url.getURL().toLowerCase();
		String temp=href.replaceAll(",","-");
		if (href.startsWith("http://www.latimes.com/"))
		{
			file3.add(temp+ "," + "OK\n");
		}
		else
		{
			file3.add(temp+","+"N_OK\n");
		}
		 return !FILTERS.matcher(href).matches()
		 && href.startsWith("http://www.latimes.com/");
		}
	@Override
	public void visit(Page page) 
	{
		String url = page.getWebURL().getURL();
		String temp=url.replaceAll(",","-");
		contentType=page.getContentType();
		int scIndex = contentType.indexOf(";");
		if (scIndex != -1)
		{
		    contentType = contentType.substring(0, scIndex);
		}
		System.out.println(contentType);
		String html="";
		Set<WebURL> links=Collections.emptySet();
		try{
		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		String text = htmlParseData.getText();
		html = htmlParseData.getHtml();
		links = htmlParseData.getOutgoingUrls();	
		}
		catch(Exception e){}
			System.out.println("Counts: " + Integer.toString(counters));
			counters += 1;
			file2.add(temp+ "," + page.getContentData().length+","+links.size()+","+contentType+"\n");
	}
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) 
	{
		String temp = webUrl.getURL();
		temp=temp.replaceAll(",","-");
		file1.add(temp+ "," + Integer.toString(statusCode)+"\n");
	}
	@Override
	public void onBeforeExit() 
	{
		try 
		{
            fileWriter = new FileWriter(new File("fetch.csv"));
            for (String s:file1)
            {
            	fileWriter.append(s);
            }
            fileWriter.close();
            fileWriter = new FileWriter(new File("visit.csv"));
            for (String s:file2)
            {
            	fileWriter.append(s);
            }
            fileWriter.close();
            fileWriter = new FileWriter(new File("urls.csv"));
            for (String s:file3)
            {
            	fileWriter.append(s);
            }
            fileWriter.close();
		}
		catch (Exception e){}
	}
}