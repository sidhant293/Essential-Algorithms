/*
Synchronized keyword is quite rigid. Doesn't offers any mechanism of ordered waiting queue. If a thread exits synchronoized block then any other one can re enter, this leads
to starvation.

Reentrant lock provide synchronisation with greater flexibility
*/

void someMethod(){
  reentrantlock.lock();
  try{
    // shared resource
  }catch(Exception e){}
  finally{
    reentrantlock.unlock();
  }
}

/*
This lock allows thread to enter only once, then thread count is incremented. Every time lock is accquired count is incremented by one.
When unlock method is called count is decremented by one. Another thread cannot enter until count becomes 0.

This lock also has a fairness parameter. When creating object of this lock, if we pass true in constructor then fairness is activated. The thread which is waited the most
will enter next.
*/
