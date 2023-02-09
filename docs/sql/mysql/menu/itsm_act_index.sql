create index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION (BUSINESS_KEY_);
create index ACT_IDX_EXEC_ROOT on ACT_RU_EXECUTION (ROOT_PROC_INST_ID_);
create index ACT_IDX_TASK_CREATE on ACT_RU_TASK (CREATE_TIME_);
create index ACT_IDX_IDENT_LNK_USER on ACT_RU_IDENTITYLINK (USER_ID_);
create index ACT_IDX_IDENT_LNK_GROUP on ACT_RU_IDENTITYLINK (GROUP_ID_);
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on ACT_RU_EVENT_SUBSCR (CONFIGURATION_);
create index ACT_IDX_BYTEAR_DEPL on ACT_GE_BYTEARRAY (DEPLOYMENT_ID_);
create index ACT_IDX_EXE_PROCINST on ACT_RU_EXECUTION (PROC_INST_ID_);
create index ACT_IDX_EXE_PARENT on ACT_RU_EXECUTION (PARENT_ID_);
alter table ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
        foreign key (DEPLOYMENT_ID_)
            references ACT_RE_DEPLOYMENT (ID_);
alter table ACT_RE_PROCDEF
    add constraint ACT_UNIQ_PROCDEF
        unique (KEY_, VERSION_, TENANT_ID_);
alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_) ON DELETE SET NULL  ON UPDATE SET NULL;
alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PARENT
        foreign key (PARENT_ID_)
            references ACT_RU_EXECUTION (ID_) ON DELETE SET NULL  ON UPDATE SET NULL;
create index ACT_IDX_EXE_SUPER on ACT_RU_EXECUTION (SUPER_EXEC_);
alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_SUPER
        foreign key (SUPER_EXEC_)
            references ACT_RU_EXECUTION (ID_) ON DELETE SET NULL  ON UPDATE SET NULL;
create index ACT_IDX_EXE_PROCDEF on ACT_RU_EXECUTION (PROC_DEF_ID_);
alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_) ON DELETE SET NULL  ON UPDATE SET NULL;
create index ACT_IDX_TSKASS_TASK on ACT_RU_IDENTITYLINK (TASK_ID_);
alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_TSKASS_TASK
        foreign key (TASK_ID_)
            references ACT_RU_TASK (ID_);
