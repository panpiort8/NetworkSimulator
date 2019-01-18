import Networks.Network;
import Networks.UEdge;
import Routers.Router;

import java.util.Map;
import java.util.Set;

public class Godernet {
    private Network network;
    private Map<Integer, Router> routers;

    public Godernet(Network network){
        this.network = network;
        this.routers = network.getRouters();
    }

    public Set<UEdge> getEdges(){ return network.getEdges(); }

    public void removeEdge(UEdge edge){

    }

    public void start(){
        for(Router router : routers.values())
            router.start();
        for(UEdge edge : network.getEdges()){
            Router r1 = routers.get(edge.getR1());
            Router r2 = routers.get(edge.getR2());
            r1.addAddRequest(r2);
            r2.addAddRequest(r1);
        }
    }

    public void forceStop(){
        for (Router router : routers.values())
            router.interrupt();
    }

}
