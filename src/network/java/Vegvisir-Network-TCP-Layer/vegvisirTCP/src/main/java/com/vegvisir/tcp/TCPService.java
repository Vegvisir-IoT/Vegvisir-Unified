package com.vegvisir.tcp;

import com.vegvisir.network.datatype.proto.Payload;
import com.vegvisir.network.datatype.proto.UDPAdvertisingMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class TCPService {

    private ServerSocket server;

    private LinkedBlockingDeque<String> establishedConnections;

    private Map<String, TCPConnection> connections;
    private Map<String, TCPConnection> clientConnections;

    private Config config;

    ExecutorService pool;


    public TCPService(Config config) {
        this.config = config;
        this.connections = new ConcurrentHashMap<>();
        this.clientConnections = new ConcurrentHashMap<>();
        pool = Executors.newCachedThreadPool();
        establishedConnections = new LinkedBlockingDeque<>();
    }


    public void startTCPServer() {
        pool.submit(() -> {
            try {
                server = new ServerSocket(0);
                config.setTcpPort(server.getLocalPort());
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
            while (true) {
                try {
                    Socket socket = server.accept();
                    UDPAdvertisingMessage message = UDPAdvertisingMessage.parseDelimitedFrom(socket.getInputStream());
                    connections.put(message.getDeviceId(), new TCPConnection(message.getDeviceId(), socket));
                    establishedConnections.add(message.getDeviceId());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void startTCPClient(UDPAdvertisingMessage message) {
        pool.submit(() -> {
            try {
                Socket socket = new Socket(message.getIpAddress(), message.getTcpPort());
                clientConnections.put(message.getDeviceId(), new TCPConnection(message.getDeviceId(), socket));
                TCPConnection connection = clientConnections.get(message.getDeviceId());
                connection.write(message);
                establishedConnections.add(message.getDeviceId());
            } catch (IOException ex) {

            }
        });
    }

    /**
     * [BLOCKING]
     * @return
     * @throws InterruptedException
     */
    public String waitingConnections() throws InterruptedException {
        return establishedConnections.take();
    }

    public void send(String remoteID, Payload payload) throws IOException {
        if (connections.containsKey(remoteID)) {
            connections.get(remoteID).write(payload);
        }
    }
}
