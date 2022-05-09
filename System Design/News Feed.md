# News Feed Design

News feed is simlilar to posts which are shown on instagram or facebook

## Data

DAU- 1 Billion
1 User Follows - 500 Users
Feed fetch for 1 user - 10 times
Feed fetch for DAU - 10^10

Size of 1 post - 1 KB
And there are 500 posts in feed for 1 user so 500KB
Data for DAU - 500TB

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



