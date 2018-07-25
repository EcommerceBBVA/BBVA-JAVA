package mx.bancomer.core.client.full;

import lombok.extern.slf4j.Slf4j;
import mx.bancomer.client.Charge;
import mx.bancomer.client.Customer;
import mx.bancomer.client.Token;
import mx.bancomer.client.core.requests.parameters.Parameter;
import mx.bancomer.client.core.requests.parameters.SingleParameter;
import mx.bancomer.client.exceptions.ServiceException;
import mx.bancomer.client.exceptions.ServiceUnavailableException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@Slf4j
public class CustomerTokenChargesTest extends BaseTest {

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(
                new Customer().name("John").lastName("Doe").email("john.doe@mail.com")
                .phoneNumber("55-25634013"));
    }

    @After
    public void tearDown() throws Exception {
        this.api.customers().delete(this.customer.getId());
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
                new SingleParameter("capture", "FALSE"),
                new SingleParameter("use_3d_secure", "FALSE"),
                new SingleParameter("use_card_points", "NONE"),
                new SingleParameter("token", createToken()),
                new SingleParameter("currency", "MXN"),
                new SingleParameter("order_id", orderId)

        ));

        Charge transaction = this.api.charges().create(this.customer.getId(), tokenChargeParams);
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        String transactionId = transaction.getId();
        transaction = this.api.charges().get(this.customer.getId(), transactionId);
        assertThat(transaction.getId(), is(transactionId));
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        Assert.assertNotNull(transaction.getFee());
        try {
            this.api.charges().get(transactionId);
            fail("Expected can't find");
        } catch (ServiceException e) {
            assertThat(e.getHttpCode(), is(404));
        }
    }

    private String createToken() throws ServiceUnavailableException, ServiceException {
        Token token = this.api.tokens().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("card_number", "4111111111111111"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "20"),
                new SingleParameter("holder_name", "Juan Perez Lopez")
        )));
        return token.getId();
    }

}
