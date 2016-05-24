/**
 * Product  : Hiperium Project
 * Architect: Andres Solorzano.
 * Created  : 08-05-2009 - 23:30:00
 * 
 * The contents of this file are copyrighted by Andres Solorzano 
 * and it is protected by the license: "GPL V3." You can find a copy of this 
 * license at: http://www.hiperium.com/about/licence.html
 * 
 * Copyright 2014 Andres Solorzano. All rights reserved.
 * 
 */
package com.hiperium.home.websocket;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * @author Andres Solorzano
 *
 */
@ClientEndpoint
public class WebSocketClient {

	/** The property webSocketSession. */
	private Session webSocketSession;
	
	/** The property messageHandler. */
	private MessageHandler messageHandler;

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param endpointURI
	 * @throws DeploymentException
	 * @throws IOException
	 */
	public WebSocketClient(URI endpointURI) throws DeploymentException, IOException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(this, endpointURI);
	}

	/**
	 * Callback hook for Connection open events.
	 * 
	 * @param webSocketSession
	 *            the WebSocket Session which is opened.
	 */
	@OnOpen
	public void onOpen(Session webSocketSession) {
		this.webSocketSession = webSocketSession;
	}

	/**
	 * Callback hook for Connection close events.
	 * 
	 * @param userSession
	 *            the userSession which is getting closed.
	 * @param reason
	 *            the reason for connection close
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.webSocketSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a
	 * client send a message.
	 * 
	 * @param message
	 *            The text message
	 */
	@OnMessage
	public void onMessage(String message) {
		if (this.messageHandler != null)
			this.messageHandler.handleMessage(message);
	}

	/**
	 * register message handler
	 * 
	 * @param message
	 */
	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	/**
	 * Send a message.
	 * 
	 * @param user
	 * @param message
	 */
	public void sendMessage(String message) {
		this.webSocketSession.getAsyncRemote().sendText(message);
	}

	/**
	 * Message handler.
	 */
	public static interface MessageHandler {
		public void handleMessage(String message);
	}

}
