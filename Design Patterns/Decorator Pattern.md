## Decorator Pattern

Objects are wrapped with other objects which extend their functionality 

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/Design%20Patterns/Assets/Decorator_Pattern.drawio.png)

```java

public abstract class Beverage{
    String description;

    public String getDescription(){
        return description;
    }

    public abstract int cost();
}

```

```java

public abstract class CondimentDecorator extends Beverage{
    Beverage beverage;

    public abstract String getDescription();

}

```

#### CondimentDecorator extends Beverage so that original object never changes

```java

public class ConcreteBeverageOne extends Beverage{

    ConcreteBeverageOne(){
        description = "ConcreteBeverageOne";
    }

    @Override
    public int cost() {
        return 10;
    }
}

```

```java

public class ConcreteCondimentOne extends CondimentDecorator{

    ConcreteCondimentOne(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return this.beverage.getDescription() + " + ConcreteCondimentOne";
    }

    @Override
    public int cost() {
        return this.beverage.cost() + 3;
    }
}

```

```java

public class ConcreteCondimentTwo extends CondimentDecorator{

    ConcreteCondimentTwo(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return this.beverage.getDescription() + " + ConcreteCondimentTwo";
    }

    @Override
    public int cost() {
        return this.beverage.cost() + 1;
    }
}

```
#### Similarly more ConcreteBeverages and ConcreteCondiments can be created

```java
// new beverage created
Beverage beverage = new ConcreteBeverageOne();

// condiments wrapped over beverage
beverage = new ConcreteCondimentOne(beverage);
beverage = new ConcreteCondimentTwo(beverage);
beverage = new ConcreteCondimentOne(beverage);

// if cost() is called for beverage, cost will be recursivly calculated
```
