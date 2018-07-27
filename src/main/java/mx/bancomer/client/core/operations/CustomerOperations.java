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
package mx.bancomer.client.core.operations;

import static mx.bancomer.client.utils.PathComponents.CUSTOMERS;
import static mx.bancomer.client.utils.PathComponents.ID;
import static mx.bancomer.client.utils.PathComponents.MERCHANT_ID;

import java.util.List;
import java.util.Map;

import mx.bancomer.client.Customer;
import mx.bancomer.client.core.JsonServiceClient;
import mx.bancomer.client.core.requests.parameters.Parameter;
import mx.bancomer.client.core.requests.parameters.ParameterBuilder;
import mx.bancomer.client.exceptions.ServiceException;
import mx.bancomer.client.exceptions.ServiceUnavailableException;
import mx.bancomer.client.utils.SearchParams;

/**
 * Operations for managing Customers.
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

    public Customer create(final List<Parameter> params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, parameterBuilder.AsMap(params), Customer.class);
    }

    public List<Customer> list(final SearchParams params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Customer.class);
    }

    public Customer get(final String customerId) throws ServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().get(path, Customer.class);
    }

    public void delete(final String customerId) throws ServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        this.getJsonClient().delete(path);
    }

}
