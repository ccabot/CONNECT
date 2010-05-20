//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//## 
//## Copyright (c) 2010 Harris Corporation
//## 
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//## 
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//## 
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//## 
//####################################################################
//********************************************************************
// FILE: ClientPropertiesService.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: ClientPropertiesService.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
// Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package gov.hhs.fha.nhinc.lift.clientManager.client.properties.imp;

import gov.hhs.fha.nhinc.lift.clientManager.client.properties.interfaces.ClientPropertiesFacade;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientPropertiesService implements ClientPropertiesFacade {
    private static Log log = LogFactory.getLog(ClientPropertiesService.class);
    //private LiftManagement port;

    public ClientPropertiesService() {
        //LiftManagementService service = new LiftManagementService();
        //port = service.getLiftManagementPort();
    }

    @Override
    public String getFileDestinationForSubscription(String subId) {
        //LiftConsumingsubscriptions sub = port.getConsumingSubscription(subId);
        //return (sub == null ? null : sub.getStorageLocation());
        System.out.println("ClientPropertiesService.getFileDestinationForSubscription needs implementing");
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDefaultFileDestination() {
        String defaultFileLoc = null;
        try {
            defaultFileLoc = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_FILEDROP);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return defaultFileLoc;
    }
}