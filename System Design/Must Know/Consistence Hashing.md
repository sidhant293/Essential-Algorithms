# Consistence Hashing

## Why Consistence Hashing ?

If we have suppose 5 servers, the common way to hash data accross servers is hash(key)%5. This will give value 0-4 and request will go to that server. But there is a problem
Data will be distributed to one of the 5 servers. Now if we add one more server then hash(key)%6 will happen and reshifting of requestId will happen 
which will lead to major cache miss. 

Also if we loose some server then hash(key)%4 will happen and again reshifting of data will lead to a major cache miss.


In order to prevent this, we will do consistence hashing.

## Consistence Hashing

[diagram](https://viewer.diagrams.net/?tags=%7B%7D&highlight=0000ff&edit=_blank&layers=1&nav=1&title=consistence_hashing_distributed_cache.drawio.png#R1ZdNc5swEIZ%2FDcfMgASEXGO7yaWdTphxzzJajCYyorIccH99RS3Mh%2ByGdrA9PiG9u%2Fp6dmclHDzbVC%2BSFNlXQYE7yKWVg%2BcOQp6Pff2plf1BiVB0ENaSUePUCjH7BUZ0jbpjFLY9RyUEV6zoi4nIc0hUTyNSirLvlgreX7Uga7CEOCHcVn8wqjJzisBt9Vdg66xZ2XONZUMaZyNsM0JF2ZHwwsEzKYQ6tDbVDHgNr%2BFyGPfljPW4MQm5GjPg29MboYs5X86j73u6LMVD%2FPFgZvkgfGcObDar9g0BPYuGrTvPZcYUxAVJakup4621TG247nm6SbbFIQIpq0Av%2BpwyzmeCC6m1XOT1FPammx2AVFB1JHOIFxAbUHKvXYwVRQaoySivAVy28UG%2B0bJObI4iMTmxPs7dYtMNQ%2B4fKGKLYgxSn0hrnsVTil1Oaz5z93OmXYQOwpRAlCZa3yop3qFjCZMIVuk0hP1wQNi3CZ8CjC%2FF1z%2FP187XO%2BDr9%2FlifGO%2BwXm%2B%2BA75okH%2BIvfGfB8tvksm1Y7UAy%2BVyQFE1D9FOkIrHIbTkA7cAWlvHOmLVeKnMaQnrslXIY29z2%2B9q5Ju9tN7OoRc1SAKkuv2um6fwI8bP71u1%2FUOozJ8i6DHcVEJLhYV%2B0H3Bj93sFWTV5g0TVFyspbTcBUGU%2BX98K689VvEQ38hPO1teSXCw%2Ff0rW9Lz35Ot4Snrd3XIWzdkiOrxH8Q1t32j%2FKPrfNfjhe%2FAQ%3D%3D)

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
