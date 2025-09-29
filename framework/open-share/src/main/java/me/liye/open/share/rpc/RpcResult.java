package me.liye.open.share.rpc;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.opentelemetry.api.trace.Span;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.liye.open.share.enums.SysErrorMsgEnum;
import me.liye.open.share.exception.BizExceptionMessageProvider;
import me.liye.open.share.page.PageQueryResult;
import me.liye.open.share.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.LF;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RpcResult<T> implements Serializable {
    public static final String HEADER_TRACE_ID = "trace-id";
    public static final String HEADER_LANG = "lang";
    /**
     * 跟蹤業務來源
     */
    public static final String HEADER_REQUEST_ORIGIN = "request-origin";
    /**
     * 指定前端路由
     */
    public static final String HEADER_RESPONSE_FORWARD = "response-forward";
    public static final String HEADER_EXCEPTION_STACK = "exception-stack";

    /**
     * 擴展字段
     */
    @Builder.Default
    private HashMap<String, String> header = new HashMap<>();

    private T data;

    private boolean success;


    private String errCode;

    private String errMsg;

    private String[] errMsgArgs;

    private Pagination page;

//
//    @Deprecated
//    private String i18nErrCode;
//    @Deprecated
//    private String keyOperation;

    public RpcResult<T> putHeader(String key, String value) {
        header.put(StringUtils.trimToEmpty(key), StringUtils.trimToEmpty(value));
        return this;
    }

    public String getHeaderValue(String key) {
        return header == null ? null : header.get(key);
    }

    public String getLang() {
        return getHeaderValue(HEADER_LANG);
    }

    public RpcResult<T> lang(String lang) {
        putHeader(HEADER_LANG, lang);
        return this;
    }

    public RpcResult<T> errMsgArgs(String... errMsgArgs) {
        setErrMsgArgs(errMsgArgs);
        return this;
    }


    public T tryGetSuccessfulData() throws RpcResultException {
        if (!success) {
            throw new RpcResultException(this);
        } else {
            return this.getData();
        }
    }

    public static <T> RpcResult<T> success() {
        return success(null);
    }

    /**
     * 通用成功請求
     */
    public static <T> RpcResult<T> success(T data) {
        RpcResult<T> rpcResult = RpcResult.<T>builder().data(data).success(true).build();
        if (data instanceof PageQueryResult) {
            PageQueryResult pageQueryResult = (PageQueryResult) data;
            Pagination pagination = pageQueryResult.getPage();
            rpcResult.setPage(pagination);
        }
        return rpcResult;
    }

    /**
     * 分頁-成功請求
     */
    public static <T> RpcResult<T> success(T data, Integer pageIndex, Integer pageSize, Long total) {
        Pagination page = Pagination.builder().pageIndex(pageIndex).pageSize(pageSize).total(total).build();
        return RpcResult.<T>builder().success(true).data(data).page(page).build();
    }

    /**
     * 分頁-成功請求
     */
    @Deprecated
    public static <T> RpcResult<T> success(T data, Pagination pagination) {
        Pagination page = Pagination.builder().pageIndex(pagination.getPageIndex()).pageSize(pagination.getPageSize()).total(pagination.getTotal()).build();
        return RpcResult.<T>builder().success(true).data(data).page(page).build();
    }

    public static <T> RpcResult<T> error(String errCode) {
        return RpcResult.<T>builder().success(false).errCode(errCode).build();
    }

//    public static <T> RpcResult<T> error(SysErrorMsgEnum sysErrorMsgEnum) {
//        RpcResult<T> result = error(sysErrorMsgEnum.getErrCode());
//        result.setErrMsg(sysErrorMsgEnum.getErrMsg());
//        return result;
//    }


    /**
     * 通用失敗請求
     */
    public static <T> RpcResult<T> error(String errCode, String errMsg) {
        return RpcResult.<T>builder().success(false).errCode(errCode).errMsg(errMsg).build();
    }

    public static <T> RpcResult<T> error(String errCode, String errMsg, String... errMsgArgs) {
        return RpcResult.<T>builder().success(false).errCode(errCode).errMsg(errMsg).errMsgArgs(errMsgArgs).build();
    }

    //    /**
//     * 通用失敗請求
//     */
//    @Deprecated
    public static <T> RpcResult<T> error(SysErrorMsgEnum sysErrorMsgEnum) {
        return RpcResult.<T>builder()
                .success(false)
                .errCode(sysErrorMsgEnum.getErrCode())
                .errMsg(sysErrorMsgEnum.getErrMsg()).build();
    }

    public static <T> RpcResult<T> error(SysErrorMsgEnum sysErrorMsgEnum, String... errMsgArgs) {
        return RpcResult.<T>builder()
                .success(false)
                .errCode(sysErrorMsgEnum.getErrCode())
                .errMsg(sysErrorMsgEnum.getErrMsg())
                .errMsgArgs(errMsgArgs)
                .build();
    }

    public static <T> RpcResult<T> error(Throwable throwable) {
        String errorCode = SysErrorMsgEnum.system_error.errorCode();
        String errorMessage = throwable.getMessage();
        String[] errorArgs = null;
        if (throwable instanceof BizExceptionMessageProvider provider) {
            errorCode = provider.getErrorCode();
            errorMessage = provider.getErrorMessage();
            errorArgs = provider.getErrorArgs();
        }

        RpcResult rpcResult = RpcResult.builder()
                .success(false)
                .errCode(errorCode)
                .errMsg(errorMessage)
                .errMsgArgs(errorArgs)
                .data(null)
                .build();
        rpcResult.putHeader(RpcResult.HEADER_TRACE_ID, Span.current().getSpanContext().getTraceId());
        List<String> stack = ExceptionUtils.getRootCauseStackTrace(throwable, 1);
        rpcResult.putHeader(HEADER_EXCEPTION_STACK, stack.stream().collect(Collectors.joining(LF)));
        return rpcResult;
    }

    public String dump(){
        return JSON.toJSONString(this);
    }

}
