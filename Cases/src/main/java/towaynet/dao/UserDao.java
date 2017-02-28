package towaynet.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import towaynet.entity.User;

public interface UserDao {
	public void save(User user);
	public User findByName(String name);
	//查找的指定员工的下属信息根据名字 模糊查询
	public List<User> searchByName(Map<String, Object>map);
	//查询手机号
	public User findByPhone(String phone);
	public User findByUid(String uid);
	public List<User>findByLuid(String l_uid);
	public List<User>findAllUser();
	public List<User>findFreeJob();
	public int updateDept(HashMap<String, Object>map);
	public List<User>findBranch(Map<String, Object>map);
	public int updatePassword(String uid,String password);
	public List<User> findCheckPerson(String uid);
}
