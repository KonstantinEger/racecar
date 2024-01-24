package de.userk.log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetOutStream extends OutputStream {
    private final List<Socket> sockets;

    public NetOutStream(List<Socket> sockets) {
        this.sockets = sockets;
    }

    public NetOutStream() {
        this.sockets = new ArrayList<>();
    }

    @Override
    public void write(int b) {
        for (int i = 0; i < sockets.size(); i++) {
            Socket s = sockets.get(i);
            try {
                s.getOutputStream().write(b);
            } catch (IOException ex) {
                ex.printStackTrace();
                sockets.remove(i);
            }
        }
    }

    @Override
    public void write(byte[] b) {
        for (int i = 0; i < sockets.size(); i++) {
            Socket s = sockets.get(i);
            try {
                s.getOutputStream().write(b);
            } catch (IOException ex) {
                ex.printStackTrace();
                sockets.remove(i);
            }
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        for (int i = 0; i < sockets.size(); i++) {
            Socket s = sockets.get(i);
            try {
                s.getOutputStream().write(b, off, len);
            } catch (IOException ex) {
                ex.printStackTrace();
                sockets.remove(i);
            }
        }
    }
    public void addSocket(Socket s) {
        sockets.add(s);
    }
}
