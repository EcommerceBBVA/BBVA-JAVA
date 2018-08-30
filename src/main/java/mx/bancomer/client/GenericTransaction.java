/*
 * Copyright 2014 Opencard Inc.
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
package mx.bancomer.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents any kind of transaction. Used in listings where we can't know what kind of transactions will we get.
 *
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Getter
@Setter
@ToString(callSuper = true)
public class GenericTransaction extends Transaction {

    private Refund refund;

    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;

}
