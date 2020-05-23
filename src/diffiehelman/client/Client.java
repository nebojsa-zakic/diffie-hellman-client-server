/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diffiehelman.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

/**
 *
 * @author user777
 */
public class Client {

    private Socket socket;
    private String address;
    private int port;

    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    private int a;

    private long s;

    public void connect(String address, int port, BigInteger p, long g) throws IOException, ClassNotFoundException {
        this.address = address;
        this.port = port;

        a = (int) Math.round(Math.random() * 999) + 1;

        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Random number a: " + a);

        BigInteger A = new BigInteger(g + "");
        A = A.pow(a);
        A = A.mod(p);

        System.out.println("Sending A: " + A);
        out.writeObject(A);

        BigInteger B = (BigInteger) in.readObject();

        System.out.println("Recieved B: " + B);

        B = B.pow(a);
        B = B.mod(p);
        BigInteger s = B;

        System.out.println("Secret s: " + s);

        String connectionId = (String)in.readObject();

        System.out.println("Connection ID: " + connectionId);

        socket.close();
        in.close();
        out.close();

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client();

        BigInteger p = new BigInteger("149266604066765214257465899845052595936980433085281120472438633560109109845062080813195674897136525949840184965312505298869948722977649469023084361550412989486060207917580540454081140587353862234445577520476872543676486167892443872308705026778461121261224322495328346630383486386663628878772838449087770123303");
        int g = 47;

        client.connect("localhost", 9000, p, g);
    }

}
