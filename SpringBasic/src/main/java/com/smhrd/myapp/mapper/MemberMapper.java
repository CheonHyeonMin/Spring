package com.smhrd.myapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.smhrd.myapp.domain.WebMember;

@Mapper
public interface MemberMapper {
	//회원가입
	public int join(WebMember m);
	
	// 로그인
	public WebMember login(WebMember m);
	
	//전체회원정보
	public List<WebMember> select();
	
	//삭제
	public int delete(WebMember m);
	
	//수정
	public int update(WebMember m);
}



