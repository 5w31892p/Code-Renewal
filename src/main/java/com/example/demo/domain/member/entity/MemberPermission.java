package com.example.demo.domain.member.entity;

public enum MemberPermission {
    ADMIIN(Authority.ADMIIN),
    MEMBER(Authority.MEMBER);

    private final String authority;

    MemberPermission(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String ADMIIN = "ADMIIN";
        public static final String MEMBER = "MEMBER";
    }
}
