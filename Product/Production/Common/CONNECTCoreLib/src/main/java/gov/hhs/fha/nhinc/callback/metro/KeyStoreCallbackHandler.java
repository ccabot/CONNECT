/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.callback.metro;

import com.sun.xml.wss.impl.callback.KeyStoreCallback;
import com.sun.xml.wss.impl.callback.PrivateKeyCallback;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class uses the keystore system properties as established in the domain.xml file to allow the configuration of
 * the SAML Keystore policy statements.
 */
public class KeyStoreCallbackHandler implements CallbackHandler {

    private KeyStore keyStore = null;
    private String password;
    private static Log log = LogFactory.getLog(KeyStoreCallbackHandler.class);

    /**
     * Creates the callback handler saving the keystore certificates information from the keystore file specified by the
     * system properties: javax.net.ssl.keyStore and javax.net.ssl.keyStorePassword.
     */
    public KeyStoreCallbackHandler() {
        log.debug("Entry KeyStoreCallbackHandler Constructor");

        InputStream is = null;
        String storeType = System.getProperty("javax.net.ssl.keyStoreType");
        password = System.getProperty("javax.net.ssl.keyStorePassword");
        String storeLoc = System.getProperty("javax.net.ssl.keyStore");

        if (storeType == null) {
            log.error("javax.net.ssl.keyStoreType is not defined in domain.xml");
            log.warn("Default to JKS keyStoreType");
            storeType = "JKS";
        }
        if (password != null) {
            if ("JKS".equals(storeType) && storeLoc == null) {
                log.error("javax.net.ssl.keyStore is not defined in domain.xml");
            } else {
                try {
                    keyStore = KeyStore.getInstance(storeType);
                    if ("JKS".equals(storeType)) {
                        is = new FileInputStream(storeLoc);
                    }
                    keyStore.load(is, password.toCharArray());
                } catch (IOException ex) {
                    log.debug("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    log.debug("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (CertificateException ex) {
                    log.debug("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (KeyStoreException ex) {
                    log.debug("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException ex) {
                        log.debug("KeyStoreCallbackHandler " + ex);
                    }
                }
            }
        } else {
            log.error("javax.net.ssl.keyStorePassword is not defined in domain.xml");
        }
        log.debug("Exit KeyStoreCallbackHandler Constructor");
    }

    /**
     * Implementing the callback, this method provides the keystore information or the private key information depending
     * on the type of callback desired to the input Callback object.
     * 
     * @param callbacks The Callback which needs to have keystore information set, should be either a KeyStoreCallback
     *            or a PrivateKeyCallback
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug("Entry KeyStoreCallbackHandler handle callback");
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof KeyStoreCallback) {
                KeyStoreCallback cb = (KeyStoreCallback) callbacks[i];
                // print(cb.getRuntimeProperties());
                cb.setKeystore(keyStore);
                log.debug("KeyStoreCallback set keystore");
            } else if (callbacks[i] instanceof PrivateKeyCallback) {
                try {
                    PrivateKeyCallback cb = (PrivateKeyCallback) callbacks[i];
                    // print(cb.getRuntimeProperties());
                    Key privkey = keyStore.getKey(cb.getAlias(), password.toCharArray());
                    cb.setKey((PrivateKey) privkey);
                    log.debug("PrivateKeyCallback set private key");
                } catch (KeyStoreException ex) {
                    log.error("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    log.error("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (UnrecoverableKeyException ex) {
                    log.error("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                }
            } else {
                log.error("Unsupported KeyStoreCallbackHandler Callback: " + callbacks[i]);
                throw new UnsupportedCallbackException(callbacks[i]);
            }
        }
        log.debug("Exit KeyStoreCallbackHandler handle callback");
    }

    /*
     * private void print(Map context) { Iterator it = context.keySet().iterator(); while (it.hasNext()) {
     * log.debug("Prop " + it.next()); } }
     */
}
