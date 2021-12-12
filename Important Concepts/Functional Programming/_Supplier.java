/*
Supplier only returns a value
*/

public class _Supplier {
    public static void main(String[] args) {
        Supplier<List<String>> getNames= ()-> List.of("sid1","sid3","sid3","sid4");
        System.out.println(getNames.get());
    }
}
