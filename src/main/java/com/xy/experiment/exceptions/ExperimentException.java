package com.xy.experiment.exceptions;

public class ExperimentException extends BizException {

    private static final long serialVersionUID = 3857448808000827183L;

    public static final int BIZ_ERROR_CODE = 110001;

    public static final int NOT_LOGIN_CODE = 999;

    public static final int PARAMS_ERROR_CODE = 110002;

    public static final int SYSTEM_ERROR_CODE = 110003;

    public ExperimentException() {
    }

    public ExperimentException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public ExperimentException(int code, String msg) {
        super(code, msg);
    }
    public ExperimentException(String msg) {
        super(msg);
    }
}
