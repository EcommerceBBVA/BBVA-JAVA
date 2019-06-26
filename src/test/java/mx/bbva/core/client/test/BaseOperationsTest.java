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
package mx.bbva.core.client.test;

import lombok.Getter;
import mx.bbva.client.core.BbvaAPI;
import org.junit.Before;

import java.util.TimeZone;

import static mx.bbva.core.client.TestConstans.*;

/**
 * @author elopez
 */
public class BaseOperationsTest {

    @Getter
    private BbvaAPI api;

    @Before
    public void setUp() throws Exception {
        BbvaAPI api = new BbvaAPI(ENDPOINT, API_KEY, MERCHANT_ID);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

}
