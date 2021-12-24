/*
Factory Design Pattern
The object creation logic is hidden. Client doesnt know how object is created for a particular class. Objects are dynamically created
*/

interface OS{}
Android implements OS{}
Windows implements OS{}
IOS implements OS{}
// now client wants objects of these classes

class Factory{
 public static OS getInstance(String name){
  if(name=="android") return new Android();
  else if(name=="windows") return new Windows();
  else if(name=="ios") return new IOS();
   return null;
 }
}

//now to get any object we can do

Os os=Factory.getInstance("android");

//*****************************************************************************************************************************************************************

/*
SingleTon Design Pattern
  Only one object should be created of a class.example Loggers,DB connections
*/

class SingleTon{
  private static SingleTon obj=null;
  
  private SingleTon{}
  
  public static SingleTon getInstance(){
    if(SingleTon.obj==null) SingleTon.obj=new SingleTon();
    return SingleTon.obj;
  }
}
