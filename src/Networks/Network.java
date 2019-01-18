package Networks;

import Routers.Router;

import java.util.*;

public class Network {
    protected Map<java.lang.Integer, Router> routers;
    protected Set<UEdge> edges;

    protected Network(){}

    protected Network(int n){
        routers = new HashMap<>();
        edges = new HashSet<>();
    }

    public Map<java.lang.Integer, Router> getRouters() {
        return routers;
    }

    public Set<UEdge> getEdges() {
        return edges;
    }
}


