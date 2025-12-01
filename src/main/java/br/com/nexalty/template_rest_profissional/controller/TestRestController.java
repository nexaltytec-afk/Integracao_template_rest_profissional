package br.com.nexalty.template_rest_profissional.controller; 

import br.com.nexalty.template_rest_profissional.rest.EAuthenticationType;
import br.com.nexalty.template_rest_profissional.rest.RestServiceImpl;
import br.com.nexalty.template_rest_profissional.types.DefaultResponse;
import br.com.nexalty.template_rest_profissional.types.Registro;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestRestController {

    private final RestServiceImpl restService;
    
    // APIs gratuitas para teste
    private static final String JSON_PLACEHOLDER_URL = "https://jsonplaceholder.typicode.com";
    private static final String REQRES_URL = "https://reqres.in/api";
    private static final String CAT_FACT_URL = "https://catfact.ninja";
    private static final String AGIFY_URL = "https://api.agify.io";
    private static final String DOG_CEO_URL = "https://dog.ceo/api";
    private static final String OPEN_LIBRARY_URL = "https://openlibrary.org";

    public TestRestController(RestServiceImpl restService) {
        this.restService = restService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "REST Test Controller");
        response.put("timestamp", new Date().toString());
        return ResponseEntity.ok(response);
    }

    // ========== TESTES GET ==========

    @GetMapping("/get/jsonplaceholder")
    public ResponseEntity<DefaultResponse> testGetJsonPlaceholder() {
        log.info("Testando GET com JSONPlaceholder API");
        
        String url = JSON_PLACEHOLDER_URL + "/posts/1";
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/get/reqres")
    public ResponseEntity<DefaultResponse> testGetReqRes() {
        log.info("Testando GET com ReqRes API");
        
        String url = REQRES_URL + "/users/2";
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/get/cat-fact")
    public ResponseEntity<DefaultResponse> testGetCatFact() {
        log.info("Testando GET com Cat Fact API");
        
        String url = CAT_FACT_URL + "/fact";
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/get/with-params")
    public ResponseEntity<DefaultResponse> testGetWithParams(@RequestParam(defaultValue = "John") String name) {
        log.info("Testando GET com parâmetros de query");
        
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        
        String baseUrl = AGIFY_URL;
        String url = restService.construirUrlComParametros(baseUrl, params);
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/get/dog-images")
    public ResponseEntity<DefaultResponse> testGetDogImages() {
        log.info("Testando GET para imagens de cachorros");
        
        String url = DOG_CEO_URL + "/breeds/image/random";
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/get/open-library")
    public ResponseEntity<DefaultResponse> testGetOpenLibrary() {
        log.info("Testando GET com Open Library API");
        
        String url = OPEN_LIBRARY_URL + "/works/OL45883W.json";
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // ========== TESTES POST ==========

    @PostMapping("/post/jsonplaceholder")
    public ResponseEntity<DefaultResponse> testPostJsonPlaceholder(@RequestBody Map<String, Object> requestBody) {
        log.info("Testando POST com JSONPlaceholder API");
        
        String url = JSON_PLACEHOLDER_URL + "/posts";
         
        
        DefaultResponse response = restService.post(
            url,
            requestBody,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/post/reqres")
    public ResponseEntity<DefaultResponse> testPostReqRes() {
        log.info("Testando POST com ReqRes API");
        
        String url = REQRES_URL + "/users";
        
        Map<String, Object> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "leader");
        
        DefaultResponse response = restService.post(
            url,
            body,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/post/form-urlencoded")
    public ResponseEntity<DefaultResponse> testPostFormUrlEncoded() {
        log.info("Testando POST com Form URL Encoded");
        
        String url = JSON_PLACEHOLDER_URL + "/posts";
        
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "Form Title");
        formData.put("body", "Form Body");
        formData.put("userId", "1");
        
        DefaultResponse response = restService.postFormUrlEncoded(
            url,
            formData,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            null,
            null,
            EAuthenticationType.NONE
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

 // ========== TESTES PUT/PATCH/DELETE ==========

    @PutMapping("/put/jsonplaceholder")
    public ResponseEntity<DefaultResponse> testPutJsonPlaceholder(@RequestBody Map<String, Object> requestBody) {
        log.info("Testando PUT com JSONPlaceholder API");
        
        String url = JSON_PLACEHOLDER_URL + "/posts/1";
        
        // Garante que o corpo tenha os campos necessários
        if (!requestBody.containsKey("title")) requestBody.put("title", "Updated Title");
        if (!requestBody.containsKey("body")) requestBody.put("body", "Updated Body");
        if (!requestBody.containsKey("userId")) requestBody.put("userId", 1);
        requestBody.put("id", 1);
        
        DefaultResponse response = restService.put(
            url,
            requestBody,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            null,  // username
            null,  // password
            EAuthenticationType.NONE,  // authType
            MediaType.APPLICATION_JSON,  // contentType
            MediaType.APPLICATION_JSON   // accept
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/patch/jsonplaceholder")
    public ResponseEntity<DefaultResponse> testPatchJsonPlaceholder(@RequestBody Map<String, Object> requestBody) {
        log.info("Testando PATCH com JSONPlaceholder API");
        
        String url = JSON_PLACEHOLDER_URL + "/posts/1";
        
        DefaultResponse response = restService.patch(
            url,
            requestBody,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            null,  // username
            null,  // password
            EAuthenticationType.NONE,  // authType
            MediaType.APPLICATION_JSON,  // contentType
            MediaType.APPLICATION_JSON   // accept
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/jsonplaceholder")
    public ResponseEntity<DefaultResponse> testDeleteJsonPlaceholder() {
        log.info("Testando DELETE com JSONPlaceholder API");
        
        String url = JSON_PLACEHOLDER_URL + "/posts/1";
        
        DefaultResponse response = restService.delete(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            null,  // username
            null,  // password
            EAuthenticationType.NONE,  // authType
            MediaType.APPLICATION_JSON   // accept
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // ========== TESTES MULTIPART ==========

    @PostMapping("/post/multipart")
    public ResponseEntity<DefaultResponse> testPostMultipart(
            @RequestParam(value = "file", required = false) MultipartFile file) {
        log.info("Testando POST Multipart");
        
        String url = "https://httpbin.org/post"; // API de teste que aceita multipart
        
        Map<String, Object> parts = new HashMap<>();
        parts.put("field1", "value1");
        parts.put("field2", "value2");
        
        if (file != null && !file.isEmpty()) {
            try {
                parts.put("file", file.getBytes());
            } catch (Exception e) {
                log.error("Erro ao processar arquivo", e);
            }
        } else {
            // Usa dados de exemplo se nenhum arquivo for enviado
            parts.put("textFile", "Conteúdo do arquivo de texto\nde exemplo".getBytes());
        }
        
        DefaultResponse response = restService.postMultipart(
            url,
            parts,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            null,
            null,
            EAuthenticationType.NONE
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // ========== TESTES DE AUTENTICAÇÃO ==========

    @GetMapping("/get/with-auth")
    public ResponseEntity<DefaultResponse> testGetWithAuthentication() {
        log.info("Testando GET com autenticação básica");
        
        // Usa uma API que suporta autenticação básica
        String url = "https://httpbin.org/basic-auth/user/passwd";
        String username = "user";
        String password = "passwd";
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            username,
            password,
            EAuthenticationType.BASIC,
            MediaType.APPLICATION_JSON
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/get/bearer-auth")
    public ResponseEntity<DefaultResponse> testGetWithBearerToken() {
        log.info("Testando GET com token Bearer");
        
        // API de exemplo que requer token (substitua por uma API real se necessário)
        String url = "https://api.github.com/user";
        String token = "seu-token-github-aqui"; // Substitua por um token real para testar
        
        DefaultResponse response = restService.get(
            url,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            token,
            null, // password não é usado para Bearer
            EAuthenticationType.BEARER,
            MediaType.APPLICATION_JSON
        );
        
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // ========== TESTE DE STREAM ==========

    @PostMapping("/process-stream")
    public ResponseEntity<DefaultResponse> testProcessStream() {
        log.info("Testando processamento de stream");
        
        try {
            // Simula dados de stream com boundary
            String boundary = "--myboundary\r\n";
            String headers = "Content-Type: image/jpeg\r\n\r\n";
            String imageData = "SIMULATED_JPEG_DATA_BYTES";
            String streamData = boundary + headers + imageData + boundary;
            
            InputStream inputStream = new ByteArrayInputStream(streamData.getBytes());
            
            Registro resultado = restService.processEventSnapCapture(inputStream);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Stream processado com sucesso");
            response.put("hasImage", resultado.containsKey("imagem"));
            response.put("imageSize", resultado.containsKey("imagem") ? 
                ((String) resultado.get("imagem")).length() : 0);
            
            return new ResponseEntity(response, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Erro ao processar stream", e);
            return new ResponseEntity(
                Map.of(
                    "success", false,
                    "error", e.getMessage()
                ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== TESTE COMPLETO DE TODOS OS MÉTODOS ==========

    @GetMapping("/run-all-tests")
    public ResponseEntity<DefaultResponse> runAllTests() {
        log.info("Executando todos os testes");
        
        Map<String, Object> results = new LinkedHashMap<>();
        results.put("timestamp", new Date().toString());
        
        // Teste 1: GET simples
        try {
            DefaultResponse response = restService.get(
                JSON_PLACEHOLDER_URL + "/posts/1",
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            results.put("test_get_simple", response.isSuccess() ? "PASS" : "FAIL");
        } catch (Exception e) {
            results.put("test_get_simple", "ERROR: " + e.getMessage());
        }
        
        // Teste 2: GET com parâmetros
        try {
            Map<String, String> params = new HashMap<>();
            params.put("name", "Michael");
            String url = restService.construirUrlComParametros(AGIFY_URL, params);
            
            DefaultResponse response = restService.get(
                url,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            results.put("test_get_with_params", response.isSuccess() ? "PASS" : "FAIL");
        } catch (Exception e) {
            results.put("test_get_with_params", "ERROR: " + e.getMessage());
        }
        
        // Teste 3: POST
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("title", "Test Post");
            body.put("body", "Test Body");
            body.put("userId", 1);
            
            DefaultResponse response = restService.post(
                JSON_PLACEHOLDER_URL + "/posts",
                body,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            results.put("test_post", response.isSuccess() ? "PASS" : "FAIL");
        } catch (Exception e) {
            results.put("test_post", "ERROR: " + e.getMessage());
        }
        
        // Teste 4: Form URL Encoded
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("title", "Form Test");
            formData.put("body", "Form Body");
            formData.put("userId", "1");
            
            DefaultResponse response = restService.postFormUrlEncoded(
                JSON_PLACEHOLDER_URL + "/posts",
                formData,
                new ParameterizedTypeReference<Map<String, Object>>() {},
                null, null, EAuthenticationType.NONE
            );
            results.put("test_form_urlencoded", response.isSuccess() ? "PASS" : "FAIL");
        } catch (Exception e) {
            results.put("test_form_urlencoded", "ERROR: " + e.getMessage());
        }
        
     // Teste 5: DELETE
        try {
            DefaultResponse response = restService.delete(
                JSON_PLACEHOLDER_URL + "/posts/1",
                new ParameterizedTypeReference<Map<String, Object>>() {},
                null, null, EAuthenticationType.NONE, MediaType.APPLICATION_JSON
            );
            results.put("test_delete", response.isSuccess() ? "PASS" : "FAIL");
        } catch (Exception e) {
            results.put("test_delete", "ERROR: " + e.getMessage());
        }
        
        // Contabiliza resultados
        long passed = results.values().stream()
            .filter(v -> v.equals("PASS"))
            .count();
        long total = results.size() - 1; // Exclui timestamp
        
        results.put("summary", String.format("%d/%d testes passaram", passed, total));
        results.put("allTestsPassed", passed == total);
        
        return new ResponseEntity( new DefaultResponse().success(results), HttpStatus.OK);
    }

    // ========== UTILITÁRIOS ==========
 
    @GetMapping("/available-apis")
    public ResponseEntity<Map<String, Object>> listAvailableApis() {
        Map<String, Object> apis = new LinkedHashMap<>();
        
        apis.put("JSONPlaceholder", Map.of(
            "url", JSON_PLACEHOLDER_URL,
            "description", "API REST fake para teste e prototipagem",
            "endpoints", Arrays.asList("/posts", "/comments", "/albums", "/photos", "/todos", "/users")
        ));
        
        apis.put("ReqRes", Map.of(
            "url", REQRES_URL,
            "description", "API para teste de requisições HTTP",
            "endpoints", Arrays.asList("/users", "/register", "/login")
        ));
        
        apis.put("Cat Facts", Map.of(
            "url", CAT_FACT_URL,
            "description", "API de fatos sobre gatos",
            "endpoints", Arrays.asList("/fact", "/facts", "/breeds")
        ));
        
        apis.put("Agify", Map.of(
            "url", AGIFY_URL,
            "description", "API para prever idade pelo nome",
            "endpoints", Arrays.asList("/?name={nome}")
        ));
        
        apis.put("Dog CEO", Map.of(
            "url", DOG_CEO_URL,
            "description", "API de imagens de cachorros",
            "endpoints", Arrays.asList("/breeds/list/all", "/breeds/image/random")
        ));
        
        apis.put("Open Library", Map.of(
            "url", OPEN_LIBRARY_URL,
            "description", "API de livros e autores",
            "endpoints", Arrays.asList("/works/{id}", "/authors/{id}", "/search.json")
        ));
        
        apis.put("HTTPBin", Map.of(
            "url", "https://httpbin.org",
            "description", "API para testar requisições HTTP",
            "endpoints", Arrays.asList("/get", "/post", "/put", "/delete", "/basic-auth", "/bearer")
        ));
        
        return ResponseEntity.ok(apis);
    }

    @GetMapping("/test-url-builder")
    public ResponseEntity<Map<String, String>> testUrlBuilder(
            @RequestParam(defaultValue = "https://api.example.com/data") String baseUrl,
            @RequestParam(required = false) Map<String, String> params) {
        
        String builtUrl = restService.construirUrlComParametros(baseUrl, params);
        
        Map<String, String> response = new HashMap<>();
        response.put("baseUrl", baseUrl);
        response.put("params", params != null ? params.toString() : "{}");
        response.put("builtUrl", builtUrl);
        
        return ResponseEntity.ok(response);
    }
}