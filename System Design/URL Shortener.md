
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
String encode_Base62(int num) {
  String s="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  char[] arr=s.toCharArray();
  String encode="";
  while(num>0){
    encode=arr[num%62]+encode;
    num/=62;
  }
  return encode;
}
```

