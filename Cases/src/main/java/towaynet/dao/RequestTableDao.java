package towaynet.dao;

import java.util.HashMap;
import java.util.List;

import towaynet.entity.RequestTable;
import towaynet.entity.WorkTask;

public interface RequestTableDao {
	public int save(RequestTable req);
	//查询
	public List<RequestTable> requestList();
	public List<RequestTable>requestListByAlreadySend();
	public List<RequestTable> draftList(String userid);
	public RequestTable findRequestById(String id);
	public List<RequestTable> findAllReqBySponsorName(HashMap<String, Object>map);
	public int findSubNum(HashMap<String, Object>map);
	public int findExcuteNum(HashMap<String, Object>map);
	public int findCloseNum(HashMap<String, Object>map);
	public int findAllReqNumByYear(HashMap<String, Object>map);
	public List<RequestTable> findSubTask(HashMap<String, Object>map);
	public List<RequestTable> findExcuteTask(HashMap<String, Object>map);
	public List<RequestTable>findCloseTask(HashMap<String, Object>map);
	public List<RequestTable>findBySponsorName(String sponsorName);
	public List<RequestTable>findSendRequestBySponsorName(String sponsorName);
	public List<RequestTable>findAllReqBySponsorNameByPageNum(HashMap<String, Object>map);
	public String findEngineerIdByRequestId(String request_id);
	public List<RequestTable> findRequestByRequestName(String request_name);
	//更新
	public int requestUpdate(HashMap<String, Object>map);
	public int updateRequestEngineerName(HashMap<String, Object>map);
	public int updateRequestRealStartTime(HashMap<String, Object>map);
	public int updateRequestRealFinshTime(HashMap<String, Object>map);
	public int updateRequestDate(HashMap<String, Object>map);
	public int updateRequestByMannerConfrim(HashMap<String, Object>map);
	public int updateRequestEngineerId(HashMap<String, Object>map);
	//删除
	public int deleteDraftRequest(String request_id);
}
