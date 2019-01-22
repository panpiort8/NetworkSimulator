package Networks;

import Godernet.Link;
import Routers.Router;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class GeoNetwork extends Network{
    protected final double radius;
    protected static final int planeSize = 1000;
    protected Map<Integer, Point> positions = new HashMap<>();
    protected Random random = new Random();

    public static class Density{
        public static Double HIGH(Integer n){ return n/4D; }
        public static Double MEDIUM(Integer n){ return Math.sqrt(n); }
        public static Double LOW(Integer n){ return 7D; }
    }

    /*I've tried to make it more accurate, but formula exploded*/
    static private double getRadius(Double avgNeighs, Integer routersNo){
        return Math.sqrt((avgNeighs*planeSize*planeSize)/(Math.PI*(routersNo-1)));
    }

    public GeoNetwork(int n){
        this(n, Density::MEDIUM);
    }

    /*densityFunction - function describing <almost> average deg(router)*/
    public GeoNetwork(int n, Function<Integer, Double> densityFunction){
        super(n);
        radius = getRadius(densityFunction.apply(n), n);

        for (Integer rid : routers.keySet())
            positions.put(rid, new Point(random.nextInt(planeSize), random.nextInt(planeSize)));

        for (Integer rid1 : routers.keySet())
            for(Integer rid2 : routers.keySet()){
                if(!rid1.equals(rid2) && positions.get(rid1).distance(positions.get(rid2)) <= radius)
                    links.add(new Link(routers.get(rid1), routers.get(rid2)));
            }
    }
}
