/*
Graph implemented like src->(des,wt)
1-> (2,3) (3,4) (5,1)
2-> (5,3) (3,4)
implemented by array of lists  
*/

class Node{
  int ele;
  int wt;
}
//n is total number of nodes,src is source node
void dijkstra(List<Node> graph[],int src,int n){
  int dist[]=new int[n];
  Arrays.fill(dist,Integer.MAX_VALUE);
  dist[src]=0;
  PriorityQueue<Node> pq=new PriorityQueue<>((a,b)->a.wt-b.wt);
  pq.add(new Node(src,0));
  
  while(!pq.isEmpty()){
    Node node=pq.remove();
    for(Node v:graph[node.ele]){
      if(dist[node.ele]+v.wt<dist[v.ele]){
        dist[v.ele]=dist[node.ele]+v.wt;
        pq.add(new Node(v.ele,dist[v.ele]));
      }
    }
  }
  
}
