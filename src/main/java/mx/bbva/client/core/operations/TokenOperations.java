/*
 * COPYRIGHT © 2014. BANCOMER.
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
 * Class: TokenOperations.java
 *
 */
package mx.bbva.client.core.operations;

import mx.bbva.client.core.JsonServiceClient;
import mx.bbva.client.core.requests.parameters.Parameter;
import mx.bbva.client.core.requests.parameters.ParameterBuilder;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;

import java.util.HashMap;
import java.util.List;

import static mx.bbva.client.utils.PathComponents.*;


/**
 * <p>Clase base que contiene las operaciones disponibles para la administracion de los webhooks </p>
 *
 * @version 1.0
 * @since 2014-11-27
 */
public class TokenOperations extends ServiceOperations {

    private static final String BASE_PATH = MERCHANT_ID + TOKENS;

    private static final String GET_PATH = BASE_PATH + TOKEN_ID;

    private ParameterBuilder parameterBuilder = new ParameterBuilder();

    public TokenOperations(final JsonServiceClient client) {
        super(client);
    }

    /**
     * <p>Método que permite crear un token en la plataforma</p>
     *
     * @param params Objeto contenedor de la información para la creación del token
     * @return Regresa el mismo objeto Token, pero con el id y tarjeta
     */
    public HashMap create(final List<Parameter> params) throws ServiceException, ServiceUnavailableException {
        String path = String.format(BASE_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, parameterBuilder.AsMap(params), HashMap.class);
    }

    /**
     * <p>Método que permite optener la información de un token</p>
     *
     * @param id Identificador único del token
     * @return Regresa un objeto Token
     */
    public HashMap get(final String id) throws ServiceException, ServiceUnavailableException {
        String path = String.format(GET_PATH, this.getMerchantId(), id);
        return this.getJsonClient().get(path, HashMap.class);
    }
}
