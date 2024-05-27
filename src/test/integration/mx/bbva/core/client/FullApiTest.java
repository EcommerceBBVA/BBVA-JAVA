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
package mx.bbva.core.client;

import lombok.extern.slf4j.Slf4j;
import mx.bbva.client.core.BbvaAPI;
import mx.bbva.client.core.requests.parameters.Parameter;
import mx.bbva.client.core.requests.parameters.ParameterContainer;
import mx.bbva.client.core.requests.parameters.SingleParameter;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

/**
 * Test creating all kinds of objects using an empty merchant account.
 *
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Slf4j
@Ignore
@SuppressWarnings("unchecked")
public class FullApiTest {

    private BbvaAPI api;

    private ParameterContainer merchantCharge;
    private ParameterContainer token;
    private String tokenId;
    private ParameterContainer customer;

    @Before
    public void setUp() throws Exception {
        String merchantId = "mptdggroasfcmqs8plpy";
        String apiKey = "sk_326c6d0443f6457aae29ffbd48f7d1be";
        String endpoint = "https://sand-api.ecommercebbva.com/";
        String publicIp = "138.84.62.109";
        this.api = new BbvaAPI(endpoint, apiKey, merchantId, publicIp);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));


    }

    @Test
    public void testFullApi() throws Exception {
        this.testCustomers();
        this.testCharges();
        this.testChargeGet();
    }

    private void testCustomers() throws Exception {
        this.testCreateCustomers();
    }

    // POST /v1/{merchantId}/customers
    private void testCreateCustomers() throws ServiceUnavailableException, ServiceException {
        ParameterContainer address = new ParameterContainer("address");
        address.addValue("line1", "Calle Morelos #12 - 11");
        address.addValue("line2", "Colonia Centro");           // Optional
        address.addValue("line3", "Cuauhtémoc");               // Optional
        address.addValue("city", "Distrito Federal");
        address.addValue("postal_code", "12345");
        address.addValue("state", "Queretaro");
        address.addValue("country_code", "MX");

        List<Parameter> customerRequest = new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("name", "John"),
                new SingleParameter("last_name", "Doe"),
                new SingleParameter("email", "johndoe@example.com"),
                new SingleParameter("phone_number", "554-170-3567"),
                address
        ));

        Map customerAsMap = this.api.customers().create(customerRequest);
        this.customer = new ParameterContainer("customer", customerAsMap);
        this.customer.getSingleValue("external_id").setParameterValue(null);

        log.info("Customer: {}", this.customer.getSingleValue("id").getParameterValue());
    }

    private void testCharges() throws ServiceException, ServiceUnavailableException {
        this.testTokens();
        this.testChargeMerchantStore();
    }

    // POST /v1/{merchantId}/charges
    private void testChargeMerchantStore() throws ServiceException, ServiceUnavailableException {
        Map chargeAsMap = this.api.charges().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "781500"),
                new SingleParameter("amount", "200.00"),
                new SingleParameter("description", "Test Charge"),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", "oid-000" + new Random().nextInt(999)),
                new SingleParameter("redirect_url", "https://sand-portal.ecommercebbva.com/"),
                this.customer
        )));
        this.merchantCharge = new ParameterContainer("charge", chargeAsMap);
        log.info("Merchant store Charge: {}", this.merchantCharge);
    }

    private void testChargeGet() throws ServiceException, ServiceUnavailableException {
        this.testMerchantGetCharge();
    }

    // GET /v1/{merchantId}/charges/{transactionId}
    private void testMerchantGetCharge() throws ServiceException, ServiceUnavailableException {
        Map chargeAsMap = this.api.charges().get(this.merchantCharge.getSingleValue("id").getParameterValue());
        this.merchantCharge = new ParameterContainer("charge", chargeAsMap);
        log.info("Merchant charge {} ", this.merchantCharge);
    }

    private void testTokens() throws ServiceException, ServiceUnavailableException {
        this.testCreateToken();
        this.testGetToken();
    }

    private void testCreateToken() throws ServiceException, ServiceUnavailableException {
        Map tokenAsMap = this.api.tokens().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("card_number", "4242424242424242"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "29"),
                new SingleParameter("holder_name", "Juan Perez Lopez")
        )));
        this.token = new ParameterContainer("token", tokenAsMap);
        this.tokenId = this.token.getSingleValue("id").getParameterValue();
        log.info("Token created {} ", this.token);
    }

    private void testGetToken() throws ServiceException, ServiceUnavailableException {
        this.testCreateToken();
        Map tokenAsMap = this.api.tokens().get(this.tokenId);
        log.info("Token {} ", tokenAsMap);
    }

}
