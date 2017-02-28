package towaynet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import towaynet.dao.UserDao;
import towaynet.entity.User;
import towaynet.util.NoteException;
import towaynet.util.NoteResult;
import towaynet.util.NoteUtil;

@Service("userService")//扫描
@Transactional
public class UserServiceImpl implements UserService{
	@Resource
	private UserDao userDao;//注入
	
	@Transactional(readOnly=true)
	public NoteResult checkLogin(
			String name, String password) {
		NoteResult result = new NoteResult();
		//判断用户名
		User user = userDao.findByName(name);
		if(user==null){
			result.setStatus(1);
			result.setMsg("用户名不存在");
			return result;
		}
		//判断密码
		//将用户输入的明文加密
		try{
			String md5_pwd = NoteUtil.md5(password);
			if(!user.getU_password()
					.equals(md5_pwd)){
				result.setStatus(2);
				result.setMsg("密码错误");
				return result;
			}
		}catch(Exception e){
			throw new NoteException(
				"密码加密异常", e);	
		}
		//登录成功
		result.setStatus(0);
		result.setMsg("登录成功");
		user.setU_password("");//把密码屏蔽不返回
		result.setData(user);//返回user信息
		return result;
	}

	@Transactional
	public NoteResult addUser(
		String name, String phone, String password) {
		NoteResult result = new NoteResult();
		try{
			//检测是否重名
			User has_user = userDao.findByName(name);
			User had_user=userDao.findByPhone(phone);
			if(has_user != null){
				result.setStatus(1);
				result.setMsg("用户名已被占用");
				return result;
			}else if(had_user!=null){
				result.setStatus(1);
				result.setMsg("手机号已被占用");
				return result;
			}
			//执行用户注册
			User user = new User();
			user.setU_name(name);//设置用户名
			user.setU_phone(phone);//设置手机号
			String md5_pwd = NoteUtil.md5(password);
			System.out.println("len:"+md5_pwd.length());
			user.setU_password(md5_pwd);//设置加密密码
			String userId = NoteUtil.createId();
			user.setU_uid(userId);//设置用户ID
			userDao.save(user);
			//创建返回结果
			result.setStatus(0);
			result.setMsg("注册成功");
			return result;
		}catch(Exception e){
			throw new NoteException("用户注册异常",e);
		}
		
	}

	public NoteResult searchUser(String name,String uid) {
		System.out.println(name);
		NoteResult nr=new NoteResult();
		name="%"+name+"%";
		User user=userDao.findByUid(uid);
		if(user==null){
			nr.setStatus(1);
			nr.setMsg("异常");
			return nr;
		}
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("name", name);
		map.put("d_lev",user.getD_lev());
		map.put("d_id",user.getD_id());
		List<User>list=userDao.searchByName(map);
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询失败");
		}else{
			if(list.size()==0){
				nr.setStatus(1);
				nr.setMsg("查无此人");
			}else{
				nr.setStatus(0);
				nr.setMsg("查询成功");
				nr.setData(list);
			}
		}
		return nr;
	}

	public NoteResult findByLuid(String l_uid) {
		NoteResult nr=new NoteResult();
		List<User>list=userDao.findByLuid(l_uid);
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询异常");
			return nr;
		}
		if(list.size()==0){
			nr.setMsg("改用户无下属");
			nr.setStatus(1);
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("查询成功");
		return nr;
	}

	public NoteResult findAllUser() {
		List<User>list=userDao.findAllUser();
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("搜索异常");
			return nr;
		}
		if(list.size()==0){
			nr.setMsg("搜索结果为空");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("搜索成功");
		nr.setStatus(0);
		nr.setData(list);
		return nr;
	}

	public NoteResult updateDept(String d_id, String u_uid, int d_lev) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("u_uid", u_uid);
		map.put("d_id", d_id);
		map.put("d_lev", d_lev);
		int i=userDao.updateDept(map);
		if(i<=0){
			nr.setStatus(1);
			nr.setMsg("设置职位失败");
		}
		nr.setMsg("设置成功");
		nr.setStatus(0);
		return nr;
	}
	//无工作安排的员工信息
	public NoteResult findFreeJob() {
		NoteResult nr=new NoteResult();
		List<User> list=userDao.findFreeJob();
		if(list==null){
			nr.setMsg("异常");
			nr.setStatus(1);
			return  nr;
		}
		nr.setMsg("查询为安排职位的员工信息成功");
		nr.setStatus(0);
		nr.setData(list);
		return nr;
	}

	public NoteResult findBranch(String uid) {
		NoteResult nr=new NoteResult();
		User user=userDao.findByUid(uid);
		System.err.println("userviceImp:198："+user);
		if(user==null){
			nr.setMsg("查询异常");
			nr.setStatus(1);
			return nr;
		}
		String d_id=user.getD_id();
		int d_lev=user.getD_lev();
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("d_id", d_id);
		map.put("d_lev", d_lev);
		List<User>list=userDao.findBranch(map);
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询失败");
			return  nr;
		}
		if(list.size()==0){
			nr.setMsg("查无结果");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("查询下属姓名成功");
		nr.setStatus(0);
		nr.setData(list);
		return nr;
	}
	public NoteResult updatePassword(String mpass,String newpass,String uid) {
		String password;
		try {
			NoteResult nr=new NoteResult();
			User user=userDao.findByUid(uid);
			if(user==null||!user.getU_password().equals(NoteUtil.md5(mpass))){
				nr.setStatus(1);
				nr.setMsg("输入原始密码错误");
				return nr;
			}
			password = NoteUtil.md5(newpass);
			int i=userDao.updatePassword(uid, password);
			if(i>0){
				nr.setStatus(0);
				nr.setMsg("重置密码成功");
				return nr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public NoteResult findCheckPerson(String uid) {
		System.out.println("Uservice:249:"+uid);
		NoteResult nr=new NoteResult();
		User u=userDao.findByUid(uid);
		if(u==null){
			nr.setStatus(1);
			nr.setMsg("查询失败");
			return nr;
		}
		List<User>list=userDao.findCheckPerson(u.getD_id());
		System.out.println("Uservice:258："+u.getD_id());
		System.out.println("uService:258:"+list);
		if(list==null || list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查询失败");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("查询成功");
		nr.setData(list);
		return nr;
	}

}
