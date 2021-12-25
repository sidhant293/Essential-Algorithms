/*
********************************YIELD*****************************************************
This method stops the current thread and gives a chance to other threads for execution.
Yield provides a hint to scheduler , and it depends upon the scheduler if it accepts or rejects the hint
*/

class Test extends Thread{
  public void run(){
    for(int i=0;i<5;i++)
       System.out.println("Thread "+i);
  }
  
  public static void main(){
    Test t=new Test();
    t.start();
    Thread.yield();
    for(int i=0;i<5;i++)
       System.out.println("Main "+i);
  }
}

/*
It depends on scheduler when it accepts hint. If hint is accepted immidietly then output will be
Thread 0,Thread 1...
Main 0,Main 1...

If hint is accepted after some time, output may be
Main 0,Main 1
Thread 0,Thread 1...
Main 2....

When hint is accepted then current thread will only continue until other one completes.

If hint is rejected then both threads will run together(as usual)
********************************************************************************************************/

/********************************************************JOIN*********************************************
If thread wants to wait for another thread to complete its task, then join is used

Below you can see when thread t executes its task then only main thread will do its task. Because main thread will wait for t thread to finish its task.
*/

class Test extends Thread{
  public void run(){
    for(int i=0;i<5;i++)
       System.out.println("Thread "+i);
  }
  
  public static void main(){
    Test t=new Test();
    t.start();
    t.join();
    for(int i=0;i<5;i++)
       System.out.println("Main "+i);
  }
}
