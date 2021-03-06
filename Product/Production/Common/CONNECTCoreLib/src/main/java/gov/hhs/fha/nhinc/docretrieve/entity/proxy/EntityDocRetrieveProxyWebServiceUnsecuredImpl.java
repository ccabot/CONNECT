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
package gov.hhs.fha.nhinc.docretrieve.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author dunnek
 */
public class EntityDocRetrieveProxyWebServiceUnsecuredImpl implements EntityDocRetrieveProxy {
    private static Service cachedService = null;
    private static EntityDocRetrieve service = null;

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitydocretrieve";
    private static final String SERVICE_LOCAL_PART = "EntityDocRetrieve";
    private static final String PORT_LOCAL_PART = "EntityDocRetrievePortType";
    private static final String WSDL_FILE = "EntityDocRetrieve.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":RespondingGateway_CrossGatewayRetrieve";

    private static org.apache.commons.logging.Log log = null;

    public EntityDocRetrieveProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        service = getWebService();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected EntityDocRetrieve getWebService() {
        return new EntityDocRetrieve();
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        RetrieveDocumentSetResponseType response = null;

        WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();

        String serviceName = NhincConstants.ENTITY_DOC_RETRIEVE_SERVICE_NAME;
        String url = this.getUrl(serviceName);
        if (NullChecker.isNotNullish(url)) {
            EntityDocRetrievePortType port = getPort(url, assertion, serviceName);
            RespondingGatewayCrossGatewayRetrieveRequestType message = new RespondingGatewayCrossGatewayRetrieveRequestType();

            message.setAssertion(assertion);
            message.setNhinTargetCommunities(targets);
            message.setRetrieveDocumentSetRequest(body);

            try {
                log.debug("invoke port");
                response = (RetrieveDocumentSetResponseType) proxyHelper.invokePort(port,
                        EntityDocRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", message);
            } catch (Exception ex) {
                log.error("Failed to call the web service (" + serviceName + ").  An unexpected exception occurred.  "
                        + "Exception: " + ex.getMessage(), ex);
            }
        }
        return response;

    }

    protected String getUrl(String serviceName) {
        String result = "";
        try {
            result = this.getWebServiceProxyHelper().getUrlLocalHomeCommunity(serviceName);
        } catch (Exception ex) {
            log.warn("Unable to retreive url for service: " + serviceName);
            log.warn("Error: " + ex.getMessage(), ex);
        }

        return result;
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected EntityDocRetrievePortType getPort(String url, AssertionType assertion, String serviceName) {
        WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();

        EntityDocRetrievePortType port = null;
        Service cacheService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null) {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityDocRetrievePortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, WS_ADDRESSING_ACTION,
                    assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    protected Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
                cachedService = proxyHelper.createService(wsdl, uri, service);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
