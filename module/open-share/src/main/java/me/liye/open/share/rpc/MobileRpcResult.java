package me.liye.open.share.rpc;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 移動端API返回的envelope數據結構
 *
 * @author knight@momo.com
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MobileRpcResult<T> implements Serializable {
    Map<String, String> header = new HashMap<>();

    T data;

    Pagination page;

    boolean success;

    String errCode;

    String errorMessage;

    public String dump() {
        return JSON.toJSONString(this);
    }

    public String dumpPretty() {
        return JSON.toJSONString(this,true);
    }

}
