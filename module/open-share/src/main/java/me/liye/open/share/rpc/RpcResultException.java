package me.liye.open.share.rpc;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * Created by cola on 2022/9/23.
 */
@Data
public class RpcResultException extends RuntimeException {


    RpcResult rpcResult;

    public RpcResultException(RpcResult rpcResult) {
        super(JSON.toJSONString(rpcResult));
        this.rpcResult = rpcResult;

    }

    public RpcResultException(String message, RpcResult rpcResult) {
        super(message);
        this.rpcResult = rpcResult;
    }

    public RpcResultException(String message, Throwable cause, RpcResult rpcResult) {
        super(message, cause);
        this.rpcResult = rpcResult;
    }

    public RpcResultException(Throwable cause, RpcResult rpcResult) {
        super(cause);
        this.rpcResult = rpcResult;
    }

    public RpcResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RpcResult rpcResult) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.rpcResult = rpcResult;
    }
}
