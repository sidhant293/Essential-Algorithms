
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

- Concurrent reads can happen in same time. When a read happens, we can read from the hashtable directly and then asynchronously update the doubly linked list. An asynchronous queue can introduced in which all the keys will be pushed and nodes regarding those keys will be moved in front of linked list( keys might not be present as they can get evicted, so if not present then skip)

- Concurrent writes cannot happen at same time as it will conflict with read and other writes also. We need to provide locks in this case. We should not provide lock on whole hashmap as latency will increase.Instead we can lock the appropriate bucket in hashmap where write operation is happening. So that reads/writes of other buckets dont suffer. (Concurrent HashMap)

In this approach we can do two things, if write request comes we can directly remove from async queue and then update hashmap and push in queue , else we can push the write requests in async queue.

In first approach wrong data might get evicted but cache will be really fast and in the second approach data order will be correct but wrties might take a lot of time.
