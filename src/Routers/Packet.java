package Routers;

public class Packet {
    private static int next_id = 1;
    private final int id;
    Packet(){
        id = next_id;
        next_id++;
    }

    public int getId() {
        return id;
    }
}
