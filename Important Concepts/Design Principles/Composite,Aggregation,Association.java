/*
Composite Design Pattern is a Structural Design Pattern 
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
