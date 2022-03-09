## Consistence Hashing

Now we want to put our algo into multiple servers and run them in parallel.
But there is a problem, as cached data is stored inside servers, they might get lost.
Or we might need to scale up and add more servers if load increases or also scale down if we dont need those servers running in parallel

If we try simple hashing and do scale up or down, there will be a major reshifting of data and everything would be a cache miss.

eg suppose we have 5 servers, if any request comes we first hash it and then hash(requestId)%5.

So data will be distributed to one of the 5 servers. Now if we add one more server then hash(requestId)%6 will happen
and reshifting of requestId will happen which will lead to major cache miss.


In order to prevent this, we will do consistence hashing.

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/consistence_hashing_distributed_cache.drawio.png)

Let us think of a logical circle which has N values. N can be anything like 2^31. Now process as follows->

- First we will map all the servers on the circle using a hashing function h1.
- Whenever a request comes we will pass it through h1 and it will also be mapped to circle.
- Move in clockwise direction on requestId, the first server found, request goes to that server.
- In this way if servers are added or removed, no major reshifting will happen
- In order to evenly distribute load on servers we need more serves. That cannot be always possible. So we can use virtual servers also.
- We can use different hash function (h2) and pass serverId to it and map it on circle. So any request going to virtual serve 2 will actuall go to original server 2. In this way we need not to deploy more servers also
