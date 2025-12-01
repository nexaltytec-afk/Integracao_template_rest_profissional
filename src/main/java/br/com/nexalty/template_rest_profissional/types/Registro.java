package br.com.nexalty.template_rest_profissional.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class Registro extends LinkedHashMap<String, Object>{

    private static final long serialVersionUID = -9100647403066998159L;

    /**
     * retorna Tipagem ja com cast não necessita criar E.g.: getListInt ou nulo
     * @param key
     * @return
     */

    public <T> T getAs(String key) {
        return getAsOrDefault(key, null);
    }

    /**
     * retorna Tipagem ja com cast não necessita criar E.g.: getListInt ou valor default quando nulo
     * @param key
     * @return
     */
    public <T> T getAsOrDefault(String key, T value) {
        return (T) this.getOrDefault(key, value);
    }

    public String getAsString(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            return String.valueOf(this.get(key));
        }
        return null;
    }

    public String getAsStringOrValue(String key, String value) {
        String ret = getAsString(key);
        return ( ret == null || StringUtils.isBlank(ret) ? value : ret );
    }

    /**
     * retorna branco caso nulo
     * @param key
     * @return
     */
    public String getAsStringNN(String key) {
        String ret = getAsString(key);
        return ( ret == null ? "" : ret );
    }

    public double getAsDouble(String key) {
        if ( this.containsKey(key) && this.get(key) != null) {
            Object obj = this.get(key);
            try {
                if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                    return ((BigDecimal)obj).doubleValue();
                } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                    return ((Integer)obj).doubleValue();
                } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                    return ((Long)obj).doubleValue();
                } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                    return ((Float)obj).doubleValue();
                } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                    return ((Double)obj).doubleValue();
                } else if ( obj.getClass() == (new String()).getClass() ) {
                    String valor = (String)obj ;
                    try {
                        return Double.valueOf(valor).doubleValue();
                    } catch (NumberFormatException e) {
                        // se nao esta no formato, desformata
                        return stringToBigDecimal(valor).doubleValue();
                    }
                } else {
                    System.err.println(">>> tipo do objeto nao pode ser convertido para double:" + obj.getClass().getName() + " : " + obj);
                }
            } catch (Exception e) {
                System.err.println(">>> erro na conversao do objeto para double:" + obj.getClass().getName());
                e.printStackTrace();
            }
        }
        return 0.0d;
    }

    public BigDecimal getAsBigDecimal(String key) {
        if ( this.containsKey(key)) {
            Object obj = this.get(key);
            try {
                if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                    return (BigDecimal)obj;
                } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                    return new BigDecimal((Integer)obj);
                } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                    return new BigDecimal((Long)obj);
                } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                    return new BigDecimal((Float)obj);
                } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                    return new BigDecimal((Double)obj);
                } else if ( obj.getClass() == (new String()).getClass() ) {
                    String valor = (String)obj ;
                    try {
                        return new BigDecimal(valor);
                    } catch (NumberFormatException e) {
                        // se nao esta no formato, desformata
                        return stringToBigDecimal(valor);
                    }
                } else {
                    System.err.println(">>> tipo do objeto nao pode ser convertido para BigDecimal:" + obj.getClass().getName() + " : " + obj);
                }
            } catch (Exception e) {
                System.err.println(">>> erro na conversao do objeto para BigDecimal:" + obj.getClass().getName());
                e.printStackTrace();
            }
        }
        return null;
    }

    public BigDecimal getAsBigDecimalOrValue(String key, BigDecimal defaultValue) {
        Object value = this.get(key);

        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue; // Retorna o valor padrão caso haja erro na conversão
        }
    }

    private static BigDecimal stringToBigDecimal(String n) {
        return stringToBigDecimal(n, null);
    }

    private static BigDecimal stringToBigDecimal(String n, Locale locale) {
        if (locale == null) {
            locale = new Locale("pt", "BR");
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        try {
            return new BigDecimal(numberFormat.parse(n).toString());
        } catch (ParseException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * retorna valor Inteiro ou 0 se nulo
     * @param key
     * @return
     */
    public Integer getAsIntOrZero(String key) {
        return getAsIntOrValue(key, 0);
    }

    /**
     * retorna inteiro ou valor definido
     * @param key
     * @param value
     * @return
     */
    public Integer getAsIntOrValue(String key, int value) {
        Integer ret = getAsInt(key);
        return ( ret == null ? value : ret );
    }


    public Integer getAsInt(String key) {
        try {
            Object obj = this.get(key);
            if ( obj != null) {
                if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                    return ((BigDecimal)obj).intValue();
                } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                    return (Integer)obj;
                } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                    return ((Long)obj).intValue();
                } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                    return ((Float)obj).intValue();
                } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                    return ((Double)obj).intValue();
                } else if ( obj.getClass() == ("").getClass() ) {
                    if ( StringUtils.isBlank((String)obj) )
                        return null;
                    return Integer.valueOf((String)obj);
                } else {
                    System.err.println(">>> tipo do objeto nao pode ser convertido para Integer:" + obj.getClass().getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getAsLong(String key) {
        if ( this.containsKey(key)) {
            Object obj = this.get(key);
            if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                return ((BigDecimal)obj).longValue();
            } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                return ((Integer)obj).longValue();
            } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                return (Long)obj;
            } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                return ((Float)obj).longValue();
            } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                return ((Double)obj).longValue();
            } else if ( obj.getClass() == (new String()).getClass() ) {
                if ( StringUtils.isBlank((String)obj) )
                    return null;
                return Long.valueOf((String)obj);
            } else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para Long:" + obj.getClass().getName());
            }
        }
        return null;
    }

    public Date getAsDate(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            Object obj = this.get(key);
            if ( obj.getClass() == (new Date()).getClass() ) {
                return (Date)obj;
            } else if ( obj.getClass() == Timestamp.class ) {
                return new Date(((Timestamp)obj).getTime());
            } else if ( obj.getClass() == java.sql.Date.class ) {
                return new Date(((java.sql.Date)obj).getTime());
            }  else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para Date:" + obj.getClass().getName());
            }
        }
        return null;
    }

    public Timestamp getAsTimestamp(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            Object obj = this.get(key);
            if ( obj.getClass() == Timestamp.class ) {
                return (Timestamp) obj;
            } else if ( obj.getClass() == java.sql.Date.class ) {
                return new Timestamp(((java.sql.Date)obj).getTime());
            } else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para Timestamp:" + obj.getClass().getName());
            }
        }
        return null;
    }

    public static Registro build() {
        return new Registro();
    }

    public Registro with(String key, Object value) {
        this.put(key, value);
        return this;
    }
    /**
     * retorna registro em formato JSON
     * @return
     */
    public String getAsJSON() {
        String ret = "";
        try {
            ret = new ObjectMapper().writer().writeValueAsString(this) ;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public boolean getAsBoolean(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            try {
                Object obj = this.get(key);
                if (obj.getClass() == String.class)
                    return Boolean.valueOf(((String) this.get(key)).trim().toLowerCase());
                else if (obj.getClass() == Boolean.class)
                    return Boolean.valueOf((Boolean)this.get(key));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Boolean.FALSE;
    }

    public boolean getAsBooleanOrValue(String key, boolean value){
        if (this.containsKey(key) && this.get(key) != null) {
            try {
                Object obj = this.get(key);
                return getAsBoolean(key);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public List<String> getFields() {
        return new ArrayList<String>(this.keySet());
    }

    public byte[] getAsByte(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            return (byte[]) this.get(key);
        }
        return null;
    }

    public BigDecimal getAsBigDecimalOrValue(String key, double defaultValue) {
        return getAsBigDecimalOrValue(key, new BigDecimal(defaultValue));
    }
}
