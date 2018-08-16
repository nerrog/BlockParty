package de.leonkoth.blockparty.web.server;

import de.leonkoth.blockparty.BlockParty;
import org.bukkit.Bukkit;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Leon on 16.03.2018.
 * Project Blockparty2
 * © 2016 - Leon Koth
 */
public class WebSocketServer extends org.java_websocket.server.WebSocketServer implements WebServer {

    private Set<WebSocket> socket;
    private BlockParty blockParty;

    public WebSocketServer(InetSocketAddress address, BlockParty blockParty) {
        super(address);
        socket = new HashSet<>();
        this.blockParty = blockParty;
    }

    @Override
    public void onOpen(WebSocket socket, ClientHandshake handshake) {
        this.socket.add(socket);
        System.out.println("New connection from " + socket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket socket, int code, String reason, boolean remote) {
        this.socket.remove(socket);
        System.out.println("Closed connection to " + socket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket socket, String message) {
        System.out.println("Message from client: " + message);
        for (WebSocket sock : this.socket) {
            sock.send(message);
        }
    }

    @Override
    public void onError(WebSocket socket, Exception e) {
        //ex.printStackTrace();
        if (socket != null) {
            this.socket.remove(socket);
            // TODO: do some thing if required
        }
        if (socket != null) {
            System.out.println("ERROR from " + socket.getRemoteSocketAddress().getAddress().getHostAddress());
        }
    }

    @Override
    public void onStart() {
        Bukkit.getLogger().info("[BlockParty] Started music server on " + this.getAddress().getHostString() + ":" + this.getAddress().getPort());
        this.blockParty.logStartMessage(true);
    }

    public void send(String ip, String arena, String song, String play) {
        for (WebSocket ws : this.getConnections()) {
            //ws.send(ip + ";" + arena + ";" + song + ";" + play); TODO
            /*System.out.println(ip + "1 " + ws.getRemoteSocketAddress().getAddress().getHostAddress());
            System.out.println(ip + "2 " + ws.getRemoteSocketAddress().getHostString());
            System.out.println(ip + "3 " + ws.getRemoteSocketAddress().getHostName());
            System.out.println(ip + "4 " + ws.getRemoteSocketAddress().getAddress().getHostName());
            System.out.println(ip + "5 " + ws.getRemoteSocketAddress().getAddress().getCanonicalHostName());*/
            if (ws.getRemoteSocketAddress().getAddress().getHostAddress().equalsIgnoreCase(ip)) {
                if (ws.isOpen()) {
                    ws.send(arena + ";" + song + ";" + play);
                }
            }
        }
    }
}