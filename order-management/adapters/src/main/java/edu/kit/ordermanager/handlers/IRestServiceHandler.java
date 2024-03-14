package edu.kit.ordermanager.handlers;

import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;

import java.net.URI;

public interface IRestServiceHandler {

    @Nullable
    public <T>  T sendGetRequest(String url, Class<T> responseType, @Nullable Object uriVariables);

    @Nullable
    <T> T sendPostRequest(String url, @Nullable Object request, Class<T> responseType) throws RestClientException;

}
