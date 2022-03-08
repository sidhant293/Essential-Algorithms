
# Rate Limiter

### Requirements
System should be
- Consitence (accurately limit excessive requests)
- Less Latency
- Less Memory
- Scalable
- Fault tolerant

So using CAP theorm it needs to be consistence and partition tolerant (availablilty will suffer)

#### Note- Rate limiter should be implemented on server side. Client side is unreliable place as it can be easily be forged by malicious softwares

## Fixed Window

We say that suppose only 10 requests should be premitted
within 1min. So threashold of each slot is 10. If limit>threashold then reject request else accept.
We need to create a fixed window so that we can normalize the incomming time to that window and count requests.

So we will create a hashmap which will contain key as userId and value as {count,timesptam}
So when a request comes->
- UserId not in hashmap, add userId with count=1 and timesptam (normalized)

Normalize means 1:20 (1min 20sec) is considered as 1min.
We will have fixed windows of 0-1min, 1-2min, 2-3min.... 59-60min and then repeat. So each time will fall into some window only

- UserId present and (currentTime-timesptam)>=1min, reset count=1 and add timesptam
- UserId present and (currentTime-timesptam)< 1min, if count < threashold then increament count else reject request

Now this approach seems ok but there are some problems->
- Suppose 10 requests comes at 1:58 min (which lies in 1-2 slot) and another 10 requests comes in 2:03min (2-3 slot). These requests will get accepted but this should not happen. Difference in time is not > 1 min
- Also if concurrent threads read same value of count, then both might get accepted. We can stop this using locks every time but this will degrade performance and increase latency



## Sliding Window

To solve above problems, we will use the same hashmap structure but instead now we will store timestamps of accepted requests in a sorted set 

Hashmap structure will be
UserId : SortedSet(timestamps)

We will store requests into sorted set and counting them will give us amount of requests accepted in a particular amount of time.
The process is->

- If UserId not present add UserId and create a new SortedSet and add timestamps in it
If UserId is present and a request comes then
- Remove the timestamps from SortedSet which are older than currentTime-1min(threshold value).
- Count total entries in SortedSet. If > threshold then reject
- If < threshold then add in SortedSet and accept

Opwrations in sortedSet are atomic operations. So race condition won't happen.

#### Lets calculate data required

Suppose 500 requests per hr are required

UserId - 8 bytes, epoc time- 4 byes, overhead for sorted set and hashmap 20 bytes each

Overhead means storage of pointers etc required for functioning of hashmap/SortedSet internally

For 1 user -> 8+(4+20(sorted set overhead))*500(req/hr) + 20(hashmap overhead)
1 user -> 12kb

for 1million users-> 12kb * 1million= 12GB

Now this is large data and not very easy to scale. We can combine previous approach to reduce data


## Sliding Window Counter

Let's think of a real requirement like 500 requests/hr should be threshlod for one user also
he can make > 10 requests/min. This is a real requirement.

So we will create slots of 60 (one for each min) and these slots can't have value more than 10. Also total of 60 slots should be < 500

In order to use sliding window also here, suppose 10 requests come in first window and now second window has started and 20 seconds have passed and 6 requests come in second window

To calulate value we can do like
value= prev_counter * (slot_time-curr_time)/slot_time + curr_counter
value= 10 * (60 - 20)/60  + 6 = 13 >10
```
    40s   20s
   -----|---            (sliding window)
[  10   ][ 6    ]       (counters)
1       2       3....   (slots, each one 60sec )
```

Using sliding window and calulating as it exceeds threshlod so reject

The process is->
- Calculate sum of all slots, > 500 reject
- Calculate value , > 10 reject

As said earlier, operations in redis are atomic operations. We can also chain operations to make a transaction which is also atomic. Race condition will not happen.

To store, we will use same hashmap 
userId : Redis Cache

#### Redis Cache
It is a datatype in redis which stores key value pairs but keys less than 100.
It can be used to store slots with their counts. Also it has functionality to delete keys after some amount of time, which can be customized.
We can customize it to 1min. That means every key which is old than 1min will get refreshed.

#### Data Calculation

userId - 4 bytes, epoc time - 4 bytes, counter - 2 bytes
overhead - 20 for hashmap and redis each

8+(4+2+20)*60(no of slots) + 20=1.6kb

for 1 million users = 1.6GB
It uses far less memory

## Generic Design

To use a same rate limiter and limit over different aspects we can create a generic rate limiter which will limit over different conditions eg only 5 emails a day
are allowed or only 10 login requests are allowed per minute. To make rate limiter generic we need to define a set of rules.

```
domain: auth
key: login
rate_limit:
    unit: minute
    requests: 10
```

```
domain: messaging
key: email
rate_limit:
    unit: day
    requests: 5
```

Here domian will be microservice we want to limit, key is what particular thing we want to limit for that microservice , unit is unit of time and requests is allowed requests
per unit

## Distrubuted System

Minimum number of request allowed in 1 hr =500
Users= 1M
Minimum total requests in one hour = 1M*500=500M

One server cannot handle these requests. So we need to have distrubued servers.

Each server will run on the same logic explained earlier.

#### Important Points
- We can use usedId to shard data
- We can have different rate limiters for different API's. eg- for a Url Shortener service we can have different limiters on createUrl and deleteUrl API

So , when a user sends a request it first goes to the load balancer and then
it goes to appropriate server. Since different servers have different rate limiters , so there is inconsistency
User can reach limit in one server but can still pass through another server

One work around is that we can use sticky sessions. Request of a particular user will go to particular server only.
But the problem with this approach is that if a server is down then a set of users will be down also

Another way is to use a central storage service such as redis, which was already explained in algorithm. It will work fine but increase some latency.

#### Hybrid Model
We can use a hybrid model. We can create different group of servers like G1,G2,G3....
Each group will have some servers. So one group will handle a range of userIds. Eg G1 has 1-5000 userIds,
G2 has 5000-10000 etc. Each group will have its own Redis storage also.

So when request comes, load balancer forwards it to specific group, but inside group it can go to any server.
If one server is down, others can handle the load. Each group can scale up and down accordingly.


![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Rate_Limiter.drawio.png)


- This image is for only one group of servers
- Load balancer sends request to rate limiter
- Rate limiter gets rules from cache then based on those rules it checks on redis
- If accepted then it sends request to servers or else it rejects request by error 429 (too many requests)
