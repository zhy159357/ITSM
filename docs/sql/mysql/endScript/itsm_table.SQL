create table OG_GROUP
(
    groupid           VARCHAR(32) not null,
    grpname           VARCHAR(64) not null,
    grpleader         VARCHAR(16),
    adder             VARCHAR(16),
    addtime           VARCHAR(32),
    moder             VARCHAR(16),
    modtime           VARCHAR(32),
    update_time       VARCHAR(32),
    invalidation_mark VARCHAR(2),
    memo              VARCHAR(512),
    orgid             VARCHAR(36),
    count             VARCHAR(8),
    tel               VARCHAR(64),
    sysid             VARCHAR(2048),
    white_list        VARCHAR(2),
    group_type        VARCHAR(2),
    constraint pk_OG_GROUP primary key (GROUPID)
);

create index OG_GOP_IDX_ORGID_FK on OG_GROUP (ORGID);
create table OG_GROUP_PERSON
(
    groupid   VARCHAR(32) not null,
    pid       VARCHAR(32) not null,
    gpid      VARCHAR(32) not null,
    gposition VARCHAR(32),
    gp_order  VARCHAR(8),
    constraint pk_OG_GROUP_PERSON primary key (GPID)
);

create index OG_GOP_P_IDX_GROUPID_FK on OG_GROUP_PERSON (GROUPID);
create index OG_GOP_P_IDX_PID_FK on OG_GROUP_PERSON (PID);
create table OG_GROUP_TYPEINFO
(
    gt_id       VARCHAR(32) not null,
    groupid     VARCHAR(32) not null,
    typeinfo_id VARCHAR(32) not null,
    constraint pk_OG_GROUP_TYPEINFO primary key (GT_ID)
);
create table OG_MODULE
(
    mid         VARCHAR(32) not null,
    module_no   VARCHAR(32),
    modulename  VARCHAR(64),
    modulekind  VARCHAR(2),
    supermodule VARCHAR(32),
    script      VARCHAR(512),
    adder       VARCHAR(32),
    addtime     VARCHAR(32),
    moder       VARCHAR(32),
    modtime     VARCHAR(32),
    memo        VARCHAR(512),
    moduleorder NUMERIC(19),
    leaf        VARCHAR(1),
    style       VARCHAR(64),
    constraint pk_OG_MODULE primary key (MID)
);

create index OG_MOD_IDX_MODULE_NO on OG_MODULE (MODULE_NO);
create table OG_ORG
(
    orgid             VARCHAR(32) not null,
    t_o_orgid         VARCHAR(32),
    orgcode           VARCHAR(64),
    orgname           VARCHAR(64),
    orglv             VARCHAR(3),
    adder             VARCHAR(16),
    addtime           VARCHAR(32),
    moder             VARCHAR(16),
    modtime           VARCHAR(32),
    update_time       VARCHAR(32),
    invalidation_mark VARCHAR(2),
    memo              VARCHAR(512),
    branch_mark       VARCHAR(8),
    inoutside_mark    VARCHAR(8),
    level_code        VARCHAR(512) comment '层级码',
    leaf              VARCHAR(1) comment '是否叶子节点',
    type              VARCHAR(8),
    sort              VARCHAR(1),
    operaternum       VARCHAR(6),
    khnum             VARCHAR(4) comment '考核厂商序号',
    constraint pk_OG_ORG primary key (ORGID)
);


create index OG_ORG_IDX_T_O_ORGID on OG_ORG (T_O_ORGID);
create table OG_OFFICE
(
    of_id             VARCHAR(36) not null,
    orgid             VARCHAR(36),
    caption           VARCHAR(64),
    memo              VARCHAR(512),
    adder             VARCHAR(16),
    addtime           VARCHAR(20),
    moder             VARCHAR(16),
    update_time       VARCHAR(20),
    invalidation_mark VARCHAR(2),
    constraint pk_OG_OFFICE primary key (OF_ID)
) comment '860111处室';



create index ORGID_FK4 on OG_OFFICE (ORGID);

create table OG_PERSON
(
    pid               VARCHAR(32) not null,
    orgid             VARCHAR(32),
    pname             VARCHAR(36),
    sex               VARCHAR(3),
    edu               VARCHAR(3),
    birthday          VARCHAR(32),
    birthplace        VARCHAR(64),
    phone             VARCHAR(32),
    mobil_phone       VARCHAR(32),
    email             VARCHAR(64),
    address           VARCHAR(64),
    adder             VARCHAR(32),
    addtime           VARCHAR(32),
    moder             VARCHAR(32),
    memo              VARCHAR(512),
    update_time       VARCHAR(32),
    invalidation_mark VARCHAR(2),
    position          VARCHAR(32),
    leader            VARCHAR(2),
    p_order           NUMERIC(19),
    p_flag            VARCHAR(2),
    pid_crc           VARCHAR(32),
    constraint pk_OG_PERSON primary key (PID)
);
create index OG_PSN_IDX_ORGID_FK on OG_PERSON (ORGID);
create table OG_OFFICE_PERSON
(
    op_id     VARCHAR(36) not null,
    pid       VARCHAR(36),
    of_id     VARCHAR(36),
    op_leader VARCHAR(1),
    op_order  VARCHAR(4),
    constraint pk_OG_OFFICE_PERSON primary key (OP_ID)
);

create index OF_ID_FK on OG_OFFICE_PERSON (OF_ID);
create index PID_FK3 on OG_OFFICE_PERSON (PID);

create table OG_POST
(
    postid            VARCHAR(32) not null,
    post_name         VARCHAR(64),
    adder             VARCHAR(32),
    addtime           VARCHAR(32),
    moder             VARCHAR(32),
    update_time       VARCHAR(32),
    memo              VARCHAR(512),
    invalidation_mark VARCHAR(2),
    serial_num        VARCHAR(20) comment '序号',
    power_flag        VARCHAR(2),
    constraint pk_OG_POST primary key (POSTID)
);



create table OG_ROLE
(
    rid               VARCHAR(32) not null,
    rname             VARCHAR(64),
    adder             VARCHAR(16),
    addtime           VARCHAR(32),
    moder             VARCHAR(16),
    modtime           VARCHAR(32),
    update_time       VARCHAR(32),
    invalidation_mark VARCHAR(2),
    memo              VARCHAR(512),
    funclist          VARCHAR(1024),
    constraint PK_OG_ROLE primary key (RID)
);
create table OG_R_POST_ROLE
(
    postid VARCHAR(32) not null,
    rid    VARCHAR(32) not null,
    constraint PK_OG_R_POST_ROLE primary key (POSTID, RID)
);
create table OG_R_PYW_RYWCZ
(
    ywid     VARCHAR(32) not null,
    cz_name  VARCHAR(64) not null,
    cz_phone VARCHAR(32) not null
);
create table OG_R_ROLE_MODULE
(
    mid VARCHAR(32) not null,
    rid VARCHAR(32) not null
);

create table OG_R_USER_POST
(
    postid VARCHAR(32) not null,
    userid VARCHAR(32) not null,
    constraint PK_OOG_R_USER_POST primary key (POSTID, USERID)
);
create table OG_R_USER_POST1
(
    postid VARCHAR(32) not null,
    userid VARCHAR(32) not null,
    constraint PK_OG_R_USER_POST1 primary key (POSTID, USERID)
);
create table OG_R_USER_ROLE
(
    rid    VARCHAR(32) not null,
    userid VARCHAR(32) not null
);


create table OG_SYS
(
    sysid             VARCHAR(36) not null,
    orgid             VARCHAR(36),
    code              VARCHAR(32),
    caption           VARCHAR(64),
    memo              VARCHAR(512),
    adder             VARCHAR(16),
    addtime           VARCHAR(20),
    moder             VARCHAR(16),
    update_time       VARCHAR(20),
    invalidation_mark VARCHAR(2),
    isexamination     VARCHAR(1),
    dept              VARCHAR(2),
    pid               VARCHAR(32) comment '系统负责人',
    businessid        VARCHAR(32) comment '所属业务部门',
    is_key_sys        VARCHAR(2) comment '是否重要系统',
    sys_type          VARCHAR(2),
    upgrade_time      VARCHAR(512),
    is_out_channel    VARCHAR(2),
    system_stage      VARCHAR(512),
    frontline_date    VARCHAR(512),
    secondline_date   VARCHAR(512),
    app_dutyman       VARCHAR(128),
    app_sub_dept      VARCHAR(128),
    constraint PK_OG_SYS primary key (SYSID)
);



create index ORGID_FK3 on OG_SYS (ORGID);

create table OG_SYS1
(
    sysid             VARCHAR(36) not null,
    orgid             VARCHAR(36),
    code              VARCHAR(32),
    caption           VARCHAR(64),
    memo              VARCHAR(512),
    adder             VARCHAR(16),
    addtime           VARCHAR(20),
    moder             VARCHAR(16),
    update_time       VARCHAR(20),
    invalidation_mark VARCHAR(2),
    isexamination     VARCHAR(1)
);

create table OG_TYPEINFO
(
    typeinfo_id       VARCHAR(36) not null,
    type_type_no      VARCHAR(32),
    type_type_name    VARCHAR(128),
    type_no           VARCHAR(32),
    type_name         VARCHAR(128),
    type_description  VARCHAR(512),
    parent_id         VARCHAR(36),
    create_user_id    VARCHAR(36),
    update_time       VARCHAR(32),
    invalidation_mark VARCHAR(2),
    serial            NUMERIC(19),
    type_level        NUMERIC(19),
    leaf              VARCHAR(1),
    constraint PK_OG_TYPEINFO primary key (TYPEINFO_ID)
);

create table OG_USER
(
    userid            VARCHAR(32) not null,
    pid               VARCHAR(32),
    username          VARCHAR(32),
    password          VARCHAR(32),
    update_time       VARCHAR(32),
    invalidation_mark VARCHAR(2),
    ip_start          VARCHAR(32),
    ip_end            VARCHAR(32),
    sms_code          VARCHAR(32) comment '短信验证码',
    last_time         NUMERIC(19) comment '验证码发送时间',
    l_count           VARCHAR(2) comment '短信验证码错误次数',
    p_not_count       VARCHAR(2) comment '密码错误次数',
    lock_time         VARCHAR(20) comment '密码锁定时间',
    weak_pwd          VARCHAR(2) comment '密码强弱校验  0-弱密码  1-强密码',
    cust_no           varchar(32),
    constraint PK_T_OG_USER primary key (USERID)
);


create index OG_USER_IDX_USERNAME on OG_USER (USERNAME);
create table PUB_ATTACHMENT
(
    at_id               VARCHAR(36) not null,
    create_id           VARCHAR(36),
    owner_id            VARCHAR(36),
    memo                VARCHAR(64),
    file_path           VARCHAR(512) comment '文件路径',
    file_name           VARCHAR(128) comment '文件名',
    file_time           VARCHAR(20) comment '上传时间',
    flag                VARCHAR(32),
    size_               VARCHAR(64),
    file_status         VARCHAR(1) comment '文件状态',
    file_ciphertext     VARCHAR(2048) comment '密文描述',
    file_retry_count    INT comment '重试次数',
    file_type           VARCHAR(2) COMMENT '附件类型 1-普通附件  2-版本包  3-自动化步骤',
    group_name          VARCHAR(32) COMMENT '文件上传fast服务器分配的组名称',
    change_reason       VARCHAR(1000) COMMENT '变更原因说明(自动化对接接口模版校验时专用)',
    automate_status     VARCHAR(2) COMMENT '自动化状态 1-未启动  2-启动中  3-启动成功  4-启动失败',
    automate_result_msg VARCHAR(512) COMMENT '自动化返回结果信息',
    sys_name            VARCHAR(512) COMMENT '自动化返回系统名称',
    md5                 VARCHAR(64) COMMENT 'md5',
    constraint PK_PUB_ATTACHMENT primary key (AT_ID)
) comment '860203通用附件';

create index PUB_ATTACHMENT_CREATE_ID_FK on PUB_ATTACHMENT (CREATE_ID);
create index PUB_ATT_IDX_OWNER_ID on PUB_ATTACHMENT (OWNER_ID);

create table PUB_BIZ_PRESMS
(
    pub_biz_presms_id VARCHAR(36) not null comment 'ID',
    sms_type          VARCHAR(2) comment '短信类型',
    telephone         VARCHAR(32) comment '电话',
    name              VARCHAR(32) comment '姓名',
    sms_text          VARCHAR(512) comment '短信内容',
    inspect_time      VARCHAR(32) comment '检查时间',
    inspect_object    VARCHAR(32) comment '检查对象',
    inspect_object_id VARCHAR(36) comment '检查对象ID',
    inspect_text      VARCHAR(32) comment '检查内容',
    creater_id        VARCHAR(36) comment '创建人',
    update_time       VARCHAR(32) comment '更新时间',
    invalidation_mark VARCHAR(2) comment '是否有效',
    sms_state         CHAR(1) comment '0 - 未发送 1 - 已发送',
    task_id           VARCHAR(512),
    verificationcode  VARCHAR(6),
    deal_status       VARCHAR(2) comment '0 - 未处理 1 - 短信处理 2 - 页面处理',
    constraint PK_PUB_BIZ_PRESMS primary key (PUB_BIZ_PRESMS_ID)
);



create table PUB_BIZ_SMS
(
    pub_biz_sms_id    VARCHAR(36) not null,
    pub_biz_presms_id VARCHAR(36) comment '暂存表ID',
    serial_number     INT comment '序号',
    telephone         VARCHAR(32) comment '电话',
    name              VARCHAR(32) comment '姓名',
    sms_text          VARCHAR(512) comment '短信内容',
    sms_time          VARCHAR(32) comment '发送时间',
    sms_state         VARCHAR(2) comment '状态',
    creater_id        VARCHAR(36) comment '创建人',
    update_time       VARCHAR(32) comment '更新时间',
    invalidation_mark VARCHAR(2) comment '是否有效',
    constraint PK_PUB_BIZ_SMS primary key (PUB_BIZ_SMS_ID)
);

create table PUB_BIZ_SMS_RECEIVE
(
    id               VARCHAR(36) not null,
    telephone        VARCHAR(36) comment '发送手机号',
    sms_text         VARCHAR(512) comment '发送短信内容',
    send_time        VARCHAR(32) comment '发送时间',
    verificationcode VARCHAR(32) comment '单号',
    sms_result       VARCHAR(32) comment '处理结果',
    constraint PK_PUB_BIZ_SMS_RECEIVE primary key (ID)
);


create table PUB_FOLDER_INDEX
(
    id_               VARCHAR(36) not null,
    name_             VARCHAR(128),
    path_             VARCHAR(1024),
    parent_           VARCHAR(36),
    description_      VARCHAR(1024),
    create_user_      VARCHAR(32),
    manager_user_     VARCHAR(32),
    create_time_      VARCHAR(20),
    modify_user_      VARCHAR(32),
    modify_time_      VARCHAR(20),
    update_time_      VARCHAR(20),
    leaf_             VARCHAR(20),
    orgs_             text,
    invalidation_mark VARCHAR(2),
    constraint PK_PUB_FOLDER_INDEX primary key (ID_)
);

create table PUB_FILE_INDEX
(
    id_               VARCHAR(36) not null,
    folder_           VARCHAR(36),
    file_name_        VARCHAR(128),
    size_             VARCHAR(32),
    description_      VARCHAR(1024),
    create_user_      VARCHAR(32),
    create_time_      VARCHAR(20),
    modify_user_      VARCHAR(32),
    modify_time_      VARCHAR(20),
    file_path_        VARCHAR(512),
    invalidation_mark VARCHAR(2),
    constraint PK_PUB_FILE_INDEX primary key (ID_)
);

create table PUB_FLOW_LOG
(
    log_id               VARCHAR(36) not null comment '事件单日志id',
    biz_id               VARCHAR(36) comment '事件单id',
    log_type             VARCHAR(20),
    task_name            VARCHAR(64) comment '环节名称',
    performer_id         VARCHAR(36),
    performer_group_id   VARCHAR(36),
    performer_org_id     VARCHAR(36),
    performer_name       VARCHAR(32),
    performer_tel        VARCHAR(20),
    performer_group_name VARCHAR(64),
    performer_org_name   VARCHAR(64),
    performer_time       VARCHAR(20) comment '操作时间',
    perform_name         VARCHAR(64),
    perform_desc         VARCHAR(2000) comment '操作描述',
    next_task_id         VARCHAR(512),
    next_task_name       VARCHAR(512) comment '下一步环节名称',
    next_performer_desc  VARCHAR(128),
    next_performer_tel   VARCHAR(64),
    sys_residence_time   VARCHAR(20) comment '系统停留时间',
    current_state        VARCHAR(32),
    serial_num           VARCHAR(4) comment '序号',
    constraint PK_PUB_FLOW_LOG primary key (LOG_ID)
) comment '流程日志';


create index PUB_FLOW_L_IDX_BIZ_ID on PUB_FLOW_LOG (BIZ_ID);
create table PUB_HOLIDAY
(
    holiday_id  VARCHAR(36) not null comment '主键ID',
    scope       VARCHAR(2) comment '使用范围',
    day         VARCHAR(20) comment '日期',
    name        VARCHAR(100) comment '节假日名称',
    type        VARCHAR(2) comment '类别',
    memo        VARCHAR(512) comment '备注',
    agency_id   text comment '使用机构id',
    agency_name text comment '使用机构名称',
    constraint PK_PUB_HOLIDAY primary key (HOLIDAY_ID)
) comment '节假日配置';


create table PUB_NO_SEQ
(
    seq_type VARCHAR(16) not null,
    seq_date VARCHAR(8)  not null,
    seq_no   VARCHAR(16) not null
);

create table PUB_PARA
(
    para_id      VARCHAR(36) not null,
    para_name    VARCHAR(64),
    para_explain VARCHAR(64),
    state        VARCHAR(4),
    update_flag  VARCHAR(2),
    constraint PUB_PARA primary key (PARA_ID)
);


create table PUB_PARAVALUE
(
    para_value_id VARCHAR(36) not null,
    para_id       VARCHAR(36),
    value         VARCHAR(64),
    value_detail  VARCHAR(64),
    serail        NUMERIC(19),
    constraint PK_PUB_PARAVALUE primary key (PARA_VALUE_ID)
);

create index PUB_PV_IDX_PARA_ID_FK on PUB_PARAVALUE (PARA_ID);

create table PUB_RELATION
(
    relation_id VARCHAR(36) not null comment '主键ID',
    obj1_id     VARCHAR(36) comment '对象1ID',
    obj2_id     VARCHAR(36) comment '对象2ID',
    type        VARCHAR(2) comment '关系分类：01事件-问题，02事件-数据变更，03业务事件-综合变更，04问题-综合变更，05问题-版本，06计划（任务）-公告，07事务事件-综合变更，08监控事件-综合变更，09故障事件-综合变更，10调度事件-综合变更,11计划-综合变更单；注：根据上述五种对用关系，对象1和对象2正向排列，方便查询',
    hangup_task VARCHAR(200) comment '挂起任务，用于标记业务事件单转数据变更时，挂起的任务',
    constraint PK_PUB_RELATION primary key (RELATION_ID)
)comment '事件/问题/变更关联关系';


create index PUB_R_IDX_OBJ1_ID on PUB_RELATION (OBJ1_ID);
create index PUB_R_IDX_OBJ2_ID on PUB_RELATION (OBJ2_ID);



create table TEL_TERMINAL
(
    id           VARCHAR(32) not null comment 'ID',
    telip        VARCHAR(32) not null comment '话机IP',
    service_ip   VARCHAR(32) not null comment '服务IP',
    extno        VARCHAR(16) not null comment '分机号',
    job_number   VARCHAR(32) not null comment '工号',
    create_id    VARCHAR(36) not null comment '创建人',
    create_time  VARCHAR(32) not null comment '创建时间',
    tel_terminal VARCHAR(36) not null comment '技能组id',
    ifskillno    VARCHAR(16) not null comment '添加默认技能组',
    bizid        VARCHAR(36) not null comment '绑定电话事件单id',
    constraint pk_TEL_TERMINAL primary key (id)
);

create table VM_BIZ_INFO
(
    version_info_id                VARCHAR(36) not null comment '版本信息单id',
    version_info_no                VARCHAR(32) not null comment '版本信息单编号',
    version_info_name              VARCHAR(128) comment '版本信息单名称',
    version_producer_id            VARCHAR(32) comment '版本制作人',
    version_create_time            VARCHAR(32) comment '版本创建时间',
    sysid                          VARCHAR(32) comment '所属应用系统',
    version_name                   VARCHAR(128) comment '升级内容摘要',
    version_no                     VARCHAR(32),
    version_description            VARCHAR(2048) comment '版本描述',
    version_upgrade_content        VARCHAR(512) comment '版本升级内容',
    business_change_description    VARCHAR(512) comment '业务变更说明',
    upgrade_environment            VARCHAR(512) comment '升级环境',
    notice_text                    text comment '版本变更内容',
    version_status                 VARCHAR(2) comment '版本状态',
    technical_support_id           VARCHAR(168),
    technical_support_phone        VARCHAR(64),
    update_time                    VARCHAR(32),
    version_attr                   VARCHAR(32),
    test_report                    VARCHAR(32),
    question_id                    VARCHAR(512),
    send_msg_flag                  VARCHAR(32),
    version_type                   VARCHAR(128),
    if_safety_audit                VARCHAR(2),
    issued_unit                    VARCHAR(2048),
    start_upgrade_time             VARCHAR(20),
    end_upgrade_time               VARCHAR(20),
    accept_organization            VARCHAR(1000),
    accept_role                    VARCHAR(2),
    createunits                    VARCHAR(512),
    copy_requite_unit              VARCHAR(512),
    business_state                 VARCHAR(2),
    technology_state               VARCHAR(2),
    vm_pro_actlog_id               VARCHAR(32),
    drafter_id                     VARCHAR(512),
    issued_no                      VARCHAR(32),
    safety_audit_id                VARCHAR(32) comment '安全审核人',
    technology_audit_id            VARCHAR(32) comment '技术审核人',
    business_audit_id              VARCHAR(2048) comment '业务审核人',
    business_approval_id           VARCHAR(2048) comment '业务主管',
    technology_approval_id         VARCHAR(32) comment '技术审批人',
    use_approval_id                VARCHAR(32) comment '应用审批人',
    uploader_approval_id           VARCHAR(32) comment '发布审批人',
    if_issue_notice                VARCHAR(2) comment '是否发布公告',
    issued_branch                  VARCHAR(2000),
    agency_center                  VARCHAR(1024),
    invalidation_mark              VARCHAR(2),
    agency_zx                      VARCHAR(32),
    business_requirement_text      text comment '业务需求',
    business_validation_text       text comment '测试及业务验证',
    if_report_office               VARCHAR(2) comment '是否通报办公室,1/是，2/否',
    if_report_cbrc                 VARCHAR(2) comment '是否报备银保监会,1/是，2/否',
    if_report_call_center          VARCHAR(2) comment '是否通报客服中心,1/是，2/否',
    if_stop_business               VARCHAR(2) comment '是否需停业升级,1/是，2/否',
    sys_name                       text comment '所涉应用系统名称',
    sys_id                         text comment '所涉应用系统ID',
    use_division_chief_approval_id VARCHAR(32) comment '应用处长审批人',
    jjsp_approval_id               VARCHAR(32) comment '紧急审批人(徐总)',
    business_org                   VARCHAR(256) comment '业务部门',
    is_automate                    VARCHAR(2),
    automate_audit_id              VARCHAR(32),
    constraint pk_VM_BIZ_INFO primary key (VERSION_INFO_ID)
);


create index VM_B_I_IDX_CREAT_TIME on VM_BIZ_INFO (VERSION_CREATE_TIME);
create index VM_B_I_IDX_PRODUCER_ID on VM_BIZ_INFO (VERSION_PRODUCER_ID);
create index VM_B_I_IDX_STAUTS on VM_BIZ_INFO (VERSION_STATUS);
create index VM_B_I_IDX_VERSION_NO on VM_BIZ_INFO (VERSION_INFO_NO);

create table VM_BIZ_TASKINFO
(
    task_id            VARCHAR(32)  not null comment '任务序号ID',
    version_info_id    VARCHAR(32)  not null comment '版本申请单ID',
    task_name          VARCHAR(128) not null comment '任务名称',
    task_type_id       VARCHAR(32) comment '任务类型',
    task_status        VARCHAR(10) comment '任务状态',
    task_content       VARCHAR(2048) comment '任务内容',
    task_deal_group_id VARCHAR(32) comment '任务处理组',
    task_deal_user_id  VARCHAR(32) comment '任务处理人',
    front_task_id      VARCHAR(32) comment '前置任务序号',
    follow_task_id     VARCHAR(32) comment '后续任务序号',
    est_start_date     VARCHAR(32) comment '预计开始时间',
    est_finish_date    VARCHAR(32) comment '预计完成时间',
    task_deal_result   VARCHAR(512) comment '任务处理结果',
    actual_start_time  VARCHAR(32) comment '实际开始时间',
    actual_finish_time VARCHAR(32) comment '实际完成时间',
    update_time        VARCHAR(32) comment '最近修改时间',
    invalidation_mark  VARCHAR(32) comment '无效标记',
    taskproducetime    VARCHAR(32) comment '任务处理时间',
    org                VARCHAR(32) comment '机构代码',
    task_no            VARCHAR(32) comment '任务编号',
    user_id            VARCHAR(1024),
    man_phone          VARCHAR(2048),
    message_flag       VARCHAR(2) comment '短信回复标志 0未回复 1已回复',
    complete_user_id   VARCHAR(32),
    constraint pk_VM_BIZ_TASKINFO primary key (TASK_ID)
);

/*
客户信息表 llf 2022/1/5
*/
create table cust_info
(
    c_id        varchar(32) primary key not null,
    c_name      varchar(48)  default null comment '客户名称',
    c_phone     varchar(12)  default null comment '客户电话号码',
    c_dept      varchar(50)  default null comment '客户部门',
    c_post      varchar(50)  default null comment '客户职务',
    c_address   varchar(200) default null comment '客户地址',
    create_time date comment '创建时间',
    c_column    varchar(50)  default null comment '扩展字段'
) comment='客户信息表';

