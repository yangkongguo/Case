package towaynet.dao;


import java.util.HashMap;
import java.util.List;

import towaynet.entity.SigninTable;;

public interface SigninTableDao {
	public int save(SigninTable st);
	
	public List<SigninTable> signinTableList(String engineerId);
	public List<SigninTable> signinTableListAll();
	public List<SigninTable>signinTabWhoFinsh(String id);
	public List<SigninTable>signinTableListAllLT2();
	public List<SigninTable>signinTableListAllLT3(String id);
	public String findWrokTaskId(String signinId);
	public List<SigninTable>findSigninByWID(String workTask_id);
	
	public int signinInUpdate(HashMap<String, Object>map);
	public int signinOutUpdate(HashMap<String, Object>map);
}
