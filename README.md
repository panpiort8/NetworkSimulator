# NetworkSimulator

Network simulator with idependent routers working in parallel and traversing packets only via links. Topology of network is not arbitrary set, so routers are to interchange information about it online. I've used Link State Algorithm to achieve that. Scheduled rebroadcasting and limiting packets/metapackets flow allowed me to avoid information lacks and floods (in dynamic scenarios).
