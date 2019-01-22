package Networks;

import Edges.Link;

public class CycleNetwork extends PathNetwork{
    public CycleNetwork(int n){
        super(n);
        links.add(new Link(routers.get(0), routers.get(n-1)));
    }
}
