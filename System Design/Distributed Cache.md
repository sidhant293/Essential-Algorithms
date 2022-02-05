
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

## Multithreading
Existing LRU approach is ok but it will only work for single threaded systems.
We need to reduce latency of our system so multithreading should be introduced

DLL- Doubly Linked List

This is the multithreaded design ->
- Offer- Appends node to head of DLL and refers it to hashmap and adds previous existing nodes to cleanup queue
- Purge- Deletes some nodes from DLL which are present in cleanup queue
- Evict- Removes some node from tail of DLL , also removes them from hashmap
- Get- Lookup in hashmap
- Put- Creates new node and put in cache(hashmap+DLL)

The strucuture of an node in hashmap is
```
key- key of map
node- contains value 
prev- pointer to previous node
next- pointer to next node 
```

### Offer

```python
offer(node e):
# as this function will run in multithreaded system so multiple threads can access same function at same time. If same node is already present at head then dont add it again as it will contain duplicates
    if head!=e:  

        assign to c <-  e
        new node n(e)    
        # hasmap used is concurrent hashmap, so only one thread can perform write operation for same bucket
        if add n to entry in hashmap:

            add n to head of DLL
            # as multiple threads can do this, if one thread has already marked for cleanup then no need to do it again
            if c is not already null:

                set c.key to null
                add c to cleanup queue

```
