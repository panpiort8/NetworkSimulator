package Networks;

import Godernet.Link;
import Routers.Router;

import java.util.Objects;

public class CompleteNetwork extends Network{
    CompleteNetwork(){}
    public CompleteNetwork(int n){
        super(n);
        for (int i = 0; i < n; i++)
            routers.put(i, new Router(i));

        for (Router r1 : routers.values())
            for(Router r2 : routers.values())
                if(!Objects.equals(r1, r2))
                    Links.add(new Link(r1, r2));

    }
}
