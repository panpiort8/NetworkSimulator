package Godernet;

public class LinkRequest {
    private final long time;
    private final Link link;
    public LinkRequest(Link link){
        this.link = link;
        this.time = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }

    public Link getLink() {
        return link;
    }
}
