// @author - Rhythm Girdhar
// @id - 6742001330

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Controller {

    private static void writeCSV(StringBuilder output, String s) throws IOException {
        PrintWriter writer = new PrintWriter(s, StandardCharsets.UTF_8);
        writer.println(output.toString().trim());
        writer.flush();
        writer.close();
    }
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "data/crawl";
        int numberOfCrawlers = 50;
        int maxPagesFetch = 20000;
        int maxCrawlingDepth = 16;
        int politenessDelay = 600;
        String seedUrl = "https://www.wsj.com";
        String task1Filename = "fetch_wsj.csv";
        String task2Filename = "visit_wsj.csv";
        String task3Filename = "urls_wsj.csv";

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setIncludeBinaryContentInCrawling(true);
        config.setMaxPagesToFetch(maxPagesFetch);
        config.setMaxDepthOfCrawling(maxCrawlingDepth);
        config.setMaxDepthOfCrawling(numberOfCrawlers);
        config.setPolitenessDelay(politenessDelay);
        config.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        /*
         * For each crawl, you need to add some seed urls. These are the first * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed(seedUrl);
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);

        StringBuilder task1 = new StringBuilder("URL,Status\n");
        StringBuilder task2 = new StringBuilder("URL,Size (Bytes),# of Outlinks,Content-Type\n");
        StringBuilder task3 = new StringBuilder("URL,URL Type\n");

        for (Object t : controller.getCrawlersLocalData()) {
            String[] tasks = (String[]) t;
            task1.append(tasks[0]);
            task2.append(tasks[1]);
            task3.append(tasks[2]);
        }

        writeCSV(task1, task1Filename);
        writeCSV(task2, task2Filename);
        writeCSV(task3, task3Filename);
    }
}