create index ACT_IDX_ATHRZ_PROCEDEF on ACT_RU_IDENTITYLINK (PROC_DEF_ID_);
alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_ATHRZ_PROCEDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);
create index ACT_IDX_IDL_PROCINST on ACT_RU_IDENTITYLINK (PROC_INST_ID_);
alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_IDL_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_TASK_EXEC on ACT_RU_TASK (EXECUTION_ID_);
alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_EXE
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_TASK_PROCINST on ACT_RU_TASK (PROC_INST_ID_);
alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_TASK_PROCDEF on ACT_RU_TASK (PROC_DEF_ID_);
alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);
create index ACT_IDX_JOB_EXECUTION_ID on ACT_RU_JOB (EXECUTION_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_JOB_PROC_INST_ID on ACT_RU_JOB (PROCESS_INSTANCE_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_JOB_PROC_DEF_ID on ACT_RU_JOB (PROC_DEF_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);
create index ACT_IDX_JOB_EXCEPTION on ACT_RU_JOB (EXCEPTION_STACK_ID_);
alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_TJOB_EXECUTION_ID on ACT_RU_TIMER_JOB (EXECUTION_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_TJOB_PROC_INST_ID on ACT_RU_TIMER_JOB (PROCESS_INSTANCE_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_TJOB_PROC_DEF_ID on ACT_RU_TIMER_JOB (PROC_DEF_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);
create index ACT_IDX_TJOB_EXCEPTION on ACT_RU_TIMER_JOB (EXCEPTION_STACK_ID_);
alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TJOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_SJOB_EXECUTION_ID on ACT_RU_SUSPENDED_JOB (EXECUTION_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_SJOB_PROC_INST_ID on ACT_RU_SUSPENDED_JOB (PROCESS_INSTANCE_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_SJOB_PROC_DEF_ID on ACT_RU_SUSPENDED_JOB (PROC_DEF_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);
create index ACT_IDX_SJOB_EXCEPTION on ACT_RU_SUSPENDED_JOB (EXCEPTION_STACK_ID_);
alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SJOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_DJOB_EXECUTION_ID on ACT_RU_DEADLETTER_JOB (EXECUTION_ID_);
alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DJOB_EXECUTION foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_DJOB_PROC_INST_ID on ACT_RU_DEADLETTER_JOB (PROCESS_INSTANCE_ID_);
alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DJOB_PROCESS_INSTANCE foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_DJOB_PROC_DEF_ID on ACT_RU_DEADLETTER_JOB (PROC_DEF_ID_);
alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DJOB_PROC_DEF foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_);
create index ACT_IDX_DJOB_EXCEPTION on ACT_RU_DEADLETTER_JOB (EXCEPTION_STACK_ID_);
alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DJOB_EXCEPTION foreign key (EXCEPTION_STACK_ID_) references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_EVENT_SUBSCR on ACT_RU_EVENT_SUBSCR (EXECUTION_ID_);
alter table ACT_RU_EVENT_SUBSCR
    add constraint ACT_FK_EVENT_EXEC foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_MODEL_SOURCE on ACT_RE_MODEL (EDITOR_SOURCE_VALUE_ID_);
alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE foreign key (EDITOR_SOURCE_VALUE_ID_) references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_MODEL_SOURCE_EXTRA on ACT_RE_MODEL (EDITOR_SOURCE_EXTRA_VALUE_ID_);
alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE_EXTRA foreign key (EDITOR_SOURCE_EXTRA_VALUE_ID_) references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_MODEL_DEPLOYMENT on ACT_RE_MODEL (DEPLOYMENT_ID_);
alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_DEPLOYMENT foreign key (DEPLOYMENT_ID_) references ACT_RE_DEPLOYMENT (ID_);
create index ACT_IDX_PROCDEF_INFO_JSON on ACT_PROCDEF_INFO (INFO_JSON_ID_);
alter table ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_JSON_BA foreign key (INFO_JSON_ID_) references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_PROCDEF_INFO_PROC on ACT_PROCDEF_INFO (PROC_DEF_ID_);
alter table ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_PROCDEF foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF (ID_);
alter table ACT_PROCDEF_INFO
    add constraint ACT_UNIQ_INFO_PROCDEF unique (PROC_DEF_ID_);
create index ACT_IDX_MEMB_GROUP on ACT_ID_MEMBERSHIP (GROUP_ID_);
alter table ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_GROUP foreign key (GROUP_ID_) references ACT_ID_GROUP (ID_);
create index ACT_IDX_MEMB_USER on ACT_ID_MEMBERSHIP (USER_ID_);
alter table ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_USER foreign key (USER_ID_) references ACT_ID_USER (ID_);
create index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST (END_TIME_);
create index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST (BUSINESS_KEY_);
create index ACT_IDX_HI_ACT_INST_START on ACT_HI_ACTINST (START_TIME_);
create index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST (END_TIME_);
create index ACT_IDX_HI_IDENT_LNK_USER on ACT_HI_IDENTITYLINK (USER_ID_);
create index ACT_IDX_HI_IDENT_LNK_TASK on ACT_HI_IDENTITYLINK (TASK_ID_);
create index ACT_IDX_HI_IDENT_LNK_PROCINST on ACT_HI_IDENTITYLINK (PROC_INST_ID_);
create index ACT_IDX_HI_ACT_INST_PROCINST on ACT_HI_ACTINST (PROC_INST_ID_, ACT_ID_);
create index ACT_IDX_HI_ACT_INST_EXEC on ACT_HI_ACTINST (EXECUTION_ID_, ACT_ID_);
create index ACT_IDX_HI_TASK_INST_PROCINST on ACT_HI_TASKINST (PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_PROC_INST on ACT_HI_VARINST (PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_NAME_TYPE on ACT_HI_VARINST (NAME_, VAR_TYPE_);
create index ACT_IDX_HI_PROCVAR_TASK_ID on ACT_HI_VARINST (TASK_ID_);
create index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL (PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL (TIME_);
create index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL (NAME_);
create index ACT_IDX_HI_DETAIL_TASK_ID on ACT_HI_DETAIL (TASK_ID_);
create index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL (ACT_INST_ID_);
create index ACT_IDX_VARIABLE_TASK_ID on ACT_RU_VARIABLE (TASK_ID_);
create index ACT_IDX_VAR_EXE on ACT_RU_VARIABLE (EXECUTION_ID_);
alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_EXE
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_VAR_PROCINST on ACT_RU_VARIABLE (PROC_INST_ID_);
alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);
create index ACT_IDX_VAR_BYTEARRAY on ACT_RU_VARIABLE (BYTEARRAY_ID_);
alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_BYTEARRAY
        foreign key (BYTEARRAY_ID_)
            references ACT_GE_BYTEARRAY (ID_);

-- 更新activiti主键起始值
update act_ge_property set value_ = '1000000', rev_ = 1 where name_='next.dbid'