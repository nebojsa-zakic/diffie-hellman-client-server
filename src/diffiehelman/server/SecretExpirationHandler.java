package diffiehelman.server;


import diffiehelman.server.SecretVendor;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nebojsa
 */
public class SecretExpirationHandler extends Thread {
    
    private static final long fiveSeconds = 5000L;

    @Override
    public void run() {

        SecretVendor secretVendor = SecretVendor.get();
        
        System.out.println("Starting expiration handler.");

        while (true) {
            // System.out.println(Instant.now() + " : removing expired registrations.");
            Map<String, Instant> regExpiryDates = secretVendor.getRegistrationsExpiryDates();

            List<String> expiredRegistrationsId = regExpiryDates.keySet()
                                                            .stream()
                                                            .filter(clientId -> regExpiryDates.get(clientId).isBefore(Instant.now()))
                                                            .collect(Collectors.toList());
            
            expiredRegistrationsId.forEach(clientId -> secretVendor.removeClient(clientId));

            try {
                this.sleep(fiveSeconds);
            } catch (InterruptedException ex) {
                Logger.getLogger(SecretExpirationHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
