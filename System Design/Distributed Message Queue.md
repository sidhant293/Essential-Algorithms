
# Distributed Message Queue

So we want to design a system which takes up messages from the producers and
send them to consumers at scale.

System should be scalable, highly available, high performance and durable(doesnt looses data)

One thing to note here is, this is not like publisher subscriber (message published goes to multiple subscribers)

One producer will give message to only one consumer , but multiple producers and consumers can also be configured.
 
## 1 Producer - 1 Consumer

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Message_Queue_1-pro_1-con.drawio.png)

We know only one consumer and one producer is configured.
There is no point of making multiple threads, only one thread can do the work(interviewer may disagree)

Here we can make use of a polling method, producer will push message to the queue.
Thread will remove the message from queue and process and send it to consumer.

But what happens when producers and consumers increase
## Multiple Producer - Multiple Consumer

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Message_Queue_multi-pro_multi-con.drawio.png)

Now as producers and consumers increase (1-producer will send to only 1-consumer)
Push mechanism will be used.
We can't expect one queue to handle all the load. So multiple queues should be created.
Also multi-threading will be used. Queue created will be synchronized.

We can increase or decrease number of queues depending on the load.

One load balancer will be used in front of the application, when a producer sends a message , it can be routed to any queue.

When message pushed in queue, it will be also stored in DB so that is queue crashes data can be loaded again.

Multiple threads running will perform the following tasks->
- Pop data from the queue
- Check which sever it should be routed to
- Consult zookeeper for the details of consumer
- Send message to consumer and update in DB
- Also retry multiple times, if not consumed update in DB

Zookeeper will keep track of all the producers which are alive
If multiple instences of same consumer want to configured the load balancer should be present.

### Database

What type of databse should be used to store messages so that if a queue fails then messages can be recovered.

It will depend on various situations, since here we dont need ACID properties and also we may store unstructured data.

We can use a file system to store data. Data can be split across one 1hr each, that means every 1hr new file can be created where whole data of that hour will be stored
When data is pushed inside any queue, data will be inserted in file and its line number will be noted.

Later if we want to update data (as data consumed by consumer or error occured) we can simply binary search on file and get that record.
