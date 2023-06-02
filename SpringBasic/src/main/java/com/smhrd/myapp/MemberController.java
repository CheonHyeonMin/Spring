package com.smhrd.myapp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
		
		//member/join : 클라이언트가 요청 -> 주소창에 member/join이라고 뜸
		if(cnt>0) { // 회원가입 성공, redirect로 재 요청할때는 param값을 모름 때문에 다시 값을 줘야됨
			//return "joinSuccess"; 이렇게 쓰면  서버쪽에서 찾아서 줌 -> member/join으로 뜸
			return "redirect:/joinsuccess?email="+email;   //joinsuccess로 email값 보내면 homecontroller에 있는 joinsuccess가 받음
		}
		else { //회원가입 실패
			//return "index"; -> 서버에서 찾아서 주기 때문에 ->member/join으로 뜸 때문에 joinsuccess로 가기 위해 재요청을함
			return "redirect:/"; 
		}
	}
	
	@RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("pw") String pw, HttpSession session) {
		WebMember wm = new WebMember(email, pw);
		WebMember loginMember = mapper.login(wm);
		session.setAttribute("loginMember", loginMember); //로그인할때 세션 생성
		
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
//		WebMember wm = new WebMember(email);
//		int deleteMember = mapper.delete(wm);
		int cnt = mapper.delete(email);
		
		return "redirect:/select"; 

	}
	
	//쿼리스트링으로 email받는 방법
//	@RequestParam("email") String email, @RequestParam("pw") String pw, @RequestParam("tel") String tel, @RequestParam("address") String address
//	@RequestMapping(value="/member/update/{email}" , method=RequestMethod.POST)
//	public String update(@PathVariable("email") String email, @RequestParam("pw") String pw, @RequestParam("tel") String tel, @RequestParam("address") String address) {
//		
//		
//		WebMember wm = new WebMember(email,pw,tel,address);
//		int updateMember = mapper.update(wm);
//		
//		return "redirect:/";
//	}
	
	
	@RequestMapping(value="/member/update", method=RequestMethod.POST)
	public String update(@ModelAttribute WebMember wm, HttpSession session) {
		System.out.println(wm.getEmail());
		WebMember loginMember = (WebMember)session.getAttribute("loginMember");
		wm.setEmail(loginMember.getEmail()); //Email값이 null이기 때문에 세션에 저장된 email값을 불러와주기
		
		int cnt = mapper.update(wm);
		
		if(cnt>0) {// 수정성공
			session.setAttribute("loginMember", wm);
			return "redirect:/";
		}
		else { //수정실패
			return "redirect:/update";
		}
	}
	
	//view 반환 x -> data(model) 반환
	//@Controller => view를 반환하는 메서드
	//@Controller + @ResponseBody => model을 반환하는 메서드
	@RequestMapping(value="/member/emailcheck")
	public @ResponseBody String emailCheck(@RequestParam("input") String email) {
		System.out.println(email);
		int result = mapper.emailCheck(email);
		System.out.println(result);
		
		if(result>0) {// 값 o -> 사용불가능한 이메일
			return "fail"; //일반 문자열 (view)
		}
		else { // 0 / 값 x -> 사용가능한 이메일
			return "success";
		}
	}
	
	
	
	
}

