/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diffiehelman.server.domain;

import java.math.BigInteger;
import java.time.Instant;

/**
 *
 * @author nebojsa
 */
public class ClientRegistration {
    private BigInteger secret;
    private Instant expirationDate;

    public ClientRegistration(BigInteger secret, Instant expirationDate) {
        this.secret = secret;
        this.expirationDate = expirationDate;
        System.out.println(expirationDate);
    }

    public BigInteger getSecret() {
        return secret;
    }
    
    public void setSecret(BigInteger secret) {
        this.secret = secret;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
    
}
