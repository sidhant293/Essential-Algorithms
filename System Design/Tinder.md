
# Tinder Design

A brief description of what this project does and who it's for


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
