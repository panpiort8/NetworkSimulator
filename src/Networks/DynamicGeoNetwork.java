package Networks;

import Edges.Link;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DynamicGeoNetwork extends GeoNetwork implements DynamicNetwork{
    private List<Link> deletedLinks;
    private List<Link> addedLinks;
    private static final double MAX_TRANSLATION_MULT = 0.1;


    public DynamicGeoNetwork(int n, Function<Integer, Double> densityFunction){
        super(n, densityFunction);
    }

    public DynamicGeoNetwork(int n){
        super(n);
    }

    private int getRandomTrans(){
        int trans = (int)(MAX_TRANSLATION_MULT*radius);
        return random.nextInt(2*trans) - trans;
    }

    @Override
    public void changeTopology() {
        deletedLinks = new ArrayList<>();
        addedLinks = new ArrayList<>();

        for(Point point : positions.values())
            point.translate(getRandomTrans(), getRandomTrans());

        for (Integer rid1 : routers.keySet())
            for(Integer rid2 : routers.keySet()){
                if(rid1.equals(rid2))
                    continue;
                Link link = new Link(routers.get(rid1), routers.get(rid2));
                if(positions.get(rid1).distance(positions.get(rid2)) <= radius && !links.contains(link)){
                    links.add(link);
                    addedLinks.add(link);
                }
                else if(positions.get(rid1).distance(positions.get(rid2)) > radius && links.contains(link)){
                    links.remove(link);
                    deletedLinks.add(link);
                }
            }
    }

    @Override
    public List<Link> pollDeletedLinks() {
        List<Link> list = deletedLinks;
        deletedLinks = null;
        return list;
    }

    @Override
    public List<Link> pollAddedLinks() {
        List<Link> list = addedLinks;
        addedLinks = null;
        return list;
    }
}
