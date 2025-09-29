package me.liye.open.share.exception;

/**
 *
 */
public interface BizExceptionMessageProvider {
    String getErrorCode();

    String getErrorMessage();

    default String[] getErrorArgs() {
        return null;
    }
}
