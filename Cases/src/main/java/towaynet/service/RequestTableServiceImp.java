package towaynet.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import towaynet.dao.FlushNumDao;
import towaynet.dao.RequestTableDao;
import towaynet.dao.SigninTableDao;
import towaynet.dao.WorkTaskDao;
import towaynet.entity.FlushNum;
import towaynet.entity.RequestTable;
import towaynet.entity.SigninTable;
import towaynet.entity.WorkTask;
import towaynet.util.NoteResult;
import towaynet.util.NoteUtil;

@Service("requestService")//扫描
@Transactional
public class RequestTableServiceImp implements RequestTableService{

	@Resource
	private RequestTableDao requestDao;
	@Resource
	private WorkTaskDao workTaskDao;
	@Resource
	private SigninTableDao signinDao;
	@Resource
	private FlushNumDao flushDao;

	public synchronized NoteResult saveRequestTable(String caseName,String caseMap,String caseType,String caseGuest,String casePhone,
			String caseDescr,String start_time,String finsh_time,String request_id,String userid,String username,String caseGuestCompany) {
		NoteResult nr=new NoteResult();
		/* 不许删除信息
		if(!" ".equals(request_id)){
			int i=requestDao.deleteDraftRequest(request_id);
			if(i<=0){
				nr.setMsg("根据草稿箱的添加失败");
				nr.setStatus(2);
				return nr;
			}
		}
		*/
		RequestTable req=new RequestTable();
		//设置地点
		req.setRequest_map(caseMap);
		//设置名称
		req.setRequest_name(caseName);
		//设置发送人的id
		req.setRequest_sponsor_id(userid);
		//设置发送人名字
		req.setRequest_sponsor_name(username);
		//设置请求表状态码
		req.setRequest_status(0);
		//设置请求表类型
		req.setRequest_type(Integer.parseInt(caseType));
		//设置请求表ID
		req.setRequest_id(NoteUtil.createId());
		//设置客户名称
		req.setRequest_guest_name(caseGuest);
		//设置客户电话
		req.setRequest_guest_tel(casePhone.trim());
		//设置客户单位名称
		req.setRequest_guest_company(caseGuestCompany);;
		//设置请求表开始时间
		Long lStart=Long.parseLong(start_time);
		Timestamp request_start_time=new Timestamp(lStart);
		req.setRequest_start_time(request_start_time);
		//设置结束时间
		Long lFinsh=Long.parseLong(finsh_time);
		Timestamp request_finsh_time=new Timestamp(lFinsh);
		req.setRequest_finsh_time(request_finsh_time);
		//设置描述
		req.setRequest_desc(caseDescr);
		//设置Case号
		//当前日期
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String dte=sdf.format(d);
		//获取文件中的Case数量
		//设置Case号
		int caseNum=0;
		FlushNum flush=flushDao.findFlushNum();
		String flushDate=flush.getNowDate();
		System.out.println(flushDate);
		//日期相同
		if(dte.equals(flushDate)){
			caseNum=flush.getCase_num();
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			String strCase="TW-QQ-"+format.format(d)+"-"+caseNum;
			req.setRequest_caseNum(strCase);
			caseNum++;
			HashMap<String, Object>map=new HashMap<String, Object>();
			map.put("nowDate", dte);
			map.put("case_num", caseNum);
			map.put("flush_num", flush.getFlush_num());
			int j=flushDao.flushUpdate(map);
			if(j<=0){
				nr.setStatus(3);
				nr.setMsg("更新数据库异常，请重试");
				return nr;
			}
		}else{
			HashMap<String, Object>map=new HashMap<String, Object>();
			map.put("nowDate", dte);
			map.put("case_num", caseNum);
			int j=flushDao.flushUpdate(map);
			if(j<=0){
				nr.setStatus(3);
				nr.setMsg("更新数据库异常，请重试");
				return nr;
			}
		}
		
		int i=requestDao.save(req);
		
		if(i<=0){
			nr.setStatus(1);
			nr.setMsg("存入失败");
			return nr;
		}
		nr.setStatus(0);
		nr.setMsg("存入成功");
		return nr;
	}

