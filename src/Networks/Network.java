package Networks;

import Edges.Link;
import Routers.Router;

import java.util.*;

public abstract class Network {
    protected Map<Integer, Router> routers;
    protected Set<Link> links;
    private Random random;
    private int n;

    protected Network(){}

    protected Network(int n){
        this.n = n;
        routers = new HashMap<>();
        links = new HashSet<>();
        random = new Random();
        for (int i = 0; i < n; i++)
            routers.put(i, new Router(i));
    }

    public Map<java.lang.Integer, Router> getRouters() {
        return routers;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public Router getRandomRouter(){
        return routers.get(random.nextInt(n));
    }
}


