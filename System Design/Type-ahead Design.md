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
                        
                                                         
## Deep Dive

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/TypeAhead.drawio.png)
                                                         

### Trie Sets

Each trie set has data present in a range. Trie sets can be partitioned into 26 different sets
one for each alphabet (a-z). But load on sets would be unfair as some words are more used than others.

So they are partitioned in such a way load is evenly distributed. Zookeeper will keep details of which trie set servers for what range.
eg **Trie Set 1 => a - ac** , **Trie Set 2 => ad - be** ...

If load on a trie set increases it can be further broken down.

A single trie set will have multiple servers having same copy of data. So if one server is down then other can serve.

Tries will be stored in server memory for fast lookups and processing. Servers will also be connected to a DB which gives persistent storage.
If server crashes then in new server trie can be rebuilt from DB

DB will be a NOSQL store like Cassandra as it has high availability than SQL and easy to scale and shard. Also we don't need transactional support which SQL provides. 

NOSQL stores have lower consistency than SQL but higher availability

Data will be stored like
```json
"prefix":{
    "count":258,
    "childNodes": ["childPrefix1","childPrefix2","childPrefix3"]
}
```

Each node, childNodes if it has any children and count of occurrence (if a proper word)

Starting from the root, using BFS whole trie can be build up easily and stored in server

When queries are asked, data needs to be updated also. That can't be done in same DB where reads are happening because in order to do so locks will be required which will lead to both decrease in read performance. 


### Query Log Storage

When a query is made it is also updated in log storage. Append strategy is used
in logs as insertion happens very fast.

Structure of logs will be
**Query | Timestamp**

When user types prefixes, possible suggestions will be returned to him. Either he would choose one of the suggestions or
else he would write the possible words and click on search.

When this proper search query is made, then only data is appended in logs. There is no need to log incomplete prefixes.
only when accurate search is made, importance of those accurate words will be considered.

### Aggregators

As logs are stored, we need to aggregate them and calculate count of words

As data is large we can do sampling. For every 1000 entires, 1 count will be updated. This will reduce all over sample size but still more searched words will have higher
relative count. This aggregated count is stored in DB. 

#### Process of aggregation and all other steps after this isn't realtime. Depends on how much recently updated data you need, these processess can run after each hour or day or even week. 


### Workers 

Workers just take aggregated data from DB and update them into tries.

Now here we follow master slave architecture. There will be slave trie sets exactly same like master but they don’t serve any server requests.

All the updates are done on these slaves and then they are switched with the masters. Slaves become masters previous masters clone from existing masters and receive futher updates.

This cycle goes on so that read performance isn’t suffered while updating data.
