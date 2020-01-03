package com.xy.experiment.exceptions;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1687478562586239073L;
    public static final int DEFAULT_CODE = 110110;
    public static final String DEFAULT_MSG = "系统错误！";
    protected String msg;
    protected int code;

    public BizException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public BizException() {
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }

    public BizException newInstance(String msgFormat, Object... args) {
        return new BizException(this.code, msgFormat, args);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message) {
        super(message);
    }
}
