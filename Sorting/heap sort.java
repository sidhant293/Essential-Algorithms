/*
first build heap
then swap last element with first and reduce length of arr.
again heapify

Time-O(nlogn) Space-O(logn) or O(1) if heapify done with iteration
*/

void heapify(int arr[], int n, int i)  
   {
     int l=2*i+1,r=2*i+2,largest=i;
     if(l<n && arr[l]>arr[i])
       largest=l;
       else largest=i;
     if(r<n && arr[r]>arr[largest])
       largest=r;
     if(i!=largest){
         swap(i,largest,arr);
         heapify(arr,n,largest);
     }
   }

   void buildHeap(int arr[], int n)  
   { 
   for(int i=n/2-1;i>=0;i--)
   heapify(arr,n,i);
   }

   void heapSort(int arr[], int n)
   {
       buildHeap(arr,n);
       for(int i=n-1;i>0;i--){
           swap(0,i,arr);
           heapify(arr,i,0);
       }
   }
   
   void swap(int i,int j,int arr[]){
       int t=arr[i];
       arr[i]=arr[j];
       arr[j]=t;
   }
