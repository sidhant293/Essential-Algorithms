/*
for each node i->j
we pick an intermediate node k , such that dist(i->k)+dist(k->j)<dist(i->j)
TC-O(N^3) where N is no of vertices
Space-O(N^2)
*/

for (k = 0; k < V; k++)
{
    for (i = 0; i < V; i++)
     {
        for (j = 0; j < V; j++)
            {
               if (dist[i][k] + dist[k][j] < dist[i][j])
                    dist[i][j] = dist[i][k] + dist[k][j];
            }
     }
}
