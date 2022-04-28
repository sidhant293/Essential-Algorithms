# Notification Service

Service should be –
-	Scalable
-	Highly Available
-	Low Latency
-	Durable (Particular notification sent only once)

## Initial Thoughts

Initially we can think of only one service which will take requests and send notifications to other users. Eventually increasing the requests and user base latency will increase so we can start decoupling the system.

When users will increase our service will be busy in sending notifications and it won’t have time to take requests. We will introduce a messaging queue.

CONSUMERS--  QUEUE --- FAN OUT 

Consumers will just take the requests and send it to queue. In this way new requests will always be taken. No waiting time will reduce. Queue on the other hand will send notifications to fan out service which will handle sending it to appropriate users. 
But it’s a lot of work for one service, we will decouple more.
