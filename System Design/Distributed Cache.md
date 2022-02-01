
# Distributed Cache

- Cache should store data in TB

Lets say we want to store 30TB of data. Now a production
level machine can have RAM of 128GB.

So number of machines needed would be 30*1000GB/128GB=235
These are minimum number of machines required. More machines will be required
if QPS is more.

- What about latency, consistency, availability.

Purpose of cache is to be faster than DB, so it should have low latency.
Now consistency should also be there(strongly consistence), if we are providing data to user, it should be correct data.
Availability should be there. But if data is evicted from cache using eviction policy then we can always hit DB.

It depends on use case wheather cache should be strongly consistence or partially consistence. Availability will change accordingly.





## Cache Eviction Policy

There are various cache eviction policies like LRU,LFU,FIFO etc.

We will use LRU here.(same as LRU leetcode problem)

### Important (Multithreading)

We know in LRU we use hashmap and doubly linked list. But our machine should
not be single threaded, it should work on multithreading environment to reduce latency.

So we need to use locks appropriatly to reduce latency and also have consistency.

- Concurrent reads can happen in same time. When a read happens, node from linked list is pushed to front but order doesnt matter which node goes to front because when removed only last one will be removed.
( Also in this case number of threads should be less than size of LRU cache or this wont work.
If number of concurrent threads are more than LRU size then node order might change and wrong node can get removed).
If interviewer conflicts in this statement, provide read locks also.

- Concurrent writes cannot happen at same time as it will conflict with read and other writes also. We need to provide locks in this case. We should not provide lock on whole hashmap as latency will increase.
Instead we can lock the appropriate bucket in hashmap where write operation is happening. So that reads/writes of other buckets dont suffer. (Concurrent HashMap)

