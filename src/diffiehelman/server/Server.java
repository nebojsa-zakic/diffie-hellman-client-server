/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diffiehelman.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;

/**
 *
 * @author nebojsa
 */
public class Server {
    
    private int port = 9000;
    
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    private BigInteger p; 
    private long g;
    
    private int secretDuration;
    
    public Server(int port, BigInteger p, long g, int secretDuration) {
        this.port = port;
        this.p = p;
        this.g = g;
        this.secretDuration = secretDuration;
    }
    
    public Server() {}
    
    public void start() throws IOException {
        SecretVendor.secretDuration(secretDuration);
        initExpirationHandler();
        
        serverSocket = new ServerSocket(port);
           
        System.out.println("Server is listening on port " + port + "...");
        
        while(true) {
            ServerConnection conn = new ServerConnection(serverSocket.accept(), p, g);
            conn.start();
        }
        
    }
    
    private void initExpirationHandler() {
        SecretExpirationHandler handler = new SecretExpirationHandler();
        handler.setDaemon(true);
        handler.start();
    }
    
    public static void main(String[] args) throws IOException {
        int threeHoursInSeconds = 3 * 60 * 60;
        
        // Used for testing since 3h is too long
        BigInteger p = new BigInteger("149266604066765214257465899845052595936980433085281120472438633560109109845062080813195674897136525949840184965312505298869948722977649469023084361550412989486060207917580540454081140587353862234445577520476872543676486167892443872308705026778461121261224322495328346630383486386663628878772838449087770123303");

        Server server = new Server(9000, p, 47, threeHoursInSeconds);
        server.start();
    }
    
}
