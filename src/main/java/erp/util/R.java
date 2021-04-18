package erp.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Yhaobo
 * 服务端统一返回的封装结果的对象
 */
@Data
public class R implements Serializable {

    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据
     */
    private Object data;

    public R() {
    }

    public R(boolean success) {
        this.success = success;
    }

    public R(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public R(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public R(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static R ok() {
        final R r = new R();
        r.setCode(200);
        r.setSuccess(true);
        return r;
    }

    public static R fail() {
        final R r = new R();
        r.setCode(406);
        r.setSuccess(false);
        return r;
    }

    public static R withFlag(boolean flag) {
        if (flag) {
            return ok();
        } else {
            return fail();
        }
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R data(Object data) {
        this.data = data;
        return this;
    }

    public R code(Integer code) {
        if (code != null) {
            this.code = code;
        }
        return this;
    }
}

