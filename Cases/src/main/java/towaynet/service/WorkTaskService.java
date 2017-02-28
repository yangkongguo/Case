package towaynet.service;


import towaynet.util.NoteResult;

public interface WorkTaskService {
	
	public NoteResult workTaskList();
	public NoteResult engineerWorkTask(String engineer_name);
	public NoteResult findRemarkByWorkTaskId(String workTask_id);
	public NoteResult findWorkTaskBySponsorName(String userName);
	public NoteResult findApplyDeleteWT();
	public NoteResult findApplyCompleteWT();
	public NoteResult findNumberByTheMon(String engineerId);
	public NoteResult findPaiMingByEId(String engineerId);
	public NoteResult searchWorkTaskFinshByWorkName(String engineerId,String workTaskName);
	public NoteResult findEngineerIdWhoWorkNow();
	public NoteResult findSaleReqNum(String userName);
	public NoteResult findSubTask(String userName);
	public NoteResult findExcuteTask(String userName);
	public NoteResult findCloseTask(String userName);
	public NoteResult findWorkTaskBySponsorNameToEngineer(String sponsorName,String engineerName);
	public NoteResult pageSelect(String num);
	
	public NoteResult engineerWorkTaskUnFinsh(String engineer_id);
	public NoteResult engineerWorkTaskFinsh(String engineer_id);
	public NoteResult engineerWorkTaskFinshByMon(String engineer_id);
	
	public NoteResult updateRemark(String remark,String workTask_id);
	public NoteResult applyDeleteWorkTaskByWID(String workTask_id);
	public NoteResult applyCompleteWorkTaskByWID(String workTask_id);
	public NoteResult rejectDeleteWorkTask(String workTask_id);
	public NoteResult rejectCompleteWorkTask(String workTask_id);
	
	public NoteResult workTaskdeleteByWID(String workTask_id);
	public NoteResult workTaskcompleteByWID(String workTask_id);
}
