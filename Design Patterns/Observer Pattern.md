## Observer Pattern

Observer Pattern is similar to publisher - subscriber pattern. 
Obervers observe the subjects

Observer pattern is different from publisher-subscriber pattern as Observers are aware of the Subject, also the Subject maintains 
a record of the Observers. Whereas, in Publisher/Subscriber, publishers and subscribers don't need to know each other. 
They simply communicate with the help of message queues or broker

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/Design%20Patterns/Assets/observer_pattern.drawio.png)


```java

public interface Subject{
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObserver();
}

```

```java

public interface Observer{
    public void update();
}

```

```java

public class WeatherConcreteSubject implements Subject{

    public List<Observer> observers;
    private int temperature;
    private int pressure;
    private int humidity;

    public WeatherConcreteSubject() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
       observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        for(Observer observer : observers){
            observer.update();
        }
    }

    public void measurementChange(int h, int p, int t){
        humidity = h;
        pressure = p;
        temperature = t;
        notifyObserver();
    }
}

```

```java

public class DisplayConcreteObserver implements Observer{

    private int temperature;
    private int pressure;
    private WeatherConcreteSubject subject;

    public DisplayConcreteObserver(WeatherConcreteSubject sub) {
        subject = sub;
        subject.registerObserver(this);
    }

    @Override
    public void update() {
       pressure = subject.getPressure();
       temperature = subject.getTemperature();
    }
}

```
#### Similarly more observers can be created

```java
// concrete subject is created and passed to observers
WeatherConcreteSubject weatherConcreteSubject = new WeatherConcreteSubject();

// new observers are created, no code change is required anywhere else
DisplayOneConcreteObserver displayOneConcreteObserver = new DisplayOneConcreteObserver(weatherConcreteSubject);
DisplayTwoConcreteObserver displayTwoConcreteObserver = new DisplayTwoConcreteObserver(weatherConcreteSubject);

// whenever weather data is updated, all observers which have subscribed to a subject are notified
weatherConcreteSubject.measurementChange(10,20,30);
weatherConcreteSubject.measurementChange(100,200,300);

```
