package com.example.demo.common;

public enum MemberPermission {
    ADMIN(Authority.ADMIN),
    MEMBER_001(Authority.MEMBER_001),
    MEMBER_002(Authority.MEMBER_002);

    private final String authority;

    MemberPermission(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String ADMIN = "ADMIN";
        public static final String MEMBER_001 = "MEMBER_001";
        public static final String MEMBER_002 = "MEMBER_002";
    }
}
