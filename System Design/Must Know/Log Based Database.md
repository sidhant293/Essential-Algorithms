
# Log Based Database

When key value pairs are stored in a cache, they eventually need
to persisted in disk also. Instead of updating each record, batch updates are
done on the disk to reduce I/O operations.

There can be two different write strategies

| Override  | Append   |
| :-------- | :------- |
| Unique keys | Duplicate keys |
| Less space | More space  |
| O(n) complexity as whole file needs to be scanned in order to update keys | O(1) complexity |
| Mutable data | Immutable data, last entry-> latest |

Since time complexity of Append strategy is far better than Override, we'll go with Append.

Read time of both strategies is O(n).

So now we need to reduce read complexity

## Indexes

Indexing drastically reduces the read time but it gives some overhead to write also.

As we know using append strategy data will be in arbitrary format

| Key  | Value   |
| :-------- | :------- |
| User 1 | 3 |
| User 5 | 6 |
| User 3 | 8 |
| User 2 | 12 |
| User 7 | 37 |

In Indexing, keys are stored in stored format in cache with value as offset of disk

| Key  | Offset   |
| :-------- | :------- |
| User 1 | 0 |
| User 2 | 20 |
| User 3 | 25 |
| User 5 | 30 |
| User 7 | 34 |

We then do binary search on a key in cache and find its offset and then directly go to
that location in disk. Superfast!!

In case of write new data is directly appended to disk but in index it is added in appropriate sorted order (thus the overhead). But it doesn't reduce write performance that much. 

## Optimization

As we are using append only strategy it will take a lot of storage.

Suppose a user does 50 transactions, so 50 times storage.Writing time will not suffer but reading time will increase.

The approach we can use is that we can have an active file. Append all data to it. And when file reaches some threshold limit like 100mb we can split file.

Again, create a new current file and append to that. Now we can also have a background thread which will merge these chunks into a single file.

As these files will have duplicates, we will take only the latest entry for each key. So, size of merged file will always be less than the sum of combined file sizes.
The merged file will again have index table to make read faster.
There will be one writer and multiple readers for disk level. As data is immutable, we don’t need locks.

So, from this we understood that if write request then route to active file and if read request then route to active file.

## Recovery

In case of server failure how can we recover data. Easy, data is present in disk. We can go through the chunks and again create key value pairs. But this will be slow.

Since we know chunks are immutable their value will never change. So, for each chunk if we create a hashmap then value of hashmap will also never change. Thus, we need not to iterate through chunks, we can use these hashmaps to create key value pairs. This will speed up recovery process.
