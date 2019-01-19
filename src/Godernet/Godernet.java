package Godernet;

import Networks.Network;
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

    public Set<Link> getEdges(){ return network.getLinks(); }

    public void removeEdge(Routers.DEdge edge){

    }

    public void start(){
        for(Link Link : network.getLinks()){
            Router r1 = Link.getR1();
            Router r2 = Link.getR2();
            r1.addLinkRequest(new LinkRequest(Link));
            r2.addLinkRequest(new LinkRequest(Link));
        }

        for(Router router : routers.values())
            router.start();
    }

    public void forceStop(){
        for (Router router : routers.values())
            router.interrupt();
    }

}
