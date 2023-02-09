package com.ruoyi.form.enums;

public enum  ChangeTaskDefineKeyEnum {
    submitTask("sid-2ACC5CE6-10DB-4C7F-B494-C46A5B397AD3"),
    waitApproval("sid-57B89DBB-8B8C-475C-B414-964601509D2D"),
    taskPreApproval("sid-6ED99DF1-637D-4F89-9881-ABB70D93B7E5"),
    changeManagerApproval("sid-CFA9C6CA-607B-45E4-93D5-55EC207980C6"),
    adminApproval("sid-C6E42B31-1AC9-4D4F-AD3F-7E2545E62F17"),
    receive("sid-0DF54238-E884-404D-91F5-D2393A16B6F5"),
    extendTimeApproval("sid-237FF6ED-630B-4C3E-940F-18341F2A0E3E"),
    remedyApproval("sid-FE551883-AE86-4837-A595-15B89D5C5ECA"),
    implement("sid-A22F1C09-A317-4A35-9540-962654AB85EC"),
    review("sid-EEDD767C-B4BF-44E1-8B95-9282D337E54C"),
    remedySubmit("sid-367C5616-BC5E-4D54-A83A-467935210FBE"),
    branch_1("sid-EEEA0779-F466-4733-AA0A-6E6A9BCCF211"),
    branch_2("sid-2EE32284-25D1-4830-9923-922B3E498C19"),
    impl_check_update("sid-813BB90E-624E-4CF9-AE13-361A152196FF"),
    edit_task("sid-B65ACEAA-527A-4AE1-B954-352E17ABE4C3"),
    REMEDY_PRE_APPROVAL("sid-84B3D070-EA3F-413F-90CD-58F2C8C93B33");
    private String name;

    ChangeTaskDefineKeyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
