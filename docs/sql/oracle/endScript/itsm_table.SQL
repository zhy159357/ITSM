create table OG_GROUP
(
    groupid           VARCHAR2(32) not null,
    grpname           VARCHAR2(64) not null,
    grpleader         VARCHAR2(16),
    adder             VARCHAR2(16),
    addtime           VARCHAR2(32),
    moder             VARCHAR2(16),
    modtime           VARCHAR2(32),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    memo              VARCHAR2(512),
    orgid             VARCHAR2(36),
    count             VARCHAR2(8),
    tel               VARCHAR2(64),
    sysid             VARCHAR2(2048),
    white_list        VARCHAR2(2),
    group_type        VARCHAR(2),
    constraint pk_OG_GROUP  primary key (GROUPID)
);

create index OG_GOP_IDX_ORGID_FK on OG_GROUP (ORGID);
create table OG_GROUP_PERSON
(
    groupid   VARCHAR2(32) not null,
    pid       VARCHAR2(32) not null,
    gpid      VARCHAR2(32) not null,
    gposition VARCHAR2(32),
    gp_order  NUMBER(8),
    constraint pk_OG_GROUP_PERSON  primary key (GPID)
);

create index OG_GOP_P_IDX_GROUPID_FK on OG_GROUP_PERSON (GROUPID);
create index OG_GOP_P_IDX_PID_FK on OG_GROUP_PERSON (PID);
create table OG_GROUP_TYPEINFO
(
    gt_id       VARCHAR2(32) not null,
    groupid     VARCHAR2(32) not null,
    typeinfo_id VARCHAR2(32) not null,
    constraint pk_OG_GROUP_TYPEINFO  primary key (GT_ID)
) ;
create table OG_MODULE
(
    mid         VARCHAR2(32) not null,
    module_no   VARCHAR2(32),
    modulename  VARCHAR2(64),
    modulekind  VARCHAR2(2),
    supermodule VARCHAR2(32),
    script      VARCHAR2(512),
    adder       VARCHAR2(32),
    addtime     VARCHAR2(32),
    moder       VARCHAR2(32),
    modtime     VARCHAR2(32),
    memo        VARCHAR2(512),
    moduleorder NUMBER(8),
    leaf        VARCHAR2(1),
    style       VARCHAR2(64),
    constraint pk_OG_MODULE  primary key (MID)
);

create index OG_MOD_IDX_MODULE_NO on OG_MODULE (MODULE_NO);
create table OG_ORG
(
    orgid             VARCHAR2(32) not null,
    t_o_orgid         VARCHAR2(32),
    orgcode           VARCHAR2(64),
    orgname           VARCHAR2(64),
    orglv             VARCHAR2(3),
    adder             VARCHAR2(16),
    addtime           VARCHAR2(32),
    moder             VARCHAR2(16),
    modtime           VARCHAR2(32),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    memo              VARCHAR2(512),
    branch_mark       VARCHAR2(8),
    inoutside_mark    VARCHAR2(8),
    level_code        VARCHAR2(512),
    leaf              VARCHAR2(1),
    type              VARCHAR2(8),
    sort              VARCHAR2(1),
    operaternum       VARCHAR2(6),
    khnum             VARCHAR2(4),
    constraint pk_OG_ORG  primary key (ORGID)
);

comment on column OG_ORG.level_code
  is '层级码';
comment on column OG_ORG.leaf
  is '是否叶子节点';
comment on column OG_ORG.khnum
  is '考核厂商序号';


create index OG_ORG_IDX_T_O_ORGID on OG_ORG (T_O_ORGID);
create table OG_OFFICE
(
    of_id             VARCHAR2(36) not null,
    orgid             VARCHAR2(36),
    caption           VARCHAR2(64),
    memo              VARCHAR2(512),
    adder             VARCHAR2(16),
    addtime           VARCHAR2(20),
    moder             VARCHAR2(16),
    update_time       VARCHAR2(20),
    invalidation_mark VARCHAR2(2),
    constraint pk_OG_OFFICE  primary key (OF_ID)
);

comment on table OG_OFFICE
  is '860111处室';

create index ORGID_FK4 on OG_OFFICE (ORGID);

create table OG_PERSON
(
    pid               VARCHAR2(32) not null,
    orgid             VARCHAR2(32),
    pname             VARCHAR2(36),
    sex               VARCHAR2(3),
    edu               VARCHAR2(3),
    birthday          VARCHAR2(32),
    birthplace        VARCHAR2(64),
    phone             VARCHAR2(32),
    mobil_phone       VARCHAR2(32),
    email             VARCHAR2(64),
    address           VARCHAR2(64),
    adder             VARCHAR2(32),
    addtime           VARCHAR2(32),
    moder             VARCHAR2(32),
    memo              VARCHAR2(512),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    position          VARCHAR2(32),
    leader            VARCHAR2(2),
    p_order           NUMBER(8),
    p_flag            VARCHAR2(2),
    constraint pk_OG_PERSON primary key (PID)
);
create index OG_PSN_IDX_ORGID_FK on OG_PERSON (ORGID);
create table OG_OFFICE_PERSON
(
    op_id     VARCHAR2(36) not null,
    pid       VARCHAR2(36),
    of_id     VARCHAR2(36),
    op_leader VARCHAR2(1),
    op_order  VARCHAR2(4),
    constraint pk_OG_OFFICE_PERSON  primary key (OP_ID)
);

create index OF_ID_FK on OG_OFFICE_PERSON (OF_ID);
create index PID_FK3 on OG_OFFICE_PERSON (PID);

create table OG_POST
(
    postid            VARCHAR2(32) not null,
    post_name         VARCHAR2(64),
    adder             VARCHAR2(32),
    addtime           VARCHAR2(32),
    moder             VARCHAR2(32),
    update_time       VARCHAR2(32),
    memo              VARCHAR2(512),
    invalidation_mark VARCHAR2(2),
    serial_num        VARCHAR2(20),
    power_flag        VARCHAR2(2),
    constraint pk_OG_POST primary key (POSTID)
);

comment on column OG_POST.serial_num
  is '序号';



create table OG_ROLE
(
    rid               VARCHAR2(32) not null,
    rname             VARCHAR2(64),
    adder             VARCHAR2(16),
    addtime           VARCHAR2(32),
    moder             VARCHAR2(16),
    modtime           VARCHAR2(32),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    memo              VARCHAR2(512),
    funclist          VARCHAR2(1024),
    constraint PK_OG_ROLE  primary key (RID)
);
create table OG_R_POST_ROLE
(
    postid VARCHAR2(32) not null,
    rid    VARCHAR2(32) not null,
    constraint PK_OG_R_POST_ROLE  primary key (POSTID, RID)
);
create table OG_R_PYW_RYWCZ
(
    ywid     VARCHAR2(32) not null,
    cz_name  VARCHAR2(64) not null,
    cz_phone VARCHAR2(32) not null
);
create table OG_R_ROLE_MODULE
(
    mid VARCHAR2(32) not null,
    rid VARCHAR2(32) not null
);

create table OG_R_USER_POST
(
    postid VARCHAR2(32) not null,
    userid VARCHAR2(32) not null,
    constraint PK_OOG_R_USER_POST primary key (POSTID, USERID)
);
create table OG_R_USER_POST1
(
    postid VARCHAR2(32) not null,
    userid VARCHAR2(32) not null,
    constraint PK_OG_R_USER_POST1 primary key (POSTID, USERID)
);
create table OG_R_USER_ROLE
(
    rid    VARCHAR2(32) not null,
    userid VARCHAR2(32) not null
);


create table OG_SYS
(
    sysid             VARCHAR2(36) not null,
    orgid             VARCHAR2(36),
    code              VARCHAR2(32),
    caption           VARCHAR2(64),
    memo              VARCHAR2(512),
    adder             VARCHAR2(16),
    addtime           VARCHAR2(20),
    moder             VARCHAR2(16),
    update_time       VARCHAR2(20),
    invalidation_mark VARCHAR2(2),
    isexamination     VARCHAR2(1),
    dept              VARCHAR2(2),
    pid               VARCHAR2(32),
    businessid        VARCHAR2(32),
    is_key_sys        VARCHAR2(2),
    sys_type          VARCHAR2(2),
    upgrade_time      VARCHAR2(20),
    is_out_channel    VARCHAR2(255),
    system_stage      VARCHAR2(255),
    frontline_date      VARCHAR2(512),
    secondline_date      VARCHAR2(512),
    app_dutyman      VARCHAR2(128),
    app_sub_dept      VARCHAR2(128),
    constraint PK_OG_SYS primary key (SYSID)
);


comment on column OG_SYS.pid
  is '系统负责人';
comment on column OG_SYS.businessid
  is '所属业务部门';
comment on column OG_SYS.is_key_sys
  is '是否重要系统';



create index ORGID_FK3 on OG_SYS (ORGID);

create table OG_SYS1
(
    sysid             VARCHAR2(36) not null,
    orgid             VARCHAR2(36),
    code              VARCHAR2(32),
    caption           VARCHAR2(64),
    memo              VARCHAR2(512),
    adder             VARCHAR2(16),
    addtime           VARCHAR2(20),
    moder             VARCHAR2(16),
    update_time       VARCHAR2(20),
    invalidation_mark VARCHAR2(2),
    isexamination     VARCHAR2(1)
);

create table OG_TYPEINFO
(
    typeinfo_id       VARCHAR2(36) not null,
    type_type_no      VARCHAR2(32),
    type_type_name    VARCHAR2(128),
    type_no           VARCHAR2(32),
    type_name         VARCHAR2(128),
    type_description  VARCHAR2(512),
    parent_id         VARCHAR2(36),
    create_user_id    VARCHAR2(36),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    serial            NUMBER(10),
    type_level        NUMBER(10),
    leaf              VARCHAR2(1),
    constraint PK_OG_TYPEINFO primary key (TYPEINFO_ID)
);

create table OG_USER
(
    userid            VARCHAR2(32) not null,
    pid               VARCHAR2(32),
    username          VARCHAR2(32),
    password          VARCHAR2(32),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    ip_start          VARCHAR2(32),
    ip_end            VARCHAR2(32),
    sms_code          VARCHAR2(32),
    last_time         NUMBER,
    l_count           VARCHAR2(2),
    p_not_count       VARCHAR2(2),
    lock_time         VARCHAR2(20),
    weak_pwd          VARCHAR2(2),
    constraint PK_T_OG_USER primary key (USERID)
);

comment on column OG_USER.sms_code
  is '短信验证码';
comment on column OG_USER.last_time
  is '验证码发送时间';
comment on column OG_USER.l_count
  is '短信验证码错误次数';
comment on column OG_USER.p_not_count
  is '密码错误次数';
comment on column OG_USER.lock_time
  is '密码锁定时间';
comment on column OG_USER.weak_pwd
  is '密码强弱校验  0-弱密码  1-强密码';

create index OG_USER_IDX_USERNAME on OG_USER (USERNAME);
create table PUB_ATTACHMENT
(
    at_id            VARCHAR2(36) not null,
    create_id        VARCHAR2(36),
    owner_id         VARCHAR2(36),
    memo             VARCHAR2(64),
    file_path        VARCHAR2(512),
    file_name        VARCHAR2(128),
    file_time        VARCHAR2(20),
    flag             VARCHAR2(32),
    size_            VARCHAR2(64),
    file_status      VARCHAR2(1),
    file_ciphertext  VARCHAR2(2048),
    file_retry_count INT,
    file_type  VARCHAR2(2)  ,
    group_name  VARCHAR2(32)  ,
    change_reason  VARCHAR2(1000)  ,
    automate_status  VARCHAR2(2)  ,
    automate_result_msg  VARCHAR2(512)  ,
    sys_name  VARCHAR2(512)  ,
    md5  VARCHAR2(64)  ,
    constraint PK_PUB_ATTACHMENT primary key (AT_ID)
);

comment on table PUB_ATTACHMENT is '860203通用附件';
comment on column PUB_ATTACHMENT.file_path is '文件路径';
comment on column PUB_ATTACHMENT.file_name is '文件名';
comment on column PUB_ATTACHMENT.file_time is '上传时间';
comment on column PUB_ATTACHMENT.file_status is '文件状态';
comment on column PUB_ATTACHMENT.file_ciphertext is '密文描述';
comment on column PUB_ATTACHMENT.file_retry_count is '重试次数';
COMMENT on column PUB_ATTACHMENT.file_type is '附件类型 1-普通附件  2-版本包  3-自动化步骤';
COMMENT on column PUB_ATTACHMENT.group_name is '文件上传fast服务器分配的组名称';
COMMENT on column PUB_ATTACHMENT.change_reason is '变更原因说明(自动化对接接口模版校验时专用)'
COMMENT on column PUB_ATTACHMENT.automate_status is '自动化状态 1-未启动  2-启动中  3-启动成功  4-启动失败';
COMMENT on column PUB_ATTACHMENT.automate_result_msg is '自动化返回结果信息';
COMMENT on column PUB_ATTACHMENT.sys_name is '自动化返回系统名称';
COMMENT on column PUB_ATTACHMENT.md5 is 'md5';

create index PUB_ATTACHMENT_CREATE_ID_FK on PUB_ATTACHMENT (CREATE_ID);
create index PUB_ATT_IDX_OWNER_ID on PUB_ATTACHMENT (OWNER_ID);

create table PUB_BIZ_PRESMS
(
    pub_biz_presms_id VARCHAR2(36) not null,
    sms_type          VARCHAR2(2),
    telephone         VARCHAR2(32),
    name              VARCHAR2(32),
    sms_text          VARCHAR2(512),
    inspect_time      VARCHAR2(32),
    inspect_object    VARCHAR2(32),
    inspect_object_id VARCHAR2(36),
    inspect_text      VARCHAR2(32),
    creater_id        VARCHAR2(36),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    sms_state         CHAR(1),
    task_id           VARCHAR2(512),
    verificationcode  VARCHAR2(6),
    deal_status       VARCHAR2(2),
    constraint PK_PUB_BIZ_PRESMS primary key (PUB_BIZ_PRESMS_ID)
);


comment on column PUB_BIZ_PRESMS.pub_biz_presms_id
  is 'ID';
comment on column PUB_BIZ_PRESMS.sms_type
  is '短信类型';
comment on column PUB_BIZ_PRESMS.telephone
  is '电话';
comment on column PUB_BIZ_PRESMS.name
  is '姓名';
comment on column PUB_BIZ_PRESMS.sms_text
  is '短信内容';
comment on column PUB_BIZ_PRESMS.inspect_time
  is '检查时间';
comment on column PUB_BIZ_PRESMS.inspect_object
  is '检查对象';
comment on column PUB_BIZ_PRESMS.inspect_object_id
  is '检查对象ID';
comment on column PUB_BIZ_PRESMS.inspect_text
  is '检查内容';
comment on column PUB_BIZ_PRESMS.creater_id
  is '创建人';
comment on column PUB_BIZ_PRESMS.update_time
  is '更新时间';
comment on column PUB_BIZ_PRESMS.invalidation_mark
  is '是否有效';
comment on column PUB_BIZ_PRESMS.sms_state
  is '0 - 未发送 1 - 已发送';
comment on column PUB_BIZ_PRESMS.deal_status
  is '0 - 未处理 1 - 短信处理 2 - 页面处理';




create table PUB_BIZ_SMS
(
    pub_biz_sms_id    VARCHAR2(36) not null,
    pub_biz_presms_id VARCHAR2(36),
    serial_number     INT,
    telephone         VARCHAR2(32),
    name              VARCHAR2(32),
    sms_text          VARCHAR2(512),
    sms_time          VARCHAR2(32),
    sms_state         VARCHAR2(2),
    creater_id        VARCHAR2(36),
    update_time       VARCHAR2(32),
    invalidation_mark VARCHAR2(2),
    constraint PK_PUB_BIZ_SMS primary key (PUB_BIZ_SMS_ID)
);
comment on column PUB_BIZ_SMS.pub_biz_presms_id
  is '暂存表ID';
comment on column PUB_BIZ_SMS.serial_number
  is '序号';
comment on column PUB_BIZ_SMS.telephone
  is '电话';
comment on column PUB_BIZ_SMS.name
  is '姓名';
comment on column PUB_BIZ_SMS.sms_text
  is '短信内容';
comment on column PUB_BIZ_SMS.sms_time
  is '发送时间';
comment on column PUB_BIZ_SMS.sms_state
  is '状态';
comment on column PUB_BIZ_SMS.creater_id
  is '创建人';
comment on column PUB_BIZ_SMS.update_time
  is '更新时间';
comment on column PUB_BIZ_SMS.invalidation_mark
  is '是否有效';

create table PUB_BIZ_SMS_RECEIVE
(
    id               VARCHAR2(36) not null,
    telephone        VARCHAR2(36),
    sms_text         VARCHAR2(512),
    send_time        VARCHAR2(32),
    verificationcode VARCHAR2(32),
    sms_result       VARCHAR2(32),
    constraint PK_PUB_BIZ_SMS_RECEIVE primary key (ID)
);

comment on column PUB_BIZ_SMS_RECEIVE.telephone
  is '发送手机号';
comment on column PUB_BIZ_SMS_RECEIVE.sms_text
  is '发送短信内容';
comment on column PUB_BIZ_SMS_RECEIVE.send_time
  is '发送时间';
comment on column PUB_BIZ_SMS_RECEIVE.verificationcode
  is '单号';
comment on column PUB_BIZ_SMS_RECEIVE.sms_result
  is '处理结果';


create table PUB_FOLDER_INDEX
(
    id_               VARCHAR2(36) not null,
    name_             VARCHAR2(128),
    path_             VARCHAR2(1024),
    parent_           VARCHAR2(36),
    description_      VARCHAR2(1024),
    create_user_      VARCHAR2(32),
    manager_user_     VARCHAR2(32),
    create_time_      VARCHAR2(20),
    modify_user_      VARCHAR2(32),
    modify_time_      VARCHAR2(20),
    update_time_      VARCHAR2(20),
    leaf_             VARCHAR2(20),
    orgs_             CLOB,
    invalidation_mark VARCHAR2(2),
    constraint PK_PUB_FOLDER_INDEX primary key (ID_)
);

create table PUB_FILE_INDEX
(
    id_               VARCHAR2(36) not null,
    folder_           VARCHAR2(36),
    file_name_        VARCHAR2(128),
    size_             VARCHAR2(32),
    description_      VARCHAR2(1024),
    create_user_      VARCHAR2(32),
    create_time_      VARCHAR2(20),
    modify_user_      VARCHAR2(32),
    modify_time_      VARCHAR2(20),
    file_path_        VARCHAR2(512),
    invalidation_mark VARCHAR2(2),
    constraint PK_PUB_FILE_INDEX primary key (ID_)
);

create table PUB_FLOW_LOG
(
    log_id               VARCHAR2(36) not null,
    biz_id               VARCHAR2(36),
    log_type             VARCHAR2(20),
    task_name            VARCHAR2(64),
    performer_id         VARCHAR2(36),
    performer_group_id   VARCHAR2(36),
    performer_org_id     VARCHAR2(36),
    performer_name       VARCHAR2(32),
    performer_tel        VARCHAR2(20),
    performer_group_name VARCHAR2(64),
    performer_org_name   VARCHAR2(64),
    performer_time       VARCHAR2(20),
    perform_name         VARCHAR2(64),
    perform_desc         VARCHAR2(2000),
    next_task_id         VARCHAR2(512),
    next_task_name       VARCHAR2(512),
    next_performer_desc  VARCHAR2(128),
    next_performer_tel   VARCHAR2(64),
    sys_residence_time   VARCHAR2(20),
    current_state        VARCHAR2(32),
    serial_num           VARCHAR2(4),
    constraint PK_PUB_FLOW_LOG primary key (LOG_ID)
);


comment on table PUB_FLOW_LOG
  is '流程日志';
comment on column PUB_FLOW_LOG.log_id
  is '事件单日志id';
comment on column PUB_FLOW_LOG.biz_id
  is '事件单id';
comment on column PUB_FLOW_LOG.task_name
  is '环节名称';
comment on column PUB_FLOW_LOG.performer_time
  is '操作时间';
comment on column PUB_FLOW_LOG.perform_desc
  is '操作描述';
comment on column PUB_FLOW_LOG.next_task_name
  is '下一步环节名称';
comment on column PUB_FLOW_LOG.sys_residence_time
  is '系统停留时间';
comment on column PUB_FLOW_LOG.serial_num
  is '序号';



create index PUB_FLOW_L_IDX_BIZ_ID on PUB_FLOW_LOG (BIZ_ID);
create table PUB_HOLIDAY
(
    holiday_id  VARCHAR2(36) not null,
    scope       VARCHAR2(2),
    day         VARCHAR2(20),
    name        VARCHAR2(100),
    type        VARCHAR2(2),
    memo        VARCHAR2(512),
    agency_id   CLOB,
    agency_name CLOB,
    constraint PK_PUB_HOLIDAY primary key (HOLIDAY_ID)
);

comment on table PUB_HOLIDAY
  is '节假日配置';
comment on column PUB_HOLIDAY.holiday_id
  is '主键ID';
comment on column PUB_HOLIDAY.scope
  is '使用范围';
comment on column PUB_HOLIDAY.day
  is '日期';
comment on column PUB_HOLIDAY.name
  is '节假日名称';
comment on column PUB_HOLIDAY.type
  is '类别';
comment on column PUB_HOLIDAY.memo
  is '备注';
comment on column PUB_HOLIDAY.agency_id
  is '使用机构id';
comment on column PUB_HOLIDAY.agency_name
  is '使用机构名称';
create table PUB_NO_SEQ
(
    seq_type VARCHAR2(16) not null,
    seq_date VARCHAR2(8) not null,
    seq_no   VARCHAR2(16) not null
);

create table PUB_PARA
(
    para_id      VARCHAR2(36) not null,
    para_name    VARCHAR2(64),
    para_explain VARCHAR2(64),
    state        VARCHAR2(4),
    update_flag  VARCHAR2(2),
    constraint PUB_PARA  primary key (PARA_ID)
);


create table PUB_PARAVALUE
(
    para_value_id VARCHAR2(36) not null,
    para_id       VARCHAR2(36),
    value         VARCHAR2(64),
    value_detail  VARCHAR2(64),
    serail        NUMBER(8),
    constraint PK_PUB_PARAVALUE  primary key (PARA_VALUE_ID)
);

create index PUB_PV_IDX_PARA_ID_FK on PUB_PARAVALUE (PARA_ID);

create table PUB_RELATION
(
    relation_id VARCHAR2(36) not null,
    obj1_id     VARCHAR2(36),
    obj2_id     VARCHAR2(36),
    type        VARCHAR2(2),
    hangup_task VARCHAR2(200),
    constraint PK_PUB_RELATION primary key (RELATION_ID)
);

comment on table PUB_RELATION
  is '事件/问题/变更关联关系';
comment on column PUB_RELATION.relation_id
  is '主键ID';
comment on column PUB_RELATION.obj1_id
  is '对象1ID';
comment on column PUB_RELATION.obj2_id
  is '对象2ID';
comment on column PUB_RELATION.type
  is '关系分类：01事件-问题，02事件-数据变更，03业务事件-综合变更，04问题-综合变更，05问题-版本，06计划（任务）-公告，07事务事件-综合变更，08监控事件-综合变更，09故障事件-综合变更，10调度事件-综合变更,11计划-综合变更单；注：根据上述五种对用关系，对象1和对象2正向排列，方便查询';
comment on column PUB_RELATION.hangup_task
  is '挂起任务，用于标记业务事件单转数据变更时，挂起的任务';



create index PUB_R_IDX_OBJ1_ID on PUB_RELATION (OBJ1_ID);
create index PUB_R_IDX_OBJ2_ID on PUB_RELATION (OBJ2_ID);



create table TEL_TERMINAL
(
    id          VARCHAR2(32) not null,
    telip       VARCHAR2(32) not null,
    service_ip  VARCHAR2(32) not null,
    extno       VARCHAR2(16) not null,
    job_number  VARCHAR2(32) not null,
    create_id   VARCHAR2(36) not null,
    create_time VARCHAR2(32) not null,
    constraint pk_TEL_TERMINAL primary key (id)
);

comment on column TEL_TERMINAL.id
  is 'ID';
comment on column TEL_TERMINAL.telip
  is '话机IP';
comment on column TEL_TERMINAL.service_ip
  is '服务IP';
comment on column TEL_TERMINAL.extno
  is '分机号';
comment on column TEL_TERMINAL.job_number
  is '工号';
comment on column TEL_TERMINAL.create_id
  is '创建人';
comment on column TEL_TERMINAL.create_time
  is '创建时间';


create table VM_BIZ_INFO
(
    version_info_id                VARCHAR2(36) not null,
    version_info_no                VARCHAR2(32) not null,
    version_info_name              VARCHAR2(128),
    version_producer_id            VARCHAR2(32),
    version_create_time            VARCHAR2(32),
    sysid                          VARCHAR2(32),
    version_name                   VARCHAR2(128),
    version_no                     VARCHAR2(32) ,
    version_description            VARCHAR2(2048),
    version_upgrade_content        VARCHAR2(512),
    business_change_description    VARCHAR2(512),
    upgrade_environment            VARCHAR2(512),
    notice_text                    VARCHAR2(4000),
    version_status                 VARCHAR2(2),
    technical_support_id           VARCHAR2(168),
    technical_support_phone        VARCHAR2(64),
    update_time                    VARCHAR2(32),
    version_attr                   VARCHAR2(32),
    test_report                    VARCHAR2(32),
    question_id                    VARCHAR2(512),
    send_msg_flag                  VARCHAR2(32),
    version_type                   VARCHAR2(128),
    if_safety_audit                VARCHAR2(2),
    issued_unit                    VARCHAR2(2048),
    start_upgrade_time             VARCHAR2(20),
    end_upgrade_time               VARCHAR2(20),
    accept_organization            VARCHAR2(1000),
    accept_role                    VARCHAR2(2),
    createunits                    VARCHAR2(512),
    copy_requite_unit              VARCHAR2(512),
    business_state                 VARCHAR2(2),
    technology_state               VARCHAR2(2),
    vm_pro_actlog_id               VARCHAR2(32),
    drafter_id                     VARCHAR2(512),
    issued_no                      VARCHAR2(32),
    safety_audit_id                VARCHAR2(32),
    technology_audit_id            VARCHAR2(32),
    business_audit_id              VARCHAR2(2048),
    business_approval_id           VARCHAR2(2048),
    technology_approval_id         VARCHAR2(32),
    use_approval_id                VARCHAR2(32),
    uploader_approval_id           VARCHAR2(32),
    if_issue_notice                VARCHAR2(2),
    issued_branch                  VARCHAR2(2000),
    agency_center                  VARCHAR2(1024),
    invalidation_mark              VARCHAR2(2),
    agency_zx                      VARCHAR2(32),
    business_requirement_text      VARCHAR2(4000),
    business_validation_text       VARCHAR2(4000),
    if_report_office               VARCHAR2(2),
    if_report_cbrc                 VARCHAR2(2),
    if_report_call_center          VARCHAR2(2),
    if_stop_business               VARCHAR2(2),
    sys_name                       CLOB,
    sys_id                         CLOB,
    use_division_chief_approval_id VARCHAR2(32),
    jjsp_approval_id               VARCHAR2(32),
    business_org                   VARCHAR2(256),
    is_automate                    VARCHAR2(2) ,
    automate_audit_id              VARCHAR2(32),
    constraint pk_VM_BIZ_INFO primary key (VERSION_INFO_ID)
);
comment on column VM_BIZ_INFO.version_info_id
  is '版本信息单id';
comment on column VM_BIZ_INFO.version_info_no
  is '版本信息单编号';
comment on column VM_BIZ_INFO.version_info_name
  is '版本信息单名称';
comment on column VM_BIZ_INFO.version_producer_id
  is '版本制作人';
comment on column VM_BIZ_INFO.version_create_time
  is '版本创建时间';
comment on column VM_BIZ_INFO.sysid
  is '所属应用系统';
comment on column VM_BIZ_INFO.version_name
  is '升级内容摘要';
comment on column VM_BIZ_INFO.version_description
  is '版本描述';
comment on column VM_BIZ_INFO.version_upgrade_content
  is '版本升级内容';
comment on column VM_BIZ_INFO.business_change_description
  is '业务变更说明';
comment on column VM_BIZ_INFO.upgrade_environment
  is '升级环境';
comment on column VM_BIZ_INFO.notice_text
  is '版本变更内容';
comment on column VM_BIZ_INFO.version_status
  is '版本状态';
comment on column VM_BIZ_INFO.safety_audit_id
  is '安全审核人';
comment on column VM_BIZ_INFO.technology_audit_id
  is '技术审核人';
comment on column VM_BIZ_INFO.business_audit_id
  is '业务审核人';
comment on column VM_BIZ_INFO.business_approval_id
  is '业务主管';
comment on column VM_BIZ_INFO.technology_approval_id
  is '技术审批人';
comment on column VM_BIZ_INFO.use_approval_id
  is '应用审批人';
comment on column VM_BIZ_INFO.uploader_approval_id
  is '发布审批人';
comment on column VM_BIZ_INFO.if_issue_notice
  is '是否发布公告';
comment on column VM_BIZ_INFO.business_requirement_text
  is '业务需求';
comment on column VM_BIZ_INFO.business_validation_text
  is '测试及业务验证';
comment on column VM_BIZ_INFO.if_report_office
  is '是否通报办公室,1/是，2/否';
comment on column VM_BIZ_INFO.if_report_cbrc
  is '是否报备银保监会,1/是，2/否';
comment on column VM_BIZ_INFO.if_report_call_center
  is '是否通报客服中心,1/是，2/否';
comment on column VM_BIZ_INFO.if_stop_business
  is '是否需停业升级,1/是，2/否';
comment on column VM_BIZ_INFO.sys_name
  is '所涉应用系统名称';
comment on column VM_BIZ_INFO.sys_id
  is '所涉应用系统ID';
comment on column VM_BIZ_INFO.use_division_chief_approval_id
  is '应用处长审批人';
comment on column VM_BIZ_INFO.jjsp_approval_id
  is '紧急审批人(徐总)';
comment on column VM_BIZ_INFO.business_org
  is '业务部门';

create index VM_B_I_IDX_CREAT_TIME on VM_BIZ_INFO (VERSION_CREATE_TIME);
create index VM_B_I_IDX_PRODUCER_ID on VM_BIZ_INFO (VERSION_PRODUCER_ID);
create index VM_B_I_IDX_STAUTS on VM_BIZ_INFO (VERSION_STATUS);
create index VM_B_I_IDX_VERSION_NO on VM_BIZ_INFO (VERSION_INFO_NO);

create table VM_BIZ_TASKINFO
(
    task_id            VARCHAR2(32) not null,
    version_info_id    VARCHAR2(32) not null,
    task_name          VARCHAR2(128) not null,
    task_type_id       VARCHAR2(32),
    task_status        VARCHAR2(10),
    task_content       VARCHAR2(2048),
    task_deal_group_id VARCHAR2(32),
    task_deal_user_id  VARCHAR2(32),
    front_task_id      VARCHAR2(32),
    follow_task_id     VARCHAR2(32),
    est_start_date     VARCHAR2(32),
    est_finish_date    VARCHAR2(32),
    task_deal_result   VARCHAR2(512),
    actual_start_time  VARCHAR2(32),
    actual_finish_time VARCHAR2(32),
    update_time        VARCHAR2(32),
    invalidation_mark  VARCHAR2(32),
    taskproducetime    VARCHAR2(32),
    org                VARCHAR2(32),
    task_no            VARCHAR2(32),
    user_id            VARCHAR2(1024),
    man_phone          VARCHAR2(2048),
    message_flag       VARCHAR2(2),
    complete_user_id   VARCHAR2(32),
    constraint pk_VM_BIZ_TASKINFO primary key (TASK_ID)
);
comment on column VM_BIZ_TASKINFO.task_id
  is '任务序号ID';
comment on column VM_BIZ_TASKINFO.version_info_id
  is '版本申请单ID';
comment on column VM_BIZ_TASKINFO.task_name
  is '任务名称';
comment on column VM_BIZ_TASKINFO.task_type_id
  is '任务类型';
