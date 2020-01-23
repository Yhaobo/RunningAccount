package erp.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 打印持久层错误信息到控制台
 */
@Deprecated
@ControllerAdvice
public class DataAccessExceptionHandler {
//    @ExceptionHandler(DataAccessException.class)
//    public void handle(Exception e, HttpServletRequest request) {
//        e.printStackTrace();
//        System.out.println("来自页面: "+request.getHeader("Referer"));
//    }
}
