package br.com.nexalty.template_rest_profissional.oap;
 
import br.com.nexalty.template_rest_profissional.types.DefaultResponse;
import br.com.nexalty.template_rest_profissional.types.Registro;
import br.com.nexalty.template_rest_profissional.utils.ExceptionUtils;
import br.com.nexalty.template_rest_profissional.utils.JSONUtils;
import br.com.nexalty.template_rest_profissional.utils.constants.ErrorConst;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

//import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
//import org.springframework.dao.InvalidDataAccessApiUsageException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.web.firewall.RequestRejectedException;
import 
org.springframework.http.converter.HttpMessageNotReadableException;
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
 
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultResponse<?>> handleRequestRejectedException(HttpMessageNotReadableException ex) {
		   log.warn("Dados Invalidos: {} {}", ex.getCause(), ex.getLocalizedMessage());
	        
	        DefaultResponse<String> response = DefaultResponse.error( 
	            "Requeste Rejeitado: " + ex.getMessage(),
	            "",
	            HttpStatus.NOT_FOUND.value(),
	            ErrorConst.INVALID_DATA_ACCESS_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
	
	@ExceptionHandler(RequestRejectedException.class)
    public ResponseEntity<DefaultResponse<?>> handleRequestRejectedException(RequestRejectedException ex) {
		   log.warn("Dados Invalidos: {} {}", ex.getCause(), ex.getLocalizedMessage());
	        
	        DefaultResponse<String> response = DefaultResponse.error( 
	            "Requeste Rejeitado: " + ex.getCause(),
	            "Dados Invalidos",
	            HttpStatus.NOT_FOUND.value(),
	            ErrorConst.INVALID_DATA_ACCESS_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
    
	
	@ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<DefaultResponse<?>> handleInvalidMalformedJwtException(MalformedJwtException ex) {
		   log.warn("Dados Invalidos: {} {}", ex.getCause(), ex.getLocalizedMessage());
	        
	        DefaultResponse<String> response = DefaultResponse.error( 
	            "Dados enviados inconsistentes: " + ex.getCause(),
	            "Dados Invalidos",
	            HttpStatus.NOT_FOUND.value(),
	            ErrorConst.INVALID_DATA_ACCESS_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
    
	
	 @ExceptionHandler(MessagingException.class)
	    public ResponseEntity<DefaultResponse<?>> handleInvalidMessagingException(MessagingException ex) {
	        log.warn("Dados Invalidos: {} {}", ex.getCause(), ex.getLocalizedMessage());
	        
	        DefaultResponse<String> response = DefaultResponse.error( 
	            "Dados enviados inconsistentes: " + ex.getCause(),
	            "Dados Invalidos",
	            HttpStatus.NOT_FOUND.value(),
	            ErrorConst.INVALID_DATA_ACCESS_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
	 
	/* @ExceptionHandler(InvalidDataAccessApiUsageException.class)
	    public ResponseEntity<DefaultResponse<?>> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
	        log.warn("Dados Invalidos: {} {}", ex.getCause(), ex.getLocalizedMessage());
	        
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Dados enviados inconsistentes: " + ex.getCause(),
	            "Dados Invalidos",
	            HttpStatus.NOT_FOUND.value(),
	            ErrorConst.INVALID_DATA_ACCESS_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
*/
	    @ExceptionHandler(NoHandlerFoundException.class)
	    public ResponseEntity<DefaultResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
	        log.warn("Endpoint não encontrado: {} {}", ex.getHttpMethod(), ex.getRequestURL());
	        
	        DefaultResponse<String> response = DefaultResponse.error(
	            "O endpoint solicitado não existe",
	            "Endpoint não encontrado",
	            HttpStatus.NOT_FOUND.value(),
	            ErrorConst.ENDPOINT_NOT_FOUND_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
	    
	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<DefaultResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
	        log.warn("Argumento ilegal: {}", ex.getMessage());
	        
	        DefaultResponse<String> response = DefaultResponse.error(
	            ex.getMessage(),
	            "Argumento inválido",
	            HttpStatus.BAD_REQUEST.value(),
	            ErrorConst.ILLEGAL_ARGUMENT_ERROR1
	        );
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
	    
	    @ExceptionHandler(NoResourceFoundException.class)
	    public ResponseEntity<DefaultResponse<?>> handleNoResourceFoundException(NoResourceFoundException ex) {
	        DefaultResponse<String> response = DefaultResponse.error(
	            "O endpoint solicitado não existe",
	            "Recurso não encontrado",
	            HttpStatus.NOT_FOUND.value(),
	            ErrorConst.ENDPOINT_NOT_FOUND_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<DefaultResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        Registro errors = new Registro();
	        ex.getBindingResult().getFieldErrors().forEach(error ->
	                errors.put(error.getField(), error.getDefaultMessage()));

	        DefaultResponse<Registro> response = DefaultResponse.error(
	            "Os parâmetros fornecidos não atendem aos requisitos esperados.",
	            errors,
	            HttpStatus.BAD_REQUEST.value(),
	            ErrorConst.VALIDATION_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<DefaultResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
	        Registro errors = new Registro();
	        ex.getConstraintViolations().forEach(violation ->
	                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

	        DefaultResponse<Registro> response = DefaultResponse.error(
	            "Dados de entrada inválidos.",
	            errors,
	            HttpStatus.BAD_REQUEST.value(),
	            ErrorConst.CONSTRAINT_VIOLATION_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(ValidationExpectedException.class)
	    public ResponseEntity<DefaultResponse<?>> trataErrosEsperados(HttpServletRequest request, ValidationExpectedException e) {
	        DefaultResponse<Object> response = DefaultResponse.error(
	            e.getMessage(),
	            e.getDados(),
	            HttpStatus.BAD_REQUEST.value(),
	            ErrorConst.VALIDATION_EXPECTED_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(InvalidAudienceException.class)
	    public ResponseEntity<DefaultResponse<?>> handleInvalidAudienceException(HttpServletRequest request, InvalidAudienceException e) {
	        log.warn("Audience inválido: {}", e.getMessage());
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Token com audience inválido",
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.INVALID_AUDIENCE_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(InvalidIssuerException.class)
	    public ResponseEntity<DefaultResponse<?>> handleInvalidIssuerException(HttpServletRequest request, InvalidIssuerException e) {
	        log.warn("Issuer inválido: {}", e.getMessage());
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Token com issuer inválido",
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.INVALID_ISSUER_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(TokenExpiredException.class)
	    public ResponseEntity<DefaultResponse<?>> handleTokenExpiredException(HttpServletRequest request, TokenExpiredException e) {
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Token expirado",
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.TOKEN_EXPIRED_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
	    
	    @ExceptionHandler(SignatureException.class)
	    public ResponseEntity<DefaultResponse<?>> handleSignatureException(HttpServletRequest request, SignatureException e) {
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Token inválido!",
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.SIGNATURE_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(AuthenticationException.class)
	    public ResponseEntity<DefaultResponse<?>> handleAuthenticationException(HttpServletRequest request, AuthenticationException e) {
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Falha na autenticação! Credenciais inválidas ou token ausente/expirado.",
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.AUTHENTICATION_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(AccessDeniedException.class)
	    public ResponseEntity<DefaultResponse<?>> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Acesso negado! Você não tem permissão para acessar este recurso.",
	            null,
	            HttpStatus.FORBIDDEN.value(),
	            ErrorConst.ACCESS_DENIED_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.FORBIDDEN)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(TokenIssuedInFutureException.class)
	    public ResponseEntity<DefaultResponse<?>> handleTokenIssuedInFutureException(HttpServletRequest request, TokenIssuedInFutureException e) {
	        log.warn("Token emitido no futuro: {}", e.getMessage());
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Token com data de emissão inválida",
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.TOKEN_FUTURE_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(EmailNotVerifiedException.class)
	    public ResponseEntity<DefaultResponse<?>> handleEmailNotVerifiedException(HttpServletRequest request, EmailNotVerifiedException e) {
	        log.warn("Email não verificado: {}", e.getMessage());
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Email não verificado",
	            null,
	            HttpStatus.FORBIDDEN.value(),
	            ErrorConst.EMAIL_NOT_VERIFIED_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.FORBIDDEN)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(TokenValidationException.class)
	    public ResponseEntity<DefaultResponse<?>> handleTokenValidationException(HttpServletRequest request, TokenValidationException e) {
	        log.error("Erro na validação do token: {}", e.getMessage(), e);
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Erro na validação do token",
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.TOKEN_VALIDATION_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	   /* @ExceptionHandler(DataIntegrityViolationException.class)
	    public ResponseEntity<DefaultResponse<?>> handleDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
	        log.error("Erro de integridade de dados: {}", e.getMessage(), e);
	        
	        Map<String, String> errorDetails = ExceptionUtils.extractErrorDetails(e.getMessage());
	        
	        
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Erro de integridade de dados. Verifique os dados fornecidos.",
	            JSONUtils.toJSON(errorDetails),
	            HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            ErrorConst.DATA_INTEGRITY_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }*/
	    
	    @ExceptionHandler(BadCredentialsException.class)
	    public ResponseEntity<DefaultResponse<?>> handleBadCredentialsException(HttpServletRequest request, BadCredentialsException e) {
	        DefaultResponse<String> response = DefaultResponse.error(
	            e.getMessage(),
	            null,
	            HttpStatus.UNAUTHORIZED.value(),
	            ErrorConst.BAD_CREDENTIALS_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<DefaultResponse<?>> handleGenericException(HttpServletRequest request, Exception e) {
	        log.error("Erro não esperado: {}", e.getMessage(), e);
	        DefaultResponse<String> response = DefaultResponse.error(
	            "Um imprevisto aconteceu, aguarde ou entre em contato com o suporte.",
	            null,
	            HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            ErrorConst.GENERIC_ERROR
	        );
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(response);
	    }
	}


