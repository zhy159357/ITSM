package com.ruoyi.es.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.es.domain.EsUserBean;
import com.ruoyi.es.domain.UserBean;
import com.ruoyi.es.service.EsService;
import com.ruoyi.es.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author dayong_sun
 * @version 1.0
 */
@Controller
@RequestMapping("knowledge/search")
public class SearchController extends BaseController {

    private String prefix = "knowledge/search";

    @Autowired
    UserService userService;

    @Autowired
    EsService esService;

    @GetMapping("/list")
    public String list(ModelMap mmap,Map<String,String> searchMap){
        List<EsUserBean> list = userService.search(searchMap);
        mmap.put("list", list);
        return prefix +"/search";
    }
    @RequestMapping("/add")
    public String add(Model view){
        return "/es/add";
    }
    @RequestMapping("/update")
    public String update(Model view, String id){
        if(null == id){
            return "index";
        }
        view.addAttribute("id",id);
        return prefix +"/update";
    }

    @ResponseBody
    @PostMapping("/selectAll")
    public TableDataInfo selectAll(){
        startPage();
        List<UserBean> userBeans = userService.selectUserBeanList(new UserBean());
        return getDataTable(userBeans);
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        UserBean ub = new UserBean();
        ub.setId(userId.toString());
        mmap.put("user", userService.selectUserBeanList(ub).get(0));
        return "edit";
    }

    /**
     * @PathVariable用于告诉Spring URI路径的一部分是您希望传递给方法的值。这是你想要的，还是应该将形式数据的变量发布到URI？
     *
     * 如果您想要表单数据，请使用@RequestParam而不是@PathVariable。
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public List<EsUserBean> search(@RequestParam Map<String,String> searchMap){
        return userService.search(searchMap);
    }

    @GetMapping("/getMess/{id}")
    @ResponseBody
    public List<UserBean> getMess(@PathVariable("id") String id){
        UserBean ub = new UserBean();
        ub.setId(id);
        List<UserBean> userBeans = userService.selectUserBeanList(ub);
        return userBeans;
    }

    @PostMapping("/updateSave")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateSave(@RequestParam("id")String id, @RequestParam("name")String name, @RequestParam("age")String age, @RequestParam("phone")String phone, @RequestParam("remark")String remark){
        //修改mysql数据库
        UserBean ub = new UserBean();
        ub.setId(id);
        ub.setName(name);
        ub.setAge(Integer.parseInt(age));
        ub.setPhone(phone);
        ub.setRemark(remark);
        int row = userService.updateById(ub);

        //修改es
        EsUserBean esUserBean = esService.queryEmployeeById(id);
        esUserBean.setId(id);
        esUserBean.setName(name);
        esUserBean.setAge(Integer.parseInt(age));
        esUserBean.setPhone(phone);
        esUserBean.setRemark(remark);
        EsUserBean save = esService.save(esUserBean);
        System.out.println(save);

        /*BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.matchPhraseQuery("id",id));
        Page<EsUserBean> search = (Page<EsUserBean>)esService.search(builder);
        List<EsUserBean> content = search.getContent();
        content.get(0).setId(Integer.parseInt(id));
        content.get(0).setName(name);
        content.get(0).setAge(Integer.parseInt(age));
        content.get(0).setPhone(phone);
        content.get(0).setRemark(remark);
        esService.save(content.get(0));*/


       /* Optional<EsUserBean> byId = esService.findById(Integer.parseInt(id));
        byId.get().setId(Integer.parseInt(id));
        byId.get().setName(name);
        byId.get().setAge(Integer.parseInt(age));
        byId.get().setPhone(phone);
        byId.get().setRemark(remark);
        esService.save(byId.get());*/


        /*Iterable<EsUserBean> id1 = esService.search(new MatchQueryBuilder("id", id));
        Iterator<EsUserBean> iterator = id1.iterator();
        while (iterator.hasNext()){
            EsUserBean next = iterator.next();
            System.out.println(next);
        }*/
        if(row == 1 && save!=null){
            return toAjax(row);
        }else{
            return toAjax(0);
        }
    }

    @PostMapping("/addSave")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(UserBean ub){
        //向oracle添加数据
        ub.setCreateTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        int row = userService.save(ub);
        //向es添加数据
        EsUserBean eub = new EsUserBean();
        eub.setId(ub.getId()); //确保es mysql 的id一样
        eub.setName(ub.getName());
        eub.setPassword(ub.getName());
        eub.setAge(ub.getAge());
        eub.setPhone(ub.getPhone());
        eub.setRemark(ub.getRemark());
        eub.setCreateTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        EsUserBean save = esService.save(eub);
        if(row==1 && save!=null){
            return toAjax(row);
        }else{
            return toAjax(0);
        }
    }


    @PostMapping("/remove")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteById(@RequestParam("ids") String id){
        try {
            //删除mysql 数据库
            int row = userService.deleteById(id);
            //删除es
            EsUserBean esUserBean = esService.queryEmployeeById(id);
            esService.delete(esUserBean);
            if(row==1){
                return toAjax(row);
            }else{
                return toAjax(0);
            }
        }catch (Exception e) {
            return error(e.getMessage());
        }

    }

}
