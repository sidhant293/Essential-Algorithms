
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