comment on column VM_BIZ_TASKINFO.task_status
  is '任务状态';
comment on column VM_BIZ_TASKINFO.task_content
  is '任务内容';
comment on column VM_BIZ_TASKINFO.task_deal_group_id
  is '任务处理组';
comment on column VM_BIZ_TASKINFO.task_deal_user_id
  is '任务处理人';
comment on column VM_BIZ_TASKINFO.front_task_id
  is '前置任务序号';
comment on column VM_BIZ_TASKINFO.follow_task_id
  is '后续任务序号';
comment on column VM_BIZ_TASKINFO.est_start_date
  is '预计开始时间';
comment on column VM_BIZ_TASKINFO.est_finish_date
  is '预计完成时间';
comment on column VM_BIZ_TASKINFO.task_deal_result
  is '任务处理结果';
comment on column VM_BIZ_TASKINFO.actual_start_time
  is '实际开始时间';
comment on column VM_BIZ_TASKINFO.actual_finish_time
  is '实际完成时间';
comment on column VM_BIZ_TASKINFO.update_time
  is '最近修改时间';
comment on column VM_BIZ_TASKINFO.invalidation_mark
  is '无效标记';
comment on column VM_BIZ_TASKINFO.taskproducetime
  is '任务处理时间';
comment on column VM_BIZ_TASKINFO.org
  is '机构代码';
comment on column VM_BIZ_TASKINFO.task_no
  is '任务编号';
comment on column VM_BIZ_TASKINFO.message_flag
  is '短信回复标志 0未回复 1已回复';
/*
客户信息表 llf 2022/1/5
*/
create table cust_info(
      c_id          VARCHAR2(32)    primary key not null,
      c_name        VARCHAR2(48)    default null,
      c_phone       VARCHAR2(12)    default null,
      c_dept        VARCHAR2(50)    default null,
      c_post        VARCHAR2(50)    default null,
      c_address     VARCHAR2(200)    default null,
      create_time 	date,
      c_column      VARCHAR2(50)    default null
);
comment on table  cust_info              is '客户信息表';
comment on column cust_info.c_name
  is '客户名称';
comment on column cust_info.c_phone
  is '客户电话号码';
comment on column cust_info.c_dept
  is '客户部门';
comment on column cust_info.c_post
  is '客户职务';
comment on column cust_info.c_address
  is '客户地址';
comment on column cust_info.create_time
  is '创建时间';
comment on column cust_info.c_column
  is '扩展字段';
/*
事件表
*/
CREATE TABLE FM_BIZ (
  FM_ID VARCHAR2(36) primary key not null ,
  CREATE_ID VARCHAR2(36)  default null,
  CREATE_ORG_ID VARCHAR2(36)  default null,
  GROUP_ID VARCHAR2(36)  default null,
  DEALER_ID VARCHAR2(36)  default null,
  DEAL_GROUP_ID VARCHAR2(36)  default null,
  TYPEINFO_ID VARCHAR2(36)  default null,
  SYSID VARCHAR2(36)  default null,
  FAULT_NO VARCHAR2(32)  default null,
  FAULT_SOURCE VARCHAR2(2)  default null,
  SERIOUS_LEV VARCHAR2(2)  default null,
  OCCURRENCE_ADDRESS VARCHAR2(128)  default null,
  OCCUR_TIME VARCHAR2(20)  default null,
  FAULT_REPORT_NAME VARCHAR2(54)  default null,
  REPORT_TIME VARCHAR2(20)  default null,
  REPORT_PHONE VARCHAR2(32)  default null,
  FAULT_CONTACT_NAME VARCHAR2(54)  default null,
  CONTACT_PHONE VARCHAR2(24)  default null,
  CONTACT_ADDRESS VARCHAR2(128)  default null,
  SERIAL_NUMBER VARCHAR2(96)  default null,
  TRADING_NAME VARCHAR2(96)  default null,
  ERROR_INFORMATION VARCHAR2(192)  default null,
  FAULT_DECRIPT_SUMMARY VARCHAR2(128)  default null,
  FAULT_DECRIPT_DETAIL VARCHAR2(3000)  default null,
  CREAT_TIME VARCHAR2(20)  default null,
  DEAL_TIME VARCHAR2(20)  default null,
  DEAL_DESCRIPTION VARCHAR2(2000)  default null,
  EVALUATE VARCHAR2(512)  default null,
  EVALUATE_TIME VARCHAR2(20)  default null,
  END_TIME VARCHAR2(20)  default null,
  CURRENT_STATE VARCHAR2(2)  default null,
  INVOLVE_AMOUNT VARCHAR2(2)  default null,
  INVOLVE_SCOUNT VARCHAR2(60)  default null,
  CUSTOMER_NAME VARCHAR2(48)  default null,
  CUSTOMER_IDCARD VARCHAR2(20)  default null,
  TRANSACTION_ACCOUNT VARCHAR2(256)  default null,
  TRANSACTION_AMOUNT VARCHAR2(108)  default null,
  ORDER_NUMBER VARCHAR2(128)  default null,
  EVALUATE_RESULT VARCHAR2(2)  default null,
  FM_CAUSE VARCHAR2(64)  default null,
  FM_JKMARK VARCHAR2(2)  default null,
  PARTICIPATOR_IDS VARCHAR2(2000)  default null,
  DEAL_MODE VARCHAR2(2)  default null,
  PARTICIPATE_GROUPIDS VARCHAR2(2000)  default null,
  TO_QGZX_TIME VARCHAR2(20)  default null,
  BACK_SZX_TIME VARCHAR2(20)  default null,
  REPEAT_MARK VARCHAR2(2)  default null,
  RECEIVER_IDS VARCHAR2(2000)  default null,
  INVALIDATION_MARK VARCHAR2(2)  default null,
  IF_JJ VARCHAR2(2)  default null,
  IF_BULLETION VARCHAR2(2)  default null,
  BULL_CAUSE VARCHAR2(256)  default null,
  BULL_TIME VARCHAR2(64)  default null,
  IS_ANTI_FRAUD VARCHAR2(2)  default null,
  IS_SEND VARCHAR2(2)  default null,
  INI_SYS VARCHAR2(36)  default null,
  ONETYPE VARCHAR2(36)  default null,
  TWOTYPE VARCHAR2(36)  default null,
  THREETYPE VARCHAR2(36)  default null,
  KEYWORDS VARCHAR2(512)  default null,
  INI_ONETYPE VARCHAR2(36)  default null,
  INI_TWOTYPE VARCHAR2(36)  default null,
  INI_THREETYPE VARCHAR2(36)  default null,
  INI_KEYWORDS VARCHAR2(512)  default null,
  KNOWLEDGE_ID VARCHAR2(512)  default null,
  IF_YN VARCHAR2(2)  default null ,
  C_ID VARCHAR2(36)  default null,
  FM_TYPE_ID VARCHAR2(36)  default null,
  OCCURRENCE_ORG_ID VARCHAR2(36)  default null,
  EVALUATER_ID VARCHAR2(36)  default null,
  record_type VARCHAR2(2)  default null,
  auditor_id            varchar2(36)   default null,
  audit_status          varchar2(2)    default null,
  audit_time	        varchar2(20)   default null,
  n1                    VARCHAR2(512)  default null,
  n2                    VARCHAR2(512)  default null,
  n3                    VARCHAR2(512)  default null,
  n4                    VARCHAR2(512)  default null
);

comment on table  FM_BIZ              is '业务事件单表';
comment on column FM_BIZ.CREATE_ID    is '创建人';
comment on column FM_BIZ.CREATE_ORG_ID    is '创建机构';
comment on column FM_BIZ.GROUP_ID    is '工作组';
comment on column FM_BIZ.DEALER_ID    is '处理人';
comment on column FM_BIZ.DEAL_GROUP_ID    is '处理人';
comment on column FM_BIZ.TYPEINFO_ID    is '类别';
comment on column FM_BIZ.SYSID    is '系统';
comment on column FM_BIZ.FAULT_NO    is '事件单编号';
comment on column FM_BIZ.FAULT_SOURCE    is '事件来源ID：1：手工，2：监控系统';
comment on column FM_BIZ.SERIOUS_LEV    is '事件等级:1:一般,2:急,3紧急';
comment on column FM_BIZ.OCCURRENCE_ADDRESS    is '发生地址';
comment on column FM_BIZ.OCCUR_TIME    is '发生时间';
comment on column FM_BIZ.FAULT_REPORT_NAME    is '报告人';
comment on column FM_BIZ.REPORT_TIME    is '报告时间';
comment on column FM_BIZ.REPORT_PHONE    is '报告人电话';
comment on column FM_BIZ.FAULT_CONTACT_NAME    is '联系人';
comment on column FM_BIZ.CONTACT_PHONE    is '联系人电话';
comment on column FM_BIZ.CONTACT_ADDRESS    is '联系人地址';
comment on column FM_BIZ.SERIAL_NUMBER    is '流水号';
comment on column FM_BIZ.TRADING_NAME    is '交易名称';
comment on column FM_BIZ.ERROR_INFORMATION    is '错误信息';
comment on column FM_BIZ.FAULT_DECRIPT_SUMMARY    is '事件标题';
comment on column FM_BIZ.FAULT_DECRIPT_DETAIL    is '事件描述';
comment on column FM_BIZ.CREAT_TIME    is '创建时间';
comment on column FM_BIZ.DEAL_TIME    is '处理时间';
comment on column FM_BIZ.DEAL_DESCRIPTION    is '处理描述';
comment on column FM_BIZ.EVALUATE    is '评价：1：满意，2：一般，3：不满意，用于运营事件单评价';
comment on column FM_BIZ.EVALUATE_TIME    is '评价时间，用于运营事件单评价';
comment on column FM_BIZ.CURRENT_STATE    is '当前状态: 1-“待提交”2—“作废”3-“待处理”4-“处理中”5-“待分派” 6—“待关闭”7—“已关闭”';
comment on column FM_BIZ.INVOLVE_AMOUNT    is '涉事金额';
comment on column FM_BIZ.INVOLVE_SCOUNT    is '涉事笔数';
comment on column FM_BIZ.CUSTOMER_NAME    is '客户姓名';
comment on column FM_BIZ.CUSTOMER_IDCARD    is '客户身份证号';
comment on column FM_BIZ.TRANSACTION_ACCOUNT    is '交易账号';
comment on column FM_BIZ.TRANSACTION_AMOUNT    is '交易金额';
comment on column FM_BIZ.ORDER_NUMBER    is '订单号';
comment on column FM_BIZ.EVALUATE_RESULT    is '评价结果：满意、不满意';
comment on column FM_BIZ.FM_CAUSE    is '事件起因';
comment on column FM_BIZ.FM_JKMARK    is '监控标记';
comment on column FM_BIZ.PARTICIPATOR_IDS    is '参与处理人员ID,用于过滤我处理过的事件单';
comment on column FM_BIZ.DEAL_MODE    is '处理方式：正常处理/疑难事件处理/多方配合处理/数据变更处理';
comment on column FM_BIZ.PARTICIPATE_GROUPIDS    is '参与工作组ID,用于过滤工作组参与过的事件单';
comment on column FM_BIZ.TO_QGZX_TIME    is '转全国中心时间';
comment on column FM_BIZ.BACK_SZX_TIME    is '退回省中心时间';
comment on column FM_BIZ.REPEAT_MARK    is '转发原因：1多方配合处理，2非本系统，用于统计省转全国中心选择的工作组是否正确';
comment on column FM_BIZ.RECEIVER_IDS    is '领取人ID,用于统计人员领取的事件单数量';
comment on column FM_BIZ.INVALIDATION_MARK    is '无效标志，0：无效，1：有效';
comment on column FM_BIZ.IF_JJ    is '是否紧急';
comment on column FM_BIZ.IF_BULLETION    is '是否通报';
comment on column FM_BIZ.BULL_CAUSE    is '通报原因';
comment on column FM_BIZ.BULL_TIME    is '通报时间';
comment on column FM_BIZ.IS_ANTI_FRAUD    is '是否是反欺诈相关 1-是  0-否';
comment on column FM_BIZ.IS_SEND    is '是否发送成功  1-是 0-否';
comment on column FM_BIZ.INI_SYS    is '初始系统';
comment on column FM_BIZ.ONETYPE    is '当前一级分类';
comment on column FM_BIZ.TWOTYPE    is '当前二级分类';
comment on column FM_BIZ.THREETYPE    is '当前三级分类';
comment on column FM_BIZ.KEYWORDS    is '当前关键字';
comment on column FM_BIZ.INI_ONETYPE    is '初始一级分类';

comment on column FM_BIZ.INI_TWOTYPE    is '初始二级分类';
comment on column FM_BIZ.INI_THREETYPE    is '初始三级分类';
comment on column FM_BIZ.INI_KEYWORDS    is '初始关键字';
comment on column FM_BIZ.KNOWLEDGE_ID    is '知识ID';
comment on column FM_BIZ.c_id    is '客户id';
comment on column FM_BIZ.record_type    is '事件单类型 1、事件单 2、咨询单';

/*
创建事件数据维护表
*/
create table fm_biz_maint (
      m_id          VARCHAR2(32)    primary key not null,
      m_title        VARCHAR2(128)    default null,
      m_detail       VARCHAR2(4000)    default null
);

comment on table  fm_biz_maint              is '事件维护表';
comment on column fm_biz_maint.m_title    is '事件标题';
comment on column fm_biz_maint.m_detail    is '事件描述';
/*
potal
*/
create table SYSTEM_APP(
    id          VARCHAR2(64) primary key not null,
    create_time TIMESTAMP default CURRENT_TIMESTAMP,
    sysname     VARCHAR2(255),
    sysurl      VARCHAR2(255) not null,
    del_flag    int,
    update_time TIMESTAMP default CURRENT_TIMESTAMP,
    param      VARCHAR2(255),
    syscode     VARCHAR2(255),
    sysorder    int,
    status      VARCHAR2(255)
);

comment on table  SYSTEM_APP              is 'potal.系统维护';
comment on column SYSTEM_APP.id    is '部门主键seq_sys_dept.nextval';
comment on column SYSTEM_APP.create_time    is '创建时间';
comment on column SYSTEM_APP.sysname    is '系统名称';
comment on column SYSTEM_APP.sysurl    is '系统平台连接url';
comment on column SYSTEM_APP.del_flag    is '删除标识';
comment on column SYSTEM_APP.update_time    is '更新时间';
comment on column SYSTEM_APP.param    is '参数信息';
comment on column SYSTEM_APP.syscode    is '系统标识码';
comment on column SYSTEM_APP.sysorder    is '系统排序标识';
comment on column SYSTEM_APP.status    is '状态（0正常 1停用）';
/*
SYSTEM_APP_USER
*/
create table SYSTEM_APP_USER(
    id         VARCHAR2(64) primary key not null,
    iappid      VARCHAR2(255),
    iuserid     VARCHAR2(255) not null,
    sysorder    int
);

/*
SYSTEM_APP_ROLE
*/
create table SYSTEM_APP_ROLE
(
    iappid      VARCHAR2(255) ,
    iROLEid     VARCHAR2(255) not null
);
/*
1、存储每一个已配置的 jobDetail 的详细信息
*/
create table qrtz_job_details (
    sched_name varchar2(120) not null,
    job_name  varchar2(200) not null,
    job_group varchar2(200) not null,
    description varchar2(250) null,
    job_class_name   varchar2(250) not null,
    is_durable varchar2(1) not null,
    is_nonconcurrent varchar2(1) not null,
    is_update_data varchar2(1) not null,
    requests_recovery varchar2(1) not null,
    job_data blob null,
    constraint qrtz_job_details_pk primary key (sched_name,job_name,job_group)
);
create index idx_qrtz_j_req_recovery on qrtz_job_details(sched_name,requests_recovery);
create index idx_qrtz_j_grp on qrtz_job_details(sched_name,job_group);

/*
2、存储已配置的 Trigger 的信息
*/
create table qrtz_triggers (
    sched_name varchar2(120) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    job_name  varchar2(200) not null,
    job_group varchar2(200) not null,
    description varchar2(250) null,
    next_fire_time number(13) null,
    prev_fire_time number(13) null,
    priority number(13) null,
    trigger_state varchar2(16) not null,
    trigger_type varchar2(8) not null,
    start_time number(13) not null,
    end_time number(13) null,
    calendar_name varchar2(200) null,
    misfire_instr number(2) null,
    job_data blob null,
    constraint qrtz_triggers_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_trigger_to_jobs_fk foreign key (sched_name,job_name,job_group)
      references qrtz_job_details(sched_name,job_name,job_group)
);
create index idx_qrtz_t_j on qrtz_triggers(sched_name,job_name,job_group);
create index idx_qrtz_t_jg on qrtz_triggers(sched_name,job_group);
create index idx_qrtz_t_c on qrtz_triggers(sched_name,calendar_name);
create index idx_qrtz_t_g on qrtz_triggers(sched_name,trigger_group);
create index idx_qrtz_t_state on qrtz_triggers(sched_name,trigger_state);
create index idx_qrtz_t_n_state on qrtz_triggers(sched_name,trigger_name,trigger_group,trigger_state);
create index idx_qrtz_t_n_g_state on qrtz_triggers(sched_name,trigger_group,trigger_state);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(sched_name,next_fire_time);
create index idx_qrtz_t_nft_st on qrtz_triggers(sched_name,trigger_state,next_fire_time);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_state);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_group,trigger_state);

/*
4、 存储 Cron Trigger，包括 Cron 表达式和时区信息
*/
create table qrtz_cron_triggers (
    sched_name VARCHAR2(120) not null,
    trigger_name VARCHAR2(200) not null,
    trigger_group VARCHAR2(200) not null,
    cron_expression VARCHAR2(120) not null,
    time_zone_id VARCHAR2(80),
    constraint qrtz_cron_trig_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_cron_trig_to_trig_fk foreign key (sched_name,trigger_name,trigger_group)
      references qrtz_triggers(sched_name,trigger_name,trigger_group)
);

/*
5、 Trigger 作为 CLOB 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
*/
create table qrtz_CLOB_triggers (
    sched_name VARCHAR2(120) not null,
    trigger_name VARCHAR2(200) not null,
    trigger_group VARCHAR2(200) not null,
    CLOB_data CLOB null,
    constraint qrtz_CLOB_trig_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_CLOB_trig_to_trig_fk foreign key (sched_name,trigger_name,trigger_group)
      references qrtz_triggers(sched_name,trigger_name,trigger_group)
);

/*
6、 以 CLOB 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围
*/
create table qrtz_calendars (
    sched_name VARCHAR2(120) not null,
    calendar_name  VARCHAR2(200) not null,
    calendar CLOB not null,
    constraint qrtz_calendars_pk primary key (sched_name,calendar_name)
);

/*
7、 存储已暂停的 Trigger 组的信息
*/
create table qrtz_paused_trigger_grps (
    sched_name VARCHAR2(120) not null,
    trigger_group  VARCHAR2(200) not null,
    constraint qrtz_paused_trig_grps_pk primary key (sched_name,trigger_group)
);

/*
8、 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息
*/
create table qrtz_fired_triggers (
    sched_name varchar2(120) not null,
    entry_id varchar2(95) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    instance_name varchar2(200) not null,
    fired_time number(13) not null,
    sched_time number(13) not null,
    priority number(13) not null,
    state varchar2(16) not null,
    job_name varchar2(200) null,
    job_group varchar2(200) null,
    is_nonconcurrent varchar2(1) null,
    requests_recovery varchar2(1) null,
    constraint qrtz_fired_trigger_pk primary key (sched_name,entry_id)
);
create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(sched_name,instance_name);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(sched_name,instance_name,requests_recovery);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(sched_name,job_name,job_group);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(sched_name,job_group);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(sched_name,trigger_name,trigger_group);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(sched_name,trigger_group);

/*
9、 存储少量的有关 Scheduler 的状态信息，假如是用于集群中，可以看到其他的 Scheduler 实例
*/
create table qrtz_scheduler_state (
    sched_name VARCHAR2(120) not null,
    instance_name VARCHAR2(200) not null,
    last_checkin_time number(13) not null,
    checkin_interval number(13) not null,
    constraint qrtz_scheduler_state_pk primary key (sched_name,instance_name)
);

/*
10、 存储程序的悲观锁的信息(假如使用了悲观锁)
*/
create table qrtz_locks (
    sched_name VARCHAR2(120) not null,
    lock_name  VARCHAR2(40) not null,
    constraint qrtz_locks_pk primary key (sched_name,lock_name)
);

/*

*/
create table qrtz_simprop_triggers (
    sched_name VARCHAR2(120) not null,
    trigger_name VARCHAR2(200) not null,
    trigger_group VARCHAR2(200) not null,
    str_prop_1 VARCHAR2(512) null,
    str_prop_2 VARCHAR2(512) null,
    str_prop_3 VARCHAR2(512) null,
    int_prop_1 number(10) null,
    int_prop_2 number(10) null,
    long_prop_1 number(13) null,
    long_prop_2 number(13) null,
    dec_prop_1 numeric(13,4) null,
    dec_prop_2 numeric(13,4) null,
    bool_prop_1 VARCHAR2(1) null,
    bool_prop_2 VARCHAR2(1) null,
    constraint qrtz_simprop_trig_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_simprop_trig_to_trig_fk foreign key (sched_name,trigger_name,trigger_group)
      references qrtz_triggers(sched_name,trigger_name,trigger_group)
);

/*
1、部门表
*/
create sequence seq_sys_dept
    increment by 1
    start with 200
    nomaxvalue
 nominvalue
 cache 20;
create table sys_dept (
  dept_id           number      not null,
  parent_id         number      default 0,
  ancestors         VARCHAR2(50)     default '',
  dept_name         VARCHAR2(30)     default '',
  order_num         number          default 0,
  leader            VARCHAR2(20)     default null,
  phone             VARCHAR2(11)     default null,
  email             VARCHAR2(50)     default null,
  status            char(1)         default '0',
  del_flag          char(1)         default '0',
  create_by         VARCHAR2(64)     default '',
  create_time 	    date,
  update_by         VARCHAR2(64)     default '',
  update_time       date,
  deptlevel         VARCHAR2(255),
  type         VARCHAR2(255),
  operaternum         VARCHAR2(255),
  remark         VARCHAR2(255),
  tier         VARCHAR2(255),
  dept         VARCHAR2(255),
  branchmark         VARCHAR2(255),
  inoutsidemark         VARCHAR2(255),
  deptcode         VARCHAR2(255)
);
alter table sys_dept add constraint pk_sys_dept primary key (dept_id);
comment on table  sys_dept              is '部门信息表';
comment on column sys_dept.dept_id      is '部门主键seq_sys_dept.nextval';
comment on column sys_dept.parent_id    is '父部门id';
comment on column sys_dept.ancestors    is '祖级列表';
comment on column sys_dept.dept_name    is '部门名称';
comment on column sys_dept.order_num    is '显示顺序';
comment on column sys_dept.leader       is '负责人';
comment on column sys_dept.phone        is '联系电话';
comment on column sys_dept.email        is '邮箱';
comment on column sys_dept.status       is '部门状态（0正常 1停用）';
comment on column sys_dept.del_flag     is '删除标志（0代表存在 2代表删除）';
comment on column sys_dept.create_by    is '创建者';
comment on column sys_dept.create_time  is '创建时间';
comment on column sys_dept.update_by    is '更新者';
comment on column sys_dept.update_time  is '更新时间';

/*
2、用户信息表
*/
create sequence seq_sys_user
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;
create table sys_user (
  user_id           number      not null,
  dept_id           number      default null,
  login_name        VARCHAR2(30)     not null,
  user_name         VARCHAR2(30)     default '',
  user_type         VARCHAR2(2)      default '00',
  email             VARCHAR2(50)     default '',
  phonenumber       VARCHAR2(11)     default '',
  sex               char(1)         default '0',
  avatar            VARCHAR2(100)    default '',
  password          VARCHAR2(50)     default '',
  salt              VARCHAR2(20)     default '',
  status            char(1)         default '0',
  del_flag          char(1)         default '0',
  login_ip          VARCHAR2(50)     default '',
  login_date        date,
  pwd_update_date   date,
  create_by         VARCHAR2(64)     default '',
  create_time       date,
  update_by         VARCHAR2(64)     default '',
  update_time       date,
  remark            VARCHAR2(500)    default null
);
alter table sys_user add constraint pk_sys_user primary key (user_id);

comment on table  sys_user              is '用户信息表';
comment on column sys_user.user_id      is '用户主键seq_sys_user.nextval';
comment on column sys_user.dept_id      is '部门ID';
comment on column sys_user.login_name   is '登录账号';
comment on column sys_user.user_name    is '用户昵称';
comment on column sys_user.user_type    is '用户类型（00系统用户）';
comment on column sys_user.email        is '用户邮箱';
comment on column sys_user.phonenumber  is '手机号码';
comment on column sys_user.sex          is '用户性别（0男 1女 2未知）';
comment on column sys_user.avatar       is '头像路径';
comment on column sys_user.password     is '密码';
comment on column sys_user.salt         is '盐加密';
comment on column sys_user.status       is '帐号状态（0正常 1停用）';
comment on column sys_user.del_flag     is '删除标志（0代表存在 2代表删除）';
comment on column sys_user.login_ip     is '最后登陆IP';
comment on column sys_user.login_date   is '最后登陆时间';
comment on column sys_user.create_by    is '创建者';
comment on column sys_user.create_time  is '创建时间';
comment on column sys_user.update_by    is '更新者';
comment on column sys_user.update_time  is '更新时间';
comment on column sys_user.remark       is '备注';
/*
3、岗位信息表
*/
create sequence seq_sys_post
    increment by 1
    start with 10
    nomaxvalue
 nominvalue
 cache 20;

create table sys_post
(
    post_id 			number 		    not null,
    post_code         varchar2(64)    not null,
    post_name         varchar2(50)    not null,
    post_sort         number          not null,
    status            char(1)         not null,
    create_by         varchar2(64)    default '',
    create_time       date,
    update_by         varchar2(64) 	default '',
    update_time       date,
    remark 		    varchar2(500)
);

alter table sys_post add constraint pk_sys_post primary key (post_id);

comment on table  sys_post              is '岗位信息表';
comment on column sys_post.post_id      is '岗位主键seq_sys_post.nextval';
comment on column sys_post.post_code    is '岗位编码';
comment on column sys_post.post_name    is '岗位名称';
comment on column sys_post.post_sort    is '显示顺序';
comment on column sys_post.status       is '状态（0正常 1停用）';
comment on column sys_post.create_by    is '创建者';
comment on column sys_post.create_time  is '创建时间';
comment on column sys_post.update_by    is '更新者';
comment on column sys_post.update_time  is '更新时间';
comment on column sys_post.remark       is '备注';
/*
4、角色信息表
*/
create sequence seq_sys_role
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;

create table sys_role (
  role_id 			number          not null,
  role_name 		varchar2(30) 	not null,
  role_key 		    varchar2(100) 	not null,
  role_sort         number          not null,
  data_scope        char(1) 	    default '1',
  status 			char(1) 		not null,
  del_flag			char(1) 		default '0',
  create_by         varchar2(64)    default '',
  create_time 		date,
  update_by 		varchar2(64) 	default '',
  update_time 		date,
  remark 			varchar2(500) 	default null
);

alter table sys_role add constraint pk_sys_role primary key (role_id);

comment on table  sys_role              is '角色信息表';
comment on column sys_role.role_id      is '角色主键seq_sys_post.nextval';
comment on column sys_role.role_name    is '角色名称';
comment on column sys_role.role_key     is '角色权限字符串';
comment on column sys_role.role_sort    is '显示顺序';
comment on column sys_role.data_scope   is '数据范围（1：全部数据权限 2：自定数据权限）';
comment on column sys_role.status       is '角色状态（0正常 1停用）';
comment on column sys_role.del_flag     is '删除标志（0代表存在 2代表删除）';
comment on column sys_role.create_by    is '创建者';
comment on column sys_role.create_time  is '创建时间';
comment on column sys_role.update_by    is '更新者';
comment on column sys_role.update_time  is '更新时间';
comment on column sys_role.remark       is '备注';

/*
5、菜单权限表
*/
create sequence seq_sys_menu
    increment by 1
    start with 2000
    nomaxvalue
 nominvalue
 cache 20;

create table sys_menu (
  menu_id 			number 		    not null,
  menu_name 		varchar2(50) 	not null,
  parent_id 		number          default 0,
  order_num 		number          default 0,
  url 				varchar2(200) 	default '#',
  target            varchar2(20)     default '',
  menu_type 		char(1) 		default '',
  visible 			char(1) 		default 0,
  perms 			varchar2(100) 	default null,
  icon 				varchar2(100) 	default '#',
  create_by         varchar2(64)    default '',
  create_time 		date,
  update_by 		varchar2(64) 	default '',
  update_time 		date ,
  remark 			varchar2(500) 	default ''
);

alter table sys_menu add constraint pk_sys_menu primary key (menu_id);

comment on table  sys_menu              is '菜单权限表';
comment on column sys_menu.menu_id      is '菜单主键seq_sys_post.nextval';
comment on column sys_menu.menu_name    is '菜单名称';
comment on column sys_menu.parent_id    is '父菜单ID';
comment on column sys_menu.order_num    is '显示顺序';
comment on column sys_menu.url          is '请求地址';
comment on column sys_menu.target       is '打开方式（menuItem页签 menuBlank新窗口）';
comment on column sys_menu.menu_type    is '菜单类型（M目录 C菜单 F按钮）';
comment on column sys_menu.visible      is '菜单状态（0显示 1隐藏）';
comment on column sys_menu.perms        is '权限标识';
comment on column sys_menu.icon         is '菜单图标';
comment on column sys_menu.create_by    is '创建者';
comment on column sys_menu.create_time  is '创建时间';
comment on column sys_menu.update_by    is '更新者';
comment on column sys_menu.update_time  is '更新时间';
comment on column sys_menu.remark       is '备注';
/*
6、用户和角色关联表  用户N-1角色
*/
create table sys_user_role (
                               user_id 	varchar2(100)  not null,
                               role_id 	number  not null
);

alter table sys_user_role add constraint pk_sys_user_role primary key (user_id, role_id);

comment on table  sys_user_role              is '用户和角色关联表';
comment on column sys_user_role.user_id      is '用户ID';
comment on column sys_user_role.role_id      is '角色ID';


/*
7、角色和菜单关联表  角色1-N菜单
*/
create table sys_role_menu (
                               ID NUMBER(19,0) NOT NULL,
                               role_id 	VARCHAR2(255)  not null,
                               menu_id 	number  not null,
                               constraint pk_sys_role_menu primary key (role_id, menu_id)
);

comment on table  sys_role_menu              is '角色和菜单关联表';
comment on column sys_role_menu.role_id      is '角色ID';
comment on column sys_role_menu.menu_id      is '菜单ID';

create sequence seq_sys_role_menu
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;
/*
8、角色和部门关联表  角色1-N部门
*/
create table sys_role_dept (
                               role_id 	number  not null,
                               dept_id 	number  not null
);

alter table sys_role_dept add constraint pk_sys_role_dept primary key (role_id, dept_id);

comment on table  sys_role_dept              is '角色和部门关联表';
comment on column sys_role_dept.role_id      is '角色ID';
comment on column sys_role_dept.dept_id      is '部门ID';
/*
9、用户与岗位关联表  用户1-N岗位
*/
create table sys_user_post
(
    user_id number  not null,
    post_id number  not null
);

alter table sys_user_post add constraint pk_sys_user_post primary key (user_id, post_id);

comment on table  sys_user_post              is '用户与岗位关联表';
comment on column sys_user_post.user_id      is '用户ID';
comment on column sys_user_post.post_id      is '岗位ID';

/*
10、操作日志记录
*/
create table sys_oper_log (
  oper_id 			number          not null ,
  title             varchar2(50)    default '',
  business_type     number          default 0,
  method            varchar2(100)   default '',
  operator_type     number          default 0,
  oper_name 	    varchar2(50)    default '',
  dept_name 		varchar2(50)    default '',
  oper_url 		    varchar2(255) 	default '',
  oper_ip 			varchar2(50) 	default '',
  oper_location     varchar2(255)   default '',
  json_result       VARCHAR2(4000)   default '',
  request_method    VARCHAR2(10)     default '',
  oper_param 		varchar2(2000) 	default '',
  status 			number 		    default 0,
  error_msg 		varchar2(2000) 	default '' ,
  oper_time 		date,
  constraint pk_sys_oper_log primary key (oper_id)
);

