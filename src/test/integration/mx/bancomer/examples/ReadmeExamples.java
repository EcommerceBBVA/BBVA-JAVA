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
package mx.bancomer.examples;

import mx.bancomer.client.Address;
import mx.bancomer.client.Customer;
import mx.bancomer.client.core.BancomerAPI;
import org.junit.Ignore;
import org.junit.Test;

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
        String merchantId = "mtfsdeoulmcoj0xofpfc";
        String apiKey = "sk_4ec3ef18cd01471487ca719f566d4d3f";

        // #### Starting the API ####

        BancomerAPI api = new BancomerAPI("https://dev-api.openpay.mx/", apiKey, merchantId);

        Address address = new Address()
                .line1("Calle Morelos #12 - 11")
                .line2("Colonia Centro") // Optional
                .line3("Cuauhtémoc") // Optional
                .city("Distrito Federal")
                .postalCode("12345")
                .state("Queretaro")
                .countryCode("MX"); // ISO 3166-1 two-letter code

        // #### Creating a customer ####

        Customer customer = api.customers().create(new Customer()
                .name("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .phoneNumber("554-170-3567")
                .address(address));

        // #### Charging ####

    }
}
