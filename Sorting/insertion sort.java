// consider two subsets, one is sorted and another is unsorted. we will take element from unsorted and put it in appropriate position in sorted subset.
// do this until length of unsorted is zero
// Time- O(n^2) , Stable Algo, Space - O(1)

//sorted subset contains only one element, as one elemnt is already sorted. 
//take first element from unsorted and insert in sorted

for(int i=1;i<n-1;i++){
  //value contains element of unsorted subset
  int value=arr[i];
  int j=i-1;
  //find right place in sorted subset to inset value, by shifting elements to right
  while(j>=0 && arr[j]>value){
    arr[j+1]=arr[j];
    j--;
  }
  arr[j+1]=value;
}

//this algo will work best in case of stream of incoming integers
//also it will give less iterations in almost sorted arrays
