package Godernet;

import Edges.Edge;
import Packets.Packet;
import Routers.Router;

public class Link extends Edge<Router>{
    private boolean enabled;

    public Link(Router router1, Router router2){
        if(router1.getRid() < router2.getRid()){
            this.r1 = router1;
            this.r2 = router2;
        }
        else{
            this.r1 = router2;
            this.r2 = router1;
        }
        enable();
    }

    public Integer getMyNeighRid(Router me){
        if(me.getRid() != r1.getRid() && me.getRid() != r2.getRid())
            return null;
        return me.getRid() == r1.getRid() ? r2.getRid() : r1.getRid();
    }

    public boolean forward(Packet packet, Integer neighRid){
        Router neigh = getRouterWithRid(neighRid);
        if(!enabled || neigh == null) return false;
        neigh.addPacketRequest(packet);
        System.out.println(String.format("%s from %s to %s", packet, getMyNeighRid(neigh), neighRid));
        return true;
    }

    private Router getRouterWithRid(Integer rid){
        if(rid != r1.getRid() && rid != r2.getRid())
            return null;
        return rid == r1.getRid() ? r1 : r2;
    }

    void enable(){
        enabled = true;
    }

    void disable(){
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Link && super.equals(o);
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", r1, r2);
    }

}
