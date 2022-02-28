
# Rate Limiter

System should be
- Highly Available
- Less Latency


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

Here as you know SortedSet will be shared by reference, so if multiple threads remove or add timestamps, then it would reflect in all other threads also.
So race condition won't happen.

#### Remember threads never run in parallel, they always context switch

eg if one thread is about to remove a timestamp, context switch happens and other threads removes old timestamps. Then again context switch happens and first thread
again beomces active, then see old nodes are already removed, no need of locks.

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
- If value < 10 , then increment counter and again calulate if < 10 then accept else reject

We caluated two times because multiple threads will be running. If two threads concurently reads the counter and calulate value, then both will get into.

But suppose if one calulates value, context switch and other also calulate - increment -accept. When first thread runs, it increments and again calulates then it might get rejected if > threshlod.

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
