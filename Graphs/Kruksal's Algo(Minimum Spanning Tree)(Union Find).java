/*
Sort all edges according to increasing order of wt
Take each edge and check if it didn't belong to same component, then consider it. Else don't
TC-O(ElogE) where E is edges
SC-O(N) where N is vertices
*/

//*********************************** Union Rank *************************
int findPar(int u, int parent[]) {
		if(u==parent[u]) return u;
		return parent[u] = findPar(parent[u], parent); 
}
	 
void union(int u, int v, int parent[], int rank[]) {
		u = findPar(u, parent); 
		v = findPar(v, parent);
		if(rank[u] < rank[v]) {
        	parent[u] = v;
    }
    else if(rank[v] < rank[u]) {
        	parent[v] = u; 
    }
    else {
        	parent[v] = u;
        	rank[u]++; 
    }
}

//*********************************************** MST**********************
// adj is a list of edges sorted by increasing order of wt
// MST cost is stored in costMst
int parent[] = new int[N]; 
int rank[] = new int[N];

for(int i = 0;i<N;i++) {
  parent[i] = i; 
  rank[i] = 0; 
}

for(Node it: adj) {
    if(findPar(it.getU(), parent) != findPar(it.getV(), parent)) {
        costMst += it.getWeight();  
     		union(it.getU(), it.getV(), parent, rank); 
    }
} 
        
