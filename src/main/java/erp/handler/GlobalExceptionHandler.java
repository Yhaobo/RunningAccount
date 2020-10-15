package erp.handler;

import erp.util.MyException;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Yhaobo
 * @since 2020/10/3
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error("", e);
        return R.fail().message("操作失败");
    }

    @ExceptionHandler(MyException.class)
    public R handleMyException(MyException e) {
        log.error("", e);
        return R.fail().code(e.getCode()).message(e.getMessage());
    }
}
