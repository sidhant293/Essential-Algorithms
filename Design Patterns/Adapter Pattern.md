## Adapter Pattern

Converts one interface to another which client expects.

---
![img](https://github.com/sidhant293/Essential-Algorithms/blob/main/Design%20Patterns/Assets/adapter_pattern.drawio.png)
---

Duck is what client expects

```java
public interface Duck{
  public void fly();
}
```

Eagle is what we have
```java
public interface Eagle{
  public void glide();
}
```

SmallEagle is adaptee
```java
public class SmallEagle extends Eagle{
  void glide(){
    System.out.println("Flying low altitude");
  }
}
```

This is adapter
```java
public class EagleAdapter extends Duck{
  Eagle eagle;
  public EagleAdapter(Eagle eagle){
    this.eagle = eagle;
  }
  
  void fly(){
    eagle.glide();
  }
}
```

```java
SmallEagle eagle = new SmallEagle();
EagleAdapter adapter = EagleAdapter(eagle);
// this will work as adapater's parent is duck
printDuck(adapter);
```
