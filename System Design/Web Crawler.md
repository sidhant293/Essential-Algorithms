# Web Crawler

- Assume 1billion web pages are downloaded each month
- Assume avg page size = 500 kb 
- Storage of 1 month = 10^9 * 500 * (10^3) = 500 10^12 = 500 TB
- Storage of 5 years = 12 * 5 * 500 TB = 30000 TB = 30 PB

Crawler should be
- Scalable
- Extensible (more features can be added)
- Fresh Pages it should have
- Polite (don't put load on servers of other sites) 

## High Lvl Design

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Web_Crawler.drawio.png)

- Seed URL – It contains the list of most popular sites. Sites can also be classified under various domains like fashion, travel, education etc. These sites act as a starting point for crawlers to crawl.
- URL Frontier- It prioritizes the URLs like which one to crawl first.
- URL downloader and renderer – It downloads the web pages from internet. DNS resolver is also used which converts URL into IP address.URL Frontier sends links to this service so that it can be crawled. It contains multiple servers. Load balancer is used. After downloading, pages are stored in Redis cache and older pages will be eventually pushed into disk. This is done because if their is a faliure then crawl can start from last recovery point.
- Duplicate Detection and URL extraction- A lot of websites such as blogs copy content of other websites. This duplicate content will be detected and removed here. After that, the URLs from the valid webpages are extracted. 
- URL Filter- These extracted URLs are filtered. Some malicious and also duplicate URLs will be filtered out here. Bloom filter is used to remove duplicates.
- URL Loader- These URLs are then checked, if they are not crawled then they are again sent to URL Frontier for crawling else they are saved in storage with some amount of web page data. So that if search happens and data gets matched then those URLs can be shown as output.

## Deep Dive

When URLs are take from seed server, they need to be crawled. Crawling can be done in DFS or BFS fashion. As a webpage has many deep links so DFS is not fisible. BFS will be used because it will exhaust current page completely before going to new page.

Even before crawling we need to prioritize what to crawl. That is done by URL Frontier

### URL Frontier

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/URL_Frontier.drawio.png)

-	Prioritizer - It first indicates which URLs to crawl first. This can be done by checking which websites it belongs (site with higher updating rate will be crawled more often). We can prioritize URLs by giving them a number like 1 to N

-	Front Queues – As there are N priorities, we will create N queues. Each queue will contain priorities for a specific value only. Each queue can have links from different sites but all will have same priority. A bias will be given to queue with higher priority. As largre amount of data will be present here , so data shoukd be
stored in disk rather than in memory.

-	Back Queue Router – It takes data from front queues and assigns them to back queues. It will contain a mapping table which has data like which queue contains data for which site. If some new site is found then a new insertion in table will be done so that links from next time are sent to that queue only

-	Back Queues – The purpose of making back queues is to handle politeness. Each back queue will have links regarding one website only. Data will be stored in memory

-	Back Queue Selector - It selects links from back queues and sends them to get crawled. Data from queues is taken in such a way that politeness is maintained. Pages from a single website are crawled after some specific threshold only.

### Handling Duplicate Content

There are some blogs and other websites which copy data from other sources. They need not be crawled.

In order to check for duplicates we can hashing like MD5. Hash the content and then check if its equal. But this approach will work only sometime because data is not even exactly copied.

Other way is to use Simhash. In this apparoch we break text into chunks , calculate frquency of words and hash the chunks. Hamming distance between hashes is calculated which tell us how close webpages are.
