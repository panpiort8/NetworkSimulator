package Routers;

import Godernet.LinkRequest;
import Godernet.Link;
import Packets.MetaPacket;
import Packets.Packet;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Router extends Thread{
    private final int rid;
    private Map<Integer, Link> neighsLinks = new HashMap<>();
    private Map<Integer, List<Integer>> graph = new HashMap<>();

    private ConcurrentLinkedQueue<LinkRequest> linksRequests = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Packet> packetsRequests = new ConcurrentLinkedQueue<>();

    private Map<DEdge, DEdgeStatus> edgesStatus = new HashMap<>();
    private List<Packet> stackedPackets = new LinkedList<>();
    private Set<DEdge> stackedEdgesToForward = new HashSet<>();

    public Router(int rid){
        this.rid = rid;
        graph.put(rid, new LinkedList<>());
    }

    public void addLinkRequest(LinkRequest linkRequest){
        linksRequests.add(linkRequest);
    }

    public void addPacketRequest(Packet packet){
        packetsRequests.add(packet);
    }

    public int getRid() {
        return rid;
    }

    private void addDEdge(DEdge edge){
        System.out.println(String.format("%s adding %s", this, edge));
        int r1 = edge.getR1();
        int r2 = edge.getR2();
        if (!graph.containsKey(r1))
            graph.put(r1, new LinkedList<>());
        graph.get(r1).add(r2);
    };

    private void removeEdge(DEdge edge){
        throw new RuntimeException("Not yet!");
    }

    private void forward(Packet packet, Integer neighRid){
        if (!neighsLinks.containsKey(neighRid) || !neighsLinks.get(neighRid).forward(packet, neighRid))
            addPacketRequest(packet);
    }

    private void forwardMetaPackets(){
        for (DEdge edge : stackedEdgesToForward){
            MetaPacket packet = new MetaPacket(edge, edgesStatus.get(edge));
            for (Integer neighRid : neighsLinks.keySet())
                forward(packet, neighRid);
        }
        stackedEdgesToForward.clear();
    }



    private void consumeMetaPacket(MetaPacket packet){
        DEdge edge = packet.getEdge();
        System.out.println(String.format("%s consuming metapacket %s", this, packet));
        DEdgeStatus currentStat = edgesStatus.get(edge);
        DEdgeStatus packetStat = packet.getStatus();
        if(currentStat != null && currentStat.newerThan(packetStat)) // packet is obsolete
            return;

        if(currentStat != null && currentStat.isEnabled() == packetStat.isEnabled()){ // just want to update time
            edgesStatus.put(edge, packetStat); // update packet status
            return;
        }

        // packet is up to date and needs to be forwarded
        edgesStatus.put(edge, packetStat);
        if(packetStat.isEnabled())
            addDEdge(edge);
        else
            removeEdge(edge);
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
        consumeMetaPacket(new MetaPacket(new DEdge(getRid(), neighRid), link.isEnabled()));
        consumeMetaPacket(new MetaPacket(new DEdge(neighRid, getRid()), link.isEnabled()));
    }

    private void consumeNextPacketRequest(){
        Packet packet = packetsRequests.poll();
        if(packet instanceof MetaPacket)
            consumeMetaPacket((MetaPacket) packet);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            while (!linksRequests.isEmpty())
                consumeNextLinkRequest();

            while(!packetsRequests.isEmpty())
                consumeNextPacketRequest();

            forwardMetaPackets();
        }
        System.out.println(String.format("%s: %s", this, graph));
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
