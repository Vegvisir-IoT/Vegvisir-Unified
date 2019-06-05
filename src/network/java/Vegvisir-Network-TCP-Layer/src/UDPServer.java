import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class UDPServer extends Thread {
    private byte[] buf = new byte[256];
    private DatagramSocket socket;
    private boolean running;
    private int port;
    private int parentPort;
    private LinkedBlockingDeque<Integer> toConnectPortAddressQueue;

    public UDPServer(LinkedBlockingDeque<Integer> in_toConnectPortAddress, int in_parentPort) throws SocketException
    {
        // initiate the UDP server and the buffer communciation with the host program
        port = 4444;
        socket = new DatagramSocket(port);
        socket.setReuseAddress(true);
        socket.setSoTimeout(60000000);
        socket.setBroadcast(true);
        this.toConnectPortAddressQueue = in_toConnectPortAddress;
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
        while (running)
        {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try
            {
                socket.receive(packet);
            }
            catch (IOException exc)
            {
                exc.printStackTrace();
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());

            if (received.indexOf("VEGVISIR") != -1)
            {
                int msgLength = Integer.valueOf(received.strip().split("-")[0]);
                String cleanMsg = received.substring(received.indexOf("-")+1,received.indexOf("-")+ msgLength);
                int toConnectPort =Integer.valueOf(cleanMsg.strip().split("-")[1]);

                if (toConnectPort != parentPort)
                {
                    System.out.println("received udp data "+ cleanMsg +"\n");
                    toConnectPortAddressQueue.add(toConnectPort);
                }
                else
                {
                    System.out.println("received my own message");
                }
            }
            else
            {
                System.out.println("Detected non vegvisir message");
            }

            // Sleep for 30 seconds --> Optimization: use OS level scheduling to avoid wasting CPU resources
            try
            {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        socket.close();
    }
}