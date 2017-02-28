package towaynet.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import towaynet.service.RequestTableService;
import towaynet.util.NoteResult;

@Controller//扫描
public class RequestTableController {
	@Resource//注入
	private RequestTableService requestService;
	//添加请求
	@RequestMapping("/request/save.do")
	@ResponseBody
	public synchronized NoteResult save(String caseName,String caseMap,String caseType,String caseGuest,String casePhone,String caseGuestCompany,
			String caseDescr,String start_time,String finsh_time,String request_id,String userid,String username,HttpServletResponse res){
		NoteResult result = 
			requestService.saveRequestTable(caseName,caseMap,caseType,caseGuest,casePhone,caseDescr,start_time,finsh_time,request_id,userid,username,caseGuestCompany);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//保存到草稿的请求
	@RequestMapping("/request/draft.do")
	@ResponseBody
	public synchronized NoteResult draft(String caseName,String caseMap,String caseType,String caseGuest,String casePhone,
			String caseDescr,String start_time,String finsh_time,String userid,HttpServletResponse res){
		NoteResult result = 
			requestService.draftRequestTable(caseName,caseMap,caseType,caseGuest,casePhone,caseDescr,start_time,finsh_time,userid);
		System.out.println("ReqControll:32:"+userid+";"+caseName+";"+caseMap+";"+caseType+";"+caseGuest+";"+casePhone+";"+caseDescr+";"+start_time+";"+finsh_time);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//所有请求的查看
	@RequestMapping("/request/requestList.do")
	@ResponseBody
	public synchronized NoteResult requestList(HttpServletResponse res){
		NoteResult result = 
			requestService.requestList();
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//忽略请求表功能
	@RequestMapping("/request/requestIgnore.do")
	@ResponseBody
	public synchronized  NoteResult requestIgnore(String request_id,HttpServletResponse res){
		NoteResult result = 
			requestService.requestIgnore(request_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//确认请求表功能
	@RequestMapping("/request/requestConfirm.do")
	@ResponseBody
	public synchronized NoteResult requestConfrim(String request_id,String engineers,String type,String startTimes,String finshTimes,HttpServletResponse res){
		NoteResult result = 
				requestService.requestConfirm(request_id,engineers,type,startTimes,finshTimes);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查询草稿箱功能
	@RequestMapping("/request/findDraftList.do")
	@ResponseBody
	public synchronized  NoteResult draftList(String userid,HttpServletResponse res){
		NoteResult result = 
				requestService.draftList(userid);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//删除草稿箱功能
	@RequestMapping("/request/deleteDraftRequest.do")
	@ResponseBody
	public synchronized  NoteResult deleteDraftRequest(String request_id,HttpServletResponse res){
		NoteResult result = 
				requestService.deleteDraftRequest(request_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查询所有请求根据发送人名功能
	@RequestMapping("/request/findAllReqBySponsorName.do")
	@ResponseBody
	public synchronized  NoteResult findAllReqBy(String userName,HttpServletResponse res){
		System.out.println("90");
		NoteResult result = 
				requestService.findAllReqBySponsorName(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//分页查询所有请求根据发送人名的功能
	@RequestMapping("/request/findAllReqBySponsorNameByPageNum.do")
	@ResponseBody
	public synchronized  NoteResult findAllReqBySponsorNameByPageNum(String num,String userName,HttpServletResponse res){
		System.out.println("90");
		NoteResult result = 
				requestService.findAllReqBySponsorNameByPageNum(num, userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//获取销售的显示数据
	//查看数据值
	@RequestMapping("/request/findSaleReqNum.do")
	@ResponseBody
	public synchronized  NoteResult findSaleReqNum(String userName,HttpServletResponse res){
		NoteResult result=requestService.findSaleReqNum(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查看派工
	@RequestMapping("/request/findSendTask.do")
	@ResponseBody
	public synchronized  NoteResult findSubTask(String userName,HttpServletResponse res){
		NoteResult result=requestService.findSubTask(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查看执行		
	@RequestMapping("/request/findExcute.do")
	@ResponseBody
	public synchronized  NoteResult findExcuteTask(String userName,HttpServletResponse res){
		System.out.println(":255：");
		NoteResult result=requestService.findExcuteTask(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查看关闭的
	@RequestMapping("/request/findCloseTask.do")
	@ResponseBody
	public synchronized   NoteResult findCloseTask(String userName,HttpServletResponse res){
		NoteResult result=requestService.findCloseTask(userName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//根据搜索名查看请求的
	@RequestMapping("/request/findRequestBySponsorName.do")
	@ResponseBody
	public synchronized   NoteResult findBySponsorName(String sponsorName,HttpServletResponse res){
		NoteResult result=requestService.findBySponsorName(sponsorName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查询已派发的请求
	@RequestMapping("/request/requestListByAlreadySend.do")
	@ResponseBody
	public synchronized   NoteResult requestListByAlreadySend(HttpServletResponse res){
		NoteResult result=requestService.requestListByAlreadySend();
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查询已派发的请求根据输入名
	@RequestMapping("/request/findSendRequestBySponsorName.do")
	@ResponseBody
	public synchronized   NoteResult findSendRequestBySponsorName(String sponsorName,HttpServletResponse res){
		NoteResult result=requestService.findSendRequestBySponsorName(sponsorName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	
	//查询执行者的ID
	@RequestMapping("/request/findRequestEngineerId.do")
	@ResponseBody
	public synchronized   NoteResult findRequestEngineerId(String request_id,HttpServletResponse res){
		NoteResult result=requestService.findRequestEngineerIdByRequetId(request_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//查询执行者的ID
	@RequestMapping("/request/findRequestByRequestName.do")
	@ResponseBody
	public synchronized   NoteResult findRequestByRequestName(String request_name,HttpServletResponse res){
		NoteResult result=requestService.findRequestByRequestName(request_name);
		res.setHeader("Access-Control-Allow-Origin", "*");
			return result;
	}
}
