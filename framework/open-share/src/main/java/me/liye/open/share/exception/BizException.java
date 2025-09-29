package me.liye.open.share.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.liye.open.share.enums.SysErrorMsgEnum;

import java.util.Arrays;

/**
 * Base class for business exception, must provide error code.
 *
 * @author knight@momo.com
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {
    String errorCode;
    String errorMessage;
    String[] errorArgs;

    public BizException() {
        super();
    }

    public static BizException of(BizException template, String... args) {
        return new BizException(template.getErrorCode(), template.getErrorMessage(), args);
    }

    public BizException(SysErrorMsgEnum sysErrorMsgEnum) {
        this(sysErrorMsgEnum.getErrCode(), sysErrorMsgEnum.name());
    }

    public BizException(SysErrorMsgEnum sysErrorMsgEnum, String... args) {
        this(sysErrorMsgEnum.getErrCode(), sysErrorMsgEnum.getErrMsg(), args);
    }

    public BizException(BizExceptionMessageProvider provider) {
        this(provider.getErrorCode(), provider.getErrorMessage(), provider.getErrorArgs());
    }

    public BizException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public BizException(String errorCode, String errorMessage) {
        super(errorCode + "," + errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizException(String errorCode, String errorMessage, String... errorArgs) {
        super(errorCode + "," + errorMessage + "," + Arrays.stream(errorArgs).toList());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorArgs = errorArgs;
    }

    public BizException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
    }

    public BizException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode + "," + errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizException(Throwable cause, String errorCode, String errorMessage, String... errorArgs) {
        super(errorCode + "," + errorMessage + "," + Arrays.stream(errorArgs).toList(), cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorArgs = errorArgs;
    }


}
