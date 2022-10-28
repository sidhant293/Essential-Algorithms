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

## Abstract Factory

Abstract factory gives us an interface for for creating families of products. Using this interface we can decouple our code from actual factory that
creates products

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/Design%20Patterns/Assets/abstract_factory.drawio.png)

```java
public interface PizzaIngrediantFactory{
  public PizzaBase createPizzaBase();
  public Sause createSause();
  // other abstract methods
}
```

```java
public class IndianPizzaIngrediantFactory{
  public PizzaBase createPizzaBase(){
    return new IndianPizzaBase();
  }
  public Sause createSause(){
    return new IndianSause();
  }
  // other abstract methods
}
```

Similarly American will have their own Concrete Ingredient Factory to create Pizzas

```java
public abstract class Pizza{
  PizzaBase base;
  Sause sause;
  // other fields
  
  public abstract void prepare();
  
  // other methods
}
```

Concrete products will be created by ingredient factory

```java

public class ConcreteIndianPizzaOne extends Pizza{
  PizzaIngrediantFactory ingrediantFactory;
  
  ConcreteIndianPizzaOne(PizzaIngrediantFactory ingrediantFactory){
    this.ingrediantFactory = ingrediantFactory;
  }
  
  void prepare(){
    base = ingrediantFactory.createPizzaBase();
    sause = ingrediantFactory.createSause();
    // other details to prepare pizza
  }
}

```
Similaly ConcreteIndianPizzaTwo, ConcreteIndianPizzThree are created 
IndianPizzaIngrediantFactory will be used to create ConcreteAmericanPizzaOne, ConcreteAmericanPizzaTwo, ConcreteAmericanPizzThree

As pizza is returned by PizzaStore

```java

public class IndianPizzaStore extends PizzaStore{

  public Pizza createPizza(String type){
    PizzaIngrediantFactory ingrediantFactory = new IndianPizzaIngrediantFactory();
    
    if(type == "one") return ConcreteIndianPizzaOne(ingrediantFactory);
    else if(type == "two") return ConcreteIndianPizzaTwo(ingrediantFactory);
    else if(type == "three") return ConcreteIndianPizzaThree(ingrediantFactory);
    else return null;
  }
}

``` 

Similarly for american factory

Thus above we decoupled concrete object creation from factory
