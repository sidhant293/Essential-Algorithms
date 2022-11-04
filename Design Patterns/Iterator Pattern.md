## Iterator Pattern

Provides a way to represent objects without exposing underlying implementation 

---

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/Design%20Patterns/Assets/iterator_pattern.drawio.png)

---

```java
public class ConcreteMenuIterator1 implements Iterator<MenuItem>{
  MenuItem[] items;
  int indx = 0;
  
  ConcreteMenuIterator(MenuItem[] items){
    this.items = items;
  }
  
  public MenuItem next(){
    MenuItem item = items[indx];
    indx++;
    return item;
  }
   
  public boolean hasNext(){
    return item < items.length;
  } 
}
```

This is aggregate
```java
public Interface Menu{
  public Iterator<MenuItem> createIterator();
}
```

```java
public class ConcreteMenu1 implements Menu{
  MenuItem[] items;
  
  // constructor
  
  // items added to array
  
  public Iterator<MenuItem> createIterator(){
    return new ConcreteMenuIterator1();
  }
}
```

```java
public class ConcreteMenu2 implements Menu{
  List<MenuItem> items;
  
  // constructor
  
  // items added to array
  
  public Iterator<MenuItem> createIterator(){
    return items.iterator();
  }
}
```

```java

Menu menu1 = new ConcreteMenu1();
Menu menu2 = new ConcreteMenu2();
print(menu1);
print(menu2);

void print(Menu menu){
  Iterator<MenuItem> iterator = menu.createIterator();
  while(iterator.hasNext()){
    System.out.println(iterator.next());
  }
}
```
