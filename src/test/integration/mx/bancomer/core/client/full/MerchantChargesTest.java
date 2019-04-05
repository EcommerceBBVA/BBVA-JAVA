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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Categories;

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
public class MerchantChargesTest extends BaseTest {

    private ParameterContainer customer;

    @Before
    public void setUp() throws Exception {
        String merchantId = "mptdggroasfcmqs8plpy";
        String apiKey = "sk_326c6d0443f6457aae29ffbd48f7d1be";
        String endpoint = "https://sand-api.ecommercebbva.com/";
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
        BigDecimal amount = new BigDecimal("10.99");
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
        BigDecimal amount = new BigDecimal("10.99");
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

    @Ignore
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

    private ParameterContainer createTransaction() throws ServiceUnavailableException, ServiceException {
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        List<Parameter> chargeParams = new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "781500"),
                new SingleParameter("amount", "10.99"),
                new SingleParameter("description", desc),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", orderId)

        ));
        chargeParams.add(this.customer);
        Map chargeAsMap = api.charges().create(chargeParams);
        return new ParameterContainer("charge", chargeAsMap);
    }

}
