package br.com.nexalty.template_rest_profissional.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.nexalty.template_rest_profissional.types.DefaultResponse;
import br.com.nexalty.template_rest_profissional.types.Registro;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.List; 
import org.springframework.stereotype.Service; 
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class RestServiceImpl implements IRestService {
     
    private ResponseService responseService;
 
    private RestTemplate restTemplate;

    private final Map<EAuthenticationType, IAuthenticationService> authenticationServices;

    public RestServiceImpl(ResponseService responseService, RestTemplate restTemplate) {
    	this.responseService = responseService;
    	this.restTemplate = restTemplate;
        this.authenticationServices = createAuthenticationServicesMap();
    }

    @Override
    public Map<EAuthenticationType, IAuthenticationService> createAuthenticationServicesMap() {
        Map<EAuthenticationType, IAuthenticationService> map = new EnumMap<>(EAuthenticationType.class);
        map.put(EAuthenticationType.BASIC, new AuthenticationServiceBasic());
        map.put(EAuthenticationType.DIGEST, new AuthenticationServiceDigest());
        map.put(EAuthenticationType.NONE, new AuthenticationServiceNoOp());
        map.put(EAuthenticationType.BEARER, new AuthenticationServiceBearer());
        return Collections.unmodifiableMap(map);
    }

    @Override
    public HttpHeaders criarHeader(
            String targetURL,
            String username,
            String password,
            EAuthenticationType authType,
            MediaType contentType,
            MediaType accept,
            HttpMethod method
    ) {
    	 
        HttpHeaders headers = new HttpHeaders(); 
	        headers.set("Content-Type", contentType != null ? contentType.toString() : MediaType.APPLICATION_FORM_URLENCODED_VALUE);
	        headers.set("Accept", accept != null ? accept.toString() : MediaType.APPLICATION_JSON_VALUE);
 
        if (!EAuthenticationType.NONE.equals(authType)) {
        	if (EAuthenticationType.BEARER.equals(authType)) {
        		IAuthenticationService authService = authenticationServices.get(authType);
        		if (authService != null) {
                    String authHeader = authService.authenticate(username, password, targetURL, method); 
                    headers.set("Authorization", authHeader);
                }
        	}
            if (username != null && password != null) {
                IAuthenticationService authService = authenticationServices.get(authType);
                if (authService != null) {
                    String authHeader = authService.authenticate(username, password, targetURL, method); 
                    headers.set("Authorization", authHeader);
                }
            }
        }
 
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");

        return headers;
    }
    
    public StringBuilder criarUrlEncoded(Map<String, String> params) {
        StringBuilder postData = new StringBuilder();
        if (params != null) {
            params.forEach((key, value) -> {
                if (postData.length() != 0) postData.append('&');
                postData.append(key)
                        .append('=')
                        .append(java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8));
            });
        }
        return postData;
    }
    
    public String construirUrlComParametros(String baseUrl, Map<String, String> parametros) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        
        if (parametros != null && !parametros.isEmpty()) {
            // Verificar se a URL já tem parâmetros
            boolean jaTemParametros = baseUrl.contains("?");
            
            if (!jaTemParametros) {
                urlBuilder.append('?');
            } else if (!baseUrl.endsWith("?") && !baseUrl.endsWith("&")) {
                urlBuilder.append('&');
            }
            
            // Adicionar parâmetros
            boolean primeiroParametro = true;
            for (Map.Entry<String, String> entry : parametros.entrySet()) {
                if (!primeiroParametro) {
                    urlBuilder.append('&');
                }
                
                urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                          .append('=')
                          .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                
                primeiroParametro = false;
            }
        }
        
        return urlBuilder.toString();
    }
 
    private <T> DefaultResponse executarRequest(
            String url,
            HttpMethod method,
            Object body,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType,
            MediaType contentType,
            MediaType accept
    ) {
        Class<?> rawType = HttpUtils.getResponseType(responseType);
         
        int maxAttempts = 3;
        long initialDelayMs = 1000; // 1 segundo
        long maxDelayMs = 5000; // 5 segundos
        
        int attempt = 0;
        
        while (attempt < maxAttempts) {
            try {
                attempt++;
                
                HttpHeaders headers = criarHeader(url, username, password, authType, contentType, accept, method);
                HttpEntity<?> entity = prepararEntity(body, headers, contentType);
                
                ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType);

                log.info("HTTP --- {} ENVIADO COM SUCESSO (Tentativa {}/{}): {}", 
                        method, attempt, maxAttempts, response.getBody(), this.getClass());
                return responseService.handleSuccess(response, rawType);

            } catch (HttpClientErrorException e) { 
                if (e.getStatusCode().is4xxClientError()) {
                    log.error("HTTP --- {} ENVIADO COM ERRO CLIENTE ({}) - NÃO SERÁ RETENTADO: {}", 
                            method, e.getStatusCode().value(), e.getResponseBodyAsString(), this.getClass());
                    return responseService.handleFailure(e, rawType);
                }
                 
                log.warn("HTTP --- {} FALHOU (Tentativa {}/{} - {}): {}", 
                        method, attempt, maxAttempts, e.getStatusCode().value(), 
                        e.getResponseBodyAsString(), this.getClass());
                
            } catch (Exception e) {
                log.warn("HTTP --- {} FALHOU (Tentativa {}/{} - Exception): {}", 
                        method, attempt, maxAttempts, e.getMessage(), this.getClass());
            }
             
            if (attempt < maxAttempts) {
                try {
                    long delay = calcularDelayExponencial(attempt, initialDelayMs, maxDelayMs);
                    log.info("Aguardando {}ms antes da próxima tentativa...", delay);
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrompida durante o delay de retry", ie);
                    return responseService.handleFailure(ie, rawType);
                }
            }
        }
         
        log.error("HTTP --- {} FALHOU APÓS {} TENTATIVAS", method, maxAttempts, this.getClass());
        return responseService.handleFailure(new RuntimeException("Falha após " + maxAttempts + " tentativas"), rawType);
    }

    
    private long calcularDelayExponencial(int attempt, long initialDelay, long maxDelay) {
        long delay = (long) (initialDelay * Math.pow(2, attempt - 1));
        return Math.min(delay, maxDelay);
    }
 
    @Override
    public <T> DefaultResponse get(
            String url,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType,
            MediaType accept
    ) { 
        return executarRequest(url, HttpMethod.GET, null, responseType, 
                             username, password, authType, null, accept);
    }
    
    @Override
    public <T> DefaultResponse get(
            String url,
            ParameterizedTypeReference<T> responseType
    ) {
        return get(url, responseType, null, null, EAuthenticationType.NONE, MediaType.APPLICATION_JSON);
    }

    @Override
    public <T> DefaultResponse post(
            String url,
            Object body,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType,
            MediaType contentType,
            MediaType accept
    ) {
        return executarRequest(url, HttpMethod.POST, body, responseType, 
                             username, password, authType, contentType, accept);
    }
    
    @Override
    public <T> DefaultResponse post(
            String url,
            Object body,
            ParameterizedTypeReference<T> responseType
    ) {
        return post(url, body, responseType, null, null, EAuthenticationType.NONE, 
                   MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
    }

    @Override
    public <T> DefaultResponse put(
            String url,
            Object body,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType,
            MediaType contentType,
            MediaType accept
    ) {
        return executarRequest(url, HttpMethod.PUT, body, responseType, 
                             username, password, authType, contentType, accept);
    }

    @Override
    public <T> DefaultResponse patch(
            String url,
            Object body,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType,
            MediaType contentType,
            MediaType accept
    ) {
        return executarRequest(url, HttpMethod.PATCH, body, responseType, 
                             username, password, authType, contentType, accept);
    }

    @Override
    public <T> DefaultResponse delete(
            String url,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType,
            MediaType accept
    ) {
        return executarRequest(url, HttpMethod.DELETE, null, responseType, 
                             username, password, authType, null, accept);
    }

    @Override
    public <T> DefaultResponse postFormUrlEncoded(
            String url,
            Map<String, String> formData,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType
    ) {
        String encodedData = criarUrlEncoded(formData).toString();
        return executarRequest(url, HttpMethod.POST, encodedData, responseType, 
                             username, password, authType, 
                             MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON);
    }

    @Override
    public <T> DefaultResponse postMultipart(
            String url,
            Map<String, Object> parts,
            ParameterizedTypeReference<T> responseType,
            String username,
            String password,
            EAuthenticationType authType
    ) {
        MultiValueMap<String, Object> multipartBody = criarPartesMultipart(parts);
        return executarRequest(url, HttpMethod.POST, multipartBody, responseType, 
                             username, password, authType, 
                             MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON);
    }

    private HttpEntity<?> prepararEntity(Object body, HttpHeaders headers, MediaType contentType) {
        if (body == null) {
            return new HttpEntity<>(headers);
        }
     
        if (contentType != null && (contentType.includes(MediaType.MULTIPART_FORM_DATA) || 
                                    contentType.includes(MediaType.MULTIPART_MIXED))) {
            return prepararMultipartEntity(body, headers);
        }
     
        if (body instanceof InputStream) {
            return prepararStreamEntity((InputStream) body, headers, contentType);
        }
     
        return new HttpEntity<>(body, headers);
    }
    
    private HttpEntity<MultiValueMap<String, Object>> prepararMultipartEntity(Object body, HttpHeaders headers) {
        if (body instanceof MultiValueMap) {
            
            MultiValueMap<String, Object> multipartBody = (MultiValueMap<String, Object>) body;
            return new HttpEntity<>(multipartBody, headers);
        } else if (body instanceof Map) {
             
            MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
            Map<String, Object> map = (Map<String, Object>) body;
            
            map.forEach((key, value) -> {
                if (value instanceof List) {
                    multipartBody.addAll(key, (List<?>) value);
                } else {
                    multipartBody.add(key, value);
                }
            });
            
            return new HttpEntity<>(multipartBody, headers);
        } else {
            throw new IllegalArgumentException("Body para multipart deve ser MultiValueMap ou Map");
        }
    }
    
    private HttpEntity<InputStreamResource> prepararStreamEntity(InputStream inputStream, HttpHeaders headers, MediaType contentType) {
        InputStreamResource resource = new InputStreamResource(inputStream);
        if (contentType != null) {
            headers.setContentType(contentType);
        }
         
        return new HttpEntity<>(resource, headers);
    }
     
    public static MultiValueMap<String, Object> criarPartesMultipart(Map<String, Object> partes) {
        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        
        partes.forEach((nome, valor) -> {
            if (valor instanceof File) {
                File file = (File) valor;
                multipartBody.add(nome, new FileSystemResource(file));
            } else if (valor instanceof byte[]) {
                byte[] bytes = (byte[]) valor;
                multipartBody.add(nome, new ByteArrayResource(bytes) {
                    @Override
                    public String getFilename() {
                        return nome;
                    }
                });
            } else if (valor instanceof InputStream) {
                InputStream inputStream = (InputStream) valor;
                multipartBody.add(nome, new InputStreamResource(inputStream) {
                    @Override
                    public String getFilename() {
                        return nome;
                    }
                });
            } else {
                multipartBody.add(nome, valor);
            }
        });
        
        return multipartBody;
    }

    @Override
    public Registro processEventSnapCapture(InputStream inputStream) throws Exception {
       
        StringBuilder accumulatedData = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Registro retorno = new Registro();
        log.info("Starting to process events...");

        while ((line = reader.readLine()) != null) {
            accumulatedData.append(line).append("\r\n");

            String boundary = "--myboundary\r\n";
            int boundaryIndex;
            while ((boundaryIndex = accumulatedData.indexOf(boundary)) != -1) {
                String part = accumulatedData.substring(0, boundaryIndex);
                accumulatedData.delete(0, boundaryIndex + boundary.length());
                log.info("Processed part: " + part);

                if (part.contains("Content-Type")) {
                    int headersEndIndex = part.indexOf("\r\n\r\n");
                    if (headersEndIndex != -1) {
                        String contentType = part.split("\r\n")[0].split(": ")[1];
                        log.info("Found content type: " + contentType);

                        if (contentType.equals("image/jpeg")) {
                            String imageData = part.substring(headersEndIndex + 4);
                            byte[] imageBytes = imageData.getBytes(StandardCharsets.ISO_8859_1);
                            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                            log.info("JPEG image processed and encoded to Base64.");
                            retorno.put("imagem", base64Image);
                            return retorno;
                        } else {
                            log.info("Unsupported content type: " + contentType);
                            throw new Exception("Unsupported content type: " + contentType);
                        }
                    }
                }
            }
        }

        log.info("No image captured.");
        throw new Exception("No image captured");
    }
}