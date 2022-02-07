
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
value- contains value 
prev- pointer to previous node
next- pointer to next node 
```

### Offer

```python
offer(node e):
# as this function will run in multithreaded system so multiple threads can access same function at same time.
# If same node is already present at head then dont add it again as it will contain duplicates
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

### Purge
Purge operation is handled in bulk. When size of cleanup queue reaches beyond some threshold then purge is called

Purge is done using single thread only as it updates prev and next nodes, in order to use multiple threads
locks should be used and it will be complex. To avoid this we use only one thread

```python

purge():
# size_queue is an atomic integer
    if size_queue > threshold:
        
        accquire lock on DLL

        remove from queue and update prev & next pointer of that node

        remove lock
```

### Evict 
Nodes are removed from tail of DLL and also from hashmap as a batch operation

### Get
In get operation we will not use locks because performance will
suffer badly. As we aren't using locks, a strict ordering of nodes
is not preserved. But still we will have approximately least recently used nodes at tail of DLL

```python
get(key):
    #non blocking operation
    lookup in hashmap:
        if key found, we have a node e:
            offer(e)  #add node at head of DLL and in hashmap
            try purge()
        else:
            load from DB 
            create a node e
            put(e)
    return e.value
```

### Put
```python
put(node e):
    #as it is concurrent hashmap, only one thread will exceed
    Node ex=map.putIfAbsent(e)

    if we have existing entry:
        return ex
    else if absent:
        offer(e)
        #when map becomes larger than required size, remove data from tail and hashmap
        if size_map >= evict_threshold:
            evict some entries
    return e
```
