import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
import java.io.*;
import java.util.concurrent.LinkedBlockingDeque;

//As per IPv4 Protocol, a broadcast address is a logical address, on which devices connected to the network are enabled to receive packets. In our example, we use a particular IP address, 255.255.255.255, which is the broadcast address of the local network.


public class BroadcastServer extends Thread {
    // Define the UDP socket
    private ServerSocket TCPSocket = null;
    private int BROADCSAT_FREQUENCY = 30;
    private LinkedBlockingDeque<Integer> toConnectPortsQueue;
    private InetAddress ip;
    private String hostname;
    private int m_port;


    public BroadcastServer(LinkedBlockingDeque<Integer> in_toConnectPortsQueue, int port) throws IOException {
        m_port = port;
        TCPSocket = new ServerSocket(port);
        TCPSocket.setReuseAddress(true);
        // Some meta parameters to be defined for the serverSocket
        int SERVER_TIMEOUT = 100000000;
        // TCPSocket timeout for this server socket --> do not run forever
        TCPSocket.setSoTimeout(SERVER_TIMEOUT);
        this.toConnectPortsQueue = in_toConnectPortsQueue;
    }



    public void run()
    {
        try
        {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        // Initiate a UDP server to run the broadcast service
        // The udp broadcasting server will broadcast to all ports in the local host and send
        // the port information to the main program


        while (true)
        {
            // First check for any connection
            while (toConnectPortsQueue.size()>0)
            {

                try
                {
                    int curPort = toConnectPortsQueue.poll();
                    System.out.println("sending connection to port: " + curPort);
                    // try to connect to the port
                    Socket socket = new Socket(hostname,curPort);
                    // Initiate a thread to handle the socket communication
                    new ComHandlerThread(socket).start();

                } catch (IOException e) { e.printStackTrace(); }
            }
            //accept new incoming connection
            try {
                // accept new incoming connections
                Socket newConn = TCPSocket.accept();
                // initiate a new thread to handle requests from the new connection
                new ComHandlerThread(newConn).start();
                System.out.println("accepted connection from port: "+newConn.getPort());
            }
            catch (IOException e) {e.printStackTrace();}

        }
    }
}




