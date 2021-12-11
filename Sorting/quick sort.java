/*
  Time-O(nlogn) for randamized, n^2 worst case
  Space-O(logn)
  
  works better for smaller arrays
  
  also works if wanna find out kth largest or kth smallest element
*/

  void quickSort(int arr[], int low, int high)
    {
        if(low<high){
            int p=partition(arr,low,high);
            quickSort(arr,low,p-1);
            quickSort(arr,p+1,high);
        }
    }

     int partition(int arr[], int low, int high)
    {
       int pivot=arr[high];
       int i=low;
       for(int j=low;j<high;j++){
           if(arr[j]<pivot){
                //swap(i,j)
               int t=arr[j];
               arr[j]=arr[i];
               arr[i]=t;
               i++;
           }
       }
       //swap(pivot , i)
       int t=arr[i];
       arr[i]=arr[high];
       arr[high]=t;
       return i;
    } 
