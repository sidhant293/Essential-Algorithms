## Decorator Pattern

Objects are wrapped with other objects which extend their functionality 

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
