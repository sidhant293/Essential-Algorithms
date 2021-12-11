/* 
for each index find the minimum element and  replace it with current index
Time-O(n^2) Space-O(1) Unstable Sort

*/

for(int i=0;i<n-1;i++){
  int min_indx=i;
  for(int j=i+1;j<n;j++){
    if(arr[min_indx]>arr[j]) min_indx=j;
  }
  swap(arr,min_indx,i);
}
