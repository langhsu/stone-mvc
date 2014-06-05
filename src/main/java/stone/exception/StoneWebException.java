package stone.exception;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author langhsu
 *
 */
public class StoneWebException extends RuntimeException {
	private static final long serialVersionUID = -3762622261674065229L;

	private final static String lineSeparator = System.getProperty("line.separator");
	
	private Object[] args;
    private ErrorCode errorCode;
    private final Map<String, Object> properties = new TreeMap<String, Object>();
    
    public StoneWebException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public StoneWebException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public StoneWebException(ErrorCode errorCode, String message, Object... args) {
        super(message);
        this.args = args;
        this.errorCode = errorCode;
    }

    public StoneWebException(Throwable cause) {
        super(cause);
    }

    public StoneWebException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public StoneWebException(Throwable cause, String message, Object... args) {
        super(message, cause);
        this.args = args;
    }

    public StoneWebException(Throwable cause, ErrorCode errorCode, String message, Object... args) {
        super(message, cause);
        this.args = args;
        this.errorCode = errorCode;
    }
    
    public static StoneWebException unchecked(Throwable e) {
        return unchecked(e, null);
    }

    public static StoneWebException unchecked(Throwable e, ErrorCode errorCode) {
        if (e instanceof StoneWebException) {
            StoneWebException se = (StoneWebException) e;
            if (errorCode != null && errorCode != se.getErrorCode()) {
                return new StoneWebException(e, errorCode);
            }
            return se;
        } else {
            return new StoneWebException(e, errorCode);
        }
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public StoneWebException setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) properties.get(name);
    }

    public StoneWebException set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    public String getSimpleMessage() {
        String message = super.getMessage();
        if (message != null) {
            if (args != null && args.length > 0) {
                message = String.format(message, args);
            }
        }
        return message;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        String message = getSimpleMessage();
        if (message != null) {
            sb.append(message);
        }
        if (errorCode != null) {
            if (sb.length() > 0) {
                sb.append(lineSeparator);
                sb.append("\t");
            }
            sb.append(errorCode.getClass().getSimpleName());
            sb.append(".");
            sb.append(errorCode);
        }
        for (String key : properties.keySet()) {
            if (sb.length() > 0) {
                sb.append(lineSeparator);
                sb.append("\t");
            }
            Object value = properties.get(key);
            if (value != null && value.getClass().isArray()) {
                value = ArrayUtils.toString(value);
            }
            sb.append(key);
            sb.append(" = [");
            sb.append(value);
            sb.append("]");
        }
        return sb.length() == 0 ? null : sb.toString();
    }
}
