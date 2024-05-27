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

import lombok.extern.slf4j.Slf4j;
import mx.bbva.client.core.BbvaAPI;
import mx.bbva.client.core.requests.parameters.ParameterContainer;
import mx.bbva.client.core.requests.transactions.ConfirmChargeParams;
import mx.bbva.client.core.requests.transactions.RefundParams;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TimeZone;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@SuppressWarnings("unchecked")
@Slf4j
public class MerchantChargesTest extends BaseTest {

    private ParameterContainer customer;
    private ParameterContainer card;
    private ParameterContainer transaction;
    private String ORDER_ID = String.valueOf(System.currentTimeMillis());
    private final String DESCRIPTION = "Pago";
    private final BigDecimal AMOUNT = new BigDecimal("100.99");

    @Before
    public void setUp() throws Exception {

        String merchantId = "mptdggroasfcmqs8plpy";
        String apiKey = "***REMOVED***";
        String endpoint = "https://sand-api.ecommercebbva.com/"; //*/
        String publicIp = "138.84.62.109";
        this.api = new BbvaAPI(endpoint, apiKey, merchantId, publicIp);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));

        ParameterContainer address = new ParameterContainer("address");
        address.addValue("line1", "Calle Morelos #12 - 11");
        address.addValue("line2", "Colonia Centro");           // Optional
        address.addValue("line3", "Cuauhtémoc");               // Optional
        address.addValue("city", "Queretaro");
        address.addValue("postal_code", "12345");
        address.addValue("state", "Queretaro");
        address.addValue("country_code", "MX");

        this.customer = new ParameterContainer("customer");
        this.customer.addValue("name", "John");
        this.customer.addValue("last_name", "Doe");
        this.customer.addValue("email", "johndoe@example.com");
        this.customer.addValue("phone_number", "554-170-3567");
        this.customer.addMultiValue(address);

        this.card = new ParameterContainer("card");
        this.card.addValue("card_number", "4242424242424242");
        this.card.addValue("holder_name", "John Doe");
        this.card.addValue("expiration_year", "29");
        this.card.addValue("expiration_month", "12");
        this.card.addValue("cvv2", "842");
    }

    @Test
    public void doTest() throws Exception {
        this.testCreatePending();
        this.testConfirm();
        //this.testRefund();
        /* You need update the merchant params this.testCreateWithCard(); */
        this.testSearchById();
    }

    private void testCreatePending() throws ServiceUnavailableException, ServiceException {
        this.transaction = createTransaction(Boolean.FALSE);
        assertNotNull(this.transaction);
        assertEquals(AMOUNT, new BigDecimal(this.transaction.getSingleValue("amount").getParameterValue()));
        assertEquals(DESCRIPTION, this.transaction.getSingleValue("description").getParameterValue());
    }

    private void testConfirm() throws Exception {
        ConfirmChargeParams request = new ConfirmChargeParams();
        request.chargeId(this.transaction.getSingleValue("id").getParameterValue());
        request.deviceSessionId("PX00avsZWvTE2YKNuENo6QS5DtZ1Quvi");
        request.tokenId(this.createToken());
        Map charge = this.api.charges().confirmCharge(request);
        ParameterContainer finalCharge = new ParameterContainer("charge", charge);
        assertNotNull(finalCharge);
    }

    private void testRefund() throws Exception {
        String originalTransactionId = this.transaction.getSingleValue("id").getParameterValue();
        assertNotNull(originalTransactionId);
        String refDesc = "Reembolso";
        Map transactionAsMap = this.api.charges().refund(new RefundParams()
                .chargeId(originalTransactionId)
                .amount(AMOUNT)
                .description(refDesc));
        this.transaction = new ParameterContainer("charge", transactionAsMap);

        ParameterContainer refund = this.transaction.getContainerValue("refund");
        assertNotNull(refund);
        assertEquals(refDesc, refund.getSingleValue("description").getParameterValue());
    }

    private void testCreateWithCard() throws ServiceException, ServiceUnavailableException {
        this.transaction = createTransaction(Boolean.TRUE);
        assertNotNull(this.transaction);
        assertEquals(AMOUNT, new BigDecimal(this.transaction.getSingleValue("amount").getParameterValue()));
        assertEquals(DESCRIPTION, this.transaction.getSingleValue("description").getParameterValue());
    }

    private void testSearchById() throws ServiceUnavailableException, ServiceException {
        Map charge = api.charges().get(this.transaction.getSingleValue("id").getParameterValue());
        assertNotNull(charge);
    }

    private ParameterContainer createTransaction(Boolean withCard) throws ServiceUnavailableException, ServiceException {
        ParameterContainer charge = new ParameterContainer("charge");
        charge.addValue("affiliation_bbva", "781500");
        charge.addValue("amount", AMOUNT.toString());
        charge.addValue("description", DESCRIPTION);
        charge.addValue("currency", "MXN");
        charge.addValue("order_id", ORDER_ID);
        charge.addValue("redirect_url", "http://requestbin.fullcontact.com/w0tv73w0");
        charge.addMultiValue(this.customer);
        if (withCard) {
            charge.addMultiValue(this.card);
        }
        Map chargeAsMap = api.charges().create(charge.getParameterValues());
        return new ParameterContainer("charge", chargeAsMap);
    }

    private String createToken() throws ServiceException, ServiceUnavailableException {
        Map tokenAsMap = this.api.tokens().create(this.card.getParameterValues());
        ParameterContainer token = new ParameterContainer("token", tokenAsMap);
        return token.getSingleValue("id").getParameterValue();
    }

}
