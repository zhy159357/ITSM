package com.ruoyi.web.controller.system;
import com.ruoyi.activiti.service.EndTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 流程结束
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/task")
public class EndTaskController extends BaseController {

    private String prefix = "system/taskpage";

    @Autowired
    private EndTaskService endTaskService;


    /**
     * 结束流程页面
     * @return
     */
    @GetMapping()
    public String taskList(){
        return prefix+"/task";
    }

    /**
     * 结束流程列表数据
     * @param type
     * @param number
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String type,String number){

        return endTaskService.getEndPagerTasks(type,number);

    }


    /**
     * 单子任务强制删除
     * @param id
     * @param type
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String id,String type){
        try {
            endTaskService.delete(id,type);
        } catch (Exception e) {
           return error("任务删除失败!");
        }
        return success("任务删除成功");
    }


    /**
     * 单子任务强制关闭
     * @param id
     * @param type
     * @return
     */
    @PostMapping("/close")
    @ResponseBody
    public AjaxResult close(String id,String type){
        try {
            endTaskService.close(id,type);
        } catch (Exception e) {
            return error("任务关闭失败!");
        }
        return success("任务关闭成功");
    }


}
