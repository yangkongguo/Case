package towaynet.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import towaynet.dao.RequestTableDao;
import towaynet.dao.WorkTaskDao;
import towaynet.entity.WorkTask;
import towaynet.util.NoteResult;

@Service("workTaskService")//扫描
@Transactional
public class WorkTaskServiceImp implements WorkTaskService{
	
	@Resource
	private WorkTaskDao workTaskDao;
	@Resource
	private RequestTableDao requestDao;
	//查处状态吗为 0 的请求
	public NoteResult workTaskList() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		String mon="";
		if(month<10){
			mon="0"+month;
		}
		String findDate="%"+year+"_"+mon+"_%";
		List<WorkTask> list=workTaskDao.workTaskList(findDate);
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询异常");
			return nr;
		}
		if(list.size()==0){
			nr.setStatus(0);
			nr.setMsg("查无结果");
			return nr;
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("查询成功");
		return nr;
	}
	public NoteResult engineerWorkTask(String engineer_name) {
		engineer_name="%"+engineer_name+"%";
		HashMap<String,Object>map=new HashMap<String, Object>();
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		String mon="";
		if(month<10){
			mon="0"+month;
		}
		String findDate="%"+year+"_"+mon+"_%";
		map.put("engineer_name", engineer_name);
		map.put("month",findDate);
		List<WorkTask>list=workTaskDao.engineerWorkTask(map);
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询异常");
			return nr;
		}
		if(list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查无结果");
			return nr;
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("获取指定工程师的工单成功");
		return nr;
	}
	public NoteResult engineerWorkTaskUnFinsh(String engineer_id) {
		List<WorkTask>list=workTaskDao.engineerWorkTaskUnFinsh(engineer_id);
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询异常");
			return nr;
		}
		if(list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查无结果");
			return nr;
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("获取指定工程师的未完成工单成功");
		return nr;
	}
	public NoteResult engineerWorkTaskFinsh(String engineer_id) {
		HashMap<String, Object>map=new HashMap<String, Object>();
		//日历属性
		Calendar ca = Calendar.getInstance();
		//获取到当前年份值，以在查询完成的任务中，只查询今年所有的完成任务。
		int year = ca.get(Calendar.YEAR);
		String findYear="%"+year+"_%";
		map.put("year", findYear);
		map.put("id", engineer_id);
		List<WorkTask>list=workTaskDao.engineerWorkTaskFinsh(map);
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询异常");
			return nr;
		}
		if(list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查无结果");
			return nr;
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("获取指定工程师的未完成工单成功");
		return nr;
	}
	public NoteResult engineerWorkTaskFinshByMon(String engineer_id) {
		HashMap<String, Object>map=new HashMap<String, Object>();
		//日历属性
		Calendar ca = Calendar.getInstance();
		//获取到当前年份值，以在查询完成的任务中，只查询今年所有的完成任务。
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		String mon="";
		if(month<10){
			mon="0"+month;
		}
		String date="%"+year+"_"+mon+"_"+"%";
		System.out.println("mon:135:"+date+engineer_id);
		map.put("year", date);
		map.put("id", engineer_id);
		List<WorkTask>list=workTaskDao.engineerWorkTaskFinsh(map);
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询异常");
			return nr;
		}
		if(list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查无结果");
			return nr;
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("获取指定工程师的未完成工单成功");
		return nr;
	}
	
	//更新备注内容
	public NoteResult updateRemark(String remark,String workTask_id) {
		System.out.println("wsimp:84:"+remark+":"+workTask_id);
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("remark", remark);
		map.put("workTask_id",workTask_id);
		int i=workTaskDao.workTaskUpdateRemark(map);
		NoteResult nr=new NoteResult();
		if(i<0){
			nr.setStatus(1);
			nr.setMsg("更新备注内容异常");
		}
		if(i==0){
			nr.setStatus(1);
			nr.setMsg("更新备注内容失败");
			return nr;
		}
		nr.setStatus(0);
		nr.setMsg("更新备注内容成功");
		return nr;
	}
	public NoteResult findRemarkByWorkTaskId(String workTask_id) {
		NoteResult nr=new NoteResult();
		String remark=workTaskDao.findRemark(workTask_id);
		if(remark==null){
			nr.setData(" ");
			nr.setStatus(0);
			return  nr;
		}
		nr.setData(remark);
		nr.setStatus(0);
		return nr;
	}
	//删除该工单
	public NoteResult workTaskdeleteByWID(String workTask_id) {
		//不能删除真实数据将其修改状态码
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("status", 4);
		map.put("workTask_id", workTask_id);
		int i=workTaskDao.workTaskUpdate(map);
		NoteResult nr=new NoteResult();
		if(i<0){
			nr.setStatus(1);
			nr.setMsg("删除异常");
			return  nr;
		}
		if(i==0){
			nr.setStatus(1);
			nr.setMsg("删除失败");
			return nr;
		}
		nr.setMsg("删除成功");
		nr.setStatus(0);
		return nr;
	}
	//关闭Case
	public NoteResult workTaskcompleteByWID(String workTask_id) {
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("status", 5);
		map.put("workTask_id", workTask_id);
		int i=workTaskDao.workTaskUpdate(map);
		NoteResult nr=new NoteResult();
		//跟新请求表
		String requestId=workTaskDao.findRequestIdByWorkID(workTask_id);
		HashMap<String, Object>requestMap=new HashMap<String, Object>();
		requestMap.put("status", 5);
		requestMap.put("request_id", requestId);
		int requestUpdateNum=requestDao.requestUpdate(requestMap);
		//是否成功跟新请求表
		if(requestUpdateNum<=0){
			nr.setStatus(4);
			nr.setMsg("更新Case请求表状态失败");
			return nr;
		}
		if(i<0){
			nr.setStatus(1);
			nr.setMsg("关闭异常");
			return  nr;
		}
		if(i==0){
			nr.setStatus(1);
			nr.setMsg("关闭失败");
			return nr;
		}
		nr.setMsg("关闭成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult applyDeleteWorkTaskByWID(String workTask_id) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("status", 2);
		map.put("workTask_id", workTask_id);
		int i=workTaskDao.workTaskUpdate(map);
		if(i<=0){
			nr.setMsg("申请删除提交失败");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("申请删除提交成功");
		nr.setStatus(0);
		return nr;
	}
	//申请完成关闭Case
	public NoteResult applyCompleteWorkTaskByWID(String workTask_id) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("status", 3);
		map.put("workTask_id", workTask_id);
		int i=workTaskDao.workTaskUpdate(map);
		if(i<=0){
			nr.setMsg("申请提交失败");
			nr.setStatus(1);
			return nr;
		}
		
		nr.setMsg("申请提交成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult findWorkTaskBySponsorName(String userName) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("workTask_sponsor_name",userName);
		Calendar ca = Calendar.getInstance(); 
		int year = ca.get(Calendar.YEAR);
		String dateLikeYear="%"+year+"_%";
		map.put("year", dateLikeYear);
		List<WorkTask>list=workTaskDao.findWorkTaskBySponsorName(map);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("根据指定的派发人查询结果为0");
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("跟你指定的排放人查询成功");
		return nr;
	}
	//查询申请删除的
	public NoteResult findApplyDeleteWT() {
		NoteResult nr=new NoteResult();
		List<WorkTask>list=workTaskDao.findApplyWT(2);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查询申请删除的工单无结果");
			return nr;
		}
		nr.setMsg("查询申请删除的工单成功");
		nr.setData(list);
		nr.setStatus(0);		
		return nr;
	}
	//查询申请完成关闭的
	public NoteResult findApplyCompleteWT() {
		NoteResult nr=new NoteResult();
		List<WorkTask>list=workTaskDao.findApplyWT(3);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查询申请关闭的工单无结果");
			return nr;
		}
		nr.setMsg("查询申请关闭的工单成功");
		nr.setData(list);
		nr.setStatus(0);		
		return nr;
	}
	public NoteResult rejectDeleteWorkTask(String workTask_id) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("status", 0);
		map.put("workTask_id", workTask_id);
		int i=workTaskDao.workTaskUpdate(map);
		if(i<0){
			nr.setMsg("拒绝删除该工单异常");
			nr.setStatus(1);
			return nr;
		}
		if(i==0){
			nr.setMsg("拒绝删除该工单失败");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("拒绝成功——该工单将会被继续执行");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult rejectCompleteWorkTask(String workTask_id) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("status", 0);
		map.put("workTask_id", workTask_id);
		int i=workTaskDao.workTaskUpdate(map);
		if(i<0){
			nr.setMsg("拒绝关闭该Case异常");
			nr.setStatus(1);
			return nr;
		}
		if(i==0){
			nr.setMsg("拒绝关闭该Case失败");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("拒绝成功——该工单将会被继续执行");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult findNumberByTheMon(String engineerId) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		HashMap<String, Object>data=new HashMap<String, Object>();
		Calendar ca = Calendar.getInstance(); 
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		String mon="";
		if(month<10){
			mon="0"+month;
		}
		String dateLikeYear="%"+year+"_%";
		map.put("id", engineerId);
		map.put("year", dateLikeYear);
		int j=workTaskDao.findNumberByTheYear(map);
		if(j<0){
			nr.setMsg("查询年度工单异常");
			nr.setStatus(1);
			return nr;
		}
		String msg="";
		if(j==0){
			msg="查询年度工单数为0;"+msg;
			
		}
		//设置年度数
		data.put("year", j);
		String dateLike="%"+year+"_"+mon+"_"+"%";
		map.put("month", dateLike);
		int i=workTaskDao.findNumberByTheMon(map);
		if(i<0){
			nr.setMsg("查询月度工单数异常");
			nr.setStatus(1);
			return nr;
		}
		if(i==0){
			msg="查询月度工单数为0;"+msg;
			nr.setStatus(1);
		}
		data.put("mon", i);
		
		Integer UndoNum=workTaskDao.findNumberByTheUndo(engineerId);
		if(UndoNum<0){
			nr.setMsg("查询未完成工单数异常");
			nr.setStatus(1);
			return  nr;
		}
		if(UndoNum==0){
			msg="查询未完成工单数为0;"+msg;
		}
		data.put("num",UndoNum );
		msg="查询:"+msg;
		nr.setStatus(0);
		nr.setData(data);
		return nr;
	}
	public NoteResult findPaiMingByEId(String engineerId) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		Calendar ca = Calendar.getInstance(); 
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		String mon="";
		if(month<10){
			mon="0"+month;
		}
		String date="%"+year+"_"+mon+"_%";
		map.put("engineerId",engineerId);
		map.put("start_time", date);
		String s=workTaskDao.findPaiMingByEId(map);
		if(s==null){
			nr.setMsg("查询排名失败");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("查询排名成功");
		nr.setStatus(0);
		nr.setData(s);
		return nr;
	}
	public NoteResult searchWorkTaskFinshByWorkName(String engineerId, String workTaskName) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("id", engineerId);
		String likeWorkName="%"+workTaskName+"%";
		map.put("workName", likeWorkName);
		List<WorkTask>list=workTaskDao.searchWorkTaskFinshByWorkName(map);
		if(list==null||list.size()==0){
			nr.setMsg("根据工单名查询结果失败");
			nr.setStatus(1);
			return  nr;
		}
		nr.setData(list);
		nr.setMsg("根据工单名查询结果成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult findEngineerIdWhoWorkNow() {
		NoteResult nr=new NoteResult();
		Calendar ca = Calendar.getInstance(); 
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		String mon="";
		if(month<10){
			mon="0"+month;
		}
		int day=ca.get(Calendar.DAY_OF_MONTH);
		String date="%"+year+"_"+mon+"_"+day+"_%";
		List<String>list=workTaskDao.findEngineerIdWhoWorkNow(date);
		if(list==null||list.size()==0){
			nr.setMsg("查询当天派工的工程师ID结果为0");
			nr.setStatus(1);
			return nr;
		}
		nr.setData(list);
		nr.setMsg("查询当天派工的工程师ID成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult findSaleReqNum(String userName) {
		NoteResult nr=new NoteResult();
		HashMap<String, Integer>map=new HashMap<String, Integer>();
		int close=workTaskDao.findCloseNum(userName);
		map.put("close", close);
		int excute=workTaskDao.findExcuteNum(userName);
		map.put("excute", excute);
		int sub=workTaskDao.findSubNum(userName);
		map.put("sub", sub);
		if(close<0||excute<0||sub<0){
			nr.setMsg("查询销售显示数据异常");
			nr.setStatus(2);
			return  nr;
		}
		/*
		if(close==0||excute==0||sub==0){
			nr.setMsg("查询数据为0");
			nr.setStatus(1);
			return  nr;
		}
		*/
		nr.setStatus(0);
		nr.setData(map);
		nr.setMsg("查询成功");
		return nr;
	}
	//查看派工
	public NoteResult findSubTask(String userName) {
		NoteResult nr=new NoteResult();
		List<WorkTask>list=workTaskDao.findSubTask(userName);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查看派发的请求结果为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("查看派发成功");
		nr.setData(list);
		return nr;
	}
	//查看在执行的任务
	public NoteResult findExcuteTask(String userName) {
		NoteResult nr=new NoteResult();
		List<WorkTask>list=workTaskDao.findExcuteTask(userName);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查看在执行中的请求为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("查看执行成功");
		nr.setData(list);
		return nr;
	}
	//查看完成-关闭的任务
	public NoteResult findCloseTask(String userName) {
		NoteResult nr=new NoteResult();
		List<WorkTask>list=workTaskDao.findCloseTask(userName);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查看已关闭的请求为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("查看已关闭的成功");
		nr.setData(list);
		return nr;	
	}
	//在工程师页面的搜索查看根据请求人的名字
	public NoteResult findWorkTaskBySponsorNameToEngineer(String sponsorName, String engineerName) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("workTask_sponsor_name", sponsorName);
		map.put("workTask_engineer_name", engineerName);
		List<WorkTask>list=workTaskDao.findWorkTaskBySponsorNameToEngineer(map);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("根据搜索条件查看结果为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("根据搜索条件查看成功");
		nr.setData(list);
		return nr;	
	}
	//
	public NoteResult pageSelect(String num) {
		NoteResult nr=new NoteResult();
		HashMap<String, Object>map=new HashMap<String, Object>();
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		String mon="";
		if(month<10){
			mon="0"+month;
		}
		String findDate="%"+year+"_"+mon+"_%";
		map.put("month", findDate);
		map.put("num", Integer.parseInt(num));
		List<WorkTask>list=workTaskDao.pageSelect(map);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("分页查询结果为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("分页查询成功");
		nr.setData(list);
		return nr;	
	}
}
