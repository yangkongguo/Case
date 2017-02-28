package towaynet.dao;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mysql.fabric.HashShardMapping;

import towaynet.entity.WorkTask;

public interface WorkTaskDao {
	public int save(WorkTask wt);
	
	/*搜索*/
	//查询所有的工单根据月份
	public List<WorkTask> workTaskList(String month);
	
	public List<WorkTask> engineerWorkTask(HashMap<String, Object>map);
	public List<WorkTask> engineerWorkTaskUnFinsh(String engineer_id);
	public List<WorkTask> engineerWorkTaskFinsh(HashMap<String, Object>map);
	public String findRemark(String workTask_id);
	public List<WorkTask>findWorkTaskBySponsorName(HashMap<String, Object>map);
	public List<WorkTask>findApplyWT(@Param("status") int i);
	public int findNumberByTheMon(HashMap<String, Object>map);
	public int findNumberByTheYear(HashMap<String, Object>map);
	public int findNumberByTheUndo(String str);
	public int findSubNum(String name);
	public int findExcuteNum(String name);
	public int findCloseNum(String name);
	public List<WorkTask> findSubTask(String name);
	public List<WorkTask> findExcuteTask(String name);
	public List<WorkTask> findCloseTask(String name);
	public String findPaiMingByEId(HashMap<String, Object>map);
	public List<WorkTask>searchWorkTaskFinshByWorkName(HashMap<String, Object>map);
	public List<String>findEngineerIdWhoWorkNow(String date);
	public List<WorkTask>findWorkTaskBySponsorNameToEngineer(HashMap<String, Object>map);
	public String findRequestIdByWorkID(String wid);
	//分页查询
	public List<WorkTask>pageSelect(HashMap<String, Object>map);
	/*更新*/
	public int workTaskUpdate(HashMap<String, Object>map);
	public int workTaskUpdateRemark(HashMap<String, Object>map);
	
	//删除
	public int workTaskdeleteByWID(String workTask_id);
}
