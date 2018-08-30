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
package mx.bancomer.core.client.full;

import mx.bancomer.client.core.BancomerAPI;
import mx.bancomer.client.exceptions.ServiceException;
import mx.bancomer.client.exceptions.ServiceUnavailableException;
import org.junit.Test;

import java.util.TimeZone;

import static mx.bancomer.core.client.TestConstans.*;
import static org.junit.Assert.*;

/**
 * @author elopez
 */
public class ConfigurationTest {

    @Test
    public void testNoAPIKey() throws Exception {
        BancomerAPI api = new BancomerAPI(ENDPOINT.replace("https", "http"), null, MERCHANT_ID);
        try {
            api.customers().list(null);
            fail();
        } catch (ServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @Test
    public void testForceHttps() throws Exception {
        BancomerAPI api = new BancomerAPI(ENDPOINT.replace("https", "http"), API_KEY, MERCHANT_ID);
        assertNotNull(api.customers().list(null));
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testNoConnection() throws Exception {
        BancomerAPI api = new BancomerAPI("http://localhost:9090", API_KEY, MERCHANT_ID);
        api.customers().list(null);
    }

    @Test
    public void testAddHttps() throws Exception {
        BancomerAPI api = new BancomerAPI(ENDPOINT.replace("https://", ""), API_KEY, MERCHANT_ID);
        assertNotNull(api.customers().list(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMerchant() throws Exception {
        new BancomerAPI(ENDPOINT.replace("https://", ""), API_KEY, null);
    }

    @Test
    public void testWrongMerchant() throws Exception {
        BancomerAPI api = new BancomerAPI(ENDPOINT, API_KEY, "notexists");
        try {
            api.customers().list(null);
            fail();
        } catch (ServiceException e) {
            assertEquals(401, e.getHttpCode().intValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLocation() throws Exception {
        new BancomerAPI(null, API_KEY, MERCHANT_ID);
    }

}
