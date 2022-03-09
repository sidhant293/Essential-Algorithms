# Consistence Hashing

## Why Consistence Hashing ?

If we have suppose 5 servers, the common way to hash data accross servers is hash(key)%5. This will give value 0-4 and request will go to that server. But there is a problem
Data will be distributed to one of the 5 servers. Now if we add one more server then hash(key)%6 will happen and reshifting of requestId will happen 
which will lead to major cache miss. 

Also if we loose some server then hash(key)%4 will happen and again reshifting of data will lead to a major cache miss.


In order to prevent this, we will do consistence hashing.

## Consistence Hashing

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/consistence_hashing_distributed_cache.drawio.png)

Suppose we use SHA-1 hashing function. The range of this function will be 0 to 2^160-1. Suppose this range is N. 

Let us think of a logical circle which has N values. Now process as follows->

- First we will map all the servers on the circle using a hashing function h1.
- Whenever a request comes we will pass it through h1 and it will also be mapped to circle.
#### Remember that here we dont use % operation. We direclty map result to the circle.
- Move in clockwise direction on requestId, the first server found, request goes to that server.
- In this way if servers are added or removed, no major reshifting will happen


But there are some problems with this approach. Hash function may be equialy likely spread load. eg keys of Justin Biber, Ronaldo, Virat Kholi all map to same server
Also if servers are removed or added, one server might get larger load than others

- In order to evenly distribute load on servers we need more serves. That cannot be always econimically possible. So we can use virtual servers also.
- We can use different hash function (h2) and pass serverId to it and map it on circle. So any request going to virtual serve 2 will actuall go to original server 2. In this way we need not to deploy more servers also
