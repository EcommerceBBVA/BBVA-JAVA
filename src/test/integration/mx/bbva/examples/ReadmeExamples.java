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
package mx.bbva.examples;

import mx.bbva.client.core.BbvaAPI;
import mx.bbva.client.core.requests.parameters.ParameterContainer;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

/**
 * A syntax test for the examples in the README.md file. It's not meant to run, just to check that the examples are
 * correct.
 *
 * @author elopez
 */
@Ignore("Not actually meant to run everytime, just to check the readme examples are ok")
@SuppressWarnings("unused")
public class ReadmeExamples {

    @Test
    public void testReadmeExamples() throws Exception {
        String merchantId = "mptdggroasfcmqs8plpy";
        String apiKey = "sk_326c6d0443f6457aae29ffbd48f7d1be";
        String orderId = String.valueOf(System.currentTimeMillis());
        String publicIp = "138.84.62.109";
        // #### Starting the API ####

        BbvaAPI api = new BbvaAPI("https://sand-api.ecommercebbva.com/", apiKey, merchantId, publicIp);

        ParameterContainer address = new ParameterContainer("address");
        address.addValue("line1", "Calle Morelos #12 - 11");
        address.addValue("line2", "Colonia Centro"); // Optional
        address.addValue("line3", "Cuauhtémoc"); // Optional
        address.addValue("city", "Distrito Federal");
        address.addValue("postal_code", "12345");
        address.addValue("state", "Queretaro");
        address.addValue("country_code", "MX"); // ISO 3166-1 two-letter code

        // #### Creating a customer ####
        ParameterContainer customer = new ParameterContainer("customer");
        customer.addValue("name","John");
        customer.addValue("last_name", "doe");
        customer.addValue("email", "johndoe@example.com");
        customer.addValue("phone_number", "554-170-3567");
        customer.addMultiValue(address);

        HashMap customerAsMap = api.customers().create(customer.getParameterValues());

        ParameterContainer charge = new ParameterContainer("charge");
        charge.addValue("affiliation_bbva", "781500");
        charge.addValue("amount", "100.00");
        charge.addValue("description", "Pago de taxi");
        charge.addValue("currency", "MXN");
        charge.addValue("order_id", orderId);
        charge.addValue("redirect_url", "https://sand-portal.ecommercebbva.com/");
        charge.addMultiValue(customer);

        HashMap chargeAsMap = api.charges().create(charge.getParameterValues());

    }
}
