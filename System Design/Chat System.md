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
HTTP is a stateless protocol. As there will be multiple chat servers and requests will be load balanced among those servers, we can't tell which client will be connected to which server
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

## High Level Design

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/chat_system.drawio.png)

### One on One Chat

- Users get connected to different chat servers
- Chat servers send messages to sessions service which knows which user is connected to which server
- Sessions service send messages to message service
- If user is online (connected to a chat server - figured out by sessions service) message is saved in DB. Responds success to sessions service and message is send to fanout service.
- Fanout service sends messages to correct server and the server sends it to user
- If user is offline, message is saved to Unread DB. In this DB all messages which are yeh to recieved by user is stored
- Whenever a user comes online, it first makes a request to message service to get all the unread messages
- In case of assets like images or videos, request is first send to assets service to upload an asset. When asset is uploaded a url is responded back by service.
- The message (if any) and url are send to chat servers and same process follows
- This uploading of asset is done separately so that it doesn't put load on the existing flow

### Group Chat

- User in a group sends a message. Message through chat servers go to group service.
- Group service finds out all the users present in a group
- Data from group service is forwarded to sessions service which finds out which user is online and connected to which server
- Then it goes to message service where group messages data is updated and unread DB of offline users is populated
- Messages to online users are send to Fanout service

### Last Sceen / Online Status

- There will be hundreds of people in someone’s contact list and status should be visible to all
- But most of contacts don't need status, only those who are currently chatting or have opened someone’s chat profile want status info
- A presence server is present which holds information **user | lastSceen**
- Whenever a user performs some action like sends message, reads message. Last sceen of that user is updated.
- But somethimes network disconnect also happens. So presence server sends heart beats to each user after every 3 seconds (can be any time)
- If a user doesn't responds with in 10 seconds, it is marked offline
- In order to check if User A wants to see User B status. When profile of User B is opened, a get request is send every 8-10 second to update status on APP
