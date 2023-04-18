# DataBase From Scratch

Lets start from scratch. To make a database we need our data to be saved somewhere. Let us save this data in a file in key value fashion.

Key will be like a primary key and value will be a like a row in db stored in any format(ex comma seprated, json, byte code..)

```
key1 '{"attribute1" : "value1", "attribute2" : "value2"}'
```
- Adding in DataBase will be a simple append operation in log file.
- Updating an entry will also be a simple append operation in log file. So more recent entries will be vaild than older.
- To get a value from database just scan the file backward until entry is found.

So here database insert and update have a very good performance but get has a terrible performance.

So inorder to improve db's performance we need an **index**.
Any kind of index *slows* the writes as index also needs an update but it speeds up the reads.

### Hash Index

So a key-value in memory map is created. Key will be the primary key and value will be the offset in file where actual value is stored.

Whenever append happens hash index will also be updated with the offset of log file. In case of a get we will dirrectly get the offset from hash index and then go to that location in file and read the value.

As more and more data is stored in file so eventually it will become larger.

One way to handel this is to break the log file into  segments when it reaches a certian size. Writes will be made on a new file. Older files will be merged and duplicate values will be discarded and only most reacent key will be stored.

Merging of segments can be done in background while new log while will be getting all the wrote requests. 

Each segment log file will have its in memory hash map. For a get request check the hash index of the most recent file then second most recent file ....

Some important points-
- Incase of deletes dont delete from a log file but just append in log file marking the entry as invalid
- If system crashes then in memory hash maps are lost. One way to recover is to recreate index from the log file but it will be time consuming. Faster way to do is to save snapshot of hashmaps on disk so that they can be quickly loaded in memory.

Append-only strategy is better than in-palce update strategy -
- As append-only is immutable, concurrent reads can happen easily.
- Appending and then merging log files is much faster than inplace writes
- Crash recovery is easy. We dont have to think about use cases when a value was being updated in place and crash occured.

### Hash Index Problem
- Hash index must fit in memory, if we have a large number of keys this wont work. Also when keys grow then more collisions occure so maintaining hash index is more of a overhead.
- We can perform range queries on hash indexes

## SSTables and LSM Trees

So above we have sceen that the data in log files is in order in which they are written which makes them hard for ranged queries.

If data in files is in sorted format of keys lets see what will be the advantages - 
- Merging two segmets will be simple and efficient even if files are bigger than the available memory. As files will be in sorted order, merge sort algorith can be applied. While merging if same keys appear then take the key which is more recent as it will have updated data.
- To find a key in file we no longer need to have an index which has all the keys in memory. We can create a sparse index which will take up less memory. From below example we canm get the gist that suppose key 250 will be between offset100 and offset253

```
.
.
key211 offset100
key310 offset253
key960 offset534
.
.
```

So to get data to be in sorted order we will use data structures like AVL trees or Red Black Trees.

Our database will now work as follows-
- Store an in memory balanced tree data structure which is sometimes known as *memtable*
- When *memtable* reaches a certain size presist the data in disk as SSTable(which will be in sorted format). Writes can continue to new *memtable* instance.
- To read from DB, first seach memtable then most recent on disk segment, second most recent on disk segment...
- In background merging process of segments will happen.

There is now only one problem if databse crashes then recent writes which are present only in memory will be lost. To solve that we will have a seperate log file which will write in just append only fashion. This log file wont be sorted, it is just maintained in case DB crashes

Also there will be time consumed on looking for an enetry which does not exist in DB. To solve that we can use bloom filter. Bloom filter can tell us that key does not exit in DB

## B-TREES

B-Trees is the data structure which is usually used in DBs.

B-Trees are similar to LSM but as LSM have varibale size segments, B-Trees have fixed size segments (usually 4KB)

A page contains several keys and references to child pages. Page can contain value of keys inline or reference to where data is stored. 

Data is stored in ranges. eg

```
        ref|100|ref|200|ref
         /       |        \
        /        |         \
       ...       |          ...
            120|val|121|val
```

For an insert, update or get query we go to the root page, root page will redirect to child page... eventually we'll reach the leaf page where we can find the value.

During insert if the the range of page can't encompass the value then page is broken down into 2 pages.

This algorith ensures that keys always have a depth of logN.

The number of child references which a page can contain is known as *branching factor*. A 4lvl tree of size page size 4KB each with a branching factor of 500 can store upto 256TB

In order to make B tree resilient to crashes a *write ahead log* is also present. Its an append only log, if database crashes then it can be easily recoverd from reading the log

##B-Tree vs LSM-Tree

LSM are faster for writes but B-tree provides a better read.

In LSM we just need to append to log but in B-Tree somethimes multiple pages can be changed during writes.

LSM provides slower writes because sometimes compaction process interferes with the performance of reads.

Main advantage of B-Tree is tat each key is present exactly once in B-Tree but in LSM there can be multiple copies of the same key. Thus B-tree simlpy provides strong transactional semantics.
