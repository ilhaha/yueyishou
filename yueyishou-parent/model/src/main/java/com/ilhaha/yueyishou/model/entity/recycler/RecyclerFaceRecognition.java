package com.ilhaha.yueyishou.model.entity.recycler;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("recycler_face_recognition")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecyclerFaceRecognition extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**回收员id*/
    private Long recyclerId;
	/**识别日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date faceDate;
}
