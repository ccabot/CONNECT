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
// FILE: SSLClient.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: SSLClient.java 
//              
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//> Feb 24, 2010 PTR#  - R. Robinson
// Initial Coding.
//<
//********************************************************************
package gov.hhs.fha.nhinc.lift.proxy.client;

import gov.hhs.fha.nhinc.lift.common.util.SecurityToken;
import gov.hhs.fha.nhinc.lift.proxy.util.ClientHandshaker;
import gov.hhs.fha.nhinc.lift.proxy.util.DemoProtocol;
import gov.hhs.fha.nhinc.lift.proxy.util.ProtocolWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


/**
 * @author rrobin20
 * 
 */
public class SSLClient extends Client {
	private SSLSocket socket;
	private ProtocolWrapper wrapper;

	public SSLClient(InetAddress address, int port, SecurityToken token, ClientHandshaker handshaker)
			throws IOException {
		super(address, port, token, handshaker);
	}

	@Override
	protected boolean connect(InetAddress address, int port) throws IOException {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
				.getDefault();
		socket = (SSLSocket) factory.createSocket();

		socket.connect(new InetSocketAddress(address, port));
		
		wrapper = new DemoProtocol(socket);

		return true;
	}

	@Override
	protected boolean performHandshake(ClientHandshaker handshaker) throws IOException {
		boolean success = handshaker.handshake(wrapper, this); 

		// Return if was a good response or not.
		if (success) {
			return true;
		} else {
			socket.close();
			throw new SSLHandshakeException(
					"Failed handshaking process failed handshake with server.");
		}
	}

	@Override
	public void sendLine(String mess) throws IOException {
		System.out.println("CLIENT: Sending message: " + mess);

		wrapper.sendLine(mess);

		System.out.println("CLIENT: Sent message.");
	}

	@Override
	public String readLine() throws IOException {
		return wrapper.readLine();
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	protected Socket getSocket() {
		return socket;
	}

	@Override
	public InputStream getInStream() throws IOException {
		return this.getSocket().getInputStream();
	}

	@Override
	public OutputStream getOutStream() throws IOException {
		return this.getSocket().getOutputStream();
	}

}