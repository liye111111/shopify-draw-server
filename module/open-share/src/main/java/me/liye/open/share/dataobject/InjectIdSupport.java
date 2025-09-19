package me.liye.open.share.dataobject;

/**
 * 用於mapper insert方法的入參類型，聲明該對象可以注入ID（使注入生效，需要method添加@InjectId)。建議繼承BaseDO或其子類。
 * @author knight@momo.com
 */
public interface InjectIdSupport{
    void setId(Long id);

    Long getId();
}
