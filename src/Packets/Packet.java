package Packets;

public class Packet {
    private static int next_id = 1;
    private final int id;
    private final Integer dest;

    Packet(){
        id = next_id;
        next_id++;
        dest = null;
    }

    Packet(Integer dest){
        id = next_id;
        next_id++;
        this.dest = dest;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("p_%d", id);
    }
}
