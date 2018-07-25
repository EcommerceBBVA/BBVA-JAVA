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
package mx.bancomer.client.exceptions;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mx.bancomer.client.RiskData;

@Getter
@Setter
@ToString(exclude = "body")
public class ServiceException extends Exception {

    private static final long serialVersionUID = -7388627000694002585L;

    private String category;

    private String description;

    @SerializedName("http_code")
    private Integer httpCode;

    @SerializedName("error_code")
    private Integer errorCode;

    @SerializedName("request_id")
    private String requestId;
    
    @SerializedName("risk_data")
    private RiskData riskData;

    private String body;

    public ServiceException() {
        super();
    }

    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final Throwable cause) {
        super(cause);
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
