
# URL Shortener

Design a url shortner which converts a url to a shortened form.

google.com/some-long-string ----> bitly.com/h4ux7z5
 


## Assumptions
 ### Users
- 30M/month --> 1M/day 
- More reads than writes

### Data 
- Long Url- 2KB (2048 chars)
- Short Url- 7 char (for shortened string) + 10 (our domain name-"bitly.com/") = 17 bytes (17char)
- Created_at - 7 bytes (7 char) (epoch format)
- Exp_at - 7 bytes (7 char) (epoch format)

 #### Total- 2.031KB

### Capacity Planning
As we have 30M/month users so data will be
- 60.7GB/month
- 0.7TB/year  (1TB-1000GB)
- 3.6TB  (in 5 years)

## Hashing Algorithm

First approach I can think of is to use MD5 hash. It takes a string (long url) and converts it into a encoded string of variable lenght.
We can take the first 7 characters of that encoded string.
Problem with this approach is , 7 characters may be same for different strings which will lead to corruption of data.

Instead we can use Base62 hashing algo.
It takes a number as input and converts it into a string.

0-9 = 10 char 

a-z = 26 char

A-Z = 26 char

Total = 62

Now as we are converting 7 char string, so total permutations will be 62^7 = 3.5 Trillion

Very less chance of data inconsistency.

#### Base62 Encode 

```java
String encode_Base62(long n) {
  String elements = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
  StringBuilder sb = new StringBuilder();
  while (n != 0) {
    sb.insert(0, elements.charAt(n % 62));
    n /= 62;
  }
  while (sb.length() != 7) {
    sb.insert(0, '0');
  }
  return sb.toString();
}
```

#### Base62 Decode 
```
public int base62ToBase10(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            n = n * 62 + convert(s.charAt(i));
        }
        return n;
}
public int convert(char c) {
        if (c >= '0' && c <= '9')
            return c - '0';
        if (c >= 'a' && c <= 'z') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 36;
        }
        return -1;
}
```

## Technique

So we need to take into account that there will be 
multiple service running in parallel. The easiest thing
we can do is ->

- Get a random number
- Create url encoding for it
- If encoding exists in DB then repeat above steps again
- Else save and return url


#### Problem
Initially it will work fine but when number of DB entries
increases then it will be inefficient.

### Optimization 1

We can take a counter value and pass it to our encode function
so that it will create a unique 7char string.

Increment the counter

Save this counter value in a DB Table so that it is accessible
to all other services.

Now we dont need to every time check wheather encode
string is unique

#### Problem
Counter value stored in a table is a bottleneck as all the
would first get value of counter and then move ahead. Services
will not be able to perform in parallel. 

### Optimization 2

Instead having all services get from a counter variable
we can have a specified ranged counter for each service.

We will also have a Zookeper which will contain information
about global counter variable and which service has a amount of range.

When range of a service is exhausted or a new service is 
to be spanned, we can give it new range and increment increment
in Zookeper.

0 can be initial value of our global counter stored in
Zookeper. Every service can have range of 10M.
Also value of global variable will be incremented by 10M

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/URL_Shortener_img1.drawio.png)


## DataBase Design
Either we can use a RDBMS or NOSQL. If we choose RDBMs
as our database to store the data then it is efficient
to check if an URL exists in database and handle 
concurrent writes. But RDBMs are difficult to scale.

On the other hand, If we opt for “NOSQL”, we can leverage
scaling power of “NOSQL”-style databases but these 
systems are eventually consistent.

Since we want our application to be highly consistent, we
should use RDBMS.

### Sharding Technique
We cannot use a single DB instance. We need to shard our
data so it is easily scaleable. 

Sharding in RDBMS means database tables are partitioned,
and each partition is put on a separate RDBMS server.
To lookup or save data we need to go to particular RDBMS
server and perform operation there.

This all depends on sharding key.

As we know ZooKeeper is keeping track of counter range,
we can have that range as a sharding key.

Each RDBMS instance will contain 10M records of data.
When range exceeds 10M, new instance will be spanned. Each
instance will store counter values as ids
like 0-10M, 10M-20M,20M-30M.

### Schema

```sql
CREATE TABLE tinyUrl (
    id  BIGINT                 NOT NULL,  AUTO_INCREMENT
    shortUrl  VARCHAR(7)       NOT NULL,
    originalUrl  VARCHAR(400)  NOT NULL,
    exp   VARCHAR(7)       NOT NULL,
    automatically on primary-key column
                                           -- INDEX (shortUrl)
                                           -- INDEX (originalUrl)
);
```
