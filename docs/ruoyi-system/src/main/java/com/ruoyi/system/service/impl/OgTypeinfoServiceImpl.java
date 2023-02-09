package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.OgTypeInfoExcelHeader;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.OgTypeinfoMapper;
import com.ruoyi.system.service.IOgTypeinfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 参数列别Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-06
 */
@Service("ogTypeInfo")
public class OgTypeinfoServiceImpl extends ServiceImpl<OgTypeinfoMapper, OgTypeinfo> implements IOgTypeinfoService {
    @Resource
    private OgTypeinfoMapper ogTypeinfoMapper;

    private final String SUCCESS = "1";// 启用状态

    /**
     * 查询参数列别
     *
     * @param typeinfoId 参数列别ID
     * @return 参数列别
     */
    @Override
    public OgTypeinfo selectOgTypeinfoById(String typeinfoId) {
        // cuicc
        //return ogTypeinfoMapper.selectOgTypeinfoById(typeinfoId);
        return ogTypeinfoMapper.selectOgTypeinfoById2(typeinfoId);
    }

    /**
     * 查询参数列别
     *
     * @param parentId 父参数ID
     * @return 参数列别
     */
    @Override
    public OgTypeinfo selectOgTypeinfoByParentId(String parentId) {
        return ogTypeinfoMapper.selectOgTypeinfoByParentId(parentId);
    }

    /**
     * 查询参数列别列表
     *
     * @param ogTypeinfo 参数列别
     * @return 参数列别
     */
    @Override
    public List<OgTypeinfo> selectOgTypeinfoList(OgTypeinfo ogTypeinfo) {
        return ogTypeinfoMapper.selectOgTypeinfoList(ogTypeinfo);
    }

    /**
     * 新增参数列别
     *
     * @param ogTypeinfo 参数列别
     * @return 结果
     */
    @Override
    public int insertOgTypeinfo(OgTypeinfo ogTypeinfo) {
        // 赋值主键id
        ogTypeinfo.setParentId(ogTypeinfo.getTypeinfoId());
        ogTypeinfo.setTypeinfoId(UUID.getUUIDStr());
        ogTypeinfo.setInvalidationMark("1");// 默认正常
        return ogTypeinfoMapper.insertOgTypeinfo(ogTypeinfo);
    }

    /**
     * 修改参数列别
     *
     * @param ogTypeinfo 参数列别
     * @return 结果
     */
    @Override
    public int updateOgTypeinfo(OgTypeinfo ogTypeinfo) {
        ogTypeinfo.setUpdateTime(DateUtils.getDate());
        return ogTypeinfoMapper.updateOgTypeinfo(ogTypeinfo);
    }

    /**
     * 删除参数列别对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgTypeinfoByIds(String ids) {
        return ogTypeinfoMapper.deleteOgTypeinfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除参数列别信息
     *
     * @param typeinfoId 参数列别ID
     * @return 结果
     */
    @Override
    public int deleteOgTypeinfoById(String typeinfoId) {
        return ogTypeinfoMapper.deleteOgTypeinfoById(typeinfoId);
    }

    /**
     * 查询参数树
     *
     * @param info
     * @return 结果
     */
    @Override
    public List<ZtreeStr> selectTypeinfoTree(OgTypeinfo info) {
        List<OgTypeinfo> infos = ogTypeinfoMapper.selectTypeinfoTree(info);
        return initZtreeStr(infos);
    }

    @Override
    public List<OgTypeinfo> selectCmbizType() {
        List<OgTypeinfo> list = ogTypeinfoMapper.selectCmbizType();
        return list;
    }