comment on table  sys_oper_log               is '操作日志记录';
comment on column sys_oper_log.oper_id       is '日志主键seq_sys_oper_log.nextval';
comment on column sys_oper_log.title         is '模块标题';
comment on column sys_oper_log.business_type is '业务类型（0其它 1新增 2修改 3删除）';
comment on column sys_oper_log.method        is '方法名称';
comment on column sys_oper_log.operator_type is '操作类别（0其它 1后台用户 2手机端用户）';
comment on column sys_oper_log.oper_name     is '操作人员';
comment on column sys_oper_log.dept_name     is '部门名称';
comment on column sys_oper_log.oper_url      is '请求URL';
comment on column sys_oper_log.oper_ip       is '主机地址';
comment on column sys_oper_log.oper_location is '操作地点';
comment on column sys_oper_log.oper_param    is '请求参数';
comment on column sys_oper_log.status        is '操作状态（0正常 1异常）';
comment on column sys_oper_log.error_msg     is '错误消息';
comment on column sys_oper_log.oper_time     is '操作时间';
/*
11、字典类型表
*/
create sequence seq_sys_dict_type
    increment by 1
    start with 200
    nomaxvalue
 nominvalue
 cache 20;

create table sys_dict_type
(
    dict_id          number          not null,
    dict_name        varchar2(100)   default '',
    dict_type        varchar2(100)   default '',
    status 			 char(1) 		 default '0',
    create_by        varchar2(64)    default '',
    create_time      date,
    update_by        varchar2(64) 	 default '',
    update_time      date,
    remark 	         varchar2(500) 	 default null,
    constraint pk_sys_dict_type primary key (dict_id)
);

create unique index sys_dict_type_index1 on sys_dict_type (dict_type);

comment on table  sys_dict_type               is '字典类型表';
comment on column sys_dict_type.dict_id       is '字典主键seq_sys_dict_type.nextval';
comment on column sys_dict_type.dict_name     is '字典名称';
comment on column sys_dict_type.dict_type     is '字典类型';
comment on column sys_dict_type.status        is '状态（0正常 1停用）';
comment on column sys_dict_type.create_by     is '创建者';
comment on column sys_dict_type.create_time   is '创建时间';
comment on column sys_dict_type.update_by     is '更新者';
comment on column sys_dict_type.update_time   is '更新时间';
comment on column sys_dict_type.remark        is '备注';


/*
12、字典数据表
*/
create sequence seq_sys_dict_data
    increment by 1
    start with 200
    nomaxvalue
 nominvalue
 cache 20;

create table sys_dict_data
(
    dict_code        number          not null,
    dict_sort        number          default 0,
    dict_label       varchar2(100)   default '',
    dict_value       varchar2(100)   default '',
    dict_type        varchar2(100)   default '',
    css_class        varchar2(100)   default null,
    list_class       varchar2(100)   default null,
    is_default       char(1)         default 'N',
    status 			 char(1) 		 default '0',
    create_by        varchar2(64)    default '',
    create_time      date,
    update_by        varchar2(64) 	 default '',
    update_time      date,
    remark 	         varchar2(500) 	 default null,
    constraint pk_sys_dict_data primary key (dict_code)
);

comment on table  sys_dict_data               is '字典数据表';
comment on column sys_dict_data.dict_code     is '字典主键seq_sys_dict_data.nextval';
comment on column sys_dict_data.dict_sort     is '字典排序';
comment on column sys_dict_data.dict_label    is '字典标签';
comment on column sys_dict_data.dict_value    is '字典键值';
comment on column sys_dict_data.dict_type     is '字典类型';
comment on column sys_dict_data.css_class     is '样式属性（其他样式扩展）';
comment on column sys_dict_data.list_class    is '表格回显样式';
comment on column sys_dict_data.is_default    is '是否默认（Y是 N否）';
comment on column sys_dict_data.status        is '状态（0正常 1停用）';
comment on column sys_dict_data.create_by     is '创建者';
comment on column sys_dict_data.create_time   is '创建时间';
comment on column sys_dict_data.update_by     is '更新者';
comment on column sys_dict_data.update_time   is '更新时间';
comment on column sys_dict_data.remark        is '备注';


/*
13、参数配置表
*/
create sequence seq_sys_config
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;

create table sys_config (
    config_id 		   number        not null,
    config_name        varchar2(100) default '',
    config_key         varchar2(100) default '',
    config_value       varchar2(100) default '',
    config_type        char(1)       default 'N',
    create_by          varchar2(64)  default '',
    create_time 	   date,
    update_by          varchar2(64)  default '',
    update_time        date,
    remark 	           varchar2(500)  default null,
    constraint pk_sys_config primary key (config_id)
);

comment on table  sys_config               is '参数配置表';
comment on column sys_config.config_id     is '参数主键seq_sys_config.nextval';
comment on column sys_config.config_name   is '参数名称';
comment on column sys_config.config_key    is '参数键名';
comment on column sys_config.config_value  is '参数键值';
comment on column sys_config.config_type   is '系统内置（Y是 N否）';
comment on column sys_config.create_by     is '创建者';
comment on column sys_config.create_time   is '创建时间';
comment on column sys_config.update_by     is '更新者';
comment on column sys_config.update_time   is '更新时间';
comment on column sys_config.remark        is '备注';

insert into sys_config values(1, '主框架页-默认皮肤样式名称', 'sys.index.skinName',     'skin-blue',     'Y', 'admin', TO_DATE('2018-03-16 11-33-00', 'YYYY-MM-DD HH24:MI:SS'), 'ry', TO_DATE('2018-03-16 11-33-00', 'YYYY-MM-DD HH24:MI:SS'), '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' );
insert into sys_config values(2, '用户管理-账号初始密码',     'sys.user.initPassword',  '123456',        'Y', 'admin', TO_DATE('2018-03-16 11-33-00', 'YYYY-MM-DD HH24:MI:SS'), 'ry', TO_DATE('2018-03-16 11-33-00', 'YYYY-MM-DD HH24:MI:SS'), '初始化密码 123456' );
insert into sys_config values(3, '主框架页-侧边栏主题',       'sys.index.sideTheme',    'theme-dark',    'Y', 'admin', TO_DATE('2018-03-16 11-33-00', 'YYYY-MM-DD HH24:MI:SS'), 'ry', TO_DATE('2018-03-16 11-33-00', 'YYYY-MM-DD HH24:MI:SS'), '深色主题theme-dark，浅色主题theme-light' );


/*
14、系统访问记录
*/
create sequence seq_sys_logininfor
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;

create table sys_logininfor (
    info_id 		 number        not null,
    login_name 	 varchar2(50)   default '',
    ipaddr 		 varchar2(50)   default '',
    login_location varchar2(255)  default '',
    browser  		 varchar2(50)   default '',
    os      		 varchar2(50)   default '',
    status 		 char(1) 	   default '0',
    msg      		 varchar2(255)  default '',
    login_time 	 date,
    constraint pk_sys_logininfor primary key (info_id)
);

comment on table  sys_logininfor                is '系统访问记录';
comment on column sys_logininfor.info_id        is '访问主键seq_seq_sys_logininfor.nextval';
comment on column sys_logininfor.login_name     is '登录账号';
comment on column sys_logininfor.ipaddr         is '登录IP地址';
comment on column sys_logininfor.login_location is '登录地点';
comment on column sys_logininfor.browser        is '浏览器类型';
comment on column sys_logininfor.os             is '操作系统';
comment on column sys_logininfor.status         is '登录状态（0成功 1失败）';
comment on column sys_logininfor.msg            is '提示消息';
comment on column sys_logininfor.login_time     is '访问时间';

/*
15、在线用户记录
*/
create table sys_user_online (
     sessionId       varchar2(50)  default '',
     login_name       varchar2(50)  default '',
     dept_name     varchar2(50)  default '',
     ipaddr         varchar2(50)  default '',
     login_location    varchar2(255) default '',
     browser          varchar2(50)  default '',
     os              varchar2(50)  default '',
     status            varchar2(10)  default '',
     start_timestamp   date,
     last_access_time  date,
     expire_time       number      default 0,
     user_id			VARCHAR2(32),
     constraint pk_sys_user_online primary key (sessionId)
);

comment on table  sys_user_online                   is '在线用户记录';
comment on column sys_user_online.sessionId         is '用户会话id';
comment on column sys_user_online.login_name        is '登录账号';
comment on column sys_user_online.dept_name         is '部门名称';
comment on column sys_user_online.ipaddr            is '登录IP地址';
comment on column sys_user_online.login_location    is '登录地点';
comment on column sys_user_online.browser           is '浏览器类型';
comment on column sys_user_online.os                is '操作系统';
comment on column sys_user_online.status            is '在线状态on_line在线off_line离线';
comment on column sys_user_online.start_timestamp   is 'session创建时间';
comment on column sys_user_online.last_access_time  is 'session最后访问时间';
comment on column sys_user_online.expire_time       is '超时时间，单位为分钟';
comment on column sys_user_online.user_id       is '人员id';

-- ----------------------------
-- 27、参数管理类别设置表
-- ----------------------------


CREATE TABLE SYS_CATEGORY (
  CATEGORY_ID NUMBER NOT NULL ,
  CATEGORY_TYPE_CODE VARCHAR2(255) NULL ,
  CATEGORY_TYPE_NAME VARCHAR2(255) NULL ,
  CATEGORY_PARENT_ID NUMBER NULL ,
  CATEGORY_CODE VARCHAR2(255) NULL ,
  CATEGORY_NAME VARCHAR2(255) NULL ,
  ANCESTORS VARCHAR2(255) NULL ,
  ORDER_NUM NUMBER NULL ,
  REMARK VARCHAR2(255) NULL ,
  DEL_FLAG VARCHAR2(255) DEFAULT 0  NULL ,
  CREATE_BY VARCHAR2(255) NULL ,
  CREATE_TIME DATE NULL ,
  UPDATE_BY VARCHAR2(7) NULL ,
  UPDATE_TIME DATE NULL ,
  TIER VARCHAR2(255) NULL,
  constraint pk_SYS_CATEGORY PRIMARY KEY (CATEGORY_ID)
);
-- 备注信息
COMMENT ON COLUMN SYS_CATEGORY.CATEGORY_ID IS '类别';
COMMENT ON COLUMN SYS_CATEGORY.CATEGORY_TYPE_CODE IS '类别类型编码';
COMMENT ON COLUMN SYS_CATEGORY.CATEGORY_TYPE_NAME IS '类别类型名称';
COMMENT ON COLUMN SYS_CATEGORY.CATEGORY_PARENT_ID IS '父类别ID';
COMMENT ON COLUMN SYS_CATEGORY.CATEGORY_CODE IS '类被编码';
COMMENT ON COLUMN SYS_CATEGORY.CATEGORY_NAME IS '类别名称';
COMMENT ON COLUMN SYS_CATEGORY.ANCESTORS IS '祖先关系';
COMMENT ON COLUMN SYS_CATEGORY.ORDER_NUM IS '类别顺序';
COMMENT ON COLUMN SYS_CATEGORY.REMARK IS '备注';
COMMENT ON COLUMN SYS_CATEGORY.DEL_FLAG IS '是否删除';
COMMENT ON COLUMN SYS_CATEGORY.CREATE_BY IS '创建人';
COMMENT ON COLUMN SYS_CATEGORY.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN SYS_CATEGORY.UPDATE_BY IS '更新人';
COMMENT ON COLUMN SYS_CATEGORY.UPDATE_TIME IS '更新时间';
COMMENT ON COLUMN SYS_CATEGORY.TIER IS '层级别';

create sequence seq_sys_category
    START WITH 17
    INCREMENT by 1
    cache 20
nomaxvalue;

-- ----------------------------
-- 28、人员管理表
-- ----------------------------

CREATE TABLE PUB_PERSONNEL_MANAGEMENT (
  PID VARCHAR2(32) NOT NULL ,
  ORGID VARCHAR2(32) NULL ,
  PNAME VARCHAR2(16) NULL ,
  SEX VARCHAR2(3) NULL ,
  EDU VARCHAR2(3) NULL ,
  BIRTHDAY DATE NULL ,
  BIRTHPLACE VARCHAR2(32) NULL ,
  PHONE VARCHAR2(32) NULL ,
  MOBLE_PHONE VARCHAR2(32) NULL ,
  EMALL VARCHAR2(32) NULL ,
  ADDRESS VARCHAR2(64) NULL ,
  ADDER VARCHAR2(32) NULL ,
  ADDTIME VARCHAR2(10) NULL ,
  MODER VARCHAR2(32) NULL ,
  MOME VARCHAR2(512) NULL ,
  UPDATE_TIME VARCHAR2(10) NULL ,
  INVALIDATION_MARK VARCHAR2(2) NULL ,
  POSITION VARCHAR2(32) NULL ,
  LEADER VARCHAR2(2) NULL ,
  P_ORDER VARCHAR2(8) NULL ,
  P_FLAG VARCHAR2(2) NULL
);
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.PID IS '人员ID';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.ORGID IS '机构ID';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.PNAME IS '人员名称';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.SEX IS '性别';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.EDU IS '教育程度';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.BIRTHDAY IS '生日';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.BIRTHPLACE IS '出生地点';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.PHONE IS '电话';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.MOBLE_PHONE IS '移动电话';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.EMALL IS '邮箱';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.ADDRESS IS '地址';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.ADDER IS '添加人';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.ADDTIME IS '添加时间';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.MODER IS '修改人';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.MOME IS '备注';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.UPDATE_TIME IS '更新时间';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.INVALIDATION_MARK IS '无效标识';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.POSITION IS '职位';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.LEADER IS '领导标志';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.P_ORDER IS '人员序列';
COMMENT ON COLUMN PUB_PERSONNEL_MANAGEMENT.P_FLAG IS '人员标志';



-- ----------------------------
-- 28、岗位与角色关联表
-- ----------------------------



CREATE TABLE SYS_POST_ROLE (
   POST_ID NUMBER(32) NOT NULL ,
   ROLE_ID NUMBER(32) NOT NULL,
   primary key (POST_ID,ROLE_ID)
);


create table ACT_GE_PROPERTY (
     NAME_ NVARCHAR2(64),
     VALUE_ NVARCHAR2(300),
     REV_ INTEGER,
     primary key (NAME_)
);

create table ACT_GE_BYTEARRAY (
      ID_ NVARCHAR2(64),
      REV_ INTEGER,
      NAME_ NVARCHAR2(255),
      DEPLOYMENT_ID_ NVARCHAR2(64),
      BYTES_ CLOB,
      GENERATED_ DECIMAL(1,0) CHECK (GENERATED_ IN (1,0)),
      primary key (ID_)
);

create table ACT_RE_DEPLOYMENT (
       ID_ NVARCHAR2(64),
       NAME_ NVARCHAR2(255),
       CATEGORY_ NVARCHAR2(255),
       KEY_ NVARCHAR2(255),
       TENANT_ID_ NVARCHAR2(255) DEFAULT '',
       DEPLOY_TIME_ TIMESTAMP(6),
       ENGINE_VERSION_ NVARCHAR2(255),
       primary key (ID_)
);


create table ACT_RE_MODEL (
  ID_ NVARCHAR2(64) not null,
  REV_ INTEGER,
  NAME_ NVARCHAR2(255),
  KEY_ NVARCHAR2(255),
  CATEGORY_ NVARCHAR2(255),
  CREATE_TIME_ TIMESTAMP(6),
  LAST_UPdatetime_TIME_ TIMESTAMP(6),
  VERSION_ INTEGER,
  META_INFO_ NVARCHAR2(2000),
  DEPLOYMENT_ID_ NVARCHAR2(64),
  EDITOR_SOURCE_VALUE_ID_ NVARCHAR2(64),
  EDITOR_SOURCE_EXTRA_VALUE_ID_ NVARCHAR2(64),
  TENANT_ID_ NVARCHAR2(255) DEFAULT '',
  constraint pk_ACT_RE_MODEL PRIMARY KEY (ID_)
);


create table ACT_RU_TIMER_JOB (
      ID_ NVARCHAR2(64) NOT NULL,
      REV_ INTEGER,
      TYPE_ NVARCHAR2(255) NOT NULL,
      LOCK_EXP_TIME_ TIMESTAMP(6),
      LOCK_OWNER_ NVARCHAR2(255),
      EXCLUSIVE_ DECIMAL(1,0),
      EXECUTION_ID_ NVARCHAR2(64),
      PROCESS_INSTANCE_ID_ NVARCHAR2(64),
      PROC_DEF_ID_ NVARCHAR2(64),
      RETRIES_ INTEGER,
      EXCEPTION_STACK_ID_ NVARCHAR2(64),
      EXCEPTION_MSG_ NVARCHAR2(2000),
      DUEDATE_ TIMESTAMP(6),
      REPEAT_ NVARCHAR2(255),
      HANDLER_TYPE_ NVARCHAR2(255),
      HANDLER_CFG_ NVARCHAR2(2000),
      TENANT_ID_ NVARCHAR2(255) DEFAULT '',
    CHECK (EXCLUSIVE_ IN (1,0)),
    constraint pk_ACT_RU_TIMER_JOB PRIMARY KEY (ID_)
);

create table ACT_RU_EXECUTION (
      ID_ NVARCHAR2(64),
      REV_ INTEGER,
      PROC_INST_ID_ NVARCHAR2(64),
      BUSINESS_KEY_ NVARCHAR2(255),
      PARENT_ID_ NVARCHAR2(64),
      PROC_DEF_ID_ NVARCHAR2(64),
      SUPER_EXEC_ NVARCHAR2(64),
      ROOT_PROC_INST_ID_ NVARCHAR2(64),
      ACT_ID_ NVARCHAR2(255),
      COMMENT_ACTIVE_ DECIMAL(1,0) ,
      COMMENT_CONCURRENT_ DECIMAL(1,0) ,
      COMMENT_SCOPE_ DECIMAL(1,0) ,
      COMMENT_EVENT_SCOPE_ DECIMAL(1,0) ,
      COMMENT_MI_ROOT_ DECIMAL(1,0) ,
      SUSPENSION_STATE_ INTEGER,
      CACHED_ENT_STATE_ INTEGER,
      TENANT_ID_ NVARCHAR2(255) DEFAULT '',
      NAME_ NVARCHAR2(255),
      START_TIME_ TIMESTAMP(6),
      START_USER_ID_ NVARCHAR2(255),
      LOCK_TIME_ TIMESTAMP(6),
      COMMENT_COUNT_ENABLED_ DECIMAL(1,0) ,
      EVT_SUBSCR_COUNT_ INTEGER,
      TASK_COUNT_ INTEGER,
      JOB_COUNT_ INTEGER,
      TIMER_JOB_COUNT_ INTEGER,
      SUSP_JOB_COUNT_ INTEGER,
      DEADLETTER_JOB_COUNT_ INTEGER,
      VAR_COUNT_ INTEGER,
      ID_LINK_COUNT_ INTEGER,
    CHECK (COMMENT_ACTIVE_ IN (1,0)),
    CHECK (COMMENT_CONCURRENT_ IN (1,0)),
    CHECK (COMMENT_SCOPE_ IN (1,0)),
    CHECK (COMMENT_EVENT_SCOPE_ IN (1,0)),
    CHECK (COMMENT_MI_ROOT_ IN (1,0)),
    CHECK (COMMENT_COUNT_ENABLED_ IN (1,0)),
    constraint pk_ACT_RU_EXECUTION PRIMARY KEY (ID_)
);

create table ACT_RU_JOB (
    ID_ NVARCHAR2(64) NOT NULL,
    REV_ INTEGER,
    TYPE_ NVARCHAR2(255) NOT NULL,
    LOCK_EXP_TIME_ TIMESTAMP(6),
    LOCK_OWNER_ NVARCHAR2(255),
    EXCLUSIVE_ DECIMAL(1,0),
    EXECUTION_ID_ NVARCHAR2(64),
    PROCESS_INSTANCE_ID_ NVARCHAR2(64),
    PROC_DEF_ID_ NVARCHAR2(64),
    RETRIES_ INTEGER,
    EXCEPTION_STACK_ID_ NVARCHAR2(64),
    EXCEPTION_MSG_ NVARCHAR2(2000),
    DUEDATE_ TIMESTAMP(6),
    REPEAT_ NVARCHAR2(255),
    HANDLER_TYPE_ NVARCHAR2(255),
    HANDLER_CFG_ NVARCHAR2(2000),
    TENANT_ID_ NVARCHAR2(255) DEFAULT '',
 CHECK (EXCLUSIVE_ IN (1,0)),
 constraint pk_ACT_RU_JOB PRIMARY KEY (ID_)
);

create table ACT_RU_SUSPENDED_JOB (
          ID_ NVARCHAR2(64) NOT NULL,
          REV_ INTEGER,
          TYPE_ NVARCHAR2(255) NOT NULL,
          EXCLUSIVE_ DECIMAL(1,0) ,
          EXECUTION_ID_ NVARCHAR2(64),
          PROCESS_INSTANCE_ID_ NVARCHAR2(64),
          PROC_DEF_ID_ NVARCHAR2(64),
          RETRIES_ INTEGER,
          EXCEPTION_STACK_ID_ NVARCHAR2(64),
          EXCEPTION_MSG_ NVARCHAR2(2000),
          DUEDATE_ TIMESTAMP(6),
          REPEAT_ NVARCHAR2(255),
          HANDLER_TYPE_ NVARCHAR2(255),
          HANDLER_CFG_ NVARCHAR2(2000),
          TENANT_ID_ NVARCHAR2(255) DEFAULT '',
		  CHECK (EXCLUSIVE_ IN (1,0)),
		  constraint pk_ACT_RU_SUSPENDED_JOB PRIMARY KEY (ID_)
);

create table ACT_RU_DEADLETTER_JOB (
           ID_ NVARCHAR2(64) NOT NULL,
           REV_ INTEGER,
           TYPE_ NVARCHAR2(255) NOT NULL,
           EXCLUSIVE_ DECIMAL(1,0),
           EXECUTION_ID_ NVARCHAR2(64),
           PROCESS_INSTANCE_ID_ NVARCHAR2(64),
           PROC_DEF_ID_ NVARCHAR2(64),
           EXCEPTION_STACK_ID_ NVARCHAR2(64),
           EXCEPTION_MSG_ NVARCHAR2(2000),
           DUEDATE_ TIMESTAMP(6),
           REPEAT_ NVARCHAR2(255),
           HANDLER_TYPE_ NVARCHAR2(255),
           HANDLER_CFG_ NVARCHAR2(2000),
           TENANT_ID_ NVARCHAR2(255) DEFAULT '',
		    CHECK (EXCLUSIVE_ IN (1,0)),
		    constraint pk_ACT_RU_DEADLETTER_JOB PRIMARY KEY (ID_)
);

create table ACT_RE_PROCDEF (
    ID_ NVARCHAR2(64) NOT NULL,
    REV_ INTEGER,
    CATEGORY_ NVARCHAR2(255),
    NAME_ NVARCHAR2(255),
    KEY_ NVARCHAR2(255) NOT NULL,
    VERSION_ INTEGER NOT NULL,
    DEPLOYMENT_ID_ NVARCHAR2(64),
    RESOURCE_NAME_ NVARCHAR2(2000),
    DGRM_RESOURCE_NAME_ VARCHAR2(4000),
    DESCRIPTION_ NVARCHAR2(2000),
    HAS_START_FORM_KEY_ DECIMAL(1,0) ,
    HAS_GRAPHICAL_NOTATION_ DECIMAL(1,0) ,
    SUSPENSION_STATE_ INTEGER,
    TENANT_ID_ NVARCHAR2(255) DEFAULT '',
    ENGINE_VERSION_ NVARCHAR2(255),
	CHECK (HAS_START_FORM_KEY_ IN (1,0)),
	CHECK (HAS_GRAPHICAL_NOTATION_ IN (1,0)),
	constraint pk_ACT_RE_PROCDEF PRIMARY KEY (ID_)
);

create table ACT_RU_TASK (
     ID_ NVARCHAR2(64) ,
     REV_ INTEGER,
     EXECUTION_ID_ NVARCHAR2(64),
     PROC_INST_ID_ NVARCHAR2(64),
     PROC_DEF_ID_ NVARCHAR2(64),
     NAME_ NVARCHAR2(255),
     PARENT_TASK_ID_ NVARCHAR2(64),
     DESCRIPTION_ NVARCHAR2(2000),
     TASK_DEF_KEY_ NVARCHAR2(255),
     OWNER_ NVARCHAR2(255),
     ASSIGNEE_ NVARCHAR2(255),
     DELEGATION_ NVARCHAR2(64),
     PRIORITY_ INTEGER,
     CREATE_TIME_ TIMESTAMP(6),
     DUE_DATE_ TIMESTAMP(6),
     CATEGORY_ NVARCHAR2(255),
     SUSPENSION_STATE_ INTEGER,
     TENANT_ID_ NVARCHAR2(255) DEFAULT '',
     FORM_KEY_ NVARCHAR2(255),
     CLAIM_TIME_ TIMESTAMP(6),
     constraint pk_ACT_RU_TASK PRIMARY KEY (ID_)
);

create table ACT_RU_IDENTITYLINK (
         ID_ NVARCHAR2(64),
         REV_ INTEGER,
         GROUP_ID_ NVARCHAR2(255),
         TYPE_ NVARCHAR2(255),
         USER_ID_ NVARCHAR2(255),
         TASK_ID_ NVARCHAR2(64),
         PROC_INST_ID_ NVARCHAR2(64),
         PROC_DEF_ID_ NVARCHAR2(64),
         constraint pk_ACT_RU_IDENTITYLINK PRIMARY KEY (ID_)
);




create table ACT_RU_EVENT_SUBSCR (
         ID_ NVARCHAR2(64) not null ,
         REV_ integer,
         EVENT_TYPE_ NVARCHAR2(255) not null,
         EVENT_NAME_ NVARCHAR2(255),
         EXECUTION_ID_ NVARCHAR2(64),
         PROC_INST_ID_ NVARCHAR2(64),
         ACTIVITY_ID_ NVARCHAR2(64),
         CONFIGURATION_ NVARCHAR2(255),
         CREATED_ TIMESTAMP(6) not null,
         PROC_DEF_ID_ NVARCHAR2(64),
         TENANT_ID_ NVARCHAR2(255) DEFAULT '',
         constraint pk_ACT_RU_EVENT_SUBSCR PRIMARY KEY (ID_)
);

create table ACT_EVT_LOG (
     LOG_NR_ int(19),
     TYPE_ NVARCHAR2(64),
     PROC_DEF_ID_ NVARCHAR2(64),
     PROC_INST_ID_ NVARCHAR2(64),
     EXECUTION_ID_ NVARCHAR2(64),
     TASK_ID_ NVARCHAR2(64),
     TIME_STAMP_ TIMESTAMP(6) not null,
     USER_ID_ NVARCHAR2(255),
     DATA_ CLOB,
     LOCK_OWNER_ NVARCHAR2(255),
     LOCK_TIME_ TIMESTAMP(6) null,
     COMMENT_PROCESSED_ int(3) default 0,
     constraint pk_ACT_EVT_LOG PRIMARY KEY (LOG_NR_)
);


create table ACT_PROCDEF_INFO (
      ID_ NVARCHAR2(64) not null,
      PROC_DEF_ID_ NVARCHAR2(64) not null,
      REV_ integer,
      INFO_JSON_ID_ NVARCHAR2(64),
      constraint pk_ACT_PROCDEF_INFO PRIMARY KEY (ID_)
);


create index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION(BUSINESS_KEY_);
create index ACT_IDX_EXEC_ROOT on ACT_RU_EXECUTION(ROOT_PROC_INST_ID_);
create index ACT_IDX_TASK_CREATE on ACT_RU_TASK(CREATE_TIME_);
create index ACT_IDX_IDENT_LNK_USER on ACT_RU_IDENTITYLINK(USER_ID_);
create index ACT_IDX_IDENT_LNK_GROUP on ACT_RU_IDENTITYLINK(GROUP_ID_);
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on ACT_RU_EVENT_SUBSCR(CONFIGURATION_);

create index ACT_IDX_BYTEAR_DEPL on ACT_GE_BYTEARRAY(DEPLOYMENT_ID_);

create index ACT_IDX_EXE_PROCINST on ACT_RU_EXECUTION(PROC_INST_ID_);

create index ACT_IDX_EXE_PARENT on ACT_RU_EXECUTION(PARENT_ID_);
alter table ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
        foreign key (DEPLOYMENT_ID_)
            references ACT_RE_DEPLOYMENT (ID_);







