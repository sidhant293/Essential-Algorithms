/*
in every iteration, swap the adjecent elements such that the largest reaches end
Time-O(n^2) Space-O(1) Stable Sort

also use a flag. if flag is false then array is already sorted

can be used for almost sorted arrays
*/

for(int i=0;i<n-1;i++){
  boolean flag=false;
  //if previous element is greater then swap adjacent elements
  for (int j = 0; j < n-i-1; j++){
    if (arr[j] > arr[j+1]){
      flag=true;
      swap(arr,j+1,j);
    } 
  }
  //if no swap happened then it means array is already sorted
  if(!flag) break;
}
