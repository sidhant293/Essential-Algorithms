# Chat System
A chat system like whatsapp, facebook messanger 

- DAU(Daily Active Users) - 500 M
- Average message by 1 user in a day - 40
- Total Messages in a day - 500M * 40 = 20B
- Size 1 Message - 100bytes
- Total Storage for 1 day - 20B * 100 = 2TB
- Storage for 5 years - 5 * 365 * 2TB= 3.6PB 

## Basic Design
-----         -------         -------
Users ------- Servers ------- Storage
-----         -------         -------
Basic design is that users are connected to servers. Sender send messages to server, server will store messages in storage and then route it to receiver.
Receiver will also send acknowledgment back that message has been received. 

## Message Handling
HTTP is client initiated protocol. Client can send requests to server but server can’t send request back to clients. So this can’t be used for chatting. But HTTP can be used for other services like login, logout etc.

### Long Polling
In long polling, a connection is made between client and server. Client repetitively asks the server “are there new messages”, “are there new messages”. Connection is open until a threshold is reached or a new message received, after that the process restarts.

But this approach has some problems-
No way for server to tell if client is disconnected 
HTTP is a stateless protocol. As there will be multiple chat servers and requests will be load balanced among those servers, we can tell which client will be connected to which server
This is not ideal for a real time application. It will lead to waste of resources for the users who don’t chat much as long polling will keep on making connections

### Web Sockets
Web sockets is a way in which client and server both can send messages to each other in an bi-directional asynchronous way. Web socket connection starts its life as a HTTP connections and then after well defined handshake it is upgraded to a web socket connection. These connections are persistent and managed on the server side.

## Database

### SQL vs NOSQL
- SQL gives consistency and transactional support. But these things are not needed for chat apps
- Transactional properties are not needed for chat messages and eventual consistency is also good for them. (As messages get delievered to users and get stored on phone so they don't generally hit DB. And after some amount of time messages are persisted in eventual consistent models)
- Key Value store is very easy to scale and has a low latency. In SQL if large data is present then indexes also become larger so reads and writes costly.

So messages to be stored in **NOSQL**

**HBase** is a column oriented DB. Multiple columns of data can be stored against one key.
It stores recent data in buffer and when buffer is full it dumps data into disk.

Key will be a combination of 2 userIds(two people chatting) *UserID1_UserID2*

Value will be messageID | from (userID) | content | link (img/video link) | created at (timestamp)

Messages are ordered by messageID. Thus messageID's should be unique and should appear sorted by time(i.e old ids should appear earlier than new ones)

A messageID generator service can be made globally which will assign messages ids to incoming messages but this will be a bottleneck.
Instead for each conversation (chat b/w 2 people or group) their will be a counter specific to that chat only.


