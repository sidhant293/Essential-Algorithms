
# Key Value Store

Make key value store that stores a large amount of data.
It should be->
- Highly available
- Should Handle Failures
- Less Latnecy
- Strongly Consistence

A system should be designed such that it can handle failures.
Using CAP therorm then system can only be highly available or consistence.

We want this to be highly available. We can make it strongly consistent(partial consistency).


## Initial Design

So we can store data in cache inside servers. And also save key-value pairs
in log based DB. If data is present in cache then return else hit the DB.
We can make a write about cache(changes are made in cache first and then DB)

How data is stored in log based DB we can discuss it at end, first consider how hudge data is handled.

## Data Partition

Initially data can be stored in single server but as data increases we need more servers.
So we need to partition data. We can use userId as partition key.

Consistence hashing will be used to partition data equally.

//// Link of consistence hashing

This helps in automatic scale up and down of data.

## Data Replication

As data is partitioned across multiple servers if a server fails then
data related to that partition will be lost. 

In order to stop this we can replicate data to next N nodes in consistence hashing ring.

Next N nodes should be physical nodes not virtual.

So when a server goes down and load comes to next node, it can serve request quickly.

Also nodes in same datacenter can fail due to power outage, natural disaster etc. So data should be replicated
to different datacenters.

#### We are now totally handling availability of data. Now we need to focus on consistence

## Consistency

Now data is replicated in multiple nodes. If data is changed in one node and not in others then
there will be inconsistency. Data must be synchronoized across nodes.

In order to do that ->

when reading data , read from multiple replicas 

when writing data, write to multiple replicas

To make it in an equation if total nodes are N , R is number of nodes we read from and W is nodes we write to

For strong consistency R+W>N. When we read from many nodes, most of them will have
correct or updated data. Similarly when we write to many nodes, it is done so that when we read we can have updated data.
Still their will be some conflicts, we should create a consistency model

### Consistency Model 



