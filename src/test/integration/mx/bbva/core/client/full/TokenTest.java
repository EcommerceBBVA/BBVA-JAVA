/*
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
 *
 * Class: TokenTest.java
 *
 *
 */
package mx.bbva.core.client.full;

import lombok.extern.slf4j.Slf4j;
import mx.bbva.client.core.BbvaAPI;
import mx.bbva.client.core.requests.parameters.Parameter;
import mx.bbva.client.core.requests.parameters.ParameterContainer;
import mx.bbva.client.core.requests.parameters.SingleParameter;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TimeZone;

/**
 * <p>
 * Clase utilizada como contenedor de los parametros para la creacion de un Token
 * </p>
 * @version 1.0
 * @since 2014-11-28
 */
@Slf4j
@SuppressWarnings("unchecked")
public class TokenTest {

    private BbvaAPI api;

    private String tokenId;

    private ParameterContainer customer;

    @Before
    public void setUp() throws Exception {
        String merchantId = "mevnavqc676iim4nfq63";
        String apiKey = "sk_142f6bf4be0145f8bae51ec2b0b0d4d3";
        String endpoint = "https://dev-api.openpay.mx/";
        String publicIp = "138.84.62.109";
        this.api = new BbvaAPI(endpoint, apiKey, merchantId, publicIp);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
        Map customerAsMap = this.api.customers().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("name", "John"),
                new SingleParameter("last_name", "Doe"),
                new SingleParameter("email", "john@mail.com"),
                new SingleParameter("phone_number", "55-25634013")
        )));
        this.customer = new ParameterContainer("customer", customerAsMap);
    }

    @After
    public void tearDown() throws Exception {
        if (this.customer != null) {
            this.api.customers().delete(this.customer.getSingleValue("id").getParameterValue());
        }
    }

    @Test
    public void testCreateToken() throws ServiceException, ServiceUnavailableException {
        // Create the list of parameters that you'll fill and you can add a single parameter only with a name and value
        Map tokenAsMap = this.api.tokens().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("card_number", "4111111111111111"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "29"),
                new SingleParameter("holder_name", "Juan Perez Lopez")
        )));
        ParameterContainer token = new ParameterContainer("token", tokenAsMap);
        Assert.assertNotNull(token);
        Assert.assertNotNull(token.getSingleValue("id"));
        this.tokenId = token.getSingleValue("id").getParameterValue();
    }

    @Test
    public void testGetToken() throws ServiceException, ServiceUnavailableException {
        Map tokenAsMap;
        this.testCreateToken();
        tokenAsMap = this.api.tokens().get(this.tokenId);
        ParameterContainer token = new ParameterContainer("token", tokenAsMap);
        Assert.assertNotNull(token);
        Assert.assertNotNull(token.getSingleValue("id"));
        this.tokenId = token.getSingleValue("id").getParameterValue();
    }

}
