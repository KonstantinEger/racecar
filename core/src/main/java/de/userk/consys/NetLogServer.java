package de.userk.consys;

import de.userk.log.NetOutStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetLogServer extends Thread {
    private final ServerSocket server;
    private final NetOutStream outStream;

    public NetLogServer(ServerSocket server, NetOutStream outStream) {
        this.server = server;
        this.outStream = outStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = server.accept();
                outStream.addSocket(socket);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public OutputStream getOutputStream() {
        return outStream;
    }
}
