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
package mx.bancomer.core.client;

import lombok.extern.slf4j.Slf4j;
import mx.bancomer.client.Charge;
import mx.bancomer.client.Token;
import mx.bancomer.client.core.BancomerAPI;
import mx.bancomer.client.core.requests.parameters.Parameter;
import mx.bancomer.client.core.requests.parameters.ParameterContainer;
import mx.bancomer.client.core.requests.parameters.SingleParameter;
import mx.bancomer.client.core.requests.transactions.ConfirmCaptureParams;
import mx.bancomer.client.core.requests.transactions.RefundParams;
import mx.bancomer.client.exceptions.ServiceException;
import mx.bancomer.client.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * Test creating all kinds of objects using an empty merchant account.
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Slf4j
@Ignore
public class FullApiTest {

    private BancomerAPI api;

    private Charge merchantCharge;
    private Token token;
    private ParameterContainer customer;

    @Before
    public void setUp() throws Exception {
        String merchantId = "miklpzr4nsvsucghm2qp";
        String apiKey = "sk_08453429e4c54220a3a82ab4d974c31a";
        String endpoint = "https://dev-api.openpay.mx/";
        this.api = new BancomerAPI(endpoint, apiKey, merchantId);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));

        ParameterContainer address = new ParameterContainer("address");
        address.addValue("line1", "Calle Morelos #12 - 11");
        address.addValue("line2", "Colonia Centro");           // Optional
        address.addValue("line3", "Cuauhtémoc");               // Optional
        address.addValue("city", "Distrito Federal");
        address.addValue("postal_code", "12345");
        address.addValue("state", "Queretaro");
        address.addValue("country_code", "MX");

        this.customer = new ParameterContainer("customer");
        this.customer.addValue("name", "John");
        this.customer.addValue("last_name", "Doe");
        this.customer.addValue("email", "johndoe@example.com");
        this.customer.addValue("phone_number", "554-170-3567");
        this.customer.addMultiValue(address);
    }

    @Test
    public void testFullApi() throws Exception {
        this.testCardCharges();
        this.testChargeGet();
        this.testTokens();
    }

    private void testCardCharges() throws ServiceException, ServiceUnavailableException {
        this.testChargeMerchantStore();
        this.testChargeMerchantCardCaptureRefund();
    }

    // POST /v1/{merchantId}/charges/{transactionId}/refund
    private void testChargeMerchantCardCaptureRefund() throws ServiceException, ServiceUnavailableException {
        this.merchantCharge = this.api.charges().createCharge(new ArrayList<Parameter>(Arrays.asList(
                        new SingleParameter("affiliation_bbva", "720931"),
                        new SingleParameter("amount", "200.00"),
                        new SingleParameter("description", "Test Charge"),
                        new SingleParameter("customer_language", ""),
                        new SingleParameter("capture", "false"),
                        new SingleParameter("use_3d_secure", "false"),
                        new SingleParameter("use_card_points", "NONE"),
                        new SingleParameter("token", this.token.getId()),
                        new SingleParameter("currency", "MXN"),
                        new SingleParameter("order_id", "oid-00051")
                )));
        log.info("Merchant card charge: {}", this.merchantCharge.getId());

        this.merchantCharge = this.api.charges().confirmCapture(
                new ConfirmCaptureParams().amount(new BigDecimal("200.00")).chargeId(this.merchantCharge.getId()));
        log.info("Merchant card charge confirmed: {}", this.merchantCharge.getId());

        this.merchantCharge = this.api.charges().refund(new RefundParams().chargeId(this.merchantCharge.getId()));
        log.info("Merchant card charge refunded: {}", this.merchantCharge.getRefund().getId());

    }

    // POST /v1/{merchantId}/charges
    private void testChargeMerchantStore() throws ServiceException, ServiceUnavailableException {
        this.merchantCharge = this.api.charges().createCharge(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "720931"),
                new SingleParameter("amount", "200.00"),
                new SingleParameter("description", "Test Charge"),
                new SingleParameter("customer_language", ""),
                new SingleParameter("capture", "true"),
                new SingleParameter("use_3d_secure", "false"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", this.token.getId()),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", "oid-00052")
                )));
        log.info("Merchant store Charge: {}", this.merchantCharge.getId());
    }

    private void testChargeGet() throws ServiceException, ServiceUnavailableException {
        this.testMerchantGetCharge();
    }

    // GET /v1/{merchantId}/charges/{transactionId}
    private void testMerchantGetCharge() throws ServiceException, ServiceUnavailableException {
        this.merchantCharge = this.api.charges().get(this.merchantCharge.getId());
        log.info("Merchant charge {} ", this.merchantCharge);
    }

    private void testTokens() throws ServiceException, ServiceUnavailableException {
        this.testCreateToken();
        this.testGetToken();
    }

    private void testCreateToken() throws ServiceException, ServiceUnavailableException {
        this.token = this.api.tokens().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("card_number", "4111111111111111"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "20"),
                new SingleParameter("holder_name", "Juan Perez Lopez")
        )));
        log.info("Token created {} ", this.token);
    }

    private void testGetToken() throws ServiceException, ServiceUnavailableException {
        this.testCreateToken();
        this.token = this.api.tokens().get(this.token.getId());
        log.info("Token {} ", this.token);
    }

}
