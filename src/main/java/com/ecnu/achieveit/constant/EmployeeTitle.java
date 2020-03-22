package com.ecnu.achieveit.constant;

public enum EmployeeTitle {

    /**
     * 员工类别
     */
    MANAGER("manager", "项目经理"),

    BOSS("boss", "项目上级"),

    DEVELOPER("developer", "开发"),

    EPG_LEADER("epg_leader", "EPG Leader"),

    ORG_CONFIG("org_config", "组织级配置管理员"),

    QA_LEADER("qa_leader", "QA经理"),

    EPG("epg", "EPG"),

    QA("qa", "QA"),

    TEST("test", "测试");

    private String title;

    private String titleName;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    EmployeeTitle(String title, String titleName){
        this.title = title;
        this.titleName = titleName;
    }
}
