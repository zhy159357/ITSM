package com.ruoyi.activiti.bmp.service.impl;

import com.ruoyi.activiti.bmp.service.IAcitivtiConfigService;
import com.ruoyi.common.annotation.ActivitiListener;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivitiConfigServiceImpl implements IAcitivtiConfigService, ApplicationContextAware {

    @Autowired
    private IdentityService identityService;

    private static final Logger log = LoggerFactory.getLogger(ActivitiConfigServiceImpl.class);

    private List<Map<String, Object>> listeners;

    @Override
    public List<Map<String, Object>> getActivityListeners(String listenerName) {
        List<Map<String, Object>> listeners = this.listeners;
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (StringUtils.isNotEmpty(listenerName)) {
            for (Map<String, Object> map : listeners) {
                // 监听器名称不为空支持模糊搜索
                if (map.get("listenerName").toString().contains(listenerName)) {
                    resultList.add(map);
                }
            }
        } else {
            resultList = listeners;
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> selectActivityUsers(Map<String, Object> params) {
        UserQuery userQuery = identityService.createUserQuery();
        List<User> list = userQuery.list();
        List<Map<String, Object>> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            result = list.stream().map(user -> {
                Map<String, Object> map = new HashMap<>();
                map.put("pId", user.getId());
                map.put("username", user.getFirstName());
                map.put("pName", user.getLastName());
                map.put("email", user.getEmail());
                return map;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectActivityGroups(Map<String, Object> params) {
        GroupQuery groupQuery = identityService.createGroupQuery();
        List<Group> list = groupQuery.list();
        List<Map<String, Object>> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            result = list.stream().map(group -> {
                Map<String, Object> map = new HashMap<>();
                map.put("groupId", group.getId());
                map.put("grpName", group.getName());
                return map;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public String selectUserGroupByIds(String ids, Integer type) {
        String name = "";
        if(StringUtils.isEmpty(ids)) {
            return name;
        }
        String[] idArr = StringUtils.split(ids, ",");
        if (type == 0 || type == 1) {
            UserQuery userQuery = identityService.createUserQuery();
            if (idArr.length == 1) {
                // 数组长度是1，表明是代理人id，直接去查询
                User user = userQuery.userId(ids).singleResult();
                name = user == null ? "" : user.getFirstName();
            } else {
                // 数组长度不是1，表明可能是候选人，存在多个需要查询名称去拼接
                List<String> usernameList = Arrays.asList(idArr).stream().map(userId -> {
                    User user = userQuery.userId(userId).singleResult();
                    return user == null ? "" : user.getFirstName();
                }).collect(Collectors.toList());
                name = StringUtils.join(usernameList, ",");
            }
        } else if (type == 2) {
            // 此处是候选组，可能存在多个，该处查询组名称去拼接
            GroupQuery groupQuery = identityService.createGroupQuery();
            List<String> groupNameList = Arrays.asList(idArr).stream().map(groupId -> {
                Group group = groupQuery.groupId(groupId).singleResult();
                return group == null ? "" : group.getName();
            }).collect(Collectors.toList());
            name = StringUtils.join(groupNameList, ",");
        }
        return name;
    }

    @Override
    public Map<String, String> showUserGroupName(String assignee, String candidateUser, String candidateGroup) {
        Map<String, String> map = new HashMap<>();
        String assigneeName = "";
        if(StringUtils.isNotEmpty(assignee)) {
            UserQuery userQuery = identityService.createUserQuery();
            User user = userQuery.userId(assignee).singleResult();
            assigneeName = user == null ? "" : user.getFirstName();
        }
        String candidateUserName = "";
        if(StringUtils.isNotEmpty(candidateUser)) {
            UserQuery userQuery = identityService.createUserQuery();
            String[] userIdArr = StringUtils.split(candidateUser, ",");
            List<String> collect = Arrays.asList(userIdArr).stream().map(id -> {
                User user = userQuery.userId(id).singleResult();
                return user == null ? "" : user.getFirstName();
            }).collect(Collectors.toList());
            candidateUserName = StringUtils.join(collect, ",");
        }
        String candidateGroupName = "";
        if(StringUtils.isNotEmpty(candidateGroup)) {
            GroupQuery groupQuery = identityService.createGroupQuery();
            String[] groupIdArr = StringUtils.split(candidateGroup, ",");
            List<String> collect = Arrays.asList(groupIdArr).stream().map(id -> {
                Group group = groupQuery.groupId(id).singleResult();
                return group == null ? "" : group.getName();
            }).collect(Collectors.toList());
            candidateGroupName = StringUtils.join(collect, ",");
        }
        map.put("assigneeName", assigneeName);
        map.put("candidateUserName", candidateUserName);
        map.put("candidateGroupName", candidateGroupName);
        return map;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.listeners = this.getAllListeners(applicationContext);
    }

    private List<Map<String, Object>> getAllListeners(ApplicationContext applicationContext) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> listenerMap = applicationContext.getBeansWithAnnotation(ActivitiListener.class);
        for (String beanName : listenerMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            Object obj = listenerMap.get(beanName);
            // 动态获取监听器注解的属性
            ActivitiListener listener = obj.getClass().getAnnotation(ActivitiListener.class);
            if (listener != null) {
                // 监听器名称
                map.put("listenerName", listener.listenerName());
                // 监听器类型
                map.put("listenerType", listener.listenerType().getName());
                // 监听器所属业务类型
                map.put("businessTypeName", listener.listenerBusinessType().getBusinessName());
            }
            // 获取注入spring容器的bean名称 可能是@Component或@Service注解  封装委托表达式格式：${"注入spring容器的bean名称"}
            Component component = obj.getClass().getAnnotation(Component.class);
            if (component != null) {
                map.put("delegateExpression", "${" + component.value() + "}");
            } else {
                Service service = obj.getClass().getAnnotation(Service.class);
                if (service != null)
                    map.put("delegateExpression", "${" + service.value() + "}");
            }
            // 如果监听器名称或委托表达式为空，默认不返回
            if (StringUtils.isEmpty(map.get("listenerName")) || StringUtils.isEmpty(map.get("delegateExpression"))) {
                continue;
            }
            // 模拟给map中村一个唯一性id
            map.put("listenerId", UUID.getUUIDStr());
            resultList.add(map);
        }
        log.info("activiti流程监听器,resultList:{}", resultList);
        return resultList;
    }
}
