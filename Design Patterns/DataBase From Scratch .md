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

Append stratergy is better than update from here ()

So inorder to improve db's performance we need an **index**.
Any kind of index *slows* the writes as index also needs an update but it speeds up the reads.

