package towaynet.service;


import towaynet.util.NoteResult;

public interface SigninTableService {
	
	public NoteResult signinTableList(String engineerId);
	public NoteResult signinTableListAll();
	public NoteResult signinTableWhoFinsh(String engineerId);
	public NoteResult signinTableListAllLT2();
	public NoteResult findSigninByWID(String workTask_id);
	
	//更新
	public NoteResult signinIn(String userid,String cid,String signin_id,String map,String image,String name,String workTaskType,String workTaskName)throws Exception;
	public NoteResult signinOut(String userid,String cid,String workTask_id,String signin_id,String map,String image,String name,String workTaskType,String workTaskName)throws Exception;
}
