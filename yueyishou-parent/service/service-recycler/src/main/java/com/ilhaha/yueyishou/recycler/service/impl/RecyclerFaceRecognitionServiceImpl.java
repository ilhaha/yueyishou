package com.ilhaha.yueyishou.recycler.service.impl;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerFaceRecognition;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerFaceRecognitionMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerFaceRecognitionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
public class RecyclerFaceRecognitionServiceImpl extends ServiceImpl<RecyclerFaceRecognitionMapper, RecyclerFaceRecognition> implements IRecyclerFaceRecognitionService {

    /**
     * 判断回收员是否已进行了每日人脸签到
     *
     * @param recyclerId
     * @return
     */
    @Override
    public Boolean isSign(Long recyclerId) {
        LambdaQueryWrapper<RecyclerFaceRecognition> recyclerFaceRecognitionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerFaceRecognitionLambdaQueryWrapper.eq(RecyclerFaceRecognition::getRecyclerId, recyclerId)
                .eq(RecyclerFaceRecognition::getFaceDate, DateUtils.format(new Date(), "yyyy-MM-dd"));
        return !ObjectUtils.isEmpty(this.getOne(recyclerFaceRecognitionLambdaQueryWrapper));
    }
}
