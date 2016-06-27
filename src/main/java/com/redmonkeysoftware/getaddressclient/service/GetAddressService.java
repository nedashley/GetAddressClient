package com.redmonkeysoftware.getaddressclient.service;

import com.redmonkeysoftware.getaddressclient.service.handler.AddressLookupResultJsonResponseHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.redmonkeysoftware.getaddressclient.model.AddressLookupResult;
import com.redmonkeysoftware.getaddressclient.model.GetAddressApiException;
import com.redmonkeysoftware.getaddressclient.model.InvalidPostcodeException;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class GetAddressService {

    private static GetAddressService instance;
    private final String apiKey;
    private final String hostname;
    private final int port;
    private final boolean ssl;
    private final String postcodePath;
    private final String postcodePropertyPath;
    private final ObjectMapper mapper;
    private final HttpClientContext context;

    private GetAddressService(final String apiKey, final String hostname, final int port, final boolean ssl, final String postcodePath, final String postcodePropertyPath) {
        this.apiKey = apiKey;
        this.hostname = hostname;
        this.port = port;
        this.ssl = ssl;
        this.postcodePath = postcodePath;
        this.postcodePropertyPath = postcodePropertyPath;
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        HttpHost targetHost = new HttpHost(hostname, port, ssl ? "https" : "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("api-key", apiKey));
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
        context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
        context.setRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(6000).setConnectTimeout(6000).setSocketTimeout(6000).build());
    }

    public static synchronized GetAddressService getInstance(final String apiKey) {
        if (instance == null) {
            instance = new GetAddressService(apiKey, "api.getAddress.io", 443, true, "/v2/uk/{postcode}", "/v2/uk/{postcode}/{property}");
        }
        return instance;
    }

    public AddressLookupResult lookup(String postcode) throws InvalidPostcodeException, GetAddressApiException {
        if (StringUtils.isNotBlank(postcode)) {
            postcode = StringUtils.trim(StringUtils.upperCase(postcode));
            if (postcode.matches("^\\s*(([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z])))) {0,1}[0-9][A-Za-z]{2})\\s*$)")) {
                postcode = postcode.replaceAll("[^A-Za-z0-9]", "");
                try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                    HttpGet request = new HttpGet((ssl ? "https://" : "http://") + hostname + StringUtils.replace(postcodePath, "{postcode}", postcode));
                    request.setHeader("Accept", "application/json");
                    AddressLookupResult result = client.execute(request, new AddressLookupResultJsonResponseHandler(mapper), context);
                    return result;
                } catch (IOException ioe) {
                    throw new GetAddressApiException("Error looking up postcode", ioe);
                }
            } else {
                throw new InvalidPostcodeException(postcode);
            }
        } else {
            throw new InvalidPostcodeException(postcode);
        }
    }

    public AddressLookupResult lookup(String postcode, String property) throws InvalidPostcodeException, GetAddressApiException {
        if (StringUtils.isNotBlank(postcode)) {
            postcode = StringUtils.trim(StringUtils.upperCase(postcode));
            if (postcode.matches("^\\s*(([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z])))) {0,1}[0-9][A-Za-z]{2})\\s*$)")) {
                if (StringUtils.isNotBlank(property)) {
                    postcode = postcode.replaceAll("[^A-Za-z0-9]", "");
                    property = StringUtils.trim(property);
                    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                        HttpGet request = new HttpGet((ssl ? "https://" : "http://") + hostname + StringUtils.replace(StringUtils.replace(postcodePropertyPath, "{postcode}", postcode), "{property}", property));
                        request.setHeader("Accept", "application/json");
                        AddressLookupResult result = client.execute(request, new AddressLookupResultJsonResponseHandler(mapper), context);
                        return result;
                    } catch (IOException ioe) {
                        throw new GetAddressApiException("Error looking up postcode", ioe);
                    }
                } else {
                    return lookup(postcode);
                }
            } else {
                throw new InvalidPostcodeException(postcode);
            }
        } else {
            throw new InvalidPostcodeException(postcode);
        }
    }
}
