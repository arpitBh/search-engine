package edu.uci.ics.crawler4j.examples.localdata;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller 
{
	final static long startTime = System.currentTimeMillis();
	public static void main(String[] args) throws Exception
	{     
		 String crawlStorageFolder = "/data/crawl";
		 int numberOfCrawlers = 1;
		 int maxFetch = 20000;
		 int maxDepth = 16;
		 CrawlConfig config = new CrawlConfig();
		 config.setCrawlStorageFolder(crawlStorageFolder);
		 PageFetcher pageFetcher = new PageFetcher(config);
		 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		 config.setMaxDepthOfCrawling(maxDepth);
		 config.setMaxPagesToFetch(maxFetch);
		 config.setPolitenessDelay(100);
		 config.setIncludeBinaryContentInCrawling(true);
		 controller.addSeed("http://www.latimes.com/");
		 controller.start(MyCrawler.class, numberOfCrawlers);
		 final long endTime = System.currentTimeMillis();
		 System.out.println("Total execution time: " + (endTime - startTime) );
	}
}