# News Feed Design

News feed is simlilar to posts which are shown on instagram or facebook

## Data

- DAU- 1 Billion
- 1 User Follows - 500 Users
- Feed fetch for 1 user - 10 times
- Feed fetch for DAU - 10^10

- Size of 1 post - 1 KB
- And there are 500 posts in feed for 1 user so 500KB
- Data for DAU - 500TB

## API 

#### Get news feed

```http
  GET /feed/${userId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `UUID` | **Required**. Id of user |
| `options` | `json` | **Optional**. A json object that contains
| | | count- max number of posts that can be returned  |
| | | afterPostId- recent posts after this Id |


#### Post news feed

```http
  Post /feed/add
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `request body`      | `json` | **Required**. Body contains |
| | | userId- id of user who is making request |
| | | media- img or video data |
| | | description- description of post |

## Database Design 

#### User Table

| UserId | Email | DOB| Gender | ... |
| :---  | :--- | :----| :---   | :--- |

#### Follower Table

| UserId | FollowId | 
| :---  | :--- | 

one UserId will have many Followers

#### Post Table

| PostId | UserId | CreateDateTime| Description | 
| :---  | :--- | :----| :---- |

one UserId will have many Posts

#### PostMedia Table

| PostId | MediaId |
| :---  | :--- | 

one PostId will have many Media

#### Media Table

| MediaId | Url | CreateDateTime| Type (photo/video) | 
| :---  | :--- | :----| :---- |


## High Level Design 

Feed service will have two parts feed publish and feed generation

## Publish Feed

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/News_Feed_Publish.drawio.png)

### Web Servers
They authenticate all incoming requests. Only vaild users should be able to post.
Rate Limiting is also done here. Only specific number of posts are allowed within a peroid of
time. This prevents spamming.

### FanOut Service
FanOut can be implemented in two ways 1) Push-Based 2) Pull Based

#### Push Based
Updates will be pushed to users

**Pros**
- Real Time Updated
- Pre computer feed, fast reads

**Cons**
- Users which have many followers, push based is time consuming and reduces performance
- Waste of resources for inactive users

#### Pull Based
Users will pull updates after specific peroid of time. System will generate feed and then users will view it

**Pros**
- No waste of resources for inactive users
- No problem if users have many followers

**Cons**
- Slow reads, takes time to generate feed

One thing we know is that users need to have minimum latency. They want updates realtime. Fast reads are required.
So we will many use Push based approach with a slight modification.

Normally push based approach will be followed but for famous people like celebrities this approach is quite an overhead.
For celebrities we will use pull based approach. Celebrity posts data will be stored in a seprate DB. CDN will also be used for people who are
famous all around the world.

Flow of this approach is->
- When a celebrity posts something just store it in celebrity DB.
- When a normal user posts something call post service to store data
- Get list of followers (userId's only)
- Get last login data of users.
- Filter out inactive users (Feed for them will be computed when they login).
- Send postId and followers list to queue
- Workers get data from queue and start populating news feed cache.

Cache uses LRU. Data from cache will be persisted into disk at regular intervals so 
that we can build cache again in case of failures.

Key value pairs will be stored in cache.

Key will be userId

Value will be an array of objects like

```
[
    {postId,createrId,TimeStamp},
    {postId,createrId,TimeStamp},
    {postId,createrId,TimeStamp},
    ...
]
```
CreaterId- UserId of creater

Only 20 recent posts will ve stored in cache for a particular user.
Rest will be pushed to disk. In disk also feed older than 4 days will be removed.

## Generate Feed

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/News_Feed_Generate.drawio.png)

When a users asks for a request to get latest feed data then->
- As feed has been precomputed get that feed from news feed cache
- Check for updates in celebrity cache, fetch them also
- Merge the data
- Get user details (who posted a specific post) from user cache and get posts data from posts cache
- Return the response
