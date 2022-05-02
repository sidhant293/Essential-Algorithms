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

## High Level Design

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/NotificatioService_highLvl.drawio.png)

-	Notification Service has only one job it takes the requests and sends them to message queue and responds with request accepted. This is done to decouple the system so that waiting time reduces
-	Queue then sends the request to validation and priority server. It validates whether request is correct and also priorities the request. E.g., OTP and transactional notification should have highest priority.
-	Now fan out service takes the request and finds the corresponding users to which notifications should be send. It also filters out the users on basis of preferences. E.g. promotional emails should not we send to users who have unsubscribed to it or at most x number of emails should be send to users in one day
-	According to which type of notification should be send to users, the message is pushed to that particular queue. Workers are present which have only one job to send user and notification data to appropriate handlers.
-	Handlers can be third party like MailChimp for email. Workers will keep track of whether notification send was successful or not. They will save each notification in DB, if send successfull then delete from DB if not retry after some time. This is done so that if a worker crashes then we can recover the data.
-	Analytics service is important here as it calculates how much notifications and send to each user and also what type of notification. This data is used to filter out users as discussed in fan out service

## Validation and Priority Service

Request will to be validated. Then it is inserted into one of the priority queue

-------------| P0 |----------------

-------------| P1 |----------------

-------------| P2 |----------------
                
-------------| Pn |----------------


There priority queues are fixed and have a bias. Data will get into one of the priority queue. Workers will be assigned to each queue and workers can autoscale based on load. Data from this service will be pushed to fan out service.

## Fan Out Service


