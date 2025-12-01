\# Commons Rest Service



Uma biblioteca robusta e completa para comunicação HTTP REST em aplicações Spring Boot, com suporte a múltiplos tipos de autenticação, formatos de dados e estratégias de retry.



\## 🌟 Características Principais



\- ✅ \*\*Múltiplos tipos de autenticação\*\*: Basic, Digest, Bearer, None

\- ✅ \*\*Formatos suportados\*\*: JSON, Form URL Encoded, Multipart/form-data, Streams

\- ✅ \*\*Sistema de retry inteligente\*\* com backoff exponencial

\- ✅ \*\*Tratamento de respostas genéricas\*\* via `DefaultResponse<T>`

\- ✅ \*\*Upload/Download de arquivos\*\* com suporte a File, byte\[], InputStream

\- ✅ \*\*Logs detalhados\*\* com SLF4J

\- ✅ \*\*Configuração flexível\*\* através de builders e enums

\- ✅ \*\*Thread-safe\*\* e pronto para produção



\## 📦 Instalação



\### Dependências Maven



```xml

<dependencies>

&nbsp;   <dependency>

&nbsp;       <groupId>org.springframework.boot</groupId>

&nbsp;       <artifactId>spring-boot-starter-web</artifactId>

&nbsp;   </dependency>

&nbsp;   <dependency>

&nbsp;       <groupId>org.projectlombok</groupId>

&nbsp;       <artifactId>lombok</artifactId>

&nbsp;       <optional>true</optional>

&nbsp;   </dependency>

&nbsp;   <dependency>

&nbsp;       <groupId>br.com.nexalty.commons</groupId>

&nbsp;       <artifactId>commons-core</artifactId>

&nbsp;       <version>1.0.0</version>

&nbsp;   </dependency>

</dependencies>

```



\### Configuração Spring



```java

@Configuration

public class RestServiceConfig {

&nbsp;   

&nbsp;   @Bean

&nbsp;   public RestTemplate restTemplate() {

&nbsp;       RestTemplate restTemplate = new RestTemplate();

&nbsp;       

&nbsp;       // Configurar timeouts

&nbsp;       SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

&nbsp;       factory.setConnectTimeout(30000);

&nbsp;       factory.setReadTimeout(30000);

&nbsp;       restTemplate.setRequestFactory(factory);

&nbsp;       

&nbsp;       return restTemplate;

&nbsp;   }

&nbsp;   

&nbsp;   @Bean

&nbsp;   public ResponseService responseService() {

&nbsp;       return new ResponseService();

&nbsp;   }

&nbsp;   

&nbsp;   @Bean

&nbsp;   public IRestService restService(ResponseService responseService, RestTemplate restTemplate) {

&nbsp;       return new RestServiceImpl(responseService, restTemplate);

&nbsp;   }

}

```



\## 🚀 Quick Start



\### Injeção do Serviço



```java

@Service

public class MeuService {

&nbsp;   

&nbsp;   @Autowired

&nbsp;   private IRestService restService;

&nbsp;   

&nbsp;   // ... métodos que usam o restService

}

```



\### Exemplo Básico - GET



```java

public Usuario buscarUsuario(Long id) {

&nbsp;   String url = String.format("https://api.exemplo.com/usuarios/%d", id);

&nbsp;   

&nbsp;   DefaultResponse<Usuario> resposta = restService.get(

&nbsp;       url,

&nbsp;       new ParameterizedTypeReference<Usuario>() {}

&nbsp;   );

&nbsp;   

&nbsp;   if (resposta.isSuccess()) {

&nbsp;       return resposta.getData();

&nbsp;   } else {

&nbsp;       throw new RuntimeException("Erro ao buscar usuário: " + resposta.getError());

&nbsp;   }

}

```



\## 🔐 Autenticação



\### Tipos Suportados



```java

public enum EAuthenticationType {

&nbsp;   BASIC,      // Basic Authentication

&nbsp;   DIGEST,     // Digest Authentication

&nbsp;   BEARER,     // Bearer Token (JWT, OAuth2)

&nbsp;   NONE        // Sem autenticação

}

```



\### Exemplos de Uso



\#### 1. Basic Authentication

```java

DefaultResponse<List<Usuario>> resposta = restService.get(

&nbsp;   "https://api.exemplo.com/usuarios",

&nbsp;   new ParameterizedTypeReference<List<Usuario>>() {},

&nbsp;   "usuario",

&nbsp;   "senha123",

&nbsp;   EAuthenticationType.BASIC,

&nbsp;   MediaType.APPLICATION\_JSON

);

```



