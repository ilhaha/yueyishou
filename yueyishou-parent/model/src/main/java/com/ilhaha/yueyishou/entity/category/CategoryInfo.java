package com.ilhaha.yueyishou.entity.category;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("category_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CategoryInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**分类名称*/
    private String categoryName;
    /**icon*/
    private String icon;
	/**上层分类ID，0表示顶级分类*/
    private Long parentId;
	/**单价*/
    private BigDecimal unitPrice;
	/**单位*/
    private String unit;
    /**品类描述*/
    private String categoryDescribe;
	/**状态，1:正常 2:禁用*/
    private Integer status;
    /**子菜单*/
    @TableField(exist = false)
    private List<CategoryInfo> children;
}
