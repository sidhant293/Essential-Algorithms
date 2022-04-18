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
- URL downloader and renderer – It downloads the web pages from internet. DNS resolver is also used which converts URL into IP address. After downloading, pages are stored in Redis cache and older pages will be eventually pushed into disk.
- Duplicate Detection and URL extraction- A lot of websites such as blogs copy content of other websites. This duplicate content will be detected and removed here. After that, the URLs from the valid webpages are extracted. 
- URL Filter- These extracted URLs are filtered. Some malicious URLs will be filtered out here
- URL Loader- These URLs are then checked, if they are not crawled then they are again sent to URL Frontier for crawling else they are saved in storage with some amount of web page data. So that if search happens and data gets matched then those URLs can be shown as output.

## Deep Dive

When URLs are take from seed server, they need to be crawled. Crawling can be done in DFS or BFS fashion. As a webpage has many deep links so DFS is not fisible. BFS will be used because it will exhaust current page completely before going to new page.

Even before crawling we need to prioritize what to crawl. That is done by URL Frontier

### URL Frontier

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Web_Crawler.drawio.png)
