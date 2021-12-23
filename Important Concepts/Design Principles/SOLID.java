/*
1) SRP-> Single Responsibility Principle
  A single class should encapsulate only one functionality of software , class should do only one thing.
*/

/*
Here we have a class book which only have one functionality. If you want to create a method printBook(), we can create a seprate class for that as it is a seprate functionality.
*/
class Book{
  String name,author;
  //getters and setters
}

// by making PrintBook class, our code becomes loosly coupled. New methods in PrintBook can be easily added
class PrintBook{
  printBookInConsole();
  printBookInOtherMedium();
}

/*********************************************************************************************************************************************************************
2) OCP-> Open Closed Principle
  Entities should be open for extension but closed for modification
*/

/*
So we have a class Guitar which has some properties. Now if new different guitar comes, so can create new guitar class and extend the existing one so that no code change is
required in existing class
*/

class Guitar{
  String make,model;
  int volume;
  //getters and setters
}

class GuitarWithFlames extends Guitar{
  String flameColor
  //getters and setters
}

/**********************************************************************************************************************************************************************
3) LSP-> Liskov Substitution Principle
  Derived classes must be substitutable by their base classes.
*/

/*
Below we can see if Reference of Animal is made then for dog it will work fine, but in case of tiger it won't work
*/

class Animal{
  feed();
  groom();
}

class Dog extends Animal{
  feed(); //override
  groom(); //override
}

class Tiger extends Animal{
  feed(); //override
  groom(); // we should throw exception in its implementation
}

Animal dog=new Dog();
dog.groom();  //ok

Animal tiger=new Tiger();
dog.groom();  //exception will be thrown

/*
To overcome this problemn we can introduce a class Pet which will take care of this problem
*/

class Animal{
  feed();
}

class Pet extends Animal{
  groom();
}

class Dog extends Pet{
  feed(); //override
  groom(); //override
}

class Tiger extends Animal{
  feed(); //override
}

/***************************************************************************************************************************************************************
4) ISP-> Interface Segregation Principle
  No class should be forced to depend on the methods it doesn't uses. One big interface can be split into smaller interfaces so that classes only use those interfaces 
  which are relevent to them
*/

/*
Suppose we have a class Shape and now Sqaure and Cube both extend it,to use its properties.
*/

interface Shape{
  calculateArea();
  calculateVolume();
}

class Cube implements Shape{
  calculateArea(); //override
  calculateVolume(); //override
}

class Square implements Shape{
  calculateArea(); //override
  calculateVolume(); //forced to override
}

/*
Instead we can split shape into smaller parts and then use it
*/

interface TwoDimensionalShape{
  calculateArea();
}

interface ThreeDimensionalShape{
  calculateVolume();
}

class Cube implements TwoDimensionalShape,ThreeDimensionalShape{
  calculateArea(); //override
  calculateVolume(); //override
}

class Square implements TwoDimensionalShape{
  calculateArea(); //override
}


/***********************************************************************************************************************************************************
5) DIP-> Dependency Inversion Principle
  Dependency should be based on abstract interfaces instead of concrete classes
*/

/*
We have a class CopyData which copies data from harddisk to pendrive. But now if another device comes then we need to create a seprate method.
*/

class CopyData{
  copy(HardDisk hdd,Pendrive pd);
}

/*
To overcome this we can instead use a interface Device. Now our system is loosly coupled. It will work for any type of device
*/

interface Device{}

class HardDisk implements Device{}

class Pendrive implements Device{}

class CopyData{
  copy(Device hdd,Device pd);
}


