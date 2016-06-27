package com.redmonkeysoftware.getaddressclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddressLookupResult implements Serializable {

    private static final long serialVersionUID = -4508615829100220704L;
    @JsonProperty("Latitude")
    protected BigDecimal latitude;
    @JsonProperty("Longitude")
    protected BigDecimal longitude;
    @JsonProperty("Addresses")
    protected List<String> addresses = new ArrayList<>();

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public List<Address> convertAddresses() {
        List<Address> results = new ArrayList<>();
        for (String result : addresses) {
            results.add(new Address(result));
        }
        return results;
    }
}
