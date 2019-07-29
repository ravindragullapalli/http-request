package com.jsunsoft.http;

/*
 * Copyright 2017 Benik Arakelyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

/**
 * A resource target identified by the resource URI.
 */
public interface WebTarget {

    /**
     * Append path to the URI of the current target instance.
     *
     * @param path the path.
     * @return target instance.
     * @throws NullPointerException if path is {@code null}.
     */
    WebTarget path(final String path);

    /**
     * Invoke an arbitrary method for the current request.
     *
     * @param method the http method.
     * @return the response to the request. This is always a final response, never an intermediate response with an 1xx status code.
     * Whether redirects or authentication challenges will be returned
     * or handled automatically depends on the implementation and configuration of this client.
     * @throws ResponseException in case of any IO problem or the connection was aborted.
     * @throws RequestException  in case of an http protocol error.
     */
    Response request(final HttpMethod method);

    /**
     * Invoke an arbitrary method for the current request.
     *
     * @param method     the http method.
     * @param httpEntity httpEntity
     * @return the response to the request. This is always a final response, never an intermediate response with an 1xx status code.
     * Whether redirects or authentication challenges will be returned
     * or handled automatically depends on the implementation and configuration of this client.
     * @throws ResponseException in case of any IO problem or the connection was aborted.
     * @throws RequestException  in case of an http protocol error.
     */
    Response request(final HttpMethod method, final HttpEntity httpEntity);

    /**
     * Invoke an arbitrary method for the current request.
     *
     * @param method       the http method.
     * @param httpEntity   httpEntity
     * @param responseType Java type the response entity will be converted to.
     * @param <T>          response entity type.
     * @return the ResponseHandler instance to the request and pass converted response in ResponseHandler instance.
     * or handled automatically depends on the implementation and configuration of this client.
     * @throws ResponseException in case of any IO problem or the connection was aborted.
     * @throws RequestException  in case of an http protocol error.
     * @see #request(HttpMethod, HttpEntity)
     * @see ResponseHandler
     */
    <T> ResponseHandler<T> request(final HttpMethod method, final HttpEntity httpEntity, Class<T> responseType);

    /**
     * Invoke an arbitrary method for the current request.
     *
     * @param method       the http method.
     * @param httpEntity   httpEntity
     * @param responseType representation of a TypeReference Java type the response entity will be converted to.
     * @param <T>          response entity type.
     * @return the ResponseHandler instance to the request and pass converted response in ResponseHandler instance.
     * or handled automatically depends on the implementation and configuration of this client.
     * @throws ResponseException in case of any IO problem or the connection was aborted.
     * @throws RequestException  in case of an http protocol error.
     * @see #request(HttpMethod, HttpEntity)
     * @see ResponseHandler
     */
    <T> ResponseHandler<T> request(final HttpMethod method, final HttpEntity httpEntity, TypeReference<T> responseType);

    /**
     * Invoke an arbitrary method for the current request.
     *
     * @param method       the http method.
     * @param responseType Java type the response entity will be converted to.
     * @param <T>          response entity type.
     * @return the ResponseHandler instance to the request and pass converted response in ResponseHandler instance.
     * or handled automatically depends on the implementation and configuration of this client.
     * @throws ResponseException in case of any IO problem or the connection was aborted.
     * @throws RequestException  in case of an http protocol error.
     * @see #request(HttpMethod)
     * @see ResponseHandler
     */
    <T> ResponseHandler<T> request(final HttpMethod method, Class<T> responseType);

    /**
     * Invoke an arbitrary method for the current request.
     *
     * @param method       the http method.
     * @param responseType epresentation of a TypeReference Java type the response entity will be converted to.
     * @param <T>          response entity type.
     * @return the ResponseHandler instance to the request and pass converted response in ResponseHandler instance.
     * or handled automatically depends on the implementation and configuration of this client.
     * @throws ResponseException in case of any IO problem or the connection was aborted.
     * @throws RequestException  in case of an http protocol error.
     * @see #request(HttpMethod)
     * @see ResponseHandler
     */
    <T> ResponseHandler<T> request(final HttpMethod method, TypeReference<T> responseType);