\#### 2. Bearer Token

```java

DefaultResponse<Perfil> resposta = restService.get(

&nbsp;   "https://api.exemplo.com/perfil",

&nbsp;   new ParameterizedTypeReference<Perfil>() {},

&nbsp;   null, // username não necessário para Bearer

&nbsp;   "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", // token

&nbsp;   EAuthenticationType.BEARER,

&nbsp;   MediaType.APPLICATION\_JSON

);

```



\## 📤 Métodos HTTP



\### POST com JSON

```java

Usuario novoUsuario = new Usuario("João", "joao@exemplo.com");



DefaultResponse<Usuario> resposta = restService.post(

&nbsp;   "https://api.exemplo.com/usuarios",

&nbsp;   novoUsuario,

&nbsp;   new ParameterizedTypeReference<Usuario>() {},

&nbsp;   "admin",

&nbsp;   "senhaAdmin",

&nbsp;   EAuthenticationType.BASIC,

&nbsp;   MediaType.APPLICATION\_JSON,

&nbsp;   MediaType.APPLICATION\_JSON

);

```



\### PUT com Atualização

```java

Usuario usuarioAtualizado = new Usuario("João Silva", "joao.silva@exemplo.com");



DefaultResponse<Usuario> resposta = restService.put(

&nbsp;   "https://api.exemplo.com/usuarios/123",

&nbsp;   usuarioAtualizado,

&nbsp;   new ParameterizedTypeReference<Usuario>() {}

);

```



\### DELETE

```java

DefaultResponse<Void> resposta = restService.delete(

&nbsp;   "https://api.exemplo.com/usuarios/123",

&nbsp;   new ParameterizedTypeReference<Void>() {},

&nbsp;   "admin",

&nbsp;   "senhaAdmin",

&nbsp;   EAuthenticationType.BASIC,

&nbsp;   MediaType.APPLICATION\_JSON

);

```



\### PATCH (Atualização Parcial)

```java

Map<String, Object> atualizacoes = Map.of("nome", "Novo Nome");



DefaultResponse<Usuario> resposta = restService.patch(

&nbsp;   "https://api.exemplo.com/usuarios/123",

&nbsp;   atualizacoes,

&nbsp;   new ParameterizedTypeReference<Usuario>() {}

);

```



\## 📎 Envio de Arquivos



\### Form URL Encoded

```java

Map<String, String> formData = new HashMap<>();

formData.put("username", "usuario");

formData.put("password", "senha123");

formData.put("grant\_type", "password");



DefaultResponse<TokenResponse> resposta = restService.postFormUrlEncoded(

&nbsp;   "https://api.exemplo.com/oauth/token",

&nbsp;   formData,

&nbsp;   new ParameterizedTypeReference<TokenResponse>() {},

&nbsp;   null, null, EAuthenticationType.NONE

);

```



\### Multipart/Form-Data (Upload)

```java

Map<String, Object> partes = new HashMap<>();



// Arquivo do sistema de arquivos

partes.put("arquivo", new File("/caminho/arquivo.pdf"));



// Bytes em memória

partes.put("imagem", imagemBytes);



// InputStream

partes.put("documento", inputStream);



// Campos de texto

partes.put("descricao", "Documento importante");

partes.put("categoria", "PDF");



DefaultResponse<UploadResponse> resposta = restService.postMultipart(

&nbsp;   "https://api.exemplo.com/upload",

&nbsp;   partes,

&nbsp;   new ParameterizedTypeReference<UploadResponse>() {},

&nbsp;   "usuario",

&nbsp;   "tokenBearer",

&nbsp;   EAuthenticationType.BEARER

);

```



\### Tipos de Arquivo Suportados

\- `File` - Arquivo do sistema de arquivos

\- `byte\[]` - Bytes em memória

\- `InputStream` - Stream de dados

\- `String` - Texto simples



\## 🔄 Sistema de Retry



\### Configuração Automática

O serviço implementa retry com backoff exponencial:



```java

int maxAttempts = 3;           // Máximo de tentativas

long initialDelayMs = 1000;    // 1 segundo inicial

long maxDelayMs = 5000;        // 5 segundos máximo

```



\### Comportamento

\- \*\*3 tentativas\*\* por padrão

