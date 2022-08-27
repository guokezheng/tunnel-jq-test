package com.tunnel.platform.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 情报板敏感字管理对象 sd_audit_vocabulary
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
public class SdAuditVocabulary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 文本 */
    @Excel(name = "文本")
    private String word;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String creatTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWord(String word) 
    {
        this.word = word;
    }

    public String getWord() 
    {
        return word;
    }
    public void setCreatTime(String creatTime) 
    {
        this.creatTime = creatTime;
    }

    public String getCreatTime() 
    {
        return creatTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("word", getWord())
            .append("creatTime", getCreatTime())
            .toString();
    }
}
