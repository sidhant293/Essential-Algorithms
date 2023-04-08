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
