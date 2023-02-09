package com.ruoyi.form.controller;

import com.ruoyi.common.annotation.validation.Create;
import com.ruoyi.common.annotation.validation.Update;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.domain.EventWhiteList;
import com.ruoyi.form.service.EventWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/event/white")
public class EventWhiteListController extends BaseController {

    private final String prefix = "event/white";

    @Autowired
    private EventWhiteListService eventWhiteListService;

    @GetMapping("/list")
    public String whiteList() {
        return prefix + "/list";
    }

    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String eventId, ModelMap map)
    {
        EventWhiteList whiteList = eventWhiteListService.getById(eventId);
        map.put("eventWhiteList", whiteList);
        return prefix + "/detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap map) {
        EventWhiteList eventWhiteList = eventWhiteListService.getById(id);
        map.put("eventWhiteList", eventWhiteList);
        return prefix + "/edit";
    }

    @PostMapping("/searchList")
    @ResponseBody
    public TableDataInfo searchList(EventWhiteList eventWhiteList) {
        startPage();
        return getDataTable(eventWhiteListService.searchWhiteListAll(eventWhiteList));
    }

    @PostMapping("/save")
    @ResponseBody
    public AjaxResult save(@Validated({Create.class}) EventWhiteList eventWhiteList, BindingResult result) {
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(item -> {
                stringBuilder.append(item.getField()).append(": ").append(item.getDefaultMessage()).append("\n");
            });
            return AjaxResult.error(stringBuilder.toString());
        }
        if (eventWhiteList.getCertificatesType().equals("1")) {  // 居民身份证
            if (!Pattern.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)", eventWhiteList.getCertificatesNumber())) {
                return AjaxResult.error("身份证号校验错误");
            }
        }
        try {
            return toAjax(eventWhiteListService.save(eventWhiteList));
        } catch (DuplicateKeyException e) {
            String err = "证件号/手机号不允许重复";
            if (Objects.requireNonNull(e.getMessage()).contains("idx_event_white_list_phone")) {
                err = "手机号不允许重复";
            } else if (Objects.requireNonNull(e.getMessage()).contains("idx_event_white_list_certificates_number")) {
                err = "证件号不允许重复";
            }
            return AjaxResult.error(err);
        }
    }

    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated({Update.class}) EventWhiteList eventWhiteList, BindingResult result) {
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(item -> {
                stringBuilder.append(item.getField()).append(": ").append(item.getDefaultMessage()).append("\n");
            });
            return AjaxResult.error(stringBuilder.toString());
        }
        if (eventWhiteList.getCertificatesType().equals("1") && eventWhiteList.getCertificatesNumber() != null && !eventWhiteList.getCertificatesNumber().isEmpty()) {  // 居民身份证
            if (!Pattern.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)", eventWhiteList.getCertificatesNumber())) {
                return AjaxResult.error("身份证号校验错误");
            }
        }
        try {
            return toAjax(eventWhiteListService.updateById(eventWhiteList));
        } catch (DuplicateKeyException e) {
            String err = "证件号/手机号不允许重复";
            if (Objects.requireNonNull(e.getMessage()).contains("idx_event_white_list_phone")) {
                err = "手机号不允许重复";
            } else if (Objects.requireNonNull(e.getMessage()).contains("idx_event_white_list_certificates_number")) {
                err = "证件号不允许重复";
            }
            return AjaxResult.error(err);
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String id) {
        return toAjax(eventWhiteListService.removeById(id));
    }

    @PostMapping("/import")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<EventWhiteList> util = new ExcelUtil<>(EventWhiteList.class);
        List<EventWhiteList> eventWhiteLists = util.importExcel(file.getInputStream());
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < eventWhiteLists.size(); i++) {
            String err = eventWhiteListService.importData(eventWhiteLists.get(i));
            if (err != null) {
                ret.append(String.format("第%d行", i + 1)).append(err);
            }
        }
        if (ret.length() > 0) {
            return AjaxResult.error(ret.toString());
        }
        return AjaxResult.success("导入成功");
    }

    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<EventWhiteList> util = new ExcelUtil<>(EventWhiteList.class);
        return util.importTemplateExcel("白名单导入模板");
    }

    @GetMapping("/listByVendor")
    @ResponseBody
    public AjaxResult listByVendor(@RequestParam("vendor") String vendor) {
        return AjaxResult.success(eventWhiteListService.selectByVendor(vendor));
    }
}