	public NoteResult draftRequestTable(String caseName, String caseMap, String caseType, String caseGuest,
			String casePhone, String caseDescr, String start_time, String finsh_time,String userid) {
		RequestTable req=new RequestTable();
		//设置地点
		req.setRequest_map(caseMap);
		//设置名称
		req.setRequest_name(caseName);
		//设置发送人的id
		req.setRequest_sponsor_id(userid);
		//设置请求表状态  为草稿箱状态
		req.setRequest_status(1);
		//设置类型
		req.setRequest_type(Integer.parseInt(caseType));
		//设置请求表ID
		req.setRequest_id(NoteUtil.createId());
		//设置客户名称
		req.setRequest_guest_name(caseGuest);
		//设置客户电话
		req.setRequest_guest_tel(casePhone.trim());
		//设置请求表开始时间
		Long lStart=Long.parseLong(start_time);
		Timestamp request_start_time=new Timestamp(lStart);
		req.setRequest_start_time(request_start_time);
		//设置结束时间
		Long lFinsh=Long.parseLong(finsh_time);
		Timestamp request_finsh_time=new Timestamp(lFinsh);
		req.setRequest_finsh_time(request_finsh_time);
		//设置描述
		req.setRequest_desc(caseDescr);
		int i=requestDao.save(req);
		System.out.println("ReService:49:"+i);
		NoteResult nr=new NoteResult();
		if(i<=0){
			nr.setStatus(1);
			nr.setMsg("存入失败");
			return nr;
		}
		nr.setStatus(0);
		nr.setMsg("存入成功");
		return nr;
	}
	//查处状态吗为 0 的请求
	public NoteResult requestList() {
		List<RequestTable> list=requestDao.requestList();
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

	public NoteResult requestIgnore(String request_id) {
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("request_id",request_id );
		map.put("status", 3);
		int i=requestDao.requestUpdate(map);
		NoteResult nr=new NoteResult();
		if(i<0){
			nr.setMsg("更新异常");
			nr.setStatus(1);
			return  nr;
		}
		if(i==0){
			nr.setMsg("更新数量为0");
			nr.setStatus(1);
			return nr;
		}
		nr.setStatus(0);
		nr.setMsg("更新成功");
		return nr;
	}
	//管理员确认后的执行
	public synchronized NoteResult requestConfirm(String request_id,String engineers,String type,String startTimes,String finshTimes) {
		Integer workTask_type=Integer.parseInt(type);
		//HashMap<String, Object>map=new HashMap<String, Object>();
		NoteResult nr=new NoteResult();
		RequestTable rt=requestDao.findRequestById(request_id);
		if(rt==null){
			nr.setMsg("查询请求对象失败");
			nr.setStatus(2);
			return nr;
		}
		
		//根据姓名数量循环
		String[] engieersList=engineers.split(";");
		List<String>dingId=new ArrayList<String>();
		List<String>excuteEngineerName=new ArrayList<String>();
		for(int l=0;l<engieersList.length;l++){
			String []engsId$Name=engieersList[l].split(",");
			String engineerId=engsId$Name[1];
			String engineerName=engsId$Name[0];
			//设置流水号
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			String dte=sdf.format(date);
			//相同说明之前调用过，获取之前的值 并且更新
			int flushNum=0;
			FlushNum flush=flushDao.findFlushNum();
			String flushDate=flush.getNowDate();
			//日期相同
			if(dte.equals(flushDate)){
				flushNum=flush.getFlush_num();
				flushNum++;
				HashMap<String, Object>flushMap=new HashMap<String, Object>();
				flushMap.put("nowDate", dte);
				flushMap.put("flush_num", flushNum);
				flushMap.put("case_num", flush.getCase_num());
				int j=flushDao.flushUpdate(flushMap);
				if(j<=0){
					nr.setStatus(3);
					nr.setMsg("更新记录表失败");
					return nr;
				}
			}else{
				HashMap<String, Object>flushMap=new HashMap<String, Object>();
				flushMap.put("nowDate", dte);
				flushMap.put("flush_num", flushNum);
				flushMap.put("case_num", flush.getCase_num());
				int j=flushDao.flushUpdate(flushMap);
				if(j<=0){
					nr.setStatus(3);
					nr.setMsg("更新记录表失败");
					return nr;
				}
			}
			SimpleDateFormat simple=new SimpleDateFormat("yyyyMMdd");
			dte="TW-GD-"+simple.format(date)+"-"+flushNum;
			WorkTask wt=new WorkTask();
			//设置工单的描述
			wt.setWorkTask_desc(rt.getRequest_desc());
			//传入工单流水号
			wt.setWorkTask_num(dte);
			//工程师名称
			wt.setWorkTask_engineer_name(engsId$Name[0]);
			//设置工程师的ID
			wt.setWorkTask_engineer_id(engsId$Name[1]);
			//将id值存入到list中，以便于发送Ding
			dingId.add(engsId$Name[1]);
			//存储执行者的名字
			excuteEngineerName.add(engineerName);
			//设置客户名称
			wt.setWorkTask_guest_name(rt.getRequest_guest_name());
			wt.setWorkTask_guest_tel(rt.getRequest_guest_tel());
			String wid=NoteUtil.createId();
			wt.setWorkTask_id(wid);
			//设置到地点
			wt.setWorkTask_map(rt.getRequest_map());
			//设置工单名称
			wt.setWorkTask_name(rt.getRequest_name());
			//设置客户名字
			wt.setWorkTask_guest_name(rt.getRequest_guest_name());
			//设置派工请求人
			wt.setWorkTask_sponsor_name(rt.getRequest_sponsor_name());
			wt.setRequest_id(request_id);
			//设置时间
			wt.setWorkTask_start_time(new Timestamp(Long.parseLong(startTimes)));
			wt.setWorkTask_finsh_time(new Timestamp(Long.parseLong(finshTimes)));
			//设置工单状态
			wt.setWorkTask_status(0);
			//设置工单类型
			wt.setWorkTask_type(workTask_type);
			int j=workTaskDao.save(wt);
			if(j<=0){
				nr.setMsg("创建工单失败");
				nr.setStatus(3);
				return nr;
			}
			//创建签到签退表
			//时间获取/根据时间来判断生成的签到签退表
			//long tt=(rt.getRequest_finsh_time().getTime()-rt.getRequest_start_time().getTime());
	        long between_days=(Long.parseLong(finshTimes)-Long.parseLong(startTimes))/(1000*3600*24);  
	        int time=Integer.parseInt(String.valueOf(between_days));
	        time++;
	        for(int k=0;k<time;k++){
				SigninTable signin=new SigninTable();
				signin.setSignin_id(NoteUtil.createId());
				signin.setSignin_status(0);
				signin.setWorkTask_id(wid);
				signin.setSignin_engineer_id(engineerId);
				int o=signinDao.save(signin);
				if(o<0){
					nr.setMsg("创建签到签退表失败");
					nr.setStatus(4);
					return nr;
				}
			}
		}
		HashMap<String, Object>updateReqMap=new HashMap<String, Object>();
		updateReqMap.put("request_excute_engineerName",excuteEngineerName.toString() );
		updateReqMap.put("request_id",request_id );
		updateReqMap.put("request_status", 2);
		String engineerId=dingId.toString().replace("[","");
		engineerId=engineerId.replace("]","");
		updateReqMap.put("request_engineerId", engineerId);
		updateReqMap.put("request_start_time", new Timestamp(Long.parseLong(startTimes)));
		updateReqMap.put("request_finsh_time", new Timestamp(Long.parseLong(startTimes)));
		int  updateNum=requestDao.updateRequestByMannerConfrim(updateReqMap);
		if(updateNum<0){
			nr.setMsg("更新请求表异常");
			nr.setStatus(1);
			return  nr;
		}
		if(updateNum==0){
			nr.setMsg("更新请求表数量为0");
			nr.setStatus(1);
			return nr;
		}
		nr.setStatus(0);
		nr.setData(dingId);
		nr.setMsg("更新成功并且创建工单成功");
		return nr;
	}

	public NoteResult draftList(String userid) {
		List<RequestTable> list=requestDao.draftList(userid);
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
		nr.setMsg("查询草稿箱成功");
		return nr;
	}

	public NoteResult deleteDraftRequest(String request_id) {
		int i=requestDao.deleteDraftRequest(request_id);
		NoteResult nr=new NoteResult();
		if(i<0){
			nr.setStatus(1);
			nr.setMsg("删除异常");
			return nr;
		}
		if(i==0){
			nr.setStatus(1);
			nr.setMsg("删除失败");
			return nr;
		}
		nr.setStatus(0);
		nr.setMsg("删除成功");
		return nr;
	}
	//查看所有的请求根据发送人名
	public NoteResult findAllReqBySponsorName(String userName) {
		NoteResult nr=new NoteResult();
		Calendar ca = Calendar.getInstance(); 
		int year = ca.get(Calendar.YEAR);
		String date="%"+year+"%";
		System.out.println("382:"+date);
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("year", date);
		map.put("userName", userName);
		List<RequestTable>list=requestDao.findAllReqBySponsorName(map);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查询个人的所有请求结果为0");
			return nr;
		}
		System.out.println(list.size());
		nr.setData(list);
		nr.setMsg("查询个人的所有请求成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult findSaleReqNum(String userName) {
		NoteResult nr=new NoteResult();
		HashMap<String, Integer>map=new HashMap<String, Integer>();
		Calendar ca = Calendar.getInstance(); 
		int year = ca.get(Calendar.YEAR);
		String date="%"+year+"_%";
		HashMap<String, Object>datemap=new HashMap<String, Object>();
		datemap.put("year", date);
		datemap.put("userName", userName);
		int  all=requestDao.findAllReqNumByYear(datemap);
		map.put("all", all);
		System.out.println("441:"+all);
		int close=requestDao.findCloseNum(datemap);
		map.put("close", close);
		int excute=requestDao.findExcuteNum(datemap);
		map.put("excute", excute);
		int sub=requestDao.findSubNum(datemap);
		map.put("sub", sub);
		if(close<0||excute<0||sub<0){
			nr.setMsg("查询销售显示数据异常");
			nr.setStatus(2);
			return  nr;
		}
		nr.setStatus(0);
		nr.setData(map);
		nr.setMsg("查询成功");
		return nr;
	}
	//查看派工
		public NoteResult findSubTask(String userName) {
			NoteResult nr=new NoteResult();
			Calendar ca = Calendar.getInstance(); 
			int year = ca.get(Calendar.YEAR);
			String date="%"+year+"_%";
			HashMap<String, Object>datemap=new HashMap<String, Object>();
			datemap.put("year", date);
			datemap.put("userName", userName);
			List<RequestTable>list=requestDao.findSubTask(datemap);
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
			Calendar ca = Calendar.getInstance(); 
			int year = ca.get(Calendar.YEAR);
			String date="%"+year+"_%";
			HashMap<String, Object>datemap=new HashMap<String, Object>();
			datemap.put("year", date);
			datemap.put("userName", userName);
			List<RequestTable>list=requestDao.findExcuteTask(datemap);
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
			Calendar ca = Calendar.getInstance(); 
			int year = ca.get(Calendar.YEAR);
			String date="%"+year+"_%";
			HashMap<String, Object>datemap=new HashMap<String, Object>();
			datemap.put("year", date);
			datemap.put("userName", userName);
			List<RequestTable>list=requestDao.findCloseTask(datemap);
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
	public NoteResult findBySponsorName(String sponsorName) {
		NoteResult nr=new NoteResult() ;
		List<RequestTable>list=requestDao.findBySponsorName(sponsorName);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("根据搜索名查看的结果为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("根据搜索名查看的结果成功");
		nr.setData(list);
		return nr;
	}
	public NoteResult requestListByAlreadySend() {
		NoteResult nr=new NoteResult() ;
		List<RequestTable>list=requestDao.requestListByAlreadySend();
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查询已派发的请求的结果为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("查询已派发的请求结果成功");
		nr.setData(list);
		return nr;
	}
	public NoteResult findSendRequestBySponsorName(String sponsorName) {
		NoteResult nr=new NoteResult() ;
		List<RequestTable>list=requestDao.findSendRequestBySponsorName(sponsorName);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查询指定发送人的已派发的请求的结果为0");
			return  nr;
		}
		nr.setStatus(0);
		nr.setMsg("查询指定发送人的已派发的请求结果成功");
		nr.setData(list);
		return nr;
		}
	public NoteResult findAllReqBySponsorNameByPageNum(String num, String userName) {
		NoteResult nr=new NoteResult();
		Calendar ca = Calendar.getInstance(); 
		int year = ca.get(Calendar.YEAR);
		String date="%"+year+"_%";
		HashMap<String, Object>map=new HashMap<String, Object>();
		map.put("year", date);
		map.put("userName", userName);
		map.put("num", Integer.parseInt(num));
		List<RequestTable>list=requestDao.findAllReqBySponsorNameByPageNum(map);
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("分页查询个人的所有请求结果为0");
			return nr;
		}
		nr.setData(list);
		nr.setMsg("分页查询个人的所有请求成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult findRequestEngineerIdByRequetId(String request_id) {
		NoteResult nr=new NoteResult();
		String engineerId=requestDao.findEngineerIdByRequestId(request_id);
		if(engineerId==null){
			nr.setMsg("查无结果");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("查询执行者的ID成功");
		nr.setStatus(0);
		nr.setData(engineerId);
		return nr;
	}
	public NoteResult findRequestByRequestName(String request_name) {
		NoteResult nr=new NoteResult();
		request_name="%"+request_name+"%";
		List<RequestTable>list=requestDao.findRequestByRequestName(request_name);
		if(list==null||list.size()==0){
			nr.setMsg("查无结果");
			nr.setStatus(1);
			return nr;
		}
		nr.setMsg("查询指定的数据成功");
		nr.setStatus(0);
		nr.setData(list);
		return nr;
	}
}
