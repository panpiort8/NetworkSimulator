package Routers;

import Networks.DEdge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

enum RequestType{
    DELETE, ADD
}

class GodRequest{
    private final RequestType type;
    private final Router router;
    GodRequest(RequestType type, Router router){
        this.type = type;
        this.router = router;
    }

    public Router getRouter() {
        return router;
    }

    public RequestType getType() {
        return type;
    }
}

public class Router extends Thread{
    private final int rid;
    private Map<Integer, Router> neighs = new HashMap<>();
    private Map<Integer, List<Integer>> graph = new HashMap<>();
    private ConcurrentLinkedQueue<GodRequest> godRequests = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Packet> packetsRequests = new ConcurrentLinkedQueue<>();
    private List<Packet> stackedPackets = new LinkedList<>();

    public Router(int rid){
        this.rid = rid;
        graph.put(rid, new LinkedList<>());
    }

    public void addDeleteRequest(Router router){
        godRequests.add(new GodRequest(RequestType.DELETE, router));
    }

    public void addAddRequest(Router router){
        godRequests.add(new GodRequest(RequestType.ADD, router));
    }

    public int getRid() {
        return rid;
    }

    private void addDEdge(DEdge edge){
        int r1 = edge.getR1();
        int r2 = edge.getR2();
        if (!graph.containsKey(r1))
            graph.put(r1, new LinkedList<>());
        graph.get(r1).add(r2);
    };

    @Override
    public void run() {
        while (!Thread.interrupted()){
            while (!godRequests.isEmpty()){
                GodRequest request = godRequests.poll();
                Router router = request.getRouter();
                if(request.getType() == RequestType.ADD){
                    neighs.put(router.getRid(), router);
                    addDEdge(new DEdge(getRid(), router.getRid()));
                }
            }
        }
        System.out.println(String.format("%s: %s", this, graph));
    }

    @Override
    public String toString() {
        return String.format("r_%d", rid);
    }
}
