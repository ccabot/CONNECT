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
package gov.hhs.fha.nhinc.hiem._20.entity.subscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * 
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityNotificationProducer", portName = "EntityNotificationProducerPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntityNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/EntitySubscribeService/EntitySubscriptionManagement.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "EntitySubscribeSoapHeaderHandler.xml")
public class EntitySubscribeService {

    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestType subscribeDocumentRequest) {
        return new EntitySubscribeServiceImpl().subscribeDocument(subscribeDocumentRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        return new EntitySubscribeServiceImpl().subscribeCdcBioPackage(subscribeCdcBioPackageRequest);
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest)
            throws UnrecognizedPolicyRequestFault, InvalidMessageContentExpressionFault, UnsupportedPolicyRequestFault,
            TopicNotSupportedFault, ResourceUnknownFault, NotifyMessageNotSupportedFault, InvalidTopicExpressionFault,
            InvalidFilterFault, SubscribeCreationFailedFault, InvalidProducerPropertiesExpressionFault,
            TopicExpressionDialectUnknownFault, UnacceptableInitialTerminationTimeFault {
        return new EntitySubscribeServiceImpl().subscribe(subscribeRequest, context);
    }

}