    /**
     * Invoke an arbitrary method for the current request.
     * <p>
     * Mainly designed to use in case when response body aren't interested.
     * </p>
     * Any attempt to get content from {@code ResponseHandler} will be thrown exception
     *
     * @param method the http method.
     * @return the ResponseHandler instance to the request and pass converted response in ResponseHandler instance.
     * or handled automatically depends on the implementation and configuration of this client.
     * @throws ResponseException in case of any IO problem or the connection was aborted.
     * @throws RequestException  in case of an http protocol error.
     * @see #request(HttpMethod)
     * @see ResponseHandler
     */
    default ResponseHandler<?> rawRequest(final HttpMethod method) {
        return request(method, Void.class);
    }

    /**
     * Removes the given header.
     *
     * @param header the header to remove
     * @return WebTarget instance
     */
    WebTarget removeHeader(final Header header);

    /**
     * Removes all headers with name.
     *
     * @param name the header name
     * @return WebTarget instance
     */
    WebTarget removeHeaders(final String name);

    /**
     * Replaces the first occurence of the header with the same name. If no header with
     * the same name is found the given header is added to the end of the list.
     *
     * @param header the new header that should replace the first header with the same
     *               name if present in the list.
     * @return WebTarget instance
     */
    WebTarget updateHeader(final Header header);

    /**
     * Adds the given header to the request. The order in which this header was added is preserved.
     *
     * @param header header instance. Can't be null
     * @return WebTarget instance
     */
    WebTarget addHeader(final Header header);

    /**
     * The same as {@link #request(HttpMethod, HttpEntity, Class)} wrapped {@code payload} into {@link StringEntity}
     *
     * @param method       the http method.
     * @param payload      payload
     * @param responseType Java type the response entity will be converted to.
     * @param <T>
     * @return
     */
    default <T> ResponseHandler<T> request(final HttpMethod method, final String payload, Class<T> responseType) {
        ArgsCheck.notNull(method, "method");
        ArgsCheck.notNull(payload, "payload");
        ArgsCheck.notNull(payload, "responseType");

        return request(method, new StringEntity(payload, UTF_8), responseType);
    }

    default <T> ResponseHandler<T> request(final HttpMethod method, final String payload, TypeReference<T> responseType) {
        ArgsCheck.notNull(method, "method");
        ArgsCheck.notNull(payload, "payload");
        ArgsCheck.notNull(payload, "responseType");

        return request(method, new StringEntity(payload, UTF_8), responseType);
    }

    default Response request(final HttpMethod method, final String payload) {
        ArgsCheck.notNull(method, "method");
        ArgsCheck.notNull(payload, "payload");

        return request(method, new StringEntity(payload, UTF_8));
    }

    /**
     * Adds the given name and value as header to the request.
     *
     * @param name  name of header. Can't be null
     * @param value value of header
     * @return WebTarget instance
     */
    default WebTarget addHeader(final String name, final String value) {
        ArgsCheck.notNull(name, "name");
        addHeader(new BasicHeader(name, value));
        return this;
    }

    /**
     * Adds the given headers to the request. The order in which this header was added is preserved.
     *
     * @param headers collections of headers
     * @return WebTarget instance
     */
    default WebTarget addHeaders(final Collection<? extends Header> headers) {
        ArgsCheck.notNull(headers, "headers");

        headers.forEach(this::addHeader);
        return this;
    }

    /**
     * Replaces the first occurence of the header with the same name by the value. If no header with
     * the same name is found the given header is added to the end of the list.
     *
     * @param name  name of header. Can't be null
     * @param value value of header
     * @return WebTarget instance
     */
    default WebTarget updateHeader(final String name, final String value) {
        ArgsCheck.notNull(name, "name");

        updateHeader(new BasicHeader(name, value));
        return this;
    }

    /**
     * Sets content type to header
     *
     * @param contentType content type of request header
     * @return WebTarget instance
     */
    default WebTarget addContentType(final ContentType contentType) {
        addHeader(CONTENT_TYPE, contentType.toString());
        return this;
    }

    /**
     * Sets the {@code requestConfig} to the request
     *
     * @param requestConfig requestConfig
     * @return WebTarget instance
     * @see RequestConfig
     */
    WebTarget setRequestConfig(final RequestConfig requestConfig);

