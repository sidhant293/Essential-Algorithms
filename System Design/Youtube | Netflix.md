
# Youtube/Netflix System Design

- DAU- 80M
- Videos viewed by people in 1 day- 5
- 1% of DAU upload video each day

Data calculation - 1% * 80M * 300MB (avg. video size) = 240TB/day

This calculation is only for one format. Videos will be stored in multiple formats and multiple resolutions

#### As DAU is very high, we will have seprate flows for upload and stream

## Video Upload

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Youtube_Upload_Flow.drawio.png)

### Application Servers

- Application servers connected to UserDB and MetadataDB. 
- When a user wants to upload a file, first its metadata is required like video name, tags etc. Those things are uploaded first in MetadataDB.
- UserDB is a SQL structure but MetadataDB is NOSQL. This is because metadata might not have a rigid schema.
- Application servers also have their own load balancer

### Workers

- When a upload request is initialed by a users, the request goes to workers. Separate workers are assigned task to upload files as upload takes a hudge bandwidth so this task can't be given to normal servers. Other requests will get blocked
- Workers don't upload file as a whole.
- From the user's end only the video files is send in chunks (10sec video files) and those files are uploaded and saved into a file storage like S3.
- This is also done to reduce failures. If a upload fails in between then whole process doesn't need to start again. It can start after the last chunk present.

### Content Processors

- Content Processors are a collection of servers each having a specific task.
- Chunks from S3 are taken up and are broken into more shorter files. This process can also be done in parallel. No need to wait for one chunk to complete then go to another.
- Files are broken in more smaller pieces so that video processing can be done at faster rate
- The smaller chunks are then filtered for privacy, nudity etc. If any of this kind of content is present then process can stop here and user will be notified.
- Now video files will be converted into different formats and resolutions. 
- Video needs to be played on a variety of devices and different devices have different resolutions and support different formats.
- A matrix can be created which shows which format should be available in which resolutions

|| 144 | 240 | 360 | 480 | 720 | 1080 |
| :--   | :-- | :-- | :-- | :-- | :-- | :-- |
| Mp4 | `True` | `True`  | `True`  | `True`  | `True`  | `True`  |
| Avi | `True` | `True`  | `True`  | `False`  | `False`  | `False`  |

#### This is not correct value just an example.
- Advantage of this approach is that a new resolution or format can be easily added.
- All the steps mentioned above will be executed using a DAG (direct acyclic graph). It will take care of the dependencies.
- Steps such as conveting video files into various formats will be done in parallel as they don't have any dependency over each other.
- Two queues are present, a task queue and a worker queue.
- Task queue is a priority queue that has all the tasks that need to be done and worker queue contains all the free workers.
- Task is removed from queue and assigned to a free worker.

### CDN
- Content delivery network (CDN) has servers present in all over the world at specific locations
- These servers hold static content like videos, static webpages which dont change so often.
- It reduces the request time for users and data is provided very fast.
- Redundancy is also a use case. If some server at a location goes down, data can be delivered by other server.
- It also helps to geographically categorise data. Eg. If a new movie is launched in India, it will be placed in servers closed to India so that users have seamless experience.

### FLOW (Upload)
- We have an initial load balancer present which sends requests to either application servers or workers.
- Initailly request does to servers where partial metadata of video is stored
- Next request goes to the workers, who upload the file into S3 in chunks.
- S3 link and other payload is pushed to a message queue which again sends it to content processing service.
- Content Processing Service gets the file from S3 link, does its processing and processed files are stored in CDN
- Remaining metadata (video playtime, number of formats, resolutions etc) is pushed to a queue which sends it to application servers which update it to MetadataDB.
- Now file uploading is complete, notification service is called which notifies the user that file is uploaded and recommendation service gets called which uses ML/AI for recommendations


## Video Stream

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/System%20Design/Images/Youtube_Stream.drawio.png)

- When a user requests something it first goes to servers where request is validated. Validation is required for cases like age permission, private videos etc.
- If it is a search request, it will be routed to search service.
- When a user requests a video, request goes to CDN. Popular videos are cached in CDN as they serve high traffic. (20% of data receives 80% of traffic).
- If it is not present in CDN, request will go to storage service like S3 from where it will recieve video data.
- Search Service will provide with video links also. High reads occur on video data and we need a scalable database also so NOSQL can be used here. Eventually consistency can do.
- Analytics is done on each request. Videos that are more searched for will be included in CDN and if some videos are loosing their traffic they can be removed from CDN.
- Different CDN servers are placed in different geographical locations to reduce video latency. Content specific to a location will be placed in near servers.
