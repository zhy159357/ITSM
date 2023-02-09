package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.validation.Create;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.EventWhiteList;
import com.ruoyi.form.mapper.EventWhiteListMapper;
import com.ruoyi.form.service.EventWhiteListService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class EventWhiteListServiceImpl extends ServiceImpl<EventWhiteListMapper, EventWhiteList> implements EventWhiteListService {
    @Override
    public List<EventWhiteList> searchWhiteListAll(EventWhiteList eventWhiteList) {
        QueryWrapper<EventWhiteList> queryWrapper = new QueryWrapper<>();
        if (eventWhiteList.getName() != null && !eventWhiteList.getName().isEmpty()) {
            queryWrapper.like("name", eventWhiteList.getName());
        }
        if (eventWhiteList.getCertificatesType() != null && !eventWhiteList.getCertificatesType().isEmpty()) {
            queryWrapper.eq("certificates_type", eventWhiteList.getCertificatesType());
        }
        if (eventWhiteList.getCertificatesNumber() != null && !eventWhiteList.getCertificatesNumber().isEmpty()) {
            queryWrapper.like("certificates_number", eventWhiteList.getCertificatesNumber());
        }
        if (eventWhiteList.getPhoneNumber() != null && !eventWhiteList.getPhoneNumber().isEmpty()) {
            queryWrapper.like("phone_number", eventWhiteList.getPhoneNumber());
        }
        if (eventWhiteList.getAvailability() != null && !eventWhiteList.getAvailability().isEmpty()) {
            queryWrapper.eq("availability", eventWhiteList.getAvailability());
        }
        if (eventWhiteList.getVendor() != null && !eventWhiteList.getVendor().isEmpty()) {
            queryWrapper.like("vendor", eventWhiteList.getVendor());
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<EventWhiteList> selectByVendor(String vendor) {
        QueryWrapper<EventWhiteList> queryWrapper = new QueryWrapper<>();
        if (vendor != null && !vendor.isEmpty()) {
            queryWrapper.like("vendor", vendor);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void insertWhiteListBatch(List<EventWhiteList> list) {
        list.forEach(white -> {
            if("0".equals(white.getAvailability())) {
                baseMapper.delete(new QueryWrapper<EventWhiteList>().eq("certificates_number", white.getCertificatesNumber()));
            } else {
                QueryWrapper<EventWhiteList> queryWrapper = new QueryWrapper<EventWhiteList>().eq("certificates_number", white.getCertificatesNumber());
                EventWhiteList eventWhiteList = baseMapper.selectOne(queryWrapper);
                if(eventWhiteList != null) {
                    baseMapper.update(eventWhiteList, new UpdateWrapper<EventWhiteList>().eq("certificates_number", white.getCertificatesNumber()));
                } else {
                    baseMapper.insert(eventWhiteList);
                }
            }
        });
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String importData(EventWhiteList eventWhiteList) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<EventWhiteList>> violations = validator.validate(eventWhiteList, Create.class);
        StringBuilder ret = new StringBuilder();
        if (!violations.isEmpty()) {
            violations.forEach(item -> {
                ret.append(item.getMessage()).append(";");
            });
            return ret.toString();
        }
        if (eventWhiteList.getCertificatesType().equals("1")) {  // 居民身份证
            if (!Pattern.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)", eventWhiteList.getCertificatesNumber())) {
                return "身份证号校验错误";
            }
        }
        try {
            baseMapper.insert(eventWhiteList);
        } catch (DuplicateKeyException e) {
            String err = "证件号/手机号不允许重复";
            if (Objects.requireNonNull(e.getMessage()).contains("idx_event_white_list_phone")) {
                err = "手机号不允许重复";
            } else if (Objects.requireNonNull(e.getMessage()).contains("idx_event_white_list_certificates_number")) {
                err = "证件号不允许重复";
            }
            return err;
        }
        return null;
    }
}
