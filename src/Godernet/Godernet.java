package Godernet;

import Edges.Link;
import Networks.DynamicNetwork;
import Networks.Network;
import Packets.DataPacket;
import Routers.Router;
import javafx.util.Pair;

import java.util.*;
import java.util.logging.Logger;

/*Main class that is superior of network transfers*/
public class Godernet {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    private Network network;
    private Map<Integer, Router> routers;
    private int totalPacketsNo = 0;
    private int addedLinksNo = 0;
    private int deletedLinksNo = 0;


    public Godernet(Network network){
        this.network = network;
        this.routers = network.getRouters();
    }

    public void prepare(){
        for(Link link : network.getLinks())
            addLink(link);

        for(Router router : routers.values())
            router.start();
    }

    public void simulate(Long time) throws InterruptedException {
        SimpleTimer simulatorTimer = new SimpleTimer(time);
        SimpleTimer topologyTimer = new SimpleTimer(1000);
        Long delay = 10L;
        while (!simulatorTimer.isOver()){
            Thread.sleep(delay);
            produceNewPacket();
            if(network instanceof DynamicNetwork && topologyTimer.isOver()){
                changeTopology();
                topologyTimer.restart();
            }
        }
        forceStop();
        waitForRouters();
    }

    public void forceStop(){
        for (Router router : routers.values())
            router.interrupt();
    }

    public void waitForRouters() throws InterruptedException {
        for (Router router : routers.values())
            router.join();
    }

    public List<Pair<String, Object>> getStatistics(){
        int succeedPacketsNo = 0;
        long totalTime = 0;
        long maxTime = 0;
        List<Pair<String, Object>> stats = new ArrayList<>();
        for (Router router : routers.values()){
            succeedPacketsNo += router.getPacketsArrivals().size();
            for(Pair<DataPacket, Long> pair : router.getPacketsArrivals()){
                long time = pair.getValue() - pair.getKey().getBirthMillis();
                totalTime += time;
                maxTime = Math.max(maxTime, time);
            }
        }
        float avgTime = (float)totalTime/succeedPacketsNo;
        stats.add(new Pair<>("total_packets", totalPacketsNo));
        stats.add(new Pair<>("succeed_packets", succeedPacketsNo));
        stats.add(new Pair<>("avg_time", avgTime));
        stats.add(new Pair<>("max_time", maxTime));
        stats.add(new Pair<>("avg_deg", 2D*network.getLinks().size()/routers.size()));
        if(network instanceof DynamicNetwork){
            stats.add(new Pair<>("deleted_links", deletedLinksNo));
            stats.add(new Pair<>("added_links", addedLinksNo));
        }
        return stats;
    }


    private void produceNewPacket(){
        totalPacketsNo++;
        Router starter = network.getRandomRouter();
        Router destination = network.getRandomRouter();
        starter.addPacketRequest(new DataPacket(destination.getRid()));
        LOGGER.fine(String.format("new packet from %s to %s", starter, destination));
    }

    private void changeTopology(){
        DynamicNetwork dynamicNetwork = (DynamicNetwork)network;
        dynamicNetwork.changeTopology();
        List<Link> addedLinks = dynamicNetwork.pollAddedLinks();
        List<Link> deletedLinks = dynamicNetwork.pollDeletedLinks();
        LOGGER.fine(String.format("added edge: %s", addedLinks));
        LOGGER.fine(String.format("deleted edge: %s", deletedLinks));
        addedLinksNo += addedLinks.size();
        deletedLinksNo += deletedLinks.size();
        for(Link link : addedLinks)
            addLink(link);
        for(Link link : deletedLinks)
            deleteLink(link);
    }

    private void addLink(Link link){
        link.getR1().addLinkRequest(new LinkRequest(link));
        link.getR2().addLinkRequest(new LinkRequest(link));
    }

    private void deleteLink(Link link){
        Link oldLink = link.getR1().getNeighsLinks().get(link.getR2().getRid());
        oldLink.disable();
        link.getR1().addLinkRequest(new LinkRequest(oldLink));
        link.getR2().addLinkRequest(new LinkRequest(oldLink));
    }

}
