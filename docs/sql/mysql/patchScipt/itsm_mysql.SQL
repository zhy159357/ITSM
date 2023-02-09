-- 参数表pub_para添加新字段自定义表单是否可用
alter table pub_para
    add column enable varchar(1) default '0' comment '自定义表单是否启用【1：启用，0：禁用】' after state;

-- 2022-06-24 新增pub_para表数据 是否为自定义表单配置项
insert into pub_para (para_id, para_name, para_explain, state)
values ('a0d8f1ebacad4400baa83797be45b226', 'paraEnable', '自定义表单配置', '1');
insert into pub_para (para_id, para_name, para_explain, state)
values ('1dbdf48f14524e08969b8a494bdf8075', 'formSearchCondition', '表单搜索条件', '1');
-- 2022-06-24 新增pub_paravalue表数据 是否字典项以及表单列表搜索条件下拉数据
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('1d2e8ed8862248a09d26978ec223f33b', 'a0d8f1ebacad4400baa83797be45b226', '1', '是', 1);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('ee49c1f18af74c1d9afec7613233a8f8', 'a0d8f1ebacad4400baa83797be45b226', '0', '否', 1);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('ad47f984f6e84ad5bf82808cb1501592', '1dbdf48f14524e08969b8a494bdf8075', 'eq', '等于', 1);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('c706bca6f23e487daef83cb2e74c6dad', '1dbdf48f14524e08969b8a494bdf8075', 'ne', '不等于', 2);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('ea846b43361b409ea1dd1957a74516f4', '1dbdf48f14524e08969b8a494bdf8075', 'like', '相似', 3);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('17ab1ad344914d79844d1eb1b3f9e698', '1dbdf48f14524e08969b8a494bdf8075', 'gt', '大于', 4);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('b9d71d766d85460a840a011a128e1091', '1dbdf48f14524e08969b8a494bdf8075', 'ge', '大于等于', 5);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('c1f9181f77e7419b8182b4f67d8aefe3', '1dbdf48f14524e08969b8a494bdf8075', 'lt', '小于', 5);
insert into pub_paravalue (para_value_id, para_id, value, value_detail, serail)
values ('7508e206b0604ba6b466513227f96754', '1dbdf48f14524e08969b8a494bdf8075', 'le', '小于等于', 6);


-- 2022-07-04 新增pub_para表数据 优先级，影响程度，影响范围
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
VALUES ('40288a1e276eeafa01276f11e0e7001f', 'event_priority', '优先级', '1', '0', '0');
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('e70ca60e90d14dccbef5d84cb5f5bb9f', 'event_impact_degree', '事件影响程度', '1', '0', NULL);
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('fd7017bd1790444caf32d7104051b120', 'event_impact_scope', '事件影响范围', '1', '0', NULL);

-- 2022-07-04 新增pub_paravalue表数据 优先级子集，影响程度子集，影响范围子集

insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
VALUES ('40288a1e276eeafa01276f1230d70020', '40288a1e276eeafa01276f11e0e7001f', 'null', '空', 1);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
VALUES ('40288a1e276eeafa01276f1255a50021', '40288a1e276eeafa01276f11e0e7001f', 'p1', 'P1', 2);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
VALUES ('40288a1e276eeafa01276f127f8e0022', '40288a1e276eeafa01276f11e0e7001f', 'p2', 'P2', 3);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
VALUES ('57c35529b76f4a638d35bf1c42d08007', '40288a1e276eeafa01276f11e0e7001f', 'p3', 'P3', 3);

insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('2923dfaefec94e3f8b59b64594c6b2f5', 'e70ca60e90d14dccbef5d84cb5f5bb9f', 'yuqing', '舆情', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('b1b081b753c74c6594c965160594666f', 'e70ca60e90d14dccbef5d84cb5f5bb9f', 'zhuanzhang', '转账交易', '1');

insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('83013b9d9c56471caba8803b0ba7f5a4', 'fd7017bd1790444caf32d7104051b120', 'quanhang', '全行', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('85769795e9ab4ee88b89462b46554b94', 'fd7017bd1790444caf32d7104051b120', 'zhihang', '支行', '2');

-- 2022-07-04 新增sys_menu表数据 前端页面参数管理 子集 优先级菜单

insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2000128, '优先级设置', 2040, 1, '/system/dict/priorityList', 'menuItem', 'C', '0', '', '#', 'admin',
        '2022-07-02 14:52:20', 'admin', '2022-07-04 11:31:05', '');

-- 2022-07-04 新增字典数据
-- 事件单状态
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('207e128f16944fb799a2255da21c94dc', 'event_status', '事件单状态', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('0e613c860ef24758b2ddf800e5b731b5', '207e128f16944fb799a2255da21c94dc', '6', '已关闭', '6');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('0fbccba2b96e47f7a0de47547ac53822', '207e128f16944fb799a2255da21c94dc', '9', '已评价', '9');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('329d056c219f4f4a958042c35137d23a', '207e128f16944fb799a2255da21c94dc', '5', '待关闭', '5');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('352f6213772748c9beaadc08324b7b38', '207e128f16944fb799a2255da21c94dc', '8', '待补全', '8');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('47e22d702c8d4a6ca33152450f799b9c', '207e128f16944fb799a2255da21c94dc', '4', '待解决', '4');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('97b09eabb09243eca999d223b06ce648', '207e128f16944fb799a2255da21c94dc', '1', '待提交', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('99c50fb22c194d5b9d279643b7df0283', '207e128f16944fb799a2255da21c94dc', '7', '已撤销', '7');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('cec7f267b3ca4bbdabf1fb497778821f', '207e128f16944fb799a2255da21c94dc', '2', '待分派', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('df5b27a8202a4f5c9092a1b75917d082', '207e128f16944fb799a2255da21c94dc', '3', '待领取', '3');
-- 总分行事件标记
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('ba11590eff784e0e854f22689bf9294f', 'org_flag', '总分行事件标记', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('37a14a8593d443dc903fdaf3c47ef31c', 'ba11590eff784e0e854f22689bf9294f', '3', '子公司', '3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('ac65bf10492f41f8bb47092787ba9be9', 'ba11590eff784e0e854f22689bf9294f', '2', '分行', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('f7b99dd0596e4c7e885ee33e2224e25c', 'ba11590eff784e0e854f22689bf9294f', '1', '总行', '1');
-- 中心侧/网点侧事件
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('8e12655dacc348689681c97309a9fdd4', 'side_flag', '中心侧/网点测事件', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('16c8fc417676498da4d75030cf14f2a3', '8e12655dacc348689681c97309a9fdd4', '2', '网点测事件', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('b073d261529640f3b914f3e6ec3a7a49', '8e12655dacc348689681c97309a9fdd4', '1', '中心侧事件', '1');
-- 事件单来源
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('d2a9e71db3bf4344b14311d5c469d6fc', 'event_source', '事件单来源', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('560869424377428291ec672e189ed1be', 'd2a9e71db3bf4344b14311d5c469d6fc', '3', '客服保障移动报障', '3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('6d6132cd2fba4b5186d2d3f101a01119', 'd2a9e71db3bf4344b14311d5c469d6fc', '2', '柜面', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('98870a1e5d004b90b1fe730805e131f6', 'd2a9e71db3bf4344b14311d5c469d6fc', '1', '监控', '1');
-- 是否通用
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('453799190fb64e96839db1834e46d2c6', 'yes_or_no', '通用是否', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('6cdcd7e9a0e5495fb2b30a8db24970fb', '453799190fb64e96839db1834e46d2c6', '1', '是', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('e36cec5d9da7407fabefd60598ba8eea', '453799190fb64e96839db1834e46d2c6', '0', '否', '1');
-- 关闭代码
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('2e9ce2c0cb234204b43e1fece07e93c6', 'close_code', '关闭代码', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('37e5c431754a4b0299b289d6ded6c07a', '2e9ce2c0cb234204b43e1fece07e93c6', '4', '撤销', '4');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('4879f7fe82354daf818db8b751e730a5', '2e9ce2c0cb234204b43e1fece07e93c6', '3', '不成功', '3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('6d81ae29a2a44946b9318c00ba49a2cd', '2e9ce2c0cb234204b43e1fece07e93c6', '2', '临时解决', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('8837e9e1677043b7aeba857a0d1049d6', '2e9ce2c0cb234204b43e1fece07e93c6', '1', '成功解决', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('ad6fbbf0f4f0427eb0314e3a46a23278', '2e9ce2c0cb234204b43e1fece07e93c6', '5', '可忽略', '5');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('d20af5d1895f4b678f0c93531f0b9eb8', '2e9ce2c0cb234204b43e1fece07e93c6', '6', '拒绝/误报', '6');

-- 事件级别级联数据
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('0f5996192dea4acabdf26ceded6b4bbf', 'BankOfShangHaiEventLevel', '上海银行事件单', 'ServiceRequest', '服务请求', NULL,
        '53dbd6116a1449e6a02662b70e82933b', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('123j1234fdcf4c6a9010aa631248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'BusinessException', '交易异常', NULL,
        '25c7b6c7daed410ea3062d9ce8035e90', NULL, '2022-06-23', '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('123j1234fdcf4c6a9010kk781248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'FinancialException1', '财务/凭证异常',
        NULL, '25c7b6c7daed410ea3062d9ce8035e90', NULL, NULL, '1', '4', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('237e693bfde14f0db63d5cba8cc8edec', 'BankOfShangHaiEventLevel', '上海银行事件单', 'BatchException', '批量异常', NULL,
        'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('25c7b6c7daed410ea3062d9ce8035e90', 'BankOfShangHaiEventLevel', '上海银行事件单', 'ImpactCustomerExperienceIn',
        '影响客户体验（行内）', NULL, 'ccedd6eab9864bc29220a43c0d0d9fad', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('33fb1234fdcf41234010aa63124823bv', 'BankOfShangHaiEventLevel', '上海银行事件单', 'HighAvailability', '高可用性', NULL,
        'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '7', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('33fb1234fdcf41234010aa631248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'Other', '其他', NULL,
        'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '4', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('33fb1234fdcf4c6a9010aa631248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'FinancialException', '财务/报表异常',
        NULL, 'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('33fb1234fdjfk1234010aa631248fut3', 'BankOfShangHaiEventLevel', '上海银行事件单', 'ChangeError', '变更失败', NULL,
        'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '5', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('33fb1234fdjfk123hfjk3a631248fut3', 'BankOfShangHaiEventLevel', '上海银行事件单', 'CapacityManagement', '容量管理', NULL,
        'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '8', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('33fbfdf1fdcf4c6a9010aa631248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'HardwareFailure', '硬件故障', NULL,
        'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('361ad98f0c44482181a95aed3654d4c9', 'BankOfShangHaiEventLevel', '上海银行事件单', 'ImpactCustomerExperienceOut',
        '影响客户体验（行外）', NULL, 'ccedd6eab9864bc29220a43c0d0d9fad', NULL, NULL, '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('456b1234fdcf41234010aa63124823bv', 'BankOfShangHaiEventLevel', '上海银行事件单', 'BatchException1', '批量异常', NULL,
        '25c7b6c7daed410ea3062d9ce8035e90', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('456b1234fdcf41234010aa63fhj823bv', 'BankOfShangHaiEventLevel', '上海银行事件单', 'Other1', '其他', NULL,
        '25c7b6c7daed410ea3062d9ce8035e90', NULL, NULL, '1', '5', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('53dbd6116a1449e6a02662b70e82933b', 'BankOfShangHaiEventLevel', '上海银行事件单', 'EventType', '上海银行事件单级别', NULL, NULL,
        NULL, '2022-06-13', '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('54de4f38e9cc4b04ae28665d6e41914d', 'BankOfShangHaiEventLevel', '上海银行事件单', 'CenterOperateRequest', '中心侧运维请求',
        NULL, '0f5996192dea4acabdf26ceded6b4bbf', NULL, NULL, '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('5eac5c1bd8094d07be5d2843ebc29667', 'BankOfShangHaiEventLevel', '上海银行事件单', 'NetworkLeakage', '网络泄密', NULL,
        '7daa4b9a654f4330a727f1e6b229b5c8', NULL, '2022-06-13', '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('65kj1234fdcf4c6a9010aa631248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'SystemInspection', '系统/网络巡检', NULL,
        'a1f86563518a4e0184ef843e1efd8690', NULL, NULL, '1', '6', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('789k1234fdjfk123hfjk3a631248fut3', 'BankOfShangHaiEventLevel', '上海银行事件单', 'HardwareFailure1', '硬件故障', NULL,
        '25c7b6c7daed410ea3062d9ce8035e90', NULL, NULL, '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('789k1234fdjfk123hik6a631248fut3', 'BankOfShangHaiEventLevel', '上海银行事件单', 'ChangeError1', '变更失败', NULL,
        '25c7b6c7daed410ea3062d9ce8035e90', NULL, NULL, '1', '6', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('7daa4b9a654f4330a727f1e6b229b5c8', 'BankOfShangHaiEventLevel', '上海银行事件单', 'SecurityEvent', '安全事件', NULL,
        '53dbd6116a1449e6a02662b70e82933b', NULL, '2022-06-13', '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('8c683ee1e08846d5bbb65a7fa1644f56', 'BankOfShangHaiEventLevel', '上海银行事件单', 'OutletSelfService', '网点自助服务', NULL,
        '0f5996192dea4acabdf26ceded6b4bbf', NULL, NULL, '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('a1f86563518a4e0184ef843e1efd8690', 'BankOfShangHaiEventLevel', '上海银行事件单', 'AffectBatch', '影响批量/运行/操作', NULL,
        'ccedd6eab9864bc29220a43c0d0d9fad', NULL, NULL, '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('ccedd6eab9864bc29220a43c0d0d9fad', 'BankOfShangHaiEventLevel', '上海银行事件单', 'ProductEvent', '突发事件', NULL,
        '53dbd6116a1449e6a02662b70e82933b', NULL, NULL, '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('d85480fb820546a8afe7dfa52feec54a', 'BankOfShangHaiEventLevel', '上海银行事件单', 'NetworkAttack', '网络攻击', NULL,
        '7daa4b9a654f4330a727f1e6b229b5c8', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('ebc97950992c4ddd9a6a9d68494f455a', 'BankOfShangHaiEventLevel', '上海银行事件单', 'NetworkFaultReport', '网点故障报修', NULL,
        '8c683ee1e08846d5bbb65a7fa1644f56', NULL, '2022-06-23', '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('ef96ef1c3b3f4be5b299d80531736f1e', 'BankOfShangHaiEventLevel', '上海银行事件单', 'BankBusinessRequest', '行内业务请求',
        NULL, '0f5996192dea4acabdf26ceded6b4bbf', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('kglj1234fdcf4c6a9010kk781248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'FinancialException2', '财务/凭证异常',
        NULL, '361ad98f0c44482181a95aed3654d4c9', NULL, NULL, '1', '4', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('olpb1234fdcf41234010aa63fhj823bv', 'BankOfShangHaiEventLevel', '上海银行事件单', 'Other2', '其他', NULL,
        '361ad98f0c44482181a95aed3654d4c9', NULL, NULL, '1', '5', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('qwej1234fdcf4c6a9010aa631248fd1f', 'BankOfShangHaiEventLevel', '上海银行事件单', 'BusinessException2', '交易异常', NULL,
        '361ad98f0c44482181a95aed3654d4c9', NULL, NULL, '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('qwej1234fdcf4c6a9010aa63124fghjk', 'BankOfShangHaiEventLevel', '上海银行事件单', 'DataQuery', '数据查询', NULL,
        'ef96ef1c3b3f4be5b299d80531736f1e', NULL, NULL, '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('qwej1234fjgkic6a9010aa63124fghjk', 'BankOfShangHaiEventLevel', '上海银行事件单', 'RoutineChangeRequest', '常规变更申请',
        NULL, '54de4f38e9cc4b04ae28665d6e41914d', NULL, NULL, '1', '1', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('tyuk1234fdjfk123hf356y631241q2w3', 'BankOfShangHaiEventLevel', '上海银行事件单', 'RoutineMaintain', '日常维护', NULL,
        '54de4f38e9cc4b04ae28665d6e41914d', NULL, NULL, '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('tyuk1234fdjfk123hfjk3a631241q2w3', 'BankOfShangHaiEventLevel', '上海银行事件单', 'SupplementaryVoucher', '补制凭证', NULL,
        'ef96ef1c3b3f4be5b299d80531736f1e', NULL, NULL, '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('tyuk1234fdjfk123hfjk3a631248fut3', 'BankOfShangHaiEventLevel', '上海银行事件单', 'HardwareFailure2', '硬件故障', NULL,
        '361ad98f0c44482181a95aed3654d4c9', NULL, NULL, '1', '3', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('werb1234fdcf41234010aa6312436987', 'BankOfShangHaiEventLevel', '上海银行事件单', 'BusinessQuery', '交易查询', NULL,
        'ef96ef1c3b3f4be5b299d80531736f1e', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('werb1234fdcf41234010aa63124823bv', 'BankOfShangHaiEventLevel', '上海银行事件单', 'BatchException2', '批量异常', NULL,
        '361ad98f0c44482181a95aed3654d4c9', NULL, NULL, '1', '2', NULL, NULL);
insert into `og_typeinfo` (`typeinfo_id`, `type_type_no`, `type_type_name`, `type_no`, `type_name`, `type_description`,
                           `parent_id`, `create_user_id`, `update_time`, `invalidation_mark`, `serial`, `type_level`,
                           `leaf`)
values ('werb1234fdcf4oit4010aa6312436987', 'BankOfShangHaiEventLevel', '上海银行事件单', 'OperateAssignment', '操作任务书', NULL,
        '54de4f38e9cc4b04ae28665d6e41914d', NULL, NULL, '1', '2', NULL, NULL);

-- 2022-07-04事件单菜单
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200000', '业务管理', '0', '14', '#', 'menuItem', 'M', '0', '', 'fa fa-book', 'admin', '2022-04-11 08:39:18',
        'admin', '2022-04-18 15:18:29', '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200003', '事件单管理', '200000', '1', '#', 'menuItem', 'M', '0', NULL, '#', 'admin', '2022-06-07 10:55:45', '',
        NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200004', '事件新建', '200003', '1', '/event/sheet', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-06-07 10:57:43', '', NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200005', '事件分派', '200003', '2', '/event/sheet/assign', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-06-14 16:53:29', '', NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200006', '事件领取', '200003', '3', '/event/sheet/receive', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-06-17 10:02:56', '', NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200007', '事件处理', '200003', '4', '/event/sheet/deal', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-06-20 13:45:27', '', NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200008', '事件解决', '200003', '5', '/event/sheet/pre/solution', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-06-21 09:04:49', '', NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200009', '事件解决复核', '200003', '6', '/event/sheet/check/solution', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-06-22 15:35:35', '', NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('200010', '事件关闭', '200003', '7', '/event/sheet/close', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-06-23 09:34:32', '', NULL, '');

--- 2022-07-07事件单菜单  add by zhangchao
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('2000130', '事件补全', '200003', '2', '/event/sheet/supply', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-07-08 10:01:15', '', NULL, '');
insert into `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`,
                        `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
values ('2000129', '事件单查询', '200003', '8', '/event/sheet/search', 'menuItem', 'C', '0', NULL, '#', 'admin',
        '2022-07-07 13:55:24', '', NULL, '');

-- 2022-07-21 贺彩利
--问题单来源
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('1796105bf15145d99a0fdb4a5fa6a6e6', 'problem_source', '问题单来源', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('191349e1657e4c5b88cadd2f917bcc57', '1796105bf15145d99a0fdb4a5fa6a6e6', '8', '重大事件', '8');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('256e469a129b4bcfb3c4ff9f47b9cbdf', '1796105bf15145d99a0fdb4a5fa6a6e6', '4', '容量管理', '4');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('309ded749f0a481c86cd1282e27b4695', '1796105bf15145d99a0fdb4a5fa6a6e6', '7', '配置管理', '7');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('6064033b90024031a47d704da6edb22a', '1796105bf15145d99a0fdb4a5fa6a6e6', '6', '数据质量', '6');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('69198d84d69b46d48d508551d142c4af', '1796105bf15145d99a0fdb4a5fa6a6e6', '1', '事件管理', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('7051808e412a497794d5f8c8d83e5bed', '1796105bf15145d99a0fdb4a5fa6a6e6', '5', '巡检发现', '5');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('8d60337f1df64defb9071b492d74c5fe', '1796105bf15145d99a0fdb4a5fa6a6e6', '12', '信息安全管理', '12');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('b0160f2cf1044b7ab06ddcf7601720c7', '1796105bf15145d99a0fdb4a5fa6a6e6', '2', '变更管理', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('cf5558710e084878a297b5c26a4c3d2a', '1796105bf15145d99a0fdb4a5fa6a6e6', '9', '可用性管理', '9');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('d5ca656bd34c4923b3e6dca2a3d4b25c', '1796105bf15145d99a0fdb4a5fa6a6e6', '11', '事件趋势分析', '11');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('dfbdba45fd8f46db95e587e849b18eb4', '1796105bf15145d99a0fdb4a5fa6a6e6', '3', '审计发现', '3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('f0f69e2d853942328224ea0c4cdcb599', '1796105bf15145d99a0fdb4a5fa6a6e6', '13', '其他', '13');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('fd03eb0336164378b3414d23855fed53', '1796105bf15145d99a0fdb4a5fa6a6e6', '10', '连续性管理', '10');
-- 解决措施标记
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('29ce18204ad54ec996a87f16ca786fe2', 'temp_solve_flag', '事件是否有临时解决措施', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('74539dc41ee9452b86842a7719791798', '29ce18204ad54ec996a87f16ca786fe2', '0', '否', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('87051243b9774e29a6675aa40d405b3f', '29ce18204ad54ec996a87f16ca786fe2', '1', '是', '2');

-- 问题类型
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('df748c6c40814c468d98dbd9e62a0ecf', 'problem_type', '问题类型', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('09266c2de8b247e5923871f37e5ad965', 'df748c6c40814c468d98dbd9e62a0ecf', '3', '账务类问题', '3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('0f01b2fa9b34481a87accb650cbea07f', 'df748c6c40814c468d98dbd9e62a0ecf', '4', '批量问题', '4');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('162f3f27a3a04bf280e87e0a119038f9', 'df748c6c40814c468d98dbd9e62a0ecf', '8', '其他', '8');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('2d895f87481a4df3964c5f99d94ec97c', 'df748c6c40814c468d98dbd9e62a0ecf', '6', '数据质量问题', '6');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('6b7bef70c3e9466790da4908a904f8db', 'df748c6c40814c468d98dbd9e62a0ecf', '2', '可用性问题', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('7231e4382df34131b51d358363ff17e5', 'df748c6c40814c468d98dbd9e62a0ecf', '5', '功能问题', '5');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('9226696b7df14ac5802d54ac8a792d6e', 'df748c6c40814c468d98dbd9e62a0ecf', '7', '流程管理类问题', '7');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('9ad13fc2edc24829a7ebd41e9037c577', 'df748c6c40814c468d98dbd9e62a0ecf', '1', '性能容量问题', '1');

--风险程度
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('2e87ccf12f6b491eac4d0efbe3570c12', 'risk_level', '风险程度', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('33d7263a5faa4c1f8956334111c3df9c', '2e87ccf12f6b491eac4d0efbe3570c12', '1', '影响报送-高', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('5fe3f54a8b314d36a52d23f86b182447', '2e87ccf12f6b491eac4d0efbe3570c12', '3', '造成账务错乱-高', '3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('ab48bcfc4e0a49ad8c6e702ba6396bcf', '2e87ccf12f6b491eac4d0efbe3570c12', '2', '影响重要业务-高', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('d6628e78751d4b588772b862cea66065', '2e87ccf12f6b491eac4d0efbe3570c12', '4', '存在信息安全风险-高', '4');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('e2aba037658f4119b277cedf987ff23d', '2e87ccf12f6b491eac4d0efbe3570c12', '5', '其他-低', '5');

--发生频率
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('971177137d1d49f1a6e2ff80d85d24ca', 'frequency', '发生频率', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('c5198f1750244cf2a53a3a53572139e4', '971177137d1d49f1a6e2ff80d85d24ca', '1', '中', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('dd89ca168ea74072a5b8cd87d2a476e7', '971177137d1d49f1a6e2ff80d85d24ca', '0', '低', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('f080c15d8ac74d71b070f8010cfdb9b6', '971177137d1d49f1a6e2ff80d85d24ca', '2', '高', '3');
--优先级
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('9fed76306a18495e9fa2fb713530f39b', 'priority', '优先级', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('df33bede51b04a6f9c1099ef72b2418d', '9fed76306a18495e9fa2fb713530f39b', '1', '高', '2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('e0da2fad11144df0a0182b11beb76f35', '9fed76306a18495e9fa2fb713530f39b', '0', '低', '1');

--业务中断标记
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`)
values ('d47932ce80b14675b89cd48b44a4f5f7', 'impact_service_interrupt_flag', '是否影响业务中断', '1', '0', NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('31ccc253251045aab18c1d2082057965', 'd47932ce80b14675b89cd48b44a4f5f7', '0', '否', '1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`)
values ('f8f76aab5f7348fe8a89414b46418418', 'd47932ce80b14675b89cd48b44a4f5f7', '1', '是', '2');
--问题牵头部室
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) values('c1db677eb52342a5937911787ed80b85','solver_dep_id','问题单牵头部室','1','1',NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('103f9150272e4286a0e4d295f7396d71','c1db677eb52342a5937911787ed80b85','330200016','杭州分行_信息技术部','8');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('485af78a311c4538b4b3547256cad88f','c1db677eb52342a5937911787ed80b85','320200083','南京分行_信息技术部','5');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('65ce39a00c6e4a24929267fc7ff3b212','c1db677eb52342a5937911787ed80b85','120200031','天津分行_信息技术部','4');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('7a5e5cf496df45d4bf5e02c87b4e7b0e','c1db677eb52342a5937911787ed80b85','310200958','数据管理与应用','2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('a7ee865c130b46dabfd6f52ccb23ae71','c1db677eb52342a5937911787ed80b85','320200124','苏州分行_信息技术部','6');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('ac5cb5eaa4aa42c89f5e2bcd3dc63bae','c1db677eb52342a5937911787ed80b85','110200022','北京分行_信息技术部','3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('c723b62f91924763ac823badfe46ed53','c1db677eb52342a5937911787ed80b85','510200022','成都分行_信息技术部','10');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('d9fec1f2a4484debab49a38d03c98e8f','c1db677eb52342a5937911787ed80b85','330200141','宁波分行_信息技术部','7');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('ef4f789ed6cf422b929c511539420ccc','c1db677eb52342a5937911787ed80b85','310200176','金融科技部','1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('f62e57033f5b4ee3b5ac7d28eaa2b8aa','c1db677eb52342a5937911787ed80b85','440200074','深圳分行_信息技术部','9');
--牵头部室分行
insert into `pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) values('60a02cbc224344749c4d1d0ec65f6d5a','branch_bank','问题单牵头部室分行','1','1',NULL);
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('0906164b8a8b45dfa200cfd5bf1fdc1b','60a02cbc224344749c4d1d0ec65f6d5a','320200083','南京分行_信息技术部','3');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('63c2dbbd9dd647458b993a88525cc4e3','60a02cbc224344749c4d1d0ec65f6d5a','120200031','天津分行_信息技术部','2');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('7c16fac43e7e46cf84d92faabeee8c2c','60a02cbc224344749c4d1d0ec65f6d5a','320200124','苏州分行_信息技术部','4');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('848c6e6c9fff47ef87bc11e1aa652d22','60a02cbc224344749c4d1d0ec65f6d5a','510200022','成都分行_信息技术部','8');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('967a8065b66f4875b2022bcf4ab72ec1','60a02cbc224344749c4d1d0ec65f6d5a','110200022','北京分行_信息技术部','1');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('b27f2450e2c147d88dbb38062f67c963','60a02cbc224344749c4d1d0ec65f6d5a','440200074','深圳分行_信息技术部','7');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('bbc1b1cfc179430b856fbb7e5ab9379c','60a02cbc224344749c4d1d0ec65f6d5a','330200141','宁波分行_信息技术部','5');
insert into `pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) values('d0f197eada6b49c0be4f494d07b44a33','60a02cbc224344749c4d1d0ec65f6d5a','330200016','杭州分行_信息技术部','6');

-- 修改流程日志表 pub_flow_log中log_type的长度
alter table pub_flow_log modify column log_type varchar (40) comment '流程类型-流程key';
-- 修改流程日志表 pub_flow_log中next_performer_desc的长度
alter table pub_flow_log modify column next_performer_desc varchar (2000) comment '下一节点审批人';
-- 修改字段在列表的显示状态
ALTER TABLE `design_field_version`
    ADD COLUMN `display` int(1) NULL COMMENT '是否显示该字段' AFTER `execution_sql`;

-- 新建事件单-问题单｜变更单关联关系表
create table event_problem_change_relation
(
    relation_id     bigint PRIMARY KEY,
    im_code         varchar(64) comment '事件单号',
    relation_type   varchar(64) comment '关系类型',
    request_type    VARCHAR(64) comment '请求类型',
    request_no      VARCHAR(64) comment '请求转换单号',
    request_general VARCHAR(512) comment '请求概要'
)
-- 插入表单代码项
INSERT INTO `itsm`.`pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) VALUES ('5478cf4a40d547f8a57d9f615e7a5d46', 'formCode', '表单代码', '1', '0', NULL);

--  插入现有单子编码

INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('8e620ea599cc4b60a208bd208002dbe6', '5478cf4a40d547f8a57d9f615e7a5d46', 'problem_new', '问题单', 1);
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('fb7b10eb82a9438cbfee1a8ae8002002', '5478cf4a40d547f8a57d9f615e7a5d46', 'problem_task', '问题任务单', 2);
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('3277f91ba3a44bfc8848636c90eaca80', '5478cf4a40d547f8a57d9f615e7a5d46', 'incident', '事件单', 3);
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('ea71ac48f3704ee083c341766a1fa285', '5478cf4a40d547f8a57d9f615e7a5d46', 'change', '变更单', 4);
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('d7ca19e07e1c4f34bd8aba280720e3e3', '5478cf4a40d547f8a57d9f615e7a5d46', 'changeTask', '变更任务单', 5);

============================下面sql要新增到测试环境=============================
--2022-9-23 贺彩利
INSERT INTO `itsm`.`pub_para`(`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) VALUES ('e9b2307f15e04f79841baec444cf3355', 'close_type', '问题关闭分类', '1', '1', NULL);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('6140049d29e6435483196e011a0625cd', 'e9b2307f15e04f79841baec444cf3355', '5', '根因未明-项目解决', 5);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('63ea4832583e4f72bcb70ab4c8915606', 'e9b2307f15e04f79841baec444cf3355', '2', '已知错误-项目解决', 2);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('aa1f82f8ef2443b396a08ed25c150627', 'e9b2307f15e04f79841baec444cf3355', '4', '根因未明-临时解决方案', 4);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('bd0719bdffd84fd38fced49fef0d918a', 'e9b2307f15e04f79841baec444cf3355', '3', '已知错误-临时解决方案', 3);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('cb886bbb55634c9eae26720dc8bb7f63', 'e9b2307f15e04f79841baec444cf3355', '1', '已知错误-成功解决', 1);

 --2022-9-30 贺彩利 新增关联别的单的中间表
DROP TABLE IF EXISTS `relation_log`;
CREATE TABLE `relation_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `relation_type` varchar(64) NOT NULL COMMENT '关系类型',
  `request_no` varchar(64) NOT NULL COMMENT '请求编号',
  `relation_no` varchar(64) NOT NULL COMMENT '关联编号',
  `request_type` varchar(64) NULL COMMENT '请求类型',
  `request_summary` varchar(500) NULL COMMENT '请求摘要',
  `status` varchar(64) NULL COMMENT '状态',
  `current_handler_id` varchar(64) NOT NULL COMMENT '当前处理人',
  `start_date` datetime(0) NULL COMMENT '开始日期',
  `end_date` datetime(0) NULL COMMENT '结束日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;


-- 2022-09-26  修改字段变更是否记录数据变化
ALTER TABLE `design_field_version`
    ADD COLUMN `record_data_changed` int(1) NULL COMMENT '是否记录数据变化' AFTER `display`;
-- 2022-09-26  修改字段变更是否需要导出
ALTER TABLE `design_field_version`
    ADD COLUMN `exportable` int(1) NULL COMMENT '是否需要导出excel' AFTER `record_data_changed`;

-- 2022-10-08 event_white_list增加所属厂商字段
alter table `event_white_list` add column `vendor` varchar(32) null comment '所属厂商' after `availability`;

-- 2022-10-08 og_sys增加红线时间字段
alter table `og_sys` add column `redline_time` time null comment '红线时间' after `app_sub_dept`;

-- 2022-10-09 增加评审变更日期配置
CREATE TABLE `itsm`.`og_review_changing_date_config`  (
    `id` bigint(20) NOT NULL COMMENT 'ID',
    `day_of_week` varchar(16) NULL COMMENT '评审日(MONDAY-SUNDAY字符串)',
    `ahead_of_hours` int(2) NULL COMMENT '提前小时数',
    PRIMARY KEY (`id`)
);
-------------------2022-10-5 賀彩利 各分行管理员-----------------------------
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('2b68266725154778b921b27f9c341d1c', '天津分行问题管理员', NULL, NULL, '2022-10-05 15:17:13', NULL, NULL, NULL, '1', '天津分行问题管理员', '120100001', NULL, '18209897645', NULL, NULL, '3');
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('3ff45b9c02cc4387a4fc1854062e6fb7', '成都分行问题管理员', NULL, NULL, '2022-10-05 15:20:16', NULL, NULL, NULL, '1', NULL, '510100001', NULL, '14567846777', NULL, NULL, '3');
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('44ad3573006542e08885e71af6afc4fe', '南京分行问题管理员', NULL, NULL, '2022-10-05 15:17:57', NULL, NULL, NULL, '1', NULL, '320100001', NULL, '17835674659', NULL, NULL, '3');
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('53c448efda024d2fb26046751cf44c91', '苏州分行问题管理员', NULL, NULL, '2022-10-05 15:18:26', NULL, NULL, NULL, '1', NULL, '320100012', NULL, '16735647892', NULL, NULL, '3');
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('9ba0a9aa12c94b539bcaaf9bb1d09ed7', '宁波分行问题管理员', NULL, NULL, '2022-10-05 15:18:53', NULL, NULL, NULL, '1', NULL, '330100001', NULL, '15678967896', NULL, NULL, '3');
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('b55c68e94aa348cf811e20bf0d5daa14', '北京分行问题管理员', NULL, NULL, '2022-09-21 14:09:12', 'admin', NULL, '2022-10-05 15:11:26', '1', '北京分行问题管理员', '110100001', NULL, '18723456789', NULL, NULL, '3');
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('b7faec0531ce488a8536ea516f5f6d83', '深圳分行问题管理员', NULL, NULL, '2022-10-05 15:19:48', NULL, NULL, NULL, '1', NULL, '440100001', NULL, '13656478976', NULL, NULL, '3');
INSERT INTO `itsm`.`og_group`(`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('d355520b498d40e984464c96473305af', '杭州分行问题管理员', NULL, NULL, '2022-10-05 15:19:26', NULL, NULL, NULL, '1', NULL, '330100010', NULL, '15678456789', NULL, NULL, '3');

-- 问题取消原因-----
INSERT INTO `itsm`.`pub_para`(`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) VALUES ('8006995242d74e9e8ac398197b610702', 'cancel_reason', '问题取消原因', '1', '1', NULL);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('2f0055ec3d8b4e328a96fa0fc4199ea0', '8006995242d74e9e8ac398197b610702', '1', '不符合开启条件', 1);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('686f63321be34e7583202e60bb592490', '8006995242d74e9e8ac398197b610702', '3', '环境变化不再适用', 3);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('80690a3f1eeb496e909d04e04bdb63ec', '8006995242d74e9e8ac398197b610702', '2', '偶发关闭', 2);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('bfe557db344c4c4ab93dd88d4ef325a4', '8006995242d74e9e8ac398197b610702', '5', '重复关闭', 5);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('ce0294f00bda4db2a01aca5a9d162b3b', '8006995242d74e9e8ac398197b610702', '4', '通过项目/需求方案解决', 4);
---2022.10.10 变更单排班管理
ALTER table design_biz_changetask add COLUMN sequence bigint(20) ;
CREATE DEFINER = `root`@`%` TRIGGER `sequecne` AFTER INSERT ON `impl_record` FOR EACH ROW update design_biz_changetask set sequence=new.id where extra1=new.changeTaskNo;
INSERT INTO `itsm`.`og_group` (`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('e22ac4236cc942adaa32722e361d5e37', '变更排班管理员', NULL, NULL, '2022-10-09 14:03:24', NULL, NULL, NULL, '1', NULL, '310100001', NULL, '123123123213', NULL, NULL, '1');
INSERT INTO `itsm`.`og_group` (`groupid`, `grpname`, `grpleader`, `adder`, `addtime`, `moder`, `modtime`, `update_time`, `invalidation_mark`, `memo`, `orgid`, `count`, `tel`, `sysid`, `white_list`, `group_type`) VALUES ('0b9bbf243c024df8a5236dc689768b0c', '变更实施人', NULL, NULL, '2022-10-09 14:02:53', NULL, NULL, NULL, '1', NULL, '310100001', NULL, '111122', NULL, NULL, '1');

-- 插入定时任务锁表数据 2022-10-11  未执行 上面的不知道执行了没-----
insert into youi_task_lock values ('changeDateTask', '1');
DROP TABLE IF EXISTS `change_task_org`;
CREATE TABLE `change_task_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orgid` varchar(255) DEFAULT NULL COMMENT '部门ID',
  `orgName` varchar(255) DEFAULT NULL COMMENT '部门名',
  `flag` varchar(50) DEFAULT NULL COMMENT '部门单号标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- 2022-10-30 add by zhangchao 事件单增加业务副表
DROP TABLE IF EXISTS `incident_sub_event`;
create table `incident_sub_event`(
	`id` bigint(0) PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
	`event_no` VARCHAR(32) COMMENT '事件单id，关联主表',
	`submit_time` VARCHAR(32) comment '提交时间 格式：yyyy-MM-dd HH:mm:ss',
	`solve_time` VARCHAR(32) comment '解决时间 格式：yyyy-MM-dd HH:mm:ss',
	`solve_length` int comment '解决时长',
	`solve_person` VARCHAR(32) comment '解决人',
	`solve_org` VARCHAR(32) comment '解决部门',
	`solve_group` VARCHAR(32) comment '解决组',
	`close_time` VARCHAR(32) COMMENT '关闭时间 格式：yyyy-MM-dd HH:mm:ss',
	`suspend_flag` VARCHAR(2) COMMENT '是否挂起标识 1-是；0-否',
	`suspend_time` VARCHAR(2) COMMENT '挂起时间 格式：yyyy-MM-dd HH:mm:ss',
	`suspend_length` int COMMENT '挂起时长',
	`urge_flag` VARCHAR(2) COMMENT '是否催单 1-是；0-否',
	`urge_time` int COMMENT '催单时间',
	`service_back_falg` VARCHAR(2) COMMENT '是否客服退回 1-是；0-否',
	`add_msg_back_falg` VARCHAR(2) COMMENT '是否补充信息退回 1-是；0-否',
	`cancel_falg` VARCHAR(2) COMMENT '是否撤单',
	`cancel_time` VARCHAR(32) COMMENT '撤单时间 格式：yyyy-MM-dd HH:mm:ss'
);

INSERT INTO `itsm`.`pub_para`(`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) VALUES ('ae7cc806a2614055a9c0adbcc6e58803', 'close_reason', '问题任务关闭理由', '1', '1', NULL);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('0901c4412fe448e9b5ce7fc33b00ec72', 'ae7cc806a2614055a9c0adbcc6e58803', '3', '取消（偶发关闭）', 3);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('4475bada4a50402ea3ea844d35dfbd50', 'ae7cc806a2614055a9c0adbcc6e58803', '1', '已完成', 1);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('50cc4c98e6ff49449a60d8c5f33a7a16', 'ae7cc806a2614055a9c0adbcc6e58803', '6', '取消（重复关闭）', 6);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('799b2caa9b034196b98988de7f492f11', 'ae7cc806a2614055a9c0adbcc6e58803', '2', '取消（不符合开启条件）', 2);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('82cbc50992f3491082fb9aec05a0eeb6', 'ae7cc806a2614055a9c0adbcc6e58803', '4', '取消（环境变化不在使用）', 4);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('b365f7313fb741adb23adeb7968f1764', 'ae7cc806a2614055a9c0adbcc6e58803', '5', '取消（通过项目/需求方式解决）', 5);

-- 2022-11-6 add by zhangchao 事件单值班表
DROP TABLE IF EXISTS `event_duty_group`;
CREATE TABLE `event_duty_group` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
  `duty_group_type` varchar(2) COMMENT '值班组类型 1-部门机构；2-工作组；3-角色',
  `duty_group_name` varchar(64) COMMENT '值班组名称',
	`duty_person_id` varchar(64) COMMENT '值班人id',
	`duty_person_name` varchar(64) COMMENT '值班人名称',
	`duty_person_phone` varchar(64) COMMENT '值班人电话',
	`duty_person_email` varchar(64) COMMENT '值班人邮箱',
  `start_time` varchar(64)  COMMENT '值班开始时间',
	`end_time` varchar(64)  COMMENT '值班结束时间'
);
-- 事件耗时情况统计表
CREATE TABLE `event_consume_details` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
  `biz_no` varchar(32) COMMENT '事件单号',
  `deal_org` varchar(64) COMMENT '部门',
	`deal_person` varchar(64) COMMENT '处理人',
	`current_node_name` varchar(64) COMMENT '当前节点名称',
	`next_node_name` varchar(64) COMMENT '下一节点名称',
	`start_time` varchar(64) COMMENT '开始处理时间',
	`end_time` varchar(64) COMMENT '结束处理时间'
);

-- -------------------------------
-- 2022-11-19 张强
-- 新增sys_file表，用于附件上传和下载
-- -------------------------------
CREATE TABLE `itsm`.`sys_biz_file`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `instance_id` varchar(64) NULL COMMENT '单号',
    `origin_file_name` varchar(255) NULL COMMENT '原始文件名',
    `file_name` varchar(64) NULL COMMENT '文件名',
    `location` varchar(255) NULL COMMENT '磁盘位置',
    `url` varchar(255) NULL COMMENT '访问地址',
    `content_type` varchar(128) NULL COMMENT '内容类型',
    `hashcode` varchar(255) NULL COMMENT '散列码(用于后期做唯一校验或重复上传校验)',
    `length` bigint(20) NULL COMMENT '文件大小(bit)',
    `create_time` datetime NULL COMMENT '创建时间',
    `update_time` datetime NULL COMMENT '更新时间',
    `create_by` varchar(64) NULL COMMENT '创建人',
    `update_by` varchar(64) NULL COMMENT '更新人',
    `status` char(1) NULL COMMENT '状态',
    PRIMARY KEY (`id`)
) COMMENT = '系统文件';
-- -------------------------------------------------
-- 2022-11-23 张强
-- 修改sys_file表，避免歧义，将instance_id改为ticket_id
-- -------------------------------------------------
ALTER TABLE `sys_biz_file`
CHANGE COLUMN `instance_id` `ticket_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单号' AFTER `id`;

====================问题任务说明====================
INSERT INTO `itsm`.`pub_para`(`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) VALUES ('e77f3229139b4a80b7ec461cf59618c9', 'problem_desc', '问题任务说明', '1', '0', NULL);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('3519c4755849456e8455699386d6dfe2', 'e77f3229139b4a80b7ec461cf59618c9', '解决方案执行', '解决方案执行', 2);
INSERT INTO `itsm`.`pub_paravalue`(`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('4cb0af35a1a04d899f857dd7f582c3a9', 'e77f3229139b4a80b7ec461cf59618c9', '原因排查', '原因排查', 1);
-----2022-12-09 硬件报障新增字段
alter table `design_biz_chm` add column `it_deal_user` varchar(255) null comment '服务台处理人' after `create_by`;
alter table `design_biz_chm` add column `it_deal_time` varchar(255) null comment '服务台处理时间' after `create_by`;

INSERT INTO `itsm`.`pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) VALUES ('c23f50639f1e4fc0ac3e56a7e43792ce', 'og_deputy_cfg_status', '代理人来源状态', '1', '0', NULL);
INSERT INTO `itsm`.`pub_para` (`para_id`, `para_name`, `para_explain`, `state`, `enable`, `update_flag`) VALUES ('d8a0aa4c093a4a3f8a524ae9bfd4f8d5', 'og_deputy_cfg_type', '代理人来源类型', '1', '0', NULL);
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('01e8a34fa9ba452e831bb6d309870789', 'c23f50639f1e4fc0ac3e56a7e43792ce', '1', '失效', '2');
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('b69567a265484292b328aad13b452af7', 'c23f50639f1e4fc0ac3e56a7e43792ce', '0', '有效', '1');
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('1598b804d9eb4428b2b17fe72208c7e3', 'd8a0aa4c093a4a3f8a524ae9bfd4f8d5', '2', '变更单', '2');
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('21fb2277a40e4e18a612af05335cd2e9', 'd8a0aa4c093a4a3f8a524ae9bfd4f8d5', '1', '事件单', '1');
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('41629a7bef4f41578aae0950f63ba93e', 'd8a0aa4c093a4a3f8a524ae9bfd4f8d5', '6', '问题任务单', '6');
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('4e452ade1a2d49bc886bea1b0413e335', 'd8a0aa4c093a4a3f8a524ae9bfd4f8d5', '3', '变更任务单', '3');
INSERT INTO `itsm`.`pub_paravalue` (`para_value_id`, `para_id`, `value`, `value_detail`, `serail`) VALUES ('92844474164d43fe828dfa9d1107603f', 'd8a0aa4c093a4a3f8a524ae9bfd4f8d5', '4', '问题单', '4');
-----2023-1-6 硬件新增字段
alter table `design_biz_chm` add column `real_deal_user` varchar(32) null comment '服务台实际处理人';
----2023 1-12- 排班新增字段
alter table `impl_record` add column `start_time` varchar(255) null comment '应用计划开始时间';
alter table `impl_record` add column `end_time` varchar(255) null comment '应用计划结束时间';
alter table `impl_record` add column `remarks` text null comment '备注';
alter table `impl_record` add column `effect_user` varchar(32) null comment '投产实施人';
alter table `impl_record` add column `flag` varchar(2) null comment '是否发布';
------2023 2-1
ALTER table `design_biz_chm` add column `fu_he_time` varchar(32) null comment '复核时间';