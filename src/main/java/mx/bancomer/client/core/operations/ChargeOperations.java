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

import mx.bancomer.client.Charge;
import mx.bancomer.client.core.requests.parameters.Parameter;
import mx.bancomer.client.core.requests.parameters.ParameterBuilder;
import mx.bancomer.client.exceptions.ServiceException;
import mx.bancomer.client.exceptions.ServiceUnavailableException;
import mx.bancomer.client.utils.PathComponents;
import mx.bancomer.client.core.JsonServiceClient;
import mx.bancomer.client.core.requests.transactions.ConfirmCaptureParams;
import mx.bancomer.client.core.requests.transactions.RefundParams;

import java.util.List;

/**
 * Operations for Openpay Charges.
 * @author elopez
 */
public class ChargeOperations extends ServiceOperations {

    private static final String FOR_MERCHANT_PATH = PathComponents.MERCHANT_ID + PathComponents.CHARGES;

    private static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + PathComponents.ID;

    private static final String REFUND_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + PathComponents.REFUND;

    private static final String CAPTURE_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + PathComponents.CAPTURE;

    public ChargeOperations(final JsonServiceClient client) {
        super(client);
    }

    private ParameterBuilder parameterBuilder = new ParameterBuilder();

    /**
     * Creates any kind of charge at the Merchant level.
     * @param params Specific request params.
     * @return Charge data returned by Openpay
     * @throws ServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     */
    public Charge createCharge(final List<Parameter> params) throws ServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, parameterBuilder.AsMap(params), Charge.class);
    }

    /** This will return a specific charge  */
    public Charge get(final String transactionId) throws ServiceException, ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        return this.getJsonClient().get(path, Charge.class);
    }

    /** Refund  */
    public Charge refund(final RefundParams params) throws ServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    /** Confirms a charge that was made with the option capture set to false.*/
    public Charge confirmCapture(final ConfirmCaptureParams params) throws ServiceException,
            ServiceUnavailableException {
        String path = String.format(CAPTURE_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

}
