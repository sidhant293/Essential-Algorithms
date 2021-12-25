/*
Generally threads are used to run tasks but if we want to run 100 new tasks then 100 threads need to be created. Instead we can create a thread pool of some fixed size
like 10.And assign tasks to it. When a thread finishes a task, then it till take up another

ThreadPoolExecutor-It is a high level API that enables us to run tasks in a multithreaded environment.
corePoolSize- Size of thread pool
maximumPoolSize- Size upto which thread pool can grow.
keepAliveTime- Threads which do not belong to core pool will live upto this amount of time.
*/

/*
1) Fixed ThreadPool- corePoolsize=maxPoolSize=n keepAliveTime=0
  A threadpool of fixed size is created.Rest of the threads will wait in queue. 
*/

class Task implements Runnable{
 public void run(){
  System.out.println("thread name: "+Thread.currentThread().getName());
 }
}

ExecuterService service=Executors.newFixedThreadPool(10)  //pool of size 10
service.execute(new Task());

/*
2) Cached ThreadPool- corePoolsize=0 maxPoolSize=MAX_VALUE keepAliveTime=60
  Pool will create new threads as needed. Reuses existing threads when available. After 60sec it will remove unused threads. 
  This uses synchronous queue which has only one slot. When slot is filled it will see wheather any thread is free, if free then it will resuse else create new.
*/

ExecuterService service=Executors.newCachedThreadPool()
service.execute(new Task());

/*
3) SingleThreadExecutor- It uses a blocking queue to store tasks. It has only one thrread. Executor ensures that is thread is killed, it gets recreated.
It also ensures that tasks run sequentially
corePoolsize=maxPoolSize=1 keepAliveTime=0
*/
  
ExecuterService service=Executors.newSingleThreadExecutor();
service.execute(new Task());
  
/* 
4) Scheduled ThreadPool Executor- Pool will execute tasks after some delay. It uses a delay queue to store tasks which will schedule tasks based on time delay.
corePoolsize=n maxPoolSize=MAX_VALUE keepAliveTime=0
*/
ScheduledExecuterService service=Executors.newScheduledThreadPool(10)
  service.schedule(new Task(),10,SECONDS) //runs after a fixed delay
  //(task,initialDelay,period,time unit)
  service.scheduleAtFixedRate(new Task(),15,10,SECONDS) //it will have a initial delay of 15 sec and then run after every 10 sec
    //(task,initialDelay,period,time unit)
  service.scheduleAtFixedDelay(new Task(),15,10,SECONDS) //it will have a initial delay of 15 sec and then run after only when task is complete it will wait 10 sec and then run
  
