package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysPubFolder;
import com.ruoyi.common.core.domain.entity.SysUpdown;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysUpdownMapper;
import com.ruoyi.system.service.ISysUpdownService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 上传下载业务层处理
 * @author Mr.Xy
 */
@Service
public class SysUpdownServiceImpl implements ISysUpdownService
{
    @Autowired
    private SysUpdownMapper updownMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    @Override
    public List<SysUpdown> selectUpdownList(SysUpdown updown) {

        if("mysql".equals(dbType)){
            return updownMapper.selectUpdownMysqlList(updown);
        }else{
            return updownMapper.selectUpdownList(updown);
        }

    }

    private static final Logger log = LoggerFactory.getLogger(SysUpdownServiceImpl.class);

    /**
     * 查询管理树
     * @param folder 信息
     */
    @Override
    public List<Ztree> selectFolderTree(SysPubFolder folder)
    {
        List<SysPubFolder> folderList = updownMapper.selectFolderTree(folder);
        List<Ztree> ztrees = initZtree(folderList);
        return ztrees;
    }
    @Override
    public int insertUpdown(SysUpdown updown){
        return updownMapper.insertUpdown(updown);
    }

    @Override
    public int updateUpdown(SysUpdown updown) {
        return updownMapper.updateUpdown(updown);
    }

    @Override
    public int deleteUpdownByIds(String ids) {
        return updownMapper.deleteUpdownByIds(Convert.toStrArray(ids));
    }
    @Override
    public SysUpdown selectUpdownById(String id_)
    {
        return updownMapper.selectUpdownById(id_);
    }
    /**
     * 查询是否存在子节点
     *
     * @param updown
     * @return
     */
    public List<SysUpdown> selectList(SysUpdown updown) {
        return updownMapper.selectList(updown);
    }

    /**
     * 根据文件名和创建时间模糊数据
     * @param updown
     * @return
     */
    public List<SysUpdown> selectListTime(SysUpdown updown) {
        return updownMapper.selectUpdListTime(updown);
    }

    /**
     * 定是删除附件任务
     * @return
     */
    @Override
    public void selectUpdownByTime() {
        log.info("---------------------------------------定时任务执行开始-------------------------------------");
        List<SysUpdown> list=updownMapper.selectUpdList();
        for (SysUpdown upd:list) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
            //一天的毫秒数
            long nd = 1000 * 60 * 60 * 24;//
            //获得两个时间的时间差异
            try {
                String time = DateUtils.dateTimeNow();
                Date d1 = sd.parse(time);
                Date d2 = sd.parse(upd.getCreate_time_());
                long diff =d1.getTime()-d2.getTime();
                long days =diff/nd;
                if(days>=40){
                    updownMapper.deleteUpdownByTime(upd.getId_());
                }
            } catch (ParseException e) {
                System.out.println("日期转换失败");
            }
        }
        log.info("####################################定时任务执行结束#####################################");
    }



    /**
     * 对象转树
     * @param folderList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysPubFolder> folderList)
    {
        return initZtree(folderList, null);
    }

    public List<Ztree> initZtree(List<SysPubFolder> folderList, List<String> roleDeptList)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (SysPubFolder folder : folderList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(folder.getId_());
            ztree.setpId(folder.getParent_());
            ztree.setName(folder.getName_());
            ztree.setTitle(folder.getName_());
            ztree.setOrgs(folder.getOrgs_());
            ztree.setCreateUser(folder.getCreate_user_());
            if (isCheck)
            {
                ztree.setChecked(roleDeptList.contains(folder.getId_() + folder.getName_()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }
}