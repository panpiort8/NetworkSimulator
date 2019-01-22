package Networks;

import Edges.Link;

import java.util.List;

public interface DynamicNetwork {
    public void changeTopology();
    public List<Link> pollDeletedLinks();
    public List<Link> pollAddedLinks();
}
