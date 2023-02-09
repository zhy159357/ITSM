package com.ruoyi.form.constants;

public class ProblemConstant {
    public static final String ID = "id";
    public static final String TASK_ID = "taskId";
    public static final String PROBLEM_SHEET = "problemSheet";
    public static final String NUM = "num";
    public static final String PROBLEM_ORIGINATOR_NAME = "problemOriginatorName";
    public static final String PROBLEM_ORIGINATE_DEPART_MANAGER_NAME = "problemOriginateDepartManagerName";
    public static final String STATUS = "status";
    public static final String STAGE = "stage";
    public static final String FOR_ERROR = "for_error";
    public static final String ORIGINATOR_ID = "originator_id";
    public static final String ORI_DEP_ID = "ori_dep_id";
    public static final String SOLVER_ID = "solver_id";
    public static final String PROBLEM_REOPEN_NUM = "problem_reopen_num";
    public static final String AUDITOR_ID = "auditor_id";
    public static final String ORI_DEP_MANAGER_ID = "ori_dep_manager_id";
    public static final String PLAN_COMPLETE_TIME = "plan_complete_time";
    public static final String SOLUTION_SUMMARY = "solution_summary";
    public static final String INSTANCE_ID = "instance_id";
    public static final String PLAN_COMPLETE_TIME_MODIFY_NUM = "plan_complete_time_modify_num";
    public static final String SOLUTION_MODIFY_NUM = "solution_modify_num";
    public static final String GENERAL_MANAGER_IDS = "generalManagerIds";
    public static final String MANAGER_ID = "manager_id";
    public static final String CURRENT_HANDLER_ID = "current_deal_id";
    public static final String PROBLEM_NO = "problem_no";
    public static final String SOLVER_DEP_ID = "solver_dep_id";
    public static final String INTERRUPT_FLAG = "interrupt_flag";
    public static final String CAUSE_CLZ1 = "cause_clz1";
    public static final String CAUSE_CLZ2 = "cause_clz2";
    public static final String PROBLEM_CATEGORY = "problem_category";
    public static final String PROBLEM_SUBCLZ = "problem_subclz";
    public static final String PROBLEM_ENTRY = "problem_entry";
    public static final String PROBLEM_SUBENTRY1 = "problem_subentry1";
    public static final String PROBLEM_SUBENTRY2 = "problem_subentry2";
    public static final String SOLVER_LAST_UPDATED = "solver_last_updated";
    public static final String SOLVE_TIME = "solve_time";
    public static final String PROBLEM_SOURCE = "problem_source";
    public static final String EVENT_NO = "event_no";
    public static final String CANCEL_REASON = "cancel_reason";
    public static final String CANCEL_NOTE = "cancel_note";
    public static final String SOLVER_CLEAR_TIME = "solver_clear_time";
    public static final String ORG_ID = "org_id";
    public static final String SUBMIT_SOLUTION_TIME = "submit_solution_time";
    public static final String SUBMIT_TIME = "submit_time";
    public static final String PROBLEM_CREATED_TIME = "problem_created_time";
    public static final String PROBLEM_UPDATE_TIME = "problem_update_time";
    public static final String CAUSE_SUMMARY = "cause_summary";
    public static final String TEMP_SOLUTIONS = "temp_solutions";
    public static final String RESOLUTION_COMPLETION = "resolution_completion";
    public static final String OBSERVE_TIME = "observe_time";
    public static final String OBSERVE_NOTE = "observe_note";
    public static final String CLOSE_TYPE = "close_type";
    public static final String PROBLEM_CONTENT = "problem_content";
    public static final String CURRENT_TASK_HANDLER_ID = "current_handler_id";
    public static final String FOR_ERROR_FIELD = "for_error";
    public static final String MANAGER_GROUP_ID = "manager_group_id";
    public static final String CURRENT_HANDLER_ID_TASK = "current_handler_id";
    public static final String PROBLEM_TITLE = "problem_title";
    public static final String CREATED_TIME = "created_time";
    public static final String CLOSE_TIME = "close_time";
    public static final String CURRENT_DEAL_ID = "current_deal_id";
    public static final String UPDATED_TIME = "updated_time";
    public static final String CANCEL_POP_UP_TASK = "[\n" +
            "  {\n" +
            "    \"identify\": \"el-form-item\",\n" +
            "    \"tag\": \"el-form-item\",\n" +
            "    \"title\": \"el表单项\",\n" +
            "    \"slots\": true,\n" +
            "    \"canMove\": true,\n" +
            "    \"canAllowTo\": true,\n" +
            "    \"display\": true,\n" +
            "    \"props\": {\n" +
            "      \"prop\": {\n" +
            "        \"default\": \"close_reason\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"label\": {\n" +
            "        \"default\": \"关闭理由\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"label-width\": {\n" +
            "        \"default\": \"120px\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"required\": {\n" +
            "        \"default\": true,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"rules\": {\n" +
            "        \"default\": {},\n" +
            "        \"sourceDefault\": {}\n" +
            "      },\n" +
            "      \"error\": {\n" +
            "        \"default\": \"\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"show-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": true\n" +
            "      },\n" +
            "      \"inline-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"size\": {\n" +
            "        \"default\": \"default\",\n" +
            "        \"sourceDefault\": \"default\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"tagSlots\": {\n" +
            "      \"enable\": false,\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"childrens\": [\n" +
            "      {\n" +
            "        \"identify\": \"remote-select\",\n" +
            "        \"tag\": \"remote-select\",\n" +
            "        \"title\": \"远程选择器\",\n" +
            "        \"slots\": false,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": false,\n" +
            "        \"display\": true,\n" +
            "        \"props\": {\n" +
            "          \"remote-info\": {\n" +
            "            \"default\": \"{\\\"url\\\":\\\"/system/dict/data/listPubParaValue\\\",\\\"info\\\":[{\\\"params\\\":\\\"paraId\\\",\\\"source\\\":\\\"Write\\\",\\\"val\\\":\\\"ae7cc806a2614055a9c0adbcc6e58803\\\"}]}\"\n" +
            "          },\n" +
            "          \"multiple\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"disabled\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"size\": {\n" +
            "            \"default\": \"default\",\n" +
            "            \"sourceDefault\": \"default\"\n" +
            "          },\n" +
            "          \"clearable\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"placeholder\": {\n" +
            "            \"default\": \"请选择\",\n" +
            "            \"sourceDefault\": \"请选择\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"childrens\": [],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"constant\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"id\": \"remote-select-41KIBYAHME\",\n" +
            "        \"father_id\": \"el-form-item-Cxmnr15xZ3\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"headerSlot\": [],\n" +
            "    \"compatibility\": false,\n" +
            "    \"v_model\": {\n" +
            "      \"type\": \"none\",\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"id\": \"el-form-item-Cxmnr15xZ3\",\n" +
            "    \"father_id\": \"\"\n" +
            "  }\n" +
            "]";
    public static final String CANCEL_POP_UP = "[\n" +
            "  {\n" +
            "    \"identify\": \"el-form\",\n" +
            "    \"tag\": \"el-form\",\n" +
            "    \"title\": \"el表单容器\",\n" +
            "    \"slots\": true,\n" +
            "    \"canMove\": true,\n" +
            "    \"canAllowTo\": false,\n" +
            "    \"display\": true,\n" +
            "    \"props\": {\n" +
            "      \"inline\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"label-position\": {\n" +
            "        \"default\": \"right\",\n" +
            "        \"sourceDefault\": \"right\"\n" +
            "      },\n" +
            "      \"label-width\": {\n" +
            "        \"default\": \"50px\",\n" +
            "        \"sourceDefault\": \"50px\"\n" +
            "      },\n" +
            "      \"label-suffix\": {\n" +
            "        \"default\": \"\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"hide-required-asterisk\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"show-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"inline-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"status-icon\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"validate-on-rule-change\": {\n" +
            "        \"default\": true,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"size\": {\n" +
            "        \"default\": \"default\",\n" +
            "        \"sourceDefault\": \"default\"\n" +
            "      },\n" +
            "      \"disabled\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      }\n" +
            "    },\n" +
            "    \"extProps\": {\n" +
            "      \"列容器个数\": \"ChangeChildrenCount\"\n" +
            "    },\n" +
            "    \"tagSlots\": {\n" +
            "      \"enable\": false,\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"childrens\": [\n" +
            "      {\n" +
            "        \"identify\": \"el-form-item\",\n" +
            "        \"tag\": \"el-form-item\",\n" +
            "        \"title\": \"el表单项\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"props\": {\n" +
            "          \"prop\": {\n" +
            "            \"default\": \"cancel_reason\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label\": {\n" +
            "            \"default\": \"问题取消原因\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label-width\": {\n" +
            "            \"default\": \"120px\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"required\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"rules\": {\n" +
            "            \"default\": {},\n" +
            "            \"sourceDefault\": {}\n" +
            "          },\n" +
            "          \"error\": {\n" +
            "            \"default\": \"\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"show-message\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": true\n" +
            "          },\n" +
            "          \"inline-message\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"size\": {\n" +
            "            \"default\": \"default\",\n" +
            "            \"sourceDefault\": \"default\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"remote-select\",\n" +
            "            \"tag\": \"remote-select\",\n" +
            "            \"title\": \"远程选择器\",\n" +
            "            \"slots\": false,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": false,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"remote-info\": {\n" +
            "                \"default\": \"{\\\"url\\\":\\\"/system/dict/data/listPubParaValue\\\",\\\"info\\\":[{\\\"params\\\":\\\"paraId\\\",\\\"source\\\":\\\"Write\\\",\\\"val\\\":\\\"8006995242d74e9e8ac398197b610702\\\"}]}\"\n" +
            "              },\n" +
            "              \"multiple\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"disabled\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"small\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              },\n" +
            "              \"clearable\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"placeholder\": {\n" +
            "                \"default\": \"请选择\",\n" +
            "                \"sourceDefault\": \"请选择\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"childrens\": [],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"constant\",\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"id\": \"remote-select-cXILj2s9Sh\",\n" +
            "            \"father_id\": \"el-form-item-Y9k9mnb8vH\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"compatibility\": false,\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"id\": \"el-form-item-Y9k9mnb8vH\",\n" +
            "        \"father_id\": \"el-form-FTJ6NyHaei\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"identify\": \"el-form-item\",\n" +
            "        \"tag\": \"el-form-item\",\n" +
            "        \"title\": \"el表单项\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"props\": {\n" +
            "          \"prop\": {\n" +
            "            \"default\": \"cancel_note\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label\": {\n" +
            "            \"default\": \"问题取消说明\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label-width\": {\n" +
            "            \"default\": \"120px\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"required\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"rules\": {\n" +
            "            \"default\": {},\n" +
            "            \"sourceDefault\": {}\n" +
            "          },\n" +
            "          \"error\": {\n" +
            "            \"default\": \"\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"show-message\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": true\n" +
            "          },\n" +
            "          \"inline-message\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"size\": {\n" +
            "            \"default\": \"default\",\n" +
            "            \"sourceDefault\": \"default\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"el-input\",\n" +
            "            \"tag\": \"el-input\",\n" +
            "            \"title\": \"el输入框\",\n" +
            "            \"slots\": false,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": false,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"type\": {\n" +
            "                \"default\": \"textarea\",\n" +
            "                \"sourceDefault\": \"text\"\n" +
            "              },\n" +
            "              \"maxlength\": {\n" +
            "                \"default\": 100,\n" +
            "                \"sourceDefault\": 100\n" +
            "              },\n" +
            "              \"minlength\": {\n" +
            "                \"default\": 0,\n" +
            "                \"sourceDefault\": 0\n" +
            "              },\n" +
            "              \"show-word-limit\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"placeholder\": {\n" +
            "                \"default\": \"请输入文字\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"clearable\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"show-password\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"disabled\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"default\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              },\n" +
            "              \"prefix-icon\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"suffix-icon\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"rows\": {\n" +
            "                \"default\": 6,\n" +
            "                \"sourceDefault\": 2\n" +
            "              },\n" +
            "              \"autosize\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"autocomplete\": {\n" +
            "                \"default\": \"off\",\n" +
            "                \"sourceDefault\": \"off\"\n" +
            "              },\n" +
            "              \"name\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"readonly\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"eventAttr\": {\n" +
            "              \"change\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"当绑定值变化时触发\",\n" +
            "                \"val\": \"console.log('ColorPicker--change:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              },\n" +
            "              \"active-change\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"显示的颜色发生改变时触发\",\n" +
            "                \"val\": \"console.log('ColorPicker--active-change:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              }\n" +
            "            },\n" +
            "            \"childrens\": [],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"compatibility\": false,\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"constant\",\n" +
            "              \"val\": \"\",\n" +
            "              \"event_name\": \"onInput\"\n" +
            "            },\n" +
            "            \"id\": \"el-input-WvV0didTqT\",\n" +
            "            \"father_id\": \"el-form-item-7wQGBtUnGE\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"compatibility\": false,\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"id\": \"el-form-item-7wQGBtUnGE\",\n" +
            "        \"father_id\": \"el-form-FTJ6NyHaei\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"headerSlot\": [],\n" +
            "    \"v_model\": {\n" +
            "      \"type\": \"none\",\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"id\": \"el-form-FTJ6NyHaei\",\n" +
            "    \"father_id\": \"\"\n" +
            "  }\n" +
            "]";
    public static final String CHM_CALE_JSOM="[\n" +
            "  {\n" +
            "    \"identify\": \"grid1-el-row\",\n" +
            "    \"tag\": \"el-row\",\n" +
            "    \"title\": \"单列栅格-行容器\",\n" +
            "    \"slots\": true,\n" +
            "    \"canMove\": true,\n" +
            "    \"canAllowTo\": false,\n" +
            "    \"display\": true,\n" +
            "    \"props\": {\n" +
            "      \"gutter\": {\n" +
            "        \"default\": 0,\n" +
            "        \"sourceDefault\": 0\n" +
            "      },\n" +
            "      \"justify\": {\n" +
            "        \"default\": \"start\",\n" +
            "        \"sourceDefault\": \"start\"\n" +
            "      },\n" +
            "      \"align\": {\n" +
            "        \"default\": \"top\",\n" +
            "        \"sourceDefault\": \"top\"\n" +
            "      },\n" +
            "      \"tag\": {\n" +
            "        \"default\": \"div\",\n" +
            "        \"sourceDefault\": \"div\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"tagSlots\": {\n" +
            "      \"enable\": false,\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"childrens\": [\n" +
            "      {\n" +
            "        \"id\": \"el-col-jKkX73cGe0\",\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"el-form-item\",\n" +
            "            \"tag\": \"el-form-item\",\n" +
            "            \"title\": \"el表单项\",\n" +
            "            \"slots\": true,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": true,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"prop\": {\n" +
            "                \"default\": \"cancle_note\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"label\": {\n" +
            "                \"default\": \"取消原因\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"label-width\": {\n" +
            "                \"default\": \"120px\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"required\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"rules\": {\n" +
            "                \"default\": {},\n" +
            "                \"sourceDefault\": {}\n" +
            "              },\n" +
            "              \"error\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"show-message\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"inline-message\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"default\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"childrens\": [\n" +
            "              {\n" +
            "                \"identify\": \"el-input\",\n" +
            "                \"tag\": \"el-input\",\n" +
            "                \"title\": \"el输入框\",\n" +
            "                \"slots\": false,\n" +
            "                \"canMove\": true,\n" +
            "                \"canAllowTo\": false,\n" +
            "                \"display\": true,\n" +
            "                \"props\": {\n" +
            "                  \"type\": {\n" +
            "                    \"default\": \"textarea\",\n" +
            "                    \"sourceDefault\": \"text\"\n" +
            "                  },\n" +
            "                  \"maxlength\": {\n" +
            "                    \"default\": 255,\n" +
            "                    \"sourceDefault\": 100\n" +
            "                  },\n" +
            "                  \"minlength\": {\n" +
            "                    \"default\": 0,\n" +
            "                    \"sourceDefault\": 0\n" +
            "                  },\n" +
            "                  \"show-word-limit\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"placeholder\": {\n" +
            "                    \"default\": \"请输入文字\",\n" +
            "                    \"sourceDefault\": \"\"\n" +
            "                  },\n" +
            "                  \"clearable\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"show-password\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"disabled\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"size\": {\n" +
            "                    \"default\": \"default\",\n" +
            "                    \"sourceDefault\": \"default\"\n" +
            "                  },\n" +
            "                  \"prefix-icon\": {\n" +
            "                    \"default\": \"\",\n" +
            "                    \"sourceDefault\": \"\"\n" +
            "                  },\n" +
            "                  \"suffix-icon\": {\n" +
            "                    \"default\": \"\",\n" +
            "                    \"sourceDefault\": \"\"\n" +
            "                  },\n" +
            "                  \"rows\": {\n" +
            "                    \"default\": 2,\n" +
            "                    \"sourceDefault\": 2\n" +
            "                  },\n" +
            "                  \"autosize\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"autocomplete\": {\n" +
            "                    \"default\": \"off\",\n" +
            "                    \"sourceDefault\": \"off\"\n" +
            "                  },\n" +
            "                  \"name\": {\n" +
            "                    \"default\": \"\",\n" +
            "                    \"sourceDefault\": \"\"\n" +
            "                  },\n" +
            "                  \"readonly\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  }\n" +
            "                },\n" +
            "                \"tagSlots\": {\n" +
            "                  \"enable\": false,\n" +
            "                  \"val\": \"\"\n" +
            "                },\n" +
            "                \"eventAttr\": {\n" +
            "                  \"change\": {\n" +
            "                    \"custom\": \"custom\",\n" +
            "                    \"tips\": \"当绑定值变化时触发\",\n" +
            "                    \"val\": \"console.log('ColorPicker--change:'+value);\",\n" +
            "                    \"anonymous_params\": [\n" +
            "                      \"value\"\n" +
            "                    ],\n" +
            "                    \"list\": []\n" +
            "                  },\n" +
            "                  \"active-change\": {\n" +
            "                    \"custom\": \"custom\",\n" +
            "                    \"tips\": \"显示的颜色发生改变时触发\",\n" +
            "                    \"val\": \"console.log('ColorPicker--active-change:'+value);\",\n" +
            "                    \"anonymous_params\": [\n" +
            "                      \"value\"\n" +
            "                    ],\n" +
            "                    \"list\": []\n" +
            "                  }\n" +
            "                },\n" +
            "                \"childrens\": [],\n" +
            "                \"headerSlot\": [],\n" +
            "                \"compatibility\": false,\n" +
            "                \"v_model\": {\n" +
            "                  \"type\": \"constant\",\n" +
            "                  \"val\": \"\",\n" +
            "                  \"event_name\": \"onInput\"\n" +
            "                },\n" +
            "                \"id\": \"el-input-4IMdLDYkxt\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"compatibility\": false,\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"none\",\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"id\": \"el-form-item-4NRm8dwPH0\",\n" +
            "            \"father_id\": \"el-col-jKkX73cGe0\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"props\": {\n" +
            "          \"span\": {\n" +
            "            \"default\": 24,\n" +
            "            \"sourceDefault\": 24\n" +
            "          },\n" +
            "          \"offset\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"push\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"pull\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"tag\": {\n" +
            "            \"default\": \"div\",\n" +
            "            \"sourceDefault\": \"div\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"identify\": \"grid1-el-col\",\n" +
            "        \"tag\": \"el-col\",\n" +
            "        \"title\": \"单列栅格-列容器\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"father_id\": \"el-row-89YT1OKxwg\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"headerSlot\": [],\n" +
            "    \"v_model\": {\n" +
            "      \"type\": \"none\",\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"extProps\": {\n" +
            "      \"列容器个数\": \"ChangeChildrenCount\"\n" +
            "    },\n" +
            "    \"id\": \"el-row-89YT1OKxwg\",\n" +
            "    \"father_id\": \"\"\n" +
            "  }\n" +
            "]";
    public static final String PLAN_COMPLETE_TIME_JSON = "[\n" +
            "  {\n" +
            "    \"identify\": \"el-form\",\n" +
            "    \"tag\": \"el-form\",\n" +
            "    \"title\": \"el表单容器\",\n" +
            "    \"slots\": true,\n" +
            "    \"canMove\": true,\n" +
            "    \"canAllowTo\": false,\n" +
            "    \"display\": true,\n" +
            "    \"props\": {\n" +
            "      \"inline\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"label-position\": {\n" +
            "        \"default\": \"right\",\n" +
            "        \"sourceDefault\": \"right\"\n" +
            "      },\n" +
            "      \"label-width\": {\n" +
            "        \"default\": \"50px\",\n" +
            "        \"sourceDefault\": \"50px\"\n" +
            "      },\n" +
            "      \"label-suffix\": {\n" +
            "        \"default\": \"\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"hide-required-asterisk\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"show-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"inline-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"status-icon\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"validate-on-rule-change\": {\n" +
            "        \"default\": true,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"size\": {\n" +
            "        \"default\": \"default\",\n" +
            "        \"sourceDefault\": \"default\"\n" +
            "      },\n" +
            "      \"disabled\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      }\n" +
            "    },\n" +
            "    \"extProps\": {\n" +
            "      \"列容器个数\": \"ChangeChildrenCount\"\n" +
            "    },\n" +
            "    \"tagSlots\": {\n" +
            "      \"enable\": false,\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"childrens\": [\n" +
            "      {\n" +
            "        \"identify\": \"el-form-item\",\n" +
            "        \"tag\": \"el-form-item\",\n" +
            "        \"title\": \"el表单项\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"props\": {\n" +
            "          \"prop\": {\n" +
            "            \"default\": \"plan_complete_time\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label\": {\n" +
            "            \"default\": \"计划完成时间\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label-width\": {\n" +
            "            \"default\": \"135px\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"required\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"rules\": {\n" +
            "            \"default\": {},\n" +
            "            \"sourceDefault\": {}\n" +
            "          },\n" +
            "          \"error\": {\n" +
            "            \"default\": \"\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"show-message\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": true\n" +
            "          },\n" +
            "          \"inline-message\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"size\": {\n" +
            "            \"default\": \"default\",\n" +
            "            \"sourceDefault\": \"default\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"common-date-picker\",\n" +
            "            \"tag\": \"common-date-picker\",\n" +
            "            \"title\": \"通用日期选择器\",\n" +
            "            \"slots\": false,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": false,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"disabled-date-config\": {\n" +
            "                \"default\": \"1\",\n" +
            "                \"sourceDefault\": \"0\"\n" +
            "              },\n" +
            "              \"readonly\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"disabled\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"default\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              },\n" +
            "              \"editable\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"clearable\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"placeholder\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"start-placeholder\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"end-placeholder\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"type\": {\n" +
            "                \"default\": \"date\",\n" +
            "                \"sourceDefault\": \"date\"\n" +
            "              },\n" +
            "              \"format\": {\n" +
            "                \"default\": null,\n" +
            "                \"sourceDefault\": null\n" +
            "              },\n" +
            "              \"popper-class\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"range-separator\": {\n" +
            "                \"default\": \"-\",\n" +
            "                \"sourceDefault\": \"-\"\n" +
            "              },\n" +
            "              \"unlink-panels\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"prefix-icon\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"clear-icon\": {\n" +
            "                \"default\": \"CircleClose\",\n" +
            "                \"sourceDefault\": \"CircleClose\"\n" +
            "              },\n" +
            "              \"validate-event\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"value-format\": {\n" +
            "                \"default\": \"YYYY-MM-DD\",\n" +
            "                \"sourceDefault\": null\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"childrens\": [],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"constant\",\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"id\": \"common-date-picker-u0sIwuE1oN\",\n" +
            "            \"father_id\": \"el-form-item-GtTWnrzR0j\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"compatibility\": false,\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"id\": \"el-form-item-GtTWnrzR0j\",\n" +
            "        \"father_id\": \"el-form-q4GU0IrKtp\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"identify\": \"el-form-item\",\n" +
            "        \"tag\": \"el-form-item\",\n" +
            "        \"title\": \"el表单项\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"props\": {\n" +
            "          \"prop\": {\n" +
            "            \"default\": \"approved_opinion\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label\": {\n" +
            "            \"default\": \"修改理由\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label-width\": {\n" +
            "            \"default\": \"135px\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"required\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"rules\": {\n" +
            "            \"default\": {},\n" +
            "            \"sourceDefault\": {}\n" +
            "          },\n" +
            "          \"error\": {\n" +
            "            \"default\": \"\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"show-message\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": true\n" +
            "          },\n" +
            "          \"inline-message\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"size\": {\n" +
            "            \"default\": \"default\",\n" +
            "            \"sourceDefault\": \"default\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"el-input\",\n" +
            "            \"tag\": \"el-input\",\n" +
            "            \"title\": \"el输入框\",\n" +
            "            \"slots\": false,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": false,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"type\": {\n" +
            "                \"default\": \"textarea\",\n" +
            "                \"sourceDefault\": \"text\"\n" +
            "              },\n" +
            "              \"maxlength\": {\n" +
            "                \"default\": 100,\n" +
            "                \"sourceDefault\": 100\n" +
            "              },\n" +
            "              \"minlength\": {\n" +
            "                \"default\": 0,\n" +
            "                \"sourceDefault\": 0\n" +
            "              },\n" +
            "              \"show-word-limit\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"placeholder\": {\n" +
            "                \"default\": \"请输入文字\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"clearable\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"show-password\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"disabled\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"default\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              },\n" +
            "              \"prefix-icon\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"suffix-icon\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"rows\": {\n" +
            "                \"default\": 6,\n" +
            "                \"sourceDefault\": 2\n" +
            "              },\n" +
            "              \"autosize\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"autocomplete\": {\n" +
            "                \"default\": \"off\",\n" +
            "                \"sourceDefault\": \"off\"\n" +
            "              },\n" +
            "              \"name\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"readonly\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"eventAttr\": {\n" +
            "              \"change\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"当绑定值变化时触发\",\n" +
            "                \"val\": \"console.log('ColorPicker--change:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              },\n" +
            "              \"active-change\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"显示的颜色发生改变时触发\",\n" +
            "                \"val\": \"console.log('ColorPicker--active-change:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              }\n" +
            "            },\n" +
            "            \"childrens\": [],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"compatibility\": false,\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"constant\",\n" +
            "              \"val\": \"\",\n" +
            "              \"event_name\": \"onInput\"\n" +
            "            },\n" +
            "            \"id\": \"el-input-JOw813Dbpv\",\n" +
            "            \"father_id\": \"el-form-item-s0cqB9XhXq\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"compatibility\": false,\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"id\": \"el-form-item-s0cqB9XhXq\",\n" +
            "        \"father_id\": \"el-form-q4GU0IrKtp\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"headerSlot\": [],\n" +
            "    \"v_model\": {\n" +
            "      \"type\": \"none\",\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"id\": \"el-form-q4GU0IrKtp\",\n" +
            "    \"father_id\": \"\"\n" +
            "  }\n" +
            "]";

