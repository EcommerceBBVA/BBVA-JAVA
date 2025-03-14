/*
 * Copyright 2013 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.bbva.core.client.full;

import mx.bbva.client.core.BbvaAPI;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.TimeZone;

import static mx.bbva.core.client.TestConstans.*;
import static org.junit.Assert.*;

/**
 * @author elopez
 */
public class ConfigurationTest {

    @Test
    public void testNoAPIKey() throws Exception {
        BbvaAPI api = new BbvaAPI(ENDPOINT.replace("https", "http"), null, MERCHANT_ID, PUBLIC_IP);
        try {
            api.customers().list(null);
            fail();
        } catch (ServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @Ignore
    @Test
    public void testForceHttps() throws Exception {
        BbvaAPI api = new BbvaAPI(ENDPOINT.replace("https", "http"), API_KEY, MERCHANT_ID, PUBLIC_IP);
        assertNotNull(api.customers().list(null));
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testNoConnection() throws Exception {
        BbvaAPI api = new BbvaAPI("http://localhost:9090", API_KEY, MERCHANT_ID, PUBLIC_IP);
        api.customers().list(null);
    }

    @Ignore
    @Test
    public void testAddHttps() throws Exception {
        BbvaAPI api = new BbvaAPI(ENDPOINT.replace("https://", ""), API_KEY, MERCHANT_ID, PUBLIC_IP);
        assertNotNull(api.customers().list(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMerchant() throws Exception {
        new BbvaAPI(ENDPOINT.replace("https://", ""), API_KEY, null, PUBLIC_IP);
    }

    @Test
    public void testWrongMerchant() throws Exception {
        BbvaAPI api = new BbvaAPI(ENDPOINT, API_KEY, "notexists", PUBLIC_IP);
        try {
            api.customers().list(null);
            fail();
        } catch (ServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLocation() throws Exception {
        new BbvaAPI(null, API_KEY, MERCHANT_ID, PUBLIC_IP);
    }

}
