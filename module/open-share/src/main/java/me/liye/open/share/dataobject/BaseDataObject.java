package me.liye.open.share.dataobject;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * DO的基類
 * @author knight@momo.com
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDataObject implements InjectIdSupport, Serializable {

    Long id;
    String shopId;
    Date gmtCreate;
    Date gmtModified;
    
    @Builder.Default
    Boolean isDeleted = false;

    public String dump(){
        return JSON.toJSONString(this);
    }
}
