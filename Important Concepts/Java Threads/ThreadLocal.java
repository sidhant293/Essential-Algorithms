/* ***************************************USE CASE 1**********************************************
Suppose we have a userService in which there is a function birthDate() which gets birthdate from db and returns it in form of string. We perform it using threads.
*/

class UserService{

  private static ExecutorService threadPool=Executors.newFixedThreadPool(10);
  
  public static void main(String args[]){
    for(int i=0;i<1000;i++){
      int id=i;
      threadPool.submit(()->{
        String birthDate=new UserSerice().birthDate(id);
        System.out.println(birthDate);
      });
    }
  }
  
  public String birthDate(int userId){
    Date date=getFromDB(userId); // get date from db
    SimpleDateFormatter df=new SimpleDateFormatter("yyyy-MM-dd");
    return df.format(date);
  }
}

/*
Now each time a new object of SimpleDateFormatter will be created when birthDate() is executed. This is not right. To overcome this we can make SimpleDateFormatter a global object, 
but then it will not be thread safe. We can make it thread safe using locks but then it will slow down the performance. 
So we use ThreadLocal, it will create 1 object per thread. If we have thread pool of 10 threads, we will have 10 objects. Thus we can create instances specific to a thread which 
reduces memory and increases efficiency. 
*/

/*
So for a current thread, it will automatically create a new object. And if object is already created then it will get the existing object.
*/

class ThreadSafeFormatter{
  public static ThreadLocal<SimpleDateFormatter> df=ThreadLocal.withInitial(()-> new SimpleDateFormatter("yyyy-MM-dd"));
}

class UserService{  
    // same as above
    
  public String birthDate(int userId){
    Date date=getFromDB(userId); // get date from db
    final SimpleDateFormatter df=ThreadSafeFormatter.df.get();
    return df.format(date);
  }
}


/********************************************************************************USE CASE 2*********************************************
Now suppose we have a web server and for each request a thread is assigned. Flow of request is such that a user object needs to passed across 4 services.
                               user             user             user
   thread1-----     service 1 -----> service 2 -----> service 3 -----> service 4
   thread2-----     service 1 -----> service 2 -----> service 3 -----> service 4
   .
   .
   .
   
   Now common way to avoid passing of this user object is that to create a map , and every other service will take user object from this map. But as this process is 
   spilt across multiple threads, map needs to be thread safe. We can use concurrent hashmap or locks, but it will reduce efficiency.
   
   But if we use ThreadLocal, we can get thread specific objects. So we do not need synchronisation in this case.
   
*/

class UserContextHolder{
  public static ThreadLocal<User> holder=new ThreadLocal();
}

class Service1(){
  void process(){
    User user=getUser();  // got user obejct in first service
    UserContextHolder.holder.set(user); // set user in ThreadLocal (thread specific). 
  }
}

class Service2(){
  void process(){
    UserContextHolder.holder.get(); // get user from ThreadLocal (thread specific). 
  }
}



