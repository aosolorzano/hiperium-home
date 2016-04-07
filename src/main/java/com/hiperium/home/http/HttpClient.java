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
package com.hiperium.home.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import com.hiperium.home.exception.InformationException;
import com.hiperium.home.logger.HiperiumLogger;


/**
 * This is a utility class for using with the Apache HTTP libraries to sent
 * requests to the REST Service.
 * 
 * @author Andres Solorzano.
 */
public class HttpClient {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(HttpClient.class);
	
	/** The property httpclient. */
	private CloseableHttpClient httpclient;
	/** The property localContext. */
	private final HttpContext localContext;
	
	/** The property response. */
	private CloseableHttpResponse response;
	
	/** The property methodGet. */
	private HttpGet methodGet;
	/** The property methodPost. */
	private HttpPost methodPost;
	/** The property methodPut. */
	private HttpPut methodPut;
	/** The property methodDelete. */
	private HttpDelete methodDelete;

	/**
	 * Class constructor.
	 */
	protected HttpClient() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        this.httpclient = HttpClients.custom().setConnectionManager(cm).build();
        this.localContext = new BasicHttpContext();
	}

	/**
	 * 
	 * @param url
	 * @param acceptedType
	 * @param token
	 * @return
	 * @throws InformationException
	 */
	public String getFromService(String url, String acceptedType, String token) throws InformationException {
		LOGGER.debug("getFromService - START: " + url);
		this.methodGet = new HttpGet(url);
		this.methodGet.setHeader("Accept", StringUtils.isBlank(acceptedType)? "application/json" : acceptedType);
		this.methodGet.setHeader("Authorization", token);
		
		String result = null;
		try {
			this.response = this.httpclient.execute(this.methodGet, this.localContext);
			result = this.populateResponse(this.response);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new InformationException(e.getMessage());
		} finally {
			if(this.response != null) {
				try {
					this.response.close();
				} catch (IOException e) {
					LOGGER.error("CAN NOT CLOSE HTTP CLIENT RESPONSE.");
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param url
	 * @param content
	 * @param contentType
	 * @param acceptedType
	 * @param token
	 * @return
	 * @throws InformationException
	 */
	public String postToService(String url, String content, String contentType, String acceptedType, String token) throws InformationException {
		LOGGER.debug("postToService - START: " + url);
		if(StringUtils.isBlank(content)) {
			throw new InformationException("HTTP content is required for this operation.");
		} else {
			this.methodPost = new HttpPost(url);
			this.methodPost.setHeader(HTTP.CONTENT_TYPE, StringUtils.isBlank(contentType)? "application/json" : contentType);
			this.methodPost.setHeader("Accept", StringUtils.isBlank(acceptedType)? "application/json" : acceptedType);
			this.methodPost.setHeader("Authorization", token);
			
			String result = null;
			try {
				StringEntity se = new StringEntity(content);
				se.setContentType(StringUtils.isBlank(contentType)? "application/json" : contentType);
				this.methodPost.setEntity(se);
				this.response  = this.httpclient.execute(this.methodPost, this.localContext);
				result = this.populateResponse(this.response);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				throw new InformationException(e.getMessage());
			} finally {
				if(this.response != null) {
					try {
						this.response.close();
					} catch (IOException e) {
						LOGGER.error("CAN NOT CLOSE HTTP CLIENT RESPONSE.");
					}
				}
			}
			return result;
		}
	}

	/**
	 * 
	 * @param url
	 * @param content
	 * @param contentType
	 * @param acceptedType
	 * @param token
	 * @return
	 * @throws InformationException
	 */
	public String putToService(String url, String content, String contentType, String acceptedType, String token) throws InformationException {
		LOGGER.debug("putToService - START: " + url);
		if(StringUtils.isBlank(content)) {
			throw new InformationException("HTTP content is required for this operation.");
		} else {
			this.methodPut = new HttpPut(url);
			this.methodPut.setHeader(HTTP.CONTENT_TYPE, StringUtils.isBlank(contentType)? "application/json" : contentType);
			this.methodPut.setHeader("Accept", StringUtils.isBlank(acceptedType)? "application/json" : acceptedType);
			this.methodPut.setHeader("Authorization", token);
			
			String result = null;
			try {
				StringEntity se = new StringEntity(content);
				se.setContentType(StringUtils.isBlank(contentType)? "application/json" : contentType);
				this.methodPut.setEntity(se);
				this.response = this.httpclient.execute(this.methodPut, this.localContext);
				result = this.populateResponse(this.response);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				throw new InformationException(e.getMessage());
			} finally {
				if(this.response != null) {
					try {
						this.response.close();
					} catch (IOException e) {
						LOGGER.error("CAN NOT CLOSE HTTP CLIENT RESPONSE.");
					}
				}
			}
			return result;
		}
	}

	/**
	 * 
	 * @param url
	 * @param token
	 * @param primaryKeyValues
	 * @throws InformationException
	 */
	public void deleteResource(String url, String token, String... primaryKeyValues) throws InformationException {
		LOGGER.debug("deleteResource - START: " + url);
		StringBuilder completeURL = new StringBuilder(url);
		for (String primaryKeyValue : primaryKeyValues) {
			completeURL.append("/").append(primaryKeyValue);
		}
		this.methodDelete = new HttpDelete(completeURL.toString());
		this.methodDelete.setHeader("Authorization", token);
		try {
			this.response = this.httpclient.execute(this.methodDelete, this.localContext);
			this.populateResponse(this.response);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new InformationException(e.getMessage());
		} finally {
			if(this.response != null) {
				try {
					this.response.close();
				} catch (IOException e) {
					LOGGER.error("CAN NOT CLOSE HTTP CLIENT RESPONSE.");
				}
			}
		}
	}

	/**
	 * 
	 * @param response
	 * @param result
	 * @throws InformationException 
	 */
	private String populateResponse(CloseableHttpResponse response) throws InformationException {
		switch (response.getStatusLine().getStatusCode()) {
			case 200: // OK
				HttpEntity entity1 = response.getEntity();
				if (entity1 != null) {
					try {
						return convertStreamToString(entity1.getContent());
					} catch (UnsupportedOperationException | IOException e1) {
						LOGGER.error(e1.getMessage(), e1);
						throw new InformationException(e1.getMessage());
					}
				}
				break;
			case 201: // CREATED
				HttpEntity entity2 = response.getEntity();
				if (entity2 != null) {
					try {
						return convertStreamToString(entity2.getContent());
					} catch (UnsupportedOperationException | IOException e2) {
						LOGGER.error(e2.getMessage(), e2);
						throw new InformationException(e2.getMessage());
					}
				}
				break;
			case 401: // UNAUTHORIZED
				throw new InformationException("Unauthorized.");
			case 404: // NOT FOUND
				throw new InformationException("NotFound.");
			case 500: // INTERNAL SERVER ERROR
				throw new InformationException("Internal Server Error.");
			default: // GENERATE THE ERROR MESSAGE AND CLOSE HTTP RESPONSE
				InformationException exception = this.generateInfoException(response);
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.error("CAN NOT CLOSE HTTP CLIENT RESPONSE.");
				}
				throw exception;
		}
		// Response code can be verified
		return null;
	}
	
	/**
	 * Convert from InputStream content to String content.
	 * 
	 * @param content
	 *            the content to convert
	 * @return the converted object
	 * @throws IOException
	 */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	private InformationException generateInfoException(CloseableHttpResponse response) {
		String serverResponse = null;
		try {
			serverResponse = convertStreamToString(response.getEntity().getContent());
		} catch (UnsupportedOperationException e) {
			LOGGER.error(e.getMessage(), e);
			serverResponse = "UnsupportedOperationException in Http Client class generating error conversion.";
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			serverResponse = "IOException in Http Client class generating error conversion.";
		}
		return new InformationException(serverResponse);
	}
}
