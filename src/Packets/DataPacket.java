package Packets;

public class DataPacket extends Packet {
    private static int next_id = 1;
    private final Integer id;
    private final Integer destination;

    public DataPacket(Integer dest){
        id = next_id;
        next_id++;
        this.destination = dest;
    }

    protected int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("p_%d", id);
    }

    public Integer getDestination() {
        return destination;
    }
}
