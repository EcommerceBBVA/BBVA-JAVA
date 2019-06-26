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

import mx.bbva.client.core.JsonServiceClient;
import mx.bbva.client.core.requests.parameters.Parameter;
import mx.bbva.client.core.requests.parameters.ParameterBuilder;
import mx.bbva.client.core.requests.transactions.ConfirmCaptureParams;
import mx.bbva.client.core.requests.transactions.ConfirmChargeParams;
import mx.bbva.client.core.requests.transactions.RefundParams;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;
import mx.bbva.client.utils.PathComponents;

import java.util.HashMap;
import java.util.List;

import static mx.bbva.client.utils.PathComponents.*;

/**
 * Operations for BBVA Charges.
 *
 * @author elopez
 */
public class ChargeOperations extends ServiceOperations {

    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + CHARGES;

    private static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + PathComponents.ID;

    private static final String REFUND_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + PathComponents.REFUND;

    private static final String CAPTURE_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + CAPTURE;

    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + CHARGES;

    private static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    private static final String REFUND_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + REFUND;

    private static final String CAPTURE_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + CAPTURE;

    private static final String CONFIRM_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + CONFIRM;

    private static final String CONFIRM_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + CONFIRM;

    public ChargeOperations(final JsonServiceClient client) {
        super(client);
    }

    private ParameterBuilder parameterBuilder = new ParameterBuilder();

    /**
     * Creates any kind of charge at the Merchant level.
     *
     * @param params Specific request params.
     * @return Charge data returned by BBVA
     * @throws ServiceException            When BBVA returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     */
    public HashMap create(final List<Parameter> params) throws ServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, parameterBuilder.AsMap(params), HashMap.class);
    }

    /**
     * Creates any kind of charge at the Customer level.
     *
     * @param customerId ID of the Customer created previously in BBVA.
     * @param params     Generic request params.
     * @return Charge data returned by BBVA
     * @throws ServiceException            When BBVA returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs
     */
    public HashMap create(final String customerId, List<Parameter> params)
            throws ServiceException, ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, parameterBuilder.AsMap(params), HashMap.class);
    }

    /**
     * This will return a specific charge merchant level
     */
    public HashMap get(final String transactionId) throws ServiceException, ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        return this.getJsonClient().get(path, HashMap.class);
    }

    /**
     * This will return a specific charge customer level
     */
    public HashMap get(final String customerId, final String transactionId) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        return this.getJsonClient().get(path, HashMap.class);
    }

    /**
     * Refund Merchant Level
     */
    public HashMap refund(final RefundParams params) throws ServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), HashMap.class);
    }

    /**
     * Refund Customer Level
     */
    public HashMap refund(final String customerId, final RefundParams params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(REFUND_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), HashMap.class);
    }

    /**
     * Confirms a charge that was made with the option capture set to false.
     */
    public HashMap confirmCapture(final ConfirmCaptureParams params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CAPTURE_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), HashMap.class);
    }

    /**
     * Confirms a charge that was made with the option capture set to false. Customer Level
     */
    public HashMap confirmCapture(final String customerId, final ConfirmCaptureParams params)
            throws ServiceException, ServiceUnavailableException {
        String path = String.format(CAPTURE_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId,
                params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), HashMap.class);
    }

    /**
     * Confirms a charge that was made with the option confirm set to false.
     */
    public HashMap confirmCharge(final ConfirmChargeParams params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CONFIRM_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), HashMap.class);
    }

    /**
     * Confirms a charge that was made with the option confirm set to false.
     */
    public HashMap confirmCharge(final String customerId, final ConfirmChargeParams params) throws ServiceException, ServiceUnavailableException {
        String path = String.format(CONFIRM_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId,
                params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), HashMap.class);
    }


}
