package com.redmonkeysoftware.getaddressclient.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redmonkeysoftware.getaddressclient.model.AddressLookupResult;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public class AddressLookupResultJsonResponseHandler implements ResponseHandler<AddressLookupResult> {

    private final ObjectMapper mapper;

    public AddressLookupResultJsonResponseHandler(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AddressLookupResult handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();
        if ((status >= 200) && (status < 300)) {
            AddressLookupResult result = mapper.readValue(response.getEntity().getContent(), AddressLookupResult.class);
            return result;
        } else {
            throw new ClientProtocolException("Address Lookup Request was not successful: " + status + ":" + response.getStatusLine().getReasonPhrase());
        }
    }
}
