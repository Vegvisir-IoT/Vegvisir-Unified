import java.util.concurrent.LinkedBlockingDeque;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("start riunning the main function ");
        // start a UDP broadcast server
        try
        {
            LinkedBlockingDeque<Integer> toConnectPortsQueue = new LinkedBlockingDeque<Integer>();
            int port = 914;
            BroadcastServer broad = new BroadcastServer(toConnectPortsQueue,port);
            UDPBroadcastServer udpbroad = new UDPBroadcastServer(port);
            UDPServer udp = new UDPServer(toConnectPortsQueue, port);
            broad.start();
            udpbroad.start();
            udp.start();
        }

        catch (Exception exc)
        {
            exc.printStackTrace();
            System.out.println("there is an error with starting the servers");
        }
    }
}
