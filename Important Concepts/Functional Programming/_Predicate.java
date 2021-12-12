/*
Predicate takes input and returns boolean
*/

public class _Predicate {
    public static void main(String[] args) {
        Predicate<String> checkHash= s-> s.contains("#");
        Predicate<String> checkOne= s-> s.contains("1");

        System.out.println(checkHash.and(checkOne).test("csuibka#sin1"));

        System.out.println(checkHash.or(checkOne).test("csuibkasin1"));
    }
}
