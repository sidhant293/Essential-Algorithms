/*
to make a class immutable->
1) make its all fields final and remove setters- so that no can change value of fields
2) make class itself final- so that no one can extend and override its method and fields
*/

public final class Student{
  final private int id;
  final private String name;
  
  Student(int id,String name){
    this.id=id;
    this.name=name;
  }
  
  public int getId(){
    return id;
  }
  
  public int getName(){
    return name;
  }
}

/*
Now the problem with this is if Student has another field which is a class itself(eg Address). Then we can easily change contents of this class. But this should not happen
*/

public final class Student{
  final private int id;
  final private String name;
  //this field can easily be changed because this class has its own getters and setters.
  final private Address address;
  
  Address getAddress(){
    return address;
  }
}


/*
Now to solve this problem , we can do something like always return the copy of an object. So that if changes are made, original object doesn't change
To return a copy, we can use a copy constructor. This constructor will create a new object using existing object
*/

public final class Student{
  final private int id;
  final private String name;
  final private Address address;
  //returning copy of object
  Address getAddress(){
    return new Address(address);
  }
}

public Address{
  String street;
  String city;
  
  Address(String city,String street){
    this.city=city;
    this.street=street;
  }
  // a copy constructor, which creates new object
  Address(Address address){
    this(address.getCity(),address.getStreet());
  }
}
