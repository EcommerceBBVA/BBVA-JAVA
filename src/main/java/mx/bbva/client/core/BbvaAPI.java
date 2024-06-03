/*
 * Copyright 2013 Opencard Inc.
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
package mx.bbva.client.core;

import mx.bbva.client.core.operations.ChargeOperations;
import mx.bbva.client.core.operations.CustomerOperations;
import mx.bbva.client.core.operations.TokenOperations;

/**
 * Initializes all Operations from the Bbva API.
 * <p>
 * A custom JsonServiceClient can be used for all the operations. If only one operation is needed in all the
 * application, it may be better to initialize a JsonServiceClient and instantiate the Operation object.
 * </p>
 *
 * @author elopez
 */
public class BbvaAPI {

    private final JsonServiceClient jsonClient;

    private final CustomerOperations customerOperations;

    private final ChargeOperations chargeOperations;

    private final TokenOperations tokenOperations;

    public BbvaAPI(final String location, final String apiKey, final String merchantId, final String publicIp) {
        this(new JsonServiceClient(location, merchantId, apiKey, publicIp));
    }

    public BbvaAPI(final JsonServiceClient client) {
        this.jsonClient = client;
        this.customerOperations = new CustomerOperations(this.jsonClient);
        this.chargeOperations = new ChargeOperations(this.jsonClient);
        this.tokenOperations = new TokenOperations(this.jsonClient);
    }

    public void setTimeout(final int timeout) {
        this.jsonClient.getHttpClient().setConnectionTimeout(timeout);
    }

    public CustomerOperations customers() {
        return this.customerOperations;
    }

    public ChargeOperations charges() {
        return this.chargeOperations;
    }

    public TokenOperations tokens() {
        return this.tokenOperations;
    }

}
