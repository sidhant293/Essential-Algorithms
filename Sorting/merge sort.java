/*
Time- O(nlogn) Space-O(n) Stable

merge sort is more prefered for sorting linked list 
*/

void merge(int arr[], int l, int mid, int r)
    {
        int n=mid-l+1,m=r-mid;
        int left[]=new int[n];
        int right[]=new int[m];
        
        for(int i=0;i<n;i++) left[i]=arr[i+l];
        for(int i=0;i<m;i++) right[i]=arr[mid+i+1];
        
        int i=0,j=0,k=l;
        
        while(i<n && j<m){
            if(left[i]<=right[j]) arr[k++]=left[i++];
            else arr[k++]=right[j++];
        }
        
        while(i<n){
            arr[k++]=left[i++];
        }
        
        while(j<m){
            arr[k++]=right[j++];
        }
    }
    void mergeSort(int arr[], int l, int r)
    {
        if(l<r){
            int mid=l+(r-l)/2;
            mergeSort(arr,l,mid);
            mergeSort(arr,mid+1,r);
            merge(arr,l,mid,r);
        }
    }
