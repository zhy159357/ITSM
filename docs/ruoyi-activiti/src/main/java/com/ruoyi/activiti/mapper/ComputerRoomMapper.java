package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.ComputerModule;
import com.ruoyi.activiti.domain.ComputerRoom;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.es.domain.KnowledgeTitle;

import java.util.List;

public interface ComputerRoomMapper {

    List<OgPerson> selectUserList(OgPerson person);

    List<ComputerRoom> selectPubParaValueByParaName(String content_type);

    int insertComputerRoomApply(ComputerRoom computerRoom);

    List<ComputerRoom> computerRoomApplylist(ComputerRoom computerRoom);

    List<ComputerRoom> computerRoomApplylistMysql(ComputerRoom computerRoom);

    ComputerRoom selectComputerRoomApplyById(String id);

    int upadateComputerRoomApply(ComputerRoom computerRoom);

    List<ComputerRoom> selectComputerRoomAuditlist(ComputerRoom computerRoom);

    List<ComputerRoom> selectComputerRoomAuditlistMysql(ComputerRoom computerRoom);

    int updateComputerRoomApplyState(ComputerRoom computerRoom);

    List<ComputerRoom> selectComputerRoomRegisterList(ComputerRoom computerRoom);

    List<ComputerRoom> selectComputerRoomRegisterListMysql(ComputerRoom computerRoom);

    List<ComputerModule> selectTree(ComputerModule computerModule);

    List<ComputerModule> selectComputerModuleList(ComputerModule cm);

    List<ComputerModule> selectComputerModuleMysqlList(ComputerModule cm);

    ComputerModule selectComputerModuleById(String id);

    int insertComputerModule(ComputerModule computerModule);

    int updateComputerModule(ComputerModule cm);

    List<ComputerModule> selectComputerModuleCenter();

    List<ComputerModule> selectComputerModuleForCenter(ComputerModule computerModule);

    int updateComputerRoomRegister(ComputerRoom computerRoom);

    List<OgUser> selectAllocatedListPost(OgUser user);
}
