package erp.util;

import lombok.Data;

@Data
public class MyException extends RuntimeException {
    private Integer code;

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(Throwable cause) {
        super(cause);
    }
}
