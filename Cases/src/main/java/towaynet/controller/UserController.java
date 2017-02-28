package towaynet.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import towaynet.service.UserService;
import towaynet.util.NoteResult;
import towaynet.util.NoteUtil;

@Controller//扫描
public class UserController {
	@Resource//注入
	private UserService userService;
	//添加
	@RequestMapping("/user/add.do")
	@ResponseBody
	public NoteResult execute(
		String name,String password,String phone,HttpServletResponse res){
		NoteResult result = 
			userService.addUser(
			name, phone, password);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//登陆
	@RequestMapping("/user/login.do")
	@ResponseBody
	public NoteResult login(String name,String password,HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");	
		return userService.checkLogin(name, password);
	}
	//通过用户名的模糊查询
	@RequestMapping("/user/likeFindName.do")
	@ResponseBody
	public NoteResult likeFindName(String name,String uid,HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		return	userService.searchUser(name,uid);
	}
	//查询指定用户的下属信息
	@RequestMapping("/user/findBranch.do")
	@ResponseBody
	public NoteResult findBranchName(String uid,HttpServletResponse res){
		System.out.println("UserController:45");
		res.setHeader("Access-Control-Allow-Origin", "*");
		return	userService.findBranch(uid);
	}
	//查询所有的用户信息
	@RequestMapping("/user/findAllUser.do")
	@ResponseBody
	public NoteResult findAllUser(HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		return	userService.findAllUser();
	}
	//更新用户部门信息
	@RequestMapping("/user/addDept.do")
	@ResponseBody
	public NoteResult updateDept(String d_id,String u_uid,int d_lev,HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		return	userService.updateDept(d_id, u_uid, d_lev);
	}
	//查看还未安排职位的员工信息
	@RequestMapping("/user/findFreeUser.do")
	@ResponseBody
	public NoteResult findFreeJob(HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		return	userService.findFreeJob();
	}
	//
	@RequestMapping("/user/updatePassword.do")
	@ResponseBody
	public NoteResult updatePassword(String mpass,String newpass,String uid,HttpServletResponse res){
		System.out.println("uid:"+uid);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return userService.updatePassword(mpass,newpass,uid);
	}
	//查询检查人
	@RequestMapping("/user/findCheckPerson.do")
	@ResponseBody
	public NoteResult findCheckPerson(String uid,HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		return userService.findCheckPerson(uid);
	}
	
}
