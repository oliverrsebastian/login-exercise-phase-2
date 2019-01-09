package com.blibli.spring.controller;

import com.blibli.spring.model.Member;
import com.blibli.spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/library")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping(value = "/member/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Member> getAllMember(){
        return memberService.getAllMembers();
    }

    @GetMapping(value = "/member/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(key = "#id", value = "member")
    public Member getMemberById(@PathVariable String id){
        return memberService.getMemberById(id);
    }

    @PostMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> register(@RequestBody Member member){
        String success = memberService.saveMember(member);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setValue(success);
        return baseResponse;
    }

    @GetMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> getRole(@AuthenticationPrincipal final User user){
        if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            BaseResponse<String> baseResponse = new BaseResponse<>();
            baseResponse.setStatus(HttpStatus.OK.toString());
            baseResponse.setValue("ADMIN");
            return baseResponse;
        }
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setValue("MEMBER");
        return baseResponse;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> login(@RequestBody LoginRequest loginRequest){
        BaseResponse<String> response = new BaseResponse<>();
        if(memberService.login(loginRequest.getUsername(), loginRequest.getPassword())) {
            response.setStatus(HttpStatus.OK.toString());
            response.setValue(memberService.getToken(loginRequest.getUsername()));
            return response;
        }
        else {
            response.setStatus(HttpStatus.NOT_FOUND.toString());
            return response;
        }
    }

    @PutMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(key = "#member.id", value = "member")
    public BaseResponse<String> updateMember(@RequestBody Member member){
        String success= memberService.saveMember(member);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setValue(success);
        return baseResponse;
    }

    @DeleteMapping(value = "/member/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(key = "#id", value = "member")
    public BaseResponse<String> deleteMember(@PathVariable String id){
        String success = memberService.deleteMemberById(id);
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setValue(success);
        return baseResponse;
    }
}
