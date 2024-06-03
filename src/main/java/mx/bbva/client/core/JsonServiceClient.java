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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mx.bbva.client.core.impl.DefaultHttpServiceClient;
import mx.bbva.client.core.impl.DefaultSerializer;
import mx.bbva.client.exceptions.ServiceException;
import mx.bbva.client.exceptions.ServiceUnavailableException;
import mx.bbva.client.utils.PathComponents;

import java.util.List;
import java.util.Map;

/**
 * Calls the HTTP Service and parses the response, delegating to a HttpServiceClient and a JsonSerializer. Custom
 * implementations can be used if the defaults are not appropiate.
 *
 * @author Heber Lazcano
 * @author elopez
 */
@Slf4j
public class JsonServiceClient {

    private static final String HTTP_RESOURCE_SEPARATOR = "/";

    private final String root;

    @Getter
    private final JsonSerializer serializer;

    @Getter
    private final HttpServiceClient httpClient;

    @Getter
    private final String merchantId;

    /**
     * Initializes a JsonServiceClient with the default JsonSerializer and HttpServiceClient.
     *
     * @param location   Base URL of the Webservice.
     * @param merchantId Merchant's Id.
     * @param key        Public or private key. Public Key may have limited permissions.
     */
    public JsonServiceClient(final String location, final String merchantId, final String key, final String publicIp) {
        this(location, merchantId, key,publicIp, new DefaultSerializer(), new DefaultHttpServiceClient(true));
    }

    /**
     * Initializes a JsonServiceClient using a custom implementation of a serializer and http client. Useful if the
     * defaults need to be changed or a different Http Client library needs to be used.
     *
     * @param location
     * @param merchantId
     * @param key
     * @param serializer
     * @param httpClient
     */
    public JsonServiceClient(final String location, final String merchantId, final String key,final String publicIp,
                             final JsonSerializer serializer, final HttpServiceClient httpClient) {
        this.validateParameters(location, merchantId, publicIp);
        String url = this.getUrl(location);
        this.root = url;
        this.merchantId = merchantId;
        this.serializer = serializer;
        this.httpClient = httpClient;
        this.httpClient.setKey(key);
        this.httpClient.setPublicIp(publicIp);
    }

    private void validateParameters(final String location, final String merchantId, final String publicIp) {
        if (location == null) {
            throw new IllegalArgumentException("Location can't be null");
        }
        if (merchantId == null) {
            throw new IllegalArgumentException("Merchant ID can't be null");
        }
        if (publicIp == null|| publicIp.isEmpty()) {
            throw new IllegalArgumentException("Public Ip can't be null or empty");
        }
    }

    private String getUrl(final String location) {
        StringBuilder baseUri = new StringBuilder();
        if (location.contains("http") || location.contains("https")) {
            baseUri.append(location.replace("http:", "https:"));
        } else {
            baseUri.append("https://").append(location);
        }
        if (!location.endsWith(HTTP_RESOURCE_SEPARATOR)) {
            baseUri.append(HTTP_RESOURCE_SEPARATOR);
        }
        baseUri.append(PathComponents.VERSION);
        return baseUri.toString();
    }

    public <T> T get(final String path, final Class<T> clazz) throws ServiceException,
            ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.get(this.buildUri(path));
        this.checkForErrors(response);
        return this.deserializeObject(response, clazz);
    }

    public <T> T get(final String path, final Map<String, String> params, final Class<T> clazz)
            throws ServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.get(this.buildUri(path), params);
        this.checkForErrors(response);
        return this.deserializeObject(response, clazz);
    }

    public <T> List<T> list(final String path, final Map<String, String> params, final Class<T> clazz)
            throws ServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.get(this.buildUri(path), params);
        this.checkForErrors(response);
        return this.deserializeList(response, clazz);
    }

    public void delete(final String path) throws ServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.delete(this.buildUri(path));
        this.checkForErrors(response);
    }

    public <T> T put(final String path, final T params, final Class<T> clazz)
            throws ServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.put(this.buildUri(path), this.serializer.serialize(params));
        this.checkForErrors(response);
        return this.deserializeObject(response, clazz);
    }

    public <T> T put(final String path, final Map<String, Object> params, final Class<T> clazz)
            throws ServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.put(this.buildUri(path), this.serializer.serialize(params));
        this.checkForErrors(response);
        return this.deserializeObject(response, clazz);
    }

    public <T> T post(final String path, final Map<String, Object> params, final Class<T> clazz)
            throws ServiceException, ServiceUnavailableException {
        return this.postObjectAsJson(path, params, clazz);
    }

    public <T> T post(final String path, final T params, final Class<T> clazz) throws ServiceException,
            ServiceUnavailableException {
        return this.postObjectAsJson(path, params, clazz);
    }

    public <T> T post(final String path, final Map<String, Object> params, final Class<T> clazz,
                      final boolean withResponse) throws ServiceException, ServiceUnavailableException {
        return this.postObjectAsJson(path, params, withResponse ? clazz : null);
    }

    public <T> T post(final String path, final T params, final Class<T> clazz, final boolean withResponse)
            throws ServiceException, ServiceUnavailableException {
        return this.postObjectAsJson(path, params, withResponse ? clazz : null);
    }

    public <T> T postObjectAsJson(final String path, final Object request, final Class<T> clazz)
            throws ServiceException, ServiceUnavailableException {
        return this.postString(path, this.serializer.serialize(request), clazz);
    }

    public <T> T postString(final String path, final String request, final Class<T> clazz)
            throws ServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.post(this.buildUri(path), request);
        this.checkForErrors(response);
        if (clazz == null) {
            return null;
        } else {
            return this.deserializeObject(response, clazz);
        }
    }

    private String buildUri(final String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.root);
        sb.append(path);
        return sb.toString();
    }

    private void checkForErrors(final HttpServiceResponse response) throws ServiceException {
        if (response.getStatusCode() >= 300) {
            if (response.isJson()) {
                ServiceException error = this.serializer.deserialize(response.getBody(),
                        ServiceException.class);
                error.setBody(response.getBody());
                throw error;
            } else {
                log.error("Not a Json response. Code: {}, body: {} ", response.getStatusCode(), response.getBody());
                ServiceException serviceException = new ServiceException("["
                        + response.getStatusCode() + "] Internal server error");
                serviceException.setHttpCode(response.getStatusCode());
                serviceException.setErrorCode(1000);
                serviceException.setBody(response.getBody());
                throw serviceException;
            }
        }
    }

    private <T> T deserializeObject(final HttpServiceResponse response, final Class<T> clazz) {
        if (response.isJson()) {
            return this.serializer.deserialize(response.getBody(), clazz);
        } else if (response.getBody() != null) {
            log.debug("Body wasn't returned as JSON: {}", response.getBody());
        }
        return null;
    }

    private <T> List<T> deserializeList(final HttpServiceResponse response, final Class<T> clazz) {
        if (response.isJson()) {
            return this.serializer.deserializeList(response.getBody(), clazz);
        } else {
            log.debug("Body wasn't returned as JSON: {}", response.getBody());
        }
        return null;
    }

}
