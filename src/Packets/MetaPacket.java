package Packets;

import Edges.UEdge;
import Routers.UEdgeStatus;

import java.util.concurrent.atomic.AtomicInteger;

public class MetaPacket extends Packet {
    private final UEdgeStatus status;
    private final UEdge edge;
    private static AtomicInteger next_mid = new AtomicInteger(1);
    private final Integer mid;

    public MetaPacket(UEdge edge, boolean enabled){
        this(edge, new UEdgeStatus(enabled, System.currentTimeMillis()));
    }

    public MetaPacket(UEdge edge, UEdgeStatus status){
        this.mid = next_mid.getAndAdd(1);
        this.edge = edge;
        this.status = status;
    }

    public UEdge getEdge() {
        return edge;
    }

    public UEdgeStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("mp_%d(%s)", mid, edge);
    }
}
