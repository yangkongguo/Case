package towaynet.service;


import towaynet.util.NoteResult;

public interface RequestTableService {
	public NoteResult saveRequestTable(String caseName,String caseMap,String caseType,String caseGuest,String casePhone,
			String caseDescr,String start_time,String finsh_time,String request_id,String userid,String username,String caseGuestCompany);
	public NoteResult draftRequestTable(String caseName,String caseMap,String caseType,String caseGuest,String casePhone,
			String caseDescr,String start_time,String finsh_time,String userid);
	public NoteResult requestList();
	public NoteResult requestListByAlreadySend();
	public NoteResult findAllReqBySponsorName(String userName);
	public NoteResult findSaleReqNum(String userName);
	public NoteResult findSubTask(String userName);
	public NoteResult findExcuteTask(String userName);
	public NoteResult findCloseTask(String userName);
	public NoteResult findBySponsorName(String sponsorName);
	public NoteResult findSendRequestBySponsorName(String sponsorName);
	public NoteResult findAllReqBySponsorNameByPageNum(String num,String userName);
	public NoteResult findRequestEngineerIdByRequetId(String request_id);
	public NoteResult findRequestByRequestName(String request_name);
	//草稿箱
	public NoteResult draftList(String userid);
	//更新程成忽略的状态
	public NoteResult requestIgnore(String request_id);
	//更新成确认状态
	public NoteResult requestConfirm(String request_id,String engineers,String type,String startTimes,String finshTimes);
	//
	public NoteResult deleteDraftRequest(String request_id);
	
}
