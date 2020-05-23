/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diffiehelman.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nebojsa
 */
class ServerConnection extends Thread {

    private Socket socket;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private BigInteger p;
    private long g;

    private int b;

    public ServerConnection(Socket socket, BigInteger p, long g) {
        this.socket = socket;
        this.p = p;
        this.g = g;

        this.b = (int) Math.round(Math.random() * 999) + 1;
    }

    @Override
    public void run() {

        SecretVendor secretVendor = SecretVendor.get();

        System.out.println("New connection started");
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            BigInteger A = (BigInteger) in.readObject();

            System.out.println("Recieved A: " + A);

            A = A.pow(b);
            A = A.mod(p);
            BigInteger s = A;

            BigInteger B = new BigInteger(g + "");
            B = B.pow(b);
            B = B.mod(p);

            System.out.println("Sending B: " + B);

            out.writeObject(B);
            System.out.println("Secret s: " + s);

            String newClientId = secretVendor.addClient(s);
            out.writeObject(newClientId);

            in.close();
            out.close();
            socket.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Socket connection exception.");
        }
    }

}
