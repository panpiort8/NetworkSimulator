package Networks;

import Edges.Link;

public class PathNetwork extends Network{
    public PathNetwork(int n){
        super(n);
        for (int i = 1; i < n; i++)
            links.add(new Link(routers.get(i-1), routers.get(i)));
    }
}
