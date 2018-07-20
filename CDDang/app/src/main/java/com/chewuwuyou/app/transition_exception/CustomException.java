package com.chewuwuyou.app.transition_exception;

/**
 * Created by xxy on 2016/8/15 0015.
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -2431196726844826744L;
    private int code;

    public CustomException() {
        super();
    }

    public CustomException(Throwable t) {
        super(t);
    }

    public CustomException(int code, Throwable t) {
        super(t);
        this.code = code;
    }

    public CustomException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public CustomException(int code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
    }

    public int getErrorCode() {
        return code;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + super.toString();
    }
}