\- \*\*Backoff exponencial\*\*: 1s, 2s, 4s (limitado a 5s)

\- \*\*Erros 4xx (Client Error)\*\*: Não são retentados

\- \*\*Erros 5xx e Network Errors\*\*: São retentados



\### Logs de Retry

```

WARN - HTTP --- GET FALHOU (Tentativa 1/3 - 503): Service Unavailable

INFO - Aguardando 1000ms antes da próxima tentativa...

INFO - HTTP --- GET ENVIADO COM SUCESSO (Tentativa 2/3): {response}

```



\## 🎯 Tratamento de Respostas



\### Estrutura `DefaultResponse<T>`



```java

public class DefaultResponse<T> {

&nbsp;   private boolean success;

&nbsp;   private T data;

&nbsp;   private String error;

&nbsp;   private Integer statusCode;

&nbsp;   private Map<String, Object> metadata;

&nbsp;   

&nbsp;   // Getters, setters e métodos utilitários

&nbsp;   public boolean isSuccess() { return success; }

&nbsp;   public T getData() { return data; }

&nbsp;   public String getError() { return error; }

}

```



\### Exemplos de Uso



\#### Sucesso

```java

DefaultResponse<Usuario> resposta = restService.get(...);



if (resposta.isSuccess()) {

&nbsp;   Usuario usuario = resposta.getData();

&nbsp;   System.out.println("Usuário: " + usuario.getNome());

} else {

&nbsp;   System.err.println("Erro: " + resposta.getError());

}

```



\#### Com Status Code

```java

if (resposta.getStatusCode() == 404) {

&nbsp;   throw new RecursoNaoEncontradoException("Usuário não encontrado");

}

```



\## 🛡️ Tratamento de Erros



\### Tipos de Erros Tratados



```java

try {

&nbsp;   DefaultResponse<?> resposta = restService.get(...);

} catch (Exception e) {

&nbsp;   // Erros são tratados internamente e retornados no DefaultResponse

}

```



\### Personalização de Tratamento

```java

@Configuration

public class CustomResponseService extends ResponseService {

&nbsp;   

&nbsp;   @Override

&nbsp;   public <T> DefaultResponse<T> handleSuccess(ResponseEntity<T> response, Class<?> rawType) {

&nbsp;       DefaultResponse<T> defaultResponse = super.handleSuccess(response, rawType);

&nbsp;       

&nbsp;       // Adicionar metadados personalizados

&nbsp;       defaultResponse.getMetadata().put("responseTime", System.currentTimeMillis());

&nbsp;       defaultResponse.getMetadata().put("headers", response.getHeaders());

&nbsp;       

&nbsp;       return defaultResponse;

&nbsp;   }

&nbsp;   

&nbsp;   @Override

&nbsp;   public <T> DefaultResponse<T> handleFailure(Exception e, Class<?> rawType) {

&nbsp;       // Log personalizado

&nbsp;       log.error("Falha na requisição: {}", e.getMessage(), e);

&nbsp;       

&nbsp;       DefaultResponse<T> response = super.handleFailure(e, rawType);

&nbsp;       response.getMetadata().put("exceptionClass", e.getClass().getName());

&nbsp;       

&nbsp;       return response;

&nbsp;   }

}

```



\## 🌐 Construção de URLs



\### Métodos Utilitários



```java

// Adicionar parâmetros de query

String urlComParams = restService.construirUrlComParametros(

&nbsp;   "https://api.exemplo.com/usuarios",

&nbsp;   Map.of("page", "1", "limit", "20", "sort", "nome")

);

// Resultado: https://api.exemplo.com/usuarios?page=1\&limit=20\&sort=nome



// Criar dados codificados para POST

StringBuilder formData = restService.criarUrlEncoded(

&nbsp;   Map.of("username", "user", "password", "pass123")

);

// Resultado: username=user\&password=pass123

```



\## 🖼️ Processamento de Eventos (Stream)



\### Exemplo: Captura de Imagens

