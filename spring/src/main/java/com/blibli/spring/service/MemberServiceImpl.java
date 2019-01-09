package com.blibli.spring.service;

import com.blibli.spring.model.Member;
import com.blibli.spring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @PostConstruct
    public void setAdminData(){
        Member member = new Member();
        member.setUsername("admin");
        member.setPassword(encoder.encode("admin"));
        member.setRole("ROLE_ADMIN, ROLE_MEMBER");
        if(memberRepository.findByUsername("admin") == null)
            memberRepository.save(member);
    }

    @Override
    public String getToken(String username){
        return jwtService.generateToken(username);
    }

    @Override
    public boolean login(String username, String password){
        Member member = getMemberByUsername(username);
        if(encoder.matches(password, member.getPassword()))
            return true;
        return false;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = getMemberByUsername(username);
        System.out.println(member.getRole());
        return new User(username, member.getPassword(), getAuthority(member));
    }

    private List<GrantedAuthority> getAuthority(Member member){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] roles = member.getRole().split(", ");
        for(String role : roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
    }

    @Override
    public List<Member> getAllMembers(){
        return memberRepository.findAll();
    }

    @Override
    public Member getMemberById(String id){
        return memberRepository.findOne(id);
    }

    @Override
    @Transactional
    public String saveMember(Member member){
        if(member.getId() == null){
            member.setPassword(encoder.encode(member.getPassword()));
        }else{
            Member dbMember = memberRepository.findOne(member.getId());
            if(member.getPassword() != null)
                member.setPassword(encoder.encode(member.getPassword()));
            else
                member.setPassword(dbMember.getPassword());
        }
        member.setRole("ROLE_MEMBER");
        memberRepository.save(member);
        return "Save succeed!";
    }

    @Override
    public Member getMemberByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public String deleteMemberById(String id){
        memberRepository.delete(id);
        return "Delete successs!";
    }
}
