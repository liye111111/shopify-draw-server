package me.liye.open.share.dataobject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import java.util.Set;


/**
 * DO的基類，默認將全部屬性保存在json_data字段，可以通過override getJsonIgnoreFieldNames方法自定義
 * @author knight@momo.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDataObjectWithJsonColumn extends BaseDataObject {

    @JsonDataProperty
    @Builder.Default
    String modelVersion = "v1";

    @JsonIgnore
    @JSONField(serialize = false)
    public String getJsonData() {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(this.getClass()); //BUGFIX: must set clz, otherwise nested Property with same name will be ignored.
        filter.getExcludes().addAll(this.getJsonIgnoreFieldNames());
        return JSON.toJSONString(this, filter, SerializerFeature.DisableCircularReferenceDetect);
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public void setJsonData(String json) {
        Object obj = JSON.parseObject(json, this.getClass());
        BeanUtils.copyProperties(obj, this, getJsonIgnoreFieldNames().toArray(new String[0]));
    }

    protected Set<String> getJsonIgnoreFieldNames() {
        return Sets.newHashSet("jsonData", "gmtCreate", "gmtModified");
    }

}
