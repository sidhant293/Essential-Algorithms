
# Tinder Design

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Tinder_Design.drawio.png)

- Chat Service is used for chatting between different users
- Swipe Service is used to handle swipes and matching
- Recommendation Service shows profile recommendations
- Profile Service is used for user registeration and stores profile data


## Recommendation Service

Recommendations will be made on preferences of a user.

- Hard Preferences -> These preferences are used as a filter criteria for users.Strict filter will be applied on these preferences, if users didn't meet this requirement then they will be filtered out (DISTANCE,AGE,GENDER)

- Soft Preferences -> These preferences are used to rank users. List of recommended profies are created such that, users with high ranks appear first. (INTERESTS like singing,dancing  , ACTIVE USERS, BAD ACTORS- which swipe too much/less , PROGRESSIVE TAXATION- highly attractive profiles can be taxed for equality)


#### Important Points
- Recommendations need not to be realtime.
- Recommendations can be computed earlier and stored somewhere.
- User data should be stored in geo-shards,to reduce dataset.

### Geo Sharding

All of the user data will be divided into different shards based
on geographical locations. 

We also need to create a mapper service which will tell a user
that for him where the data related to him is present.We will give
it (lat,lon) location indexes and it will give us servers where data might be
present.  

Recommendation query will be fired only on the servers given by mapper.
Thus we reduced our dataset.

### Design of Recommendation Service

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Recomendation_Tinder.drawio.png)

Distributed asynchronus queue is used to trigger recommendations.
We are not making recommendations realtime as it will be
time consuming and a bad user experiance.

Async queue and the queue will carry requests of different
users.These requests will be send to Search/Rank service.

Search service will filter, rank and create a list of recommendations
for each user.It will do this with the help of mapper service.
Search service will query only the servers given by mapper service.

These list of recommendations will be stored in DB. Suppose a list of
200 recommendations is generated and stored in DB. From this list
30-40 users will be taken and stored in cache in advance.

- When user opens his app, request is send to recommendation service to get recommendations
- Recommendation service hits the cache and gives list of 30-40 recommendations. Also it hits DB in advance to populate further recommendations in cache.
- While swiping (user is scrolling through list), when list is about to get finished, app again hits cache to get list. Again when list is delivered, request is triggered to get further recommendations from DB in advance. If we are about to reach at the end of recommendations list in DB, then again recommendations are calculated 

#### DataBase Design

To store users data in geo-shards we can use RDBMS table like

- UserId- PK
- Location Data- SK
- Last Login Time- SK
- UserData (gender,age,interests...)- SK


To store recommendation list, we can use NOSQL and shard it on
UserId

- UserId- unique 
- Recommendation List - json contains users 



## Swipe Service

This service is used to handle swipes and what happens if
both users swipe right, i.e they get matched.

This service can be setup in multiple ways-

- Store the data according to geo shards. Each database will be setup according to the number of geo servers and mapping service will be used to get to the appropriate database
- Instead of geo shards, each city should have one database where swipe and match data of all users will be stored.

Now the problem with first approach is that swipe data of a user should be duplicated to multiple geo shards.
As we dont know where the other user will be present, so if other user also swipes right then both should get matched. This will only happen if swipe data of both users is present in same geo shard.

The other problem is, location of a user will not stay same. As a city is further divided into geo shards, if swipe data of a user is present in one shard and user changes his location (goes to work from home or vice versa), then his geo shard will change. He might not be able to match with other person.

If we use one database for a city then these problems will not occur. Duplication of data will not be required (special case if users live on border of cities).
And change of location within a city will also not be a problem.

This database can also be further sharded on primary keys to remove bottleneck.



### Design of Swipe Service

#### Database Design

We can use a key value storage (like AWS DynamoDB) to store swipe information of users.
Unique key will be combination of userId's of both users in sorted format

```
UserId1_UserId2:{
    updatedOn: Timestamp,
    likes:[
        {
            likerId: UserId1,
            likedId: UserId2,
            time: Timestamp
        },
        {
            likerId: UserId2,
            likedId: UserId1,
            time: Timestamp
        }
    ]
}
```

Likes Data older than one month can be removed using cron jobs which will run periodcially

A RDBMS instance will also be used to store only matched data of users

- UserId
- UserId
- Timestamp

PK is combination of userids which are matched. This DB will be stored so that we get to know matching information of each user and whome they can text.

#### Flow

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Swipe_Tinder.drawio.png)


We don't need to store the data of left swipes(rejections) as these profiles can be 
shown again later some point of time. 

Instead we will store data of right swipes only. When a person right swipes, data gets stored in key-value storage.

This data is also forwarded to Recommendation Service so that liker's profile can again be ranked and showed on top to other user.

When both users like each other's profiles then data is inserted into RDBMS, which contains only matched data.
This RDBMS data will be used so that users can chat to each other.

Key Value storage will be sharded based on unique key to remove single point of failer , similarly RDBMS will be 
sharded on primary key.

Swipe data of 20-30 profiles together can be sent to Swipe Service, no need to hit API after each swipe. Matching of two users need not to be in realtime, some
delay is ok.

