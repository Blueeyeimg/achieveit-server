package com.ecnu.achieveit.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 倪事通
 */

public enum ProjectRole {

    /**
     * 项目成员在项目中的角色，区别于在人事系统中的身份
     */
    MANAGER("项目经理"),

    DEVELOP_LEADER("开发Leader"),

    TEST_LEADER("测试Leader"),

    DEVELOPER("开发"),

    TEST("测试"),

    EPG("EPG"),

    QA("QA"),

    CONFIG("配置管理员");

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    ProjectRole(String role) {
        this.role = role;
    }

    public static boolean valid(String role){
        List<String> roles = Arrays.stream(role.split(",")).collect(Collectors.toList());

        long count = Arrays.stream(ProjectRole.values()).map(ProjectRole::getRole).filter(roles::contains).count();
        return count == roles.size();
    }

    public boolean in(String role){
        long count = Arrays.stream(role.split(",")).filter(r -> this.role.equals(r)).count();
        return count != 0;
    }

}
