## Factory Pattern

Defines an interface for creating objects but let's subclass decide which class to instantiate 

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/Design%20Patterns/Assets/factory_pattern.drawio.png)

```java

public abstract class Pizza{
  // some fields and methods
}

```


```java

public class ConcreteIndianPizzaOne extends Pizza{
  // some fields and methods
}

```
Similaly ConcreteIndianPizzaTwo, ConcreteIndianPizzThree and ConcreteAmericanPizzaOne, ConcreteAmericanPizzaTwo, ConcreteAmericanPizzThree are also created

```java

public abstract class PizzaStore{
// anyOperation()
  public Pizza orderPizza(String type){
    Pizza pizza;
    pizza = createPizza(type);
   
    pizza.prepare();
    pizza.bake();
    return pizza
  }
  // factoryMethod()
  public abstract Pizza createPizza(String type);
}

```

```java

public class IndianPizzaStore extends PizzaStore{

  public Pizza createPizza(String type){
    if(type == "one") return ConcreteIndianPizzaOne();
    else if(type == "two") return ConcreteIndianPizzaTwo();
    else if(type == "three") return ConcreteIndianPizzaThree();
    else return null;
  }
}

```

Similarly make AmericanPizzaStore

```java
// create the pizza stores
PizzaStore indianStore = new IndianPizzaStore();
PizzaStore americanStore = new AmericanPizzaStore();

// if we want indian pizza
Pizza pizza = indianStore.orderPizza("one");

// if we want american pizza
Pizza pizza = americanStore.orderPizza("one");
```