    WebTarget addParameter(final NameValuePair nameValuePair);

    default WebTarget addParameters(final NameValuePair... parameters) {
        ArgsCheck.notNull(parameters, "parameters");

        Arrays.stream(parameters).forEach(this::addParameter);
        return this;
    }

    default WebTarget addParameters(final String queryString, final Charset charset) {
        ArgsCheck.notNull(queryString, "queryString");
        ArgsCheck.notNull(charset, "charset");

        return addParameters(URLEncodedUtils.parse(queryString, charset));
    }

    default WebTarget addParameters(final String queryString) {
        return addParameters(queryString, UTF_8);
    }

    default WebTarget addParameters(final String... nameValues) {
        int nameValuesLength = ArgsCheck.notNull(nameValues, "nameValues").length;
        Args.check(nameValuesLength != 0, "Length of parameter can't be ZERO");
        Args.check(nameValuesLength % 2 == 0, "Length of nameValues can't be odd");

        int end = nameValuesLength - 2;

        for (int i = 0; i <= end; i += 2) {
            addParameter(new BasicNameValuePair(nameValues[i], nameValues[i + 1]));
        }

        return this;
    }

    default WebTarget addParameters(final Collection<? extends NameValuePair> parameters) {
        ArgsCheck.notNull(parameters, "parameters");

        parameters.forEach(this::addParameter);
        return this;
    }

    default WebTarget addParameters(final Map<String, String> parameters) {
        ArgsCheck.notNull(parameters, "parameters");

        parameters.entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .forEach(this::addParameter);
        return this;
    }

    default WebTarget addParameter(final String name, final String value) {
        ArgsCheck.notNull(name, "name");

        return addParameter(new BasicNameValuePair(name, value));
    }


    /**
     * Invoke HTTP GET method for the current request
     *
     * @return the response to the request.
     * @see #request(HttpMethod)
     */
    default Response get() {
        return request(HttpMethod.GET);
    }

    /**
     * Invoke HTTP GET method for the current request
     *
     * @return the ResponseHandler instance to the request.
     * @see #rawRequest(HttpMethod)
     */
    default ResponseHandler<?> rawGet() {
        return rawRequest(HttpMethod.GET);
    }

    /**
     * Invoke HTTP GET method for the current request
     *
     * @return the ResponseHandler instance to the request.
     * @see #request(HttpMethod, HttpEntity)
     */
    default Response get(final HttpEntity httpEntity) {
        return request(HttpMethod.GET, httpEntity);
    }

