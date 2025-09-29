package me.liye.open.share.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * DO的基類，默認json_data不保存字段，需要保存的field要用@JsonDataProperty聲明
 *
 * @author knight@momo.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class BaseDataObjectWithEmptyJsonColumn extends BaseDataObjectWithJsonColumn {
    @Override
    protected Set<String> getJsonIgnoreFieldNames() {
        Set<String> ignores = super.getJsonIgnoreFieldNames();
        Field[] fields = FieldUtils.getAllFields(this.getClass());
        for (Field field : fields) {
            String name = field.getName();
            JsonDataProperty ann = field.getAnnotation(JsonDataProperty.class);
            if (ann == null) {
                ignores.add(name);
            }
        }
        return ignores;
    }
}