    public static final String GENERAL_MANAGE_POP_UP = "[\n" +
            "  {\n" +
            "    \"identify\": \"el-form\",\n" +
            "    \"tag\": \"el-form\",\n" +
            "    \"title\": \"el表单容器\",\n" +
            "    \"slots\": true,\n" +
            "    \"canMove\": true,\n" +
            "    \"canAllowTo\": false,\n" +
            "    \"display\": true,\n" +
            "    \"props\": {\n" +
            "      \"inline\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"label-position\": {\n" +
            "        \"default\": \"right\",\n" +
            "        \"sourceDefault\": \"right\"\n" +
            "      },\n" +
            "      \"label-width\": {\n" +
            "        \"default\": \"50px\",\n" +
            "        \"sourceDefault\": \"50px\"\n" +
            "      },\n" +
            "      \"label-suffix\": {\n" +
            "        \"default\": \"\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"hide-required-asterisk\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"show-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"inline-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"status-icon\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"validate-on-rule-change\": {\n" +
            "        \"default\": true,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"size\": {\n" +
            "        \"default\": \"default\",\n" +
            "        \"sourceDefault\": \"default\"\n" +
            "      },\n" +
            "      \"disabled\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      }\n" +
            "    },\n" +
            "    \"extProps\": {\n" +
            "      \"列容器个数\": \"ChangeChildrenCount\"\n" +
            "    },\n" +
            "    \"tagSlots\": {\n" +
            "      \"enable\": false,\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"childrens\": [\n" +
            "      {\n" +
            "        \"identify\": \"el-form-item\",\n" +
            "        \"tag\": \"el-form-item\",\n" +
            "        \"title\": \"el表单项\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"props\": {\n" +
            "          \"prop\": {\n" +
            "            \"default\": \"approved_person\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label\": {\n" +
            "            \"default\": \"取消总经理\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"label-width\": {\n" +
            "            \"default\": \"120px\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"required\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"rules\": {\n" +
            "            \"default\": {},\n" +
            "            \"sourceDefault\": {}\n" +
            "          },\n" +
            "          \"error\": {\n" +
            "            \"default\": \"\",\n" +
            "            \"sourceDefault\": \"\"\n" +
            "          },\n" +
            "          \"show-message\": {\n" +
            "            \"default\": true,\n" +
            "            \"sourceDefault\": true\n" +
            "          },\n" +
            "          \"inline-message\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"size\": {\n" +
            "            \"default\": \"default\",\n" +
            "            \"sourceDefault\": \"default\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"el-select-v2\",\n" +
            "            \"tag\": \"el-select-v2\",\n" +
            "            \"title\": \"el-虚拟选择器\",\n" +
            "            \"slots\": false,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": true,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"multiple\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"options\": {\n" +
            "                \"default\": [\n" +
            "                  {\n" +
            "                    \"label\": \"选项一\",\n" +
            "                    \"value\": 1\n" +
            "                  },\n" +
            "                  {\n" +
            "                    \"label\": \"选项二\",\n" +
            "                    \"value\": 2\n" +
            "                  }\n" +
            "                ],\n" +
            "                \"sourceDefault\": [],\n" +
            "                \"isRemote\": false\n" +
            "              },\n" +
            "              \"disabled\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"value-key\": {\n" +
            "                \"default\": \"value\",\n" +
            "                \"sourceDefault\": \"value\"\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"default\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              },\n" +
            "              \"clearable\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"clear-icon\": {\n" +
            "                \"default\": \"CircleClose\",\n" +
            "                \"sourceDefault\": \"CircleClose\"\n" +
            "              },\n" +
            "              \"collapse-tags\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"collapse-tags-tooltip\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"multiple-limit\": {\n" +
            "                \"default\": 0,\n" +
            "                \"sourceDefault\": 0\n" +
            "              },\n" +
            "              \"name\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"effect\": {\n" +
            "                \"default\": \"light\",\n" +
            "                \"sourceDefault\": \"light\"\n" +
            "              },\n" +
            "              \"autocomplete\": {\n" +
            "                \"default\": \"off\",\n" +
            "                \"sourceDefault\": \"off\"\n" +
            "              },\n" +
            "              \"placeholder\": {\n" +
            "                \"default\": \"请选择\",\n" +
            "                \"sourceDefault\": \"请选择\"\n" +
            "              },\n" +
            "              \"filterable\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"allow-create\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"reserve-keyword\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"no-data-text\": {\n" +
            "                \"default\": \"无数据\",\n" +
            "                \"sourceDefault\": \"无数据\"\n" +
            "              },\n" +
            "              \"popper-class\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"teleported\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"persistent\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"popper-options\": {\n" +
            "                \"default\": {},\n" +
            "                \"sourceDefault\": {}\n" +
            "              },\n" +
            "              \"automatic-dropdown\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"height\": {\n" +
            "                \"default\": 170,\n" +
            "                \"sourceDefault\": 170\n" +
            "              },\n" +
            "              \"scrollbar-always-on\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"remote\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"remote-method\": {\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"eventAttr\": {\n" +
            "              \"change\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"当绑定值变化时触发\",\n" +
            "                \"val\": \"console.log('SelectV2--change:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              },\n" +
            "              \"visible-change\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"下拉框出现/隐藏时触发\",\n" +
            "                \"val\": \"console.log('SelectV2 --visible-change:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              },\n" +
            "              \"remove-tag\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"多选模式下移除tag时触发\",\n" +
            "                \"val\": \"console.log('SelectV2--remote-tag:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              },\n" +
            "              \"clear\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"可清空的单选模式下用户点击清空按钮时触发\",\n" +
            "                \"val\": \"console.log('SelectV2 --clear:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              },\n" +
            "              \"blur\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"当选择器的输入框失去焦点时触发\",\n" +
            "                \"val\": \"console.log('SelectV2--blur:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              },\n" +
            "              \"focus\": {\n" +
            "                \"custom\": \"custom\",\n" +
            "                \"tips\": \"当选择器的输入框获得焦点时触发\",\n" +
            "                \"val\": \"console.log('SelectV2--focus:'+value);\",\n" +
            "                \"anonymous_params\": [\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"list\": []\n" +
            "              }\n" +
            "            },\n" +
            "            \"childrens\": [],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"compatibility\": false,\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"constant\",\n" +
            "              \"val\": [],\n" +
            "              \"event_name\": \"onChange\"\n" +
            "            },\n" +
            "            \"id\": \"el-select-v2-s9yiSXgQSv\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"compatibility\": false,\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"id\": \"el-form-item-b68NgkmLyU\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"headerSlot\": [],\n" +
            "    \"v_model\": {\n" +
            "      \"type\": \"none\",\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"id\": \"el-form-EIGJQNDNsm\"\n" +
            "  }\n" +
            "]";
    public static final String CHM_GROUP_PERSON="[\n" +
            "  {\n" +
            "    \"identify\": \"grid2-el-row\",\n" +
            "    \"tag\": \"el-row\",\n" +
            "    \"title\": \"双列栅格-行容器\",\n" +
            "    \"slots\": true,\n" +
            "    \"canMove\": true,\n" +
            "    \"canAllowTo\": false,\n" +
            "    \"display\": true,\n" +
            "    \"props\": {\n" +
            "      \"gutter\": {\n" +
            "        \"default\": 0,\n" +
            "        \"sourceDefault\": 0\n" +
            "      },\n" +
            "      \"justify\": {\n" +
            "        \"default\": \"start\",\n" +
            "        \"sourceDefault\": \"start\"\n" +
            "      },\n" +
            "      \"align\": {\n" +
            "        \"default\": \"top\",\n" +
            "        \"sourceDefault\": \"top\"\n" +
            "      },\n" +
            "      \"tag\": {\n" +
            "        \"default\": \"div\",\n" +
            "        \"sourceDefault\": \"div\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"tagSlots\": {\n" +
            "      \"enable\": false,\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"childrens\": [\n" +
            "      {\n" +
            "        \"id\": \"el-col-h2V4OkXir7\",\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"el-form-item\",\n" +
            "            \"tag\": \"el-form-item\",\n" +
            "            \"title\": \"el表单项\",\n" +
            "            \"slots\": true,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": true,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"prop\": {\n" +
            "                \"default\": \"assigned_group\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"label\": {\n" +
            "                \"default\": \"受派组\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"label-width\": {\n" +
            "                \"default\": \"120px\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"required\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"rules\": {\n" +
            "                \"default\": {},\n" +
            "                \"sourceDefault\": {}\n" +
            "              },\n" +
            "              \"error\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"show-message\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"inline-message\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"default\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"childrens\": [\n" +
            "              {\n" +
            "                \"identify\": \"remote-select\",\n" +
            "                \"tag\": \"remote-select\",\n" +
            "                \"title\": \"受派组选择器\",\n" +
            "                \"slots\": false,\n" +
            "                \"canMove\": true,\n" +
            "                \"canAllowTo\": false,\n" +
            "                \"display\": true,\n" +
            "                \"props\": {\n" +
            "                  \"remote-info\": {\n" +
            "                    \"default\": \"{\\\"url\\\":\\\"/system/work/incident/selectAllGroup\\\",\\\"info\\\":[{\\\"params\\\":\\\"orgId\\\",\\\"source\\\":\\\"Write\\\",\\\"val\\\":\\\"\\\\\\\"\\\\\\\"\\\"}]}\"\n" +
            "                  },\n" +
            "                  \"multiple\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"disabled\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"size\": {\n" +
            "                    \"default\": \"default\",\n" +
            "                    \"sourceDefault\": \"default\"\n" +
            "                  },\n" +
            "                  \"clearable\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"placeholder\": {\n" +
            "                    \"default\": \"请选择\",\n" +
            "                    \"sourceDefault\": \"请选择\"\n" +
            "                  },\n" +
            "                  \"filterable\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  }\n" +
            "                },\n" +
            "                \"tagSlots\": {\n" +
            "                  \"enable\": false,\n" +
            "                  \"val\": \"\"\n" +
            "                },\n" +
            "                \"childrens\": [],\n" +
            "                \"headerSlot\": [],\n" +
            "                \"eventAttr\": {\n" +
            "                  \"onChange\": {\n" +
            "                    \"custom\": \"custom\",\n" +
            "                    \"tips\": \"当绑定值变化时触发\",\n" +
            "                    \"val\": \"  console.log('SelectV2--change:'+value);\",\n" +
            "                    \"anonymous_params\": [\n" +
            "                      \"value\"\n" +
            "                    ],\n" +
            "                    \"list\": []\n" +
            "                  }\n" +
            "                },\n" +
            "                \"v_model\": {\n" +
            "                  \"type\": \"constant\",\n" +
            "                  \"val\": \"\"\n" +
            "                },\n" +
            "                \"id\": \"remote-select-Rx2SUK8E2M\",\n" +
            "                \"father_id\": \"el-form-item-Ljmxin7BoA\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"compatibility\": false,\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"none\",\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"id\": \"el-form-item-Ljmxin7BoA\",\n" +
            "            \"father_id\": \"el-col-h2V4OkXir7\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"props\": {\n" +
            "          \"span\": {\n" +
            "            \"default\": 12,\n" +
            "            \"sourceDefault\": 24\n" +
            "          },\n" +
            "          \"offset\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"push\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"pull\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"tag\": {\n" +
            "            \"default\": \"div\",\n" +
            "            \"sourceDefault\": \"div\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"identify\": \"grid2-el-col1\",\n" +
            "        \"tag\": \"el-col\",\n" +
            "        \"title\": \"双列栅格-列容器1\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"father_id\": \"el-row-NJY7jDL1bT\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"el-col-IojL2wsGyy\",\n" +
            "        \"childrens\": [\n" +
            "          {\n" +
            "            \"identify\": \"el-form-item\",\n" +
            "            \"tag\": \"el-form-item\",\n" +
            "            \"title\": \"el表单项\",\n" +
            "            \"slots\": true,\n" +
            "            \"canMove\": true,\n" +
            "            \"canAllowTo\": true,\n" +
            "            \"display\": true,\n" +
            "            \"props\": {\n" +
            "              \"prop\": {\n" +
            "                \"default\": \"assigned_person\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"label\": {\n" +
            "                \"default\": \"受派人\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"label-width\": {\n" +
            "                \"default\": \"120px\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"required\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"rules\": {\n" +
            "                \"default\": {},\n" +
            "                \"sourceDefault\": {}\n" +
            "              },\n" +
            "              \"error\": {\n" +
            "                \"default\": \"\",\n" +
            "                \"sourceDefault\": \"\"\n" +
            "              },\n" +
            "              \"show-message\": {\n" +
            "                \"default\": true,\n" +
            "                \"sourceDefault\": true\n" +
            "              },\n" +
            "              \"inline-message\": {\n" +
            "                \"default\": false,\n" +
            "                \"sourceDefault\": false\n" +
            "              },\n" +
            "              \"size\": {\n" +
            "                \"default\": \"default\",\n" +
            "                \"sourceDefault\": \"default\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"tagSlots\": {\n" +
            "              \"enable\": false,\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"childrens\": [\n" +
            "              {\n" +
            "                \"identify\": \"remote-select\",\n" +
            "                \"tag\": \"remote-select\",\n" +
            "                \"title\": \"远程选择器\",\n" +
            "                \"slots\": false,\n" +
            "                \"canMove\": true,\n" +
            "                \"canAllowTo\": false,\n" +
            "                \"display\": true,\n" +
            "                \"props\": {\n" +
            "                  \"remote-info\": {\n" +
            "                    \"default\": \"{\\\"url\\\":\\\"/system/work/incident/selectGroupUsers\\\",\\\"info\\\":[{\\\"params\\\":\\\"groupId\\\",\\\"source\\\":\\\"Component\\\",\\\"val\\\":\\\"remote-select-Rx2SUK8E2M\\\"}]}\"\n" +
            "                  },\n" +
            "                  \"multiple\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"disabled\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"size\": {\n" +
            "                    \"default\": \"default\",\n" +
            "                    \"sourceDefault\": \"default\"\n" +
            "                  },\n" +
            "                  \"clearable\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  },\n" +
            "                  \"placeholder\": {\n" +
            "                    \"default\": \"请选择\",\n" +
            "                    \"sourceDefault\": \"请选择\"\n" +
            "                  },\n" +
            "                  \"filterable\": {\n" +
            "                    \"default\": false,\n" +
            "                    \"sourceDefault\": false\n" +
            "                  }\n" +
            "                },\n" +
            "                \"tagSlots\": {\n" +
            "                  \"enable\": false,\n" +
            "                  \"val\": \"\"\n" +
            "                },\n" +
            "                \"childrens\": [],\n" +
            "                \"headerSlot\": [],\n" +
            "                \"eventAttr\": {\n" +
            "                  \"onChange\": {\n" +
            "                    \"custom\": \"custom\",\n" +
            "                    \"tips\": \"当绑定值变化时触发\",\n" +
            "                    \"val\": \"  console.log('SelectV2--change:'+value);\",\n" +
            "                    \"anonymous_params\": [\n" +
            "                      \"value\"\n" +
            "                    ],\n" +
            "                    \"list\": []\n" +
            "                  }\n" +
            "                },\n" +
            "                \"v_model\": {\n" +
            "                  \"type\": \"constant\",\n" +
            "                  \"val\": \"\"\n" +
            "                },\n" +
            "                \"id\": \"remote-select-w5Nn8R8gZl\",\n" +
            "                \"father_id\": \"el-form-item-C6sfOsbUsU\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"headerSlot\": [],\n" +
            "            \"compatibility\": false,\n" +
            "            \"v_model\": {\n" +
            "              \"type\": \"none\",\n" +
            "              \"val\": \"\"\n" +
            "            },\n" +
            "            \"id\": \"el-form-item-C6sfOsbUsU\",\n" +
            "            \"father_id\": \"el-col-IojL2wsGyy\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"props\": {\n" +
            "          \"span\": {\n" +
            "            \"default\": 12,\n" +
            "            \"sourceDefault\": 24\n" +
            "          },\n" +
            "          \"offset\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"push\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"pull\": {\n" +
            "            \"default\": 0,\n" +
            "            \"sourceDefault\": 0\n" +
            "          },\n" +
            "          \"tag\": {\n" +
            "            \"default\": \"div\",\n" +
            "            \"sourceDefault\": \"div\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"identify\": \"grid2-el-col2\",\n" +
            "        \"tag\": \"el-col\",\n" +
            "        \"title\": \"双列栅格-列容器2\",\n" +
            "        \"slots\": true,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": true,\n" +
            "        \"display\": true,\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"none\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"father_id\": \"el-row-NJY7jDL1bT\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"headerSlot\": [],\n" +
            "    \"v_model\": {\n" +
            "      \"type\": \"none\",\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"extProps\": {\n" +
            "      \"列容器个数\": \"ChangeChildrenCount\"\n" +
            "    },\n" +
            "    \"id\": \"el-row-NJY7jDL1bT\",\n" +
            "    \"father_id\": \"\"\n" +
            "  }\n" +
            "]";
    public static final String PROBLEM_TASK_CANCEL = "[\n" +
            "  {\n" +
            "    \"identify\": \"el-form-item\",\n" +
            "    \"tag\": \"el-form-item\",\n" +
            "    \"title\": \"el表单项\",\n" +
            "    \"slots\": true,\n" +
            "    \"canMove\": true,\n" +
            "    \"canAllowTo\": true,\n" +
            "    \"display\": true,\n" +
            "    \"props\": {\n" +
            "      \"prop\": {\n" +
            "        \"default\": \"close_reason\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"label\": {\n" +
            "        \"default\": \"关闭理由\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"label-width\": {\n" +
            "        \"default\": \"120px\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"required\": {\n" +
            "        \"default\": true,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"rules\": {\n" +
            "        \"default\": {},\n" +
            "        \"sourceDefault\": {}\n" +
            "      },\n" +
            "      \"error\": {\n" +
            "        \"default\": \"\",\n" +
            "        \"sourceDefault\": \"\"\n" +
            "      },\n" +
            "      \"show-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": true\n" +
            "      },\n" +
            "      \"inline-message\": {\n" +
            "        \"default\": false,\n" +
            "        \"sourceDefault\": false\n" +
            "      },\n" +
            "      \"size\": {\n" +
            "        \"default\": \"default\",\n" +
            "        \"sourceDefault\": \"default\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"tagSlots\": {\n" +
            "      \"enable\": false,\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"childrens\": [\n" +
            "      {\n" +
            "        \"identify\": \"remote-select\",\n" +
            "        \"tag\": \"remote-select\",\n" +
            "        \"title\": \"远程选择器\",\n" +
            "        \"slots\": false,\n" +
            "        \"canMove\": true,\n" +
            "        \"canAllowTo\": false,\n" +
            "        \"display\": true,\n" +
            "        \"props\": {\n" +
            "          \"remote-info\": {\n" +
            "            \"default\": \"{\\\"url\\\":\\\"/system/dict/data/listPubParaValue\\\",\\\"info\\\":[{\\\"params\\\":\\\"paraId\\\",\\\"source\\\":\\\"Write\\\",\\\"val\\\":\\\"ae7cc806a2614055a9c0adbcc6e58803\\\"}]}\"\n" +
            "          },\n" +
            "          \"multiple\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"disabled\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"size\": {\n" +
            "            \"default\": \"default\",\n" +
            "            \"sourceDefault\": \"default\"\n" +
            "          },\n" +
            "          \"clearable\": {\n" +
            "            \"default\": false,\n" +
            "            \"sourceDefault\": false\n" +
            "          },\n" +
            "          \"placeholder\": {\n" +
            "            \"default\": \"请选择\",\n" +
            "            \"sourceDefault\": \"请选择\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"tagSlots\": {\n" +
            "          \"enable\": false,\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"childrens\": [],\n" +
            "        \"headerSlot\": [],\n" +
            "        \"v_model\": {\n" +
            "          \"type\": \"constant\",\n" +
            "          \"val\": \"\"\n" +
            "        },\n" +
            "        \"id\": \"remote-select-41KIBYAHME\",\n" +
            "        \"father_id\": \"el-form-item-Cxmnr15xZ3\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"headerSlot\": [],\n" +
            "    \"compatibility\": false,\n" +
            "    \"v_model\": {\n" +
            "      \"type\": \"none\",\n" +
            "      \"val\": \"\"\n" +
            "    },\n" +
            "    \"id\": \"el-form-item-Cxmnr15xZ3\",\n" +
            "    \"father_id\": \"\"\n" +
            "  }\n" +
            "]";
    /**
     * 提交或保存标识
     */
    public interface Remark {
        String SAVE = "save";
    }

    /**
     * 已知错误标识
     */
    public interface ForError {
        String ZERO = "否";
        String ONE = "是";
    }

    /**
     * 影响业务中断标识
     */
    public interface InterruptFlag {
        String ZERO = "0";
        String ONE = "1";
        String CH_ZERO = "否";
        String CH_ONE = "是";
    }

    /**
     * 问题单来源 重大事件 事件管理
     */
    public interface ProblemSource {
        String MAJOR_EVENT = "重大事件";
        String EVENT_MANAGE = "事件管理";
    }

    /**
     * 来源于事件单标识 1-来源于事件单 0-人工页面发起
     */
    public interface FromEventFlag {
        String ZERO = "0";
        String ONE = "1";
    }
}
