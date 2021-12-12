/*
Function takes input and returns an output
*/

public class _Function {
    public static void main(String args[]){
        Function<Integer,Integer> incrementBy1= no-> no+1;
        Function<Integer,Integer> multiplyBy10=no->no*10;

        System.out.println(incrementBy1.apply(4));
        System.out.println(multiplyBy10.apply(2));
        
      //join two funtions
        Function<Integer, Integer> newFunction= incrementBy1.andThen(multiplyBy10);
        System.out.println(newFunction.apply(4));

//      *****************BI FUNCTION*********************
        BiFunction<Integer,Integer,String> compareTwoNumbers=
                (num1,num2)-> {
                    if(num1>num2) return "First";
                    else if(num2>num1) return "Second";
                    return "Equal";
                };
        System.out.println(compareTwoNumbers.apply(2,2));

        BiFunction<Integer,Integer,Integer> addTwoNums=(num1,num2)->num1+num2;
        System.out.println(addTwoNums.apply(5,6));


    }
}
