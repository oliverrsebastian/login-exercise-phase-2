package com.blibli.spring.service;

import com.blibli.spring.model.Member;

import java.util.List;

public interface MemberService {

    String getToken(String username);

    boolean login(String username, String password);

    List<Member> getAllMembers();

    Member getMemberById(String id);

    String saveMember(Member member);

    Member getMemberByUsername(String username);

    String deleteMemberById(String id);
}
