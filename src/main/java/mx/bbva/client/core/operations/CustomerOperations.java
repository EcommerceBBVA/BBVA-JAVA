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
package mx.bbva.client.core.operations;

import mx.bbva.client.Customer;
import mx.bbva.client.core.JsonServiceClient;
import mx.bbva.client.core.requests.parameters.Parameter;
import mx.bbva.client.core.requests.parameters.ParameterBuilder;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;
import mx.bbva.client.utils.SearchParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mx.bbva.client.utils.PathComponents.*;

/**
 * Operations for managing Customers.
 *
 * @author elopez
 */
public class CustomerOperations extends ServiceOperations {

    private static final String CUSTOMERS_PATH = MERCHANT_ID + CUSTOMERS;

    private static final String GET_CUSTOMER_PATH = CUSTOMERS_PATH + ID;

    private ParameterBuilder parameterBuilder = new ParameterBuilder();

    public CustomerOperations(final JsonServiceClient client) {
        super(client);
    }

    public Customer create(final Customer create) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, create, Customer.class);
    }

    public HashMap create(final List<Parameter> params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, parameterBuilder.AsMap(params), HashMap.class);
    }

    public List<HashMap> list(final SearchParams params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, HashMap.class);
    }

    public HashMap get(final String customerId) throws ServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().get(path, HashMap.class);
    }

    public void delete(final String customerId) throws ServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        this.getJsonClient().delete(path);
    }

}
