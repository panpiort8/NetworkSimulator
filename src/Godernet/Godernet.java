package Godernet;

import Networks.Network;
import Packets.DataPacket;
import Routers.Router;
import Routers.UEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Godernet {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    class Timer{
        private final long startTime;
        private final long period;
        Timer(long period){
            startTime = System.currentTimeMillis();
            this.period = period;
        }
        boolean isOver(){
            return System.currentTimeMillis()-startTime >= period;
        }
    }

    private Network network;
    private Map<Integer, Router> routers;
    private int totalPacketsNo = 0;
    private int succeedPacketsNo = 0;

    public Godernet(Network network){
        this.network = network;
        this.routers = network.getRouters();
    }

    public Set<Link> getEdges(){ return network.getLinks(); }

    public void removeEdge(UEdge edge){

    }

    public void prepare(){
        for(Link Link : network.getLinks()){
            Link.getR1().addLinkRequest(new LinkRequest(Link));
            Link.getR2().addLinkRequest(new LinkRequest(Link));
        }

        for(Router router : routers.values())
            router.start();
    }

    public Map<String, Object> getStatistics(){
        Map<String, Object> stats = new HashMap<>();
        stats.put("succeed packets", succeedPacketsNo);
        stats.put("total packets", totalPacketsNo);
        return stats;
    }

    private void produceNewPacket(){
        totalPacketsNo++;
        Router starter = network.getRandomRouter();
        Router destination = network.getRandomRouter();
        starter.addPacketRequest(new DataPacket(destination.getRid()));
        LOGGER.fine(String.format("new packet from %s to %s", starter, destination));
    }

    public void simulate(Long time) throws InterruptedException {
        Timer timer = new Timer(time);
        Long delay = 10L;
        while (!timer.isOver()){
            Thread.sleep(delay);
            produceNewPacket();
        }
        forceStop();
        for (Router router : routers.values())
            succeedPacketsNo += router.getPacketsArrivals().size();
    }

    public void forceStop(){
        for (Router router : routers.values())
            router.interrupt();
    }

    public void waitForRouters() throws InterruptedException {
        for (Router router : routers.values())
            router.join();
    }

}
