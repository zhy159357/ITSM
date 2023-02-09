package com.ruoyi.form.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalValidExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleValidException(MethodArgumentNotValidException e){
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return AjaxResult.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
}
