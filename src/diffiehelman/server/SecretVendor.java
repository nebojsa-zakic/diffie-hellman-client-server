package diffiehelman.server;

import diffiehelman.server.domain.ClientRegistration;
import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author nebojsa
 */
public class SecretVendor {
    
    /**
     * Determines duration of the client secret in cache.
     * Time is in seconds.
     */
    private static int secretDuration;

    private static SecretVendor instance;

    private ConcurrentHashMap<String, ClientRegistration> clientSecrets = new ConcurrentHashMap<>();

    private SecretVendor() {}
    
    public static void secretDuration(int duration) {
        secretDuration = duration;
    }

    public String addClient(BigInteger secret) {
        String uniqueId = UUID.randomUUID().toString();
        
        Instant expirationDate = Instant.now().plusSeconds(secretDuration);
        
        ClientRegistration registration = new ClientRegistration(secret, expirationDate);

        clientSecrets.put(uniqueId, registration);

        System.out.println("Registered new client: " + uniqueId + " with secret " + secret);

        return uniqueId;
    }

    public boolean removeClient(String clientId) {
        if (clientSecrets.containsKey(clientId)) {
            clientSecrets.remove(clientId);
            System.out.println("Removed client with id " + clientId);

            return true;
        }
        
        System.err.print("Error client with " + clientId + " not found.");

        return false;
    }
    
    public Map<String, Instant> getRegistrationsExpiryDates() {
         Map<String, Instant> resultMap = new HashMap<>();
         clientSecrets.entrySet().stream().forEach((entry) -> {
             resultMap.put(entry.getKey(), entry.getValue().getExpirationDate());
        });
         
         return resultMap;
    }

    public static SecretVendor get() {
        if (instance == null) {
            System.out.println("Initializing secret vendor.");
            instance = new SecretVendor();
        }
        return instance;
    }
}
