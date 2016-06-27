package com.redmonkeysoftware.getaddressclient.model;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class Address implements Serializable {

    private static final long serialVersionUID = -2179275162528862185L;
    protected String line1;
    protected String line2;
    protected String line3;
    protected String line4;
    protected String locality;
    protected String town;
    protected String county;

    public Address() {
    }

    public Address(String input) {
        String[] inputs = StringUtils.split(input, ",");
        if (inputs != null) {
            int i = 0;
            for (String str : inputs) {
                str = StringUtils.trimToNull(str);
                switch (i) {
                    case 0:
                        line1 = str;
                        break;
                    case 1:
                        line2 = str;
                        break;
                    case 2:
                        line3 = str;
                        break;
                    case 3:
                        line4 = str;
                        break;
                    case 4:
                        locality = str;
                        break;
                    case 5:
                        town = str;
                        break;
                    case 6:
                        county = str;
                        break;
                }
                i++;
            }
        }
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
