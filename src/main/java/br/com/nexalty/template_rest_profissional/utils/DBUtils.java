package br.com.nexalty.template_rest_profissional.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.nexalty.template_rest_profissional.types.Registro;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.stream.Streams;
//import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;


@Slf4j
public class DBUtils {
	
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   /* public static RowMapper<Registro> getRegistroRowMapper() {
        return (rs, rowNum) -> {
            Registro registro = new Registro();
            int columnCount = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                registro.putAll(mapResultSetToRegistro(rs));
            }
            return registro;
        };
    }*/

    public static Registro mapResultSetToRegistro( ResultSet rs ) throws SQLException {
        Registro ret = new Registro();
        ResultSetMetaData rm = rs.getMetaData();

        for (int i = 1; i <=  rm.getColumnCount(); i++) {
            int columnType = rm.getColumnType(i);
            String columnTypeName = rm.getColumnTypeName(i);

            String columnName = rm.getColumnLabel(i);
            switch (columnType) {
                case Types.NULL:
                    ret.put(columnName, null);
                    break;
                case Types.INTEGER:
                case Types.SMALLINT:
                case Types.TINYINT:
                    ret.put(columnName, rs.getInt(i));
                    break;
                case Types.BIGINT:
                    ret.put(columnName, rs.getLong(i));
                    break;
                case Types.FLOAT:
                    ret.put(columnName, rs.getFloat(i));
                    break;
                case Types.DOUBLE:
                case Types.REAL:
                    ret.put(columnName, rs.getDouble(i));
                    break;
                case Types.NUMERIC:
                case Types.DECIMAL:
                    BigDecimal bigDecimalValue = rs.getBigDecimal(i);
                    ret.put(columnName, bigDecimalValue);
                    break;
                case Types.BOOLEAN:
                case Types.BIT:
                    ret.put(columnName, rs.getBoolean(i));
                    break;
                case Types.VARCHAR:
                case Types.CHAR:
                case Types.LONGVARCHAR:
                case Types.NVARCHAR:
                case Types.NCHAR:
                    ret.put(columnName, rs.getString(i));
                    break;
                case Types.TIME:
                    Time time = rs.getTime(i);
                    ret.put(columnName, time != null ? time.toLocalTime() : null);
                    break;
                case Types.DATE:
                    Date date = rs.getDate(i);
                    ret.put(columnName, date != null ? date.toLocalDate() : null);
                    break;
                case Types.TIMESTAMP:
                case Types.TIMESTAMP_WITH_TIMEZONE:
                    Timestamp timestamp = rs.getTimestamp(i);
                    ret.put(columnName, timestamp != null ? timestamp.toLocalDateTime() : null);
                    break;
                case Types.OTHER:
                    boolean isJSONType = isIsJSONType(columnTypeName);
                    if(isJSONType) {
                        ret.put(columnName, getJSONAsRegistro(JSONUtils.toJSON(rs.getObject(i)), columnName));
                        break;
                    }
                    ret.put(columnName, rs.getObject(i));
                    break;
                default:
                    log.warn("Tipo nao identificado para coluna: {} tipo: [{}, {}]", columnName, columnType, columnTypeName);
                    ret.put(columnName, null);
                    break;
            }
        }
        return  ret;
    }

    private static Registro getJSONAsRegistro(String json, String columnName) throws SQLException {
        try {
            return OBJECT_MAPPER.readValue(json, Registro.class);
        } catch (Exception e) {
            throw new SQLException("Erro: Nao foi possivel efetaur o parser column: " + columnName, e);
        }
    }

    private static boolean isIsJSONType(String columnTypeName) {
        return Streams.of("json", "jsonb").anyMatch(columnTypeName::equalsIgnoreCase);
    }

    public static Registro getFirst(List<Registro> lista) {
        return lista.stream().findFirst().orElse(null);
    }
}