/*
事件表
*/
CREATE TABLE FM_BIZ
(
    FM_ID                 varchar(36) primary key not null,
    CREATE_ID             varchar(36)   default null comment '创建人',
    CREATE_ORG_ID         varchar(36)   default null comment '创建机构',
    GROUP_ID              varchar(36)   default null comment '工作组',
    DEALER_ID             varchar(36)   default null comment '处理人',
    DEAL_GROUP_ID         varchar(36)   default null comment '处理组',
    TYPEINFO_ID           varchar(36)   default null comment '类别',
    SYSID                 varchar(36)   default null comment '系统',
    FAULT_NO              varchar(32)   default null comment '事件单编号',
    FAULT_SOURCE          varchar(2)    default null comment '事件来源ID：1：手工，2：监控系统',
    SERIOUS_LEV           varchar(2)    default null comment '事件等级:1:一般,2:急,3紧急',
    OCCURRENCE_ADDRESS    varchar(128)  default null comment '发生地址',
    OCCUR_TIME            varchar(20)   default null comment '发生时间',
    FAULT_REPORT_NAME     varchar(54)   default null comment '报告人',
    REPORT_TIME           varchar(20)   default null comment '报告时间',
    REPORT_PHONE          varchar(32)   default null comment '报告人电话',
    FAULT_CONTACT_NAME    varchar(54)   default null comment '联系人',
    CONTACT_PHONE         varchar(24)   default null comment '联系人电话',
    CONTACT_ADDRESS       varchar(128)  default null comment '联系人地址',
    SERIAL_NUMBER         varchar(96)   default null comment '流水号',
    TRADING_NAME          varchar(96)   default null comment '交易名称',
    ERROR_INFORMATION     varchar(192)  default null comment '错误信息',
    FAULT_DECRIPT_SUMMARY varchar(128)  default null comment '事件标题',
    FAULT_DECRIPT_DETAIL  varchar(3000) default null comment '事件描述',
    CREAT_TIME            varchar(20)   default null comment '创建时间',
    DEAL_TIME             varchar(20)   default null comment '处理时间',
    DEAL_DESCRIPTION      varchar(2000) default null comment '处理描述',
    EVALUATE              varchar(512)  default null comment '评价：1：满意，2：一般，3：不满意，用于运营事件单评价',
    EVALUATE_TIME         varchar(20)   default null comment '评价时间，用于运营事件单评价',
    END_TIME              varchar(20)   default null comment '',
    CURRENT_STATE         varchar(2)    default null comment '当前状态: 1-“待提交”2—“作废”3-“待处理”4-“处理中”5-“待分派” 6—“待关闭”7—“已关闭”',
    DEAL_USE_TIME         varchar(20)   default null comment '',
    INVOLVE_AMOUNT        varchar(2)    default null comment '涉事金额',
    INVOLVE_SCOUNT        varchar(60)   default null comment '涉事笔数',
    CUSTOMER_NAME         varchar(48)   default null comment '客户姓名',
    CUSTOMER_IDCARD       varchar(20)   default null comment '客户身份证号',
    TRANSACTION_ACCOUNT   varchar(256)  default null comment '交易账号',
    TRANSACTION_AMOUNT    varchar(108)  default null comment '交易金额',
    ORDER_NUMBER          varchar(128)  default null comment '订单号',
    EVALUATE_RESULT       varchar(2)    default null comment '评价结果：满意、不满意',
    FM_CAUSE              varchar(64)   default null comment '事件起因',
    FM_JKMARK             varchar(2)    default null comment '监控标记',
    PARTICIPATOR_IDS      varchar(2000) default null comment '参与处理人员ID,用于过滤我处理过的事件单',
    DEAL_MODE             varchar(2)    default null comment '处理方式：正常处理/疑难事件处理/多方配合处理/数据变更处理',
    PARTICIPATE_GROUPIDS  varchar(2000) default null comment '参与工作组ID,用于过滤工作组参与过的事件单',
    TO_QGZX_TIME          varchar(20)   default null comment '转全国中心时间',
    BACK_SZX_TIME         varchar(20)   default null comment '退回省中心时间',
    REPEAT_MARK           varchar(2)    default null comment '转发原因：1多方配合处理，2非本系统，用于统计省转全国中心选择的工作组是否正确',
    RECEIVER_IDS          varchar(2000) default null comment '领取人ID,用于统计人员领取的事件单数量',
    INVALIDATION_MARK     varchar(2)    default null comment '无效标志，0：无效，1：有效',
    IF_JJ                 varchar(2)    default null comment '是否紧急',
    IF_BULLETION          varchar(2)    default null comment '是否通报',
    BULL_CAUSE            varchar(256)  default null comment '通报原因',
    BULL_TIME             varchar(64)   default null comment '通报时间',
    IS_ANTI_FRAUD         varchar(2)    default null comment '是否是反欺诈相关 1-是  0-否',
    IS_SEND               varchar(2)    default null comment '是否发送成功  1-是 0-否',
    INI_SYS               varchar(36)   default null comment '初始系统',
    ONETYPE               varchar(36)   default null comment '当前一级分类',
    TWOTYPE               varchar(36)   default null comment '当前二级分类',
    THREETYPE             varchar(36)   default null comment '当前三级分类',
    KEYWORDS              varchar(512)  default null comment '当前关键字',
    INI_ONETYPE           varchar(36)   default null comment '初始一级分类',
    INI_TWOTYPE           varchar(36)   default null comment '初始二级分类',
    INI_THREETYPE         varchar(36)   default null comment '初始三级分类',
    INI_KEYWORDS          varchar(512)  default null comment '初始关键字',
    KNOWLEDGE_ID          varchar(512)  default null comment '知识ID',
    if_yn                 varchar(2)    default null,
    c_id                  varchar(36)   default null comment '客户id',
    fm_type_id            varchar(36)   default null,
    occurrence_org_id     varchar(36)   default null,
    EVALUATER_ID          varchar(36)   default null,
    record_type           varchar(2)    default null comment '事件单类型 1、事件单 2、咨询单',
    auditor_id            varchar(36)   default null,
    audit_status          varchar(2)    default null,
    audit_time            varchar(20)   default null,
    n1                    text,
    n2                    text,
    n3                    text,
    n4                    text
) comment='业务事件单表';

/*
创建事件数据维护表
*/
create table fm_biz_maint
(
    m_id     varchar(32) primary key not null,
    m_title  varchar(128)  default null comment '事件标题',
    m_detail varchar(4000) default null comment '事件描述'
) comment='事件维护表';

/*
potal
*/
create table SYSTEM_APP
(
    id          varchar(64) primary key not null comment '部门主键seq_sys_dept.nextval',
    create_time TIMESTAMP default CURRENT_TIMESTAMP comment '创建时间',
    sysname     varchar(255) comment '系统名称',
    sysurl      varchar(255)            not null comment '系统平台连接url',
    del_flag    int comment '删除标识',
    update_time TIMESTAMP default CURRENT_TIMESTAMP comment '更新时间',
    param       varchar(255) comment '参数信息',
    syscode     varchar(255) comment '系统标识码',
    sysorder    int comment '系统排序标识',
    status      varchar(255) comment '状态（0正常 1停用）'
) comment='potal.系统维护';

/*
SYSTEM_APP_USER
*/
create table SYSTEM_APP_USER
(
    id       varchar(64) primary key not null,
    iappid   varchar(255),
    iuserid  varchar(255)            not null,
    sysorder int
);

/*
SYSTEM_APP_ROLE
*/
create table SYSTEM_APP_ROLE
(
    iappid  varchar(255),
    iROLEid varchar(255) not null
);

create table synchronize_user
(
    id           int(11) not null,
    service_name VARCHAR(255),
    service_url  VARCHAR(2000) not null,
    json_param   VARCHAR(2000),
    css_param    VARCHAR(200),
    create_time  TIMESTAMP default CURRENT_TIMESTAMP,
    SYNCFLAG     int(11)
);

/*
1、存储每一个已配置的 jobDetail 的详细信息
*/
create table qrtz_job_details
(
    sched_name        varchar(120) not null,
    job_name          varchar(200) not null,
    job_group         varchar(200) not null,
    description       varchar(250) null,
    job_class_name    varchar(250) not null,
    is_durable        varchar(1)   not null,
    is_nonconcurrent  varchar(1)   not null,
    is_update_data    varchar(1)   not null,
    requests_recovery varchar(1)   not null,
    job_data          text null,
    constraint qrtz_job_details_pk primary key (sched_name, job_name, job_group),
    index             idx_qrtz_j_req_recovery(sched_name,requests_recovery),
    index             idx_qrtz_j_grp(sched_name,job_group)
);

/*
2、存储已配置的 Trigger 的信息
*/
create table qrtz_triggers
(
    sched_name     varchar(120) not null,
    trigger_name   varchar(200) not null,
    trigger_group  varchar(200) not null,
    job_name       varchar(200) not null,
    job_group      varchar(200) not null,
    description    varchar(250) null,
    next_fire_time int(13) null,
    prev_fire_time int(13) null,
    priority       int(13) null,
    trigger_state  varchar(16)  not null,
    trigger_type   varchar(8)   not null,
    start_time     int(13) not null,
    end_time       int(13) null,
    calendar_name  varchar(200) null,
    misfire_instr  int(2) null,
    job_data       text null,
    constraint qrtz_triggers_pk primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_trigger_to_jobs_fk foreign key (sched_name, job_name, job_group)
        references qrtz_job_details (sched_name, job_name, job_group),
    index          idx_qrtz_t_j (sched_name,job_name,job_group),
    index          idx_qrtz_t_jg (sched_name,job_group),
    index          idx_qrtz_t_c (sched_name,calendar_name),
    index          idx_qrtz_t_g (sched_name,trigger_group),
    index          idx_qrtz_t_state (sched_name,trigger_state),
    index          idx_qrtz_t_n_state (sched_name, trigger_name,trigger_group,trigger_state),
    index          idx_qrtz_t_n_g_state (sched_name,trigger_group,trigger_state),
    index          idx_qrtz_t_next_fire_time (sched_name,next_fire_time),
    index          idx_qrtz_t_nft_st (sched_name,trigger_state,next_fire_time),
    index          idx_qrtz_t_nft_misfire (sched_name,misfire_instr,next_fire_time),
    index          idx_qrtz_t_nft_st_misfire (sched_name,misfire_instr,next_fire_time,trigger_state),
    index          idx_qrtz_t_nft_st_misfire_grp (sched_name,misfire_instr,next_fire_time,trigger_group,trigger_state)
);

/*
3、 存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数
*/
create table qrtz_simple_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    repeat_count    int(7) not null,
    repeat_interval int(12) not null,
    times_triggered int(10) not null,
    constraint qrtz_simple_trig_pk primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_simple_trig_to_trig_fk foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

/*
4、 存储 Cron Trigger，包括 Cron 表达式和时区信息
*/
create table qrtz_cron_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    cron_expression varchar(120) not null,
    time_zone_id    varchar(80),
    constraint qrtz_cron_trig_pk primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_cron_trig_to_trig_fk foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

/*
5、 Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
*/
create table qrtz_blob_triggers
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(200) not null,
    trigger_group varchar(200) not null,
    blob_data     text null,
    constraint qrtz_blob_trig_pk primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_blob_trig_to_trig_fk foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

/*
6、 以 Blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围
*/
create table qrtz_calendars
(
    sched_name    varchar(120) not null,
    calendar_name varchar(200) not null,
    calendar      text         not null,
    constraint qrtz_calendars_pk primary key (sched_name, calendar_name)
);

/*
7、 存储已暂停的 Trigger 组的信息
*/
create table qrtz_paused_trigger_grps
(
    sched_name    varchar(120) not null,
    trigger_group varchar(200) not null,
    constraint qrtz_paused_trig_grps_pk primary key (sched_name, trigger_group)
);

/*
8、 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息
*/
create table qrtz_fired_triggers
(
    sched_name        varchar(120) not null,
    entry_id          varchar(95)  not null,
    trigger_name      varchar(200) not null,
    trigger_group     varchar(200) not null,
    instance_name     varchar(200) not null,
    fired_time        int(13) not null,
    sched_time        int(13) not null,
    priority          int(13) not null,
    state             varchar(16)  not null,
    job_name          varchar(200) null,
    job_group         varchar(200) null,
    is_nonconcurrent  varchar(1) null,
    requests_recovery varchar(1) null,
    constraint qrtz_fired_trigger_pk primary key (sched_name, entry_id),
    index             idx_qrtz_ft_trig_inst_name(sched_name,instance_name),
    index             idx_qrtz_ft_inst_job_req_rcvry(sched_name,instance_name,requests_recovery),
    index             idx_qrtz_ft_j_g(sched_name,job_name,job_group),
    index             idx_qrtz_ft_jg(sched_name,job_group),
    index             idx_qrtz_ft_t_g(sched_name, trigger_name,trigger_group),
    index             idx_qrtz_ft_tg(sched_name,trigger_group)
);

/*
9、 存储少量的有关 Scheduler 的状态信息，假如是用于集群中，可以看到其他的 Scheduler 实例
*/
create table qrtz_scheduler_state
(
    sched_name        varchar(120) not null,
    instance_name     varchar(200) not null,
    last_checkin_time int(13) not null,
    checkin_interval  int(13) not null,
    constraint qrtz_scheduler_state_pk primary key (sched_name, instance_name)
);

/*
10、 存储程序的悲观锁的信息(假如使用了悲观锁)
*/
create table qrtz_locks
(
    sched_name varchar(120) not null,
    lock_name  varchar(40)  not null,
    constraint qrtz_locks_pk primary key (sched_name, lock_name)
);

/*

*/
create table qrtz_simprop_triggers
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(200) not null,
    trigger_group varchar(200) not null,
    str_prop_1    varchar(512) null,
    str_prop_2    varchar(512) null,
    str_prop_3    varchar(512) null,
    int_prop_1    int(10) null,
    int_prop_2    int(10) null,
    long_prop_1   int(13) null,
    long_prop_2   int(13) null,
    dec_prop_1    numeric(13, 4) null,
    dec_prop_2    numeric(13, 4) null,
    bool_prop_1   varchar(1) null,
    bool_prop_2   varchar(1) null,
    constraint qrtz_simprop_trig_pk primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_simprop_trig_to_trig_fk foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