    default <T> ResponseHandler<T> get(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.GET, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> get(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.GET, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> get(final String payload, Class<T> responseType) {
        return request(HttpMethod.GET, payload, responseType);
    }

    default <T> ResponseHandler<T> get(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.GET, payload, responseType);
    }

    default <T> ResponseHandler<T> get(Class<T> responseType) {
        return request(HttpMethod.GET, responseType);
    }

    default <T> ResponseHandler<T> get(TypeReference<T> responseType) {
        return request(HttpMethod.GET, responseType);
    }

    default Response get(final String payload) {
        return request(HttpMethod.GET, payload);
    }

    default Response put() {
        return request(HttpMethod.PUT);
    }

    default ResponseHandler<?> rawPut() {
        return rawRequest(HttpMethod.PUT);
    }

    default Response put(final HttpEntity httpEntity) {
        return request(HttpMethod.PUT, httpEntity);
    }

    default <T> ResponseHandler<T> put(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.PUT, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> put(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.PUT, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> put(final String payload, Class<T> responseType) {
        return request(HttpMethod.PUT, payload, responseType);
    }

    default <T> ResponseHandler<T> put(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.PUT, payload, responseType);
    }

    default Response put(final String payload) {
        return request(HttpMethod.PUT, payload);
    }


    default Response post() {
        return request(HttpMethod.POST);
    }

    default ResponseHandler<?> rawPost() {
        return rawRequest(HttpMethod.POST);
    }

    default Response post(final HttpEntity httpEntity) {
        return request(HttpMethod.POST, httpEntity);
    }

    default <T> ResponseHandler<T> post(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.POST, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> post(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.POST, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> post(final String payload, Class<T> responseType) {
        return request(HttpMethod.POST, payload, responseType);
    }

    default <T> ResponseHandler<T> post(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.POST, payload, responseType);
    }

    default Response post(final String payload) {
        return request(HttpMethod.POST, payload);
    }

    default Response head() {
        return request(HttpMethod.HEAD);
    }

    default ResponseHandler<?> rawHead() {
        return rawRequest(HttpMethod.HEAD);
    }

    default Response head(final HttpEntity httpEntity) {
        return request(HttpMethod.HEAD, httpEntity);
    }

    default <T> ResponseHandler<T> head(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.HEAD, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> head(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.HEAD, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> head(final String payload, Class<T> responseType) {
        return request(HttpMethod.HEAD, payload, responseType);
    }

    default <T> ResponseHandler<T> head(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.HEAD, payload, responseType);
    }

    default Response head(final String payload) {
        return request(HttpMethod.HEAD, payload);
    }


    default Response delete() {
        return request(HttpMethod.DELETE);
    }

    default ResponseHandler<?> rawDelete() {
        return rawRequest(HttpMethod.DELETE);
    }

    default Response delete(final HttpEntity httpEntity) {
        return request(HttpMethod.DELETE, httpEntity);
    }

    default <T> ResponseHandler<T> delete(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.DELETE, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> delete(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.DELETE, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> delete(final String payload, Class<T> responseType) {
        return request(HttpMethod.DELETE, payload, responseType);
    }

    default <T> ResponseHandler<T> delete(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.DELETE, payload, responseType);
    }

    default Response delete(final String payload) {
        return request(HttpMethod.DELETE, payload);
    }


    default Response options() {
        return request(HttpMethod.OPTIONS);
    }

    default ResponseHandler<?> rawOptions() {
        return rawRequest(HttpMethod.OPTIONS);
    }


    default Response options(final HttpEntity httpEntity) {
        return request(HttpMethod.OPTIONS, httpEntity);
    }

    default <T> ResponseHandler<T> options(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.OPTIONS, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> options(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.OPTIONS, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> options(final String payload, Class<T> responseType) {
        return request(HttpMethod.OPTIONS, payload, responseType);
    }

    default <T> ResponseHandler<T> options(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.OPTIONS, payload, responseType);
    }

    default Response options(final String payload) {
        return request(HttpMethod.OPTIONS, payload);
    }


    default Response patch() {
        return request(HttpMethod.PATCH);
    }

    default ResponseHandler<?> rawPatch() {
        return rawRequest(HttpMethod.PATCH);
    }


    default Response patch(final HttpEntity httpEntity) {
        return request(HttpMethod.PATCH, httpEntity);
    }

    default <T> ResponseHandler<T> patch(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.PATCH, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> patch(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.PATCH, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> patch(final String payload, Class<T> responseType) {
        return request(HttpMethod.PATCH, payload, responseType);
    }

    default <T> ResponseHandler<T> patch(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.PATCH, payload, responseType);
    }

    default Response patch(final String payload) {
        return request(HttpMethod.PATCH, payload);
    }


    default Response trace() {
        return request(HttpMethod.TRACE);
    }

    default ResponseHandler<?> rawTrace() {
        return rawRequest(HttpMethod.TRACE);
    }

    default Response trace(final HttpEntity httpEntity) {
        return request(HttpMethod.TRACE, httpEntity);
    }

    default <T> ResponseHandler<T> trace(final HttpEntity httpEntity, Class<T> responseType) {
        return request(HttpMethod.TRACE, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> trace(final HttpEntity httpEntity, TypeReference<T> responseType) {
        return request(HttpMethod.TRACE, httpEntity, responseType);
    }

    default <T> ResponseHandler<T> trace(final String payload, Class<T> responseType) {
        return request(HttpMethod.TRACE, payload, responseType);
    }

    default <T> ResponseHandler<T> trace(final String payload, TypeReference<T> responseType) {
        return request(HttpMethod.TRACE, payload, responseType);
    }

    default Response trace(final String payload) {
        return request(HttpMethod.TRACE, payload);
    }

}
