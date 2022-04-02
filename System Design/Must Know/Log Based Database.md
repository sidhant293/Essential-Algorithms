
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

