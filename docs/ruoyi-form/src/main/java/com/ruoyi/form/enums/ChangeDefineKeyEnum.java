package com.ruoyi.form.enums;

public enum ChangeDefineKeyEnum {
    submit("submitChange"),
    changeMangerPrepared("changeManger"),
    completion("sid-AB373D97-9448-425C-9ABA-FA2A40281DD8"),
    waitTaskPreOver("waitTaskPreOver"),
    changeMangerApproval("sid-295180A5-7CD2-4367-AA0B-5BD89D6B2118"),
    adminApproval("sid-0EE4432B-2AA3-485F-8171-0203030AC04A"),
    generalManager("leader"),
    waitOver("waitOver"),
    branchManager("sid-CA5B3BD4-3735-488C-94CA-78DC8E57B0B1"),
    branchGeneralManager("sid-BEAC099A-11DC-4D9D-AE6C-BD077B6A1C4A"),
    impling("sid-103FCC37-683E-43E5-B58C-EF89B977E2B1"),
    preparedApproval("sid-F488F143-0FF2-4BED-8819-C394567C56F0"),
    changeMangerApprovalWhenBranch("sid-F7C4D417-589C-46AE-B0A9-50D871383AA6");
    private String name;

    ChangeDefineKeyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
