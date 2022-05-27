# TypeAhead Design

- DAU = 10M
- 1 person = 10 searches/day
- Data of 1 query= 1char=1 byte. On average let each word has 5 chars and be average 4 words. 4 * 5 * 1= 20 bytes/query
- Data for 1 day= 10M*20*10 = 2GB 
- New queries/day = 20%
- Data for 1 year = 2GB + ( 2GB * 0.2 * 365 ) = 148GB
