
# When To Shard

Generally what happens is you have a database and sever is
connected to it. Now client makes a request to fetch data from database.

Initially it works well but then data increases. Table becomes larger and larger.

### Indexing

Now to read, generally we need to scan whole database. But to optimize reads we can do indexing.

Create indexes on columns, instead to reading whole, do binary search to indexes and have fast lookups.

It works well but when data increases 1M, 10M, 50M, 100M. Indexes also become larger.
So reads again becomes slower.

### Horizontal Partitioning

As data in one table is huge, we can horizontally divide data into
multiple tables (same serve). And use a partitioning key (usually primary key) to
hit the right table.

As reads now become faster but still there are many TCP connections made to same serve.
A single server cant handle that much.

We can introduce a caching layer but it will lead to inconsistency. If you are ok with that then good.
But at some point at time, still coneections will increase.

### Master Slave

Make replication of DB. One master which will handle all the writes but multiple slaves
where read requests will go. So connection to single server reduces.

#### Now we made reads faster but still writes have a problem

There is only one master. All writes are done to it only. When number of writes increases then
this will be a problem.

This problem can be solve without sharding for some applications.
Applications like food delievery apps or dating apps , we can have multiple different master for different zones like east, west.
Where data of one zone is not dependent on another

If not fesible then

### Sharding

It is partitioning of data along different servers.
It solves all the above problems but then there are downsides.

If partitioning is done within one server, we can still do transactions.

But in sharding -> transactions, rollbacks, commits, atomicity these things cant happen.
Forget about all these things.
