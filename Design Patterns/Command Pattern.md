## Command Pattern

Encapsulate different requests as an object and let queues and logs to execute objects and do undo operations

---

![alt text](https://github.com/sidhant293/Essential-Algorithms/blob/main/Design%20Patterns/Assets/command_pattern.drawio.png)

---

```java
public interface Command{
 public void execute();
}
```

```java
public class LightOnCommand implements Command{
  // this is a reciever which performs some action
  Light light;
 
  //constructor 
 
  public void execute(){
    light.on();
  }

  public void undo(){
    light.off();
  }
}
```

this is invoker
```java
class RemoteControl{
  Command[] commands;
  Command undoCommand;
  
 publlic RemoteControl(){
  Command noCommand = new Command()
  //create array of commands and set each to noCommand
  undo Command = noCommand;
 }
 
 void setCommand(int slot, Command command){
  commands[slot] = command;
 }
 
 public void pushButton(int slot){
  commands[slot].execute();
  undoCommand = commands[slot];
 }
 
 public void undoLastCommand(){
  undoCommand.undo();
 }
}
```


```java
Light light = new Light();
LightOnCommand command = new LightOnCommand(light);

RemoteControl remoteControl = new RemoteControl();
remoteControl.set(0,command);

remoteControl.pushButtom(0);
remoteControl.undoLastCommand();
```
