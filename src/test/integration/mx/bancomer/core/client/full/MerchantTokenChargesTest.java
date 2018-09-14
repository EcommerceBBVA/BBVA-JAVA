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
package mx.bancomer.core.client.full;

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
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@SuppressWarnings("unchecked")
@Slf4j
public class MerchantTokenChargesTest extends BaseTest {

    private ParameterContainer customer;

    @Before
    public void setUp() throws Exception {
        String merchantId = "mdopyxbg6cacgbdvqqxd";
        String apiKey = "sk_36ad147b36234a30bc279031ac17e1a6";
        String endpoint = "https://sandbox-api.openpay.mx/";
        this.api = new BancomerAPI(endpoint, apiKey, merchantId);
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
    }

    @Test
    public void testCreate() throws ServiceUnavailableException, ServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        ParameterContainer transaction = createTransaction();
        assertNotNull(transaction);
        assertEquals(amount, new BigDecimal(transaction.getSingleValue("amount").getParameterValue()));
        assertEquals(desc, transaction.getSingleValue("description").getParameterValue());
        ParameterContainer cardPoints = transaction.getContainerValue("card_points");
        assertThat(cardPoints, is(nullValue()));
    }

    @Test
    public void testSearchById() throws ServiceUnavailableException, ServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        ParameterContainer transaction = createTransaction();
        assertNotNull(transaction);
        assertEquals(amount, new BigDecimal(transaction.getSingleValue("amount").getParameterValue()));
        assertEquals(desc, transaction.getSingleValue("description").getParameterValue());
        ParameterContainer cardPoints = transaction.getContainerValue("card_points");
        assertThat(cardPoints, is(nullValue()));
        Map charge = api.charges().get(transaction.getSingleValue("id").getParameterValue());
        assertNotNull(charge);
    }

    @Test
    public void testCreate_WithCaptureFalse() throws ServiceUnavailableException, ServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());

        Map transactionAsMap = api.charges().create(Arrays.asList(
                new SingleParameter("affiliation_bbva", "720931"),
                new SingleParameter("amount", "10.00"),
                new SingleParameter("description", desc),
                new SingleParameter("customer_language", "SP"),
                new SingleParameter("capture", "false"),
                new SingleParameter("use_3d_secure", "false"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", createToken()),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", orderId),
                this.customer
        ));

        ParameterContainer transaction = new ParameterContainer("charge", transactionAsMap);
        String transactionId = transaction.getSingleValue("id").getParameterValue();
        assertNotNull(transaction);
        assertEquals(amount, new BigDecimal(transaction.getSingleValue("amount").getParameterValue()));
        assertEquals(desc, transaction.getSingleValue("description").getParameterValue());
        assertEquals("in_progress", transaction.getSingleValue("status").getParameterValue());

        Map confirmedAsMap = this.api.charges().confirmCapture(new ConfirmCaptureParams().chargeId(transactionId)
                .amount(amount));
        ParameterContainer confirmed = new ParameterContainer("charge", confirmedAsMap);
        assertEquals("completed", confirmed.getSingleValue("status").getParameterValue());
    }

    @Test
    public void testRefund() throws Exception {
        ParameterContainer transaction = createTransaction();
        String originalTransactionId = transaction.getSingleValue("id").getParameterValue();
        assertNotNull(transaction);
        String refDesc = "cancelacion (ignored description)";
        Map transactionAsMap = this.api.charges().refund(new RefundParams()
                .chargeId(originalTransactionId)
                .description(refDesc));
        transaction = new ParameterContainer("charge", transactionAsMap);

        ParameterContainer refund = transaction.getContainerValue("refund");
        assertNotNull(refund);
        assertNull(refund.getContainerValue("fee"));
        assertEquals(refDesc, refund.getSingleValue("description").getParameterValue());
    }

    private String createToken() throws ServiceUnavailableException, ServiceException {
        HashMap token = this.api.tokens().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("card_number", "4111111111111111"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "20"),
                new SingleParameter("holder_name", "Juan Perez Lopez")
        )));
        return token.get("id").toString();
    }

    private ParameterContainer createTransaction() throws ServiceUnavailableException, ServiceException {
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        List<Parameter> tokenChargeParams = new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "720931"),
                new SingleParameter("amount", "10.00"),
                new SingleParameter("description", desc),
                new SingleParameter("customer_language", "SP"),
                new SingleParameter("capture", "true"),
                new SingleParameter("use_3d_secure", "false"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", createToken()),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", orderId)

        ));
        tokenChargeParams.add(this.customer);
        Map chargeAsMap = api.charges().create(tokenChargeParams);
        return new ParameterContainer("charge", chargeAsMap);
    }

}
