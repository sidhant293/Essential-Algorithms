/*
Composite Design Pattern
Strong Association. Entities can't exist independently
It imporves strucutre and readability of code
It has composite and leaf nodes.
Leaf nodes calculate result directly , composite ndoes forward request to leaves
*/

/*
                                            Computer
                                           /        \
                                          CPU       Peripheral
                                         /    \       /     \
                                MotherBoard   HDD  Keyboard Mouse
                                  /      \
                                 RAM    Processor
                                 
*/

interface Component{
  String name;
  int price;
}
//all leaves
class Processor implements Component{}
class RAM implements Component{}
class Mouse implements Component{}
....
  
// composite nodes
class CPU implements Component{
  List<Component> components; // its children (motherboard,hdd)
  
  calculatePrice(){} // price of CPU will be calculated using its childern
  
}
//***********************************************************************************************************************************************

/*
  Association
  Relationship between two classes.
  1-1,1-Many,Many-1,Many-Many
*/

//***********************************************************************************************************************************************

/*
Aggrigation
Weak Association. Entities can exist independently.
Aggrigation depicts "has-a" relationship. 
A has B. Cart has products.
Cart and products are independent entities but still has a relationship
*/