/*
1、部门表
*/
drop table if exists sys_dept;
create table sys_dept
(
    dept_id       bigint(20) not null auto_increment comment '部门id',
    parent_id     bigint(20) default 0 comment '父部门id',
    ancestors     varchar(50) default '' comment '祖级列表',
    dept_name     varchar(30) default '' comment '部门名称',
    order_num     int(4) default 0 comment '显示顺序',
    leader        varchar(20) default null comment '负责人',
    phone         varchar(11) default null comment '联系电话',
    email         varchar(50) default null comment '邮箱',
    status        char(1)     default '0' comment '部门状态（0正常 1停用）',
    del_flag      char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by     varchar(64) default '' comment '创建者',
    create_time   datetime comment '创建时间',
    update_by     varchar(64) default '' comment '更新者',
    update_time   datetime comment '更新时间',
    deptlevel     varchar(255),
    type          varchar(255),
    operaternum   varchar(255),
    remark        varchar(255),
    tier          varchar(255),
    dept          varchar(255),
    branchmark    varchar(255),
    inoutsidemark varchar(255),
    deptcode      varchar(255),
    primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

/*
2、用户信息表
*/
create table sys_user
(
    user_id         bigint(20) not null auto_increment comment '用户ID',
    dept_id         bigint(20) default null comment '部门ID',
    login_name      varchar(30) not null comment '登录账号',
    user_name       varchar(30)  default '' comment '用户昵称',
    user_type       varchar(2)   default '00' comment '用户类型（00系统用户 01注册用户）',
    email           varchar(50)  default '' comment '用户邮箱',
    phonenumber     varchar(11)  default '' comment '手机号码',
    sex             char(1)      default '0' comment '用户性别（0男 1女 2未知）',
    avatar          varchar(100) default '' comment '头像路径',
    password        varchar(50)  default '' comment '密码',
    salt            varchar(20)  default '' comment '盐加密',
    status          char(1)      default '0' comment '帐号状态（0正常 1停用）',
    del_flag        char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    login_ip        varchar(50)  default '' comment '最后登录IP',
    login_date      datetime comment '最后登录时间',
    pwd_update_date datetime comment '密码最后更新时间',
    create_by       varchar(64)  default '' comment '创建者',
    create_time     datetime comment '创建时间',
    update_by       varchar(64)  default '' comment '更新者',
    update_time     datetime comment '更新时间',
    remark          varchar(500) default null comment '备注',
    primary key (user_id)
) engine=innodb auto_increment=100 comment = '用户信息表';

/*
3、岗位信息表
*/
drop table if exists sys_post;
create table sys_post
(
    post_id     bigint(20) not null auto_increment comment '岗位ID',
    post_code   varchar(64) not null comment '岗位编码',
    post_name   varchar(50) not null comment '岗位名称',
    post_sort   int(4) not null comment '显示顺序',
    status      char(1)     not null comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (post_id)
) engine=innodb comment = '岗位信息表';

/*
4、角色信息表
*/
create table sys_role
(
    role_id     bigint(20) not null auto_increment comment '角色ID',
    role_name   varchar(30)  not null comment '角色名称',
    role_key    varchar(100) not null comment '角色权限字符串',
    role_sort   int(4) not null comment '显示顺序',
    data_scope  char(1)      default '1' comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    status      char(1)      not null comment '角色状态（0正常 1停用）',
    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

/*
5、菜单权限表
*/
create table sys_menu
(
    menu_id     bigint(20) not null auto_increment comment '菜单ID',
    menu_name   varchar(50) not null comment '菜单名称',
    parent_id   bigint(20) default 0 comment '父菜单ID',
    order_num   int(4) default 0 comment '显示顺序',
    url         varchar(200) default '#' comment '请求地址',
    target      varchar(20)  default '' comment '打开方式（menuItem页签 menuBlank新窗口）',
    menu_type   char(1)      default '' comment '菜单类型（M目录 C菜单 F按钮）',
    visible     char(1)      default 0 comment '菜单状态（0显示 1隐藏）',
    perms       varchar(100) default null comment '权限标识',
    icon        varchar(100) default '#' comment '菜单图标',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default '' comment '备注',
    primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

/*
6、用户和角色关联表  用户N-1角色
*/
create table sys_user_role
(
    user_id varchar(100) not null comment '用户ID',
    role_id bigint(20) not null comment '角色ID',
    primary key (user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

/*
7、角色和菜单关联表  角色1-N菜单 (注：id非主键，但是需要自增，所以加了 KEY id (id))
*/
create table sys_role_menu
(
    id      bigint(20) NOT NULL auto_increment,
    role_id varchar(32) not null comment '角色ID',
    menu_id bigint(20) not null comment '菜单ID',
    primary key (role_id, menu_id),
    KEY     id (id)
) engine=innodb comment = '角色和菜单关联表';

/*
8、角色和部门关联表  角色1-N部门
*/
create table sys_role_dept
(
    role_id bigint(20) not null comment '角色ID',
    dept_id bigint(20) not null comment '部门ID',
    primary key (role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

/*
9、用户与岗位关联表  用户1-N岗位
*/
create table sys_user_post
(
    user_id bigint(20) not null comment '用户ID',
    post_id bigint(20) not null comment '岗位ID',
    primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

/*
10、操作日志记录
*/
create table sys_oper_log
(
    oper_id        bigint(20) not null auto_increment comment '日志主键',
    title          varchar(50)   default '' comment '模块标题',
    business_type  int(2) default 0 comment '业务类型（0其它 1新增 2修改 3删除）',
    method         varchar(100)  default '' comment '方法名称',
    request_method varchar(10)   default '' comment '请求方式',
    operator_type  int(1) default 0 comment '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name      varchar(50)   default '' comment '操作人员',
    dept_name      varchar(50)   default '' comment '部门名称',
    oper_url       varchar(255)  default '' comment '请求URL',
    oper_ip        varchar(50)   default '' comment '主机地址',
    oper_location  varchar(255)  default '' comment '操作地点',
    oper_param     varchar(2000) default '' comment '请求参数',
    json_result    varchar(4000) default '' comment '返回参数',
    status         int(1) default 0 comment '操作状态（0正常 1异常）',
    error_msg      varchar(2000) default '' comment '错误消息',
    oper_time      datetime comment '操作时间',
    primary key (oper_id)
) engine=innodb auto_increment=100 comment = '操作日志记录';

/*
11、字典类型表
*/
create table sys_dict_type
(
    dict_id     bigint(20) not null auto_increment comment '字典主键',
    dict_name   varchar(100) default '' comment '字典名称',
    dict_type   varchar(100) default '' comment '字典类型',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    constraint pk_sys_dict_type primary key (dict_id),
    unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

/*
12、字典数据表
*/
create table sys_dict_data
(
    dict_code   bigint(20) not null auto_increment comment '字典编码',
    dict_sort   int(4) default 0 comment '字典排序',
    dict_label  varchar(100) default '' comment '字典标签',
    dict_value  varchar(100) default '' comment '字典键值',
    dict_type   varchar(100) default '' comment '字典类型',
    css_class   varchar(100) default null comment '样式属性（其他样式扩展）',
    list_class  varchar(100) default null comment '表格回显样式',
    is_default  char(1)      default 'N' comment '是否默认（Y是 N否）',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    constraint pk_sys_dict_data primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

/*
13、参数配置表
*/
drop table if exists sys_config;
create table sys_config
(
    config_id    int(5) not null auto_increment comment '参数主键',
    config_name  varchar(100) default '' comment '参数名称',
    config_key   varchar(100) default '' comment '参数键名',
    config_value varchar(500) default '' comment '参数键值',
    config_type  char(1)      default 'N' comment '系统内置（Y是 N否）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    constraint pk_sys_config primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

/*
14、系统访问记录
*/
create table sys_logininfor
(
    info_id        bigint(20) not null auto_increment comment '访问ID',
    login_name     varchar(50)  default '' comment '登录账号',
    ipaddr         varchar(50)  default '' comment '登录IP地址',
    login_location varchar(255) default '' comment '登录地点',
    browser        varchar(50)  default '' comment '浏览器类型',
    os             varchar(50)  default '' comment '操作系统',
    status         char(1)      default '0' comment '登录状态（0成功 1失败）',
    msg            varchar(255) default '' comment '提示消息',
    login_time     datetime comment '访问时间',
    constraint pk_sys_logininfor primary key (info_id)
) engine=innodb auto_increment=100 comment = '系统访问记录';

/*
15、在线用户记录
*/
create table sys_user_online
(
    sessionId        varchar(50)  default '' comment '用户会话id',
    login_name       varchar(50)  default '' comment '登录账号',
    dept_name        varchar(50)  default '' comment '部门名称',
    ipaddr           varchar(50)  default '' comment '登录IP地址',
    login_location   varchar(255) default '' comment '登录地点',
    browser          varchar(50)  default '' comment '浏览器类型',
    os               varchar(50)  default '' comment '操作系统',
    status           varchar(10)  default '' comment '在线状态on_line在线off_line离线',
    start_timestamp  datetime comment 'session创建时间',
    last_access_time datetime comment 'session最后访问时间',
    expire_time      int(5) default 0 comment '超时时间，单位为分钟',
    user_id          varchar(32) comment '人员id',
    constraint pk_sys_user_online primary key (sessionId)
) engine=innodb comment = '在线用户记录';

/*
16、定时任务调度表
*/
create table sys_job
(
    job_id          bigint(20) not null auto_increment comment '任务ID',
    job_name        varchar(64)  default '' comment '任务名称',
    job_group       varchar(64)  default 'DEFAULT' comment '任务组名',
    invoke_target   varchar(500) not null comment '调用目标字符串',
    cron_expression varchar(255) default '' comment 'cron执行表达式',
    misfire_policy  varchar(20)  default '3' comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    concurrent      char(1)      default '1' comment '是否并发执行（0允许 1禁止）',
    status          char(1)      default '0' comment '状态（0正常 1暂停）',
    create_by       varchar(64)  default '' comment '创建者',
    create_time     datetime comment '创建时间',
    update_by       varchar(64)  default '' comment '更新者',
    update_time     datetime comment '更新时间',
    remark          varchar(500) default '' comment '备注信息',
    constraint pk_sys_job primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

/*
17、定时任务调度日志表
*/
create table sys_job_log
(
    job_log_id     bigint(20) not null auto_increment comment '任务日志ID',
    job_name       varchar(64)  not null comment '任务名称',
    job_group      varchar(64)  not null comment '任务组名',
    invoke_target  varchar(500) not null comment '调用目标字符串',
    job_message    varchar(500) comment '日志信息',
    status         char(1)       default '0' comment '执行状态（0正常 1失败）',
    exception_info varchar(2000) default '' comment '异常信息',
    create_time    datetime comment '创建时间',
    method_name    varchar(500) null,
    method_params  varchar(200)  default null,
    constraint pk_sys_job_log primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


/*
18、通知公告表
*/
create table sys_notice
(
    notice_id      int(4) not null auto_increment comment '公告ID',
    notice_title   varchar(50) not null comment '公告标题',
    notice_type    char(1)     not null comment '公告类型（1通知 2公告）',
    notice_content varchar(2000) default null comment '公告内容',
    status         char(1)       default '0' comment '公告状态（0正常 1关闭）',
    create_by      varchar(64)   default '' comment '创建者',
    create_time    datetime comment '创建时间',
    update_by      varchar(64)   default '' comment '更新者',
    update_time    datetime comment '更新时间',
    remark         varchar(255)  default null comment '备注',
    constraint pk_sys_notice primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

/*
19、代码生成业务表
*/
create table gen_table
(
    table_id          bigint(20) not null auto_increment comment '编号',
    table_name        varchar(200) default '' comment '表名称',
    table_comment     varchar(500) default '' comment '表描述',
    sub_table_name    varchar(64)  default null comment '关联子表的表名',
    sub_table_fk_name varchar(64)  default null comment '子表关联的外键名',
    class_name        varchar(100) default '' comment '实体类名称',
    tpl_category      varchar(200) default 'crud' comment '使用的模板（crud单表操作 tree树表操作 sub主子表操作）',
    package_name      varchar(100) comment '生成包路径',
    module_name       varchar(30) comment '生成模块名',
    business_name     varchar(30) comment '生成业务名',
    function_name     varchar(50) comment '生成功能名',
    function_author   varchar(50) comment '生成功能作者',
    gen_type          char(1)      default '0' comment '生成代码方式（0zip压缩包 1自定义路径）',
    gen_path          varchar(200) default '/' comment '生成路径（不填默认项目路径）',
    options           varchar(1000) comment '其它生成选项',
    create_by         varchar(64)  default '' comment '创建者',
    create_time       datetime comment '创建时间',
    update_by         varchar(64)  default '' comment '更新者',
    update_time       datetime comment '更新时间',
    remark            varchar(500) default null comment '备注',
    constraint pk_gen_table primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';

/*
20、代码生成业务表字段
*/
create table gen_table_column
(
    column_id      bigint(20) not null auto_increment comment '编号',
    table_id       varchar(64) comment '归属表编号',
    column_name    varchar(200) comment '列名称',
    column_comment varchar(500) comment '列描述',
    column_type    varchar(100) comment '列类型',
    java_type      varchar(500) comment 'JAVA类型',
    java_field     varchar(200) comment 'JAVA字段名',
    is_pk          char(1) comment '是否主键（1是）',
    is_increment   char(1) comment '是否自增（1是）',
    is_required    char(1) comment '是否必填（1是）',
    is_insert      char(1) comment '是否为插入字段（1是）',
    is_edit        char(1) comment '是否编辑字段（1是）',
    is_list        char(1) comment '是否列表字段（1是）',
    is_query       char(1) comment '是否查询字段（1是）',
    query_type     varchar(200) default 'EQ' comment '查询方式（等于、不等于、大于、小于、范围）',
    html_type      varchar(200) comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
    dict_type      varchar(200) default '' comment '字典类型',
    sort           int comment '排序',
    create_by      varchar(64)  default '' comment '创建者',
    create_time    datetime comment '创建时间',
    update_by      varchar(64)  default '' comment '更新者',
    update_time    datetime comment '更新时间',
    constraint pk_gen_table_column primary key (column_id)
    /*,primary key (column_id)*/
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

/*
业务事件单考核超时表
*/
create table FM_BIZ_OVERTIME
(
    id            VARCHAR(36) not null comment '主键',
    singel_number VARCHAR(36) comment '单号',
    datetime      VARCHAR(20) comment '日期',
    system_id     VARCHAR(36) comment '所涉系统ID',
    proportion    VARCHAR(36) comment '所占权重',
    ifjjtype      VARCHAR(2) comment '是否紧急  1是  2否'
) comment = '业务事件单考核超时表';

/*
2021业务事件考核表
*/
create table FM_BIZ_KH
(
    id             VARCHAR(36) not null,
    sysid          VARCHAR(36) comment '系统id',
    datetime       VARCHAR(20) comment '考核日期',
    completecount  VARCHAR(20) comment '关闭事件单数量',
    jjcount        VARCHAR(20) comment '紧急事件单超时数量',
    fjjcount       VARCHAR(20) comment '非紧急事件单超时数量',
    satisfaction   VARCHAR(20) comment '满意率',
    unsatisfactory VARCHAR(20) comment '不满意率',
    SYSNAME        VARCHAR(64) comment '不满意率'
) comment = '2021业务事件考核表';

/*

*/
create table FM_BIZ_SCRIPT
(
    fbs_id           VARCHAR(36) not null comment '主键ID',
    fm_id            VARCHAR(36) comment '业务事件单ID',
    executor_id      VARCHAR(36) comment '执行人ID',
    executor_groupid VARCHAR(36) comment '执行工作组ID',
    executor_result  VARCHAR(2048) comment '执行结果',
    result_status    VARCHAR(32) comment '执行状态',
    script_name      VARCHAR(256) comment '脚本名称',
    script_para      VARCHAR(1024) comment '脚本参数',
    script_id        VARCHAR(128) comment '脚本ID',
    executor_time    VARCHAR(36) comment '执行时间',
    executor_endtime VARCHAR(32) comment '执行结束时间',
    flow_id          VARCHAR(32) comment '执行任务ID',
    type             VARCHAR(2),
    script_version   VARCHAR(32)
) comment = '2021业务事件考核表';

-- ----------------------------
-- 27、参数管理类别设置表
-- ----------------------------

CREATE TABLE SYS_CATEGORY
(
    CATEGORY_ID        int NOT NULL primary key auto_increment COMMENT '类别',
    CATEGORY_TYPE_CODE VARCHAR(255) COMMENT '类别类型编码',
    CATEGORY_TYPE_NAME VARCHAR(255) COMMENT '类别类型名称',
    CATEGORY_PARENT_ID int COMMENT '父类别ID',
    CATEGORY_CODE      VARCHAR(255) COMMENT '类被编码',
    CATEGORY_NAME      VARCHAR(255) COMMENT '类别名称',
    ANCESTORS          VARCHAR(255) COMMENT '祖先关系',
    ORDER_NUM          int COMMENT '类别顺序',
    REMARK             VARCHAR(255) COMMENT '备注',
    DEL_FLAG           VARCHAR(255) DEFAULT 0 COMMENT '是否删除',
    CREATE_BY          VARCHAR(255) COMMENT '创建人',
    CREATE_TIME        datetime COMMENT '创建时间',
    UPdate_BY          VARCHAR(7) COMMENT '更新人',
    UPdate_TIME        datetime COMMENT '更新时间',
    TIER               VARCHAR(255) COMMENT '层级别'

);

-- ----------------------------
-- 28、人员管理表
-- ----------------------------

CREATE TABLE PUB_PERSONNEL_MANAGEMENT
(
    PID               VARCHAR(32) NOT NULL COMMENT '人员ID',
    ORGID             VARCHAR(32) COMMENT '机构ID',
    PNAME             VARCHAR(16) COMMENT '人员名称',
    SEX               VARCHAR(3) COMMENT '性别',
    EDU               VARCHAR(3) COMMENT '教育程度',
    BIRTHDAY          datetime COMMENT '生日',
    BIRTHPLACE        VARCHAR(32) COMMENT '出生地点',
    PHONE             VARCHAR(32) COMMENT '电话',
    MOBLE_PHONE       VARCHAR(32) COMMENT '移动电话',
    EMALL             VARCHAR(32) COMMENT '邮箱',
    ADDRESS           VARCHAR(64) COMMENT '地址',
    ADDER             VARCHAR(32) COMMENT '添加人',
    ADDTIME           VARCHAR(10) COMMENT '添加时间',
    MODER             VARCHAR(32) COMMENT '修改人',
    MOME              VARCHAR(512) COMMENT '备注',
    UPdate_TIME       VARCHAR(10) COMMENT '更新时间',
    INVALIDATION_MARK VARCHAR(2) COMMENT '无效标识',
    POSITION          VARCHAR(32) COMMENT '职位',
    LEADER            VARCHAR(2) COMMENT '领导标志',
    P_ORDER           VARCHAR(8) COMMENT '人员序列',
    P_FLAG            VARCHAR(2) COMMENT '人员标志'
);


-- ----------------------------
-- 28、岗位与角色关联表
-- ----------------------------


CREATE TABLE SYS_POST_ROLE
(
    POST_ID int(32) NOT NULL,
    ROLE_ID int(32) NOT NULL,
    primary key (POST_ID, ROLE_ID)
);


create table ACT_GE_PROPERTY
(
    NAME_  NVARCHAR(64),
    VALUE_ NVARCHAR(300),
    REV_   INTEGER,
    primary key (NAME_)
);

create table ACT_GE_BYTEARRAY
(
    ID_            NVARCHAR(64),
    REV_           INTEGER,
    NAME_          NVARCHAR(255),
    DEPLOYMENT_ID_ NVARCHAR(64),
    BYTES_         longblob,
    GENERATED_     DECIMAL(1, 0) CHECK (GENERATED_ IN (1, 0)),
    primary key (ID_)
);

create table ACT_RE_DEPLOYMENT
(
    ID_             NVARCHAR(64),
    NAME_           NVARCHAR(255),
    CATEGORY_       NVARCHAR(255),
    KEY_            NVARCHAR(255),
    TENANT_ID_      NVARCHAR(255) DEFAULT '',
    DEPLOY_TIME_    datetime,
    ENGINE_VERSION_ NVARCHAR(255),
    primary key (ID_)
);


create table ACT_RE_MODEL
(
    ID_                           NVARCHAR(64) not null primary key,
    REV_                          INTEGER,
    NAME_                         NVARCHAR(255),
    KEY_                          NVARCHAR(255),
    CATEGORY_                     NVARCHAR(255),
    CREATE_TIME_                  datetime,
    LAST_UPdate_TIME_             datetime,
    VERSION_                      INTEGER,
    META_INFO_                    NVARCHAR(2000),
    DEPLOYMENT_ID_                NVARCHAR(64),
    EDITOR_SOURCE_VALUE_ID_       NVARCHAR(64),
    EDITOR_SOURCE_EXTRA_VALUE_ID_ NVARCHAR(64),
    TENANT_ID_                    NVARCHAR(255) DEFAULT ''
);


create table ACT_RU_TIMER_JOB
(
    ID_                  NVARCHAR(64) NOT NULL primary key,
    REV_                 INTEGER,
    TYPE_                NVARCHAR(255) NOT NULL,
    LOCK_EXP_TIME_       datetime,
    LOCK_OWNER_          NVARCHAR(255),
    EXCLUSIVE_           DECIMAL(1, 0),
    EXECUTION_ID_        NVARCHAR(64),
    PROCESS_INSTANCE_ID_ NVARCHAR(64),
    PROC_DEF_ID_         NVARCHAR(64),
    RETRIES_             INTEGER,
    EXCEPTION_STACK_ID_  NVARCHAR(64),
    EXCEPTION_MSG_       NVARCHAR(2000),
    DUEDATE_             datetime,
    REPEAT_              NVARCHAR(255),
    HANDLER_TYPE_        NVARCHAR(255),
    HANDLER_CFG_         NVARCHAR(2000),
    TENANT_ID_           NVARCHAR(255) DEFAULT '',
    CHECK (EXCLUSIVE_ IN (1, 0))
);

create table ACT_RU_EXECUTION
(
    ID_                   NVARCHAR(64) primary key,
    REV_                  INTEGER,
    PROC_INST_ID_         NVARCHAR(64),
    BUSINESS_KEY_         NVARCHAR(255),
    PARENT_ID_            NVARCHAR(64),
    PROC_DEF_ID_          NVARCHAR(64),
    SUPER_EXEC_           NVARCHAR(64),
    ROOT_PROC_INST_ID_    NVARCHAR(64),
    ACT_ID_               NVARCHAR(255),
    IS_ACTIVE_            DECIMAL(1, 0),
    IS_CONCURRENT_        DECIMAL(1, 0),
    IS_SCOPE_             DECIMAL(1, 0),
    IS_EVENT_SCOPE_       DECIMAL(1, 0),
    IS_MI_ROOT_           DECIMAL(1, 0),
    SUSPENSION_STATE_     INTEGER,
    CACHED_ENT_STATE_     INTEGER,
    TENANT_ID_            NVARCHAR(255) DEFAULT '',
    NAME_                 NVARCHAR(255),
    START_TIME_           datetime,
    START_USER_ID_        NVARCHAR(255),
    LOCK_TIME_            datetime,
    IS_COUNT_ENABLED_     DECIMAL(1, 0),
    EVT_SUBSCR_COUNT_     INTEGER,
    TASK_COUNT_           INTEGER,
    JOB_COUNT_            INTEGER,
    TIMER_JOB_COUNT_      INTEGER,
    SUSP_JOB_COUNT_       INTEGER,
    DEADLETTER_JOB_COUNT_ INTEGER,
    VAR_COUNT_            INTEGER,
    ID_LINK_COUNT_        INTEGER,
    CHECK (COMMENT_ACTIVE_ IN (1, 0)),
    CHECK (COMMENT_CONCURRENT_ IN (1, 0)),
    CHECK (COMMENT_SCOPE_ IN (1, 0)),
    CHECK (COMMENT_EVENT_SCOPE_ IN (1, 0)),
    CHECK (COMMENT_MI_ROOT_ IN (1, 0)),
    CHECK (COMMENT_COUNT_ENABLED_ IN (1, 0))
);


create table ACT_RU_JOB
(
    ID_                  NVARCHAR(64) NOT NULL primary key,
    REV_                 INTEGER,
    TYPE_                NVARCHAR(255) NOT NULL,
    LOCK_EXP_TIME_       datetime,
    LOCK_OWNER_          NVARCHAR(255),
    EXCLUSIVE_           DECIMAL(1, 0),
    EXECUTION_ID_        NVARCHAR(64),
    PROCESS_INSTANCE_ID_ NVARCHAR(64),
    PROC_DEF_ID_         NVARCHAR(64),
    RETRIES_             INTEGER,
    EXCEPTION_STACK_ID_  NVARCHAR(64),
    EXCEPTION_MSG_       NVARCHAR(2000),
    DUEDATE_             datetime,
    REPEAT_              NVARCHAR(255),
    HANDLER_TYPE_        NVARCHAR(255),
    HANDLER_CFG_         NVARCHAR(2000),
    TENANT_ID_           NVARCHAR(255) DEFAULT '',
    CHECK (EXCLUSIVE_ IN (1, 0))
);

create table ACT_RU_SUSPENDED_JOB
(
    ID_                  NVARCHAR(64) NOT NULL primary key,
    REV_                 INTEGER,
    TYPE_                NVARCHAR(255) NOT NULL,
    EXCLUSIVE_           DECIMAL(1, 0),
    EXECUTION_ID_        NVARCHAR(64),
    PROCESS_INSTANCE_ID_ NVARCHAR(64),
    PROC_DEF_ID_         NVARCHAR(64),
    RETRIES_             INTEGER,
    EXCEPTION_STACK_ID_  NVARCHAR(64),
    EXCEPTION_MSG_       NVARCHAR(2000),
    DUEDATE_             datetime,
    REPEAT_              NVARCHAR(255),
    HANDLER_TYPE_        NVARCHAR(255),
    HANDLER_CFG_         NVARCHAR(2000),
    TENANT_ID_           NVARCHAR(255) DEFAULT '',
    CHECK (EXCLUSIVE_ IN (1, 0))
);

create table ACT_RU_DEADLETTER_JOB
(
    ID_                  NVARCHAR(64) NOT NULL primary key,
    REV_                 INTEGER,
    TYPE_                NVARCHAR(255) NOT NULL,
    EXCLUSIVE_           DECIMAL(1, 0),
    EXECUTION_ID_        NVARCHAR(64),
    PROCESS_INSTANCE_ID_ NVARCHAR(64),
    PROC_DEF_ID_         NVARCHAR(64),
    EXCEPTION_STACK_ID_  NVARCHAR(64),
    EXCEPTION_MSG_       NVARCHAR(2000),
    DUEDATE_             datetime,
    REPEAT_              NVARCHAR(255),
    HANDLER_TYPE_        NVARCHAR(255),
    HANDLER_CFG_         NVARCHAR(2000),
    TENANT_ID_           NVARCHAR(255) DEFAULT '',
    CHECK (EXCLUSIVE_ IN (1, 0))
);

create table ACT_RE_PROCDEF
(
    ID_                     NVARCHAR(64) NOT NULL primary key,
    REV_                    INTEGER,
    CATEGORY_               NVARCHAR(255),
    NAME_                   NVARCHAR(255),
    KEY_                    NVARCHAR(255) NOT NULL,
    VERSION_                INTEGER NOT NULL,
    DEPLOYMENT_ID_          NVARCHAR(64),
    RESOURCE_NAME_          NVARCHAR(2000),
    DGRM_RESOURCE_NAME_     varchar(4000),
    DESCRIPTION_            NVARCHAR(2000),
    HAS_START_FORM_KEY_     DECIMAL(1, 0),
    HAS_GRAPHICAL_NOTATION_ DECIMAL(1, 0),
    SUSPENSION_STATE_       INTEGER,
    TENANT_ID_              NVARCHAR(255) DEFAULT '',
    ENGINE_VERSION_         NVARCHAR(255),
    CHECK (HAS_START_FORM_KEY_ IN (1, 0)),
    CHECK (HAS_GRAPHICAL_NOTATION_ IN (1, 0))
);

create table ACT_RU_TASK
(
    ID_               NVARCHAR(64) primary key,
    REV_              INTEGER,
    EXECUTION_ID_     NVARCHAR(64),
    PROC_INST_ID_     NVARCHAR(64),
    PROC_DEF_ID_      NVARCHAR(64),
    NAME_             NVARCHAR(255),
    PARENT_TASK_ID_   NVARCHAR(64),
    DESCRIPTION_      NVARCHAR(2000),
    TASK_DEF_KEY_     NVARCHAR(255),
    OWNER_            NVARCHAR(255),
    ASSIGNEE_         NVARCHAR(255),
    DELEGATION_       NVARCHAR(64),
    PRIORITY_         INTEGER,
    CREATE_TIME_      datetime,
    DUE_DATE_         datetime,
    CATEGORY_         NVARCHAR(255),
    SUSPENSION_STATE_ INTEGER,
    TENANT_ID_        NVARCHAR(255) DEFAULT '',
    FORM_KEY_         NVARCHAR(255),
    CLAIM_TIME_       datetime
);

create table ACT_RU_IDENTITYLINK
(
    ID_           NVARCHAR(64) primary key,
    REV_          INTEGER,
    GROUP_ID_     NVARCHAR(255),
    TYPE_         NVARCHAR(255),
    USER_ID_      NVARCHAR(255),
    TASK_ID_      NVARCHAR(64),
    PROC_INST_ID_ NVARCHAR(64),
    PROC_DEF_ID_  NVARCHAR(64)
);



create table ACT_RU_EVENT_SUBSCR
(
    ID_            NVARCHAR(64) not null primary key,
    REV_           integer,
    EVENT_TYPE_    NVARCHAR(255) not null,
    EVENT_NAME_    NVARCHAR(255),
    EXECUTION_ID_  NVARCHAR(64),
    PROC_INST_ID_  NVARCHAR(64),
    ACTIVITY_ID_   NVARCHAR(64),
    CONFIGURATION_ NVARCHAR(255),
    CREATED_       datetime not null,
    PROC_DEF_ID_   NVARCHAR(64),
    TENANT_ID_     NVARCHAR(255) DEFAULT ''
);

create table ACT_EVT_LOG
(
    LOG_NR_            int(19) primary key,
    TYPE_              NVARCHAR(64),
    PROC_DEF_ID_       NVARCHAR(64),
    PROC_INST_ID_      NVARCHAR(64),
    EXECUTION_ID_      NVARCHAR(64),
    TASK_ID_           NVARCHAR(64),
    TIME_STAMP_        datetime not null,
    USER_ID_           NVARCHAR(255),
    DATA_              text,
    LOCK_OWNER_        NVARCHAR(255),
    LOCK_TIME_         datetime null,
    COMMENT_PROCESSED_ int(3) default 0
);


create table ACT_PROCDEF_INFO
(
    ID_           NVARCHAR(64) not null primary key,
    PROC_DEF_ID_  NVARCHAR(64) not null,
    REV_          integer,
    INFO_JSON_ID_ NVARCHAR(64)
);

create table ACT_ID_GROUP
(
    ID_   NVARCHAR(64) primary key,
    REV_  INTEGER,
    NAME_ NVARCHAR(255),
    TYPE_ NVARCHAR(255)
);

create table ACT_ID_MEMBERSHIP
(
    USER_ID_  NVARCHAR(64),
    GROUP_ID_ NVARCHAR(64),
    primary key (USER_ID_, GROUP_ID_)
);

create table ACT_ID_USER
(
    ID_         NVARCHAR(64) primary key,
    REV_        INTEGER,
    FIRST_      NVARCHAR(255),
    LAST_       NVARCHAR(255),
    EMAIL_      NVARCHAR(255),
    PWD_        NVARCHAR(255),
    PICTURE_ID_ NVARCHAR(64)
);

create table ACT_ID_INFO
(
    ID_        NVARCHAR(64) primary key,
    REV_       INTEGER,
    USER_ID_   NVARCHAR(64),
    TYPE_      NVARCHAR(64),
    KEY_       NVARCHAR(255),
    VALUE_     NVARCHAR(255),
    PASSWORD_  text,
    PARENT_ID_ NVARCHAR(255)
);


create table ACT_HI_PROCINST
(
    ID_                        NVARCHAR(64) not null primary key,
    PROC_INST_ID_              NVARCHAR(64) not null,
    BUSINESS_KEY_              NVARCHAR(255),
    PROC_DEF_ID_               NVARCHAR(64) not null,
    START_TIME_                datetime not null,
    END_TIME_                  datetime,
    DURATION_                  DECIMAL(19, 0),
    START_USER_ID_             NVARCHAR(255),
    START_ACT_ID_              NVARCHAR(255),
    END_ACT_ID_                NVARCHAR(255),
    SUPER_PROCESS_INSTANCE_ID_ NVARCHAR(64),
    DELETE_REASON_             NVARCHAR(2000),
    TENANT_ID_                 NVARCHAR(255) default '',
    NAME_                      NVARCHAR(255),
    unique (PROC_INST_ID_)
);

create table ACT_HI_ACTINST
(
    ID_                NVARCHAR(64) not null primary key,
    PROC_DEF_ID_       NVARCHAR(64) not null,
    PROC_INST_ID_      NVARCHAR(64) not null,
    EXECUTION_ID_      NVARCHAR(64) not null,
    ACT_ID_            NVARCHAR(255) not null,
    TASK_ID_           NVARCHAR(64),
    CALL_PROC_INST_ID_ NVARCHAR(64),
    ACT_NAME_          NVARCHAR(255),
    ACT_TYPE_          NVARCHAR(255) not null,
    ASSIGNEE_          NVARCHAR(255),
    START_TIME_        datetime not null,
    END_TIME_          datetime,
    DURATION_          DECIMAL(19, 0),
    DELETE_REASON_     NVARCHAR(2000),
    TENANT_ID_         NVARCHAR(255) default ''
);

create table ACT_HI_TASKINST
(
    ID_             NVARCHAR(64) not null primary key,
    PROC_DEF_ID_    NVARCHAR(64),
    TASK_DEF_KEY_   NVARCHAR(255),
    PROC_INST_ID_   NVARCHAR(64),
    EXECUTION_ID_   NVARCHAR(64),
    PARENT_TASK_ID_ NVARCHAR(64),
    NAME_           NVARCHAR(255),
    DESCRIPTION_    NVARCHAR(2000),
    OWNER_          NVARCHAR(255),
    ASSIGNEE_       NVARCHAR(255),
    START_TIME_     datetime not null,
    CLAIM_TIME_     datetime,
    END_TIME_       datetime,
    DURATION_       DECIMAL(19, 0),
    DELETE_REASON_  NVARCHAR(2000),
    PRIORITY_       INTEGER,
    DUE_DATE_       datetime,
    FORM_KEY_       NVARCHAR(255),
    CATEGORY_       NVARCHAR(255),
    TENANT_ID_      NVARCHAR(255) default ''
);

create table ACT_HI_COMMENT
(
    ID_           NVARCHAR(64) not null primary key,
    TYPE_         NVARCHAR(255),
    TIME_         datetime not null,
    USER_ID_      NVARCHAR(255),
    TASK_ID_      NVARCHAR(64),
    PROC_INST_ID_ NVARCHAR(64),
    ACTION_       NVARCHAR(255),
    MESSAGE_      NVARCHAR(2000),
    FULL_MSG_     text
);

create table ACT_HI_ATTACHMENT
(
    ID_           NVARCHAR(64) not null primary key,
    REV_          INTEGER,
    USER_ID_      NVARCHAR(255),
    NAME_         NVARCHAR(255),
    DESCRIPTION_  NVARCHAR(2000),
    TYPE_         NVARCHAR(255),
    TASK_ID_      NVARCHAR(64),
    PROC_INST_ID_ NVARCHAR(64),
    URL_          NVARCHAR(2000),
    CONTENT_ID_   NVARCHAR(64),
    TIME_         datetime
);


create table ACT_HI_IDENTITYLINK
(
    ID_           NVARCHAR(64) primary key,
    GROUP_ID_     NVARCHAR(255),
    TYPE_         NVARCHAR(255),
    USER_ID_      NVARCHAR(255),
    TASK_ID_      NVARCHAR(64),
    PROC_INST_ID_ NVARCHAR(64)
);


CREATE TABLE biz_leave
(
    id                 int(19) NOT NULL PRIMARY KEY COMMENT '主键ID',
    type               varchar(20) COMMENT '请假类型',
    title              VARCHAR(100) COMMENT '标题',
    reason             VARCHAR(500) COMMENT '原因',
    start_time         datetime COMMENT '开始时间',
    end_time           datetime COMMENT '结束时间',
    total_time         int(11) COMMENT '请假时长，单位秒',
    instance_id        VARCHAR(32) COMMENT '流程实例ID',
    create_by          VARCHAR(64) COMMENT '创建者',
    create_time        datetime COMMENT '创建时间',
    update_by          VARCHAR(64) COMMENT '更新者',
    update_time        datetime COMMENT '更新时间',
    apply_user         VARCHAR(64) COMMENT '申请人',
    apply_time         datetime COMMENT '申请时间',
    reality_start_time datetime COMMENT '实际开始时间',
    reality_end_time   datetime COMMENT '实际结束时间'
) COMMENT '请假表';


CREATE TABLE ACCOUNT_REQ
(
    ID_             VARCHAR(100) NOT NULL,
    NAME_           VARCHAR(50),
    MOB_PHONE       VARCHAR(50),
    GENDER          VARCHAR(3)  DEFAULT 0,
    DEPT            VARCHAR(50),
    AUDIT_DEPT      VARCHAR(50),
    MARK            VARCHAR(3)  DEFAULT 0,
    EDUCATION       VARCHAR(3),
    BIRTH_PLACE     VARCHAR(100),
    JOB_            VARCHAR(50),
    DIRECTOR        VARCHAR(50),
    ORDER_          VARCHAR(7),
    BIRTHDAY        VARCHAR(50),
    OFFICE_PHONE    VARCHAR(50),
    EMAIL           VARCHAR(100),
    ADDRESS         VARCHAR(100),
    REMARK          VARCHAR(255),
    AUDIT_SUG       VARCHAR(255),
    DEAL_SUG        VARCHAR(255),
    CREATE_BY       VARCHAR(64),
    CREATE_TIME     datetime,
    UPdatetime_BY   VARCHAR(64) DEFAULT NULL,
    UPdatetime_TIME datetime,
    STATUS          VARCHAR(3),
    AUDITOR         VARCHAR(50),
    DEALER          VARCHAR(50),
    OTHER_PHONE     VARCHAR(255),
    DEAL_DEPT       VARCHAR(255),
    REQ_TYPE        VARCHAR(2),
    PID             VARCHAR(100)
);

CREATE TABLE GROUP_REQ
(
    ID_             VARCHAR(100) NOT NULL,
    NAME_           VARCHAR(50),
    PHONE           VARCHAR(50),
    DEPT            VARCHAR(50),
    SYS             VARCHAR(50),
    NOTE            VARCHAR(255),
    DIRECTOR        VARCHAR(3),
    MARK            VARCHAR(3),
    AUDIT_DEPT      VARCHAR(50),
    AUDITOR         VARCHAR(50),
    DEAL_DEPT       VARCHAR(50),
    DEALER          VARCHAR(50),
    AUDIT_SUG       VARCHAR(255),
    DEAL_SUG        VARCHAR(255),
    STATUS          VARCHAR(3),
    CREATE_TIME     datetime,
    CREATE_BY       VARCHAR(50),
    UPdatetime_TIME datetime,
    UPdatetime_BY   VARCHAR(50),
    REMARK          VARCHAR(255),
    REQ_TYPE        VARCHAR(2),
    GROUPID         VARCHAR(100)
);



create table FM_KIND
(
    fm_type_id        VARCHAR(36) not null,
    name              VARCHAR(64),
    sys_id            VARCHAR(36),
    invalidation_mark VARCHAR(2),
    knowledge_url     VARCHAR(128),
    adder_id          VARCHAR(36),
    updatetimer_id    VARCHAR(36),
    add_time          VARCHAR(16),
    updatetime_time   VARCHAR(16),
    type              VARCHAR(2),
    serial_num        VARCHAR(8),
    parent_id         VARCHAR(36)
);

create table FM_SYS_D_TIME
(
    fm_sys_d_time_id VARCHAR(36) not null,
    biz_id           VARCHAR(36),
    sys_id           VARCHAR(36),
    perform_name     VARCHAR(64),
    deal_use_time    VARCHAR(20),
    dealid           VARCHAR(36),
    dealtime         VARCHAR(20),
    group_id         VARCHAR(32)
);

create table YOUI_TASK_LOCK
(
    task_name  VARCHAR(64) not null,
    lock_value VARCHAR(2)
);


create table CM_OS_RECOVERY_REGCOMMENTTER
(
    recovery_regCOMMENTter_id VARCHAR(36) not null primary key,
    centre_name               VARCHAR(50),
    sys_id                    VARCHAR(36),
    sys_name                  VARCHAR(100),
    host_name                 VARCHAR(100),
    backup_datetime           VARCHAR(50),
    recovery_start_time       VARCHAR(50),
    recovery_end_time         VARCHAR(50),
    recovery_time             VARCHAR(50),
    verification_result       VARCHAR(255),
    remark                    VARCHAR(500),
    data_size                 VARCHAR(50),
    type                      VARCHAR(2),
    line_start                VARCHAR(2)
);


create table CM_DB_RECOVERY_REGCOMMENTTER
(
    recovery_regCOMMENTter_id VARCHAR(36) not null primary key,
    centre_name               VARCHAR(50),
    sys_id                    VARCHAR(36),
    sys_name                  VARCHAR(100),
    host_name                 VARCHAR(100),
    backup_datetime           VARCHAR(50),
    recovery_start_time       VARCHAR(50),
    recovery_end_time         VARCHAR(50),
    recovery_time             VARCHAR(50),
    verification_result       VARCHAR(255),
    remark                    VARCHAR(500),
    data_size                 VARCHAR(50),
    type                      VARCHAR(2),
    line_start                VARCHAR(2)
);

-- 创建知识分类表
create table KNOWLEDGE_CATEGORY
(
    CATEGORY_ID   VARCHAR(32) not null,
    CREATE_BY     VARCHAR(32),
    CREATE_DATE   VARCHAR(32),
    UPDATE_BY     VARCHAR(32),
    UPDATE_DATE   VARCHAR(32),
    CATEGORY_NAME VARCHAR(32),
    PARENT_ID     VARCHAR(32),
    REMARK        VARCHAR(256),
    STATUS        VARCHAR(2),
    EVENT_TYPE    VARCHAR(2)
) COMMENT '知识分类';

-- 创建知识收藏表
create table KNOWLEDGE_COLLECT
(
    ID          VARCHAR(32) not null COMMENT 'id',
    CREATE_ID   VARCHAR(32) COMMENT '人员id',
    CONTENT_ID  VARCHAR(32) COMMENT '知识id',
    STATUS      VARCHAR(2) COMMENT '（0收藏，1推荐，2下载）',
    CREATE_TIME VARCHAR(32)
) COMMENT '知识收藏';


-- 创建知识评论表
create table KNOWLEDGE_COMMENT
(
    ID          VARCHAR(32) not null COMMENT 'id',
    CREATE_ID   VARCHAR(32) COMMENT '人员id',
    CONTENT_ID  VARCHAR(32) COMMENT '知识id',
    CREATE_TIME VARCHAR(32) COMMENT '创建时间',
    REMARK      VARCHAR(512) COMMENT '评价状态',
    STATUS      VARCHAR(2) COMMENT '评价（0好评，1差评）',
    REPLY_ID    VARCHAR(32)
) COMMENT '知识评价';


-- 创建知识流程表
create table KNOWLEDGE_OPERATION_HCOMMENTTORY
(
    ID         VARCHAR(32),
    CONTENT_ID VARCHAR(32) COMMENT '对应知识id',
    OPER_ID    VARCHAR(32) COMMENT '操作人员id',
    OPER_TIME  VARCHAR(32) COMMENT '操作时间',
    OPERATION  VARCHAR(2) COMMENT '操作内容（字典项KNOWLEDGE_OPER_TYPE）',
    REMARK     VARCHAR(255) COMMENT '备注（操作原因）'
);

-- 创建知识标题表
create table KNOWLEDGE_TITIL
(
    ID          VARCHAR(32) not null COMMENT '标题id',
    NAME        VARCHAR(64) COMMENT '标题',
    CATEGORY    VARCHAR(2) COMMENT '标题等级',
    CREATE_TIME VARCHAR(20) COMMENT '创建时间',
    CREATE_ID   VARCHAR(32) COMMENT '创建人',
    PARENT_ID   VARCHAR(32) COMMENT '父id',
    EVENT_TYPE  VARCHAR(200) COMMENT '事件类型',
    SYS_ID      VARCHAR(64),
    STATUS      VARCHAR(2)
) COMMENT '知识管理标题表';


-- 创建知识访问表
create table KNOWLEDGE_VCOMMENTITS
(
    ID          VARCHAR(32) not null COMMENT 'id',
    CONTENT_ID  VARCHAR(32) COMMENT '知识id',
    CREATE_ID   VARCHAR(32) COMMENT '创建人id',
    CREATE_TIME VARCHAR(32) COMMENT '创建时间'
) COMMENT '访问历史';

-- wpd 2021/08/26 流程的日誌  （孙大永所需）
create table TEL_FLOW_LOG
(
    log_id         VARCHAR(36) not null COMMENT 'ID',
    tel_id         VARCHAR(32) COMMENT '电话单id',
    operation_name VARCHAR(20) COMMENT '操作名称',
    create_time    VARCHAR(32) COMMENT '操作时间',
    operator       VARCHAR(32) COMMENT '操作人',
    operator_tel   VARCHAR(20) COMMENT '联系方式',
    state          VARCHAR(16) COMMENT '状态',
    serial_num     VARCHAR(4) COMMENT '序号'
);

-- tel_skill_org  技能组
create table TEL_SKILLS_ORG
(
    id                   VARCHAR(36) not null,
    og_org_id            VARCHAR(36),
    skills_group_telname VARCHAR(36),
    skills_group_name    VARCHAR(50),
    skills_status        VARCHAR(2),
    create_time          VARCHAR(36),
    updatetime_time      VARCHAR(36),
    create_person        VARCHAR(36)
);


-- ------------- 生产环境 cuicc 0826

-- ---------wangpingdu   2021/11/19-------------------
create table SYS_DEPLOYMENT_VERSION
(
    dv_id           VARCHAR(36) not null primary key,
    version_no      VARCHAR(36),
    version_content VARCHAR(4000),
    sys_name        VARCHAR(250),
    version_show    VARCHAR(3),
    deployment_time VARCHAR(36),
    creater_id      VARCHAR(20)
);

create table FM_NOTE
(
    note_id             VARCHAR(50) not null primary key,
    note_no             VARCHAR(50),
    note_name           VARCHAR(225),
    note_type           VARCHAR(225),
    note_time           VARCHAR(225),
    if_feedback         VARCHAR(225),
    feedback_abort_time VARCHAR(225),
    square              VARCHAR(225),
    file_path           VARCHAR(225),
    dCOMMENTpose_time   VARCHAR(225),
    feedback_time       VARCHAR(225),
    orgid               VARCHAR(225),
    accepting_state     VARCHAR(225),
    dCOMMENTpose_state  VARCHAR(225),
    dCOMMENTpose_id     VARCHAR(225),
    if_involve          VARCHAR(225),
    feedback_detail     VARCHAR(225),
    n1                  VARCHAR(225),
    n2                  VARCHAR(225),
    n3                  VARCHAR(225),
    n4                  VARCHAR(225),
    n5                  VARCHAR(225),
    start_time          VARCHAR(255),
    note_state          VARCHAR(5)
);

-- 创建知识库告警分析表
CREATE TABLE KNOWLEDGE_ALARM_EXAMPLE
(
    ID       VARCHAR(32) NOT NULL PRIMARY KEY,
    TITLE    VARCHAR(255) COMMENT '告警名称=知识标题',
    CONTENT  VARCHAR(255) COMMENT '对象=一级标题',
    SECTITLE VARCHAR(255) COMMENT '告警来源二级=二级标题',
    NAME     text COMMENT '告警描述三级=标签'
);


create table cm_biz_single
(
    change_id                  varchar(32) not null primary key,
    change_code                varchar(36),
    createtime                 varchar(36),
    application_submit_time    varchar(32),
    change_category_id         varchar(32),
    change_stage               varchar(32),
    expect_start_time          varchar(32),
    applicant_id               varchar(32),
    change_type                varchar(32),
    change_single_stauts       varchar(32),
    important_lev              varchar(32),
    expect_end_time            varchar(36),
    closuer_often              varchar(32),
    change_equipment           varchar(32),
    equipment_flag             varchar(32),
    group_person               varchar(32),
    change_manager_id          varchar(32),
    change_single_name         varchar(512),
    change_cause_text          varchar(2048),
    implement_supviser_id      varchar(32),
    practicle_start            varchar(36),
    practicle_end              varchar(36),
    change_affect_desccription varchar(32),
    access_id                  varchar(32),
    notes_text                 varchar(1500),
    invalidation_mark          varchar(32),
    implementor_id             varchar(32),
    creater_org_id             varchar(32),
    perform_result             varchar(2),
    checker_id                 varchar(32),
    is_stop                    varchar(2),
    change_resource            varchar(2),
    is_notice                  varchar(2),
    change_org                 varchar(36),
    implementor_orgid          varchar(36),
    check_org                  varchar(36),
    is_r_system                varchar(2),
    other_system               varchar(200),
    is_stop_real               varchar(2),
    sysname                    text,
    sysid                      text,
    xtchange_manager_id        varchar(512),
    change_xtorg               varchar(32),
    is_jjbg                    varchar(2),
    is_js                      varchar(2),
    change_reason              varchar(32),
    fuchecker_id               varchar(32),
    if_oasis                   varchar(2),
    if_auto                    varchar(2),
    if_mastart                 varchar(2),
    change_details             text,
    change_program             text,
    issue_id                   text,
    fm_id                      text,
    fuchecker_flag             varchar(2),
    if_add_work                varchar(2),
    not_auto_execution         varchar(2),
    not_execution_text         varchar(400),
    urgent_change_reason       varchar(2),
    urgent_reason_text         varchar(400),
    one_type_name              varchar(128),
    two_type_name              varchar(128),
    three_type_name            varchar(128),
    four_type_name             varchar(128)
);

create table KNOWLEDGE_BUSINESS_EXAMPLE
(
    ID          VARCHAR(32) not null,
    SYSTEM_NAME VARCHAR(255) COMMENT '业务系统名称',
    CONTENT     VARCHAR(255) COMMENT '业务一级标题',
    SECTITLE    VARCHAR(255) COMMENT '业务二级标题',
    THREETITLE  VARCHAR(255) COMMENT '业务三级标题',
    NAME        VARCHAR(255) COMMENT '业务关键字、标签',
    describes   TEXT COMMENT '业务描述'
);

-- 创建知识管理表
create table KNOWLEDGE_CONTENT
(
    ID          VARCHAR(32) not null COMMENT 'id',
    TITLE       VARCHAR(200) COMMENT '知识标题',
    DESCRIBES   TEXT COMMENT '知识描述',
    CONTENT     VARCHAR(32) COMMENT '知识内容',
    CATEGORY_ID VARCHAR(32) COMMENT '类别id',
    CREATE_ID   VARCHAR(32) COMMENT '创建人id',
    CREATE_TIME VARCHAR(32) COMMENT '创建时间',
    STATUS      VARCHAR(2) COMMENT '状态（0启用，1停用）',
    AT_ID       VARCHAR(32) COMMENT '附件id',
    FILENAME    VARCHAR(50),
    SECTITLE    VARCHAR(32),
    POWER_ID    VARCHAR(2),
    NAME        VARCHAR(400),
    EVENT_TYPE  VARCHAR(2) COMMENT '事件类型',
    THREETITLE  VARCHAR(32) COMMENT '三级标题',
    SYS_ID      VARCHAR(200) COMMENT '系统名称',
    REASON      VARCHAR(255) COMMENT '审核理由',
    ONE_AUDITOR VARCHAR(32) COMMENT '一线审核人',
    TWO_AUDITOR VARCHAR(32) COMMENT '二线审核人'
) COMMENT '知识管理';

create table ACT_HI_VARINST
(
    ID_                NVARCHAR(64) not null primary key,
    PROC_INST_ID_      NVARCHAR(64),
    EXECUTION_ID_      NVARCHAR(64),
    TASK_ID_           NVARCHAR(64),
    NAME_              NVARCHAR(255) not null,
    VAR_TYPE_          NVARCHAR(100),
    REV_               INTEGER,
    BYTEARRAY_ID_      NVARCHAR(64),
    DOUBLE_            DECIMAL(65, 10),
    LONG_              DECIMAL(19, 0),
    TEXT_              NVARCHAR(2000),
    TEXT2_             NVARCHAR(2000),
    CREATE_TIME_       datetime,
    LAST_UPDATED_TIME_ datetime
);

create table ACT_HI_DETAIL
(
    ID_           NVARCHAR(64) not null primary key,
    TYPE_         NVARCHAR(255) not null,
    PROC_INST_ID_ NVARCHAR(64),
    EXECUTION_ID_ NVARCHAR(64),
    TASK_ID_      NVARCHAR(64),
    ACT_INST_ID_  NVARCHAR(64),
    NAME_         NVARCHAR(255) not null,
    VAR_TYPE_     NVARCHAR(64),
    REV_          INTEGER,
    TIME_         datetime not null,
    BYTEARRAY_ID_ NVARCHAR(64),
    DOUBLE_       DECIMAL(65, 10),
    LONG_         DECIMAL(19, 0),
    TEXT_         NVARCHAR(2000),
    TEXT2_        NVARCHAR(2000)
);

create table ACT_RU_VARIABLE
(
    ID_           NVARCHAR(64) not null primary key,
    REV_          INTEGER,
    TYPE_         NVARCHAR(255) not null,
    NAME_         NVARCHAR(255) not null,
    EXECUTION_ID_ NVARCHAR(64),
    PROC_INST_ID_ NVARCHAR(64),
    TASK_ID_      NVARCHAR(64),
    BYTEARRAY_ID_ NVARCHAR(64),
    DOUBLE_       DECIMAL(65, 10),
    LONG_         DECIMAL(19, 0),
    TEXT_         NVARCHAR(2000),
    TEXT2_        NVARCHAR(2000)
);

-- 首页工作状态表 (6/28号已执行)
create table ITSM_WORK_STATUS
(
    work_id      VARCHAR(32) not null,
    pid          VARCHAR(32) comment '登录人id',
    user_id      VARCHAR(32) comment '用户id',
    work_status  VARCHAR(2) comment '工作状态',
    operate_time VARCHAR(64) comment '操作时间',
    constraint ITSM_WORK_STATUS primary key (work_id)
);
-- 首页工作表状态记录表 （6/29）
create table ITSM_WORK_STATUS_LOG
(
    log_id       VARCHAR(32) not null,
    work_id      VARCHAR(32) comment '工作表id',
    pid          VARCHAR(32) comment '登录人id',
    user_id      VARCHAR(32) comment '用户id',
    work_status  VARCHAR(32) comment '工作表状态',
    operate_time VARCHAR(64) comment '操作时间',
    constraint ITSM_WORK_STATUS_LOG primary key (LOG_ID)
);
-- 添加知识与问题单关联表
CREATE TABLE KNOWLEDGE_TO_ISSUEFX
(
    KNOWLEDGE_ID VARCHAR(32),
    ISSUEFX_NO   VARCHAR(32),
    ISSUEFX_ID   VARCHAR(32)
);

-- 公告通知表
CREATE TABLE AM_BIZ_NOTICE
(
    am_biz_id         varchar(36) not null comment '公告id',
    am_code           varchar(72) null comment '公告编码',
    checker_id        varchar(36) null comment '审核人id',
    create_id         varchar(36) null comment '创建人',
    description       varchar(2000) null comment '描述',
    release_date      varchar(32) null comment '发布时间',
    release_org       varchar(36) null comment '发布机构 ',
    checker_flag      varchar(2) null comment '审核标志',
    checker_advice    varchar(128) null comment '审核意见',
    invalidation_mark varchar(2) null comment '有效标志',
    checker_time      varchar(32) null comment '审核时间',
    send_range        varchar(36) null comment '发送范围 1 数据中心 2 厂商+数据中心（72号院） 3 所有用户',
    am_title          varchar(80) null comment '公告标题',
    current_status    varchar(2) null comment '状态 01新建、02待审核、03=退回,04=待阅,05=已过期',
    is_top            varchar(2) null comment '是否置顶  0 否 1 是',
    top_time          varchar(72) null comment '置顶时间',
    create_time       varchar(72) null comment '创建时间',
    top_time_flag     varchar(2) null comment '置顶时间flag 回显用 1 一天 /2 三天 /3 一周 /4 一月 /5 长期',
    am_offline        varchar(2) null comment '公告下线类型 (1、一天 2、一周 3、一个月 4、三个月 5、六个月 6、一年)',
    am_offline_time   varchar(72) null comment '公告下线时间'
) comment '公告通知表';


CREATE TABLE ID_GENERATOR
(
    ID         VARCHAR(32) NOT NULL,
    CURRENT_ID NUMERIC(19) NOT NULL,
    BIZ_TYPE   VARCHAR(32) NOT NULL,
    CUR_DATE   VARCHAR(32) NOT NULL
) comment '单号关联表';

CREATE TABLE DUTY_SCHEDULING
(
    SCHEDULING_ID VARCHAR(32) NOT NULL,
    PID           VARCHAR(1024) NULL,
    PNAME         VARCHAR(512) NULL,
    MOBILE_PHONE  VARCHAR(512) NULL,
    DUTY_DATE     VARCHAR(32) NULL,
    TYPE_NO       VARCHAR(32) NULL,
    TYPEINFO_ID   VARCHAR(32) NULL,
    LEADER        VARCHAR(2) NULL,
    ACCOUNT_PID   VARCHAR(32) NULL
--    TIME_SHIFT    VARCHAR(2) NULL,
--    COMMAND_HALL  VARCHAR(2) NULL,
) comment '值班安排表';

CREATE TABLE SM_BIZ_SCHEDULING
(
    SCHEDULING_ID          VARCHAR(36) NOT NULL PRIMARY KEY,
    SCHEDULING_NO          VARCHAR(32) NULL,
    SCHEDULING_NAME        VARCHAR(128) NULL,
    SCHEDULING_DESCRIPTION text NULL,
    CREATE_TIME            VARCHAR(32) NULL,
    EFFECTIVE_TIME         VARCHAR(32) NULL,
    CREATOR_ID             VARCHAR(36) NULL,
    CREATOR_DEPT_ID        VARCHAR(36) NULL,
    RECHECK_MARK           VARCHAR(2) NULL,
    RECHECKER_ID           VARCHAR(36) NULL,
    TASK_NUM               VARCHAR(32) NULL,
    UPDATE_TIME            VARCHAR(32) NULL,
    INVALIDATION_MARK      VARCHAR(2) NULL,
    PLAN_STATUS            VARCHAR(2) NULL,
    PROCESS                VARCHAR(64) NULL,
    PLAN_TYPE              VARCHAR(2) NULL,
    CHECK_PERSON_ID        text NULL,
    CHECK_PERSON_NAME      text NULL,
    CHECK_DESCRIPTION      text NULL,
    MASTER_ORG_ID          VARCHAR(32) NULL,
    CHECK_TIME             VARCHAR(32) NULL,
    PLAN_CHECKPASS         VARCHAR(32) NULL,
    CANCEL_FLAG            VARCHAR(2) NULL,
    FOLDER                 VARCHAR(36) NULL
);
CREATE INDEX SM_BIZ_S_IDX_CREATOR_ID ON SM_BIZ_SCHEDULING (CREATOR_ID ASC);
CREATE INDEX SM_BIZ_S_IDX_PLAN_STATUS ON SM_BIZ_SCHEDULING (PLAN_STATUS ASC);
CREATE INDEX SM_BIZ_S_IDX_SCHEDULING_NO ON SM_BIZ_SCHEDULING (SCHEDULING_NO ASC);

CREATE TABLE SM_BIZ_FOLDER
(
    ID_          VARCHAR(36) NOT NULL PRIMARY KEY,
    NAME_        VARCHAR(128) NULL,
    PATH_        VARCHAR(1024) NULL,
    PARENT_      VARCHAR(36) NULL,
    DESCRIPTION_ VARCHAR(1024) NULL,
    CREATE_USER_ VARCHAR(32) NULL,
    CREATE_TIME_ VARCHAR(20) NULL,
    MODIFY_USER_ VARCHAR(32) NULL,
    MODIFY_TIME_ VARCHAR(20) NULL,
    UPDATE_TIME_ VARCHAR(20) NULL,
    LEAF_        VARCHAR(20) NULL,
    TYPE         VARCHAR(2) NULL
);

CREATE TABLE SM_BIZ_TASK
(
    TASK_ID            VARCHAR(36) NOT NULL PRIMARY KEY,
    SCHEDULING_ID      VARCHAR(36) NULL,
    TASK_NO            VARCHAR(32) NULL,
    PERFORM_USER_NAME  text NULL,
    HANDLER_DEPT_NAME  text NULL,
    TASK_TITLE         VARCHAR(128) NULL,
    TASK_DESCRIPTION   VARCHAR(2048) NULL,
    TASK_EFFECT_DATE   VARCHAR(32) NULL,
    TASK_FAILURE_DATE  VARCHAR(32) NULL,
    CHARACTERS         VARCHAR(2) NULL,
    TASK_TYPE_ID       VARCHAR(36) NULL,
    START_TIME         VARCHAR(32) NULL,
    TASK_HOURS_WORKED  VARCHAR(64) NULL,
    TASK_CYCLE         VARCHAR(32) NULL,
    TASK_FREQUENCY     VARCHAR(32) NULL,
    UPDATE_TIME        VARCHAR(32) NULL,
    PERFORM_TIME       VARCHAR(32) NULL,
    INVALIDATION_MARK  VARCHAR(2) NULL,
    WORK_STATUS        VARCHAR(2) NULL,
    CREATE_TIME        VARCHAR(32) NULL,
    DUTY_POST_NAME     text NULL,
    PROCESS            VARCHAR(64) NULL,
    CHECKER_ID         VARCHAR(36) NULL,
    PERFORM_USER_ID    text NULL,
    HANDLER_DEPT_ID    text NULL,
    DUTY_POST_ID       VARCHAR(4000) NULL,
    HOUR               VARCHAR(4) NULL,
    MIN                VARCHAR(4) NULL,
    PERFORM_WEEKS      VARCHAR(128) NULL,
    PERFORM_MONTHS     VARCHAR(128) NULL,
    PERFORM_DAY        VARCHAR(8) NULL,
    CREATE_ORG_ID      VARCHAR(36) NULL,
    MSG_DOOR           VARCHAR(2) NULL,
    MSG_HOUR           VARCHAR(4) NULL,
    MSG_MIN            VARCHAR(4) NULL,
    SEND_RANGE         VARCHAR(36) NULL,
    DAY                VARCHAR(4) NULL,
    RECEIVE_ROLE_NAME  text NULL,
    RECEIVE_ROLE_ID    VARCHAR(2000) NULL,
    PERFORM_DEPT_NAME  text NULL,
    PERFORM_DEPT_ID    text NULL,
    PERFORM_GROUP_ID   text NULL,
    PERFORM_GROUP_NAME text NULL
);
CREATE INDEX SM_BIZ_T_IDX_SCHEDULING_ID ON SM_BIZ_TASK (SCHEDULING_ID ASC);
CREATE INDEX SM_BIZ_T_IDX_TASK_NO ON SM_BIZ_TASK (TASK_NO ASC);

CREATE TABLE SM_BIZ_LXBGAPPLY
(
    ID             VARCHAR(36) NOT NULL PRIMARY KEY comment '申请id',
    SCHEDULING_ID  VARCHAR(36) NULL comment '计划单id',
    CREATE_ID      VARCHAR(36) NULL comment '创建人id',
    REASON         VARCHAR(4000) NULL comment '原因',
    CREATE_TIME    VARCHAR(36) NULL comment '创建时间',
    RELEASE_TIME   VARCHAR(32) NULL comment '计划发布时间',
    CHECK_ID       VARCHAR(36) NULL comment '处长审核人id',
    APP_TYPE       VARCHAR(255) NULL comment '状态',
    CTOR_CHECKTEXT text NULL comment '处长审核意见',
    JH_CHECKTEXT   text comment '计划审核人审核意见'
) comment '例行变更计划申请延期表';

CREATE TABLE AM_BIZ_RECEIVE
(
    AM_RECEIVE_ID       VARCHAR(36) NOT NULL PRIMARY KEY,
    AM_BIZ_ID           VARCHAR(36) NULL,
    RECEIVE_DEPT        VARCHAR(36) NULL,
    RECEIVE_GROUP       VARCHAR(36) NULL,
    RECEIVE_TIME        VARCHAR(32) NULL,
    IF_RECEIVE          VARCHAR(2) NULL,
    RECEIVE_REPLY       VARCHAR(3072) NULL,
    INVALIDATION_MARK   VARCHAR(2) NULL,
    UPDATE_TIME         VARCHAR(32) NULL,
    RECEIVE_PERSON_LIST text NULL,
    DEALER              VARCHAR(32) NULL
) comment '公告接收表';
CREATE INDEX AM_BIZ_R_IDX_BIZ_ID ON AM_BIZ_RECEIVE (AM_BIZ_ID ASC);

CREATE TABLE AM_BIZ_ANNOUNCEMENT
(
    AM_BIZ_ID          VARCHAR(36) NOT NULL PRIMARY KEY,
    AM_CODE            VARCHAR(72) NULL,
    AM_TYPE            VARCHAR(36) NULL,
    CREATE_TIME        VARCHAR(72) NULL,
    CHECKER_ID         VARCHAR(36) NULL,
    AM_DEAL_TYPE       VARCHAR(2) NULL,
    CONTACT_PHONE      VARCHAR(32) NULL,
    OTHER_PHONE        VARCHAR(32) NULL,
    CREATE_ID          VARCHAR(36) NULL,
    URGENT_LEV         VARCHAR(2) NULL,
    SECRET_LEV         VARCHAR(2) NULL,
    SUMMRY             VARCHAR(512) NULL,
    DESCRIPTION        VARCHAR(2000) NULL,
    RELEASE_DATE       VARCHAR(32) NULL,
    RELEASE_ORG        VARCHAR(36) NULL,
    CHECKER_FLAG       VARCHAR(2) NULL,
    CHECKER_ADVICE     VARCHAR(128) NULL,
    INVALIDATION_MARK  VARCHAR(2) NULL,
    CHECKER_TIME       VARCHAR(32) NULL,
    SMS                VARCHAR(2) NULL,
    SEND_RANGE         VARCHAR(36) NULL,
    RECEIVE_DEPT_ID    text NULL,
    RECEIVE_DEPT_NAME  text NULL,
    RECEIVE_ROLE_ID    text NULL,
    RECEIVE_ROLE_NAME  text NULL,
    RECEIVE_GROUP_ID   text NULL,
    RECEIVE_GROUP_NAME text NULL,
    AM_TITLE           VARCHAR(80) NULL,
    CURRENT_STATUS     VARCHAR(2) NULL,
    END_TIME           VARCHAR(32) NULL,
    DEAL_SCHDULE       VARCHAR(30) NULL
);

CREATE INDEX AM_BIZ_A_IDX_AM_CODE ON AM_BIZ_ANNOUNCEMENT (AM_CODE ASC);
CREATE INDEX AM_BIZ_A_IDX_CREATE_ID ON AM_BIZ_ANNOUNCEMENT (CREATE_ID ASC);
CREATE INDEX AM_BIZ_A_IDX_CREATE_TIME ON AM_BIZ_ANNOUNCEMENT (CREATE_TIME ASC);
CREATE INDEX AM_BIZ_A_IDX_STAUTS ON AM_BIZ_ANNOUNCEMENT (CURRENT_STATUS ASC);

CREATE TABLE EVENT_RUN
(
    EVENT_ID              VARCHAR(128) NOT NULL PRIMARY KEY,
    EVENT_TITLE           VARCHAR(256) COMMENT '事件标题',
    EVENT_NO              VARCHAR(64) COMMENT '单号',
    CREATE_TIME           VARCHAR(64) COMMENT '创建时间',
    CLOSE_TIME            VARCHAR(64) COMMENT '关闭时间',
    EVENT_DESCR           VARCHAR(4000) COMMENT '事件描述',
    APP_SYSTEM_CODE       VARCHAR(128) COMMENT '应用系统编码',
    APP_SYSTEM_NAME       VARCHAR(256) COMMENT '应用系统名称',
    REPORT_SOURCE         VARCHAR(256) COMMENT '告警来源',
    EVENT_SOURCE          VARCHAR(4) COMMENT '事件来源',
    EVENT_LEVEL           VARCHAR(4) COMMENT '事件等级0一般，1急，2紧急（告警级别：1-警告、2-错误、3-紧急）',
    EVENT_TYPE            VARCHAR(4) COMMENT '事件类型（0-应用、1-系统、2-网络）',
    REPORT_TIME           VARCHAR(64) COMMENT '发生时间',
    AFFILIATED_CENTER     VARCHAR(256) COMMENT '所属中心--所属中心（0-合肥中心、1-亦庄中心、2-丰台中心）',
    HANDLE_ROLES          VARCHAR(4) COMMENT '处理角色-0-网络告警派单（合肥）1-网络告警派单（亦庄）2-网络告警派单（丰台）3-系统告警派单（合肥）4-系统告警派单（亦庄）5-系统告警派单（丰台）6-应用告警派单7-告警派单（无所属中心）',
    SYS_ATTENDANT         VARCHAR(64) COMMENT '系统管理员',
    WEB_ATTENDANT         VARCHAR(64) COMMENT '网络管理员',
    APP_ATTENDANT         VARCHAR(64) COMMENT '应用管理员',
    HANDLE_PERSON         VARCHAR(128) COMMENT '处理人',
    HANDLE_TIME           VARCHAR(64) COMMENT '处理时间',
    REPORT_REASON         text COMMENT '报警原因',
    HANDLE_DESCR          text COMMENT '处理描述',
    CREATE_ID             VARCHAR(64) COMMENT '创建人',
    STATUS                VARCHAR(2) COMMENT '状态',
    REQUEST_CLOB          text,
    ANTICIPATE_TIME       VARCHAR(32) COMMENT '预计解决时间',
    CHARGE_PERSON         VARCHAR(64) COMMENT '负责人',
    SUPPLEMENT_EXPLAIN    text COMMENT '补充说明',
    DUTY_ACCOUNT          VARCHAR(32) COMMENT '值班账号',
    REALITY_HANDLE_PERSON VARCHAR(64) NULL,
    PLAN_HANDLE__TIME     VARCHAR(64) NULL,
    CREAT_TYPE            VARCHAR(32) NULL,
    EVENT_CAUSE           VARCHAR(10) NULL,
    CHANGE_FLAG           VARCHAR(100) NULL,
    VERSION_FLAG          VARCHAR(100) NULL,
    BATCH_FLAG            VARCHAR(100) NULL

) COMMENT '运行事件单表';

CREATE TABLE SM_BIZ_TASKINFO
(
    TASK_FORM_ID        VARCHAR(36) NOT NULL,
    TASK_ID             VARCHAR(36) NULL,
    TASK_FROM_NO        VARCHAR(32) NULL,
    TASK_PERFORM_TIME   VARCHAR(32) NULL,
    GENERATE_TIME       VARCHAR(32) NULL,
    TASK_FORM_STATUS    VARCHAR(2) NULL,
    UPDATE_TIME         VARCHAR(32) NULL,
    INVALIDATION_MARK   VARCHAR(2) NULL,
    PERFORM_DATE        VARCHAR(32) NULL,
    EXCUTE_DATE         VARCHAR(32) NULL,
    EXCUTE_TIME         VARCHAR(32) NULL,
    PERFORM_DEPT_ID     VARCHAR(36) NULL,
    PERFORM_USER_ID     VARCHAR(36) NULL,
    PERFORM_DUTY_ID     VARCHAR(36) NULL,
    CHECKER_ID          VARCHAR(36) NULL,
    EXCUTE_DESCRIPTION  text NULL,
    INSPECT_DESCRIPTION text NULL,
    WORK_BEGIN_TIME     VARCHAR(32) NULL,
    WORK_END_TIME       VARCHAR(32) NULL,
    RELATE_CHANGE       VARCHAR(32) NULL,
    PERFORM_GROUP_ID    VARCHAR(36) NULL,
    IS_CHANGE           VARCHAR(2) NULL,
    CHANGES             VARCHAR(36) NULL,
    PUBLISH_TIME        VARCHAR(32) NULL,
    IS_WRITE            VARCHAR(8) NULL
);

CREATE INDEX SM_BIZ_TI_IDX_TASK_FORM_STATUS ON SM_BIZ_TASKINFO (TASK_FORM_STATUS ASC);
CREATE INDEX SM_BIZ_TI_IDX_TASK_FROM_NO ON SM_BIZ_TASKINFO (TASK_FROM_NO ASC);

CREATE TABLE fmbiz_and_issue
(
    ID_            varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
    FM_ID          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    STATE_         varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    ISSUEFX_ID     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    GROUP_ID       varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    OPERATION_ID   varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    OPERATION_TIME varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '' ROW_FORMAT = Compact;

-- 创建知识流程表
create table KNOWLEDGE_OPERATION_HISTORY
(
    ID         VARCHAR(32),
    CONTENT_ID VARCHAR(32) comment '对应知识id',
    OPER_ID    VARCHAR(32) comment '操作人员id',
    OPER_TIME  VARCHAR(32) comment '操作时间',
    OPERATION  VARCHAR(2) comment '操作内容（字典项KNOWLEDGE_OPER_TYPE）',
    REMARK     VARCHAR(255) comment '备注（操作原因）'
);

CREATE TABLE difficult_events
(
    ID_          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    NO_          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单号',
    DEPT_        varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门',
    FM_ID        varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运维事件单ID',
    SYS_ID       varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属系统ID',
    GROUP_ID     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属工作组ID',
    DEAL_ID      varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人ID',
    CREATER_TIME varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
    DEAL_TIME    varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理时间',
    DEAL_DESC    text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '处理描述',
    STATUS_      varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
    SOURCE_      varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源',
    N1           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
    N2           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
    N3           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
    N4           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
    PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- Create table
create table DUTY_VERSION
(
    VERSION_ID   VARCHAR(32) not null primary key,
    VERSION_NO   VARCHAR(32),
    VERSION_NAME VARCHAR(128),
    DUTY_DATE    VARCHAR(4000),
    ADDTIME      VARCHAR(20),
    UPDATE_TIME  VARCHAR(20),
    REMARK       VARCHAR(256)
);

CREATE TABLE cm_biz_single_sj
(
    CHANGE_SINGLE_ID     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变更单ID',
    CHANGE_CODE          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更单号',
    CHANGE_APPLICANT     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
    CREAT_TIME           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更申请人',
    CHANGE_SINGLE_STATUS varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更状态',
    IMPORTANT_LEV        varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更风险等级',
    CHANGE_CATEGORY_ID   varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更分类',
    EXPECT_START_TIME    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预计开始时间',
    EXPECT_END_TIME      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预计结束时间',
    RELATE_RY_ID         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '关联系统',
    CHECK_MANAGER        varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
    APPROVE_MANAGER      varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批人',
    CHANGE_SINGLE_NAME   varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更主题',
    CHANGE_CAUSE_TEXT    text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '变更原因',
    CHANGE_DETAILS       text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '变更内容',
    CHANGE_PROGRAM       text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '变更实施方案',
    IMPLEMENT_SUPVISER   varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实施监督人',
    PRACTICAL_START      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实施开始时间',
    PRACTICAL_END        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实施结束时间',
    IMPLEMENTOR          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实施人',
    CREATE_ORG_ID        varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建机构',
    INVALIDATION_MARK    varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '无效标记',
    SYSID                varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统ID',
    GROUP_ID             varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作组',
    FM_NO                varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件单号',
    FM_TITLE             text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '事件标题',
    CHIEF_MANAGER        varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用处长',
    CHANGE_REASON        varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更原因',
    CHANGE_ORGTYPE       text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '变更省别',
    CHANGE_NUM           varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更条数',
    CHIEFY_MANAGER       varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务处长',
    IF_AQBL              varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否安全堡垒',
    SQSPSM               text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '授权审批说明',
    IS_TOOLS             varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否使用工具',
    TOOLS_NAME           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工具名称',
    CLOSE_TIME           varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关闭时间',
    PROCHECK_MANAGER     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省处理人',
    TECHNOLOGY_AUDIT_ID  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技术审核人',
    IS_CHANGE_OPS        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否转运维',
    CHANGE_SINGLE_TYPE   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '判断是手工创建还是事件单转：0：事件单转   1:手工创建',
    RESULT               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理结果',
    LOG_PERFORM_DESC     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关闭说明',
    CHANGE_COPY          text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备份方案',
    CHANGE_BACK          text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回退方案',
    RELATE_SYSTEM_ID     text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
    PK_CM_BIZ_SINGLE_SJ  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    SJ_CHANGE_PATTERN    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    SJFLAG               varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否新增问题单   0：否  1：是',
    IM_ID                varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据问题单ID',
    WT_DEPT              text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '部门信息',
    WT_LEV               text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '审批级别',
    WT_BUSSINESSID       text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '经办人信息',
    WT_BUSSINESSID_QZ    text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '经办人信息（全值）',
    DEPT_LEAD_ID         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '部门领导ID',
    LINE_LEAD_ID         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '行领导ID',
    IF_AUTO              varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否自动化 1是，0否'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- Create table
create table KNOWLEDGE_VISITS
(
    ID          VARCHAR(32) not null primary key,
    CONTENT_ID  VARCHAR(32),
    CREATE_ID   VARCHAR(32),
    CREATE_TIME VARCHAR(32)
);

create table am_pro_actlog
(
    am_actlog_id  varchar(32) not null,
    am_receive_id varchar(32) null,
    performer_id  varchar(32) null,
    update_time   varchar(32) null,
    description   varchar(2048) null,
    current_state varchar(2) null,
    constraint pk_sys_logininfor primary key (am_actlog_id),
    index(am_receive_id ASC)
);

CREATE TABLE AM_BIZ_PARA
(
    AM_PARA_ID    VARCHAR(36) NOT NULL,
    RECEIVE_RANGE VARCHAR(128) NULL,
    constraint pk_VM_BIZ_INFO primary key (AM_PARA_ID)
);

CREATE TABLE AM_BIZ_PARA_VALUE
(
    AM_PARA_VALUE_ID VARCHAR(36) NOT NULL,
    AM_PARA_ID       VARCHAR(36) NULL,
    RECEIVE_DEPT     text NULL,
    RECEIVE_GROUP    text NULL,
    RECEIVEDEPTID    VARCHAR(36) NULL,
    RECEIVEGROUPID   VARCHAR(36) NULL,
    constraint pk_AM_BIZ_PARA_VALUE primary key (AM_PARA_VALUE_ID)
);

CREATE TABLE im_biz_issuefx
(
    ISSUEFX_ID           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
    ISSUEFX_NO           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    CURRENT_STATE        varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    CREATER_ID           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    CREAT_TIME           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    ISSUE_TYPE           varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    ISSUE_FENLEI         varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    SERIOUS_LEV          varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    ISSUE_ORG            varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    REVIEWER_ID          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    ISSUESOURCE          varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    REPORTNAME           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    REPORTPHONE          text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    REPORTTIME           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    SYSNAME              longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    ISSUEFX_NAME         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    ISSUEFX_TEXT         longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    ISSUEFX_SCHEME       text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    DEAL_STATUS          varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    CREATE_ORG           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    FM_NO                text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    CM_NO                text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    DEAL_ID              varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    BUSINESS_ORG         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    AUDIT_ID             varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    FLAG                 varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    ISSUEFX_IMPACT       text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    DEAL_DESCRIPTION     longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    MULTICOUNT           decimal(65, 30) NULL DEFAULT NULL COMMENT '',
    BUSINESSID           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    EXPECT_TIME          text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    REALITY_TIME         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    BU_DEAL_DESC         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    SOLV_STATUS          varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    BUSS_ID              varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    PAP_DESC             text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    PLAN_DESC            text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    ISSUE_YYFENLEI       varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    MULTIUSERS           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    DISTRIBUTE_TIME      text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    ACCEPT_TIME          text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    PRE_SOLUTION_TIME    text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    SOLUTION_TIME        text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    CSNO                 varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    HANGUPTASK           varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    BU_FUHE_DESC         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    SYSID                longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    FMJK_NO              text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    FU_NO                text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    DEAL_DETAIL          longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    IMPORT_LEVEL         varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    LAST_TIME            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    PUT_UNIT             varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    UNIT_SCHEDULE        longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    INVALIDATION_MARK    varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    I_STATUS             varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    JJ_TIME              varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    IS_INSIDE            varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    SUB_DEPT             text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '',
    ONE_TYPE             varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    TWO_TYPE             varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    THREE_TYPE           varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    TARDING_NAME         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    RELATION_FMBIZ_COUNT varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '',
    INDEX                ISSUEFX_NO(ISSUEFX_NO) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

CREATE TABLE cm_biz_qingqiu
(
    CHANGE_ID          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变更单ID',
    CHANGE_CODE        varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更单号',
    CREATETIME         varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
    CREATER_ORG_ID     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建机构',
    SUBMIT_TIME        varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交时间',
    APPLICANT_ID       varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人',
    CHANGE_CATEGORY_ID varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更分类',
    SYSID              text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '所涉应用系统ID',
    SYSNAME            text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '涉及系统名称',
    IMPORTANT_LEV      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '等级',
    IS_NOTICE          varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否通知',
    IS_STOP            varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否停止',
    CHANGE_RESOURCE    varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更来源',
    CHANGE_SINGLE_NAME text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '标题',
    CHANGE_DETAILS     longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '变更内容',
    CHECK_ORG          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核机构',
    CHECKER_ID         varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
    PRACTICLE_TIME     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '受理时间',
    IMPLEMENTOR_ID     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实施人',
    IMPLEMENTOR_ORGID  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实施机构',
    STAUTS             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
    CHANGE_STAGE       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更阶段',
    INVALIDATION_MARK  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '无效标记',
    FUCHECKER_ID       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分管领导审核人',
    INSTANCEID         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程id',
    CHANGE_CAUSE_TEXT  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '变更原因',
    DEAL_ID_LIST       longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '处理人ID集合',
    FUCHECKER_FLAG     varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分管领导审阅1待审2已审',
    PRIMARY KEY (CHANGE_ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

CREATE TABLE AM_BIZ_TASKLOG
(
    AM_TASK_ID   VARCHAR(36) NOT NULL primary key,
    AM_BIZ_ID    VARCHAR(36) NULL,
    UPDATE_TIME  VARCHAR(32) NULL,
    PERSON_ID    VARCHAR(36) NULL,
    AM_STATE     VARCHAR(36) NULL,
    PROCESS_NAME VARCHAR(256) NULL,
    DESCRIPTION  VARCHAR(2048) NULL,
    NUM          VARCHAR(5) NULL,
    index(AM_BIZ_ID ASC)
);

CREATE TABLE FM_SW
(
    FM_SW_ID          VARCHAR(36)            NOT NULL,
    CHECKER_ID        VARCHAR(36) NULL,
    AUTH_ID           VARCHAR(36) NULL,
    CREATE_ORG_ID     VARCHAR(36) NULL,
    DEAL_ORG_ID       VARCHAR(36) NULL,
    CREATE_ID         VARCHAR(36) NULL,
    DEALER_ID         VARCHAR(36) NULL,
    AUDITOR_ID        VARCHAR(36) NULL,
    FAULT_NO          VARCHAR(32) NULL,
    FAULT_TITLE       VARCHAR(1500) NULL,
    CREATE_TIME       VARCHAR(20) NULL,
    DEAL_RESULT       VARCHAR(2) NULL,
    DEAL_CONTENT      VARCHAR(2000) NULL,
    AUDIT_RESULT      VARCHAR(2) NULL,
    AUDIT_TIME        VARCHAR(20) NULL,
    CURRENT_STATE     VARCHAR(2) NULL,
    FAULT_KIND        VARCHAR(512) NULL,
    AUTH_NAME         VARCHAR(1000) NULL,
    PARTICIPATOR_IDS  VARCHAR(2000) NULL,
    INVALIDATION_MARK VARCHAR(2) DEFAULT NULL NULL,
    FAULT_DESCRIPTION TEXT NULL,
    PROCESS_STATUS    CHAR(1)    DEFAULT '1' NOT NULL,
    CHECKER_TWO_ID    VARCHAR(36) NULL,
    CHECKER_THREE_ID  VARCHAR(36) NULL,
    CHECKER_FOUR_ID   VARCHAR(36) NULL,
    DEALER_FIVE_ID    VARCHAR(36) NULL,
    constraint pk_FM_SW primary key (FM_SW_ID)
)
;

CREATE INDEX FM_SW_IDX_CREATE_ID
    ON FM_SW (CREATE_ID ASC);

CREATE INDEX FM_SW_IDX_CREATE_TIME
    ON FM_SW (CREATE_TIME ASC);

CREATE INDEX FM_SW_IDX_FAULT_NO
    ON FM_SW (FAULT_NO ASC);

CREATE INDEX FM_SW_IDX_STAUTS
    ON FM_SW (CURRENT_STATE ASC);


ALTER TABLE FM_SW
    ADD CHECK (FM_SW_ID IS NOT NULL);
ALTER TABLE FM_SW
    ADD CHECK (PROCESS_STATUS IS NOT NULL);
ALTER TABLE FM_SW
    ADD CHECK (FM_SW_ID IS NOT NULL);
ALTER TABLE FM_SW
    ADD CHECK (FM_SW_ID IS NOT NULL);
ALTER TABLE FM_SW
    ADD CHECK (FM_SW_ID IS NOT NULL);
ALTER TABLE FM_SW
    ADD CHECK (PROCESS_STATUS IS NOT NULL);


alter table pub_flow_log
    add column DUTY_ACCOUNT VARCHAR(36);

alter table pub_flow_log
    add column DUTY_ID VARCHAR(64);



CREATE TABLE FM_SWMB
(
    SWMB_ID              VARCHAR(36)         NOT NULL PRIMARY KEY,
    CREATE_ID            VARCHAR(36) NULL,
    CREATE_TIME          VARCHAR(20) NULL,
    FILE_STORE_ID        VARCHAR(4000) NULL,
    SWMB_TITLE           VARCHAR(512) NULL,
    MEMO                 VARCHAR(512) NULL,
    CREATE_ORG_ID        VARCHAR(36) NULL,
    FAULT_KIND           VARCHAR(512) NULL,
    DEAL_ORG_ID          VARCHAR(36) NULL,
    CONTENT              TEXT NULL,
    DEAL_P_ID            VARCHAR(36) NULL,
    PROCESS_STATUS       CHAR(1) DEFAULT '1' NOT NULL,
    CHECKER_TWO_STATUS   CHAR(1) NULL,
    CHECKER_THREE_STATUS CHAR(1) NULL,
    CHECKER_FOUR_STATUS  CHAR(1) NULL,
    DEALER_FIVE_STATUS   CHAR(1) NULL
);

ALTER TABLE FM_SWMB
    ADD CHECK (SWMB_ID IS NOT NULL);
ALTER TABLE FM_SWMB
    ADD CHECK (PROCESS_STATUS IS NOT NULL);
ALTER TABLE FM_SWMB
    ADD CHECK (SWMB_ID IS NOT NULL);
ALTER TABLE FM_SWMB
    ADD CHECK (SWMB_ID IS NOT NULL);
ALTER TABLE FM_SWMB
    ADD CHECK (SWMB_ID IS NOT NULL);
ALTER TABLE FM_SWMB
    ADD CHECK (PROCESS_STATUS IS NOT NULL);


alter table OG_PERSON
    add column IDCARD VARCHAR(18);


CREATE TABLE SELF_SERVICE_QUERY
(
    ID_                   VARCHAR(36) NOT NULL PRIMARY KEY,
    NO_                   VARCHAR(36) NULL,
    CREATE_ORG_ID         VARCHAR(36) NULL,
    CREATER_TIME          VARCHAR(20) NULL,
    CREATER_ID            VARCHAR(36) NULL,
    CREATE_PHONE          VARCHAR(16) NULL,
    OCCURRENCE_TIME       VARCHAR(20) NULL,
    OCCURRENCE_ADDRESS    VARCHAR(128) NULL,
    CUSTOMER_IDCARD       VARCHAR(20) NULL,
    SYSID                 VARCHAR(36) NULL,
    CUSTOMER_NAME         VARCHAR(48) NULL,
    TRANSACTION_ACCOUNT   VARCHAR(256) NULL,
    FAULT_REPORT_NAME     VARCHAR(54) NULL,
    REPORT_PHONE          VARCHAR(36) NULL,
    FAULT_DECRIPT_SUMMARY VARCHAR(128) NULL,
    FAULT_DECRIPT_DETAIL  VARCHAR(4000) NULL,
    PLAN_QUERY_CONTENT    VARCHAR(2600) NULL,
    DEADLINE              VARCHAR(20) NULL,
    STATE                 VARCHAR(2) NULL,
    N4                    VARCHAR(512) NULL,
    N1                    VARCHAR(512) NULL,
    N5                    VARCHAR(512) NULL
);

ALTER TABLE SELF_SERVICE_QUERY
    ADD CHECK (ID_ IS NOT NULL);



CREATE TABLE SJ_FETCH_DEMAND
(
    FETCH_ID              VARCHAR(36) NOT NULL,
    FETCH_NO              VARCHAR(36) NOT NULL,
    CREAT_TIME            VARCHAR(64) NULL,
    SOURCE_TYPE           VARCHAR(2) NULL,
    TITLE_NAME            VARCHAR(256) NULL,
    PROCESSID             VARCHAR(32) NULL,
    ORG_NAME              VARCHAR(256) NULL,
    PERSON_NAME           VARCHAR(36) NULL,
    BUSINESS_NUMBER       VARCHAR(36) NULL,
    BUSINESS_TYPE         VARCHAR(2) NULL,
    BUSINESS_ORG_NAME     VARCHAR(36) NULL,
    BBUSINESS_PERSON_NAME VARCHAR(36) NULL,
    SYS_NAME              VARCHAR(256) NULL,
    INTERFACE_NAME        VARCHAR(36) NULL,
    INTERFACE_PHONE       VARCHAR(36) NULL,
    FILE_SIZE             int NULL,
    FETCH_STATE           VARCHAR(2) NULL,
    GROUP_ID              VARCHAR(36) NULL,
    ASSING_ID             VARCHAR(36) NULL,
    SYS_MANAGER_ID        VARCHAR(512) NULL,
    DEAL_ID               VARCHAR(36) NULL,
    AUDIT_BACK_ID         VARCHAR(36) NULL,
    COLLECT_BACK_ID       VARCHAR(36) NULL,
    ORGID                 VARCHAR(32) NULL,
    CREATE_ID             VARCHAR(36) NULL,
    SYS_MANAGER_NAME      VARCHAR(36) NULL,
    BUSINESS_ORG_ID       VARCHAR(255) NULL,
    SOURCE_TYPE_TEXT      VARCHAR(10) NULL,
    BUSINESS_TYPE_TEXT    VARCHAR(36) NULL,
    SYS_NAME_TEXT         VARCHAR(255) NULL,
    FETCH_STATE_TEXT      VARCHAR(36) NULL,
    SYS_ORG_ID            VARCHAR(36) NULL,
    SYS_ORG_NAME          VARCHAR(36) NULL,
    SYS_ID                VARCHAR(256) NULL,
    DEAL_SCHDULE          VARCHAR(255) NULL
);

ALTER TABLE SJ_FETCH_DEMAND
    ADD CHECK (FETCH_ID IS NOT NULL);
ALTER TABLE SJ_FETCH_DEMAND
    ADD CHECK (FETCH_NO IS NOT NULL);



CREATE TABLE SJ_FETCH_SINGLE
(
    FETCH_ID              VARCHAR(36) NOT NULL PRIMARY KEY,
    FETCH_NO              VARCHAR(36) NOT NULL,
    CREAT_TIME            VARCHAR(64) NULL,
    SOURCE_TYPE           VARCHAR(2) DEFAULT 01 NULL,
    TITLE_NAME            VARCHAR(256) NULL,
    PROCESSID             VARCHAR(32) NULL,
    ORG_NAME              VARCHAR(256) NULL,
    PERSON_NAME           VARCHAR(36) NULL,
    BUSINESS_NUMBER       VARCHAR(36) NULL,
    BUSINESS_TYPE         VARCHAR(2) NULL,
    BUSINESS_ORG_NAME     VARCHAR(36) NULL,
    BBUSINESS_PERSON_NAME VARCHAR(36) NULL,
    SYS_NAME              VARCHAR(256) NULL,
    INTERFACE_NAME        VARCHAR(36) NULL,
    INTERFACE_PHONE       VARCHAR(36) NULL,
    FILE_SIZE             INT NULL,
    FETCH_STATE           VARCHAR(2) NULL,
    GROUP_ID              VARCHAR(36) NULL,
    ASSING_ID             VARCHAR(36) NULL,
    SYS_MANAGER_ID        VARCHAR(512) NULL,
    DEAL_ID               VARCHAR(36) NULL,
    AUDIT_BACK_ID         VARCHAR(36) NULL,
    COLLECT_BACK_ID       VARCHAR(36) NULL,
    ORGID                 VARCHAR(32) NULL,
    CREATE_ID             VARCHAR(36) NULL,
    SYS_MANAGER_NAME      VARCHAR(36) NULL,
    BUSINESS_ORG_ID       VARCHAR(255) NULL,
    SOURCE_TYPE_TEXT      VARCHAR(10) NULL,
    BUSINESS_TYPE_TEXT    VARCHAR(36) NULL,
    SYS_NAME_TEXT         VARCHAR(255) NULL,
    FETCH_STATE_TEXT      VARCHAR(36) NULL,
    SYS_ORG_ID            VARCHAR(36) NULL,
    SYS_ORG_NAME          VARCHAR(36) NULL,
    SYS_ID                VARCHAR(256) NULL,
    FETCH_DEMAND_ID       VARCHAR(36) NULL,
    DEAL_FLAG             VARCHAR(2)
);


ALTER TABLE SJ_FETCH_SINGLE
    ADD CHECK (FETCH_ID IS NOT NULL);
ALTER TABLE SJ_FETCH_SINGLE
    ADD CHECK (FETCH_NO IS NOT NULL);


CREATE TABLE OG_LINE
(
    LINE_ID             VARCHAR(33) NOT NULL PRIMARY KEY,
    LINE_CORE           VARCHAR(255) NULL,
    LINE_AMBIENT        VARCHAR(255) NULL,
    LINE_OFFICE         VARCHAR(255) NULL,
    SYS_ID              VARCHAR(33) NULL,
    PURPOSE             VARCHAR(255) NULL,
    EQUIPMENT           VARCHAR(255) NULL,
    START_TIME          VARCHAR(255) NULL,
    PRESENT_TIME        VARCHAR(255) NULL,
    END_TIME            VARCHAR(255) NULL,
    ELAPSED_TIME        VARCHAR(255) NULL,
    CLASS_IFICATION     VARCHAR(255) NULL,
    CLASS_DOT           VARCHAR(255) NULL,
    TREATMENT           VARCHAR(255) NULL,
    BRIEFING            VARCHAR(255) NULL,
    CONTINUITY          VARCHAR(255) NULL,
    P_ID                VARCHAR(33) NULL,
    UNIT_TYPE           VARCHAR(255) NULL,
    SERIES_NUMBER       VARCHAR(255) NULL,
    COMPANIES           VARCHAR(255) NULL,
    MONITORING          VARCHAR(255) NULL,
    CASE_ID             VARCHAR(255) NULL,
    HANDLER             VARCHAR(255) NULL,
    HANDLER_STRUCTURE   VARCHAR(255) NULL,
    THREE_PARTIES       VARCHAR(255) NULL,
    MANUFACTURER        VARCHAR(255) NULL,
    BG_ID               VARCHAR(255) NULL,
    DEGAUSSING          VARCHAR(255) NULL,
    MAINTENANCE         VARCHAR(255) NULL,
    REMARK              VARCHAR(255) NULL,
    SPARE_TIME          VARCHAR(255) NULL,
    ORG_NAME_TWO        VARCHAR(255) NULL,
    ORG_NAME_ONE        VARCHAR(255) NULL,
    LINE_START          VARCHAR(33) NULL,
    LINE_TYPE_ONE       VARCHAR(255) NULL,
    LINE_ISNO_ONE       VARCHAR(255) NULL,
    REPAIRS_TIME        VARCHAR(255) NULL,
    AFFIRM_REPAIRS_TIME VARCHAR(255) NULL,
    PERSON_PRESENT_TIME VARCHAR(255) NULL,
    PERSON_ANSWER_TIME  VARCHAR(255) NULL,
    PIDTWO              VARCHAR(255) NULL,
    ORGID               VARCHAR(255) NULL,
    ORGIDNAMETHREE      VARCHAR(255) NULL,
    PIDNAMEONE          VARCHAR(255) NULL
);

ALTER TABLE OG_LINE
    ADD CHECK (LINE_ID IS NOT NULL);
ALTER TABLE OG_LINE
    ADD CHECK (LINE_ID IS NOT NULL);



CREATE TABLE SYS_LINE
(
    HARDWARE_ID                   VARCHAR(36) NOT NULL PRIMARY KEY,
    HARDWARE_CENTRE               VARCHAR(255) NULL,
    HARDWARE_ENVIRONMENT          VARCHAR(255) NULL,
    HARDWARE_MISTRESS             VARCHAR(255) NULL,
    HARDWARE_DEVICENAME           VARCHAR(255) NULL,
    HARDWARE_BRAND                VARCHAR(255) NULL,
    HARDWARE_TYPE_ONE             VARCHAR(255) NULL,
    HARDWARE_SERIAL_NUMBER        VARCHAR(255) NULL,
    HARDWARE_SERIAL_NUMBER_CHANGE VARCHAR(255) NULL,
    HARDWARE_ISNO_POLICE          VARCHAR(255) NULL,
    HARDWARE_TYPE_TWO             VARCHAR(255) NULL,
    HARDWARE_DOT                  VARCHAR(255) NULL,
    REGISTER_SYSTEM               VARCHAR(255) NULL,
    CREATE_TIME_TWO               VARCHAR(255) NULL,
    HARDWARE_TIME                 VARCHAR(255) NULL,
    PART_PRESENT_TIME             VARCHAR(255) NULL,
    PERSON_PRESENT_TIME           VARCHAR(255) NULL,
    DISPOSE_FINISH_TIME           VARCHAR(255) NULL,
    PART_ELAPSED_TIME             VARCHAR(255) NULL,
    HARDWARE_DISPOSE_TIME         VARCHAR(255) NULL,
    PERSON_ANSWER_TIME            VARCHAR(255) NULL,
    INFORMATION_CASE              VARCHAR(255) NULL,
    CONTINUITY_ISNO               VARCHAR(255) NULL,
    WATCHKEEPER_PERSON            VARCHAR(255) NULL,
    DEAL_WITH                     VARCHAR(255) NULL,
    ALTERATION_NUMBERS            VARCHAR(255) NULL,
    OVERTIME_CAUSE                VARCHAR(255) NULL,
    HARDWARE_REMARK               VARCHAR(500) NULL,
    LINE_START                    VARCHAR(255) NULL,
    HARDWARE_PERSON               VARCHAR(255) NULL,
    HARDWARE_TYPE_THERE           VARCHAR(255) NULL,
    IS_NO                         VARCHAR(4) NULL,
    NO_IS                         VARCHAR(4) NULL
);

ALTER TABLE SYS_LINE
    ADD CHECK (HARDWARE_ID IS NOT NULL);
ALTER TABLE SYS_LINE
    ADD CHECK (HARDWARE_ID IS NOT NULL);


CREATE TABLE CM_OS_RECOVERY_REGISTER
(
    RECOVERY_REGISTER_ID VARCHAR(36) NOT NULL PRIMARY KEY,
    CENTRE_NAME          VARCHAR(50) NULL,
    SYS_ID               VARCHAR(36) NULL,
    SYS_NAME             VARCHAR(100) NULL,
    HOST_NAME            VARCHAR(100) NULL,
    BACKUP_DATE          VARCHAR(50) NULL,
    RECOVERY_START_TIME  VARCHAR(50) NULL,
    RECOVERY_END_TIME    VARCHAR(50) NULL,
    RECOVERY_TIME        VARCHAR(50) NULL,
    VERIFICATION_RESULT  VARCHAR(255) NULL,
    REMARK               VARCHAR(500) NULL,
    DATA_SIZE            VARCHAR(50) NULL,
    TYPE                 VARCHAR(2) NULL,
    LINE_START           VARCHAR(2) NULL,
    PID                  VARCHAR(36) NULL,
    ORGID                VARCHAR(36) NULL,
    ORGNAME              VARCHAR(50) NULL,
    SOMMENT              VARCHAR(500) NULL
);

ALTER TABLE CM_OS_RECOVERY_REGISTER
    ADD CHECK (RECOVERY_REGISTER_ID IS NOT NULL);


CREATE TABLE CM_DB_RECOVERY_REGISTER
(
    RECOVERY_REGISTER_ID VARCHAR(36) NOT NULL PRIMARY KEY,
    CENTRE_NAME          VARCHAR(50) NULL,
    SYS_ID               VARCHAR(36) NULL,
    SYS_NAME             VARCHAR(100) NULL,
    HOST_NAME            VARCHAR(100) NULL,
    BACKUP_DATE          VARCHAR(50) NULL,
    RECOVERY_START_TIME  VARCHAR(50) NULL,
    RECOVERY_END_TIME    VARCHAR(50) NULL,
    RECOVERY_TIME        VARCHAR(50) NULL,
    VERIFICATION_RESULT  VARCHAR(255) NULL,
    REMARK               VARCHAR(500) NULL,
    DATA_SIZE            VARCHAR(50) NULL,
    TYPE                 VARCHAR(2) NULL,
    LINE_START           VARCHAR(2) NULL,
    PID                  VARCHAR(36) NULL,
    ORGID                VARCHAR(36) NULL,
    ORGNAME              VARCHAR(50) NULL,
    SOMMENT              VARCHAR(500) NULL
);

ALTER TABLE CM_DB_RECOVERY_REGISTER
    ADD CHECK (RECOVERY_REGISTER_ID IS NOT NULL);

CREATE TABLE FM_DD
(
    FM_DD_ID           VARCHAR(36) NOT NULL,
    CREATE_ORG_ID      VARCHAR(36) NULL,
    CREATE_ID          VARCHAR(36) NULL,
    CHECKER_ID         VARCHAR(36) NULL,
    FAULT_NO           VARCHAR(32) NULL,
    FAULT_TITLE        VARCHAR(128) NULL,
    FAULT_TYPE         VARCHAR(2) NULL,
    CREATE_TIME        VARCHAR(20) NULL,
    TEL_PHONE          VARCHAR(36) NULL,
    OTHER_CONTACT      VARCHAR(32) NULL,
    URGENT_LEV         VARCHAR(2) NULL,
    TYPE               VARCHAR(2) NULL,
    CURRENT_STATE      VARCHAR(2) NULL,
    FAULT_CONTENT      TEXT NULL,
    PLAN_TIME          VARCHAR(20) NULL,
    FAULT_CONTACT_NAME VARCHAR(36) NULL,
    PARTICIPATOR_IDS   VARCHAR(2000) NULL,
    INVALIDATION_MARK  VARCHAR(2) NULL,
    XT_IDS             VARCHAR(2048) NULL
);
CREATE INDEX FM_DD_IDX_CREATE_ID
    ON FM_DD (CREATE_ID ASC);
CREATE INDEX FM_DD_IDX_CREATE_TIME
    ON FM_DD (CREATE_TIME ASC);
CREATE INDEX FM_DD_IDX_FAULT_NO
    ON FM_DD (FAULT_NO ASC);
CREATE INDEX FM_DD_IDX_STAUTS
    ON FM_DD (CURRENT_STATE ASC);

ALTER TABLE FM_DD
    ADD CHECK (FM_DD_ID IS NOT NULL);
ALTER TABLE FM_DD
    ADD CHECK (FM_DD_ID IS NOT NULL);
ALTER TABLE FM_DD
    ADD CHECK (FM_DD_ID IS NOT NULL);
ALTER TABLE FM_DD
    ADD CHECK (FM_DD_ID IS NOT NULL);

ALTER TABLE FM_DD
    ADD PRIMARY KEY (FM_DD_ID);


alter table account_req
    add column NOTE VARCHAR(100);
alter table account_req
    add column UPDATE_BY VARCHAR(64);
alter table account_req
    add column UPDATE_TIME DATETIME;

CREATE TABLE fm_biz_control
(
    CONTROL_ID   varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
    FM_BIZ_ID    varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件单id',
    SYS_ID       varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属应用系统',
    GROUP_ID     varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属工作组',
    UNUSUAL_TYPE varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常分类:长时间未领取/长时间未处理/不及时',
    CONTROL_TIME varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计时间',
    PRIMARY KEY (CONTROL_ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务事件单监控表' ROW_FORMAT = Compact;

CREATE TABLE fm_biz_life
(
    FM_LIFE_ID          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
    SYS_ID              varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属系统',
    DEPT_LEV            varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位级别 1公司 2系统',
    SUR_COUNT           varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '剩余数量',
    EIGHT_NDEAL_COUNT   varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '8小时未处理数量',
    EIGHT_DEAL_COUNT    varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '8小时处理数量',
    EIGHT_DEAL_LIFE     varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '8小时处理效率',
    SIXTEEN_NDEAL_COUNT varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '16小时未处理数量',
    SIXTEEN_DEAL_COUNT  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '16小时处理数量',
    SIXTEEN_DEAL_LIFE   varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '16小时处理效率',
    TTFOUR_NDEAL_COUNT  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '24小时未处理数量',
    TTFOUR_DEAL_COUNT   varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '24小时处理数量',
    TTFOUR_DEAL_LIFE    varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '24小时处理效率',
    STATISTICAL_TIME    varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计时间',
    ORG_ID              varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属公司',
    FLAG                varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计标识 1日常效率 2报表',
    INVALIDATION_MARK   varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '历史数据标志 1 当前数据 0 历史数据'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '事件单效率表' ROW_FORMAT = Compact;

create table DUTY_TYPEINFO
(
    TYPEINFO_ID       VARCHAR(36) not null,
    TYPE_NAME         VARCHAR(256),
    MOBILE_PHONE      VARCHAR(32),
    PARENT_ID         VARCHAR(36),
    UPDATE_TIME       VARCHAR(32),
    SERIAL            int,
    LEADER            VARCHAR(2),
    TYPE_NO           VARCHAR(32),
    TYPE_DESCRIPTION  VARCHAR(1024),
    PID               VARCHAR(32),
    DUTY_NUMBER       VARCHAR(2),
    INVALIDATION_MARK VARCHAR(2),
    TYPE_ROWS         VARCHAR(2),
    TYPE_COLUMNS      VARCHAR(2)
);

create table DUTY_VERSION_TYPEINFO
(
    VERSION_TYPEINFO_ID VARCHAR(36) not null,
    TYPE_NAME           VARCHAR(256),
    MOBILE_PHONE        VARCHAR(32),
    PARENT_ID           VARCHAR(36),
    UPDATE_TIME         VARCHAR(32),
    INVALIDATION_MARK   VARCHAR(2),
    SERIAL              int,
    LEADER              VARCHAR(2),
    TYPE_NO             VARCHAR(32),
    TYPE_DESCRIPTION    VARCHAR(1024),
    PID                 VARCHAR(32),
    VERSION_ID          VARCHAR(32),
    TYPEINFO_ID         VARCHAR(32),
    DUTY_NUMBER         VARCHAR(2),
    TYPE_ROWS           VARCHAR(2),
    TYPE_COLUMNS        VARCHAR(2)
);

CREATE TABLE tel_biz
(
    TELID             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话单id',
    TELNO             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话单号',
    ISTRANSFER        varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否为转接(1是0否)',
    CONTACT_ORG       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来电人所属机构',
    SYS_ID            varchar(56) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题所属系统',
    TEL_TYPE          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题类型',
    CONTENT           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '来电内容',
    CREATE_ID         varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
    CREATE_TIME       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入时间',
    CREATE_ORG        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建机构',
    CONTACT_NAME      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来电人',
    CONTACT_PHONE     varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来电人电话',
    INVALIDATION_MARK varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '有效标志',
    FAULT_NO          varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务事件单号（转业务事件单的才有）',
    STATE             varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
    CLOSE_TIME        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关闭时间',
    EVENT_TYPE        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件类型',
    IS_SOLVE          varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否解决',
    GROUP_NO          varchar(156) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组号',
    OVER_FLOW_GROUP   varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '溢出组',
    OVER_FLOW_TIME    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '溢出时间',
    CLOSE_OPTION      text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '关闭意见',
    START_TIME        varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电话事件单' ROW_FORMAT = Compact;

create table TEL_SUPPORT_PEOPLE_EXTENSION
(
    id            VARCHAR(36)  not null PRIMARY KEY,
    orgid         VARCHAR(36)  not null,
    name          VARCHAR(36)  not null,
    extension     VARCHAR(36)  not null,
    sysname       VARCHAR(255) not null,
    create_person VARCHAR(50),
    create_time   VARCHAR(50),
    update_person VARCHAR(50),
    update_time   VARCHAR(50),
    sysid         VARCHAR(255)
) COMMENT '支持人员对应分机号查询';

create table OG_PERSON_DUTY
(
    duty_id                 VARCHAR(32) not null primary key COMMENT '值班表id',
    pid                     VARCHAR(32) COMMENT '人员id',
    user_id                 VARCHAR(32) COMMENT '用户id',
    group_id                VARCHAR(32) COMMENT '工作组表id',
    monitor_type_account_id VARCHAR(32) COMMENT '人员-监控类型关联表id',
    duty_type               VARCHAR(2) COMMENT '值班组类型',
    duty_name               VARCHAR(256) COMMENT '值班组名称',
    operate_time            date COMMENT '添加值班组时间',
    tel                     VARCHAR(50) COMMENT '联系方式'
) COMMENT '监控值班表';

CREATE TABLE DUTY_ACCOUNT
(
    ID          VARCHAR(32) NOT NULL primary key,
    PID         VARCHAR(32) NOT NULL,
    ACCOUNT_PID VARCHAR(32) NOT NULL
);

CREATE TABLE DUTY_SUBSTITUTE
(
    SUBSTITUTE_ID VARCHAR(32) NULL,
    PID           VARCHAR(1024) NULL,
    TID           VARCHAR(32) NULL,
    DUTY_DATE     VARCHAR(32) NULL,
    REMARK        VARCHAR(512) NULL,
    STATUS        VARCHAR(2) NULL,
    TYPE_NO       VARCHAR(32) NULL,
    SCHEDULING_ID VARCHAR(32) NULL
);

CREATE TABLE DUTY_SUBSTITUTE_REMARK
(
    SUBSTITUTE_REMARK_ID VARCHAR(32) NOT NULL primary key,
    PID                  VARCHAR(1024) NULL,
    TID                  VARCHAR(32) NULL,
    DUTY_DATE            VARCHAR(32) NULL,
    REMARK               VARCHAR(512) NULL,
    TYPE_NO              VARCHAR(32) NULL
);



CREATE TABLE ADDRESS_LIST
(
    ADDRESS_LIST_ID   VARCHAR(32) NOT NULL,
    ADDRESS_LIST_TYPE VARCHAR(2),
    CREATE_PID        VARCHAR(32),
    ORGID             VARCHAR(32),
    CREATE_TIME       VARCHAR(20),
    UPDATE_TIME       VARCHAR(20),
    UPDATE_USER_ID    VARCHAR(32),
    PHONE             VARCHAR(32),
    EMAIL             VARCHAR(256),
    INVALIDATIONMARK  VARCHAR(2),
    PNAME             VARCHAR(80),
    SERVER_NAME       VARCHAR(100),
    TEL               VARCHAR(200),
    SERVER_ORDER      VARCHAR(60),
    ISCANREAD         VARCHAR(2),
    CREATE_ORGID      VARCHAR(32),
    DUTY              VARCHAR(80),
    RESPONSIBILITY    VARCHAR(1000),
    MEMO              VARCHAR(1000)
);



CREATE TABLE REG_FOLDER_INDEX
(
    ID_          VARCHAR(32) NOT NULL,
    NAME_        VARCHAR(256),
    PATH_        VARCHAR(1024),
    PARENT_      VARCHAR(32),
    DESCRIPTION_ VARCHAR(2056),
    CREATE_USER_ VARCHAR(64),
    CREATE_TIME_ VARCHAR(20),
    MODIFY_USER_ VARCHAR(64),
    MODIFY_TIME_ VARCHAR(20),
    UPDATE_TIME_ VARCHAR(20),
    LEAF_        VARCHAR(20)
);



CREATE TABLE REGIME_INFO
(
    REGIME_INFO_ID         VARCHAR(32) NOT NULL,
    REGIME_TITLE           VARCHAR(150),
    REGIME_DIGEST          VARCHAR(2000),
    COMMIT_ID              VARCHAR(32),
    COMMIT_TIME            VARCHAR(30),
    ORGID                  VARCHAR(32),
    REGIME_INFO_TYPE       VARCHAR(2),
    STEP_UP_OPERATION_ID   VARCHAR(32),
    STEP_UP_OPERATION_TIME VARCHAR(30),
    PRINT_TIME             VARCHAR(30),
    COMMIT_DEPT            VARCHAR(45),
    NUM                    VARCHAR(150),
    MARK                   VARCHAR(4000),
    TYPE_ONE               VARCHAR(2),
    TYPE_TWO               VARCHAR(2),
    SYTEM_FOUR             VARCHAR(2),
    CLASSIFY               VARCHAR(2),
    DELETE_REASON          VARCHAR(2),
    KEY_WORD               VARCHAR(150),
    IS_DELETE              VARCHAR(2),
    CURRENT_STATE          VARCHAR(4),
    CHECKER                VARCHAR(36),
    FOLDER_                VARCHAR(36),
    DELETE_TIME            VARCHAR(30)
);



CREATE TABLE COMPUTER_MODULE
(
    ID          VARCHAR(32) NOT NULL,
    NAME        VARCHAR(64),
    CREATE_TIME VARCHAR(20),
    CREATE_ID   VARCHAR(32),
    PARENT_ID   VARCHAR(32),
    STATUS      VARCHAR(2)
);



CREATE TABLE COMPUTER_ROOM_APPLY
(
    ID                           VARCHAR(32) NOT NULL,
    APPLY_USER_ID                VARCHAR(32),
    APPLY_ORG_ID                 VARCHAR(32),
    COMPUTER_ROOM_MODULE         VARCHAR(1024),
    ACCOMPANY_USER_ID            VARCHAR(32),
    PREDICT_IN_TIME              VARCHAR(32),
    PREDICT_OUT_TIME             VARCHAR(32),
    WORK_CONTENT_IDS             VARCHAR(512),
    WORK_DESCRIPTION             VARCHAR(512),
    APPLY_STATE                  VARCHAR(2),
    REGISTER_TIME                VARCHAR(32),
    REGISTER_ACCOMPANY_USER_NAME VARCHAR(32),
    CREATE_TIMES                 VARCHAR(32),
    UPDATE_TIMES                 VARCHAR(32),
    AUDITOR_ID                   VARCHAR(32)  DEFAULT NULL,
    REASON                       VARCHAR(255) DEFAULT NULL,
    WORK_CONTENT                 VARCHAR(255),
    IS_TEMPORARY                 VARCHAR(2),
    INTO_NAME                    VARCHAR(255),
    INTO_IDNUMBER                VARCHAR(255),
    INTO_PHONE                   VARCHAR(255),
    INTO_UNIT                    VARCHAR(255),
    IS_BELONGINGS                VARCHAR(2),
    BELONGINGS                   VARCHAR(255),
    IS_URGENCY                   VARCHAR(2),
    REMARK                       VARCHAR(1024),
    COMPUTERCENTER               VARCHAR(32),
    ACCOMPANYUSERONE             VARCHAR(255),
    REALITYINTO_TIME             VARCHAR(32),
    REALITYOUTTO_TIME            VARCHAR(32),
    REALITYINTOPERSONNEL         VARCHAR(255),
    REALITY_BELONGINGS           VARCHAR(512),
    REALITY_MODULE               VARCHAR(255),
    ACCOMPANYUSERIDTWO           VARCHAR(32),
    BELONGINGSNUM                VARCHAR(32),
    INOUTTYPE                    VARCHAR(64),
    INSTALLSITE                  VARCHAR(32),
    COMPUTER_ROOM_MODULE_NAME    VARCHAR(255),
    REALITY_BELONGINGS_NUM       VARCHAR(32),
    COMPUTER_APPLY_NO            VARCHAR(32),
    REGISTER_NAME                VARCHAR(32)
);

create table TEL_SYSTEM_SUPPORTGROUP_NUMBER
(
    id                 VARCHAR(36)  not null primary key,
    orgid              VARCHAR(100) not null,
    sysid              VARCHAR(100) not null,
    support_group_code VARCHAR(100) not null,
    support_group_name VARCHAR(100) not null,
    sj_group_code      VARCHAR(100) not null,
    sj_group_name      VARCHAR(100) not null,
    create_person      VARCHAR(100),
    create_time        VARCHAR(100),
    update_person      VARCHAR(100),
    update_time        VARCHAR(100)
) COMMENT '系统对应支持组编号查询';

CREATE TABLE forms_inspection
(
    id                  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '巡检记录单主键',
    sys_name            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用系统名称版本',
    sys_company         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用系统运维单位',
    inspectors          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '巡检人员',
    contact             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
    sys_manager         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统负责人',
    last_date           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '巡检项最后更新日期',
    start_time          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
    end_time            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
    login_states        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录页面是否正常(0：异常；1：正常)',
    login_annotation    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录页面是否正常说明',
    date_states         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据统计页是否正常(0：异常；1：正常)',
    date_annotation     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据统计页是否正常说明',
    book_states         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通讯录管理页面是否正常(0：异常；1：正常)',
    book_annotation     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通讯录管理页面是否正常说明',
    app_states          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用管理页面是否正常(0：异常；1：正常)',
    app_annotation      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用管理页面是否正常说明',
    global_states       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '全局设置页面是否正常(0：异常；1：正常)',
    global_annotation   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '全局设置页面是否正常说明',
    limit_states        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限管理页面是否正常(0：异常；1：正常)',
    limit_annotation    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限管理页面是否正常说明',
    upload_states       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批量上传页面是否正常(0：异常；1：正常)',
    upload_annotation   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批量上传页面是否正常说明',
    os_states           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器操作系统是否正常(0：异常；1：正常)',
    os_annotation       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器操作系统是否正常说明',
    network_states      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器网络是否正常(0：异常；1：正常)',
    network_annotation  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器网络是否正常说明',
    access_states       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用分系统访问是否正常(0：异常；1：正常)',
    access_annotation   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用分系统访问是否正常说明',
    database_states     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库是否正常(0：异常；1：正常)',
    database_annotation varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库是否正常说明',
    remark              varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET
FOREIGN_KEY_CHECKS = 1;

create table sys_upgrade_form
(
    id                      varchar(32)  NOT NULL,
    sys_number              varchar(50)  not null comment '编号',
    sys_name                varchar(100) not null comment '应用系统名称',
    sys_contractor          varchar(100) comment '应用系统承建单位',
    item_leader             varchar(32) comment '项目负责人',
    tel                     varchar(20) comment '联系方式',
    apply_date              varchar(32) comment '申请日期',
    dept                    varchar(64) comment '业务部门',
    current_version         varchar(50) comment '当前版本',
    upgraded_version        varchar(50) comment '升级后版本',
    related_systems         varchar(50) comment '相关系统',
    start_time              varchar(32) comment '系统升级开始时间',
    end_time                varchar(32) comment '系统升级完成时间',
    upgrade_reason          varchar(500) comment '升级原因',
    upgrade_content         varchar(500) comment '升级内容',
    remarks                 varchar(200) comment '备注',
    leader_sign             varchar(100) comment '项目负责人签字',
    leader_sign_time        varchar(32) comment '项目负责人签字日期',
    sys_opinion             varchar(100) comment '相关系统意见',
    sys_opinion_time        varchar(32) comment '相关系统意见日期',
    collective_unit_opinion varchar(100) comment '总集单位审批意见',
    collective_unit_time    varchar(32) comment '总集单位审批意见日期',
    bureau_unit_opinion     varchar(500) comment '技术局审批意见',
    creat_time              varchar(32) comment '创建时间',
    primary key (id)
) engine=innodb comment = '系统升级申请单';


CREATE TABLE automate_middle
(
    AUTO_ID      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
    BUSINESS_ID  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务表主键ID',
    BUSINESS_NO  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务表单号',
    STATUS       varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '待自动化校验标识，0-代表未校验，1-代表校验通过，9-表示校验失败',
    CHECK_COUNT  decimal(65, 30) NULL DEFAULT NULL COMMENT '校验次数,最多校验三次，三次都失败不再校验',
    CREATE_TIME  datetime(0) NULL DEFAULT NULL COMMENT '生成时间',
    CHANGE_BACK  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回退方案',
    COMONT       text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '处理意见',
    AUTO_MESSAGE varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自动化执行结果信息（针对校验失败场景存储失败原因）',
    PRIMARY KEY (AUTO_ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '软研接口定时任务中间表' ROW_FORMAT = Compact;

CREATE TABLE knowledge_search
(
    ID              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    NAME            varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引名称',
    REMARK          varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    CREATE_TIME     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
    UPdatetime_TIME varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
    STATUS          varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
    CREATE_ID       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
    UPdatetime_ID   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
    CONTENT_ID      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文档id',
    TITLE           varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识标题'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '知识检索' ROW_FORMAT = Compact;


CREATE TABLE ITSM_HC_COLLECTING
(
    id                     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '巡检记录单主键',
    sys_name               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用系统名称版本',
    sys_company            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用系统运维单位',
    inspectors             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '巡检人员',
    contact                varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
    sys_manager            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统负责人',
    last_date              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '巡检项最后更新日期',
    start_time             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
    end_time               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
    crawler_states         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爬虫进程是否正常(0：异常；1：正常)',
    crawler_annotation     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爬虫进程是否正常说明',
    dispatching_states     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采集调度进程运行状态是否正常',
    dispatching_annotation varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采集调度进程运行状态是否正常说明',
    process_states         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采集进程是否正常',
    process_annotation     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采集进程是否正常说明',
    warning_states         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常预警运行是否正常',
    warning_annotation     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常预警运行是否正常说明',
    update_states          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爬虫更新进程运行是否正常',
    update_annotation      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爬虫更新进程运行是否正常说明',
    quality_states         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据质量核验运行是否正常',
    quality_annotation     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据质量核验运行是否正常说明',
    remark                 varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

CREATE TABLE patrol_inspection
(
    PATROL_ID         varchar(36) NOT NULL COMMENT '主键id',
    PATROL_NAME       varchar(100) DEFAULT NULL COMMENT '应用系统名称版本',
    PATROL_DEPARTNAME varchar(200) DEFAULT NULL COMMENT '应用系统运维单位',
    CREATE_NAME       varchar(50)  DEFAULT NULL COMMENT '巡查人员',
    TELEPHONE         varchar(50)  DEFAULT NULL COMMENT '联系方式',
    LEADER_NAME       varchar(50)  DEFAULT NULL COMMENT '系统负责人',
    END_UP_TIME       varchar(50)  DEFAULT NULL COMMENT '巡检项最后更新日期',
    START_TIME        varchar(50)  DEFAULT NULL COMMENT '开始时间',
    END_TIME          varchar(50)  DEFAULT NULL COMMENT '结束时间',
    SYNC_DATA         varchar(2)   DEFAULT NULL COMMENT '网闸数据同步巡查结果 0：正常 1：异常',
    SYS_SERVER_DATA   varchar(2)   DEFAULT NULL COMMENT '系统服务器状态 0：正常 1：异常',
    PAGE_DATA         varchar(2)   DEFAULT NULL COMMENT '首页展示 0：正常 1：异常',
    HOT_SEARCH_DATA   varchar(2)   DEFAULT NULL COMMENT '门户热搜词展示 0：正常 1：异常',
    EMERGENCY_DATA    varchar(2)   DEFAULT NULL COMMENT '突发预警展示 0：正常 1：异常',
    HOT_TOPIC_DATA    varchar(2)   DEFAULT NULL COMMENT '热点话题展示 0：正常 1：异常',
    HOT_ESSAY_DATA    varchar(2)   DEFAULT NULL COMMENT '热点文章展示 0：正常 1：异常',
    CONTENT_DATA      varchar(2)   DEFAULT NULL COMMENT '内容监测展示 0：正常 1：异常',
    SPECIAL_DATA      varchar(2)   DEFAULT NULL COMMENT '专题分析展示 0：正常 1：异常',
    INFORMATION_DATA  varchar(2)   DEFAULT NULL COMMENT '信息检索展示 0：正常 1：异常',
    REMART            varchar(500) DEFAULT NULL COMMENT '备注',
    CREATE_TIME       varchar(50)  DEFAULT NULL COMMENT '创建时间',
    CREATE_ID         varchar(36)  DEFAULT NULL COMMENT '创建人id',
    SYS_ID            varchar(64)  DEFAULT NULL,
    PRIMARY KEY (PATROL_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 上海银行事件单  2022-06-08
drop table if exists event_sheet;
CREATE TABLE `event_sheet`
(
    `event_id`            varchar(32) NOT NULL DEFAULT '' COMMENT '事件单id',
    `event_no`            varchar(32)          DEFAULT NULL COMMENT '事件单编号',
    `report_org`          varchar(32)          DEFAULT NULL COMMENT '上报部门',
    `report_person`       varchar(32)          DEFAULT NULL COMMENT '上报人',
    `report_phone`        varchar(32)          DEFAULT NULL COMMENT '上报电话',
    `event_title`         varchar(512)         DEFAULT NULL COMMENT '事件标题',
    `event_info`          varchar(4000)        DEFAULT NULL COMMENT '事件详述',
    `custom_info`         varchar(4000)        DEFAULT NULL COMMENT '客户信息',
    `org_flag`            varchar(2)           DEFAULT NULL COMMENT '总分行事件标记 0-总行；1-分行',
    `side_flag`           varchar(2)           DEFAULT NULL COMMENT '中心侧/网点侧事件 0-中心测事件；1-网点测事件',
    `event_category`      varchar(32)          DEFAULT NULL COMMENT '类别',
    `event_subclass`      varchar(32)          DEFAULT NULL COMMENT '子类',
    `event_entry`         varchar(32)          DEFAULT NULL COMMENT '条目',
    `event_subentry`      varchar(32)          DEFAULT NULL COMMENT '子条目',
    `event_source`        varchar(32)          DEFAULT NULL COMMENT '来源',
    `inf_level`           varchar(32)          DEFAULT NULL COMMENT '影响程度',
    `inf_range`           varchar(32)          DEFAULT NULL COMMENT '影响范围',
    `event_priority`      varchar(32)          DEFAULT NULL COMMENT '优先级',
    `target_resolve_date` varchar(32)          DEFAULT NULL COMMENT '目标解决日期',
    `event_first_type`    varchar(32)          DEFAULT NULL COMMENT '一级',
    `event_second_type`   varchar(32)          DEFAULT NULL COMMENT '二级',
    `event_three_type`    varchar(32)          DEFAULT NULL COMMENT '三级',
    `urgent_flag`         varchar(2)           DEFAULT NULL COMMENT '是否加急，0-否；1-是',
    `finance_flag`        varchar(2)           DEFAULT NULL COMMENT '是否财务类事件',
    `assigned_group`      varchar(32)          DEFAULT NULL COMMENT '受派组',
    `assigned_person`     varchar(32)          DEFAULT NULL COMMENT '受派人',
    `event_status`        varchar(2)           DEFAULT NULL COMMENT '状态',
    `event_type`          varchar(32)          DEFAULT NULL COMMENT '事件分类',
    `event_helper_result` varchar(2000)        DEFAULT NULL COMMENT '事件助手处理结果',
    `event_heal_result`   varchar(2000)        DEFAULT NULL COMMENT '事件自愈处理结果',
    `event_solution`      varchar(2000)        DEFAULT NULL COMMENT '解决方案',
    `event_reason_type`   varchar(32)          DEFAULT NULL COMMENT '事件原因类型（一级）',
    `event_reason`        varchar(2000)        DEFAULT NULL COMMENT '事件原因',
    `inf_system`          varchar(512)         DEFAULT NULL COMMENT '受影响的系统',
    `actual_fault_system` varchar(512)         DEFAULT NULL COMMENT '实际故障系统',
    `fault_know_point`    varchar(512)         DEFAULT NULL COMMENT '故障知识点',
    `inf_face`            varchar(32)          DEFAULT NULL COMMENT '影响面',
    `second_org`          varchar(32)          DEFAULT NULL COMMENT '二线部门',
    `second_deal_org`     varchar(32)          DEFAULT NULL COMMENT '二线处理部门',
    `second_deal_person`  varchar(32)          DEFAULT NULL COMMENT '二线处理人员',
    `solution_valid_flag` varchar(2)           DEFAULT NULL COMMENT '二线解决方案是否有效 0-否；1-是',
    `data_quality_flag`   varchar(2)           DEFAULT NULL COMMENT '是否数据质量问题 0-否；1-是',
    `close_code`          varchar(2)           DEFAULT NULL COMMENT '关闭代码',
    `event_level`         varchar(32)          DEFAULT NULL COMMENT '事件级别',
    `change_no`           varchar(32)          DEFAULT NULL COMMENT '关联的变更、需求号',
    `problem_no`          varchar(32)          DEFAULT NULL COMMENT '关联的问题号',
    `history_event_id`    varchar(2000)        DEFAULT NULL COMMENT '历史事件单id',
    `assign_id`           varchar(32)          DEFAULT NULL COMMENT 'IT服务台分派人',
    `receive_group_id`    varchar(32)          DEFAULT NULL COMMENT '接单（工作组id）',
    `deal_id`             varchar(32)          DEFAULT NULL COMMENT '处理人id',
    `draw_id`             varchar(32)          DEFAULT NULL COMMENT '领取人id',
    `pre_solution_id`     varchar(32)          DEFAULT NULL COMMENT '预解决人id',
    `solution_id`         varchar(32)          DEFAULT NULL COMMENT '解决人id',
    `close_id`            varchar(32)          DEFAULT NULL COMMENT '关闭人id',
    `create_by`           varchar(32)          DEFAULT NULL COMMENT '创建人',
    `create_time`         datetime             DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(32)          DEFAULT NULL COMMENT '更新人',
    `update_time`         datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`event_id`),
    UNIQUE KEY `event_no` (`event_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件单表';

-- 上海银行问题单  贺彩利 2022-06-18
drop table if exists problem_sheet;
CREATE TABLE `problem_sheet`
(
    `problem_id`                    VARCHAR(32) NOT NULL DEFAULT '' COMMENT '问题单id',
    `problem_no`                    VARCHAR(32)          DEFAULT NULL COMMENT '问题单编号',
    `stage`                         VARCHAR(2)           DEFAULT NULL COMMENT '阶段 0-未提交(草稿);1-处理中(合规审核、技术审核、已指派、调查中、根因已明解决方案未定、解决方案审核中);
                                                2-已解决(已解决审核人确认中、已解决管理员确认中、根因已明解决中、已解决发起人确认中、已解决发起部室经理确认中、已解决总经理室审批中、观察中);
						                        3-已关闭(已取消和已关闭)',
    `status`                        VARCHAR(2)           DEFAULT NULL COMMENT '状态 0-草稿;1-合规审核;2-技术审核;3-已指派;4-调查中;5-根因已明解决方案未定;6-解决方案审核中;7-根因已明解决中;8-已解决审核人确认中;
                                                 9-已解决管理员确认中;10-已解决发起人确认中;11-已解决发起部室经理确认中;12-已解决总经理室审批中;13-观察中;14-取消;15-关闭',
    `status_reason`                 VARCHAR(500)         DEFAULT NULL COMMENT '状态理由',
    `current_handler_id`            VARCHAR(64)          DEFAULT NULL COMMENT '问题当前处理人',
    `ori_dep_manager_id`            VARCHAR(32)          DEFAULT NULL COMMENT '问题发起部室经理',
    `originator_id`                 VARCHAR(64)          DEFAULT NULL COMMENT '问题发起人',
    `manager_id`                    VARCHAR(64)          DEFAULT NULL COMMENT '问题管理员',
    `problem_title`                 VARCHAR(512)         DEFAULT NULL COMMENT '问题标题',
    `problem_description`           VARCHAR(4000)        DEFAULT NULL COMMENT '问题现象描述',
    `problem_source`                VARCHAR(32)          DEFAULT NULL COMMENT '来源',
    `problem_category`              VARCHAR(32)          DEFAULT NULL COMMENT '类别',
    `problem_subclz`                VARCHAR(32)          DEFAULT NULL COMMENT '子类',
    `problem_entry`                 VARCHAR(32)          DEFAULT NULL COMMENT '条目',
    `problem_subentry`              VARCHAR(32)          DEFAULT NULL COMMENT '子条目',
    `temp_solve_flag`               VARCHAR(2)           DEFAULT NULL COMMENT '是否有临时解决措施标记 0-否(没有解决方案,不会弹出事件临时解决措施输入框,系统默认值:无)；1-是(有解决方案,会弹出事件临时解决措施输入框)',
    `temp_solutions`                VARCHAR(500)         DEFAULT NULL COMMENT '临时解决措施',
    `problem_type`                  VARCHAR(2)           DEFAULT NULL COMMENT '问题类型 1-性能容量问题;2-可用性问题;3-账务类问题;4-账务类问题;5-功能问题;6-数据质量问题;7-流程管理类问题;8-其他',
    `risk_level`                    VARCHAR(2)           DEFAULT NULL COMMENT '风险程度 0-低;1-高',
    `frequency`                     VARCHAR(2)           DEFAULT NULL COMMENT '发生频率 0-低(一月以上不发生);1-中(一月数次以上);2-高(一周数次以上)',
    `priority`                      VARCHAR(2)           DEFAULT NULL COMMENT '优先级 0-低;1-高',
    `interrupt_flag`                VARCHAR(2)           DEFAULT NULL COMMENT '影响业务中断 0-否;1-是',
    `plan_complete_time`            datetime             DEFAULT NULL COMMENT '计划完成时间',
    `solver_dep_id`                 VARCHAR(64)          DEFAULT NULL COMMENT '问题牵头部室',
    `auditor_id`                    VARCHAR(64)          DEFAULT NULL COMMENT '问题审核人',
    `solver_id`                     VARCHAR(64)          DEFAULT NULL COMMENT '问题牵头人',
    `cause_summary`                 VARCHAR(4000)        DEFAULT NULL COMMENT '根因分析汇总',
    `cause_clz1`                    VARCHAR(32)          DEFAULT NULL COMMENT '根因分类一',
    `cause_clz2`                    VARCHAR(32)          DEFAULT NULL COMMENT '根因分类二',
    `solution_summary`              VARCHAR(4000)        DEFAULT NULL COMMENT '解决方案汇总',
    `solution_modify_num`           INT                  DEFAULT 0 COMMENT '解决方案修改次数',
    `plan_complete_time_modify_num` INT                  DEFAULT 0 COMMENT '计划解决时间修改次数',
    `resolution_completion`         VARCHAR(500)         DEFAULT NULL COMMENT '解决完成情况',
    `solver_last_updated`           datetime             DEFAULT NULL COMMENT '问题牵头人上次更新时间',
    `solve_time`                    datetime             DEFAULT NULL COMMENT '解决时间',
    `observe_time`                  datetime             DEFAULT NULL COMMENT '观察期限',
    `observe_note`                  VARCHAR(500)         DEFAULT NULL COMMENT '观察说明',
    `reopen_num`                    INT                  DEFAULT 0 COMMENT '问题重新打开次数',
    `cancel_note`                   VARCHAR(500)         DEFAULT NULL COMMENT '问题取消说明',
    `cancel_reason`                 VARCHAR(32)          DEFAULT NULL COMMENT '问题取消原因',
    `close_time`                    datetime             DEFAULT NULL COMMENT '问题关闭时间(包括关闭时间和取消时间（取消要有取消说明）)',
    `close_type`                    VARCHAR(32)          DEFAULT NULL COMMENT '问题关闭分类',
    `event_no`                      varchar(32)          DEFAULT NULL COMMENT '关联的事件号',
    `change_no`                     varchar(32)          DEFAULT NULL COMMENT '关联的变更号',
    `create_by`                     VARCHAR(64)          DEFAULT NULL COMMENT '创建人',
    `create_time`                   datetime             DEFAULT NULL COMMENT '创建时间',
    `update_by`                     VARCHAR(64)          DEFAULT NULL COMMENT '更新人',
    `update_time`                   datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`problem_id`),
    UNIQUE KEY `problem_no` (`problem_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='问题单表';

-- 优先级表单  姜峰 2022-07-4

DROP TABLE IF EXISTS `pub_paravalue_priority`;
CREATE TABLE `pub_paravalue_priority`
(
    `id`                      varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级ID',
    `pub_paravalue_id_degree` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '影响程度子集ID',
    `pub_paravalue_id_scope`  varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '影响范围子集ID',
    `priority`                varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级级别',
    `code`                    varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表code“textPriority”'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;


-- 2022-07-04 新建自定义表单带有json数据的表单格式

-- ----------------------------
-- Table structure for design_biz_json_data
-- ----------------------------
DROP TABLE IF EXISTS `design_biz_json_data`;
CREATE TABLE `design_biz_json_data`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自定义表单内容表主键ID',
    `json_data`    longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '自定义表单内容json',
    `biz_id`       bigint(20) NOT NULL COMMENT '自定义表单业务表ID 关联表design_biz_xxx',
    `biz_table`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务表名称  design_biz_xxx',
    `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `created_by`   varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `updated_by`   varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uniq_biz_id_biz_table`(`biz_id`, `biz_table`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;


-- 2022-07-04 新建优先级 表单

DROP TABLE IF EXISTS `pub_paravalue_priority`;
CREATE TABLE `pub_paravalue_priority`
(
    `id`                      varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级ID',
    `pub_paravalue_id_degree` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '影响程度子集ID',
    `pub_paravalue_id_scope`  varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '影响范围子集ID',
    `priority`                varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级级别',
    `code`                    varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表code“textPriority”'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;


-- 2022-07-05  低代码平台基础表
-- ----------------------------
-- Table structure for design_api
-- ----------------------------
DROP TABLE IF EXISTS `design_api`;
CREATE TABLE `design_api`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`            bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `name`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `url`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
    `proxy_url`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理地址',
    `method`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
    `request_header`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求头',
    `response_header`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应头',
    `request_content_type`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求内容类型',
    `response_content_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '响应内容类型',
    `query_param`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '查询字符串',
    `body_param`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求体',
    `response`              text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应',
    `status`                char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time`          datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time`          datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_code
-- ----------------------------
DROP TABLE IF EXISTS `design_code`;
CREATE TABLE `design_code`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`   bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `code`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态码',
    `type`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(0->成功, 1->失败)',
    `description`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '状态码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_config
-- ----------------------------
DROP TABLE IF EXISTS `design_config`;
CREATE TABLE `design_config`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`      bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `api_host`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API地址',
    `description_api` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述接口回调地址',
    `root_path`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '根路由',
    `package_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打包名称',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time`    datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time`    datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_design_config_project_id`(`project_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_datasource
-- ----------------------------
DROP TABLE IF EXISTS `design_datasource`;
CREATE TABLE `design_datasource`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`   bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `description`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `api_host`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机地址',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_dict
-- ----------------------------
DROP TABLE IF EXISTS `design_dict`;
CREATE TABLE `design_dict`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`   bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `type`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_design_dict_project_id_type`(`project_id`, `type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `design_dict_item`;
CREATE TABLE `design_dict_item`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dict_id`      bigint(20) NULL DEFAULT NULL COMMENT '字典ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `value`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值',
    `sort`         int(11) NULL DEFAULT NULL COMMENT '顺序',
    `is_default`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否默认',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_field
-- ----------------------------
DROP TABLE IF EXISTS `design_field`;
CREATE TABLE `design_field`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `table_id`     bigint(20) NULL DEFAULT NULL COMMENT '表ID',
    `component_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '表单组件ID',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_field_version
-- ----------------------------
DROP TABLE IF EXISTS `design_field_version`;
CREATE TABLE `design_field_version`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `field_id`        bigint(20) NULL DEFAULT NULL COMMENT '字段ID',
    `table_id`        bigint(20) NULL DEFAULT NULL COMMENT '表ID',
    `form_version_id` bigint(20) NULL DEFAULT NULL COMMENT '表单版本ID',
    `name`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
    `type`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
    `length`          int(4) NULL DEFAULT NULL COMMENT '长度',
    `decimals`        int(2) NULL DEFAULT NULL COMMENT '浮点数精度',
    `nullable`        varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否可为空(null或not null)',
    `description`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `execution_sql`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'SQL语句',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time`    datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time`    datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '字段版本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_form
-- ----------------------------
DROP TABLE IF EXISTS `design_form`;
CREATE TABLE `design_form`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_form_version
-- ----------------------------
DROP TABLE IF EXISTS `design_form_version`;
CREATE TABLE `design_form_version`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `form_id`        bigint(20) NULL DEFAULT NULL COMMENT '表单ID',
    `version`        int(4) NULL DEFAULT NULL COMMENT '版本号(自增数字)',
    `data`           longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '字段记录',
    `increased_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '增加后字段记录(全表记录)',
    `is_current`     int(1) NULL DEFAULT NULL COMMENT '是否为当前版本(0->否,1->是)',
    `name`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `type`           int(1) NULL DEFAULT NULL COMMENT '类型',
    `code`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
    `charset`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字符集',
    `html`           longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'HTML源码',
    `json`           longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'JSON源码',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_time`   datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `created_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `updated_time`   datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `updated_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单版本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_function
-- ----------------------------
DROP TABLE IF EXISTS `design_function`;
CREATE TABLE `design_function`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`   bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `code`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '函数定义名',
    `params`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
    `content`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '函数体',
    `description`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '函数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_menu
-- ----------------------------
DROP TABLE IF EXISTS `design_menu`;
CREATE TABLE `design_menu`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `parent_id`    bigint(20) NULL DEFAULT NULL COMMENT '父级ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `sort`         int(11) NULL DEFAULT NULL COMMENT '顺序',
    `permission`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限',
    `type`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
    `visible`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否显示',
    `path`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由',
    `component`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
    `icon`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_page
-- ----------------------------
DROP TABLE IF EXISTS `design_page`;
CREATE TABLE `design_page`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`   bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `code`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面定义名',
    `html`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'HTML源码',
    `json`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'JSON源码',
    `preview`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预览图',
    `life_cycle`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '生命周期',
    `style`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '样式',
    `init_request` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '初始化请求',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `path`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由',
    `route`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '路由结构',
    `is_index`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否为首页',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_design_page_project_id_path`(`project_id`, `path`) USING BTREE,
    UNIQUE INDEX `idx_design_page_project_id_code`(`project_id`, `code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '页面' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_page_function
-- ----------------------------
DROP TABLE IF EXISTS `design_page_function`;
CREATE TABLE `design_page_function`
(
    `page_id`     bigint(20) NOT NULL COMMENT '页面ID',
    `function_id` bigint(20) NOT NULL COMMENT '函数ID',
    PRIMARY KEY (`page_id`, `function_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '页面函数关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_project
-- ----------------------------
DROP TABLE IF EXISTS `design_project`;
CREATE TABLE `design_project`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `template_id`  bigint(20) NULL DEFAULT NULL COMMENT '默认模板ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `identity`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目标识',
    `type`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
    `description`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_design_project_identity`(`identity`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_role
-- ----------------------------
DROP TABLE IF EXISTS `design_role`;
CREATE TABLE `design_role`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `key`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键字',
    `sort`         int(11) NULL DEFAULT NULL COMMENT '顺序',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `design_role_menu`;
CREATE TABLE `design_role_menu`
(
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_route
-- ----------------------------
DROP TABLE IF EXISTS `design_route`;
CREATE TABLE `design_route`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `page_id`      bigint(20) NULL DEFAULT NULL COMMENT '页面ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `path`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由',
    `route`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '路由结构',
    `is_index`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否为首页',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '路由' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_table
-- ----------------------------
DROP TABLE IF EXISTS `design_table`;
CREATE TABLE `design_table`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `form_id`      bigint(20) NULL DEFAULT NULL COMMENT '表单ID',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = ' 表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_table_record
-- ----------------------------
DROP TABLE IF EXISTS `design_table_record`;
CREATE TABLE `design_table_record`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `table_id`             bigint(20) NULL DEFAULT NULL COMMENT '表ID',
    `package_name`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包名',
    `module_name`          varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名',
    `class_name`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '类',
    `business_name`        varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务名',
    `business_description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务描述',
    `author`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
    `path`                 varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/' COMMENT '路径(默认项目路径)',
    `status`               int(1) NULL DEFAULT NULL COMMENT '状态',
    `created_time`         datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `created_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `updated_time`         datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `updated_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_table_version
-- ----------------------------
DROP TABLE IF EXISTS `design_table_version`;
CREATE TABLE `design_table_version`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `table_id`        bigint(20) NULL DEFAULT NULL COMMENT '表ID',
    `form_version_id` bigint(20) NULL DEFAULT NULL COMMENT '表单版本ID',
    `version`         int(4) NULL DEFAULT NULL COMMENT '版本号(自增数字)',
    `name`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
    `description`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表描述',
    `is_current`      int(1) NULL DEFAULT NULL COMMENT '是否为当前版本(0->否,1->是)',
    `execution_sql`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'SQL语句',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time`    datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time`    datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '表版本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_template
-- ----------------------------
DROP TABLE IF EXISTS `design_template`;
CREATE TABLE `design_template`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `path`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认路由',
    `type`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
    `content`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'JSON源码',
    `html`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'HTML源码',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '布局模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_user
-- ----------------------------
DROP TABLE IF EXISTS `design_user`;
CREATE TABLE `design_user`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `nickname`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
    `avatar`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
    `phone`        varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
    `email`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
    `gender`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
    `password`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
    `salt`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_user_role
-- ----------------------------
DROP TABLE IF EXISTS `design_user_role`;
CREATE TABLE `design_user_role`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for design_version
-- ----------------------------
DROP TABLE IF EXISTS `design_version`;
CREATE TABLE `design_version`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id`   bigint(20) NULL DEFAULT NULL COMMENT '项目ID',
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `description`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `sort`         int(11) NULL DEFAULT NULL COMMENT '顺序',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
    `created_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `updated_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '版本' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;

-- cuicc 20220706
commit;

-- add by zhangchao  2022-07-19
ALTER TABLE event_sheet
    ADD back_flag VARCHAR(2) COMMENT '退回补全标识';


-- 流程和表单字段关联表

/*
 Navicat Premium Data Transfer

 Source Server         : MZY
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : bj-cynosdbmysql-grp-lr6kihug.sql.tencentcdb.com:20285
 Source Schema         : zjq

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 25/07/2022 14:28:27
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for field_activity_node
-- ----------------------------
DROP TABLE IF EXISTS `field_activity_node`;
CREATE TABLE `field_activity_node`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `field_name`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段',
    `field_desc`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段描述',
    `field_status`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '表字段回显状态：0-只读，1-可编辑，2-隐藏',
    `activity_node_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '流程节点ID',
    `form_version_id`  bigint(20) NOT NULL COMMENT '表单版本主键表ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `field_name_activity_node_id_form_version_id`(`field_name`, `activity_node_id`, `form_version_id`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;

-- 文件表  add by    2022-07-27
CREATE TABLE `design_file`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `origin_file_name` varchar(64) NULL COMMENT '原始文件名',
    `file_name`        varchar(64) NULL COMMENT '文件名',
    `location`         varchar(255) NULL COMMENT '路径',
    `url`              varchar(255) NULL COMMENT '访问地址',
    `content_type`     varchar(32) NULL COMMENT '内容类型',
    `length`           bigint(20) NULL COMMENT '文件大小',
    `hashcode`         varchar(255) NULL COMMENT '散列码',
    `created_time`     datetime NULL COMMENT '创建时间',
    `updated_time`     datetime NULL COMMENT '更新时间',
    `created_by`       varchar(64) NULL COMMENT '创建人',
    `updated_by`       varchar(64) NULL COMMENT '更新人',
    `status`           int(1) NULL COMMENT '状态',
    PRIMARY KEY (`id`)
) COMMENT = '文件';

--  2022-08-04 业务状态和节点配置表
-- ----------------------------
-- Table structure for form_status_activity_node
-- ----------------------------
DROP TABLE IF EXISTS `form_status_activity_node`;
CREATE TABLE `form_status_activity_node`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表单状态id',
    `biz_status_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态名称',
    `activity_node_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '流程节点ID',
    `form_version_id`  bigint(20) NOT NULL COMMENT '表单版本id',
    `sort`             int(255) NULL DEFAULT NULL COMMENT '排序',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `biz_status_activity_node`(`biz_status_name`, `activity_node_id`, `form_version_id`) USING BTREE COMMENT '业务状态名+节点id唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

--  2022-08-04 业务状态和步骤配置表
DROP TABLE IF EXISTS `form_step_status`;
CREATE TABLE `form_step_status`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '步骤和工单状态关联表',
    `step_name`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '步骤名称',
    `biz_status_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务状态名称',
    `form_version_id` bigint(20) NOT NULL COMMENT '表单id',
    `sort`            int(255) NULL DEFAULT NULL COMMENT '排序',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `biz_status_name_form_version_id`(`biz_status_name`, `form_version_id`, `step_name`) USING BTREE COMMENT '业务状态+表单ID+步骤名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



-- 2022-08-22  按钮审批模板表

-- ----------------------------
-- Table structure for approve_template
-- ----------------------------
DROP TABLE IF EXISTS `approve_template`;
CREATE TABLE `approve_template`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板表ID',
    `json`          longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板json样式',
    `template_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_template
-- ----------------------------
INSERT INTO `approve_template` VALUES (1, '[{\r\n	\"identify\": \"el-form\",\r\n	\"tag\": \"el-form\",\r\n	\"title\": \"el表单容器\",\r\n	\"slots\": true,\r\n	\"canMove\": true,\r\n	\"canAllowTo\": false,\r\n	\"display\": true,\r\n	\"props\": {\r\n		\"inline\": {\r\n			\"default\": false,\r\n			\"sourceDefault\": false\r\n		},\r\n		\"label-position\": {\r\n			\"default\": \"right\",\r\n			\"sourceDefault\": \"right\"\r\n		},\r\n		\"label-width\": {\r\n			\"default\": \"50px\",\r\n			\"sourceDefault\": \"50px\"\r\n		},\r\n		\"label-suffix\": {\r\n			\"default\": \"\",\r\n			\"sourceDefault\": \"\"\r\n		},\r\n		\"hide-required-asterisk\": {\r\n			\"default\": false,\r\n			\"sourceDefault\": false\r\n		},\r\n		\"show-message\": {\r\n			\"default\": false,\r\n			\"sourceDefault\": false\r\n		},\r\n		\"inline-message\": {\r\n			\"default\": false,\r\n			\"sourceDefault\": false\r\n		},\r\n		\"status-icon\": {\r\n			\"default\": false,\r\n			\"sourceDefault\": false\r\n		},\r\n		\"validate-on-rule-change\": {\r\n			\"default\": true,\r\n			\"sourceDefault\": false\r\n		},\r\n		\"size\": {\r\n			\"default\": \"default\",\r\n			\"sourceDefault\": \"default\"\r\n		},\r\n		\"disabled\": {\r\n			\"default\": false,\r\n			\"sourceDefault\": false\r\n		}\r\n	},\r\n	\"extProps\": {\r\n		\"列容器个数\": \"ChangeChildrenCount\"\r\n	},\r\n	\"tagSlots\": {\r\n		\"enable\": false,\r\n		\"val\": \"\"\r\n	},\r\n	\"childrens\": [{\r\n		\"identify\": \"el-form-item\",\r\n		\"tag\": \"el-form-item\",\r\n		\"title\": \"el表单项\",\r\n		\"slots\": true,\r\n		\"canMove\": true,\r\n		\"canAllowTo\": true,\r\n		\"display\": true,\r\n		\"props\": {\r\n			\"prop\": {\r\n				\"default\": \"approved_person\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"label\": {\r\n				\"default\": \"审批人\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"label-width\": {\r\n				\"default\": \"120px\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"required\": {\r\n				\"default\": false,\r\n				\"sourceDefault\": false\r\n			},\r\n			\"rules\": {\r\n				\"default\": {},\r\n				\"sourceDefault\": {}\r\n			},\r\n			\"error\": {\r\n				\"default\": \"\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"show-message\": {\r\n				\"default\": true,\r\n				\"sourceDefault\": true\r\n			},\r\n			\"inline-message\": {\r\n				\"default\": false,\r\n				\"sourceDefault\": false\r\n			},\r\n			\"size\": {\r\n				\"default\": \"default\",\r\n				\"sourceDefault\": \"default\"\r\n			}\r\n		},\r\n		\"tagSlots\": {\r\n			\"enable\": false,\r\n			\"val\": \"\"\r\n		},\r\n		\"childrens\": [{\r\n			\"identify\": \"el-select-v2\",\r\n			\"tag\": \"el-select-v2\",\r\n			\"title\": \"el-虚拟选择器\",\r\n			\"slots\": false,\r\n			\"canMove\": true,\r\n			\"canAllowTo\": true,\r\n			\"display\": true,\r\n			\"props\": {\r\n				\"multiple\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"options\": {\r\n					\"default\": [{\r\n						\"label\": \"选项一\",\r\n						\"value\": 1\r\n					}, {\r\n						\"label\": \"选项二\",\r\n						\"value\": 2\r\n					}],\r\n					\"sourceDefault\": []\r\n				},\r\n				\"disabled\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"value-key\": {\r\n					\"default\": \"value\",\r\n					\"sourceDefault\": \"value\"\r\n				},\r\n				\"size\": {\r\n					\"default\": \"default\",\r\n					\"sourceDefault\": \"default\"\r\n				},\r\n				\"clearable\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"clear-icon\": {\r\n					\"default\": \"CircleClose\",\r\n					\"sourceDefault\": \"CircleClose\"\r\n				},\r\n				\"collapse-tags\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"collapse-tags-tooltip\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"multiple-limit\": {\r\n					\"default\": 0,\r\n					\"sourceDefault\": 0\r\n				},\r\n				\"name\": {\r\n					\"default\": \"\",\r\n					\"sourceDefault\": \"\"\r\n				},\r\n				\"effect\": {\r\n					\"default\": \"light\",\r\n					\"sourceDefault\": \"light\"\r\n				},\r\n				\"autocomplete\": {\r\n					\"default\": \"off\",\r\n					\"sourceDefault\": \"off\"\r\n				},\r\n				\"placeholder\": {\r\n					\"default\": \"请选择\",\r\n					\"sourceDefault\": \"请选择\"\r\n				},\r\n				\"filterable\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"allow-create\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"reserve-keyword\": {\r\n					\"default\": true,\r\n					\"sourceDefault\": true\r\n				},\r\n				\"no-data-text\": {\r\n					\"default\": \"无数据\",\r\n					\"sourceDefault\": \"无数据\"\r\n				},\r\n				\"popper-class\": {\r\n					\"default\": \"\",\r\n					\"sourceDefault\": \"\"\r\n				},\r\n				\"teleported\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": true\r\n				},\r\n				\"persistent\": {\r\n					\"default\": true,\r\n					\"sourceDefault\": true\r\n				},\r\n				\"popper-options\": {\r\n					\"default\": {},\r\n					\"sourceDefault\": {}\r\n				},\r\n				\"automatic-dropdown\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"height\": {\r\n					\"default\": 170,\r\n					\"sourceDefault\": 170\r\n				},\r\n				\"scrollbar-always-on\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"remote\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"remote-method\": {\r\n					\"sourceDefault\": \"\"\r\n				}\r\n			},\r\n			\"tagSlots\": {\r\n				\"enable\": false,\r\n				\"val\": \"\"\r\n			},\r\n			\"eventAttr\": {\r\n				\"change\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"当绑定值变化时触发\",\r\n					\"val\": \"console.log(\'SelectV2--change:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				},\r\n				\"visible-change\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"下拉框出现/隐藏时触发\",\r\n					\"val\": \"console.log(\'SelectV2 --visible-change:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				},\r\n				\"remove-tag\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"多选模式下移除tag时触发\",\r\n					\"val\": \"console.log(\'SelectV2--remote-tag:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				},\r\n				\"clear\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"可清空的单选模式下用户点击清空按钮时触发\",\r\n					\"val\": \"console.log(\'SelectV2 --clear:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				},\r\n				\"blur\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"当选择器的输入框失去焦点时触发\",\r\n					\"val\": \"console.log(\'SelectV2--blur:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				},\r\n				\"focus\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"当选择器的输入框获得焦点时触发\",\r\n					\"val\": \"console.log(\'SelectV2--focus:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				}\r\n			},\r\n			\"childrens\": [],\r\n			\"headerSlot\": [],\r\n			\"compatibility\": false,\r\n			\"v_model\": {\r\n				\"type\": \"constant\",\r\n				\"val\": \"\",\r\n				\"event_name\": \"onChange\"\r\n			},\r\n			\"id\": \"el-select-v2-o6rhj8oyKW\"\r\n		}],\r\n		\"headerSlot\": [],\r\n		\"compatibility\": false,\r\n		\"v_model\": {\r\n			\"type\": \"none\",\r\n			\"val\": \"\"\r\n		},\r\n		\"id\": \"el-form-item-wCV6cmpbUT\"\r\n	}, {\r\n		\"identify\": \"el-form-item\",\r\n		\"tag\": \"el-form-item\",\r\n		\"title\": \"el表单项\",\r\n		\"slots\": true,\r\n		\"canMove\": true,\r\n		\"canAllowTo\": true,\r\n		\"display\": true,\r\n		\"props\": {\r\n			\"prop\": {\r\n				\"default\": \"approved_opinion\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"label\": {\r\n				\"default\": \"审批意见\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"label-width\": {\r\n				\"default\": \"120px\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"required\": {\r\n				\"default\": false,\r\n				\"sourceDefault\": false\r\n			},\r\n			\"rules\": {\r\n				\"default\": {},\r\n				\"sourceDefault\": {}\r\n			},\r\n			\"error\": {\r\n				\"default\": \"\",\r\n				\"sourceDefault\": \"\"\r\n			},\r\n			\"show-message\": {\r\n				\"default\": true,\r\n				\"sourceDefault\": true\r\n			},\r\n			\"inline-message\": {\r\n				\"default\": false,\r\n				\"sourceDefault\": false\r\n			},\r\n			\"size\": {\r\n				\"default\": \"default\",\r\n				\"sourceDefault\": \"default\"\r\n			}\r\n		},\r\n		\"tagSlots\": {\r\n			\"enable\": false,\r\n			\"val\": \"\"\r\n		},\r\n		\"childrens\": [{\r\n			\"identify\": \"el-input\",\r\n			\"tag\": \"el-input\",\r\n			\"title\": \"el输入框\",\r\n			\"slots\": false,\r\n			\"canMove\": true,\r\n			\"canAllowTo\": false,\r\n			\"display\": true,\r\n			\"props\": {\r\n				\"type\": {\r\n					\"default\": \"textarea\",\r\n					\"sourceDefault\": \"text\"\r\n				},\r\n				\"maxlength\": {\r\n					\"default\": 500,\r\n					\"sourceDefault\": 100\r\n				},\r\n				\"minlength\": {\r\n					\"default\": 0,\r\n					\"sourceDefault\": 0\r\n				},\r\n				\"show-word-limit\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"placeholder\": {\r\n					\"default\": \"请输入文字\",\r\n					\"sourceDefault\": \"\"\r\n				},\r\n				\"clearable\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"show-password\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"disabled\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"size\": {\r\n					\"default\": \"default\",\r\n					\"sourceDefault\": \"default\"\r\n				},\r\n				\"prefix-icon\": {\r\n					\"default\": \"\",\r\n					\"sourceDefault\": \"\"\r\n				},\r\n				\"suffix-icon\": {\r\n					\"default\": \"\",\r\n					\"sourceDefault\": \"\"\r\n				},\r\n				\"rows\": {\r\n					\"default\": 6,\r\n					\"sourceDefault\": 2\r\n				},\r\n				\"autosize\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				},\r\n				\"autocomplete\": {\r\n					\"default\": \"off\",\r\n					\"sourceDefault\": \"off\"\r\n				},\r\n				\"name\": {\r\n					\"default\": \"\",\r\n					\"sourceDefault\": \"\"\r\n				},\r\n				\"readonly\": {\r\n					\"default\": false,\r\n					\"sourceDefault\": false\r\n				}\r\n			},\r\n			\"tagSlots\": {\r\n				\"enable\": false,\r\n				\"val\": \"\"\r\n			},\r\n			\"eventAttr\": {\r\n				\"change\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"当绑定值变化时触发\",\r\n					\"val\": \"console.log(\'ColorPicker--change:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				},\r\n				\"active-change\": {\r\n					\"custom\": \"custom\",\r\n					\"tips\": \"显示的颜色发生改变时触发\",\r\n					\"val\": \"console.log(\'ColorPicker--active-change:\'+value);\",\r\n					\"anonymous_params\": [\"value\"],\r\n					\"list\": []\r\n				}\r\n			},\r\n			\"childrens\": [],\r\n			\"headerSlot\": [],\r\n			\"compatibility\": false,\r\n			\"v_model\": {\r\n				\"type\": \"constant\",\r\n				\"val\": \"\",\r\n				\"event_name\": \"onInput\"\r\n			},\r\n			\"id\": \"el-input-mOfA1aTo4X\"\r\n		}],\r\n		\"headerSlot\": [],\r\n		\"compatibility\": false,\r\n		\"v_model\": {\r\n			\"type\": \"none\",\r\n			\"val\": \"\"\r\n		},\r\n		\"id\": \"el-form-item-FHVNPwHRoS\"\r\n	}],\r\n	\"headerSlot\": [],\r\n	\"v_model\": {\r\n		\"type\": \"none\",\r\n		\"val\": \"\"\r\n	},\r\n	\"id\": \"el-form-xyWN9sM7du\"\r\n}]', '审批人+审批意见');
INSERT INTO `approve_template` VALUES (2, '[{\"identify\":\"el-form\",\"tag\":\"el-form\",\"title\":\"el表单容器\",\"slots\":true,\"canMove\":true,\"canAllowTo\":false,\"display\":true,\"props\":{\"inline\":{\"default\":false,\"sourceDefault\":false},\"label-position\":{\"default\":\"right\",\"sourceDefault\":\"right\"},\"label-width\":{\"default\":\"50px\",\"sourceDefault\":\"50px\"},\"label-suffix\":{\"default\":\"\",\"sourceDefault\":\"\"},\"hide-required-asterisk\":{\"default\":false,\"sourceDefault\":false},\"show-message\":{\"default\":false,\"sourceDefault\":false},\"inline-message\":{\"default\":false,\"sourceDefault\":false},\"status-icon\":{\"default\":false,\"sourceDefault\":false},\"validate-on-rule-change\":{\"default\":true,\"sourceDefault\":false},\"size\":{\"default\":\"default\",\"sourceDefault\":\"default\"},\"disabled\":{\"default\":false,\"sourceDefault\":false}},\"extProps\":{\"列容器个数\":\"ChangeChildrenCount\"},\"tagSlots\":{\"enable\":false,\"val\":\"\"},\"childrens\":[{\"identify\":\"el-form-item\",\"tag\":\"el-form-item\",\"title\":\"el表单项\",\"slots\":true,\"canMove\":true,\"canAllowTo\":true,\"display\":true,\"props\":{\"prop\":{\"default\":\"approved_opinion\",\"sourceDefault\":\"\"},\"label\":{\"default\":\"审批意见\",\"sourceDefault\":\"\"},\"label-width\":{\"default\":\"120px\",\"sourceDefault\":\"\"},\"required\":{\"default\":false,\"sourceDefault\":false},\"rules\":{\"default\":{},\"sourceDefault\":{}},\"error\":{\"default\":\"\",\"sourceDefault\":\"\"},\"show-message\":{\"default\":true,\"sourceDefault\":true},\"inline-message\":{\"default\":false,\"sourceDefault\":false},\"size\":{\"default\":\"default\",\"sourceDefault\":\"default\"}},\"tagSlots\":{\"enable\":false,\"val\":\"\"},\"childrens\":[{\"identify\":\"el-input\",\"tag\":\"el-input\",\"title\":\"el输入框\",\"slots\":false,\"canMove\":true,\"canAllowTo\":false,\"display\":true,\"props\":{\"type\":{\"default\":\"textarea\",\"sourceDefault\":\"text\"},\"maxlength\":{\"default\":500,\"sourceDefault\":100},\"minlength\":{\"default\":0,\"sourceDefault\":0},\"show-word-limit\":{\"default\":false,\"sourceDefault\":false},\"placeholder\":{\"default\":\"请输入文字\",\"sourceDefault\":\"\"},\"clearable\":{\"default\":false,\"sourceDefault\":false},\"show-password\":{\"default\":false,\"sourceDefault\":false},\"disabled\":{\"default\":false,\"sourceDefault\":false},\"size\":{\"default\":\"default\",\"sourceDefault\":\"default\"},\"prefix-icon\":{\"default\":\"\",\"sourceDefault\":\"\"},\"suffix-icon\":{\"default\":\"\",\"sourceDefault\":\"\"},\"rows\":{\"default\":6,\"sourceDefault\":2},\"autosize\":{\"default\":false,\"sourceDefault\":false},\"autocomplete\":{\"default\":\"off\",\"sourceDefault\":\"off\"},\"name\":{\"default\":\"\",\"sourceDefault\":\"\"},\"readonly\":{\"default\":false,\"sourceDefault\":false}},\"tagSlots\":{\"enable\":false,\"val\":\"\"},\"eventAttr\":{\"change\":{\"custom\":\"custom\",\"tips\":\"当绑定值变化时触发\",\"val\":\"console.log(\'ColorPicker--change:\'+value);\",\"anonymous_params\":[\"value\"],\"list\":[]},\"active-change\":{\"custom\":\"custom\",\"tips\":\"显示的颜色发生改变时触发\",\"val\":\"console.log(\'ColorPicker--active-change:\'+value);\",\"anonymous_params\":[\"value\"],\"list\":[]}},\"childrens\":[],\"headerSlot\":[],\"compatibility\":false,\"v_model\":{\"type\":\"constant\",\"val\":\"\",\"event_name\":\"onInput\"},\"id\":\"el-input-mOfA1aTo4X\"}],\"headerSlot\":[],\"compatibility\":false,\"v_model\":{\"type\":\"none\",\"val\":\"\"},\"id\":\"el-form-item-FHVNPwHRoS\"}],\"headerSlot\":[],\"v_model\":{\"type\":\"none\",\"val\":\"\"},\"id\":\"el-form-xyWN9sM7du\"}]', '审批意见');
SET FOREIGN_KEY_CHECKS = 1;

-- 2022-08-22  按钮触发动作表
-- ----------------------------
-- Table structure for button_action
-- ----------------------------
DROP TABLE IF EXISTS `button_action`;
CREATE TABLE `button_action`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '按钮配置表',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `fully_qualified_name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类的全限定名',
  `bean_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法所在类的Bean名称',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名',
  `param_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数值',
  `activity_node_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点ID',
  `template_id` bigint(30) NOT NULL COMMENT '模板ID 关联approve_template',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `allField`(`method_name`, `activity_node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;



/*Table structure for table `risk_assessment` */

DROP TABLE IF EXISTS `risk_assessment`;

CREATE TABLE `risk_assessment` (
  `form_id` varchar(255) NOT NULL COMMENT '变更单单号',
  `inf_sys_count` int(11) DEFAULT NULL COMMENT '可能影响的系统数量',
  `inf_func_interrupt_type` int(11) DEFAULT NULL COMMENT '影响在用业务中断的情况',
  `change_type` int(11) DEFAULT NULL COMMENT '变更类别',
  `change_plan_time` int(11) DEFAULT NULL COMMENT '变更规划耗时',
  `change_impl_time` int(11) DEFAULT NULL COMMENT '变更实施耗时',
  `back_time` int(11) DEFAULT NULL COMMENT '回退耗时',
  `back_plan_flag` int(11) DEFAULT NULL COMMENT '有无应急或退回方案',
  `refer_dept_count` int(11) DEFAULT NULL COMMENT '实施所涉及的部室个数',
  `software_change_type` int(11) DEFAULT NULL COMMENT '软件所属变更模块',
  `app_change_type` int(11) DEFAULT NULL COMMENT '应用所属变更模块',
  `refer_sys` varchar(2000) DEFAULT NULL COMMENT '涉及系统',
  `risk_level` int(11) DEFAULT NULL COMMENT '风险级别',
  PRIMARY KEY (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*变更任务单的场景和组织*/
/*Table structure for table `change_task_org` */

/*Table structure for table `change_task_scene` */

DROP TABLE IF EXISTS `change_task_scene`;

CREATE TABLE `change_task_scene` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orgid` varchar(255) DEFAULT NULL COMMENT 'og_org表的orgid',
  `orgname` varchar(255) DEFAULT NULL COMMENT '所属部门',
  `code` varchar(255) DEFAULT NULL COMMENT '表名',
  `name` varchar(255) DEFAULT NULL COMMENT '中文名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

--  操作记录表
DROP TABLE IF EXISTS `operation_details`;
CREATE TABLE `operation_details`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '操作记录主键ID',
  `operation_type` varchar(20) NOT NULL COMMENT '操作类型 --业务单类型',
  `biz_no` varchar(32) NOT NULL COMMENT '业务编号',
  `old_value` varchar(255) NULL DEFAULT NULL COMMENT '旧值',
  `new_value` varchar(255) NULL DEFAULT NULL COMMENT '新值',
  `description` varchar(2000) NOT NULL COMMENT '操作详情记录',
  `created_by` varchar(255) NOT NULL COMMENT '创建人ID',
  `created_name` varchar(255) NOT NULL COMMENT '创建人名称',
  `created_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  DEFAULT CHARSET=utf8;



CREATE TABLE `risk_access_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `changeNo` varchar(255) DEFAULT NULL COMMENT '变更单单号',
  `operateTime` varchar(50) DEFAULT NULL COMMENT '操作时间',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  `initLevel` varchar(50) DEFAULT NULL COMMENT '原始风险级别',
  `currentLevel` varchar(50) DEFAULT NULL COMMENT '当前风险级别',
  `reason` varchar(1000) DEFAULT NULL COMMENT '变更风险级别的原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

CREATE TABLE `impl_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` varchar(255) DEFAULT NULL COMMENT '实施人id',
  `changeTaskNo` varchar(255) DEFAULT NULL COMMENT '变更任务单单号',
  `taskStatus` varchar(255) DEFAULT NULL COMMENT '变更任务单状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 机构紧急额度配置表  SQL已在测试环境执行  2022-10-11

-- itsm.change_dept_person definition
CREATE TABLE `change_dept_person` (
  `id` varchar(100) NOT NULL,
  `dept_id` varchar(100) DEFAULT NULL COMMENT '机构id',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '机构名称',
  `dept_person` varchar(100) DEFAULT NULL COMMENT '机构负责人',
  `dept_leader_person` varchar(100) DEFAULT NULL COMMENT '机构分管领导',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 紧急额度配置表添加字段信息
ALTER TABLE change_dept_person ADD user_account varchar(100);
ALTER TABLE change_dept_person ADD user_id varchar(100);

-- 机构紧急额度配置表  SQL已在测试环境执行  2022-10-11
CREATE TABLE `change_dept_system` (
`id` varchar(100) NOT NULL COMMENT '主键id',
`dept_id` varchar(100) NOT NULL COMMENT '机构id',
`init_size` varchar(100) DEFAULT NULL COMMENT '默认额度',
`over_size` varchar(100) DEFAULT NULL COMMENT '剩余额度',
`replace_time` varchar(100) DEFAULT NULL COMMENT '重置时间',
`remarks` varchar(500) DEFAULT NULL COMMENT '备注',
`start_status` varchar(100) DEFAULT NULL COMMENT '状态',
`dept_name` varchar(100) DEFAULT NULL COMMENT '机构名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `og_review_changing_date_config` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `day_of_week` varchar(16) DEFAULT NULL COMMENT '评审日(MONDAY-SUNDAY字符串)',
  `ahead_of_hours` int(2) DEFAULT NULL COMMENT '提前小时数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE table it_sh_event_duty(
`dutyid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
`group_code` varchar(100) NOT NULL COMMENT appli'值班组编号',
`nickname` varchar(100) DEFAULT NULL COMMENT '值班人',
`position` varchar(100) DEFAULT NULL COMMENT '值班职责 0组长、1组员',
`kind` int(5) DEFAULT NULL COMMENT '值班类别 0白班、1晚班',
`phone` varchar(25) DEFAULT NULL COMMENT '值班电话',
`duty_time` varchar(30) DEFAULT NULL COMMENT '值班时间,yyyy-MM-dd',
`remark` varchar(300) DEFAULT NULL COMMENT '值班说明',
`status` int(5) DEFAULT NULL COMMENT '审批状态,0:未通过,1:已通过',
`group_name` varchar(25) DEFAULT NULL COMMENT '值班组',
`user_no` varchar(32) DEFAULT NULL COMMENT '值班人工号',
`userno` varchar(32) DEFAULT NULL COMMENT '更信任工号',
`updatetime` date DEFAULT NULL COMMENT '更新时间',
`id` varchar(100) NOT NULL COMMENT '值班编号',
CONSTRAINT pk_it_sh_event_duty PRIMARY KEY(dutyid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `og_deputy_cfg`;
CREATE TABLE `og_deputy_cfg` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cfg_type` int DEFAULT '0' COMMENT '配置来源类型',
  `director` varchar(64) DEFAULT '' COMMENT '负责人',
  `secondary` varchar(64) DEFAULT '' COMMENT '代理人',
  `status` char(1) DEFAULT '0' COMMENT '状态',
  `START_time` datetime DEFAULT NULL COMMENT '开始时间',
  `END_time` datetime DEFAULT NULL COMMENT '结束时间',
  `created_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='代理人配置';
CREATE TABLE  it_entegor_data(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
itsmno varchar(64) DEFAULT NULL COMMENT '任务单号',
status int DEFAULT NULL COMMENT '状态',
`start_time` datetime DEFAULT NULL COMMENT '开始时间',
`end_time` datetime DEFAULT NULL COMMENT '结束时间',
params text DEFAULT NULL COMMENT '传递的参数',
result text DEFAULT NULL COMMENT '返回的结果',
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='服务目录自动化数据表';
CREATE TABLE  it_tables_data(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
`service_tablename` varchar(128) DEFAULT NULL COMMENT '任务对应表名',
`colname` varchar(64) DEFAULT NULL COMMENT '列名称',
`coltype` varchar(32) DEFAULT NULL COMMENT '列类型',
`collength` int DEFAULT NULL COMMENT '列长度',
`colcode` varchar(64) DEFAULT NULL COMMENT '列标识',
`orders` int DEFAULT NULL COMMENT '顺序',
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='个性化数据配置表';
CREATE TABLE  it_task_spv_data(
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
`itsmno` varchar(64) DEFAULT NULL COMMENT '任务单号',
`tables_id` bigint DEFAULT NULL COMMENT 'it_tables_data主键',
`app_ip` varchar(64) DEFAULT NULL COMMENT '申请IP',
`app_uer` varchar(64) DEFAULT NULL COMMENT '申请用户',
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='个性化数据表';