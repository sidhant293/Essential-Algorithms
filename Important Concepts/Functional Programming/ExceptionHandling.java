/*
Suppose we have a Biconsumer which takes two numbers and prints their division. Now if an exception occures, where should we handle that exception 
*/

public class _ExceptionHandling {
    public static void main(String[] args) {

      BiConsumer<Integer,Integer> biConsumer= (a,b)-> System.out.println(a/b);
      biConsumer.accept(3/2);
        
    }
}

/*
One thing we can do is that implement try catch inside lambda. 
But that will be a contradiction to one line expression
*/

public class _ExceptionHandling {
    public static void main(String[] args) {

      BiConsumer<Integer,Integer> biConsumer= (a,b)-> {
        try{
          System.out.println(a/b);
        }catch(Exception e){
          System.out.println("exception occured");
        }
      }
      biConsumer.accept(3/0);
        
    }
}

/*
To handle exceptions correctly we can wrap one lambda inside another
*/

public class _ExceptionHandling {
    public static void main(String[] args) {

      BiConsumer<Integer,Integer> biConsumer= (a,b)-> System.out.println(a/b);      
      wrapperLambda(biConsumer).accept(3/0);        
    }
  
    private static BiConsumer<Integer,Integer> wrapperLambda(BiConsumer<Integer,Integer> biConsumer){
      return (a,b)->{
        try{
          biConsumer.accept(a,b);
        }catch(Exception e){
          System.out.println("exception occured");
        }
      }
    }
}

