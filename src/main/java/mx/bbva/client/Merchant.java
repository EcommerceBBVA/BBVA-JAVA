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
package mx.bbva.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Getter
public class Merchant {

    private String id;

    private String name;

    private String email;

    private String phone;

    private String status;

    @SerializedName("creation_date")
    private String creationDate;

    private BigDecimal balance;

    private String clabe;

}
