/*
 * Copyright 2012 - 2015 Opencard Inc.
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

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Getter
@ToString
public class CardPoints {

    private BigDecimal used;

    private BigDecimal remaining;

    private BigDecimal amount;

    private String caption;

}
