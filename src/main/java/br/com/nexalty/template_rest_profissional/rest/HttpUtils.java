package br.com.nexalty.template_rest_profissional.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.nexalty.template_rest_profissional.types.Registro;
import br.com.nexalty.template_rest_profissional.utils.RegistroUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class HttpUtils {

	
	public static Registro requestToRegistro(HttpServletRequest request) {
		return requestToRegistro(request, false);
	}
	
	/**
	 * recupera parametros do request e transforma em Map<String,Object>
	 * @param request request a processar
	 * @param isToLog se eh para gerar log em sysout
	 * @return
	 */
	public static Registro requestToRegistro(HttpServletRequest request, boolean isToLog) {
		Registro ret = new Registro();
		try {
			Enumeration<?> param = request.getParameterNames();
			while (param.hasMoreElements()) {
				String name = (String) param.nextElement();
				String[] values = request.getParameterValues(name);

				// se eh lista de objetos
				if ( name.contains("[") ) {
					name = name.substring(0, name.indexOf("["));
				}
				
				if (values.length > 1) {					
					ret.put(name, values);
					if ( isToLog ) {
						System.out.print("\n::" + name + "::");
						for( int i = 0 ; i < values.length; i++ ) 
							System.out.print(String.format(" %s | ",values[i]));
					}
					
				} else {
					ret.put(name, (values[0]).trim());
					if ( isToLog ) System.out.print("\n::" + name + "::" + values[0]);
				}
			}
			if ( isToLog ) System.out.print("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String requestToJson(HttpServletRequest request) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	    
	    try {
	        Map<String, Object> registro = requestToRegistro(request);
	        return objectMapper.writeValueAsString(registro);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "{}";
	    }
	}

	
	public static void flushJSON( ServletOutputStream out, String ret ) throws JSONException {
		try {
			JSONObject jsonObject = new JSONObject(ret);
			String jsonValue = jsonObject.toString();
			out.write(jsonValue.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void flushJSON( ServletOutputStream out, JSONArray ret ) {
		try {
			out.write(ret.toString().getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void flushJSON( ServletOutputStream out, List<Registro> ret ) {
		flushJSON( out, new JSONArray(ret));
	}
	 
    /**
     * recupera IP do request
     * @param request
     * @return
     */
    public static String getIpFromRequest(HttpServletRequest request ) {
    	String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        } 
        return ip;
    }
    
    public static String getBrowserFromRequest(HttpServletRequest request) {
    	String userAgent = request.getHeader("User-Agent");
    	String browserType = "Unknown";
    	String browserVersion = "";
    	if (userAgent.contains("Edge/")) {
            browserType = "Edge";
            browserVersion = userAgent.substring(userAgent.indexOf("Edge")).split("/")[1];

        } else if (userAgent.contains("Safari/") && userAgent.contains("Version/")) {
            browserType = "Safari";
            browserVersion = userAgent.substring(userAgent.indexOf("Version/")+8).split(" ")[0];

        } else if (userAgent.contains("OPR/") || userAgent.contains("Opera/")) {
            browserType = "Opera";
            browserVersion = userAgent.substring(userAgent.indexOf("OPR")).split("/")[1];

        } else if (userAgent.contains("Chrome/")) {
            browserType = "Chrome"; 
            browserVersion = userAgent.substring(userAgent.indexOf("Chrome")).split("/")[1];
            browserVersion = browserVersion.split(" ")[0];

        } else if (userAgent.contains("Firefox/")) {
            browserType = "Firefox"; 
            browserVersion = userAgent.substring(userAgent.indexOf("Firefox")).split("/")[1];
        }
    	
    	return String.format("%s/%s", browserType,browserVersion);
    }

   
    
    /**
     * gerar PDF para response
     * @param response
     * @param pdf
     * @param nome
     */
    public static void flushPdf(HttpServletResponse response, byte[] pdf, String nome) {
    	try {
    		String fileName = String.format("attachment; filename=%s", nome);
    		response.setContentType("application/pdf");
    		response.setContentLength(pdf.length);
    		response.setHeader("Content-Disposition", fileName);
    		OutputStream outStream = response.getOutputStream();
    		outStream.write(pdf);
    		outStream.flush();
    	} catch (IOException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * gerar PDF para response
     * @param response
     * @param pdf
     * @param nome
     */
    public static void flushDefinedType(HttpServletResponse response, byte[] arquivo, String nome, String type) {
    	try {
    		String fileName = String.format("attachment; filename=%s", nome);
    		response.setContentType("application/"+type);
    		response.setContentLength(arquivo.length);
    		response.setHeader("Content-Disposition", fileName);
    		OutputStream outStream = response.getOutputStream();
    		outStream.write(arquivo);
    		outStream.flush();
    	} catch (IOException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
	 * retorna conteudo de request enviado como "payload"/anexo para API's. Exemplo abaixo
	 * curl -X POST "http://localhost:8080/neurons/indicators" -d  "{ \"token\": \"2c94ef0f\", \"usersExternalCodes\": \"[1201, 1247, 1218, 8014]\" }"
	 * @param request
	 * @return
	 */
	public static String requestAsPayLoadToString(HttpServletRequest request ) {
		return requestAsPayLoadToString(request, false);
	}
	
	/**
	 * transforma bloco de dados recebido como formato anexo para string
	 * @param request 
	 * @param isToLog se eh para logar ou nao
	 * @return
	 */
	public static String requestAsPayLoadToString(HttpServletRequest request, boolean isToLog ) {
		StringBuilder ret = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
		    InputStream inputStream = request.getInputStream();
		    if (inputStream != null) {
		        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		        char[] charBuffer = new char[128];
		        int bytesRead = -1;
		        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		        	ret.append(charBuffer, 0, bytesRead);
		        }
		    } else {
		    	ret.append("");
		    }
		} catch (IOException ex) {
		    ex.printStackTrace();
		} finally {
		    if (bufferedReader != null) {
		        try {
		            bufferedReader.close();
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
		}
		
		if ( isToLog ) {			
			System.out.println("::" + request.getContextPath() + " :: " + new java.util.Date() +  ":: parametros lidos ::");
			System.out.println(ret.toString());
		}
		
		return ret.toString();
	}

	/**
	 * recupera dados dos parametros como json body
	 * @param request
	 * @return
	 */
	public static Registro requestAsJsonBodyToRegistro(HttpServletRequest request) {
	    Registro ret = new Registro();
	    String in = HttpUtils.requestAsPayLoadToString(request);
	    if (in != null && !in.isEmpty()) {
	        try {
	            ret = RegistroUtils.converter(new JSONObject(in));
	        } catch (Exception e) {
	            System.err.println(":: erro convertendo entrada para json ::\n" /*+ in*/);
	            e.printStackTrace();
	        }
	    }
	    return ret;
	}
	 
	 public static <T> Class<?> getResponseType(ParameterizedTypeReference<T> parameterizedTypeReference) {
	        Type type = parameterizedTypeReference.getType();
	        

	        if (type instanceof ParameterizedType) {
	            ParameterizedType parameterizedType = (ParameterizedType) type;
	            Type[] typeArguments = parameterizedType.getActualTypeArguments();
	            if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
	                return (Class<?>) typeArguments[0];
	            }
	        } else if (type instanceof Class) {
	           
	            return (Class<?>) type;
	        }
	        
	        throw new IllegalArgumentException("Não foi possível determinar o tipo de resposta. Tipo recebido: " + type.getTypeName());
	    }
    
}