```java

@Service

public class CameraService {

&nbsp;   

&nbsp;   @Autowired

&nbsp;   private IRestService restService;

&nbsp;   

&nbsp;   public Registro capturarImagem(String cameraUrl) throws Exception {

&nbsp;       // Simulação - em produção, isso viria de uma stream real

&nbsp;       InputStream videoStream = obterStreamDaCamera(cameraUrl);

&nbsp;       

&nbsp;       // Processa frames do stream procurando por imagens JPEG

&nbsp;       Registro resultado = restService.processEventSnapCapture(videoStream);

&nbsp;       

&nbsp;       if (resultado.containsKey("imagem")) {

&nbsp;           String base64Image = resultado.getString("imagem");

&nbsp;           // Converter base64 para bytes ou salvar

&nbsp;           byte\[] imagemBytes = Base64.getDecoder().decode(base64Image);

&nbsp;           return resultado;

&nbsp;       }

&nbsp;       

&nbsp;       throw new Exception("Nenhuma imagem capturada");

&nbsp;   }

}

```



\## 📊 Logs e Monitoramento



\### Logs Automáticos

```java

// Sucesso

log.info("HTTP --- {} ENVIADO COM SUCESSO (Tentativa {}/{}): {}", 

&nbsp;       method, attempt, maxAttempts, response.getBody());



// Erro Cliente (4xx)

log.error("HTTP --- {} ENVIADO COM ERRO CLIENTE ({}) - NÃO SERÁ RETENTADO: {}", 

&nbsp;       method, statusCode, responseBody);



// Erro com Retry

log.warn("HTTP --- {} FALHOU (Tentativa {}/{} - {}): {}", 

&nbsp;       method, attempt, maxAttempts, statusCode, message);

```



\### Métricas Personalizadas

```java

@Component

public class RestServiceMetrics {

&nbsp;   

&nbsp;   private final MeterRegistry meterRegistry;

&nbsp;   

&nbsp;   @EventListener

&nbsp;   public void onRestRequest(RestRequestEvent event) {

&nbsp;       // Registrar métricas

&nbsp;       meterRegistry.counter("http.requests.total", 

&nbsp;           "method", event.getMethod(),

&nbsp;           "status", event.getStatus())

&nbsp;           .increment();

&nbsp;       

&nbsp;       meterRegistry.timer("http.request.duration",

&nbsp;           "method", event.getMethod())

&nbsp;           .record(event.getDuration());

&nbsp;   }

}

```



\## ⚙️ Configuração Avançada



\### Customização do RestTemplate

```java

@Bean

public RestTemplate customRestTemplate() {

&nbsp;   RestTemplate restTemplate = new RestTemplate();

&nbsp;   

&nbsp;   // Configurar timeouts

&nbsp;   SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

&nbsp;   factory.setConnectTimeout(30000);

&nbsp;   factory.setReadTimeout(60000);

&nbsp;   restTemplate.setRequestFactory(factory);

&nbsp;   

&nbsp;   // Adicionar interceptors

&nbsp;   restTemplate.getInterceptors().add(new CustomInterceptor());

&nbsp;   

&nbsp;   // Configurar message converters

&nbsp;   restTemplate.getMessageConverters().add(0, new CustomMessageConverter());

&nbsp;   

&nbsp;   return restTemplate;

}

```



\### Implementação de Autenticação Personalizada

```java

@Component

public class CustomAuthenticationService implements IAuthenticationService {

&nbsp;   

&nbsp;   @Override

&nbsp;   public String authenticate(String username, String password, 

&nbsp;                             String targetURL, HttpMethod method) {

&nbsp;       // Lógica personalizada de autenticação

&nbsp;       String token = obterTokenPersonalizado(username, password);

&nbsp;       return "Custom " + token;

&nbsp;   }

&nbsp;   

&nbsp;   @Override

&nbsp;   public EAuthenticationType getAuthType() {

&nbsp;       return EAuthenticationType.CUSTOM;

&nbsp;   }

}

```



\## 🧪 Testes



\### Testes Unitários

```java

@SpringBootTest

class RestServiceTest {

&nbsp;   

&nbsp;   @MockBean

&nbsp;   private RestTemplate restTemplate;

&nbsp;   

&nbsp;   @Autowired

&nbsp;   private IRestService restService;

&nbsp;   

&nbsp;   @Test

&nbsp;   void testGetSuccess() {

&nbsp;       // Mock da resposta

&nbsp;       ResponseEntity<Usuario> mockResponse = ResponseEntity.ok(new Usuario("Teste"));

&nbsp;       

&nbsp;       when(restTemplate.exchange(

&nbsp;           any(String.class),

&nbsp;           eq(HttpMethod.GET),

&nbsp;           any(HttpEntity.class),

&nbsp;           any(ParameterizedTypeReference.class)

&nbsp;       )).thenReturn(mockResponse);

&nbsp;       

&nbsp;       // Executar teste

&nbsp;       DefaultResponse<Usuario> resposta = restService.get(

&nbsp;           "https://api.exemplo.com/teste",

&nbsp;           new ParameterizedTypeReference<Usuario>() {}

&nbsp;       );

&nbsp;       

&nbsp;       assertTrue(resposta.isSuccess());

&nbsp;       assertEquals("Teste", resposta.getData().getNome());

&nbsp;   }

}

```



