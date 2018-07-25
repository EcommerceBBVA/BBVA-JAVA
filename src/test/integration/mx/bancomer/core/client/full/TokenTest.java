/*
 * COPYRIGHT © 2014. OPENPAY.
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
 * Change control:
 * ---------------------------------------------------------------------------------------
 * Version | Date       | Name                                      | Description
 * ---------------------------------------------------------------------------------------
 *   1.0	2014-11-28	Marcos Coronado marcos.coronado@openpay.mx	 Creating Class.
 *
 */
package mx.bancomer.core.client.full;

import lombok.extern.slf4j.Slf4j;
import mx.bancomer.client.Token;
import mx.bancomer.client.core.BancomerAPI;
import mx.bancomer.client.core.requests.parameters.Parameter;
import mx.bancomer.client.core.requests.parameters.SingleParameter;
import mx.bancomer.client.exceptions.ServiceException;
import mx.bancomer.client.exceptions.ServiceUnavailableException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>
 * Clase utilizada como contenedor de los parametros para la creacion de un Token
 * </p>
 *
 * @author Marcos Coronado marcos.coronado@openpay.mx
 * @version 1.0
 * @since 2014-11-28
 */
@Slf4j
public class TokenTest {

    private BancomerAPI api;

    private String tokenId;

    @Before
    public void setUp() {
        String merchantId = "miklpzr4nsvsucghm2qp";
        String apiKey = "sk_08453429e4c54220a3a82ab4d974c31a";
        String endpoint = "https://dev-api.openpay.mx/";
        this.api = new BancomerAPI(endpoint, apiKey, merchantId);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @Test
    public void testCreateToken() throws ServiceException, ServiceUnavailableException {
        // Create the list of parameters that you'll fill and you can add a single parameter only with a name and value
        /** Example
        List<Parameter> tokenParams2 = new ArrayList<Parameter>();
        tokenParams2.add(new SingleParameter("card_number", "4111111111111111"));
        tokenParams2.add(new SingleParameter("cvv2", "295"));
        tokenParams2.add(new SingleParameter("expiration_month", "12"));
        tokenParams2.add(new SingleParameter("expiration_year", "20"));
        tokenParams2.add(new SingleParameter("holder_name", "Juan Perez Lopez")); */

        Token token = this.api.tokens().create(new ArrayList<Parameter>(Arrays.asList(
                new SingleParameter("card_number", "4111111111111111"),
                new SingleParameter("cvv2", "295"),
                new SingleParameter("expiration_month", "12"),
                new SingleParameter("expiration_year", "20"),
                new SingleParameter("holder_name", "Juan Perez Lopez")
        )));

        log.info(token.toString());
        this.tokenId = token.getId();
    }

    @Test
    public void testGetToken() throws ServiceException, ServiceUnavailableException {
        Token token;
        this.testCreateToken();
        token = this.api.tokens().get(this.tokenId);
        log.info(token.toString());
        Assert.assertNotNull(token.getId());
        this.tokenId = token.getId();
    }

}
