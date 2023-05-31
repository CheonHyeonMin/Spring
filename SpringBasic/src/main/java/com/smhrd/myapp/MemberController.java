package com.smhrd.myapp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.myapp.domain.WebMember;
import com.smhrd.myapp.mapper.MemberMapper;

@Controller
public class MemberController {
	
	@Autowired
	private MemberMapper mapper;
	
	@RequestMapping(value="/member/join", method=RequestMethod.POST)
	public String join(@RequestParam("email") String email, @RequestParam("pw") String pw, 
						@RequestParam("tel") String tel, @RequestParam("address") String address) {
		
		WebMember wm = new WebMember(email, pw, tel, address);
		
		
		int cnt = mapper.join(wm);
		System.out.println(cnt);
		
		
		if(cnt>0) { // 회원가입 성공, redirect로 재 요청할때는 param값을 모름 때문에 다시 값을 줘야됨
			return "redirect:/joinsuccess?email="+email;   //joinsuccess로 email값 보내면 homecontroller에 있는 joinsuccess가 받음
		}
		else { //회원가입 실패
			return "redirect:/";
		}
	}
	
	@RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("pw") String pw, HttpSession session) {
		WebMember wm = new WebMember(email, pw);
		WebMember loginMember = mapper.login(wm);
		session.setAttribute("loginMember", loginMember);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/member/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("loginMember");
		return "redirect:/";
		
	}
	
	@RequestMapping(value="/member/delete/{email}")
	public String delete(@PathVariable("email") String email) {
		//RequestParam : 쿼리스트링, post packet 바디 데이터, 데이터를 각각 받을 수 있음
		//PathVariable : 경로에 포함된 데이터 가지고 올 때 
		System.out.println(email);
		WebMember wm = new WebMember(email);
		int deleteMember = mapper.delete(wm);
		
		return "redirect:/select";

	}
	
//	@RequestParam("email") String email, @RequestParam("pw") String pw, @RequestParam("tel") String tel, @RequestParam("address") String address
	@RequestMapping(value="/member/update" , method=RequestMethod.POST)
	public String update( ) {
//		WebMember wm = new WebMember(email,pw,tel,address);
//		int updateMember = mapper.update(wm);
		
		return "redirect:/";
	}
	
	
	
	
	
}
