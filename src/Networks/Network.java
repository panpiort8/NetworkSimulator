package Networks;

import Godernet.Link;
import Routers.Router;

import java.util.*;

public class Network {
    protected Map<Integer, Router> routers;
    protected Set<Link> Links;

    protected Network(){}

    protected Network(int n){
        routers = new HashMap<>();
        Links = new HashSet<>();
    }

    public Map<java.lang.Integer, Router> getRouters() {
        return routers;
    }

    public Set<Link> getLinks() {
        return Links;
    }
}


