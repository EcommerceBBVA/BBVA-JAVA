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

    private BancomerAPI api;

    private ParameterContainer merchantCharge;
    private ParameterContainer token;
    private String tokenId;
    private ParameterContainer customer;

    @Before
    public void setUp() throws Exception {
        String merchantId = "mtiwbo29vd4svc3lwm5a";
        String apiKey = "sk_650a09d01a484fbbb407c34b311b62de";
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
        this.testCharges();
        this.testChargeGet();
        this.testTokens();
    }

    private void testCharges() throws ServiceException, ServiceUnavailableException {
        this.testChargeMerchantStore();
        this.testChargeMerchantCardCaptureRefund();
        this.testChargeCustomerDirectCaptureRefund();
    }

    // POST
    // /v1/{merchantId}/customers/{customerId}/charges/{transactionId}/refund
    private void testChargeCustomerDirectCaptureRefund() throws ServiceException,
            ServiceUnavailableException {
        List<Parameter> request = new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "129354"),
                new SingleParameter("amount", "200.00"),
                new SingleParameter("description", "Test Charge"),
                new SingleParameter("customer_language", ""),
                new SingleParameter("capture", "FALSE"),
                new SingleParameter("use_3d_secure", "FALSE"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", this.tokenId),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", "oid-00091")
        ));
        Map chargeAsMap = this.api.charges().create(this.customer.getSingleValue("id").getParameterValue(), request);
        ParameterContainer charge = new ParameterContainer("charge", chargeAsMap);
        String chargeId = charge.getSingleValue("id").getParameterValue();
        log.info("Customer direct card charge: {}", chargeId);

        chargeAsMap = this.api.charges().confirmCapture(this.customer.getSingleValue("id").getParameterValue(), new ConfirmCaptureParams()
                .amount(new BigDecimal("200.00")).chargeId(chargeId));
        charge = new ParameterContainer("charge", chargeAsMap);
        chargeId = charge.getSingleValue("id").getParameterValue();
        log.info("Customer direct card charge confirmed: {}", chargeId);

        chargeAsMap = this.api.charges().refund(this.customer.getSingleValue("id").getParameterValue(),
                new RefundParams().chargeId(chargeId));
        charge = new ParameterContainer("charge", chargeAsMap);
        log.info("Customer card charge refunded: {}", charge.getContainerValue("refund").getSingleValue("id"));
    }

    // POST /v1/{merchantId}/charges/{transactionId}/refund
    private void testChargeMerchantCardCaptureRefund() throws ServiceException, ServiceUnavailableException {
        Map chargeAsMap = this.api.charges().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "129354"),
                new SingleParameter("amount", "200.00"),
                new SingleParameter("description", "Test Charge"),
                new SingleParameter("customer_language", ""),
                new SingleParameter("capture", "false"),
                new SingleParameter("use_3d_secure", "false"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", this.tokenId),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", "oid-00051")
        )));
        this.merchantCharge = new ParameterContainer("charge", chargeAsMap);
        String merchantChargeId = this.merchantCharge.getSingleValue("id").getParameterValue();
        log.info("Merchant card charge: {}", merchantChargeId);

        chargeAsMap = this.api.charges().confirmCapture(
                new ConfirmCaptureParams().amount(new BigDecimal("200.00")).chargeId(merchantChargeId));
        this.merchantCharge = new ParameterContainer("charge", chargeAsMap);
        merchantChargeId = this.merchantCharge.getSingleValue("id").getParameterValue();
        log.info("Merchant card charge confirmed: {}", merchantChargeId);

        chargeAsMap = this.api.charges().refund(new RefundParams().chargeId(merchantChargeId));
        this.merchantCharge = new ParameterContainer("charge", chargeAsMap);
        log.info("Merchant card charge refunded: {}", this.merchantCharge.getContainerValue("refund").getSingleValue("id"));

    }

    // POST /v1/{merchantId}/charges
    private void testChargeMerchantStore() throws ServiceException, ServiceUnavailableException {
        Map chargeAsMap = this.api.charges().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "129354"),
                new SingleParameter("amount", "200.00"),
                new SingleParameter("description", "Test Charge"),
                new SingleParameter("customer_language", ""),
                new SingleParameter("capture", "true"),
                new SingleParameter("use_3d_secure", "false"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", this.tokenId),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", "oid-00052")
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
                new SingleParameter("card_number", "4111111111111111"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "20"),
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
