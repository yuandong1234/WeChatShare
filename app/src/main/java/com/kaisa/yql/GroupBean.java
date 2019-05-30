package com.kaisa.yql;

import java.util.List;

public class GroupBean {
    public String avatar;
    public String name;
    public String countdown;
    public String numberLeft;
    public List<Member> members;

    public static class Member{
        public String avatar;
        public String name;
    }
}