\### Testes de Integração

```java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM\_PORT)

class RestServiceIntegrationTest {

&nbsp;   

&nbsp;   @LocalServerPort

&nbsp;   private int port;

&nbsp;   

&nbsp;   @Autowired

&nbsp;   private IRestService restService;

&nbsp;   

&nbsp;   @Test

&nbsp;   void testEndToEnd() {

&nbsp;       String url = "http://localhost:" + port + "/api/teste";

&nbsp;       

&nbsp;       DefaultResponse<String> resposta = restService.get(

&nbsp;           url,

&nbsp;           new ParameterizedTypeReference<String>() {}

&nbsp;       );

&nbsp;       

&nbsp;       assertTrue(resposta.isSuccess());

&nbsp;   }

}

```



\## 📈 Performance



\### Configurações Recomendadas



```yaml

\# application.yml

rest:

&nbsp; service:

&nbsp;   max-attempts: 3

&nbsp;   initial-delay-ms: 1000

&nbsp;   max-delay-ms: 5000

&nbsp;   connection-timeout: 30000

&nbsp;   read-timeout: 60000

&nbsp;   max-connections: 100

&nbsp;   max-connections-per-route: 20

```



\### Monitoramento de Performance

```java

@Component

public class RestServiceMonitor {

&nbsp;   

&nbsp;   public void monitorPerformance() {

&nbsp;       // Métricas sugeridas para monitorar:

&nbsp;       // - Tempo médio de resposta

&nbsp;       // - Taxa de sucesso/falha

&nbsp;       // - Tentativas de retry

&nbsp;       // - Uso de conexões

&nbsp;       // - Erros por tipo de autenticação

&nbsp;   }

}

```



\## 🔧 Extensibilidade



\### Adicionar Novo Tipo de Autenticação

1\. Estender `EAuthenticationType`

2\. Implementar `IAuthenticationService`

3\. Registrar no `createAuthenticationServicesMap()`



\### Adicionar Novo Formato de Resposta

1\. Criar novo `MessageConverter`

2\. Registrar no `RestTemplate`

3\. Criar métodos específicos se necessário



\## 🚨 Troubleshooting



\### Problemas Comuns



1\. \*\*Timeout Excessivo\*\*

&nbsp;  ```java

&nbsp;  // Verificar configurações do RestTemplate

&nbsp;  factory.setConnectTimeout(30000);

&nbsp;  factory.setReadTimeout(30000);

&nbsp;  ```



2\. \*\*Erros de Autenticação\*\*

&nbsp;  - Verificar tipo de autenticação correto

&nbsp;  - Confirmar credenciais

&nbsp;  - Verificar se o token Bearer está expirado



3\. \*\*Erros de Serialização\*\*

&nbsp;  - Verificar `ParameterizedTypeReference`

&nbsp;  - Confirmar que a classe de resposta tem getters/setters públicos



\### Logs de Depuração

```java

logging:

&nbsp; level:

&nbsp;   br.com.nexalty.commons.rest: DEBUG

&nbsp;   org.springframework.web.client.RestTemplate: DEBUG

```



\## 📄 Licença



Copyright © nexalty. Todos os direitos reservados.



\## 🤝 Contribuição



1\. Fork o projeto

2\. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)

3\. Commit suas mudanças (`git commit -m 'Add: AmazingFeature'`)

4\. Push para a branch (`git push origin feature/AmazingFeature`)

5\. Abra um Pull Request



\## 📞 Suporte



Para suporte, entre em contato:

\- Email: suporte@nexalty.com.br

\- Issue Tracker: \[GitHub Issues](https://github.com/nexalty/commons-rest/issues)



---



\*\*Versão\*\*: 2.0.0  

\*\*Última Atualização\*\*: Novembro 2024  

\*\*Requisitos Mínimos\*\*: Java 11+, Spring Boot 2.7+

