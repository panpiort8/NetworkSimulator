package Networks;

import Godernet.Link;
import Routers.Router;

public class CycleNetwork extends PathNetwork{
    public CycleNetwork(int n){
        super(n);
        links.add(new Link(routers.get(0), routers.get(n-1)));
    }
}
