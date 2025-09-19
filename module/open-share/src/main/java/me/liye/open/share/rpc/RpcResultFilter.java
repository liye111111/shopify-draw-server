package me.liye.open.share.rpc;

import org.springframework.core.Ordered;

/**
 * 用於增強方法的出參RpcRpcResult
 *
 * @author knight@momo.com
 */
public interface RpcResultFilter extends Ordered {
    <T> void filter(RpcResult<T> rpcResult);

}
