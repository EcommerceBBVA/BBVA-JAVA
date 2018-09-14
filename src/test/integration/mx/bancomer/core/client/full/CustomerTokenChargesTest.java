package mx.bancomer.core.client.full;

import lombok.extern.slf4j.Slf4j;
import mx.bancomer.client.core.BancomerAPI;
import mx.bancomer.client.core.requests.parameters.Parameter;
import mx.bancomer.client.core.requests.parameters.ParameterContainer;
import mx.bancomer.client.core.requests.parameters.SingleParameter;
import mx.bancomer.client.exceptions.ServiceException;
import mx.bancomer.client.exceptions.ServiceUnavailableException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@Slf4j
@SuppressWarnings("unchecked")
public class CustomerTokenChargesTest extends BaseTest {

    private ParameterContainer customer;
    private ParameterContainer customerRequest;

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
        address.addValue("city", "Distrito Federal");
        address.addValue("postal_code", "12345");
        address.addValue("state", "Queretaro");
        address.addValue("country_code", "MX");

        this.customerRequest = new ParameterContainer("customer");
        this.customerRequest.addValue("name", "John");
        this.customerRequest.addValue("last_name", "Doe");
        this.customerRequest.addValue("email", "johndoe@example.com");
        this.customerRequest.addValue("phone_number", "554-170-3567");
        this.customerRequest.addMultiValue(address);

        Map customerAsMap = this.api.customers().create(this.customerRequest.getParameterValues());
        this.customer = new ParameterContainer("customer", customerAsMap);
    }

    @After
    public void tearDown() throws Exception {
        String customerId = this.customer.getSingleValue("id").getParameterValue();
        this.api.customers().delete(customerId);
    }

    @Test
    public void testCreate_Customer_WithToken() throws ServiceUnavailableException, ServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());

        List<Parameter> tokenChargeParams = new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("affiliation_bbva", "720931"),
                new SingleParameter("amount", "10.00"),
                new SingleParameter("description", desc),
                new SingleParameter("customer_language", "SP"),
                new SingleParameter("capture", "false"),
                new SingleParameter("use_3d_secure", "false"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", createToken()),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", orderId)
        ));

        String customerId = this.customer.getSingleValue("id").getParameterValue();

        Map transactionAsMap = this.api.charges().create(customerId, tokenChargeParams);
        ParameterContainer transaction = new ParameterContainer("transaction", transactionAsMap);
        assertNotNull(transaction);

        BigDecimal transactionAmount = new BigDecimal(transaction.getSingleValue("amount").getParameterValue());
        assertEquals(amount, transactionAmount);
        assertEquals(desc, transaction.getSingleValue("description").getParameterValue());
        String transactionId = transaction.getSingleValue("id").getParameterValue();
        transactionAsMap = this.api.charges().get(customerId, transactionId);
        transaction = new ParameterContainer("transaction", transactionAsMap);
        assertThat(transaction.getSingleValue("id").getParameterValue(), is(transactionId));
        assertNotNull(transaction);
        assertEquals(desc, transaction.getSingleValue("description").getParameterValue());
        try {
            this.api.charges().get(transactionId);
            fail("Expected can't find");
        } catch (ServiceException e) {
            assertThat(e.getHttpCode(), is(404));
        }
    }

    private String createToken() throws ServiceUnavailableException, ServiceException {
        HashMap tokenAsMap = this.api.tokens().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("card_number", "4111111111111111"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "20"),
                new SingleParameter("holder_name", "Juan Perez Lopez")
        )));
        ParameterContainer token = new ParameterContainer("token", tokenAsMap);
        return token.getSingleValue("id").getParameterValue();
    }

}
