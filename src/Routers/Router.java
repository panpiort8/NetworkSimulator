package Routers;

import Godernet.LinkRequest;
import Godernet.Link;
import Packets.MetaPacket;
import Godernet.Packet;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Router extends Thread{
    private final Logger LOGGER = Logger.getLogger(getClass().getName());
    private final Object locker = new Object();

    private final int rid;
    private Map<Integer, Link> neighsLinks = new HashMap<>();
    private Map<Integer, List<Integer>> graph = new HashMap<>();
    private Map<Integer, Integer> pathVector = new HashMap<>();
    private boolean isDistanceVectorActual = true;
    private AtomicInteger succeedPacketsNo = new AtomicInteger(0);

    private ConcurrentLinkedQueue<LinkRequest> linksRequests = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Packet> packetsRequests = new ConcurrentLinkedQueue<>();

    private Map<UEdge, UEdgeStatus> edgesStatus = new HashMap<>();
    private List<Packet> stackedPackets = new LinkedList<>();
    private Set<UEdge> stackedEdgesToForward = new HashSet<>();

    /*PUBLIC INTERFACE METHODS*/
    public Router(int rid){
        this.rid = rid;
        graph.put(rid, new LinkedList<>());
    }

    public int getRid() {
        return rid;
    }

    public void addLinkRequest(LinkRequest linkRequest){
        synchronized (locker){
            linksRequests.add(linkRequest);
            locker.notifyAll();
        }
    }

    public void addPacketRequest(Packet packet){
        synchronized (locker) {
            packetsRequests.add(packet);
            locker.notifyAll();
        }
    }

    public int getSucceedPacketsNo() {
        return succeedPacketsNo.get();
    }
    /*END OF PUBLIC INTERFACE METHODS*/


    /*MANIPULATING GRAPH METHODS*/
    private void updatePathVector(){
        // first: rid; second: bestNeighRid
        Queue<Integer> queue = new LinkedList<>();
        pathVector.clear();
        pathVector.put(getRid(), null);

        for(Integer neigh : neighsLinks.keySet()){
            pathVector.put(neigh, neigh);
            queue.add(neigh);
        }

        while (!queue.isEmpty()){
            Integer v = queue.poll();
            Integer neigh = pathVector.get(v);
            for(Integer x : graph.get(v)){
                if (!pathVector.containsKey(x)){
                    pathVector.put(x, neigh);
                    queue.add(x);
                }
            }
        }
        isDistanceVectorActual = true;
    }

    private void addUEdge(UEdge edge){
//        System.out.println(String.format("%s adding %s", this, edge));
        addDEdge(edge.getR1(), edge.getR2());
        addDEdge(edge.getR2(), edge.getR1());


    };

    private void addDEdge(Integer r1, Integer r2){
        if (!graph.containsKey(r1))
            graph.put(r1, new LinkedList<>());
        graph.get(r1).add(r2);
    }

    private void removeEdge(UEdge edge){
        throw new RuntimeException("Not yet!");
    }
    /*END OF MANIPULATING GRAPH METHODS*/


    /*FORWARDING PACKETS METHODS*/
    private void forward(Packet packet, Integer neighRid){
        if (!neighsLinks.containsKey(neighRid) || !neighsLinks.get(neighRid).forward(packet, neighRid))
            addPacketRequest(packet);
    }

    private void forwardMetaPackets(){
        for (UEdge edge : stackedEdgesToForward){
            MetaPacket packet = new MetaPacket(edge, edgesStatus.get(edge));
            for (Integer neighRid : neighsLinks.keySet())
                forward(packet, neighRid);
        }
        stackedEdgesToForward.clear();
    }

    private void forwardNormalPackets(){
        for(Packet packet : stackedPackets)
            forward(packet, pathVector.get(packet.getDestination()));
        stackedPackets.clear();
    }
    /*END OF FORWARDING PACKETS METHODS*/


    /*CONSUMING PACKETS METHODS*/
    private void consumeMetaPacket(MetaPacket packet){
        UEdge edge = packet.getEdge();
        LOGGER.finest(String.format("%s consuming metapacket %s", this, packet));
        UEdgeStatus currentStat = edgesStatus.get(edge);
        UEdgeStatus packetStat = packet.getStatus();
        if(currentStat != null && currentStat.newerThan(packetStat)) // packet is obsolete
            return;

        if(currentStat != null && currentStat.isEnabled() == packetStat.isEnabled()){ // just want to update time
            edgesStatus.put(edge, packetStat); // update packet status
            return;
        }

        // packet is up to date and needs to be forwarded
        edgesStatus.put(edge, packetStat);
        if(packetStat.isEnabled())
            addUEdge(edge);
        else
            removeEdge(edge);
        isDistanceVectorActual = false;
        stackedEdgesToForward.add(edge);
    }

    private void consumeNextLinkRequest(){
        LinkRequest request = linksRequests.poll();
        Link link = request.getLink();
        Integer neighRid = link.getMyNeighRid(this);
        if(link.isEnabled())
            neighsLinks.put(neighRid, link);
        else
            neighsLinks.remove(neighRid);
        consumeMetaPacket(new MetaPacket(new UEdge(getRid(), neighRid), link.isEnabled()));
    }

    private void consumeNextPacketRequest(){
        Packet packet = packetsRequests.poll();
        if(packet instanceof MetaPacket)
            consumeMetaPacket((MetaPacket) packet);
        else if (packet.getDestination()  == getRid())
            succeedPacketsNo.addAndGet(1);
        else
            stackedPackets.add(packet);
    }
    /*END OF CONSUMING PACKETS METHODS*/

    private void goForBreak(){
        //  It's made only for performance reasons.
        //  We don't need to care about satisfying any condition (no while loop).
        try {
            locker.wait();
        } catch (InterruptedException e) {
            interrupt();
        }
    }
    @Override
    public void run() {
        int linkRequestsLimit = 10;
        int packetRequestsLimit = 20;
        while (!Thread.interrupted()){
            int linkRequested = 0;
            int packetRequested = 0;
            while (linkRequested < linkRequestsLimit && !linksRequests.isEmpty()){
                consumeNextLinkRequest();
                linkRequested++;
            }

            while(packetRequested < packetRequestsLimit && !packetsRequests.isEmpty()) {
                consumeNextPacketRequest();
                packetRequested++;
            }

            if(!isDistanceVectorActual)
                updatePathVector();

            forwardMetaPackets();
            forwardNormalPackets();

            synchronized (locker){
                if(linksRequests.isEmpty() && packetsRequests.isEmpty())
                    goForBreak();
            }
        }
        LOGGER.info(String.format("%s: %s", this, graph));
    }

    @Override
    public String toString() {
        return String.format("r_%d", rid);
    }

    @Override
    public int hashCode() {
        return rid;
    }
}
