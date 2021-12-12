/*
Consumer takes an input but doesnt return anything
*/

public class _Cosumer {
    public static void main(String[] args) {

        Consumer<Product> printProduct= p-> System.out.println(p.id+" "+p.name);

        printProduct.accept(new Product(1,"apple"));
      
//        ************************* BI CONSUMER*****************************

        BiConsumer<Product,Integer> checkAndChangeName=
                (product, id) -> {
                    if (product.id==id) product.name="Apple";
                    System.out.println(product.name);
                };

        checkAndChangeName.accept(new Product(6,"Mango"),6);
        //prints apple
    }
}

class Product{
    int id;
    String name;

    Product(int id,String name){
        this.id=id;
        this.name=name;
    }
}