alter table ACT_RE_PROCDEF
    add constraint ACT_UNIQ_PROCDEF
        unique (KEY_,VERSION_, TENANT_ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PARENT
        foreign key (PARENT_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_EXE_SUPER on ACT_RU_EXECUTION(SUPER_EXEC_);
alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_SUPER
        foreign key (SUPER_EXEC_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_EXE_PROCDEF on ACT_RU_EXECUTION(PROC_DEF_ID_);
alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

create index ACT_IDX_TSKASS_TASK on ACT_RU_IDENTITYLINK(TASK_ID_);
alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_TSKASS_TASK
        foreign key (TASK_ID_)
            references ACT_RU_TASK (ID_);

create index ACT_IDX_ATHRZ_PROCEDEF  on ACT_RU_IDENTITYLINK(PROC_DEF_ID_);
alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_ATHRZ_PROCEDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

create index ACT_IDX_IDL_PROCINST on ACT_RU_IDENTITYLINK(PROC_INST_ID_);
alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_IDL_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_TASK_EXEC on ACT_RU_TASK(EXECUTION_ID_);
alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_EXE
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_TASK_PROCINST on ACT_RU_TASK(PROC_INST_ID_);
alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_TASK_PROCDEF on ACT_RU_TASK(PROC_DEF_ID_);
alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);


create index ACT_IDX_JOB_EXECUTION_ID on ACT_RU_JOB(EXECUTION_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_JOB_PROC_INST_ID on ACT_RU_JOB(PROCESS_INSTANCE_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_JOB_PROC_DEF_ID on ACT_RU_JOB(PROC_DEF_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

create index ACT_IDX_JOB_EXCEPTION on ACT_RU_JOB(EXCEPTION_STACK_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_TJOB_EXECUTION_ID on ACT_RU_TIMER_JOB(EXECUTION_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_TJOB_PROC_INST_ID on ACT_RU_TIMER_JOB(PROCESS_INSTANCE_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_TJOB_PROC_DEF_ID on ACT_RU_TIMER_JOB(PROC_DEF_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

create index ACT_IDX_TJOB_EXCEPTION on ACT_RU_TIMER_JOB(EXCEPTION_STACK_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_SJOB_EXECUTION_ID on ACT_RU_SUSPENDED_JOB(EXECUTION_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_SJOB_PROC_INST_ID on ACT_RU_SUSPENDED_JOB(PROCESS_INSTANCE_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_SJOB_PROC_DEF_ID on ACT_RU_SUSPENDED_JOB(PROC_DEF_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

create index ACT_IDX_SJOB_EXCEPTION on ACT_RU_SUSPENDED_JOB(EXCEPTION_STACK_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);


create index ACT_IDX_DJOB_EXECUTION_ID on ACT_RU_DEADLETTER_JOB(EXECUTION_ID_);
alter table ACT_RU_DEADLETTER_JOB add constraint ACT_FK_DJOB_EXECUTION  foreign key (EXECUTION_ID_)  references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_DJOB_PROC_INST_ID on ACT_RU_DEADLETTER_JOB(PROCESS_INSTANCE_ID_);
alter table ACT_RU_DEADLETTER_JOB add constraint ACT_FK_DJOB_PROCESS_INSTANCE  foreign key (PROCESS_INSTANCE_ID_)  references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_DJOB_PROC_DEF_ID on ACT_RU_DEADLETTER_JOB(PROC_DEF_ID_);
alter table ACT_RU_DEADLETTER_JOB   add constraint ACT_FK_DJOB_PROC_DEF  foreign key (PROC_DEF_ID_)  references ACT_RE_PROCDEF (ID_);

create index ACT_IDX_DJOB_EXCEPTION on ACT_RU_DEADLETTER_JOB(EXCEPTION_STACK_ID_);
alter table ACT_RU_DEADLETTER_JOB  add constraint ACT_FK_DJOB_EXCEPTION foreign key (EXCEPTION_STACK_ID_)  references ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_EVENT_SUBSCR on ACT_RU_EVENT_SUBSCR(EXECUTION_ID_);
alter table ACT_RU_EVENT_SUBSCR  add constraint ACT_FK_EVENT_EXEC  foreign key (EXECUTION_ID_)  references ACT_RU_EXECUTION(ID_);

create index ACT_IDX_MODEL_SOURCE on ACT_RE_MODEL(EDITOR_SOURCE_VALUE_ID_);
alter table ACT_RE_MODEL  add constraint ACT_FK_MODEL_SOURCE  foreign key (EDITOR_SOURCE_VALUE_ID_)  references ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_MODEL_SOURCE_EXTRA on ACT_RE_MODEL(EDITOR_SOURCE_EXTRA_VALUE_ID_);
alter table ACT_RE_MODEL  add constraint ACT_FK_MODEL_SOURCE_EXTRA  foreign key (EDITOR_SOURCE_EXTRA_VALUE_ID_)  references ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_MODEL_DEPLOYMENT on ACT_RE_MODEL(DEPLOYMENT_ID_);
alter table ACT_RE_MODEL  add constraint ACT_FK_MODEL_DEPLOYMENT  foreign key (DEPLOYMENT_ID_)   references ACT_RE_DEPLOYMENT (ID_);

create index ACT_IDX_PROCDEF_INFO_JSON on ACT_PROCDEF_INFO(INFO_JSON_ID_);
alter table ACT_PROCDEF_INFO  add constraint ACT_FK_INFO_JSON_BA   foreign key (INFO_JSON_ID_)   references ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_PROCDEF_INFO_PROC on ACT_PROCDEF_INFO(PROC_DEF_ID_);
alter table ACT_PROCDEF_INFO  add constraint ACT_FK_INFO_PROCDEF  foreign key (PROC_DEF_ID_)  references ACT_RE_PROCDEF (ID_);

alter table ACT_PROCDEF_INFO  add constraint ACT_UNIQ_INFO_PROCDEF  unique (PROC_DEF_ID_);


create table ACT_ID_GROUP (
  ID_ NVARCHAR2(64),
  REV_ INTEGER,
  NAME_ NVARCHAR2(255),
  TYPE_ NVARCHAR2(255),
  constraint pk_ACT_ID_GROUP PRIMARY KEY (ID_)
);

create table ACT_ID_MEMBERSHIP (
       USER_ID_ NVARCHAR2(64),
       GROUP_ID_ NVARCHAR2(64),
       primary key (USER_ID_, GROUP_ID_)
);

create table ACT_ID_USER (
 ID_ NVARCHAR2(64)  primary key ,
 REV_ INTEGER,
 FIRST_ NVARCHAR2(255),
 LAST_ NVARCHAR2(255),
 EMAIL_ NVARCHAR2(255),
 PWD_ NVARCHAR2(255),
 PICTURE_ID_ NVARCHAR2(64),
 constraint pk_ACT_ID_USER PRIMARY KEY (ID_)
);

create table ACT_ID_INFO (
 ID_ NVARCHAR2(64) ,
 REV_ INTEGER,
 USER_ID_ NVARCHAR2(64),
 TYPE_ NVARCHAR2(64),
 KEY_ NVARCHAR2(255),
 VALUE_ NVARCHAR2(255),
 PASSWORD_ CLOB,
 PARENT_ID_ NVARCHAR2(255),
 constraint pk_ACT_ID_INFO PRIMARY KEY (ID_)
);

create index ACT_IDX_MEMB_GROUP on ACT_ID_MEMBERSHIP(GROUP_ID_);
alter table ACT_ID_MEMBERSHIP  add constraint ACT_FK_MEMB_GROUP   foreign key (GROUP_ID_)  references ACT_ID_GROUP (ID_);

create index ACT_IDX_MEMB_USER on ACT_ID_MEMBERSHIP(USER_ID_);
alter table ACT_ID_MEMBERSHIP  add constraint ACT_FK_MEMB_USER  foreign key (USER_ID_)  references ACT_ID_USER (ID_);


create table ACT_HI_PROCINST (
     ID_ NVARCHAR2(64) not null,
     PROC_INST_ID_ NVARCHAR2(64) not null,
     BUSINESS_KEY_ NVARCHAR2(255),
     PROC_DEF_ID_ NVARCHAR2(64) not null,
     START_TIME_ TIMESTAMP(6) not null,
     END_TIME_ TIMESTAMP(6),
     DURATION_ DECIMAL(19,0),
     START_USER_ID_ NVARCHAR2(255),
     START_ACT_ID_ NVARCHAR2(255),
     END_ACT_ID_ NVARCHAR2(255),
     SUPER_PROCESS_INSTANCE_ID_ NVARCHAR2(64),
     DELETE_REASON_ NVARCHAR2(2000),
     TENANT_ID_ NVARCHAR2(255) default '',
     NAME_ NVARCHAR2(255),
     unique (PROC_INST_ID_),
     constraint pk_ACT_HI_PROCINST PRIMARY KEY (ID_)
);



create table ACT_HI_ACTINST (
    ID_ NVARCHAR2(64) not null,
    PROC_DEF_ID_ NVARCHAR2(64) not null,
    PROC_INST_ID_ NVARCHAR2(64) not null,
    EXECUTION_ID_ NVARCHAR2(64) not null,
    ACT_ID_ NVARCHAR2(255) not null,
    TASK_ID_ NVARCHAR2(64),
    CALL_PROC_INST_ID_ NVARCHAR2(64),
    ACT_NAME_ NVARCHAR2(255),
    ACT_TYPE_ NVARCHAR2(255) not null,
    ASSIGNEE_ NVARCHAR2(255),
    START_TIME_ TIMESTAMP(6) not null,
    END_TIME_ TIMESTAMP(6),
    DURATION_ DECIMAL(19,0),
    DELETE_REASON_ NVARCHAR2(2000),
    TENANT_ID_ NVARCHAR2(255) default '',
    constraint pk_ACT_HI_ACTINST PRIMARY KEY (ID_)
);

create table ACT_HI_TASKINST (
     ID_ NVARCHAR2(64) not null,
     PROC_DEF_ID_ NVARCHAR2(64),
     TASK_DEF_KEY_ NVARCHAR2(255),
     PROC_INST_ID_ NVARCHAR2(64),
     EXECUTION_ID_ NVARCHAR2(64),
     PARENT_TASK_ID_ NVARCHAR2(64),
     NAME_ NVARCHAR2(255),
     DESCRIPTION_ NVARCHAR2(2000),
     OWNER_ NVARCHAR2(255),
     ASSIGNEE_ NVARCHAR2(255),
     START_TIME_ TIMESTAMP(6) not null,
     CLAIM_TIME_ TIMESTAMP(6),
     END_TIME_ TIMESTAMP(6),
     DURATION_ DECIMAL(19,0),
     DELETE_REASON_ NVARCHAR2(2000),
     PRIORITY_ INTEGER,
     DUE_datetime_ TIMESTAMP(6),
     FORM_KEY_ NVARCHAR2(255),
     CATEGORY_ NVARCHAR2(255),
     TENANT_ID_ NVARCHAR2(255) default '',
     constraint pk_ACT_HI_TASKINST PRIMARY KEY (ID_)
);




create table ACT_HI_COMMENT (
    ID_ NVARCHAR2(64) not null,
    TYPE_ NVARCHAR2(255),
    TIME_ TIMESTAMP(6) not null,
    USER_ID_ NVARCHAR2(255),
    TASK_ID_ NVARCHAR2(64),
    PROC_INST_ID_ NVARCHAR2(64),
    ACTION_ NVARCHAR2(255),
    MESSAGE_ NVARCHAR2(2000),
    FULL_MSG_ CLOB,
    constraint pk_ACT_HI_COMMENT PRIMARY KEY (ID_)
);

create table ACT_HI_ATTACHMENT (
    ID_ NVARCHAR2(64) not null,
    REV_ INTEGER,
    USER_ID_ NVARCHAR2(255),
    NAME_ NVARCHAR2(255),
    DESCRIPTION_ NVARCHAR2(2000),
    TYPE_ NVARCHAR2(255),
    TASK_ID_ NVARCHAR2(64),
    PROC_INST_ID_ NVARCHAR2(64),
    URL_ NVARCHAR2(2000),
    CONTENT_ID_ NVARCHAR2(64),
    TIME_ TIMESTAMP(6),
    constraint pk_ACT_HI_ATTACHMENT PRIMARY KEY (ID_)
);


create table ACT_HI_IDENTITYLINK (
     ID_ NVARCHAR2(64),
     GROUP_ID_ NVARCHAR2(255),
     TYPE_ NVARCHAR2(255),
     USER_ID_ NVARCHAR2(255),
     TASK_ID_ NVARCHAR2(64),
     PROC_INST_ID_ NVARCHAR2(64),
     constraint pk_ACT_HI_IDENTITYLINK PRIMARY KEY (ID_)
);

create index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST(END_TIME_);
create index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST(BUSINESS_KEY_);
create index ACT_IDX_HI_ACT_INST_START on ACT_HI_ACTINST(START_TIME_);
create index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST(END_TIME_);

create index ACT_IDX_HI_IDENT_LNK_USER on ACT_HI_IDENTITYLINK(USER_ID_);
create index ACT_IDX_HI_IDENT_LNK_TASK on ACT_HI_IDENTITYLINK(TASK_ID_);
create index ACT_IDX_HI_IDENT_LNK_PROCINST on ACT_HI_IDENTITYLINK(PROC_INST_ID_);
create index ACT_IDX_HI_ACT_INST_PROCINST on ACT_HI_ACTINST(PROC_INST_ID_, ACT_ID_);
create index ACT_IDX_HI_ACT_INST_EXEC on ACT_HI_ACTINST(EXECUTION_ID_, ACT_ID_);
create index ACT_IDX_HI_TASK_INST_PROCINST on ACT_HI_TASKINST(PROC_INST_ID_);


CREATE TABLE biz_leave (
    id number(19) NOT NULL ,
    type varchar(20),
    title varchar2(100),
    reason varchar2(500),
    start_time date,
    end_time date,
    total_time number(11),
    instance_id varchar2(32),
    create_by varchar2(64),
    create_time date ,
    update_by varchar2(64),
    update_time date ,
    apply_user varchar2(64),
    apply_time date,
    reality_start_time date,
    reality_end_time date,
    constraint pk_biz_leave PRIMARY KEY (id)
);
comment on table  biz_leave         is '请假表';
comment on column biz_leave.id      is '主键ID';

comment on column biz_leave.type is '请假类型';
comment on column biz_leave.title is '标题';
comment on column biz_leave.reason is '原因';
comment on column biz_leave.start_time is '开始时间';
comment on column biz_leave.end_time is '结束时间';
comment on column biz_leave.total_time is  '请假时长，单位秒';
comment on column biz_leave.instance_id is '流程实例ID';
comment on column biz_leave.create_by is '创建者';
comment on column biz_leave.create_time is '创建时间';
comment on column biz_leave.update_by is  '更新者';
comment on column biz_leave.update_time is  '更新时间';
comment on column biz_leave.apply_user is  '申请人';
comment on column biz_leave.apply_time is  '申请时间';
comment on column biz_leave.reality_start_time is  '实际开始时间';
comment on column biz_leave.reality_end_time is  '实际结束时间';

CREATE TABLE ACCOUNT_REQ (
     ID_ VARCHAR2(100) NOT NULL ,
     NAME_ VARCHAR2(50),
     MOB_PHONE VARCHAR2(50),
     GENDER VARCHAR2(3) DEFAULT 0 ,
     DEPT VARCHAR2(50),
     AUDIT_DEPT VARCHAR2(50),
     MARK VARCHAR2(3) DEFAULT 0 ,
     EDUCATION VARCHAR2(3),
     BIRTH_PLACE VARCHAR2(100),
     JOB_ VARCHAR2(50),
     DIRECTOR VARCHAR2(50),
     ORDER_ VARCHAR2(7),
     BIRTHDAY VARCHAR2(50),
     OFFICE_PHONE VARCHAR2(50),
     EMAIL VARCHAR2(100),
     ADDRESS VARCHAR2(100),
     REMARK VARCHAR2(255),
     AUDIT_SUG VARCHAR2(255),
     DEAL_SUG VARCHAR2(255),
     CREATE_BY VARCHAR2(64),
     CREATE_TIME date ,
     UPdatetime_BY VARCHAR2(64) DEFAULT NULL ,
     UPdatetime_TIME date,
     STATUS VARCHAR2(3),
     AUDITOR VARCHAR2(50),
     DEALER VARCHAR2(50),
     OTHER_PHONE VARCHAR2(255),
     DEAL_DEPT VARCHAR2(255),
     REQ_TYPE VARCHAR2(2),
     PID VARCHAR2(100)
);

CREATE TABLE GROUP_REQ (
   ID_ VARCHAR2(100) NOT NULL ,
   NAME_ VARCHAR2(50),
   PHONE VARCHAR2(50),
   DEPT VARCHAR2(50),
   SYS VARCHAR2(50),
   NOTE VARCHAR2(255),
   DIRECTOR VARCHAR2(3),
   MARK VARCHAR2(3),
   AUDIT_DEPT VARCHAR2(50),
   AUDITOR VARCHAR2(50),
   DEAL_DEPT VARCHAR2(50),
   DEALER VARCHAR2(50),
   AUDIT_SUG VARCHAR2(255),
   DEAL_SUG VARCHAR2(255),
   STATUS VARCHAR2(3),
   CREATE_TIME date,
   CREATE_BY VARCHAR2(50),
   UPdatetime_TIME date,
   UPdatetime_BY VARCHAR2(50),
   REMARK VARCHAR2(255),
   REQ_TYPE VARCHAR2(2),
   GROUPID VARCHAR2(100)
);



create table FM_KIND
(
  fm_type_id        VARCHAR2(36) not null,
  name              VARCHAR2(64),
  sys_id            VARCHAR2(36),
  invalidation_mark VARCHAR2(2),
  knowledge_url     VARCHAR2(128),
  adder_id          VARCHAR2(36),
  updatetimer_id        VARCHAR2(36),
  add_time          VARCHAR2(16),
  updatetime_time       VARCHAR2(16),
  type              VARCHAR2(2),
  serial_num        VARCHAR2(8),
  parent_id         VARCHAR2(36)
);

  create table FM_SYS_D_TIME
(
  fm_sys_d_time_id VARCHAR2(36) not null,
  biz_id           VARCHAR2(36),
  sys_id           VARCHAR2(36),
  perform_name     VARCHAR2(64),
  deal_use_time    VARCHAR2(20),
  dealid           VARCHAR2(36),
  dealtime         VARCHAR2(20),
  group_id         VARCHAR2(32)
);

  create table YOUI_TASK_LOCK
(
  task_name  VARCHAR2(64) not null,
  lock_value VARCHAR2(2)
);


  create table CM_OS_RECOVERY_REGCOMMENTTER
(
  recovery_regCOMMENTter_id VARCHAR2(36) not null,
  centre_name          VARCHAR2(50),
  sys_id               VARCHAR2(36),
  sys_name             VARCHAR2(100),
  host_name            VARCHAR2(100),
  backup_datetime          VARCHAR2(50),
  recovery_start_time  VARCHAR2(50),
  recovery_end_time    VARCHAR2(50),
  recovery_time        VARCHAR2(50),
  verification_result  VARCHAR2(255),
  remark               VARCHAR2(500),
  data_size            VARCHAR2(50),
  type                 VARCHAR2(2),
  line_start           VARCHAR2(2),
  constraint pk_CM_OS_RECOVERY_REGCOMMENTTER primary key (recovery_regCOMMENTter_id)
);


create table CM_DB_RECOVERY_REGCOMMENTTER
(
  recovery_regCOMMENTter_id VARCHAR2(36) not null,
  centre_name          VARCHAR2(50),
  sys_id               VARCHAR2(36),
  sys_name             VARCHAR2(100),
  host_name            VARCHAR2(100),
  backup_datetime          VARCHAR2(50),
  recovery_start_time  VARCHAR2(50),
  recovery_end_time    VARCHAR2(50),
  recovery_time        VARCHAR2(50),
  verification_result  VARCHAR2(255),
  remark               VARCHAR2(500),
  data_size            VARCHAR2(50),
  type                 VARCHAR2(2),
  line_start           VARCHAR2(2),
  constraint pk_CM_DB_RECOVERY_REGCOMMENTTER primary key (recovery_regCOMMENTter_id)
);

-- 创建知识分类表
create table KNOWLEDGE_CATEGORY
(
  CATEGORY_ID   VARCHAR2(32) not null,
  CREATE_BY     VARCHAR2(32),
  CREATE_DATE   VARCHAR2(32),
  UPDATE_BY     VARCHAR2(32),
  UPDATE_DATE   VARCHAR2(32),
  CATEGORY_NAME VARCHAR2(32),
  PARENT_ID     VARCHAR2(32),
  REMARK        VARCHAR2(256),
  STATUS        VARCHAR2(2),
  EVENT_TYPE        VARCHAR2(2)
);

comment on table KNOWLEDGE_CATEGORY is '知识分类';
-- 创建知识收藏表
create table KNOWLEDGE_COLLECT
(
  ID         VARCHAR2(32) not null,
  CREATE_ID  VARCHAR2(32),
  CONTENT_ID VARCHAR2(32),
  STATUS     VARCHAR2(2),
  CREATE_TIME VARCHAR2(32)
);

comment on table KNOWLEDGE_COLLECT is '知识收藏';
comment on column KNOWLEDGE_COLLECT.ID is 'id';
comment on column KNOWLEDGE_COLLECT.CREATE_ID is '人员id';
comment on column KNOWLEDGE_COLLECT.CONTENT_ID is '知识id';
comment on column KNOWLEDGE_COLLECT.STATUS is '（0收藏，1推荐，2下载）';



 -- 创建知识评论表
create table KNOWLEDGE_COMMENT
(
  ID          VARCHAR2(32) not null,
  CREATE_ID   VARCHAR2(32),
  CONTENT_ID  VARCHAR2(32),
  CREATE_TIME VARCHAR2(32),
  REMARK      VARCHAR2(512),
  STATUS      VARCHAR2(2),
  REPLY_ID  VARCHAR2(32)
);

comment on table KNOWLEDGE_COMMENT is '知识评价';
comment on column KNOWLEDGE_COMMENT.ID is 'id';
comment on column KNOWLEDGE_COMMENT.CREATE_ID is '人员id';
comment on column KNOWLEDGE_COMMENT.CONTENT_ID is '知识id';
comment on column KNOWLEDGE_COMMENT.CREATE_TIME is '创建时间';
comment on column KNOWLEDGE_COMMENT.REMARK is '评价状态';
comment on column KNOWLEDGE_COMMENT.STATUS is '评价（0好评，1差评）';


-- 创建知识流程表
create table KNOWLEDGE_OPERATION_HISTORY
(
  ID         VARCHAR2(32),
  CONTENT_ID VARCHAR2(32),
  OPER_ID    VARCHAR2(32),
  OPER_TIME  VARCHAR2(32),
  OPERATION  VARCHAR2(2),
  REMARK     VARCHAR2(255)
);

comment on column KNOWLEDGE_OPERATION_HISTORY.CONTENT_ID is '对应知识id';
comment on column KNOWLEDGE_OPERATION_HISTORY.OPER_ID is '操作人员id';
comment on column KNOWLEDGE_OPERATION_HISTORY.OPER_TIME is '操作时间';
comment on column KNOWLEDGE_OPERATION_HISTORY.OPERATION is '操作内容（字典项KNOWLEDGE_OPER_TYPE）';
comment on column KNOWLEDGE_OPERATION_HISTORY.REMARK is '备注（操作原因）';

-- 创建知识标题表
create table KNOWLEDGE_TITIL
(
  ID          VARCHAR2(32) not null,
  NAME        VARCHAR2(64),
  CATEGORY      VARCHAR2(2),
  CREATE_TIME  VARCHAR2(20),
  CREATE_ID   VARCHAR2(32),
  PARENT_ID  VARCHAR2(32),
  EVENT_TYPE       VARCHAR2(200),
  SYS_ID  VARCHAR2(64),
  STATUS vachar2(2)
);

COMMENT ON TABLE KNOWLEDGE_TITIL IS '知识管理标题表';
COMMENT ON COLUMN KNOWLEDGE_TITIL.ID IS '标题id';
COMMENT ON COLUMN KNOWLEDGE_TITIL.NAME IS '标题';
COMMENT ON COLUMN KNOWLEDGE_TITIL.CATEGORY IS '标题等级';
COMMENT ON COLUMN KNOWLEDGE_TITIL.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN KNOWLEDGE_TITIL.CREATE_ID IS '创建人';
COMMENT ON COLUMN KNOWLEDGE_TITIL.EVENT_TYPE IS '事件类型';
COMMENT ON COLUMN KNOWLEDGE_TITIL.PARENT_ID IS '父id';

-- 创建知识访问表
create table KNOWLEDGE_VISITS
(
  ID         VARCHAR2(32) not null,
  CONTENT_ID VARCHAR2(32),
  CREATE_ID VARCHAR2(32),
  CREATE_TIME VARCHAR2(32)
);

comment on table KNOWLEDGE_VISITS is '访问历史';
comment on column KNOWLEDGE_VISITS.ID is 'id';
comment on column KNOWLEDGE_VISITS.CONTENT_ID is '知识id';
COMMENT ON COLUMN KNOWLEDGE_VISITS.CREATE_ID IS '创建人id';
COMMENT ON COLUMN KNOWLEDGE_VISITS.CREATE_TIME IS '创建时间';

------------------------wpd 2021/08/26 流程的日誌  （孙大永所需）
create table TEL_FLOW_LOG
(
  log_id         VARCHAR2(36) not null,
  tel_id         VARCHAR2(32),
  operation_name VARCHAR2(20),
  create_time    VARCHAR2(32),
  operator       VARCHAR2(32),
  operator_tel   VARCHAR2(20),
  state          VARCHAR2(16),
  serial_num     VARCHAR2(4)
);
-- Add comments to the columns
comment on column TEL_FLOW_LOG.log_id
  is 'ID';
comment on column TEL_FLOW_LOG.tel_id
  is '电话单id';
comment on column TEL_FLOW_LOG.operation_name
  is '操作名称';
comment on column TEL_FLOW_LOG.create_time
  is '操作时间';
comment on column TEL_FLOW_LOG.operator
  is '操作人';
comment on column TEL_FLOW_LOG.operator_tel
  is '联系方式';
comment on column TEL_FLOW_LOG.state
  is '状态';
comment on column TEL_FLOW_LOG.serial_num
  is '序号';
------tel_skill_org  技能组
create table TEL_SKILLS_ORG
(
  id                   VARCHAR2(36) not null,
  og_org_id            VARCHAR2(36),
  skills_group_telname VARCHAR2(36),
  skills_group_name    VARCHAR2(50),
  skills_status        VARCHAR2(2),
  create_time          VARCHAR2(36),
  updatetime_time          VARCHAR2(36),
  create_person        VARCHAR2(36)
);


--------------- 生产环境 cuicc 0826

-----------wangpingdu   2021/11/19-------------------
create table SYS_DEPLOYMENT_VERSION
(
  dv_id   VARCHAR2(36) not null,
  version_no           VARCHAR2(36),
  version_content           VARCHAR2(4000),
  sys_name     VARCHAR2(250),
  version_show    VARCHAR2(3),
  deployment_time           VARCHAR2(36),
  creater_id         VARCHAR2(20),
  constraint pk_SYS_DEPLOYMENT_VERSION primary key (dv_id)
);

  create table FM_NOTE
(
  note_id             VARCHAR2(50) not null,
  note_no             VARCHAR2(50),
  note_name           VARCHAR2(225),
  note_type           VARCHAR2(225),
  note_time           VARCHAR2(225),
  if_feedback         VARCHAR2(225),
  feedback_abort_time VARCHAR2(225),
  square              VARCHAR2(225),
  file_path           VARCHAR2(225),
  dCOMMENTpose_time        VARCHAR2(225),
  feedback_time       VARCHAR2(225),
  orgid               VARCHAR2(225),
  accepting_state     VARCHAR2(225),
  dCOMMENTpose_state       VARCHAR2(225),
  dCOMMENTpose_id          VARCHAR2(225),
  if_involve          VARCHAR2(225),
  feedback_detail     VARCHAR2(225),
  n1                  VARCHAR2(225),
  n2                  VARCHAR2(225),
  n3                  VARCHAR2(225),
  n4                  VARCHAR2(225),
  n5                  VARCHAR2(225),
  start_time          VARCHAR2(255),
  note_state          VARCHAR2(5),
  constraint pk_FM_NOTE primary key (note_id)
);

--创建知识库告警分析表
CREATE TABLE KNOWLEDGE_ALARM_EXAMPLE
   (
      ID VARCHAR2(32) NOT NULL,
	    TITLE VARCHAR2(255),
	    CONTENT VARCHAR2(255),
	    SECTITLE VARCHAR2(255),
	    NAME CLOB,
	    PRIMARY KEY (ID)
    );

COMMENT ON COLUMN KNOWLEDGE_ALARM_EXAMPLE.TITLE IS '告警名称=知识标题';
COMMENT ON COLUMN KNOWLEDGE_ALARM_EXAMPLE.CONTENT IS '对象=一级标题';
COMMENT ON COLUMN KNOWLEDGE_ALARM_EXAMPLE.SECTITLE IS '告警来源二级=二级标题';
COMMENT ON COLUMN KNOWLEDGE_ALARM_EXAMPLE.NAME IS '告警描述三级=标签';



create table cm_biz_single (
	change_id varchar2(32) not null ,
	change_code varchar2(36) null ,
	createtime varchar2(36) null ,
	application_submit_time varchar2(32) null ,
	change_category_id varchar2(32) null ,
	change_stage varchar2(32) null ,
	expect_start_time varchar2(32) null ,
	applicant_id varchar2(32) null ,
	change_type varchar2(32) null ,
	change_single_stauts varchar2(32) null ,
	important_lev varchar2(32) null ,
	expect_end_time varchar2(36) null ,
	closuer_often varchar2(32) null ,
	change_equipment varchar2(32) null ,
	equipment_flag varchar2(32) null ,
	group_person varchar2(32) null ,
	change_manager_id varchar2(32) null ,
	change_single_name varchar2(512) null ,
	change_cause_text varchar2(2048) null ,
	implement_supviser_id varchar2(32) null ,
	practicle_start varchar2(36) null ,
	practicle_end varchar2(36) null ,
	change_affect_desccription varchar2(32) null ,
	access_id varchar2(32) null ,
	notes_text varchar2(1500) null ,
	invalidation_mark varchar2(32) null ,
	implementor_id varchar2(32) null ,
	creater_org_id varchar2(32) null ,
	perform_result varchar2(2) null ,
	checker_id varchar2(32) null ,
	is_stop varchar2(2) null ,
	change_resource varchar2(2) null ,
	is_notice varchar2(2) null ,
	change_org varchar2(36) null ,
	implementor_orgid varchar2(36) null ,
	check_org varchar2(36) null ,
	is_r_system varchar2(2) null ,
	other_system varchar2(200) null ,
	is_stop_real varchar2(2) null ,
	sysname clob null ,
	sysid clob null ,
	xtchange_manager_id varchar2(512) null ,
	change_xtorg varchar2(32) null ,
	is_jjbg varchar2(2) null ,
	is_js varchar2(2) null ,
	change_reason varchar2(32) null ,
	fuchecker_id varchar2(32) null ,
	if_oasis varchar2(2) null ,
	if_auto varchar2(2) null ,
	if_mastart varchar2(2) null ,
	change_details clob null ,
	change_program clob null ,
	issue_id clob null ,
	fm_id clob null ,
	fuchecker_flag varchar2(2) null ,
	if_add_work varchar2(2) null ,
	not_auto_execution varchar2(2) null ,
	not_execution_text varchar2(400) null ,
	urgent_change_reason varchar2(2) null ,
	urgent_reason_text varchar2(400) null ,
	one_type_name varchar2(128) null ,
	two_type_name varchar2(128) null ,
	three_type_name varchar2(128) null ,
	four_type_name varchar2(128) null,
	primary key (change_id)
)
logging
nocompress
nocache
;


create table KNOWLEDGE_BUSINESS_EXAMPLE
   (
     ID                    VARCHAR2(32) not null,
     SYSTEM_NAME           VARCHAR2(255),
     CONTENT               VARCHAR2(255),
     SECTITLE              VARCHAR2(255),
     THREETITLE            VARCHAR2(255),
     NAME                  VARCHAR2(255),
     DESCRIBE              CLOB
   );

comment on column KNOWLEDGE_BUSINESS_EXAMPLE.SYSTEM_NAME is '业务系统名称';
comment on column KNOWLEDGE_BUSINESS_EXAMPLE.CONTENT is '业务一级标题';
comment on column KNOWLEDGE_BUSINESS_EXAMPLE.SECTITLE is '业务二级标题';
comment on column KNOWLEDGE_BUSINESS_EXAMPLE.THREETITLE is '业务三级标题';
comment on column KNOWLEDGE_BUSINESS_EXAMPLE.NAME is '业务关键字、标签';
comment on column KNOWLEDGE_BUSINESS_EXAMPLE.DESCRIBE is '业务描述';

-- 创建知识管理表
create table KNOWLEDGE_CONTENT
(
  ID          VARCHAR2(32) not null,
  TITLE       VARCHAR2(200),
  DESCRIBE    CLOB,
  CONTENT     VARCHAR2(32),
  CATEGORY_ID VARCHAR2(32),
  CREATE_ID   VARCHAR2(32),
  CREATE_TIME VARCHAR2(32),
  STATUS      VARCHAR2(2),
  AT_ID       VARCHAR2(32),
  FILENAME  VARCHAR2(50),
  SECTITLE  VARCHAR2(32),
  POWER_ID  VARCHAR2(2),
  NAME  VARCHAR2(400),
  EVENT_TYPE VARCHAR2(2),
  THREETITLE  VARCHAR2(32),
  SYS_ID VARCHAR2(200),
  REASON VARCHAR2(255),
  ONE_AUDITOR VARCHAR2(32),
  TWO_AUDITOR VARCHAR2(32)
);

comment on table KNOWLEDGE_CONTENT is '知识管理';
comment on column KNOWLEDGE_CONTENT.ID is 'id';
comment on column KNOWLEDGE_CONTENT.TITLE is '知识标题';
comment on column KNOWLEDGE_CONTENT.DESCRIBE is '知识描述';
comment on column KNOWLEDGE_CONTENT.CONTENT is '知识内容';
comment on column KNOWLEDGE_CONTENT.CATEGORY_ID is '类别id';
comment on column KNOWLEDGE_CONTENT.CREATE_ID is '创建人id';
comment on column KNOWLEDGE_CONTENT.CREATE_TIME is '创建时间';
comment on column KNOWLEDGE_CONTENT.STATUS is '状态（0启用，1停用）';
comment on column KNOWLEDGE_CONTENT.AT_ID is '附件id';
comment on column KNOWLEDGE_CONTENT.EVENT_TYPE is '事件类型';
comment on column KNOWLEDGE_CONTENT.THREETITLE is '三级标题';
comment on column KNOWLEDGE_CONTENT.SYS_ID is '系统名称';
comment on column KNOWLEDGE_CONTENT.REASON is '审核理由';
comment on column KNOWLEDGE_CONTENT.ONE_AUDITOR is '一线审核人';
comment on column KNOWLEDGE_CONTENT.TWO_AUDITOR is '二线审核人';


create table ACT_HI_VARINST (
    ID_ NVARCHAR2(64) not null,
    PROC_INST_ID_ NVARCHAR2(64),
    EXECUTION_ID_ NVARCHAR2(64),
    TASK_ID_ NVARCHAR2(64),
    NAME_ NVARCHAR2(255) not null,
    VAR_TYPE_ NVARCHAR2(100),
    REV_ INTEGER,
    BYTEARRAY_ID_ NVARCHAR2(64),
    DOUBLE_ NUMBER(*,10),
    LONG_ NUMBER(19,0),
    TEXT_ NVARCHAR2(2000),
    TEXT2_ NVARCHAR2(2000),
    CREATE_TIME_ TIMESTAMP(6),
    LAST_UPdatetimeD_TIME_ TIMESTAMP(6),
    constraint pk_ACT_HI_VARINST primary key (ID_)
);

create index ACT_IDX_HI_PROCVAR_PROC_INST on ACT_HI_VARINST(PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_NAME_TYPE on ACT_HI_VARINST(NAME_, VAR_TYPE_);
create index ACT_IDX_HI_PROCVAR_TASK_ID on ACT_HI_VARINST(TASK_ID_);



create table ACT_HI_DETAIL (
   ID_ NVARCHAR2(64) not null,
   TYPE_ NVARCHAR2(255) not null,
   PROC_INST_ID_ NVARCHAR2(64),
   EXECUTION_ID_ NVARCHAR2(64),
   TASK_ID_ NVARCHAR2(64),
   ACT_INST_ID_ NVARCHAR2(64),
   NAME_ NVARCHAR2(255) not null,
   VAR_TYPE_ NVARCHAR2(64),
   REV_ INTEGER,
   TIME_ TIMESTAMP(6) not null,
   BYTEARRAY_ID_ NVARCHAR2(64),
   DOUBLE_ NUMBER(*,10),
   LONG_ NUMBER(19,0),
   TEXT_ NVARCHAR2(2000),
   TEXT2_ NVARCHAR2(2000),
   constraint pk_ACT_HI_DETAIL primary key (ID_)
);

create index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL(PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL(TIME_);
create index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL(NAME_);
create index ACT_IDX_HI_DETAIL_TASK_ID on ACT_HI_DETAIL(TASK_ID_);
create index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL(ACT_INST_ID_);



create table ACT_RU_VARIABLE (
     ID_ NVARCHAR2(64) not null  primary key,
     REV_ INTEGER,
     TYPE_ NVARCHAR2(255) not null,
     NAME_ NVARCHAR2(255) not null,
     EXECUTION_ID_ NVARCHAR2(64),
     PROC_INST_ID_ NVARCHAR2(64),
     TASK_ID_ NVARCHAR2(64),
     BYTEARRAY_ID_ NVARCHAR2(64),
     DOUBLE_ NUMBER(*,10),
     LONG_ NUMBER(19,0),
     TEXT_ NVARCHAR2(2000),
     TEXT2_ NVARCHAR2(2000)
);

create index ACT_IDX_VARIABLE_TASK_ID on ACT_RU_VARIABLE(TASK_ID_);
create index ACT_IDX_VAR_EXE on ACT_RU_VARIABLE(EXECUTION_ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_EXE
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_VAR_PROCINST on ACT_RU_VARIABLE(PROC_INST_ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION(ID_);

create index ACT_IDX_VAR_BYTEARRAY on ACT_RU_VARIABLE(BYTEARRAY_ID_);
alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_BYTEARRAY
        foreign key (BYTEARRAY_ID_)
            references ACT_GE_BYTEARRAY (ID_);


-- 首页工作状态表 (6/28号已执行)
    create table ITSM_WORK_STATUS
    (
      work_id      VARCHAR2(32) not null,
      pid          VARCHAR2(32),
      user_id      VARCHAR2(32),
      work_status  VARCHAR2(2),
      operate_time VARCHAR2(64),
			constraint ITSM_WORK_STATUS primary key (work_id)
    );
    comment on column ITSM_WORK_STATUS.pid is '登录人id';
    comment on column ITSM_WORK_STATUS.user_id is '用户id';
    comment on column ITSM_WORK_STATUS.work_status is '工作状态';
    comment on column ITSM_WORK_STATUS.operate_time is '操作时间';
 -- 首页工作表状态记录表 （6/29）
    create table ITSM_WORK_STATUS_LOG
    (
      log_id       VARCHAR2(32) not null,
      work_id      VARCHAR2(32),
      pid          VARCHAR2(3),
      user_id      VARCHAR2(32),
      work_status  VARCHAR2(32),
      operate_time VARCHAR2(64),
	  constraint ITSM_WORK_STATUS_LOG primary key (LOG_ID)
    );
   comment on column ITSM_WORK_STATUS_LOG.work_id is '工作表id';
   comment on column ITSM_WORK_STATUS_LOG.pid is '登录人id';
   comment on column ITSM_WORK_STATUS_LOG.user_id is '用户id';
   comment on column ITSM_WORK_STATUS_LOG.work_status is '工作表状态';
   comment on column ITSM_WORK_STATUS_LOG.operate_time is '操作时间';


--
/*
客户信息表 llf 2022/1/5
*/
create table cust_info (
      c_id          varchar2(32)    not null,
      c_name        varchar2(48)    default null,
      c_phone       varchar2(12)    default null,
      c_dept        varchar2(50)    default null,
      c_post        varchar2(50)    default null,
      c_address     varchar2(200)    default null,
      create_time 	date,
      c_column      varchar2(50)    default null
);


alter table cust_info add constraint pk_cust_info primary key (c_id);

comment on table  cust_info              is '客户信息表';
comment on column cust_info.c_name       is '客户名称';
comment on column cust_info.c_phone      is '客户电话号码';
comment on column cust_info.c_dept       is '客户部门';
comment on column cust_info.c_post       is '客户职务';
comment on column cust_info.c_address    is '客户地址';
comment on column cust_info.create_time  is '创建时间';
comment on column cust_info.c_column     is '扩展字段';
commit;

/*
事件表
*/
CREATE TABLE FM_BIZ (
  FM_ID varchar2(36) not null ,
  CREATE_ID varchar2(36)  default null,
  CREATE_ORG_ID varchar2(36)  default null,
  GROUP_ID varchar2(36)  default null,
  DEALER_ID varchar2(36)  default null,
  DEAL_GROUP_ID varchar2(36)  default null,
  TYPEINFO_ID varchar2(36)  default null,
  SYSID varchar2(36)  default null,
  FAULT_NO varchar2(32)  default null,
  FAULT_SOURCE varchar2(2)  default null,
  SERIOUS_LEV varchar2(2)  default null,
  OCCURRENCE_ADDRESS varchar2(128)  default null,
  OCCUR_TIME varchar2(20)  default null,
  FAULT_REPORT_NAME varchar2(54)  default null,
  REPORT_TIME varchar2(20)  default null,
  REPORT_PHONE varchar2(32)  default null,
  FAULT_CONTACT_NAME varchar2(54)  default null,
  CONTACT_PHONE varchar2(24)  default null,
  CONTACT_ADDRESS varchar2(128)  default null,
  SERIAL_NUMBER varchar2(96)  default null,
  TRADING_NAME varchar2(96)  default null,
  ERROR_INFORMATION varchar2(192)  default null,
  FAULT_DECRIPT_SUMMARY varchar2(128)  default null,
  FAULT_DECRIPT_DETAIL varchar2(3000)  default null,
  CREAT_TIME varchar2(20)  default null,
  DEAL_TIME varchar2(20)  default null,
  DEAL_DESCRIPTION varchar2(2000)  default null,
  EVALUATE varchar2(512)  default null,
  EVALUATE_TIME varchar2(20)  default null,
  END_TIME varchar2(20)  default null,
  CURRENT_STATE varchar2(2)  default null,
  DEAL_USE_TIME varchar2(20)  default null,
  INVOLVE_AMOUNT varchar2(2)  default null,
  INVOLVE_SCOUNT varchar2(60)  default null,
  CUSTOMER_NAME varchar2(48)  default null,
  CUSTOMER_IDCARD varchar2(20)  default null,
  TRANSACTION_ACCOUNT varchar2(256)  default null,
  TRANSACTION_AMOUNT varchar2(108)  default null,
  ORDER_NUMBER varchar2(128)  default null,
  EVALUATE_RESULT varchar2(2)  default null,
  FM_CAUSE varchar2(64)  default null,
  FM_JKMARK varchar2(2)  default null,
  PARTICIPATOR_IDS varchar2(2000)  default null,
  DEAL_MODE varchar2(2)  default null,
  PARTICIPATE_GROUPIDS varchar2(2000)  default null,
  TO_QGZX_TIME varchar2(20)  default null,
  BACK_SZX_TIME varchar2(20)  default null,
  REPEAT_MARK varchar2(2)  default null,
  RECEIVER_IDS varchar2(2000)  default null,
  INVALIDATION_MARK varchar2(2)  default null,
  IF_JJ varchar2(2)  default null,
  IF_BULLETION varchar2(2)  default null,
  BULL_CAUSE varchar2(256)  default null,
  BULL_TIME varchar2(64)  default null,
  IS_ANTI_FRAUD varchar2(2)  default null,
  IS_SEND varchar2(2)  default null,
  INI_SYS varchar2(36)  default null,
  ONETYPE varchar2(36)  default null,
  TWOTYPE varchar2(36)  default null,
  THREETYPE varchar2(36)  default null,
  KEYWORDS varchar2(512)  default null,
  INI_ONETYPE varchar2(36)  default null,
  INI_TWOTYPE varchar2(36)  default null,
  INI_THREETYPE varchar2(36)  default null,
  INI_KEYWORDS varchar2(512)  default null,
  KNOWLEDGE_ID varchar2(512)  default null,
  if_yn varchar2(2)  default null ,
  c_id varchar2(36)  default null,
  record_type varchar2(2)  default null
);

alter table FM_BIZ add constraint pk_fm_biz primary key (FM_ID);

comment on table  FM_BIZ              is '业务事件单表';
comment on column  FM_BIZ.FM_ID              is '主键';
comment on column  FM_BIZ.CREATE_ID              is '创建人';
comment on column  FM_BIZ.CREATE_ORG_ID              is '创建机构';
comment on column  FM_BIZ.GROUP_ID              is '工作组';
comment on column  FM_BIZ.DEALER_ID              is '处理人';
comment on column  FM_BIZ.DEAL_GROUP_ID              is '处理组';
comment on column  FM_BIZ.TYPEINFO_ID              is '类别';
comment on column  FM_BIZ.SYSID              is '系统';
comment on column  FM_BIZ.FAULT_NO              is '事件单编号';
comment on column  FM_BIZ.FAULT_SOURCE              is '事件来源ID：1：手工，2：监控系统';
comment on column  FM_BIZ.SERIOUS_LEV              is '事件等级:1:一般,2:急,3紧急';
comment on column  FM_BIZ.OCCURRENCE_ADDRESS              is '发生地址';
comment on column  FM_BIZ.OCCUR_TIME              is '发生时间';
comment on column  FM_BIZ.FAULT_REPORT_NAME              is '报告人';
comment on column  FM_BIZ.REPORT_TIME              is '报告时间';
comment on column  FM_BIZ.REPORT_PHONE              is '报告人电话';
comment on column  FM_BIZ.FAULT_CONTACT_NAME              is '联系人';
comment on column  FM_BIZ.CONTACT_PHONE              is '联系人电话';
comment on column  FM_BIZ.CONTACT_ADDRESS              is '联系人地址';
comment on column  FM_BIZ.SERIAL_NUMBER              is '流水号';
comment on column  FM_BIZ.TRADING_NAME              is '交易名称';
comment on column  FM_BIZ.ERROR_INFORMATION              is '错误信息';
comment on column  FM_BIZ.FAULT_DECRIPT_SUMMARY              is '事件标题';
comment on column  FM_BIZ.FAULT_DECRIPT_DETAIL              is '事件描述';
comment on column  FM_BIZ.CREAT_TIME              is '创建时间';
comment on column  FM_BIZ.DEAL_TIME              is '处理时间';
comment on column  FM_BIZ.DEAL_DESCRIPTION              is '处理描述';
comment on column  FM_BIZ.EVALUATE              is '评价：1：满意，2：一般，3：不满意，用于运营事件单评价';
comment on column  FM_BIZ.EVALUATE_TIME              is '评价时间，用于运营事件单评价';
comment on column  FM_BIZ.CURRENT_STATE              is '当前状态: 1-“待提交”2—“作废”3-“待处理”4-“处理中”5-“待分派” 6—“待关闭”7—“已关闭”';
comment on column  FM_BIZ.INVOLVE_AMOUNT              is '涉事金额';
comment on column  FM_BIZ.INVOLVE_SCOUNT              is '涉事笔数';
comment on column  FM_BIZ.CUSTOMER_NAME              is '客户姓名';
comment on column  FM_BIZ.CUSTOMER_IDCARD              is '客户身份证号';
comment on column  FM_BIZ.TRANSACTION_ACCOUNT              is '交易账号';
comment on column  FM_BIZ.TRANSACTION_AMOUNT              is '交易金额';
comment on column  FM_BIZ.ORDER_NUMBER              is '订单号';
comment on column  FM_BIZ.EVALUATE_RESULT              is '评价结果：满意、不满意';
comment on column  FM_BIZ.FM_CAUSE              is '事件起因';
comment on column  FM_BIZ.FM_JKMARK              is '监控标记';
comment on column  FM_BIZ.PARTICIPATOR_IDS              is '参与处理人员ID,用于过滤我处理过的事件单';
comment on column  FM_BIZ.DEAL_MODE              is '处理方式：正常处理/疑难事件处理/多方配合处理/数据变更处理';
comment on column  FM_BIZ.PARTICIPATE_GROUPIDS              is '参与工作组ID,用于过滤工作组参与过的事件单';
comment on column  FM_BIZ.TO_QGZX_TIME              is '转全国中心时间';
comment on column  FM_BIZ.TO_QGZX_TIME              is '转全国中心时间';
comment on column  FM_BIZ.BACK_SZX_TIME              is '退回省中心时间';
comment on column  FM_BIZ.REPEAT_MARK              is '转发原因：1多方配合处理，2非本系统，用于统计省转全国中心选择的工作组是否正确';
comment on column  FM_BIZ.RECEIVER_IDS              is '领取人ID,用于统计人员领取的事件单数量';
comment on column  FM_BIZ.INVALIDATION_MARK              is '无效标志，0：无效，1：有效';
comment on column  FM_BIZ.IF_JJ              is '是否紧急';
comment on column  FM_BIZ.IF_BULLETION              is '是否通报';
comment on column  FM_BIZ.BULL_CAUSE              is '通报原因';
comment on column  FM_BIZ.BULL_TIME              is '通报时间';
comment on column  FM_BIZ.IS_ANTI_FRAUD              is '是否是反欺诈相关 1-是  0-否';
comment on column  FM_BIZ.IS_SEND              is '是否发送成功  1-是 0-否';
comment on column  FM_BIZ.INI_SYS              is '初始系统';
comment on column  FM_BIZ.ONETYPE              is '当前一级分类';
comment on column  FM_BIZ.TWOTYPE              is '当前二级分类';
comment on column  FM_BIZ.THREETYPE              is '当前三级分类';
comment on column  FM_BIZ.KEYWORDS              is '当前关键字';
comment on column  FM_BIZ.INI_ONETYPE              is '初始一级分类';
comment on column  FM_BIZ.INI_TWOTYPE              is '初始二级分类';
comment on column  FM_BIZ.INI_THREETYPE              is '初始三级分类';
comment on column  FM_BIZ.INI_KEYWORDS              is '初始关键字';
comment on column  FM_BIZ.KNOWLEDGE_ID              is '知识ID';
comment on column  FM_BIZ.c_id              is '客户id';
comment on column  FM_BIZ.record_type              is '事件单类型 1、事件单 2、咨询单';
commit;

/*
创建事件数据维护表
*/
create table fm_biz_maint (
      m_id          varchar2(32)    not null,
      m_title        varchar2(128)    default null,
      m_detail       varchar2(4000)    default null
);


alter table fm_biz_maint add constraint fm_biz_maint primary key (m_id);

comment on table  fm_biz_maint              is '事件维护表';
comment on column fm_biz_maint.m_title       is '事件标题';
comment on column fm_biz_maint.m_detail      is '事件描述';
commit;

/*
potal
*/
create table SYSTEM_APP
(
    id          VARCHAR2(64) not null,
    create_time TIMESTAMP(6) default sysdate,
    sysname     VARCHAR2(255),
    sysurl      VARCHAR2(255) not null,
    del_flag    NUMBER,
    update_time TIMESTAMP(6) default sysdate,
    param      VARCHAR2(255),
    syscode     VARCHAR2(255),
    sysorder    NUMBER,
    status      VARCHAR2(255)
);

alter table SYSTEM_APP add constraint pk_system_app primary key (id);

comment on table  SYSTEM_APP              is 'potal.系统维护';
comment on column SYSTEM_APP.id      is '部门主键seq_sys_dept.nextval';
comment on column SYSTEM_APP.create_time    is '创建时间';
comment on column SYSTEM_APP.sysname    is '系统名称';
comment on column SYSTEM_APP.sysurl    is '系统平台连接url';
comment on column SYSTEM_APP.del_flag    is '删除标识';
comment on column SYSTEM_APP.update_time       is '更新时间';
comment on column SYSTEM_APP.param        is '参数信息';
comment on column SYSTEM_APP.syscode        is '系统标识码';
comment on column SYSTEM_APP.sysorder        is '系统排序标识';
comment on column SYSTEM_APP.status       is '状态（0正常 1停用）';

/*
SYSTEM_APP_USER
*/
create table SYSTEM_APP_USER(
    id         VARCHAR2(64) not null,
    iappid      VARCHAR2(255),
    iuserid     VARCHAR2(255) not null,
    sysorder    NUMBER
);
alter table SYSTEM_APP_USER add constraint pk_system_app_user primary key (id);

/*
SYSTEM_APP_ROLE
*/
create table SYSTEM_APP_ROLE(
    iappid      varchar2(255) ,
    iROLEid     varchar2(255) not null
);

create table synchronize_user(
    id           NUMBER(11) not null,
    service_name VARCHAR2(255),
    service_url  VARCHAR2(2000) not null,
    json_param   VARCHAR2(2000),
    css_param   VARCHAR2(200),
    create_time  TIMESTAMP(6) default sysdate,
    syncFlag    NUMBER(11)
);
alter table synchronize_user add constraint pk_synchronize_user primary key (id);

/*
1、存储每一个已配置的 jobDetail 的详细信息
*/
create table qrtz_job_details (
    sched_name varchar2(120) not null,
    job_name  varchar2(200) not null,
    job_group varchar2(200) not null,
    description varchar2(250) null,
    job_class_name   varchar2(250) not null,
    is_durable varchar2(1) not null,
    is_nonconcurrent varchar2(1) not null,
    is_update_data varchar2(1) not null,
    requests_recovery varchar2(1) not null,
    job_data blob null,
    constraint qrtz_job_details_pk primary key (sched_name,job_name,job_group)
);
create index idx_qrtz_j_req_recovery on qrtz_job_details(sched_name,requests_recovery);
create index idx_qrtz_j_grp on qrtz_job_details(sched_name,job_group);
commit;

/*
2、存储已配置的 Trigger 的信息
*/
create table qrtz_triggers (
    sched_name varchar2(120) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    job_name  varchar2(200) not null,
    job_group varchar2(200) not null,
    description varchar2(250) null,
    next_fire_time number(13) null,
    prev_fire_time number(13) null,
    priority number(13) null,
    trigger_state varchar2(16) not null,
    trigger_type varchar2(8) not null,
    start_time number(13) not null,
    end_time number(13) null,
    calendar_name varchar2(200) null,
    misfire_instr number(2) null,
    job_data blob null,
    constraint qrtz_triggers_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_trigger_to_jobs_fk foreign key (sched_name,job_name,job_group)
      references qrtz_job_details(sched_name,job_name,job_group)
);
create index idx_qrtz_t_j on qrtz_triggers(sched_name,job_name,job_group);
create index idx_qrtz_t_jg on qrtz_triggers(sched_name,job_group);
create index idx_qrtz_t_c on qrtz_triggers(sched_name,calendar_name);
create index idx_qrtz_t_g on qrtz_triggers(sched_name,trigger_group);
create index idx_qrtz_t_state on qrtz_triggers(sched_name,trigger_state);
create index idx_qrtz_t_n_state on qrtz_triggers(sched_name,trigger_name,trigger_group,trigger_state);
create index idx_qrtz_t_n_g_state on qrtz_triggers(sched_name,trigger_group,trigger_state);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(sched_name,next_fire_time);
create index idx_qrtz_t_nft_st on qrtz_triggers(sched_name,trigger_state,next_fire_time);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_state);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_group,trigger_state);
commit;

/*
3、 存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数
*/
create table qrtz_simple_triggers (
    sched_name varchar2(120) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    repeat_count number(7) not null,
    repeat_interval number(12) not null,
    times_triggered number(10) not null,
    constraint qrtz_simple_trig_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_simple_trig_to_trig_fk foreign key (sched_name,trigger_name,trigger_group)
      references qrtz_triggers(sched_name,trigger_name,trigger_group)
);

/*
4、 存储 Cron Trigger，包括 Cron 表达式和时区信息
*/
create table qrtz_cron_triggers (
    sched_name varchar2(120) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    cron_expression varchar2(120) not null,
    time_zone_id varchar2(80),
    constraint qrtz_cron_trig_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_cron_trig_to_trig_fk foreign key (sched_name,trigger_name,trigger_group)
      references qrtz_triggers(sched_name,trigger_name,trigger_group)
);
/*
5、 Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
*/
create table qrtz_blob_triggers (
    sched_name varchar2(120) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    blob_data blob null,
    constraint qrtz_blob_trig_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_blob_trig_to_trig_fk foreign key (sched_name,trigger_name,trigger_group)
      references qrtz_triggers(sched_name,trigger_name,trigger_group)
);

/*
6、 以 Blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围
*/
create table qrtz_calendars (
    sched_name varchar2(120) not null,
    calendar_name  varchar2(200) not null,
    calendar blob not null,
    constraint qrtz_calendars_pk primary key (sched_name,calendar_name)
);

/*
7、 存储已暂停的 Trigger 组的信息
*/
create table qrtz_paused_trigger_grps (
    sched_name varchar2(120) not null,
    trigger_group  varchar2(200) not null,
    constraint qrtz_paused_trig_grps_pk primary key (sched_name,trigger_group)
);

/*
8、 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息
*/
create table qrtz_fired_triggers (
    sched_name varchar2(120) not null,
    entry_id varchar2(95) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    instance_name varchar2(200) not null,
    fired_time number(13) not null,
    sched_time number(13) not null,
    priority number(13) not null,
    state varchar2(16) not null,
    job_name varchar2(200) null,
    job_group varchar2(200) null,
    is_nonconcurrent varchar2(1) null,
    requests_recovery varchar2(1) null,
    constraint qrtz_fired_trigger_pk primary key (sched_name,entry_id)
);
create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(sched_name,instance_name);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(sched_name,instance_name,requests_recovery);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(sched_name,job_name,job_group);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(sched_name,job_group);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(sched_name,trigger_name,trigger_group);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(sched_name,trigger_group);
commit;

/*
9、 存储少量的有关 Scheduler 的状态信息，假如是用于集群中，可以看到其他的 Scheduler 实例
*/
create table qrtz_scheduler_state (
    sched_name varchar2(120) not null,
    instance_name varchar2(200) not null,
    last_checkin_time number(13) not null,
    checkin_interval number(13) not null,
    constraint qrtz_scheduler_state_pk primary key (sched_name,instance_name)
);

/*
10、 存储程序的悲观锁的信息(假如使用了悲观锁)
*/
create table qrtz_locks (
    sched_name varchar2(120) not null,
    lock_name  varchar2(40) not null,
    constraint qrtz_locks_pk primary key (sched_name,lock_name)
);

/*

*/
create table qrtz_simprop_triggers (
    sched_name varchar2(120) not null,
    trigger_name varchar2(200) not null,
    trigger_group varchar2(200) not null,
    str_prop_1 varchar2(512) null,
    str_prop_2 varchar2(512) null,
    str_prop_3 varchar2(512) null,
    int_prop_1 number(10) null,
    int_prop_2 number(10) null,
    long_prop_1 number(13) null,
    long_prop_2 number(13) null,
    dec_prop_1 numeric(13,4) null,
    dec_prop_2 numeric(13,4) null,
    bool_prop_1 varchar2(1) null,
    bool_prop_2 varchar2(1) null,
    constraint qrtz_simprop_trig_pk primary key (sched_name,trigger_name,trigger_group),
    constraint qrtz_simprop_trig_to_trig_fk foreign key (sched_name,trigger_name,trigger_group)
      references qrtz_triggers(sched_name,trigger_name,trigger_group)
);

/*
1、部门表
*/
create table sys_dept (
  dept_id           number      not null,
  parent_id         number      	default 0 ,
  ancestors         varchar2(50)     default '',
  dept_name         varchar2(30)     default '',
  order_num         number          default 0,
  leader            varchar2(20)     default null,
  phone             varchar2(11)     default null,
  email             varchar2(50)     default null,
  status            char(1)         default '0',
  del_flag          char(1)         default '0',
  create_by         varchar2(64)     default '',
  create_time 	    TIMESTAMP(6) 	default sysdate,
  update_by         varchar2(64)     default '',
  update_time       TIMESTAMP(6) 	default sysdate,
  deptlevel         varchar2(255),
  type         		varchar2(255),
  operaternum       varchar2(255),
  remark         	varchar2(255),
  tier         		varchar2(255),
  dept        		varchar2(255),
  branchmark        varchar2(255),
  inoutsidemark     varchar2(255),
  deptcode        	varchar2(255),
  constraint pk_sys_dept primary key (dept_id)
);
comment on table   sys_dept              is '部门表';
comment on column  sys_dept.dept_id              is '部门id';
comment on column  sys_dept.parent_id              is '父部门id';
comment on column  sys_dept.ancestors              is '祖级列表';
comment on column  sys_dept.dept_name              is '部门名称';
comment on column  sys_dept.order_num              is '显示顺序';
comment on column  sys_dept.leader              is '负责人';
comment on column  sys_dept.phone              is '联系电话';
comment on column  sys_dept.email              is '邮箱';
comment on column  sys_dept.status              is '部门状态（0正常 1停用）';
comment on column  sys_dept.del_flag              is '删除标志（0代表存在 2代表删除）';
comment on column  sys_dept.create_by              is '创建者';
comment on column  sys_dept.create_time              is '创建时间';
comment on column  sys_dept.update_by              is '更新者';
comment on column  sys_dept.update_time              is '更新时间';

/*
2、用户信息表
*/
create table sys_user (
  user_id           number      	not null,
  dept_id           number      	default null,
  login_name        varchar2(30)     not null,
  user_name         varchar2(30)     default '',
  user_type         varchar2(2)      default '00',
  email             varchar2(50)     default '',
  phonenumber       varchar2(11)     default '',
  sex               char(1)         default '0',
  avatar            varchar2(100)    default '',
  password          varchar2(50)     default '',
  salt              varchar2(20)     default '',
  status            char(1)         default '0',
  del_flag          char(1)         default '0',
  login_ip          varchar2(50)     default '',
  login_date        TIMESTAMP(6) 	default sysdate,
  pwd_update_date   TIMESTAMP(6) 	default sysdate,
  create_by         varchar2(64)     default '',
  create_time       TIMESTAMP(6) 	default sysdate,
  update_by         varchar2(64)     default '',
  update_time       TIMESTAMP(6) 	default sysdate,
  remark            varchar2(500)    default null,
  constraint pk_sys_user primary key (user_id)
);

comment on table   sys_user              		is '用户信息表';
comment on column  sys_user.user_id         	is '用户ID';
comment on column  sys_user.dept_id       		is '部门ID';
comment on column  sys_user.login_name        	is '登录账号';
comment on column  sys_user.user_name        	is '用户昵称';
comment on column  sys_user.user_type        	is '用户类型（00系统用户 01注册用户）';
comment on column  sys_user.email              	is '用户邮箱';
comment on column  sys_user.phonenumber      	is '手机号码';
comment on column  sys_user.sex              	is '用户性别（0男 1女 2未知）';
comment on column  sys_user.avatar              is '头像路径';
comment on column  sys_user.password          	is '密码';
comment on column  sys_user.salt              	is '盐加密';
comment on column  sys_user.status              is '帐号状态（0正常 1停用）';
comment on column  sys_user.del_flag          	is '删除标志（0代表存在 2代表删除）';
comment on column  sys_user.login_ip           	is '最后登录IP';
comment on column  sys_user.login_date         	is '最后登录时间';
comment on column  sys_user.pwd_update_date    	is '密码最后更新时间';
comment on column  sys_user.create_by        	is '创建者';
comment on column  sys_user.create_time        	is '创建时间';
comment on column  sys_user.update_by          	is '更新者';
comment on column  sys_user.update_time        	is '更新时间';
comment on column  sys_user.remark              is '备注';

/*
3、岗位信息表
*/
create table sys_post(
  post_id       number      	not null,
  post_code     varchar2(64)     not null,
  post_name     varchar2(50)     not null,
  post_sort     number          not null,
  status        char(1)         not null,
  create_by     varchar2(64)     default '',
  create_time   TIMESTAMP(6) 	default sysdate,
  update_by     varchar2(64)     default '',
  update_time   TIMESTAMP(6)	 default sysdate,
  remark        varchar2(500)    default null,
  constraint pk_TEL_SYSTEM_SUPPORTGROUP_NUMBER primary key (post_id)
) ;

comment on table   sys_post              		is '岗位信息表';
comment on column  sys_post.post_id         	is '岗位ID';
comment on column  sys_post.post_code         	is '岗位编码';
comment on column  sys_post.post_name         	is '岗位名称';
comment on column  sys_post.post_sort         	is '显示顺序';
comment on column  sys_post.status         		is '状态（0正常 1停用）';
comment on column  sys_post.create_by         	is '创建者';
comment on column  sys_post.create_time        	is '创建时间';
comment on column  sys_post.update_by         	is '更新者';
comment on column  sys_post.update_time    		is '更新时间';
comment on column  sys_post.remark         		is '备注';

/*
4、角色信息表
*/
create table sys_role (
  role_id           number      not null,
  role_name         varchar2(30)     not null,
  role_key          varchar2(100)    not null,
  role_sort         number          not null,
  data_scope        char(1)         default '1',
  status            char(1)         not null,
  del_flag          char(1)         default '0',
  create_by         varchar2(64)     default '',
  create_time       TIMESTAMP(6) default sysdate,
  update_by         varchar2(64)     default '',
  update_time       TIMESTAMP(6) default sysdate,
  remark            varchar2(500)    default null,
  constraint pk_sys_role primary key (role_id)
);

comment on table   sys_role              		is '角色信息表';
comment on column  sys_role.role_id         	is '角色ID';
comment on column  sys_role.role_name         	is '角色名称';
comment on column  sys_role.role_key         	is '角色权限字符串';
comment on column  sys_role.role_sort         	is '显示顺序';
comment on column  sys_role.data_scope         	is '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';
comment on column  sys_role.status         		is '角色状态（0正常 1停用）';
comment on column  sys_role.del_flag         	is '删除标志（0代表存在 2代表删除）';
comment on column  sys_role.create_by         	is '创建者';
comment on column  sys_role.create_time     	is '创建时间';
comment on column  sys_role.update_by         	is '更新者';
comment on column  sys_role.update_time      	is '更新时间';
comment on column  sys_role.remark         		is '备注';

/*
5、菜单权限表
*/
create table sys_menu (
  menu_id           number      not null ,
  menu_name         varchar2(50)     not null,
  parent_id         number      default 0 ,
  order_num         number          default 0 ,
  url               varchar2(200)    default '#',
  target            varchar2(20)     default '' ,
  menu_type         char(1)         default ''  ,
  visible           char(1)         default 0  ,
  perms             varchar2(100)    default null ,
  icon              varchar2(100)    default '#' ,
  create_by         varchar2(64)     default '' ,
  create_time       TIMESTAMP(6) default sysdate ,
  update_by         varchar2(64)     default '' ,
  update_time       TIMESTAMP(6) default sysdate  ,
  remark            varchar2(500)    default '',
  constraint pk_sys_menu primary key (menu_id)
);

comment on table   sys_menu              		is '菜单权限表';
comment on column  sys_menu.menu_id         	is '菜单ID';
comment on column  sys_menu.menu_name         	is '菜单名称';
comment on column  sys_menu.parent_id         	is '父菜单ID';
comment on column  sys_menu.order_num         	is '显示顺序';
comment on column  sys_menu.url         		is '请求地址';
comment on column  sys_menu.target         		is '打开方式（menuItem页签 menuBlank新窗口）';
comment on column  sys_menu.menu_type         	is '菜单类型（M目录 C菜单 F按钮）';
comment on column  sys_menu.visible         	is '菜单状态（0显示 1隐藏）';
comment on column  sys_menu.perms         		is '权限标识';
comment on column  sys_menu.icon         		is '菜单图标';
comment on column  sys_menu.create_by         	is '创建者';
comment on column  sys_menu.create_time 	  	is '创建时间';
comment on column  sys_menu.update_by         	is '更新者';
comment on column  sys_menu.update_time      	is '更新时间';
comment on column  sys_menu.remark         		is '备注';

/*
6、用户和角色关联表  用户N-1角色
*/
create table sys_user_role (
  user_id   number not null,
  role_id   number not null,
  constraint sys_user_role primary key (user_id,role_id)
);

comment on table   sys_user_role              		is '用户和角色关联表';
comment on column  sys_user_role.user_id         	is '用户ID';
comment on column  sys_user_role.role_id         	is '角色ID';

/*
7、角色和菜单关联表  角色1-N菜单
*/
create table sys_role_menu (
  role_id   number not null,
  menu_id   number not null,
  constraint sys_role_menu primary key (role_id,menu_id)
);

comment on table   sys_role_menu              		is '角色和菜单关联表';
comment on column  sys_role_menu.role_id         	is '角色ID';
comment on column  sys_role_menu.menu_id         	is '菜单ID';

/*
8、角色和部门关联表  角色1-N部门
*/
create table sys_role_dept (
  role_id   number not null,
  dept_id   number not null,
  constraint sys_role_dept primary key(role_id, dept_id)
);

comment on table   sys_role_dept              		is '角色和部门关联表';
comment on column  sys_role_dept.role_id         	is '角色ID';
comment on column  sys_role_dept.dept_id         	is '部门ID';

/*
9、用户与岗位关联表  用户1-N岗位
*/
create table sys_user_post(
  user_id   number not null,
  post_id  number not null,
  constraint sys_user_post primary key (user_id, post_id)
);

comment on table   sys_user_post              		is '用户与岗位关联表';
comment on column  sys_user_post.user_id         	is '用户ID';
comment on column  sys_user_post.post_id         	is '岗位ID';

/*
10、操作日志记录
*/
create table sys_oper_log (
  oper_id           number     not null,
  title             varchar2(50)     default '',
  business_type     number         default 0,
  method            varchar2(100)    default '',
  request_method    varchar2(10)     default '',
  operator_type     number         default 0,
  oper_name         varchar2(50)     default '',
  dept_name         varchar2(50)     default '',
  oper_url          varchar2(255)    default '',
  oper_ip           varchar2(50)     default '',
  oper_location     varchar2(255)    default '',
  oper_param        varchar2(2000)   default '',
  json_result       varchar2(4000)   default '',
  status           	number         default 0,
  error_msg         varchar2(2000)   default '',
  oper_time         TIMESTAMP(6) default sysdate,
  constraint pk_sys_oper_log primary key (oper_id)
);

comment on table   sys_oper_log              		is '操作日志记录';
comment on column  sys_oper_log.oper_id         	is '日志主键';
comment on column  sys_oper_log.title         		is '模块标题';
comment on column  sys_oper_log.business_type         	is '业务类型（0其它 1新增 2修改 3删除）';
comment on column  sys_oper_log.method         		is '方法名称';
comment on column  sys_oper_log.request_method         	is '请求方式';
comment on column  sys_oper_log.operator_type         	is '操作类别（0其它 1后台用户 2手机端用户）';
comment on column  sys_oper_log.oper_name         	is '操作人员';
comment on column  sys_oper_log.dept_name         	is '部门名称';
comment on column  sys_oper_log.oper_url         	is '请求URL';
comment on column  sys_oper_log.oper_ip         	is '主机地址';
comment on column  sys_oper_log.oper_location         	is '操作地点';
comment on column  sys_oper_log.oper_param         	is '请求参数';
comment on column  sys_oper_log.json_result         	is '返回参数';
comment on column  sys_oper_log.status         		is '操作状态（0正常 1异常）';
comment on column  sys_oper_log.error_msg         	is '错误消息';
comment on column  sys_oper_log.oper_time         	is '操作时间';

/*
11、字典类型表
*/
create table sys_dict_type(
  dict_id          number     not null,
  dict_name        varchar2(100)    default '',
  dict_type        varchar2(100)    default '',
  status           char(1)         default '0',
  create_by        varchar2(64)     default '',
  create_time      TIMESTAMP(6) default sysdate,
  update_by        varchar2(64)     default '',
  update_time      TIMESTAMP(6) default sysdate,
  remark           varchar2(500)    default null,
  constraint pk_sys_dict_type primary key (dict_id),
  unique (dict_type)
);

comment on table   sys_dict_type              		is '字典类型表';
comment on column  sys_dict_type.dict_id         	is '字典主键';
comment on column  sys_dict_type.dict_name         	is '字典名称';
comment on column  sys_dict_type.dict_type         	is '字典类型';
comment on column  sys_dict_type.status         	is '状态（0正常 1停用）';
comment on column  sys_dict_type.create_by         	is '创建者';
comment on column  sys_dict_type.create_time         	is '创建时间';
comment on column  sys_dict_type.update_by         	is '更新者';
comment on column  sys_dict_type.update_time         	is '更新时间';
comment on column  sys_dict_type.remark         	is '备注';

/*
12、字典数据表
*/
create table sys_dict_data(
  dict_code        number      not null,
  dict_sort        number         default 0,
  dict_label       varchar2(100)    default '',
  dict_value       varchar2(100)    default '',
  dict_type        varchar2(100)    default '',
  css_class        varchar2(100)    default null,
  list_class       varchar2(100)    default null,
  is_default       char(1)         default 'N',
  status           char(1)         default '0',
  create_by        varchar2(64)     default '',
  create_time      TIMESTAMP(6) default sysdate,
  update_by        varchar2(64)     default '',
  update_time      TIMESTAMP(6) default sysdate,
  remark           varchar2(500)    default null,
  constraint pk_sys_dict_data primary key (dict_code)
);

comment on table   sys_dict_data              		is '字典数据表';
comment on column  sys_dict_data.dict_code         	is '字典编码';
comment on column  sys_dict_data.dict_sort         	is '字典排序';
comment on column  sys_dict_data.dict_label         	is '字典标签';
comment on column  sys_dict_data.dict_value         	is '字典键值';
comment on column  sys_dict_data.dict_type         	is '字典类型';
comment on column  sys_dict_data.css_class         	is '样式属性（其他样式扩展）';
comment on column  sys_dict_data.list_class         	is '表格回显样式';
comment on column  sys_dict_data.is_default         	is '是否默认（Y是 N否）';
comment on column  sys_dict_data.status         	is '状态（0正常 1停用）';
comment on column  sys_dict_data.create_by         	is '创建者';
comment on column  sys_dict_data.create_time         	is '创建时间';
comment on column  sys_dict_data.update_by         	is '更新者';
comment on column  sys_dict_data.update_time         	is '更新时间';
comment on column  sys_dict_data.remark         	is '备注';

/*
13、参数配置表
*/
create table sys_config (
  config_id         number          not null,
  config_name       varchar2(100)    default '',
  config_key        varchar2(100)    default '',
  config_value      varchar2(500)    default '',
  config_type       char(1)         default 'N',
  create_by         varchar2(64)     default '',
  create_time       TIMESTAMP(6) default sysdate,
  update_by         varchar2(64)     default '',
  update_time       TIMESTAMP(6) default sysdate,
  remark            varchar2(500)    default null,
  constraint pk_sys_config primary key (config_id)
);

comment on table   sys_config              		is '参数配置表';
comment on column  sys_config.config_id         	is '参数主键';
comment on column  sys_config.config_name         	is '参数名称';
comment on column  sys_config.config_key         	is '参数键名';
comment on column  sys_config.config_value         	is '参数键值';
comment on column  sys_config.config_type         	is '系统内置（Y是 N否）';
comment on column  sys_config.create_by         	is '创建者';
comment on column  sys_config.create_time         	is '创建时间';
comment on column  sys_config.update_by         	is '更新者';
comment on column  sys_config.update_time         	is '更新时间';
comment on column  sys_config.remark         	is '备注';


/*
14、系统访问记录
*/
create table sys_logininfor (
  info_id        number     not null,
  login_name     varchar2(50)    default '',
  ipaddr         varchar2(50)    default '',
  login_location varchar2(255)   default '',
  browser        varchar2(50)    default '',
  os             varchar2(50)    default '',
  status         char(1)        default '0',
  msg            varchar2(255)   default '',
  login_time     TIMESTAMP(6) default sysdate,
  constraint pk_sys_logininfor primary key (info_id)
);

comment on table   sys_logininfor              		is '系统访问记录';
comment on column  sys_logininfor.info_id         	is '访问ID';
comment on column  sys_logininfor.login_name         	is '登录账号';
comment on column  sys_logininfor.ipaddr         	is '登录IP地址';
comment on column  sys_logininfor.login_location         	is '登录地点';
comment on column  sys_logininfor.browser         	is '浏览器类型';
comment on column  sys_logininfor.os         	is '操作系统';
comment on column  sys_logininfor.status         	is '登录状态（0成功 1失败）';
comment on column  sys_logininfor.msg         	is '提示消息';
comment on column  sys_logininfor.login_time         	is '访问时间';


/*
15、在线用户记录
*/
create table sys_user_online (
  sessionId         varchar2(50)   default '',
  login_name        varchar2(50)   default '',
  dept_name         varchar2(50)   default '',
  ipaddr            varchar2(50)   default '',
  login_location    varchar2(255)  default '',
  browser           varchar2(50)   default '',
  os                varchar2(50)   default '',
  status            varchar2(10)   default '',
  start_timestamp   TIMESTAMP(6) default sysdate,
  last_access_time  TIMESTAMP(6) default sysdate,
  expire_time       number       default 0,
  user_id			varchar2(32),
  constraint pk_sys_user_online primary key (sessionId)
);

comment on table   sys_user_online              		is '在线用户记录';
comment on column  sys_user_online.sessionId         	is '用户会话id';
comment on column  sys_user_online.login_name         	is '登录账号';
comment on column  sys_user_online.dept_name         	is '部门名称';
comment on column  sys_user_online.ipaddr         	is '登录IP地址';
comment on column  sys_user_online.login_location         	is '登录地点';
comment on column  sys_user_online.browser         	is '浏览器类型';
comment on column  sys_user_online.os         	is '操作系统';
comment on column  sys_user_online.status         	is '在线状态on_line在线off_line离线';
comment on column  sys_user_online.start_timestamp         	is 'session创建时间';
comment on column  sys_user_online.last_access_time         	is 'session最后访问时间';
comment on column  sys_user_online.expire_time         	is '超时时间，单位为分钟';
comment on column  sys_user_online.user_id         	is '人员id';

/*
16、定时任务调度表
*/
create sequence seq_sys_job
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;
create table sys_job (
  job_id              number    not null,
  job_name            varchar2(64)   default '',
  job_group           varchar2(64)   default 'DEFAULT',
  invoke_target       varchar2(500)  not null,
  cron_expression     varchar2(255)  default '',
  misfire_policy      varchar2(20)   default '3',
  concurrent          char(1)       default '1',
  status              char(1)       default '0',
  create_by           varchar2(64)   default '',
  create_time         TIMESTAMP(6) default sysdate,
  update_by           varchar2(64)   default '',
  update_time         TIMESTAMP(6) default sysdate,
  remark              varchar2(500)  default '',
  constraint pk_sys_job primary key (job_id, job_name, job_group)
);

comment on table   sys_job              		is '定时任务调度表';
comment on column  sys_job.job_id         	is '任务ID';
comment on column  sys_job.job_name         	is '任务名称';
comment on column  sys_job.job_group         	is '任务组名';
comment on column  sys_job.invoke_target         	is '调用目标字符串';
comment on column  sys_job.cron_expression         	is 'cron执行表达式';
comment on column  sys_job.misfire_policy         	is '计划执行错误策略（1立即执行 2执行一次 3放弃执行）';
comment on column  sys_job.concurrent         	is '是否并发执行（0允许 1禁止）';
comment on column  sys_job.status         	is '状态（0正常 1暂停）';
comment on column  sys_job.create_by         	is '创建者';
comment on column  sys_job.create_time         	is '创建时间';
comment on column  sys_job.update_by         	is '更新者';
comment on column  sys_job.update_time         	is '更新时间';
comment on column  sys_job.remark         	is '备注信息';


/*
17、定时任务调度日志表
*/
create sequence seq_sys_job_log
    increment by 1
    start with 1
    nomaxvalue
 nominvalue
 cache 20;
create table sys_job_log (
  job_log_id          number     not null,
  job_name            varchar2(64)    not null,
  job_group           varchar2(64)    not null,
  invoke_target       varchar2(500)   not null,
  job_message         varchar2(500),
  status              char(1)        default '0',
  exception_info      varchar2(2000)  default '',
  create_time         TIMESTAMP(6) default sysdate,
  method_name 		  varchar2(500) null ,
  method_params       varchar2(200) default null ,
  constraint pk_sys_job_log primary key (job_log_id)
);

comment on table   sys_job_log              		is '定时任务调度日志表';
comment on column  sys_job_log.job_log_id         	is '任务日志ID';
comment on column  sys_job_log.job_name         	is '任务名称';
comment on column  sys_job_log.job_group         	is '任务组名';
comment on column  sys_job_log.invoke_target         	is '调用目标字符串';
comment on column  sys_job_log.job_message         	is '日志信息';
comment on column  sys_job_log.status         	is '执行状态（0正常 1失败）';
comment on column  sys_job_log.exception_info         	is '异常信息';
comment on column  sys_job_log.create_time         	is '创建时间';


/*
18、通知公告表
*/
create sequence seq_sys_notice
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;

create table sys_notice (
  notice_id         number          not null,
  notice_title      varchar2(50)     not null,
  notice_type       char(1)         not null,
  notice_content    varchar2(2000)   default null,
  status            char(1)         default '0',
  create_by         varchar2(64)     default '',
  create_time       TIMESTAMP(6) default sysdate,
  update_by         varchar2(64)     default '',
  update_time       TIMESTAMP(6) default sysdate,
  remark            varchar2(255)    default null,
  constraint pk_sys_notice primary key (notice_id)
);

comment on table   sys_notice              		is '通知公告表';
comment on column  sys_notice.notice_id         	is '公告ID';
comment on column  sys_notice.notice_title         	is '公告标题';
comment on column  sys_notice.notice_type         	is '公告类型（1通知 2公告）';
comment on column  sys_notice.notice_content         	is '公告内容';
comment on column  sys_notice.status         	is '公告状态（0正常 1关闭）';
comment on column  sys_notice.create_by         	is '创建者';
comment on column  sys_notice.create_time         	is '创建时间';
comment on column  sys_notice.update_by         	is '更新者';
comment on column  sys_notice.update_time         	is '更新时间';
comment on column  sys_notice.remark         	is '备注';


/*
19、代码生成业务表
*/
create sequence seq_gen_table
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;
create table gen_table (
  table_id             number      not null,
  table_name           varchar2(200)    default '',
  table_comment        varchar2(500)    default '',
  sub_table_name       varchar2(64)     default null,
  sub_table_fk_name    varchar2(64)     default null,
  class_name           varchar2(100)    default '',
  tpl_category         varchar2(200)    default 'crud',
  package_name         varchar2(100),
  module_name          varchar2(30),
  business_name        varchar2(30),
  function_name        varchar2(50),
  function_author      varchar2(50),
  gen_type             char(1)         default '0',
  gen_path             varchar2(200)    default '/',
  options              varchar2(1000),
  create_by            varchar2(64)     default '',
  create_time 	       TIMESTAMP(6) default sysdate,
  update_by            varchar2(64)     default '',
  update_time          TIMESTAMP(6) default sysdate,
  remark               varchar2(500)    default null,
  constraint pk_gen_table primary key (table_id)
);

comment on table   gen_table              		is '代码生成业务表';
comment on column  gen_table.table_id         	is '编号';
comment on column  gen_table.table_name         	is '表名称';
comment on column  gen_table.table_comment         	is '表描述';
comment on column  gen_table.sub_table_name         	is '关联子表的表名';
comment on column  gen_table.sub_table_fk_name         	is '子表关联的外键名';
comment on column  gen_table.class_name         	is '实体类名称';
comment on column  gen_table.tpl_category         	is '使用的模板（crud单表操作 tree树表操作 sub主子表操作）';
comment on column  gen_table.package_name         	is '生成包路径';
comment on column  gen_table.module_name         	is '生成模块名';
comment on column  gen_table.business_name         	is '生成业务名';
comment on column  gen_table.function_name         	is '生成功能名';
comment on column  gen_table.function_author         	is '生成功能作者';
comment on column  gen_table.gen_type         	is '生成代码方式（0zip压缩包 1自定义路径）';
comment on column  gen_table.gen_path         	is '生成路径（不填默认项目路径）';
comment on column  gen_table.options         	is '其它生成选项';
comment on column  gen_table.create_by         	is '创建者';
comment on column  gen_table.create_time         	is '创建时间';
comment on column  gen_table.update_by         	is '更新者';
comment on column  gen_table.update_time         	is '更新时间';
comment on column  gen_table.remark         	is '备注';


/*
20、代码生成业务表字段
*/
create sequence seq_gen_table_column
    increment by 1
    start with 100
    nomaxvalue
 nominvalue
 cache 20;
create table gen_table_column (
  column_id         number      not null,
  table_id          varchar2(64),
  column_name       varchar2(200),
  column_comment    varchar2(500),
  column_type       varchar2(100),
  java_type         varchar2(500),
  java_field        varchar2(200),
  is_pk             char(1),
  is_increment      char(1),
  is_required       char(1),
  is_insert         char(1),
  is_edit           char(1),
  is_list           char(1),
  is_query          char(1),
  query_type        varchar2(200)    default 'EQ',
  html_type         varchar2(200),
  dict_type         varchar2(200)    default '',
  sort              number,
  create_by         varchar2(64)     default '',
  create_time 	    TIMESTAMP(6) default sysdate,
  update_by         varchar2(64)     default '',
  update_time       TIMESTAMP(6) default sysdate,
  constraint pk_gen_table_column primary key (column_id)
) ;

comment on table   gen_table_column              		is '代码生成业务表字段';
comment on column  gen_table_column.column_id         	is '编号';
comment on column  gen_table_column.table_id         	is '归属表编号';
comment on column  gen_table_column.column_name         	is '列名称';
comment on column  gen_table_column.column_comment         	is '列描述';
comment on column  gen_table_column.column_type         	is '列类型';
comment on column  gen_table_column.java_type         	is 'JAVA类型';
comment on column  gen_table_column.java_field         	is 'JAVA字段名';
comment on column  gen_table_column.is_pk         	is '是否主键（1是）';
comment on column  gen_table_column.is_increment         	is '是否自增（1是）';
comment on column  gen_table_column.is_required         	is '是否必填（1是）';
comment on column  gen_table_column.is_insert         	is '是否为插入字段（1是）';
comment on column  gen_table_column.is_edit         	is '是否编辑字段（1是）';
comment on column  gen_table_column.is_list         	is '是否列表字段（1是）';
comment on column  gen_table_column.is_query         	is '是否查询字段（1是）';
comment on column  gen_table_column.query_type         	is '查询方式（等于、不等于、大于、小于、范围）';
comment on column  gen_table_column.html_type         	is '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
comment on column  gen_table_column.dict_type         	is '字典类型';
comment on column  gen_table_column.sort         	is '排序';
comment on column  gen_table_column.create_by         	is '创建者';
comment on column  gen_table_column.create_time         	is '创建时间';
comment on column  gen_table_column.update_by         	is '更新者';
comment on column  gen_table_column.update_time         	is '更新时间';


/*
业务事件单考核超时表
*/
create table FM_BIZ_OVERTIME(
  id            VARCHAR(36) not null,
  singel_number VARCHAR(36),
  datetime      VARCHAR(20),
  system_id     VARCHAR(36),
  proportion    VARCHAR(36),
  ifjjtype      VARCHAR(2),
  constraint pk_FM_BIZ_OVERTIME primary key (id)
);

comment on table   FM_BIZ_OVERTIME              		is '业务事件单考核超时表';
comment on column  FM_BIZ_OVERTIME.id         			is '主键';
comment on column  FM_BIZ_OVERTIME.singel_number      	is '单号';
comment on column  FM_BIZ_OVERTIME.datetime         	is '日期';
comment on column  FM_BIZ_OVERTIME.system_id         	is '所涉系统';
comment on column  FM_BIZ_OVERTIME.proportion         	is '所占权重';
comment on column  FM_BIZ_OVERTIME.ifjjtype         	is '是否紧急  1是  2否';

/*
2021业务事件考核表
*/
create table FM_BIZ_KH(
  id             VARCHAR(36) not null,
  sysid          VARCHAR(36),
  datetime       VARCHAR(20),
  completecount  VARCHAR(20),
  jjcount        VARCHAR(20),
  fjjcount       VARCHAR(20),
  satisfaction   VARCHAR(20),
  unsatisfactory VARCHAR(20),
  constraint pk_FM_BIZ_KH primary key (id)
);

comment on table   FM_BIZ_KH              		is '2021业务事件考核表';
comment on column  FM_BIZ_KH.id         			is 'id';
comment on column  FM_BIZ_KH.sysid         			is '系统id';
comment on column  FM_BIZ_KH.datetime         			is '考核日期';
comment on column  FM_BIZ_KH.completecount         			is '关闭事件单数量';
comment on column  FM_BIZ_KH.jjcount         			is '紧急事件单超时数量';
comment on column  FM_BIZ_KH.fjjcount         			is '非紧急事件单超时数量';
comment on column  FM_BIZ_KH.satisfaction         			is '满意率';
comment on column  FM_BIZ_KH.unsatisfactory         			is '不满意率';

/*

*/
create table FM_BIZ_SCRIPT(
	fbs_id				VARCHAR(36)		not null,
	fm_id				VARCHAR(36),
	executor_id			VARCHAR(36),
	executor_groupid	VARCHAR(36),
	executor_result		VARCHAR(2048),
	result_status		VARCHAR(32),
	script_name			VARCHAR(256),
	script_para			VARCHAR(1024),
	script_id			VARCHAR(128),
	executor_time		VARCHAR(36),
	executor_endtime	VARCHAR(32),
	flow_id				VARCHAR(32),
	type				VARCHAR(2),
	script_version		VARCHAR(32),
	constraint pk_FM_BIZ_SCRIPT primary key (fbs_id)
);

comment on table   FM_BIZ_SCRIPT              		is '2021业务事件考核表';
comment on column  FM_BIZ_SCRIPT.fbs_id         			is '主键ID';
comment on column  FM_BIZ_SCRIPT.fm_id         			is '业务事件单ID';
comment on column  FM_BIZ_SCRIPT.executor_id         			is '执行人ID';
comment on column  FM_BIZ_SCRIPT.executor_groupid         			is '执行工作组ID';
comment on column  FM_BIZ_SCRIPT.executor_result         			is '执行结果';
comment on column  FM_BIZ_SCRIPT.result_status         			is '执行状态';
comment on column  FM_BIZ_SCRIPT.script_name         			is '脚本名称';
comment on column  FM_BIZ_SCRIPT.script_para         			is '脚本参数';
comment on column  FM_BIZ_SCRIPT.script_id         			is '脚本ID';
comment on column  FM_BIZ_SCRIPT.executor_time         			is '执行时间';
comment on column  FM_BIZ_SCRIPT.executor_endtime         			is '执行结束时间';
comment on column  FM_BIZ_SCRIPT.flow_id         			is '执行任务ID';

-- 添加知识与问题单关联表
CREATE TABLE KNOWLEDGE_TO_ISSUEFX  (
     KNOWLEDGE_ID VARCHAR2(32),
     ISSUEFX_NO VARCHAR2(32),
     ISSUEFX_ID VARCHAR2(32)
);


-- 公告通知表
CREATE TABLE AM_BIZ_NOTICE (
	am_biz_id varchar2(36) not null,
	am_code varchar2(72) null,
	checker_id varchar2(36) null,
	create_id varchar2(36) null,
	description varchar2(2000) null,
	release_date varchar2(32) null,
	release_org varchar2(36) null,
	checker_flag varchar2(2) null,
	checker_advice varchar2(128) null,
	invalidation_mark varchar2(2) null,
	checker_time varchar2(32) null,
	send_range varchar2(36) null,
	am_title varchar2(80) null ,
	current_status varchar2(2),
	is_top varchar2(2) null,
	top_time varchar2(72) null,
	create_time varchar2(72) null,
	top_time_flag varchar2(2) null,
	am_offline varchar2(2) null ,
	am_offline_time varchar2(72) null
);

comment on table AM_BIZ_NOTICE is '公告通知表';
comment on column  AM_BIZ_NOTICE.am_biz_id is '公告id';
comment on column  AM_BIZ_NOTICE.am_code is '公告编码';
comment on column  AM_BIZ_NOTICE.checker_id is '审核人id';
comment on column  AM_BIZ_NOTICE.create_id is '创建人';
comment on column  AM_BIZ_NOTICE.description is '描述';
comment on column  AM_BIZ_NOTICE.release_date is '发布时间';
comment on column  AM_BIZ_NOTICE.release_org is '发布机构';
comment on column  AM_BIZ_NOTICE.checker_flag is '审核标志';
comment on column  AM_BIZ_NOTICE.checker_advice is '审核意见';
comment on column  AM_BIZ_NOTICE.invalidation_mark is '有效标志';
comment on column  AM_BIZ_NOTICE.checker_time is '审核时间';
comment on column  AM_BIZ_NOTICE.send_range is '发送范围 1 数据中心 2 厂商+数据中心（72号院） 3 所有用户';
comment on column  AM_BIZ_NOTICE.am_title is '公告标题';
comment on column  AM_BIZ_NOTICE.current_status is '状态 01新建、02待审核、03=退回,04=待阅,05=已过期';
comment on column  AM_BIZ_NOTICE.is_top is '是否置顶  0 否 1 是';
comment on column  AM_BIZ_NOTICE.top_time is '置顶时间';
comment on column  AM_BIZ_NOTICE.create_time is '创建时间';
comment on column  AM_BIZ_NOTICE.top_time_flag is '置顶时间flag 回显用 1 一天 /2 三天 /3 一周 /4 一月 /5 长期';
comment on column  AM_BIZ_NOTICE.am_offline is '公告下线类型 (1、一天 2、一周 3、一个月 4、三个月 5、六个月 6、一年)';
comment on column  AM_BIZ_NOTICE.am_offline_time is '公告下线时间';

CREATE TABLE ID_GENERATOR (
    ID VARCHAR2(32) NOT NULL ,
    CURRENT_ID NUMBER(32) NOT NULL ,
    BIZ_TYPE VARCHAR2(32) NOT NULL ,
    CUR_DATE VARCHAR2(32) NOT NULL
);

comment on table ID_GENERATOR is '单号关联表';

CREATE TABLE DUTY_SCHEDULING (
	SCHEDULING_ID VARCHAR2(32) NOT NULL ,
	PID VARCHAR2(1024) NULL ,
	PNAME VARCHAR2(512) NULL ,
	MOBILE_PHONE VARCHAR2(512) NULL ,
	DUTY_DATE VARCHAR2(32) NULL ,
	TIME_SHIFT VARCHAR2(2) NULL ,
	COMMAND_HALL VARCHAR2(2) NULL ,
	TYPE_NO VARCHAR2(32) NULL ,
	TYPEINFO_ID VARCHAR2(32) NULL ,
	LEADER VARCHAR2(2) NULL ,
	ACCOUNT_PID VARCHAR2(32) NULL
) comment '值班安排表';


CREATE TABLE SM_BIZ_SCHEDULING (
	SCHEDULING_ID VARCHAR2(36) NOT NULL ,
	SCHEDULING_NO VARCHAR2(32) NULL ,
	SCHEDULING_NAME VARCHAR2(128) NULL ,
	SCHEDULING_DESCRIPTION CLOB NULL ,
	CREATE_TIME VARCHAR2(32) NULL ,
	EFFECTIVE_TIME VARCHAR2(32) NULL ,
	CREATOR_ID VARCHAR2(36) NULL ,
	CREATOR_DEPT_ID VARCHAR2(36) NULL ,
	RECHECK_MARK VARCHAR2(2) NULL ,
	RECHECKER_ID VARCHAR2(36) NULL ,
	TASK_NUM VARCHAR2(32) NULL ,
	UPDATE_TIME VARCHAR2(32) NULL ,
	INVALIDATION_MARK VARCHAR2(2) NULL ,
	PLAN_STATUS VARCHAR2(2) NULL ,
	PROCESS VARCHAR2(64) NULL ,
	PLAN_TYPE VARCHAR2(2) NULL ,
	CHECK_PERSON_ID VARCHAR2(4000) NULL ,
	CHECK_PERSON_NAME CLOB NULL ,
	CHECK_DESCRIPTION CLOB NULL ,
	MASTER_ORG_ID VARCHAR2(32) NULL ,
	CHECK_TIME VARCHAR2(32) NULL ,
	PLAN_CHECKPASS VARCHAR2(32) NULL ,
	CANCEL_FLAG VARCHAR2(2) NULL ,
	FOLDER VARCHAR2(36) NULL
);
CREATE INDEX SM_BIZ_S_IDX_CREATOR_ID ON SM_BIZ_SCHEDULING (CREATOR_ID ASC);
CREATE INDEX SM_BIZ_S_IDX_PLAN_STATUS ON SM_BIZ_SCHEDULING (PLAN_STATUS ASC);
CREATE INDEX SM_BIZ_S_IDX_SCHEDULING_NO ON SM_BIZ_SCHEDULING (SCHEDULING_NO ASC);

CREATE TABLE SM_BIZ_FOLDER (
    ID_ VARCHAR2(36) NOT NULL ,
    NAME_ VARCHAR2(128) NULL ,
    PATH_ VARCHAR2(1024) NULL ,
    PARENT_ VARCHAR2(36) NULL ,
    DESCRIPTION_ VARCHAR2(1024) NULL ,
    CREATE_USER_ VARCHAR2(32) NULL ,
    CREATE_TIME_ VARCHAR2(20) NULL ,
    MODIFY_USER_ VARCHAR2(32) NULL ,
    MODIFY_TIME_ VARCHAR2(20) NULL ,
    UPDATE_TIME_ VARCHAR2(20) NULL ,
    LEAF_ VARCHAR2(20) NULL ,
    TYPE VARCHAR2(2) NULL
);

CREATE TABLE SM_BIZ_TASK (
    TASK_ID VARCHAR2(36) NOT NULL primary key,
    SCHEDULING_ID VARCHAR2(36) NULL ,
    TASK_NO VARCHAR2(32) NULL ,
    PERFORM_USER_NAME CLOB NULL ,
    HANDLER_DEPT_NAME CLOB NULL ,
    TASK_TITLE VARCHAR2(128) NULL ,
    TASK_DESCRIPTION VARCHAR2(2048) NULL ,
    TASK_EFFECT_DATE VARCHAR2(32) NULL ,
    TASK_FAILURE_DATE VARCHAR2(32) NULL ,
    CHARACTERS VARCHAR2(2) NULL ,
    TASK_TYPE_ID VARCHAR2(36) NULL ,
    START_TIME VARCHAR2(32) NULL ,
    TASK_HOURS_WORKED VARCHAR2(64) NULL ,
    TASK_CYCLE VARCHAR2(32) NULL ,
    TASK_FREQUENCY VARCHAR2(32) NULL ,
    UPDATE_TIME VARCHAR2(32) NULL ,
    PERFORM_TIME VARCHAR2(32) NULL ,
    INVALIDATION_MARK VARCHAR2(2) NULL ,
    WORK_STATUS VARCHAR2(2) NULL ,
    CREATE_TIME VARCHAR2(32) NULL ,
    DUTY_POST_NAME CLOB NULL ,
    PROCESS VARCHAR2(64) NULL ,
    CHECKER_ID VARCHAR2(36) NULL ,
    PERFORM_USER_ID VARCHAR2(4000) NULL ,
    HANDLER_DEPT_ID VARCHAR2(4000) NULL ,
    DUTY_POST_ID VARCHAR2(4000) NULL ,
    HOUR VARCHAR2(4) NULL ,
    MIN VARCHAR2(4) NULL ,
    PERFORM_WEEKS VARCHAR2(128) NULL ,
    PERFORM_MONTHS VARCHAR2(128) NULL ,
    PERFORM_DAY VARCHAR2(8) NULL ,
    CREATE_ORG_ID VARCHAR2(36) NULL ,
    MSG_DOOR VARCHAR2(2) NULL ,
    MSG_HOUR VARCHAR2(4) NULL ,
    MSG_MIN VARCHAR2(4) NULL ,
    SEND_RANGE VARCHAR2(36) NULL ,
    DAY VARCHAR2(4) NULL ,
    RECEIVE_ROLE_NAME CLOB NULL ,
    RECEIVE_ROLE_ID VARCHAR2(2000) NULL ,
    PERFORM_DEPT_NAME CLOB NULL ,
    PERFORM_DEPT_ID CLOB NULL ,
    PERFORM_GROUP_ID CLOB NULL ,
    PERFORM_GROUP_NAME CLOB NULL
);
CREATE INDEX SM_BIZ_T_IDX_SCHEDULING_ID ON SM_BIZ_TASK (SCHEDULING_ID ASC);
CREATE INDEX SM_BIZ_T_IDX_TASK_NO ON SM_BIZ_TASK (TASK_NO ASC);

CREATE TABLE SM_BIZ_LXBGAPPLY (
	ID VARCHAR2(36) NOT NULL,
	SCHEDULING_ID VARCHAR2(36) NULL,
	CREATE_ID VARCHAR2(36) NULL,
	REASON VARCHAR2(4000) NULL,
	CREATE_TIME VARCHAR2(36) NULL,
	RELEASE_TIME VARCHAR2(32) NULL,
	CHECK_ID VARCHAR2(36) NULL,
	APP_TYPE VARCHAR2(255) NULL,
	CTOR_CHECKTEXT VARCHAR2(4000) NULL,
	JH_CHECKTEXT VARCHAR2(4000) DEFAULT ''  NULL,
	constraint pk_SM_BIZ_LXBGAPPLY  primary key (ID)
);

COMMENT ON TABLE SM_BIZ_LXBGAPPLY  IS '例行变更计划申请延期表';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.ID IS '申请id';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.SCHEDULING_ID '计划单id';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.CREATE_ID '创建人id';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.REASON '原因';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.CREATE_TIME '创建时间';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.RELEASE_TIME '计划发布时间';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.CHECK_ID '处长审核人id';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.APP_TYPE '状态';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.CTOR_CHECKTEXT '处长审核意见';
COMMENT ON COLUMN SM_BIZ_LXBGAPPLY.JH_CHECKTEXT '计划审核人审核意见';

CREATE TABLE AM_BIZ_RECEIVE (
    AM_RECEIVE_ID VARCHAR2(36) NOT NULL primary key,
    AM_BIZ_ID VARCHAR2(36) NULL ,
    RECEIVE_DEPT VARCHAR2(36) NULL ,
    RECEIVE_GROUP VARCHAR2(36) NULL ,
    RECEIVE_TIME VARCHAR2(32) NULL ,
    IF_RECEIVE VARCHAR2(2) NULL ,
    RECEIVE_REPLY VARCHAR2(3072) NULL ,
    INVALIDATION_MARK VARCHAR2(2) NULL ,
    UPDATE_TIME VARCHAR2(32) NULL ,
    RECEIVE_PERSON_LIST CLOB NULL ,
    DEALER VARCHAR2(32) NULL
);
CREATE INDEX AM_BIZ_R_IDX_BIZ_ID ON AM_BIZ_RECEIVE (AM_BIZ_ID ASC);
COMMENT ON TABLE OG_OFFICE IS '公告接收表';

CREATE TABLE AM_BIZ_ANNOUNCEMENT (
	AM_BIZ_ID VARCHAR2(36) NOT NULL primary key,
	AM_CODE VARCHAR2(72) NULL ,
	AM_TYPE VARCHAR2(36) NULL ,
	CREATE_TIME VARCHAR2(72) NULL ,
	CHECKER_ID VARCHAR2(36) NULL ,
	AM_DEAL_TYPE VARCHAR2(2) NULL ,
	CONTACT_PHONE VARCHAR2(32) NULL ,
	OTHER_PHONE VARCHAR2(32) NULL ,
	CREATE_ID VARCHAR2(36) NULL ,
	URGENT_LEV VARCHAR2(2) NULL ,
	SECRET_LEV VARCHAR2(2) NULL ,
	SUMMRY VARCHAR2(512) NULL ,
	DESCRIPTION VARCHAR2(2000) NULL ,
	RELEASE_DATE VARCHAR2(32) NULL ,
	RELEASE_ORG VARCHAR2(36) NULL ,
	CHECKER_FLAG VARCHAR2(2) NULL ,
	CHECKER_ADVICE VARCHAR2(128) NULL ,
	INVALIDATION_MARK VARCHAR2(2) NULL ,
	CHECKER_TIME VARCHAR2(32) NULL ,
	SMS VARCHAR2(2) NULL ,
	SEND_RANGE VARCHAR2(36) NULL ,
	RECEIVE_DEPT_ID CLOB NULL ,
	RECEIVE_DEPT_NAME CLOB NULL ,
	RECEIVE_ROLE_ID CLOB NULL ,
	RECEIVE_ROLE_NAME CLOB NULL ,
	RECEIVE_GROUP_ID CLOB NULL ,
	RECEIVE_GROUP_NAME CLOB NULL ,
	AM_TITLE VARCHAR2(80) NULL ,
	CURRENT_STATUS VARCHAR2(2) NULL ,
	END_TIME VARCHAR2(32) NULL ,
	DEAL_SCHDULE VARCHAR2(30) NULL
);
CREATE INDEX AM_BIZ_A_IDX_AM_CODE ON AM_BIZ_ANNOUNCEMENT (AM_CODE ASC);
CREATE INDEX AM_BIZ_A_IDX_CREATE_ID ON AM_BIZ_ANNOUNCEMENT (CREATE_ID ASC);
CREATE INDEX AM_BIZ_A_IDX_CREATE_TIME ON AM_BIZ_ANNOUNCEMENT (CREATE_TIME ASC);
CREATE INDEX AM_BIZ_A_IDX_STAUTS ON AM_BIZ_ANNOUNCEMENT (CURRENT_STATUS ASC);

 CREATE TABLE EVENT_RUN
   (EVENT_ID VARCHAR2(128) NOT NULL ENABLE,
	EVENT_TITLE VARCHAR2(256),
	EVENT_NO VARCHAR2(64),
	CREATE_TIME VARCHAR2(64),
	CLOSE_TIME VARCHAR2(64),
	EVENT_DESCR VARCHAR2(4000),
	APP_SYSTEM_CODE VARCHAR2(128),
	APP_SYSTEM_NAME VARCHAR2(256),
	REPORT_SOURCE VARCHAR2(256),
	EVENT_SOURCE VARCHAR2(4),
	EVENT_LEVEL VARCHAR2(4),
	EVENT_TYPE VARCHAR2(4),
	REPORT_TIME VARCHAR2(64),
	AFFILIATED_CENTER VARCHAR2(256),
	HANDLE_ROLES VARCHAR2(4),
	SYS_ATTENDANT VARCHAR2(64),
	WEB_ATTENDANT VARCHAR2(64),
	APP_ATTENDANT VARCHAR2(64),
	HANDLE_PERSON VARCHAR2(128),
	HANDLE_TIME VARCHAR2(64),
	REPORT_REASON VARCHAR2(4000),
	HANDLE_DESCR VARCHAR2(4000),
	CREATE_ID VARCHAR2(64),
	STATUS VARCHAR2(2),
	REQUEST_CLOB CLOB,
	ANTICIPATE_TIME VARCHAR2(32),
	CHARGE_PERSON VARCHAR2(64),
	SUPPLEMENT_EXPLAIN VARCHAR2(4000),
	DUTY_ACCOUNT VARCHAR2(32),
    REALITY_HANDLE_PERSON VARCHAR2(64) NULL ,
    PLAN_HANDLE__TIME VARCHAR2(64) NULL ,
    CREAT_TYPE VARCHAR2(32) NULL ,
    EVENT_CAUSE VARCHAR2(10) NULL ,
    CHANGE_FLAG VARCHAR2(100) NULL ,
    VERSION_FLAG VARCHAR2(100) NULL ,
    BATCH_FLAG VARCHAR2(100) NULL
) ;
   COMMENT ON COLUMN EVENT_RUN.EVENT_TITLE IS '事件标题';
   COMMENT ON COLUMN EVENT_RUN.EVENT_NO IS '单号';
   COMMENT ON COLUMN EVENT_RUN.CREATE_TIME IS '创建时间';
   COMMENT ON COLUMN EVENT_RUN.CLOSE_TIME IS '关闭时间';
   COMMENT ON COLUMN EVENT_RUN.EVENT_DESCR IS '事件描述';
   COMMENT ON COLUMN EVENT_RUN.APP_SYSTEM_CODE IS '应用系统编码';
   COMMENT ON COLUMN EVENT_RUN.APP_SYSTEM_NAME IS '应用系统名称';
   COMMENT ON COLUMN EVENT_RUN.REPORT_SOURCE IS '告警来源';
   COMMENT ON COLUMN EVENT_RUN.EVENT_SOURCE IS '事件来源';
   COMMENT ON COLUMN EVENT_RUN.EVENT_LEVEL IS '事件等级0一般，1急，2紧急（告警级别：1-警告、2-错误、3-紧急）';
   COMMENT ON COLUMN EVENT_RUN.EVENT_TYPE IS '事件类型（0-应用、1-系统、2-网络）';
   COMMENT ON COLUMN EVENT_RUN.REPORT_TIME IS '发生时间';
   COMMENT ON COLUMN EVENT_RUN.AFFILIATED_CENTER IS '所属中心--所属中心（0-合肥中心、1-亦庄中心、2-丰台中心）';
   COMMENT ON COLUMN EVENT_RUN.HANDLE_ROLES IS '处理角色-0-网络告警派单（合肥）1-网络告警派单（亦庄）2-网络告警派单（丰台）3-系统告警派单（合肥）4-系统告警派单（亦庄）5-系统告警派单（丰台）6-应用告警派单7-告警派单（无所属中心）';
   COMMENT ON COLUMN EVENT_RUN.SYS_ATTENDANT IS '系统管理员';
   COMMENT ON COLUMN EVENT_RUN.WEB_ATTENDANT IS '网络管理员';
   COMMENT ON COLUMN EVENT_RUN.APP_ATTENDANT IS '应用管理员';
   COMMENT ON COLUMN EVENT_RUN.HANDLE_PERSON IS '处理人';
   COMMENT ON COLUMN EVENT_RUN.HANDLE_TIME IS '处理时间';
   COMMENT ON COLUMN EVENT_RUN.REPORT_REASON IS '报警原因';
   COMMENT ON COLUMN EVENT_RUN.HANDLE_DESCR IS '处理描述';
   COMMENT ON COLUMN EVENT_RUN.CREATE_ID IS '创建人';
   COMMENT ON COLUMN EVENT_RUN.STATUS IS '状态';
   COMMENT ON COLUMN EVENT_RUN.ANTICIPATE_TIME IS '预计解决时间';
   COMMENT ON COLUMN EVENT_RUN.CHARGE_PERSON IS '负责人';
   COMMENT ON COLUMN EVENT_RUN.SUPPLEMENT_EXPLAIN IS '补充说明';
   COMMENT ON COLUMN EVENT_RUN.DUTY_ACCOUNT IS '值班账号';
   COMMENT ON TABLE EVENT_RUN  IS '运行事件单表';

   CREATE TABLE SM_BIZ_TASKINFO1 (
	TASK_FORM_ID VARCHAR2(36) NOT NULL ,
	TASK_ID VARCHAR2(36) NULL ,
	TASK_FROM_NO VARCHAR2(32) NULL ,
	TASK_PERFORM_TIME VARCHAR2(32) NULL ,
	GENERATE_TIME VARCHAR2(32) NULL ,
	TASK_FORM_STATUS VARCHAR2(2) NULL ,
	UPDATE_TIME VARCHAR2(32) NULL ,
	INVALIDATION_MARK VARCHAR2(2) NULL ,
	PERFORM_DATE VARCHAR2(32) NULL ,
	EXCUTE_DATE VARCHAR2(32) NULL ,
	EXCUTE_TIME VARCHAR2(32) NULL ,
	PERFORM_DEPT_ID VARCHAR2(36) NULL ,
	PERFORM_USER_ID VARCHAR2(36) NULL ,
	PERFORM_DUTY_ID VARCHAR2(36) NULL ,
	CHECKER_ID VARCHAR2(36) NULL ,
	EXCUTE_DESCRIPTION CLOB NULL ,
	INSPECT_DESCRIPTION CLOB NULL ,
	WORK_BEGIN_TIME VARCHAR2(32) NULL ,
	WORK_END_TIME VARCHAR2(32) NULL ,
	RELATE_CHANGE VARCHAR2(32) NULL ,
	PERFORM_GROUP_ID VARCHAR2(36) NULL ,
	IS_CHANGE VARCHAR2(2) NULL ,
	CHANGES VARCHAR2(36) NULL ,
	PUBLISH_TIME VARCHAR2(32) NULL ,
	IS_WRITE VARCHAR2(8) NULL
);
CREATE INDEX SM_BIZ_TI_IDX_TASK_FORM_STATUS ON SM_BIZ_TASKINFO (TASK_FORM_STATUS ASC);
CREATE INDEX SM_BIZ_TI_IDX_TASK_FROM_NO ON SM_BIZ_TASKINFO (TASK_FROM_NO ASC);

CREATE TABLE FMBIZ_AND_ISSUE (
  ID_ VARCHAR2(36) NOT NULL primary key,
  FM_ID VARCHAR2(36),
  STATE_ VARCHAR2(2),
  ISSUEFX_ID VARCHAR2(36),
  GROUP_ID VARCHAR2(36),
  OPERATION_ID VARCHAR2(36),
  OPERATION_TIME VARCHAR2(20)
);

CREATE TABLE DIFFICULT_EVENTS (
  ID_ VARCHAR2(36) NOT NULL primary key,
  NO_ VARCHAR2(36),
  DEPT_ VARCHAR2(36),
  FM_ID VARCHAR2(36),
  SYS_ID VARCHAR2(36),
  GROUP_ID VARCHAR2(36),
  DEAL_ID VARCHAR2(36),
  CREATER_TIME VARCHAR2(20),
  DEAL_TIME VARCHAR2(20),
  DEAL_DESC VARCHAR2(2000),
  STATUS_ VARCHAR2(2),
  SOURCE_ VARCHAR2(2),
  N1 VARCHAR2(256),
  N2 VARCHAR2(256),
  N3 VARCHAR2(256),
  N4 VARCHAR2(256),
  PRIMARY KEY (ID_)
);

COMMENT ON COLUMN DIFFICULT_EVENTS.NO_ IS '单号';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEPT_ IS '部门';
COMMENT ON COLUMN DIFFICULT_EVENTS.FM_ID IS '运维事件单ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.SYS_ID IS '归属系统ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.GROUP_ID IS '归属工作组ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEAL_ID IS '处理人ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.CREATER_TIME IS '创建时间';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEAL_TIME IS '处理时间';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEAL_DESC IS '处理描述';
COMMENT ON COLUMN DIFFICULT_EVENTS.STATUS_ IS '状态';
COMMENT ON COLUMN DIFFICULT_EVENTS.SOURCE_ IS '来源';

-- Create table
create table DUTY_VERSION
(
  VERSION_ID   VARCHAR2(32) not null primary key,
  VERSION_NO   VARCHAR2(32),
  VERSION_NAME VARCHAR2(128),
  DUTY_DATE    VARCHAR2(4000),
  ADDTIME      VARCHAR2(20),
  UPDATE_TIME  VARCHAR2(20),
  REMARK       VARCHAR2(256)
);
CREATE TABLE CM_BIZ_SINGLE_SJ (
  CHANGE_SINGLE_ID VARCHAR2(36) NOT NULL,
  CHANGE_CODE VARCHAR2(36),
  CHANGE_APPLICANT VARCHAR2(36),
  CREAT_TIME VARCHAR2(32),
  CHANGE_SINGLE_STATUS VARCHAR2(4),
  IMPORTANT_LEV VARCHAR2(2),
  CHANGE_CATEGORY_ID VARCHAR2(36),
  EXPECT_START_TIME VARCHAR2(32),
  EXPECT_END_TIME VARCHAR2(32),
  RELATE_RY_ID VARCHAR2(1024),
  CHECK_MANAGER VARCHAR2(36),
  APPROVE_MANAGER VARCHAR2(36),
  CHANGE_SINGLE_NAME VARCHAR2(120),
  CHANGE_CAUSE_TEXT VARCHAR2(2000),
  CHANGE_DETAILS VARCHAR2(4000),
  CHANGE_PROGRAM VARCHAR2(4000),
  IMPLEMENT_SUPVISER VARCHAR2(36),
  PRACTICAL_START VARCHAR2(32),
  PRACTICAL_END VARCHAR2(32),
  IMPLEMENTOR VARCHAR2(36),
  CREATE_ORG_ID VARCHAR2(36),
  INVALIDATION_MARK VARCHAR2(2),
  SYSID VARCHAR2(36),
  GROUP_ID VARCHAR2(36),
  FM_NO VARCHAR2(36),
  FM_TITLE VARCHAR2(512),
  CHIEF_MANAGER VARCHAR2(36),
  CHANGE_REASON VARCHAR2(20),
  CHANGE_ORGTYPE VARCHAR2(3000),
  CHANGE_NUM VARCHAR2(20),
  CHIEFY_MANAGER VARCHAR2(36),
  IF_AQBL VARCHAR2(2),
  SQSPSM VARCHAR2(1024),
  IS_TOOLS VARCHAR2(2),
  TOOLS_NAME VARCHAR2(32),
  CLOSE_TIME VARCHAR2(36),
  PROCHECK_MANAGER VARCHAR2(36),
  TECHNOLOGY_AUDIT_ID VARCHAR2(36),
  IS_CHANGE_OPS VARCHAR2(255),
  CHANGE_SINGLE_TYPE VARCHAR2(255),
  RESULT VARCHAR2(255),
  LOG_PERFORM_DESC VARCHAR2(255),
  CHANGE_COPY VARCHAR2(1600),
  CHANGE_BACK VARCHAR2(1600),
  RELATE_SYSTEM_ID VARCHAR2(1024),
  PK_CM_BIZ_SINGLE_SJ VARCHAR2(36),
  SJ_CHANGE_PATTERN VARCHAR2(32),
  SJFLAG VARCHAR2(32),
  IM_ID VARCHAR2(36),
  WT_DEPT VARCHAR2(4000),
  WT_LEV VARCHAR2(4000),
  WT_BUSSINESSID VARCHAR2(4000),
  WT_BUSSINESSID_QZ VARCHAR2(4000),
  DEPT_LEAD_ID VARCHAR2(4000),
  LINE_LEAD_ID VARCHAR2(4000),
  IF_AUTO VARCHAR2(2)
);
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_SINGLE_ID IS '变更单ID';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_CODE IS '变更单号';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_APPLICANT IS '创建时间';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CREAT_TIME IS '变更申请人';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_SINGLE_STATUS IS '变更状态';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IMPORTANT_LEV IS '变更风险等级';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_CATEGORY_ID IS '变更分类';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.EXPECT_START_TIME IS '预计开始时间';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.EXPECT_END_TIME IS '预计结束时间';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.RELATE_RY_ID IS '关联系统';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHECK_MANAGER IS '审核人';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.APPROVE_MANAGER IS '审批人';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_SINGLE_NAME IS '变更主题';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_CAUSE_TEXT IS '变更原因';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_DETAILS IS '变更内容';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_PROGRAM IS '变更实施方案';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IMPLEMENT_SUPVISER IS '实施监督人';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.PRACTICAL_START IS '实施开始时间';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.PRACTICAL_END IS '实施结束时间';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IMPLEMENTOR IS '实施人';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CREATE_ORG_ID IS '创建机构';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.INVALIDATION_MARK IS '无效标记';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.SYSID IS '系统ID';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.GROUP_ID IS '工作组';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.FM_NO IS '事件单号';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.FM_TITLE IS '事件标题';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHIEF_MANAGER IS '应用处长';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_REASON IS '变更原因';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_ORGTYPE IS '变更省别';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_NUM IS '变更条数';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHIEFY_MANAGER IS '业务处长';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IF_AQBL IS '是否安全堡垒';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.SQSPSM IS '授权审批说明';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IS_TOOLS IS '是否使用工具';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.TOOLS_NAME IS '工具名称';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CLOSE_TIME IS '关闭时间';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.PROCHECK_MANAGER IS '省处理人';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.TECHNOLOGY_AUDIT_ID IS '技术审核人';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IS_CHANGE_OPS IS '是否转运维';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_SINGLE_TYPE IS '判断是手工创建还是事件单转：0：事件单转   1:手工创建';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.RESULT IS '处理结果';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.LOG_PERFORM_DESC IS '关闭说明';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_COPY IS '备份方案';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.CHANGE_BACK IS '回退方案';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.SJFLAG IS '是否新增问题单   0：否  1：是';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IM_ID IS '数据问题单ID';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.WT_DEPT IS '部门信息';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.WT_LEV IS '审批级别';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.WT_BUSSINESSID IS '经办人信息';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.WT_BUSSINESSID_QZ IS '经办人信息（全值）';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.DEPT_LEAD_ID IS '部门领导ID';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.LINE_LEAD_ID IS '行领导ID';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IF_AUTO IS '是否自动化 1是，0否';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IF_FMSC IS '是否事件单首次转的变更单，1：首次；2：非首次';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.IF_DXTPH IS '是否多系统配合；1：是；0：否';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.GROUPS_ID IS '多工作组ID';
COMMENT ON COLUMN CM_BIZ_SINGLE_SJ.GROUPS_NAME IS '多工作组名称';=======
COMMENT ON COLUMN DIFFICULT_EVENTS.NO_ IS '单号';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEPT_ IS '部门';
COMMENT ON COLUMN DIFFICULT_EVENTS.FM_ID IS '运维事件单ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.SYS_ID IS '归属系统ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.GROUP_ID IS '归属工作组ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEAL_ID IS '处理人ID';
COMMENT ON COLUMN DIFFICULT_EVENTS.CREATER_TIME IS '创建时间';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEAL_TIME IS '处理时间';
COMMENT ON COLUMN DIFFICULT_EVENTS.DEAL_DESC IS '处理描述';
COMMENT ON COLUMN DIFFICULT_EVENTS.STATUS_ IS '状态';
COMMENT ON COLUMN DIFFICULT_EVENTS.SOURCE_ IS '来源';

-- Create table
create table KNOWLEDGE_VISITS
(
  ID          VARCHAR2(32) not null primary key,
  CONTENT_ID  VARCHAR2(32),
  CREATE_ID   VARCHAR2(32),
  CREATE_TIME VARCHAR2(32)
);

create table am_pro_actlog (
	am_actlog_id varchar2(32) not null ,
	am_receive_id varchar2(32) null ,
	performer_id varchar2(32) null ,
	update_time varchar2(32) null ,
	description varchar2(2048) null ,
	current_state varchar2(2) null ,
 constraint pk_am_pro_actlog primary key (am_actlog_id)
);
create index index_am_receive_id on am_pro_actlog (am_receive_id);

CREATE TABLE AM_BIZ_PARA (
	AM_PARA_ID VARCHAR2(36) NOT NULL ,
	RECEIVE_RANGE VARCHAR2(128) NULL ,
	constraint pk_AM_BIZ_PARA  primary key (AM_PARA_ID)
);

CREATE TABLE AM_BIZ_PARA_VALUE (
	AM_PARA_VALUE_ID VARCHAR2(36) NOT NULL ,
	AM_PARA_ID VARCHAR2(36) NULL ,
	RECEIVE_DEPT CLOB NULL ,
	RECEIVE_GROUP CLOB NULL ,
	RECEIVEDEPTID VARCHAR2(36) NULL ,
	RECEIVEGROUPID VARCHAR2(36) NULL ,
  constraint pk_AM_BIZ_PARA_VALUE primary key (AM_PARA_VALUE_ID)
);

CREATE TABLE IM_BIZ_ISSUEFX (
  ISSUEFX_ID VARCHAR2(64) NOT NULL,
  ISSUEFX_NO VARCHAR2(64),
  CURRENT_STATE VARCHAR2(4),
  CREATER_ID VARCHAR2(64),
  CREAT_TIME VARCHAR2(64),
  ISSUE_TYPE VARCHAR2(4),
  ISSUE_FENLEI VARCHAR2(4),
  SERIOUS_LEV VARCHAR2(4),
  ISSUE_ORG VARCHAR2(64),
  REVIEWER_ID VARCHAR2(64),
  ISSUESOURCE VARCHAR2(4),
  REPORTNAME VARCHAR2(256),
  REPORTPHONE VARCHAR2(256),
  REPORTTIME VARCHAR2(64),
  SYSNAME CLOB,
  ISSUEFX_NAME VARCHAR2(1024),
  ISSUEFX_TEXT CLOB,
  ISSUEFX_SCHEME VARCHAR2(4000),
  DEAL_STATUS VARCHAR2(4),
  CREATE_ORG VARCHAR2(64),
  FM_NO VARCHAR2(4000),
  CM_NO VARCHAR2(4000),
  DEAL_ID VARCHAR2(64),
  BUSINESS_ORG VARCHAR2(4000),
  AUDIT_ID VARCHAR2(64),
  FLAG VARCHAR2(4),
  ISSUEFX_IMPACT VARCHAR2(4000),
  DEAL_DESCRIPTION CLOB,
  MULTICOUNT NUMBER,
  BUSINESSID VARCHAR2(4000),
  EXPECT_TIME VARCHAR2(256),
  REALITY_TIME VARCHAR2(256),
  BU_DEAL_DESC VARCHAR2(4000),
  SOLV_STATUS VARCHAR2(4),
  BUSS_ID VARCHAR2(64),
  PAP_DESC VARCHAR2(4000),
  PLAN_DESC VARCHAR2(4000),
  ISSUE_YYFENLEI VARCHAR2(4),
  MULTIUSERS VARCHAR2(2048),
  DISTRIBUTE_TIME VARCHAR2(256),
  ACCEPT_TIME VARCHAR2(256),
  PRE_SOLUTION_TIME VARCHAR2(256),
  SOLUTION_TIME VARCHAR2(256),
  CSNO VARCHAR2(128),
  HANGUPTASK VARCHAR2(8),
  BU_FUHE_DESC VARCHAR2(4000),
  SYSID CLOB,
  FMJK_NO VARCHAR2(4000),
  FU_NO VARCHAR2(4000),
  DEAL_DETAIL CLOB,
  IMPORT_LEVEL VARCHAR2(2),
  LAST_TIME VARCHAR2(32),
  PUT_UNIT VARCHAR2(64),
  UNIT_SCHEDULE CLOB,
  INVALIDATION_MARK VARCHAR2(2),
  I_STATUS VARCHAR2(2),
  JJ_TIME VARCHAR2(64),
  IS_INSIDE VARCHAR2(2),
  SUB_DEPT VARCHAR2(4000),
  ONE_TYPE VARCHAR2(36),
  TWO_TYPE VARCHAR2(36),
  THREE_TYPE VARCHAR2(36),
  TARDING_NAME VARCHAR2(255),
  RELATION_FMBIZ_COUNT VARCHAR2(32)
);
CREATE TABLE CM_BIZ_QINGQIU (
  CHANGE_ID VARCHAR2(32) NOT NULL,
  CHANGE_CODE VARCHAR2(36),
  CREATETIME VARCHAR2(36),
  CREATER_ORG_ID VARCHAR2(36),
  SUBMIT_TIME VARCHAR2(36),
  APPLICANT_ID VARCHAR2(36),
  CHANGE_CATEGORY_ID VARCHAR2(36),
  SYSID VARCHAR2(2000),
  SYSNAME VARCHAR2(2000),
  IMPORTANT_LEV VARCHAR2(32),
  IS_NOTICE VARCHAR2(2),
  IS_STOP VARCHAR2(2),
  CHANGE_RESOURCE VARCHAR2(2),
  CHANGE_SINGLE_NAME VARCHAR2(1000),
  CHANGE_DETAILS CLOB,
  CHECK_ORG VARCHAR2(36),
  CHECKER_ID VARCHAR2(36),
  PRACTICLE_TIME VARCHAR2(36),
  IMPLEMENTOR_ID VARCHAR2(36),
  IMPLEMENTOR_ORGID VARCHAR2(36),
  STAUTS VARCHAR2(32),
  CHANGE_STAGE VARCHAR2(32),
  INVALIDATION_MARK VARCHAR2(2),
  FUCHECKER_ID VARCHAR2(32),
  INSTANCEID VARCHAR2(32),
  CHANGE_CAUSE_TEXT VARCHAR2(1024),
  DEAL_ID_LIST CLOB,
  FUCHECKER_FLAG VARCHAR2(2)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 8192
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_ID IS '变更单ID';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_CODE IS '变更单号';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CREATETIME IS '创建时间';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CREATER_ORG_ID IS '创建机构';
COMMENT ON COLUMN CM_BIZ_QINGQIU.SUBMIT_TIME IS '提交时间';
COMMENT ON COLUMN CM_BIZ_QINGQIU.APPLICANT_ID IS '申请人';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_CATEGORY_ID IS '变更分类';
COMMENT ON COLUMN CM_BIZ_QINGQIU.SYSID IS '所涉应用系统ID';
COMMENT ON COLUMN CM_BIZ_QINGQIU.SYSNAME IS '涉及系统名称';
COMMENT ON COLUMN CM_BIZ_QINGQIU.IMPORTANT_LEV IS '等级';
COMMENT ON COLUMN CM_BIZ_QINGQIU.IS_NOTICE IS '是否通知';
COMMENT ON COLUMN CM_BIZ_QINGQIU.IS_STOP IS '是否停止';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_RESOURCE IS '变更来源';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_SINGLE_NAME IS '标题';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_DETAILS IS '变更内容';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHECK_ORG IS '审核机构';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHECKER_ID IS '审核人';
COMMENT ON COLUMN CM_BIZ_QINGQIU.PRACTICLE_TIME IS '受理时间';
COMMENT ON COLUMN CM_BIZ_QINGQIU.IMPLEMENTOR_ID IS '实施人';
COMMENT ON COLUMN CM_BIZ_QINGQIU.IMPLEMENTOR_ORGID IS '实施机构';
COMMENT ON COLUMN CM_BIZ_QINGQIU.STAUTS IS '状态';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_STAGE IS '变更阶段';
COMMENT ON COLUMN CM_BIZ_QINGQIU.INVALIDATION_MARK IS '无效标记';
COMMENT ON COLUMN CM_BIZ_QINGQIU.FUCHECKER_ID IS '分管领导审核人';
COMMENT ON COLUMN CM_BIZ_QINGQIU.INSTANCEID IS '流程id';
COMMENT ON COLUMN CM_BIZ_QINGQIU.CHANGE_CAUSE_TEXT IS '变更原因';
COMMENT ON COLUMN CM_BIZ_QINGQIU.DEAL_ID_LIST IS '处理人ID集合';
COMMENT ON COLUMN CM_BIZ_QINGQIU.FUCHECKER_FLAG IS '分管领导审阅1待审2已审';

CREATE TABLE AM_BIZ_TASKLOG (
	AM_TASK_ID VARCHAR2(36) NOT NULL ,
	AM_BIZ_ID VARCHAR2(36) NULL ,
	UPDATE_TIME VARCHAR2(32) NULL ,
	PERSON_ID VARCHAR2(36) NULL ,
	AM_STATE VARCHAR2(36) NULL ,
	PROCESS_NAME VARCHAR2(256) NULL ,
	DESCRIPTION VARCHAR2(2048) NULL ,
	NUM VARCHAR2(5) NULL ,
	constraint pk_AM_BIZ_TASKLOG primary key (AM_TASK_ID)
);
create index index_AM_BIZ_ID on AM_BIZ_TASKLOG (AM_BIZ_ID);

CREATE TABLE FM_BIZ_CONTROL (
        CONTROL_ID VARCHAR2(36) NOT NULL,
        FM_BIZ_ID VARCHAR2(36),
        SYS_ID VARCHAR2(36),
        GROUP_ID VARCHAR2(36),
        UNUSUAL_TYPE VARCHAR2(2),
        CONTROL_TIME VARCHAR2(20)
);
COMMENT ON COLUMN FM_BIZ_CONTROL.CONTROL_ID IS '主键id';
COMMENT ON COLUMN FM_BIZ_CONTROL.FM_BIZ_ID IS '事件单id';
COMMENT ON COLUMN FM_BIZ_CONTROL.SYS_ID IS '所属应用系统';
COMMENT ON COLUMN FM_BIZ_CONTROL.GROUP_ID IS '所属工作组';
COMMENT ON COLUMN FM_BIZ_CONTROL.UNUSUAL_TYPE IS '异常分类:长时间未领取/长时间未处理/不及时';
COMMENT ON COLUMN FM_BIZ_CONTROL.CONTROL_TIME IS '统计时间';
COMMENT ON TABLE FM_BIZ_CONTROL IS '业务事件单监控表';

CREATE TABLE FM_BIZ_LIFE (
     FM_LIFE_ID VARCHAR2(36) NOT NULL,
     SYS_ID VARCHAR2(36),
     DEPT_LEV VARCHAR2(2),
     SUR_COUNT VARCHAR2(8),
     EIGHT_NDEAL_COUNT VARCHAR2(8),
     EIGHT_DEAL_COUNT VARCHAR2(8),
     EIGHT_DEAL_LIFE VARCHAR2(8),
     SIXTEEN_NDEAL_COUNT VARCHAR2(8),
     SIXTEEN_DEAL_COUNT VARCHAR2(8),
     SIXTEEN_DEAL_LIFE VARCHAR2(8),
     TTFOUR_NDEAL_COUNT VARCHAR2(8),
     TTFOUR_DEAL_COUNT VARCHAR2(8),
     TTFOUR_DEAL_LIFE VARCHAR2(8),
     STATISTICAL_TIME VARCHAR2(36),
     ORG_ID VARCHAR2(36),
     FLAG VARCHAR2(2),
     INVALIDATION_MARK VARCHAR2(2)
)
    TABLESPACE USERS
        LOGGING
    NOCOMPRESS
    PCTFREE 10
    INITRANS 1
    STORAGE (
    INITIAL 14680064
    NEXT 8192
    MINEXTENTS 1
    MAXEXTENTS 2147483645
    BUFFER_POOL DEFAULT
            )
    PARALLEL 1
    NOCACHE
    DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN FM_BIZ_LIFE.FM_LIFE_ID IS '主键id';
COMMENT ON COLUMN FM_BIZ_LIFE.SYS_ID IS '所属系统';
COMMENT ON COLUMN FM_BIZ_LIFE.DEPT_LEV IS '单位级别 1公司 2系统';
COMMENT ON COLUMN FM_BIZ_LIFE.SUR_COUNT IS '剩余数量';
COMMENT ON COLUMN FM_BIZ_LIFE.EIGHT_NDEAL_COUNT IS '8小时未处理数量';
COMMENT ON COLUMN FM_BIZ_LIFE.EIGHT_DEAL_COUNT IS '8小时处理数量';
COMMENT ON COLUMN FM_BIZ_LIFE.EIGHT_DEAL_LIFE IS '8小时处理效率';
COMMENT ON COLUMN FM_BIZ_LIFE.SIXTEEN_NDEAL_COUNT IS '16小时未处理数量';
COMMENT ON COLUMN FM_BIZ_LIFE.SIXTEEN_DEAL_COUNT IS '16小时处理数量';
COMMENT ON COLUMN FM_BIZ_LIFE.SIXTEEN_DEAL_LIFE IS '16小时处理效率';
COMMENT ON COLUMN FM_BIZ_LIFE.TTFOUR_NDEAL_COUNT IS '24小时未处理数量';
COMMENT ON COLUMN FM_BIZ_LIFE.TTFOUR_DEAL_COUNT IS '24小时处理数量';
COMMENT ON COLUMN FM_BIZ_LIFE.TTFOUR_DEAL_LIFE IS '24小时处理效率';
COMMENT ON COLUMN FM_BIZ_LIFE.STATISTICAL_TIME IS '统计时间';
COMMENT ON COLUMN FM_BIZ_LIFE.ORG_ID IS '所属公司';
COMMENT ON COLUMN FM_BIZ_LIFE.FLAG IS '统计标识 1日常效率 2报表';
COMMENT ON COLUMN FM_BIZ_LIFE.INVALIDATION_MARK IS '历史数据标志 1 当前数据 0 历史数据';
COMMENT ON TABLE FM_BIZ_LIFE IS '事件单效率表';

create table DUTY_VERSION_TYPEINFO
(
  VERSION_TYPEINFO_ID VARCHAR2(36) not null,
  TYPE_NAME           VARCHAR2(256),
  MOBILE_PHONE        VARCHAR2(32),
  PARENT_ID           VARCHAR2(36),
  UPDATE_TIME         VARCHAR2(32),
  INVALIDATION_MARK   VARCHAR2(2),
  SERIAL              NUMBER(10),
  LEADER              VARCHAR2(2),
  TYPE_NO             VARCHAR2(32),
  TYPE_DESCRIPTION    VARCHAR2(1024),
  PID                 VARCHAR2(32),
  VERSION_ID          VARCHAR2(32),
  TYPEINFO_ID         VARCHAR2(32),
  DUTY_NUMBER         VARCHAR2(2),
  TYPE_ROWS           VARCHAR2(2),
  TYPE_COLUMNS        VARCHAR2(2)
);

CREATE TABLE TEL_BIZ (
     TELID VARCHAR2(32) NOT NULL,
     TELNO VARCHAR2(32),
     ISTRANSFER VARCHAR2(2),
     CONTACT_ORG VARCHAR2(32),
     SYS_ID VARCHAR2(56),
     TEL_TYPE VARCHAR2(36),
     CONTENT VARCHAR2(1024),
     CREATE_ID VARCHAR2(100),
     CREATE_TIME VARCHAR2(32),
     CREATE_ORG VARCHAR2(32),
     CONTACT_NAME VARCHAR2(32),
     CONTACT_PHONE VARCHAR2(16),
     INVALIDATION_MARK VARCHAR2(2),
     FAULT_NO VARCHAR2(36),
     STATE VARCHAR2(2),
     CLOSE_TIME VARCHAR2(32),
     EVENT_TYPE VARCHAR2(32),
     IS_SOLVE VARCHAR2(2),
     GROUP_NO VARCHAR2(156),
     OVER_FLOW_GROUP VARCHAR2(16),
     OVER_FLOW_TIME VARCHAR2(32),
     CLOSE_OPTION VARCHAR2(512),
     START_TIME VARCHAR2(40)
);
COMMENT ON COLUMN TEL_BIZ.TELID IS '电话单id';
COMMENT ON COLUMN TEL_BIZ.TELNO IS '电话单号';
COMMENT ON COLUMN TEL_BIZ.ISTRANSFER IS '是否为转接(1是0否)';
COMMENT ON COLUMN TEL_BIZ.CONTACT_ORG IS '来电人所属机构';
COMMENT ON COLUMN TEL_BIZ.SYS_ID IS '问题所属系统';
COMMENT ON COLUMN TEL_BIZ.TEL_TYPE IS '问题类型';
COMMENT ON COLUMN TEL_BIZ.CONTENT IS '来电内容';
COMMENT ON COLUMN TEL_BIZ.CREATE_ID IS '创建人';
COMMENT ON COLUMN TEL_BIZ.CREATE_TIME IS '接入时间';
COMMENT ON COLUMN TEL_BIZ.CREATE_ORG IS '创建机构';
COMMENT ON COLUMN TEL_BIZ.CONTACT_NAME IS '来电人';
COMMENT ON COLUMN TEL_BIZ.CONTACT_PHONE IS '来电人电话';
COMMENT ON COLUMN TEL_BIZ.INVALIDATION_MARK IS '有效标志';
COMMENT ON COLUMN TEL_BIZ.FAULT_NO IS '业务事件单号（转业务事件单的才有）';
COMMENT ON COLUMN TEL_BIZ.STATE IS '状态';
COMMENT ON COLUMN TEL_BIZ.CLOSE_TIME IS '关闭时间';
COMMENT ON COLUMN TEL_BIZ.EVENT_TYPE IS '事件类型';
COMMENT ON COLUMN TEL_BIZ.IS_SOLVE IS '是否解决';
COMMENT ON COLUMN TEL_BIZ.GROUP_NO IS '组号';
COMMENT ON COLUMN TEL_BIZ.OVER_FLOW_GROUP IS '溢出组';
COMMENT ON COLUMN TEL_BIZ.OVER_FLOW_TIME IS '溢出时间';
COMMENT ON COLUMN TEL_BIZ.CLOSE_OPTION IS '关闭意见';
COMMENT ON COLUMN TEL_BIZ.START_TIME IS '接入时间';
COMMENT ON TABLE TEL_BIZ IS '电话事件单';

create table TEL_SUPPORT_PEOPLE_EXTENSION
(
  id            VARCHAR2(36) not null,
  orgid         VARCHAR2(36) not null,
  name          VARCHAR2(36) not null,
  extension     VARCHAR2(36) not null,
  sysname       VARCHAR2(255) not null,
  create_person VARCHAR2(50),
  create_time   VARCHAR2(50),
  update_person VARCHAR2(50),
  update_time   VARCHAR2(50),
  sysid         VARCHAR2(255),
  constraint pk_TEL_SUPPORT_PEOPLE_EXTENSION primary key (id)
);
COMMENT ON TABLE TEL_BIZ IS '支持人员对应分机号查询';

create table OG_PERSON_DUTY(
  duty_id                 VARCHAR2(32) not null,
  pid                     VARCHAR2(32),
  user_id                 VARCHAR2(32),
  group_id                VARCHAR2(32),
  monitor_type_account_id VARCHAR2(32),
  duty_type               VARCHAR2(2),
  duty_name               VARCHAR2(256),
  operate_time			  DATE,
  tel 					  VARCHAR2(50),
  constraint pk_OG_PERSON_DUTY primary key (duty_id)
);

COMMENT ON TABLE OG_PERSON_DUTY IS '监控值班表';
COMMENT ON COLUMN OG_PERSON_DUTY.duty_id IS '值班表id';
COMMENT ON COLUMN OG_PERSON_DUTY.pid IS '人员id';
COMMENT ON COLUMN OG_PERSON_DUTY.user_id IS '用户id';
COMMENT ON COLUMN OG_PERSON_DUTY. GROUP_ID IS '工作组表id';
COMMENT ON COLUMN OG_PERSON_DUTY.monitor_type_account_id IS '人员-监控类型关联表id';
COMMENT ON COLUMN OG_PERSON_DUTY.duty_type IS '值班组类型';
COMMENT ON COLUMN OG_PERSON_DUTY.duty_name IS '值班组名称';
COMMENT ON COLUMN OG_PERSON_DUTY.operate_time IS '添加值班组时间';
COMMENT ON COLUMN OG_PERSON_DUTY.TEL IS '联系方式';

CREATE TABLE DUTY_ACCOUNT (
	PID VARCHAR2(32) NOT NULL ,
	ACCOUNT_PID VARCHAR2(32) NOT NULL ,
	ID VARCHAR2(32) NOT NULL ,
	constraint pk_DUTY_ACCOUNT primary key (ID)
)
LOGGING
NOCOMPRESS
NOCACHE
;

CREATE TABLE DUTY_SUBSTITUTE (
	SUBSTITUTE_ID VARCHAR2(32) NULL ,
	PID VARCHAR2(1024) NULL ,
	TID VARCHAR2(32) NULL ,
	DUTY_DATE VARCHAR2(32) NULL ,
	REMARK VARCHAR2(512) NULL ,
	STATUS VARCHAR2(2) NULL ,
	TYPE_NO VARCHAR2(32) NULL ,
	SCHEDULING_ID VARCHAR2(32) NULL
)
LOGGING
NOCOMPRESS
NOCACHE
;

CREATE TABLE DUTY_SUBSTITUTE_REMARK (
	SUBSTITUTE_REMARK_ID VARCHAR2(32) NOT NULL ,
	PID VARCHAR2(1024) NULL ,
	TID VARCHAR2(32) NULL ,
	DUTY_DATE VARCHAR2(32) NULL ,
	REMARK VARCHAR2(512) NULL ,
	TYPE_NO VARCHAR2(32) NULL ,
	constraint pk_DUTY_SUBSTITUTE_REMARK primary key (SUBSTITUTE_REMARK_ID)
)
LOGGING
NOCOMPRESS
NOCACHE
;

CREATE TABLE ADDRESS_LIST (
  ADDRESS_LIST_ID VARCHAR2(32 )  NOT NULL,
  ADDRESS_LIST_TYPE VARCHAR2(2 ) ,
  CREATE_PID VARCHAR2(32 ) ,
  ORGID VARCHAR2(32 ) ,
  CREATE_TIME VARCHAR2(20 ) ,
  UPDATE_TIME VARCHAR2(20 ) ,
  UPDATE_USER_ID VARCHAR2(32 ) ,
  PHONE VARCHAR2(32 ) ,
  EMAIL VARCHAR2(256 ) ,
  INVALIDATIONMARK VARCHAR2(2 ) ,
  PNAME VARCHAR2(80 ) ,
  SERVER_NAME VARCHAR2(100 ) ,
  TEL VARCHAR2(200 ) ,
  SERVER_ORDER VARCHAR2(60 ) ,
  ISCANREAD VARCHAR2(2 ) ,
  CREATE_ORGID VARCHAR2(32 ) ,
  DUTY VARCHAR2(80 ) ,
  RESPONSIBILITY VARCHAR2(1000 ) ,
  MEMO VARCHAR2(1000 )
);




CREATE TABLE REG_FOLDER_INDEX (
  ID_ VARCHAR2(32 )  NOT NULL,
  NAME_ VARCHAR2(256 ) ,
  PATH_ VARCHAR2(1024 ) ,
  PARENT_ VARCHAR2(32 ) ,
  DESCRIPTION_ VARCHAR2(2056 ) ,
  CREATE_USER_ VARCHAR2(64 ) ,
  CREATE_TIME_ VARCHAR2(20 ) ,
  MODIFY_USER_ VARCHAR2(64 ) ,
  MODIFY_TIME_ VARCHAR2(20 ) ,
  UPDATE_TIME_ VARCHAR2(20 ) ,
  LEAF_ VARCHAR2(20 )
);



CREATE TABLE REGIME_INFO (
  REGIME_INFO_ID VARCHAR2(32 )  NOT NULL,
  REGIME_TITLE VARCHAR2(150 ) ,
  REGIME_DIGEST VARCHAR2(2000 ) ,
  COMMIT_ID VARCHAR2(32 ) ,
  COMMIT_TIME VARCHAR2(30 ) ,
  ORGID VARCHAR2(32 ) ,
  REGIME_INFO_TYPE VARCHAR2(2 ) ,
  STEP_UP_OPERATION_ID VARCHAR2(32 ) ,
  STEP_UP_OPERATION_TIME VARCHAR2(30 ) ,
  PRINT_TIME VARCHAR2(30 ) ,
  COMMIT_DEPT VARCHAR2(45 ) ,
  NUM VARCHAR2(150 ) ,
  MARK VARCHAR2(4000 ) ,
  TYPE_ONE VARCHAR2(2 ) ,
  TYPE_TWO VARCHAR2(2 ) ,
  SYTEM_FOUR VARCHAR2(2 ) ,
  CLASSIFY VARCHAR2(2 ) ,
  DELETE_REASON VARCHAR2(2 ) ,
  KEY_WORD VARCHAR2(150 ) ,
  IS_DELETE VARCHAR2(2 ) ,
  CURRENT_STATE VARCHAR2(4 ) ,
  CHECKER VARCHAR2(36 ) ,
  FOLDER_ VARCHAR2(36 ) ,
  DELETE_TIME VARCHAR2(30 )
);



CREATE TABLE COMPUTER_MODULE (
  ID VARCHAR2(32 )  NOT NULL,
  NAME VARCHAR2(64 ) ,
  CREATE_TIME VARCHAR2(20 ) ,
  CREATE_ID VARCHAR2(32 ) ,
  PARENT_ID VARCHAR2(32 ) ,
  STATUS VARCHAR2(2 )
);




CREATE TABLE COMPUTER_ROOM_APPLY (
  ID VARCHAR2(32 )  NOT NULL,
  APPLY_USER_ID VARCHAR2(32 ) ,
  APPLY_ORG_ID VARCHAR2(32 ) ,
  COMPUTER_ROOM_MODULE VARCHAR2(1024 ) ,
  ACCOMPANY_USER_ID VARCHAR2(32 ) ,
  PREDICT_IN_TIME VARCHAR2(32 ) ,
  PREDICT_OUT_TIME VARCHAR2(32 ) ,
  WORK_CONTENT_IDS VARCHAR2(512 ) ,
  WORK_DESCRIPTION VARCHAR2(512 ) ,
  APPLY_STATE VARCHAR2(2 ) ,
  REGISTER_TIME VARCHAR2(32 ) ,
  REGISTER_ACCOMPANY_USER_NAME VARCHAR2(32 ) ,
  CREATE_TIMES VARCHAR2(32 ) ,
  UPDATE_TIMES VARCHAR2(32 ) ,
  AUDITOR_ID VARCHAR2(32 )  DEFAULT NULL,
  REASON VARCHAR2(255 )  DEFAULT NULL,
  WORK_CONTENT VARCHAR2(255 ) ,
  IS_TEMPORARY VARCHAR2(2 ) ,
  INTO_NAME VARCHAR2(255 ) ,
  INTO_IDNUMBER VARCHAR2(255 ) ,
  INTO_PHONE VARCHAR2(255 ) ,
  INTO_UNIT VARCHAR2(255 ) ,
  IS_BELONGINGS VARCHAR2(2 ) ,
  BELONGINGS VARCHAR2(255 ) ,
  IS_URGENCY VARCHAR2(2 ) ,
  REMARK VARCHAR2(1024 ) ,
  COMPUTERCENTER VARCHAR2(32 ) ,
  ACCOMPANYUSERONE VARCHAR2(255 ) ,
  REALITYINTO_TIME VARCHAR2(32 ) ,
  REALITYOUTTO_TIME VARCHAR2(32 ) ,
  REALITYINTOPERSONNEL VARCHAR2(255 ) ,
  REALITY_BELONGINGS VARCHAR2(512 ) ,
  REALITY_MODULE VARCHAR2(255 ) ,
  ACCOMPANYUSERIDTWO VARCHAR2(32 ) ,
  BELONGINGSNUM VARCHAR2(32 ) ,
  INOUTTYPE VARCHAR2(64 ) ,
  INSTALLSITE VARCHAR2(32 ) ,
  COMPUTER_ROOM_MODULE_NAME VARCHAR2(255 ) ,
  REALITY_BELONGINGS_NUM VARCHAR2(32 ) ,
  COMPUTER_APPLY_NO VARCHAR2(32 ) ,
  REGISTER_NAME VARCHAR2(32 )
);

create table TEL_SYSTEM_SUPPORTGROUP_NUMBER
(
  id                 VARCHAR2(36) not null,
  orgid              VARCHAR2(100) not null,
  sysid              VARCHAR2(100) not null,
  support_group_code VARCHAR2(100) not null,
  support_group_name VARCHAR2(100) not null,
  sj_group_code      VARCHAR2(100) not null,
  sj_group_name      VARCHAR2(100) not null,
  create_person      VARCHAR2(100),
  create_time        VARCHAR2(100),
  update_person      VARCHAR2(100),
  update_time        VARCHAR2(100)
  constraint pk_TEL_SYSTEM_SUPPORTGROUP_NUMBER primary key (id)
);

COMMENT ON TABLE OG_PERSON_DUTY IS '系统对应支持组编号查询';

CREATE TABLE forms_inspection (
          id NVARCHAR2(255) NOT NULL,
          sys_name NVARCHAR2(255),
          sys_company NVARCHAR2(255),
          inspectors NVARCHAR2(255),
          contact NVARCHAR2(255),
          sys_manager NVARCHAR2(255),
          last_date NVARCHAR2(255),
          start_time NVARCHAR2(255),
          end_time NVARCHAR2(255),
          login_states NVARCHAR2(255),
          login_annotation NVARCHAR2(255),
          date_states NVARCHAR2(255),
          date_annotation NVARCHAR2(255),
          book_states NVARCHAR2(255),
          book_annotation NVARCHAR2(255),
          app_states NVARCHAR2(255),
          app_annotation NVARCHAR2(255),
          global_states NVARCHAR2(255),
          global_annotation NVARCHAR2(255),
          limit_states NVARCHAR2(255),
          limit_annotation NVARCHAR2(255),
          upload_states NVARCHAR2(255),
          upload_annotation NVARCHAR2(255),
          os_states NVARCHAR2(255),
          os_annotation NVARCHAR2(255),
          network_states NVARCHAR2(255),
          network_annotation NVARCHAR2(255),
          access_states NVARCHAR2(255),
          access_annotation NVARCHAR2(255),
          database_states NVARCHAR2(255),
          database_annotation NVARCHAR2(255),
          remark NCLOB
)
    TABLESPACE ENTEGOR_TSPACE
        LOGGING
    NOCOMPRESS
    PCTFREE 10
    INITRANS 1
    STORAGE (
    BUFFER_POOL DEFAULT
            )
    PARALLEL 1
    NOCACHE
    DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN ITSM.forms_inspection.id IS '巡检记录单主键';
COMMENT ON COLUMN ITSM.forms_inspection.sys_name IS '应用系统名称版本';
COMMENT ON COLUMN ITSM.forms_inspection.sys_company IS '应用系统运维单位';
COMMENT ON COLUMN ITSM.forms_inspection.inspectors IS '巡检人员';
COMMENT ON COLUMN ITSM.forms_inspection.contact IS '联系方式';
COMMENT ON COLUMN ITSM.forms_inspection.sys_manager IS '系统负责人';
COMMENT ON COLUMN ITSM.forms_inspection.last_date IS '巡检项最后更新日期';
COMMENT ON COLUMN ITSM.forms_inspection.start_time IS '开始时间';
COMMENT ON COLUMN ITSM.forms_inspection.end_time IS '结束时间';
COMMENT ON COLUMN ITSM.forms_inspection.login_states IS '登录页面是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.login_annotation IS '登录页面是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.date_states IS '数据统计页是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.date_annotation IS '数据统计页是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.book_states IS '通讯录管理页面是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.book_annotation IS '通讯录管理页面是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.app_states IS '应用管理页面是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.app_annotation IS '应用管理页面是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.global_states IS '全局设置页面是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.global_annotation IS '全局设置页面是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.limit_states IS '权限管理页面是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.limit_annotation IS '权限管理页面是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.upload_states IS '批量上传页面是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.upload_annotation IS '批量上传页面是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.os_states IS '服务器操作系统是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.os_annotation IS '服务器操作系统是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.network_states IS '服务器网络是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.network_annotation IS '服务器网络是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.access_states IS '应用分系统访问是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.access_annotation IS '应用分系统访问是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.database_states IS '数据库是否正常(0：异常；1：正常)';
COMMENT ON COLUMN ITSM.forms_inspection.database_annotation IS '数据库是否正常说明';
COMMENT ON COLUMN ITSM.forms_inspection.remark IS '备注';

-- 2022-3-9 llf
-- 删除序列
-- DROP SEQUENCE seq_sys_upgrade_form;
-- 创建序列
-- create sequence seq_sys_upgrade_form
--     increment by 1
--     start with 100
--     nomaxvalue
--  nominvalue
--  cache 20;
-- 创建 系统升级申请单
create table sys_upgrade_form(
		id 				   							varchar2(32) NOT NULL,
		sys_number   							    varchar2(50) not null,
		sys_name     							    varchar2(100) not null,
		sys_contractor     				            varchar2(100),
		item_leader								    varchar2(32),
		tel											varchar2(20),
		apply_date								    varchar2(32),
		dept										varchar2(64),
		current_version						        varchar2(50) ,
		upgraded_version 					        varchar2(50) ,
		related_systems						        varchar2(50) ,
		start_time 								    varchar2(32),
		end_time 									varchar2(32),
		upgrade_reason						        varchar2(500) ,
		upgrade_content						        varchar2(500) ,
		remarks										varchar2(200) ,
		leader_sign								    varchar2(100),
		leader_sign_time					        varchar2(32),
		sys_opinion								    varchar2(100),
		sys_opinion_time					        varchar2(32),
		collective_unit_opinion		                varchar2(100),
		collective_unit_time			            varchar2(32),
		bureau_unit_opinion				            varchar2(500),
		creat_time                                  varchar2(32)
);

alter table sys_upgrade_form add constraint pk_sys_upgrade_form primary key (id);

-- 删除触发器
-- DROP TRIGGER trig_sys_upgrade_form;

-- 建立触发器 为设置主键自增
-- create trigger trig_sys_upgrade_form before
-- insert on sys_upgrade_form for each row when (new.id is null)
-- begin
--  select seq_sys_upgrade_form.nextval into:new.id from dual;
-- end;

comment on table  sys_upgrade_form               is '系统升级申请单';
comment on column sys_upgrade_form.sys_number    is '编号';
comment on column sys_upgrade_form.sys_name    is '应用系统名称';
comment on column sys_upgrade_form.sys_contractor    is '应用系统承建单位';
comment on column sys_upgrade_form.item_leader    is '项目负责人';
comment on column sys_upgrade_form.tel    is '联系方式';
comment on column sys_upgrade_form.apply_date    is '申请日期';
comment on column sys_upgrade_form.dept    is '业务部门';
comment on column sys_upgrade_form.current_version    is '当前版本';
comment on column sys_upgrade_form.upgraded_version    is '升级后版本';
comment on column sys_upgrade_form.related_systems    is '相关系统';
comment on column sys_upgrade_form.start_time    is '系统升级开始时间';
comment on column sys_upgrade_form.end_time    is '系统升级完成时间';
comment on column sys_upgrade_form.upgrade_reason    is '升级原因';
comment on column sys_upgrade_form.upgrade_content    is '升级内容';
comment on column sys_upgrade_form.remarks    is '备注';
comment on column sys_upgrade_form.leader_sign    is '项目负责人签字';
comment on column sys_upgrade_form.leader_sign_time    is '项目负责人签字日期';
comment on column sys_upgrade_form.sys_opinion    is '相关系统意见';
comment on column sys_upgrade_form.sys_opinion_time    is '相关系统意见日期';
comment on column sys_upgrade_form.collective_unit_opinion    is '总集单位审批意见';
comment on column sys_upgrade_form.collective_unit_time    is '总集单位审批意见日期';
comment on column sys_upgrade_form.bureau_unit_opinion    is '技术局审批意见';
comment on column sys_upgrade_form.creat_time    is '创建时间';


CREATE TABLE AUTOMATE_MIDDLE (
    AUTO_ID VARCHAR2(32 BYTE) VISIBLE NOT NULL,
    BUSINESS_ID VARCHAR2(32 BYTE) VISIBLE,
    BUSINESS_NO VARCHAR2(32 BYTE) VISIBLE,
    STATUS VARCHAR2(2 BYTE) VISIBLE,
    CHECK_COUNT NUMBER VISIBLE DEFAULT NULL,
    CREATE_TIME DATE VISIBLE,
    CHANGE_BACK VARCHAR2(4000 BYTE) VISIBLE,
    COMONT VARCHAR2(4000 BYTE) VISIBLE,
    AUTO_MESSAGE VARCHAR2(4000 BYTE) VISIBLE,
    constraint pk_AUTOMATE_MIDDLE primary key (AUTO_ID)
);
COMMENT ON COLUMN AUTOMATE_MIDDLE.AUTO_ID IS '主键ID';
COMMENT ON COLUMN AUTOMATE_MIDDLE.BUSINESS_ID IS '业务表主键ID';
COMMENT ON COLUMN AUTOMATE_MIDDLE.BUSINESS_NO IS '业务表单号';
COMMENT ON COLUMN AUTOMATE_MIDDLE.STATUS IS '待自动化校验标识，0-代表未校验，1-代表校验通过，9-表示校验失败';
COMMENT ON COLUMN AUTOMATE_MIDDLE.CHECK_COUNT IS '校验次数,最多校验三次，三次都失败不再校验';
COMMENT ON COLUMN AUTOMATE_MIDDLE.CREATE_TIME IS '生成时间';
COMMENT ON COLUMN AUTOMATE_MIDDLE.CHANGE_BACK IS '回退方案';
COMMENT ON COLUMN AUTOMATE_MIDDLE.COMONT IS '处理意见';
COMMENT ON COLUMN AUTOMATE_MIDDLE.AUTO_MESSAGE IS '自动化执行结果信息（针对校验失败场景存储失败原因）';
COMMENT ON TABLE AUTOMATE_MIDDLE IS '软研接口定时任务中间表';

CREATE TABLE KNOWLEDGE_SEARCH (
                                  ID VARCHAR2(32 BYTE) VISIBLE NOT NULL,
                                  NAME VARCHAR2(200 BYTE) VISIBLE,
                                  REMARK VARCHAR2(512 BYTE) VISIBLE,
                                  CREATE_TIME VARCHAR2(20 BYTE) VISIBLE,
                                  UPDATE_TIME VARCHAR2(20 BYTE) VISIBLE,
                                  STATUS VARCHAR2(2 BYTE) VISIBLE,
                                  CREATE_ID VARCHAR2(32 BYTE) VISIBLE,
                                  UPDATE_ID VARCHAR2(32 BYTE) VISIBLE,
                                  TITLE VARCHAR2(200 BYTE) VISIBLE,
                                  CONTENT_ID CLOB VISIBLE
)
    LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN KNOWLEDGE_SEARCH.ID IS 'id';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.NAME IS '索引名称';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.REMARK IS '备注';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.UPDATE_TIME IS '修改时间';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.STATUS IS '状态';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.CREATE_ID IS '创建人';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.UPDATE_ID IS '修改人';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.TITLE IS '知识标题';
COMMENT ON COLUMN KNOWLEDGE_SEARCH.CONTENT_ID IS '知识id';
COMMENT ON TABLE KNOWLEDGE_SEARCH IS '知识检索';


	CREATE TABLE ITSM_HC_COLLECTING	  (
		   id varchar2(255)  NOT NULL PRIMARY KEY,
		   sys_name varchar2(255) ,
		   sys_company varchar2(255) ,
		   inspectors varchar2(255) ,
		   contact varchar2(255) ,
		   sys_manager varchar2(255) ,
		   last_date varchar2(255) ,
		   start_time varchar2(255) ,
		   end_time varchar2(255) ,
		   crawler_states varchar2(255) ,
		   crawler_annotation varchar2(255),
		   dispatching_states varchar2(255) ,
		   dispatching_annotation varchar2(255) ,
		   process_states varchar2(255) ,
		   process_annotation varchar2(255) ,
		   warning_states varchar2(255) ,
		   warning_annotation varchar2(255) ,
		   update_states varchar2(255),
		   update_annotation varchar2(255) ,
		   quality_states varchar2(255) ,
		   quality_annotation varchar2(255) ,
		   remark varchar2(4000)
		   );

