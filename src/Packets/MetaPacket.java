package Packets;

import Routers.DEdge;
import Routers.DEdgeStatus;

public class MetaPacket extends Packet{
    private final DEdgeStatus status;
    private final DEdge edge;

    public MetaPacket(DEdge edge, boolean enabled){
        super();
        this.edge = edge;
        this.status = new DEdgeStatus(enabled, System.currentTimeMillis());
    }

    public MetaPacket(DEdge edge, DEdgeStatus status){
        this.edge = edge;
        this.status = status;
    }

    public DEdge getEdge() {
        return edge;
    }

    public DEdgeStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("mp_%d(%s)", getId(), edge);
    }
}
