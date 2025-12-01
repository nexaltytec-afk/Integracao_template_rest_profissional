package br.com.nexalty.template_rest_profissional.rest;
 
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.MediaType;

import br.com.nexalty.template_rest_profissional.types.DefaultResponse;
import br.com.nexalty.template_rest_profissional.types.Registro;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;
public interface IRestService {
    
    Map<EAuthenticationType, IAuthenticationService> createAuthenticationServicesMap();
    
    HttpHeaders criarHeader(String targetURL, String username, String password, 
                          EAuthenticationType authType, MediaType contentType, 
                          MediaType accept, HttpMethod method);
    
    <T> DefaultResponse get(String url, ParameterizedTypeReference<T> responseType,
                          String username, String password, EAuthenticationType authType,
                          MediaType accept);
    
    <T> DefaultResponse post(String url, Object body, ParameterizedTypeReference<T> responseType,
                           String username, String password, EAuthenticationType authType,
                           MediaType contentType, MediaType accept);
    
    <T> DefaultResponse put(String url, Object body, ParameterizedTypeReference<T> responseType,
                          String username, String password, EAuthenticationType authType,
                          MediaType contentType, MediaType accept);
    
    <T> DefaultResponse patch(String url, Object body, ParameterizedTypeReference<T> responseType,
                            String username, String password, EAuthenticationType authType,
                            MediaType contentType, MediaType accept);
    
    <T> DefaultResponse delete(String url, ParameterizedTypeReference<T> responseType,
                             String username, String password, EAuthenticationType authType,
                             MediaType accept); 
  
    <T> DefaultResponse get(String url, ParameterizedTypeReference<T> responseType);
    
    <T> DefaultResponse post(String url, Object body, ParameterizedTypeReference<T> responseType);
    
    <T> DefaultResponse postFormUrlEncoded(String url, Map<String, String> formData,
                                         ParameterizedTypeReference<T> responseType,
                                         String username, String password,
                                         EAuthenticationType authType);
    
    <T> DefaultResponse postMultipart(String url, Map<String, Object> parts,
                                    ParameterizedTypeReference<T> responseType,
                                    String username, String password,
                                    EAuthenticationType authType);
    
    Registro processEventSnapCapture(InputStream inputStream) throws Exception;
    StringBuilder criarUrlEncoded(Map<String, String> params);
}
