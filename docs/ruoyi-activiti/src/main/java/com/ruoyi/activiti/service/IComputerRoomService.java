package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.ComputerModule;
import com.ruoyi.activiti.domain.ComputerRoom;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;

import java.util.List;
import java.util.Map;

public interface IComputerRoomService {

    /**
     * 查询动力处下人员
     * @param person
     * @return
     */
    List<OgPerson> selectUserList(OgPerson person);

    /**
     * 获取工作内容复选框
     * @return
     */
    String getContentMap();

    /**
     * 获取工作内容
     * @return
     */
    List<ComputerRoom> workContent();

    /**
     * 获取当前用户岗位状态
     * @param userId
     * @return
     */
    String getStatusByUserId(String userId);

    /**
     * 获取当前登录人员所处在的机构的所有人员
     * @param userId
     * @return
     */
    List<OgPerson> getOgPersonList(String userId);

    /**
     * 保存机房出入申请
     * @param computerRoom
     * @return
     */
    int insertComputerRoomApply(ComputerRoom computerRoom);

    /**
     * 查询机房进入申请列表
     * @param computerRoom
     * @return
     */
    List<ComputerRoom> computerRoomApplylist(ComputerRoom computerRoom);

    /**
     * 根据ID查出对应机房出入申请
     * @param id
     * @return
     */
    ComputerRoom selectComputerRoomApplyById(String id);

    /**
     * 修改保存机房进出申请
     * @param computerRoom
     * @return
     */
    int upadateComputerRoomApply(ComputerRoom computerRoom);

    /**
     * 查询审核进出机房申请列表
     * @param computerRoom
     * @return
     */
    List<ComputerRoom> selectComputerRoomAuditlist(ComputerRoom computerRoom);

    /**
     * 更新机房申请审批状态
     * @param computerRoom
     * @return
     */
    int updateComputerRoomApplyState(ComputerRoom computerRoom);

    /**
     * 查询登记列表
     * @param computerRoom
     * @return
     */
    List<ComputerRoom> selectComputerRoomRegisterList(ComputerRoom computerRoom);

    /**
     * 机房出入登记保存修改
     * @param computerRoom
     * @return
     */
    int updateComputerRoomRegister(ComputerRoom computerRoom);

    List<Map> getIntoList(ComputerRoom computerRoom);

    List<Map> getBelongingsList(ComputerRoom computerRoom);

    //查询机房模块分类树列表
    List<Ztree> selectTree(ComputerModule computerModule);

    //查询机房模块分类下对应的子分类列表
    List<ComputerModule> selectComputerModuleList(ComputerModule cm);

    //根据Id查询机房模块列表
    ComputerModule selectComputerModuleById(String id);

    //保存分类
    int insertComputerModule(ComputerModule computerModule);
    //更新分类
    int updateComputerModule(ComputerModule cm);

    //查询机房中心一级分类
    List<ComputerModule> selectComputerModuleCenter();
    //根据机房中心查询所属模块列表
    List<ComputerModule> selectComputerModuleForCenter(ComputerModule computerModule);
    //暂存
    int insertTsComputerRoomApply(ComputerRoom computerRoom);

    //申请信息与陪同信息联动
    Map<String, List> getApplyForRegister(ComputerRoom computerRoom);

    //实际进入携带物品和数量
    List<Map> getRealityBelongingsList(ComputerRoom computerRoom);

    List<OgUser> selectAllocatedListPost(OgUser user);
}
