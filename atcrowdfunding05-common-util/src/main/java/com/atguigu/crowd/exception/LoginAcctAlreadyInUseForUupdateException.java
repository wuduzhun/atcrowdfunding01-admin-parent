package com.atguigu.crowd.exception;

/**
 * 保存或更新admin时，如果检测到登录账号重复抛出异常
 */
public class LoginAcctAlreadyInUseForUupdateException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public LoginAcctAlreadyInUseForUupdateException() {
        super();
    }

    public LoginAcctAlreadyInUseForUupdateException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseForUupdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseForUupdateException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctAlreadyInUseForUupdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
