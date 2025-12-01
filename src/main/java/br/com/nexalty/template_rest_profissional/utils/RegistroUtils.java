package br.com.nexalty.template_rest_profissional.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.nexalty.template_rest_profissional.types.Registro;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;


public class RegistroUtils {
	
	private RegistroUtils() {}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Registro toRegistro(Map<String, Object> from) {
		if(from == null)
			return null;
		
    	Registro reg = new Registro();
    	for (Map.Entry<String, Object> entry : from.entrySet()) {
			Object obj = entry.getValue();
			if(obj instanceof Map) {
				reg.put(entry.getKey(), toRegistro((Map)obj));
			} else if (obj instanceof List) {
				reg.put(entry.getKey(), ((List) entry.getValue()).stream().map(o -> toRegistro((Map)o)).collect(Collectors.toList()) );
			}else {
				reg.put(entry.getKey(), entry.getValue());
			}
		}
    	return reg;
    }

	public static Registro toRegistro(Object o) {
		return new ObjectMapper().convertValue(o,Registro.class);
	}
	
	public static Registro converter(JSONObject jsonObject) {
	    if (jsonObject == null) return new Registro();
	    
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	        Map<String, Object> map = mapper.readValue(jsonObject.toString(), Map.class);
	        return toRegistro(map);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new Registro();
	    }
	}

}