    public List<Ztree> initZtree(List<OgTypeinfo> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (OgTypeinfo dept : deptList) {
            if (SUCCESS.equals(dept.getInvalidationMark())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getTypeinfoId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getTypeName());
                ztree.setTitle(dept.getTypeName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getTypeinfoId() + dept.getTypeName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    public List<Ztree> initZtree(List<OgTypeinfo> list) {
        return initZtree(list, null);
    }

    @Override
    public List<Ztree> selectTypeTreeExcludeChild(OgTypeinfo ogTypeinfo) {
        List<OgTypeinfo> deptList = ogTypeinfoMapper.selectOgTypeinfoList(ogTypeinfo);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importOgTypeData(List<OgTypeInfoExcelHeader> list, String typeinfoId) {
        //先校验表格重复数据 有相同给出提示 code相同，名称不同
        if (!CollectionUtils.isEmpty(list)) {
            HashMap<String, Integer> map = new HashMap<>();
            list.forEach(ogTypeInfoExcelHeader -> {
                String typeNo = ogTypeInfoExcelHeader.getTypeNo();
                map.put(typeNo, (map.get(typeNo) == null ? 0 : map.get(typeNo)) + 1);
            });
            String message = "";
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String s = entry.getKey();
                Integer integer = entry.getValue();
                if (integer > 1) {
                    message = message + "," + s;
                }
            }
            if (StringUtils.isNotEmpty(message)) {
                throw new BusinessException("导入数据存在重复类别编号：" + message + ", 请修改后重新上传");
            }

        }
        List<String> typeNos = list.stream().map(OgTypeInfoExcelHeader::getTypeNo).collect(Collectors.toList());
        QueryWrapper<OgTypeinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", typeinfoId).in("type_no", typeNos);
        List<OgTypeinfo> ogTypeinfos = ogTypeinfoMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(ogTypeinfos)) {

            //存在数据相同code
            String codes = ogTypeinfos.stream().map(OgTypeinfo::getTypeNo).collect(Collectors.joining(","));
            throw new BusinessException("系统已存在类别编号:" + codes + "请修改后重新上传");
        }

        //校验完毕，导入数据库
        OgTypeinfo typeinfo = ogTypeinfoMapper.selectOgTypeinfoById(typeinfoId);
         ArrayList<OgTypeinfo> ogTypeinfos1 = new ArrayList<>();
        list.forEach(ogTypeInfoExcelHeader -> {
            OgTypeinfo ogTypeinfo = new OgTypeinfo();
            ogTypeinfo.setTypeinfoId(UUID.getUUIDStr());
            ogTypeinfo.setTypeTypeName(typeinfo.getTypeTypeName());
            ogTypeinfo.setTypeTypeNo(typeinfo.getTypeTypeNo());
            ogTypeinfo.setInvalidationMark("1");// 默认正常
            ogTypeinfo.setParentId(typeinfoId);
            ogTypeinfo.setTypeName(ogTypeInfoExcelHeader.getTypeName());
            ogTypeinfo.setTypeNo(ogTypeInfoExcelHeader.getTypeNo());
            ogTypeinfos1.add(ogTypeinfo);
        });
        this.saveBatch(ogTypeinfos1);
    }

    /**
     * 数据验证
     */
//    private void verifyData(List<OgTypeInfoExcelHeader> list) {
//        if (!CollectionUtils.isEmpty(list)) {
//            list.forEach(ogTypeInfoExcelHeader -> {
//                 OgTypeInfoExcelHeader ogTypeInfoContainChildren = this.setOgTypeInfoExcelHeaderValue(ogTypeInfoExcelHeader);
//
//            });
//        }
//
//    }

    /**
     * 变成层级关系
     * @param header
     */
//    private OgTypeInfoExcelHeader setOgTypeInfoExcelHeaderValue(OgTypeInfoExcelHeader header) {
//         OgTypeInfoExcelHeader two = new OgTypeInfoExcelHeader();
//         two.setOne(header.getTwo());
//         two.setOneCode(header.getTwoCode());
//         header.setChildren(two);
//
//        OgTypeInfoExcelHeader three = new OgTypeInfoExcelHeader();
//        System.err.println(two==three);
//        three.setOne(header.getThree());
//        three.setOneCode(header.getThreeCode());
//        two.setChildren(three);
//
//        OgTypeInfoExcelHeader four = new OgTypeInfoExcelHeader();
//        four.setOne(header.getFour());
//        four.setOneCode(header.getFourCode());
//        three.setChildren(four);
//
//        OgTypeInfoExcelHeader five = new OgTypeInfoExcelHeader();
//        five.setOne(header.getFive());
//        five.setOneCode(header.getFiveCode());
//        four.setChildren(five);
//        return header;
//    }

    /**
     * 根据字段查询ogTypeInfo
     *
     * @return
     */

//    private Boolean verifyData(OgTypeInfoExcelHeader ogTypeInfoExcelHeader, String parentId) {
//        List<OgTypeinfo> ogTypeinfos = getOgTypeInfoByColumn(ogTypeInfoExcelHeader.getOneCode(), parentId);
//        if (!CollectionUtils.isEmpty(ogTypeinfos)) {
//            List<String> list = ogTypeinfos.stream().map(OgTypeinfo::getTypeName).collect(Collectors.toList());
//            if (list.contains(ogTypeInfoExcelHeader.getOne())) {
//                //处理下一场
//                //verifyData(ogTypeInfoExcelHeader.getChildren(),);
//
//            } else {
//                return false;
//            }
//        } else {
//            //新增
//            OgTypeinfo ogTypeinfo = new OgTypeinfo();
//            // 赋值主键id
//            ogTypeinfo.setTypeinfoId(UUID.getUUIDStr());
//            ogTypeinfo.setInvalidationMark("1");// 默认正常
//            ogTypeinfo.setTypeNo(ogTypeInfoExcelHeader.getOneCode());
//            ogTypeinfo.setTypeName(ogTypeInfoExcelHeader.getOne());
//            ogTypeinfoMapper.insert(ogTypeinfo);
//            System.err.println(ogTypeinfo);
//        }
//
//        return null;
//
//    }

    /**
     * 根据typeNO和parentId获取OgTypeInfo
     *
     * @param typeNo
     * @param parentId
     * @return
     */
    private List<OgTypeinfo> getOgTypeInfoByColumn(String typeNo, String parentId) {
        QueryWrapper<OgTypeinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("type_no", typeNo).eq("parent_id", parentId);
        List<OgTypeinfo> ogTypeinfos = ogTypeinfoMapper.selectList(wrapper);
        return ogTypeinfos;
    }

    /**
     * 对象转参数树
     *
     * @param typeinfoList 参数列表
     * @return 树结构列表
     */
    public List<ZtreeStr> initZtreeStr(List<OgTypeinfo> typeinfoList) {

        List<ZtreeStr> ztrees = new ArrayList<ZtreeStr>();
        for (OgTypeinfo info : typeinfoList) {
            if ("1".equals(info.getInvalidationMark())) {
                ZtreeStr ztreeStr = new ZtreeStr();
                ztreeStr.setId(info.getTypeinfoId());
                ztreeStr.setpId(info.getParentId());
                ztreeStr.setName(info.getTypeName());
                ztreeStr.setTitle(info.getTypeName());
                ztrees.add(ztreeStr);
            }
        }
        return ztrees;
    }

    /**
     * 根据类型编号和层级取值
     * 事件单取一级分类时typeNo传最大的分类编号
     * @param typeNo 类别编号
     * @param level 事件单级别
     * @return
     */
    public List<OgTypeinfo> selectOgTypeInfoByEvent(String typeNo, String level) {
        QueryWrapper<OgTypeinfo> queryWrapper = new QueryWrapper<>();
        String inSql = "";
        if("1".equals(level)) {
            inSql = "SELECT typeinfo_id FROM og_typeinfo WHERE type_type_no=? AND parent_id IS NULL";
            inSql = inSql.replace("?", "'" + typeNo + "'");
        } else {
            inSql = "SELECT typeinfo_id FROM og_typeinfo WHERE type_no=? AND parent_id IS NOT NULL";
            inSql = inSql.replace("?", "'" + typeNo + "'");
        }
        queryWrapper.inSql("parent_id", inSql).orderByAsc("serial");
        List<OgTypeinfo> list = ogTypeinfoMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public OgTypeinfo selectOgTypeInfoByTypeNo(String typeNo) {
        QueryWrapper<OgTypeinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_no", typeNo);
        return ogTypeinfoMapper.selectOne(queryWrapper);
    }
    @Override
    public List<OgTypeinfo> selectOgTypeInfoByTypeNoForProblem(String typeNo) {
        QueryWrapper<OgTypeinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_no", typeNo);
        return ogTypeinfoMapper.selectList(queryWrapper);
    }
    @Override
    public List<OgTypeinfo> selectCurrentOgTypeInfoList(String typeTypeNo, String typeNo) {
        List<OgTypeinfo> list = new ArrayList<>();
        QueryWrapper<OgTypeinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_type_no", typeTypeNo);
        queryWrapper.eq("type_no", typeNo);
        OgTypeinfo ogTypeinfo = ogTypeinfoMapper.selectOne(queryWrapper);
        if(ogTypeinfo != null) {
            list = ogTypeinfoMapper.selectList(new QueryWrapper<OgTypeinfo>().eq("parent_id", ogTypeinfo.getTypeinfoId()));
        }
        return list;
    }
}
