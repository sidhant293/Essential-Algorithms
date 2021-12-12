/*
Each time take a vertex with in degree 0
Reduce the in degrees of all the nodes conencted to it
Repeat process
*/
// adj is list of list
int topo[] = new int[N]; 
int indegree[] = new int[N]; 

for(int i = 0;i<N;i++) {
    for(Integer it: adj.get(i)) {
        indegree[it]++; 
    }
}

Queue<Integer> q = new LinkedList<Integer>(); 
for(int i = 0;i<N;i++) {
    if(indegree[i] == 0) {
       q.add(i); 
    }
}

while(!q.isEmpty()) {
    Integer node = q.poll(); 
    for(Integer it: adj.get(node)) {
        indegree[it]--; 
        if(indegree[it] == 0) {
           q.add(it); 
        }
    }
}
