package towaynet.controller;



import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Timer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import towaynet.dao.DingTokenDao;
import towaynet.entity.DingToken;
import towaynet.service.WorkTaskService;
import towaynet.util.DingUtil;
import towaynet.util.NoteResult;
import towaynet.util.OApiException;

@Controller//扫描
public class WorkTaskController {
	@Resource//注入
	private WorkTaskService workTaskService;
	
	@Resource
	private DingTokenDao dingTokenDao;
	
	//所有工单查看
	@RequestMapping("/workTask/workTaskList.do")
	@ResponseBody
	public synchronized NoteResult workTaskList(HttpServletResponse res){
		NoteResult result = 
			workTaskService.workTaskList();
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//指定工程师的所有工单
	@RequestMapping("/workTask/workTaskEngineer.do")
	@ResponseBody
	public synchronized NoteResult engineerWorkTask(String engineerName,HttpServletResponse res){
		NoteResult result = 
			workTaskService.engineerWorkTask(engineerName);
		res.setHeader("Access-Control-Allow-Origin", "*");
			return result;
	}
	//指定用户还没完成的工单任务的查看
	@RequestMapping("/workTask/workTaskEngineerUnFinsh.do")
	@ResponseBody
	public synchronized NoteResult engineerWorkTaskUnFinsh(String engineerId,HttpServletResponse res){
		NoteResult result = 
			workTaskService.engineerWorkTaskUnFinsh(engineerId);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}	
	//指定工程师的所有完成任务
	@RequestMapping("/workTask/workTaskEngineerFinsh.do")
	@ResponseBody
	public synchronized NoteResult engineerWorkTaskFinsh(String engineerId,HttpServletResponse res){
		NoteResult result = 
			workTaskService.engineerWorkTaskFinsh(engineerId);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}	
	//指定工程师的所有完成任务
	@RequestMapping("/workTask/workTaskEngineerFinshByMon.do")
	@ResponseBody
	public synchronized NoteResult engineerWorkTaskFinshByMon(String engineer_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.engineerWorkTaskFinshByMon(engineer_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}	
	//指定用户更新备注
	@RequestMapping("/workTask/updateRemark.do")
	@ResponseBody
	public synchronized NoteResult engineerWorkTaskUnpateRemark(String remark,String workTask_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.updateRemark(remark, workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
		}
	//查看指定用户的备注
	@RequestMapping("/workTask/findRemarkByWorkID.do")
	@ResponseBody
	public NoteResult findRemark(String workTask_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.findRemarkByWorkTaskId(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}
	//申请删除指定工单
	@RequestMapping("/workTask/applyDeleteWorkTaskByWID.do")
	@ResponseBody
	public synchronized NoteResult applDeleteWorkTaskByWID(String workTask_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.applyDeleteWorkTaskByWID(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}
	//申请完成关闭Close指定工单
	@RequestMapping("/workTask/applyCompleteWorkTaskByWID.do")
	@ResponseBody
	public synchronized NoteResult applCompleteWorkTaskByWID(String workTask_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.applyCompleteWorkTaskByWID(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}
	
	//删除指定工单
	@RequestMapping("/workTask/DeleteWorkTaskByWID.do")
	@ResponseBody
	public synchronized NoteResult deleteWorkTaskByWID(String workTask_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.workTaskdeleteByWID(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}
	
	//关闭指定工单
	@RequestMapping("/workTask/CompleteWorkTaskByWID.do")
	@ResponseBody
	public synchronized NoteResult completeWorkTaskByWID(String workTask_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.workTaskcompleteByWID(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}
	private String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
	private String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
	private String url = "http://116.205.12.44:8011/Cases/nowTask_Ding.html";
	private String nonceStr = "nonceStr";
	//获取签名
	@RequestMapping("/workTask/getSign.do")
	@ResponseBody
	public synchronized NoteResult getSign(HttpServletResponse res) throws UnsupportedEncodingException, OApiException{
		System.out.println(233+122);
		res.setHeader("Access-Control-Allow-Origin", "*");
		//查表获取到时间
		DingToken dt=dingTokenDao.findDingToken();
		//获取上次存入的token时间
		String dingTokenTime=dt.getDingToken_time();
		long dingTokenTimeMills=Long.parseLong(dingTokenTime);
		//获取当前时间
		long timeStamp = System.currentTimeMillis();
		String token="";
		//当当前时间与获取过token 的时间差小于7000*1000毫秒就直接用之前的token
		if(timeStamp-dingTokenTimeMills<7000*1000){
			token=dt.getDingToken_body();
		//当时间差大于7000*1000毫秒就重新获取，并存入新的token跟时间到表中
		}else{
			token=DingUtil.getAccessToken(corpid, corpsecret);
			HashMap<String, Object>map=new HashMap<String, Object>();
			map.put("dingToken_time", timeStamp);
			map.put("dingToken_body", token);
			int i=dingTokenDao.dingTokenUpdate(map);
			if(i<=0){
				NoteResult nr=new NoteResult();
				nr.setMsg("更新Token表失败，请刷新");
				nr.setStatus(1);
				return nr;
			}
		}
		String ticket=DingUtil.getTicket(token);
		
		String signature =DingUtil.sign(ticket, nonceStr, timeStamp, url);
		NoteResult nr=new NoteResult();
		//HashMap<String, String>map=new HashMap<String, String>();
		nr.setData(signature);
		nr.setMsg(timeStamp+"");
		nr.setStatus(0);
		return nr;
	}
	//免登
	@RequestMapping("/workTask/freeLogin.do")
	@ResponseBody
	public synchronized NoteResult FreeLogink(String code,HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		String userId=DingUtil.getUserId(code);
		HashMap<String, String>map=DingUtil.getUserInfo(userId);
		map.put("userid", userId);
		NoteResult nr=new NoteResult();
		nr.setData(map);
		return nr;
	}	
	//查询指定的请求人下的工单
	@RequestMapping("/workTask/findWorkTaskBySponsorName.do")
	@ResponseBody
	public synchronized NoteResult findRequestByUserid(String userName,HttpServletResponse res){
		NoteResult result = workTaskService.findWorkTaskBySponsorName(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//工程师下的搜索根据请求人
	@RequestMapping("/workTask/findWorkTaskBySponsorNameToEngineer.do")
	@ResponseBody
	public synchronized NoteResult findWorkTaskBySponsorNameToEngineer(String sponsorName,String userName,HttpServletResponse res){
		NoteResult result = workTaskService.findWorkTaskBySponsorNameToEngineer(sponsorName,userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查询所有的申请删除工单
	@RequestMapping("/workTask/findApplyDeleteWT.do")
	@ResponseBody
	public synchronized NoteResult huihuaTest(String cid,String token,String str,HttpServletResponse res){
		NoteResult result=workTaskService.findApplyDeleteWT();
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查询申请完成关闭Case指定工单
	@RequestMapping("/workTask/findApplyCompleteWT.do")
	@ResponseBody
	public synchronized NoteResult findCompleteWorkTaskByWT(String workTask_id,HttpServletResponse res){
		NoteResult result = 
			workTaskService.findApplyCompleteWT();
		System.out.println("170");
		res.setHeader("Access-Control-Allow-Origin", "*");
		return  result;
	}
	//拒绝删除该工单的请求
	@RequestMapping("/workTask/rejectDeleteWorkTaskByWID.do")
	@ResponseBody
	public synchronized NoteResult rejectDeleteWT(String workTask_id,HttpServletResponse res){
		NoteResult result=workTaskService.rejectDeleteWorkTask(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//拒绝关闭该	Case的请求
	@RequestMapping("/workTask/rejectCompleteWorkTaskByWID.do")
	@ResponseBody
	public synchronized NoteResult rejectCompleteWT(String workTask_id,HttpServletResponse res){
		NoteResult result=workTaskService.rejectCompleteWorkTask(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	///workTask/findNumberByTheMon.do
	//获取制定用户的当月工单的请求
	@RequestMapping("/workTask/findNumberByTheMon.do")
	@ResponseBody
	public synchronized NoteResult findNumberByTheMon(String engineerId,HttpServletResponse res){
		NoteResult result=workTaskService.findNumberByTheMon(engineerId);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//用户的排名的请求 根据月份
	@RequestMapping("/workTask/findPaiMingByEID.do")
	@ResponseBody
	public synchronized NoteResult findPaiMingByEID(String engineerId,HttpServletResponse res){
		NoteResult result=workTaskService.findPaiMingByEId(engineerId);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//获取根据工单名search的结果
	@RequestMapping("/workTask/searchByWorkTaskName.do")
	@ResponseBody
	public synchronized NoteResult searchByWorkTaskName(String workTaskName,String engineerId,HttpServletResponse res){
		NoteResult result=workTaskService.searchWorkTaskFinshByWorkName(engineerId, workTaskName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	///workTask/findEngineerIdWhoWorkNow.do"
	//获取工程师id
	@RequestMapping("/workTask/findEngineerIdWhoWorkNow.do")
	@ResponseBody
	public synchronized NoteResult findEngineerIdWhoWorkNow(HttpServletResponse res){
		NoteResult result=workTaskService.findEngineerIdWhoWorkNow();
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//获取销售的显示数据
	//获取工程师id
	@RequestMapping("/workTask/findSaleReqNum.do")
	@ResponseBody
	public synchronized NoteResult findSaleReqNum(String userName,HttpServletResponse res){
		NoteResult result=workTaskService.findSaleReqNum(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查看派工
	@RequestMapping("/workTask/findSendTask.do")
	@ResponseBody
	public synchronized NoteResult findSubTask(String userName,HttpServletResponse res){
		NoteResult result=workTaskService.findSubTask(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查看执行		/workTask/findExcuteTask.do
	@RequestMapping("/workTask/findExcute.do")
	@ResponseBody
	public synchronized NoteResult findExcuteTask(String userName,HttpServletResponse res){
		System.out.println(":255：");
		NoteResult result=workTaskService.findExcuteTask(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查看关闭的
	@RequestMapping("/workTask/findCloseTask.do")
	@ResponseBody
	public synchronized NoteResult findCloseTask(String userName,HttpServletResponse res){
		NoteResult result=workTaskService.findCloseTask(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//分页查询
	@RequestMapping("/workTask/pageSelect.do")
	@ResponseBody
	public synchronized NoteResult pageSelect(String num,HttpServletResponse res){
		NoteResult result=workTaskService.pageSelect(num);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
}
