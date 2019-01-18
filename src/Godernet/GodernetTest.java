package Godernet;

import Networks.CompleteNetwork;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodernetTest {
    @Test
    public void CompleteGraphBuildTest(){
        Godernet godernet = new Godernet(new CompleteNetwork(10));
        assertEquals(45, godernet.getEdges().size());
    }

}