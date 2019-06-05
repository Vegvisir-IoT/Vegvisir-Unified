import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;



// This server is solely in charge of broadcasting the ip address of the tcp port

public class UDPBroadcastServer extends Thread {
    private byte[] buf = new byte[256];
    private DatagramSocket socket;
    private boolean running;
    private int port;
    private int parentPort;

    public UDPBroadcastServer(int in_parentPort) throws SocketException
    {
        // initiate the UDP server and the buffer communciation with the host program
        port = 3333;
        socket = new DatagramSocket(port);
        socket.setReuseAddress(true);
        socket.setSoTimeout(60000000);
        socket.setBroadcast(true);
        this.parentPort = in_parentPort;
    }

    public void broadcast() throws IOException
    {
        //System.out.println("broadcast to all servers in local network");
        // broadcast to all receiving sockets in the local network
        InetAddress address = InetAddress.getByName("255.255.255.255");
        String broadcastMessage = "VEGVISIR-" + parentPort +"\n";
        broadcastMessage = broadcastMessage.length() + "-" + broadcastMessage;
        System.out.println(broadcastMessage);
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }


    public void run()
    {
        running = true;
        while(running)
        {
            try
            {
                this.broadcast();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            // sleep for ten seconds
            try
            {
                TimeUnit.SECONDS.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}