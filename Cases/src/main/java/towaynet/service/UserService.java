package towaynet.service;

import towaynet.util.NoteResult;

public interface UserService {
	public NoteResult addUser(String name,String nick,String password);
	public NoteResult checkLogin(String name,String password);
	public NoteResult searchUser(String name,String uid);
	public NoteResult findByLuid(String l_uid);
	public NoteResult findAllUser();
	public NoteResult findFreeJob();
	public NoteResult findBranch(String uid);
	public NoteResult updateDept(String d_di,String u_uid,int d_lev);
	public NoteResult updatePassword(String mpass,String newpass,String uid);
	public NoteResult findCheckPerson(String uid);
}
