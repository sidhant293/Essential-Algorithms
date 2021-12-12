/*
Relax edges n-1 times where n is number of nodes
After relaxing, the distances you get should be minimum. But if you do one more iteration, and distance reduces then you have anegative cycle in graph
*/
//TC-O(n-1*E) where E are edges and n are vertices
//SC-O(n)
// here source is 0
//edges->(u,v,wt)
boolean doesNegativeCycleExist(){
        int dist[]=new int[n];
        Arrays.fill(dist,Integer.MAX_VALUE);
        dist[0]=0;
        for(int i=0;i<n-1;i++){
            for(int j=0;j<edges.length;j++){
                if(dist[edges[j][0]]<Integer.MAX_VALUE){
                    dist[edges[j][1]]=Math.min(dist[edges[j][0]]+edges[j][2],dist[edges[j][1]]);
                }
            }
        }
        
        for(int i=0;i<edges.length;i++){
            if(dist[edges[i][0]]<Integer.MAX_VALUE && dist[edges[i][0]]+edges[i][2] <  dist[edges[i][1]]){
                return true;
            }
        }
        return false;
}
