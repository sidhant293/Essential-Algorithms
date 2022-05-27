# TypeAhead Design

- DAU = 10M
- 1 person = 10 searches/day
- Data of 1 query= 1char=1 byte. On average let each word has 5 chars and be average 4 words. 4 * 5 * 1= 20 bytes/query
- Data for 1 day= 10M*20*10 = 2GB 
- New queries/day = 20%
- Data for 1 year = 2GB + ( 2GB * 0.2 * 365 ) = 148GB

## API 

#### Get suggestions

```http
  GET /customsearch?query=word1+word2
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `query` | `string` | Words searched by user , concatenated by + sign  |

## HLD

We need to do two things-
- Gather data to update search results
- Return top 5 suggestions for each query

Simple solution is to store every word in DB along with its frequency and use sql to get data that matches the given prefix. But this won’t work for
large amount of data.

## Trie

                                                            root
                                                           /    \
                                                          /      \
                                                         b        w
                                                        / \        \
                                                       /   \        \
                                                      be    bu      wi
                                                     / \     |        \
                                                    /   \   buy        \
                                                   bee  bet  19        win
                                                   12   8              25
                                                   
 Each leaf node that is a proper word will have count of how many times it was searched
 
 But this approach will also become slow as for each node we need to traverse every child node in order to calculate top 5 most searched words
 
 ### Optimisations 
 - Limit the length of search prefix to some constant length like 20. After this limit it won't query trie. So earlier we needed to traverse prefix of length n. Now we traverse of length 25. Complexity O(n) --> O(20) = O(1)
 - Instead of calculating top search using every child, cache at every node. So at every node, top searches regarding that prefix would be caached that reduces complexity to O(1)

So at every node we store prefixes with their counts. Instead we can store pointers to the top result nodes. On every query, words at those pointers will
get directly returned.

Updates will also be easy, just replace the pointer.


                                                                 root   {(bee:12),(bet,8),(buy:19),win:25)}
                                                           /               \
                                                          /                 \
                           {(bee:12),(bet,8),(buy:19)}   b                   w {(win:25)}
                                                        / \                   \
                                                       /   \                   \
                                  {(bee:12),(bet,8)}  be    bu {(buy:19)}     wi {(win:25)}
                                                     / \     |                   \
                                                    /   \   buy                   \
                                                   bee  bet  19                   win
                                                   12   8                           25
                        
                                                         
#### CReate Diagram
                                                         
                                                         
