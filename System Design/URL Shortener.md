
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
about global counter variable and which service has what amount of range.

When range of a service is exhausted or a new service is 
to be spanned, we can give it new range and increment increment
in Zookeper.

0 can be initial value of our global counter stored in
Zookeper. Every service can have range of 10M.

service1 - (0-10M)

service2- (10M-20M)

and so on. Value of global variable will be incremented
by 10M

https://medium.com/@sandeep4.verma/system-design-scalable-url-shortener-service-like-tinyurl-106f30f23a82
