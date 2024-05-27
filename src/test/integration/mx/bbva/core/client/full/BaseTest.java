/*
 * Copyright 2014 Opencard Inc.
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
import org.junit.Before;

import java.util.TimeZone;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class BaseTest {

    protected BbvaAPI api;

    @Before
    public void setupAPI() throws Exception {
        String merchantId = "mptdggroasfcmqs8plpy";
        String apiKey = "sk_326c6d0443f6457aae29ffbd48f7d1be";
        String endpoint = "https://sand-api.ecommercebbva.com/";
        String publicIp = "138.84.62.109";
        this.api = new BbvaAPI(endpoint, apiKey, merchantId, publicIp);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

}
