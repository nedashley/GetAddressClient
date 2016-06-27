package com.redmonkeysoftware.getaddressclient.test;

import com.redmonkeysoftware.getaddressclient.model.Address;
import com.redmonkeysoftware.getaddressclient.model.AddressLookupResult;
import com.redmonkeysoftware.getaddressclient.model.GetAddressApiException;
import com.redmonkeysoftware.getaddressclient.model.InvalidPostcodeException;
import com.redmonkeysoftware.getaddressclient.service.GetAddressService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GetAddressServiceTest {

    private GetAddressService service;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        service = GetAddressService.getInstance("q40diQBM-EietbVSSGOppw4678");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPostcodeLookup() {
        try {
            AddressLookupResult result = service.lookup("np44 1nj");
            Assert.assertEquals("NP44 1NJ has 22 addresses", 22, result.convertAddresses().size());
        } catch (InvalidPostcodeException | GetAddressApiException e) {
            fail();
        }
    }
    
    @Test
    public void testPostcodePropertyLookup() {
        try {
            AddressLookupResult result = service.lookup("np44 1nj", "6");
            Assert.assertEquals("6:NP44 1NJ has 1 address", 1, result.convertAddresses().size());
            Address address = result.convertAddresses().get(0);
            Assert.assertEquals("Line 1 matches", "6 Wayfield Crescent", address.getLine1());
            Assert.assertNull("Line 2 matches", address.getLine2());
            Assert.assertNull("Line 3 matches", address.getLine3());
            Assert.assertNull("Line 4 matches", address.getLine4());
            Assert.assertNull("Locality matches", address.getLocality());
            Assert.assertEquals("Town / City matches", "Cwmbran", address.getTown());
            Assert.assertEquals("County matches", "Gwent", address.getCounty());
        } catch (InvalidPostcodeException | GetAddressApiException e) {
            fail();
        }
    }

    @Test
    public void invalidPostcodeTest() {
        try {
            AddressLookupResult result = service.lookup("678nudji");
            fail();
        } catch (InvalidPostcodeException | GetAddressApiException e) {
            Assert.assertEquals("Invalid Postcode Exception", InvalidPostcodeException.class.getName(), e.getClass().getName());
        }
    }
}
