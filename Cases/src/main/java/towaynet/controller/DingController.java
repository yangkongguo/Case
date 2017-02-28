package towaynet.controller;



import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import towaynet.dao.DingTokenDao;
import towaynet.entity.Department;
import towaynet.entity.DingToken;
import towaynet.util.DingUtil;
import towaynet.util.NoteResult;
import towaynet.util.OApiException;




@Controller//扫描
public class DingController {
	//钉钉测试-AccessToken
	@Resource//注入
	private DingTokenDao dingTokenDao;
	
	@RequestMapping("/table/dingdingTest.do")
	@ResponseBody
	public NoteResult testDingDing(){
		System.out.println("DingDingTest-AccessToken");
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
		String accesstoken=DingUtil.getAccessToken(corpid, corpsecret);
		NoteResult nr=new NoteResult();
		nr.setData(accesstoken);
		return nr;
	}
	//钉钉测试-SsoToken
	@RequestMapping("/table/dingdingTest-SsoToken.do")
	@ResponseBody
	public NoteResult testDingDingSsoToken(){
		System.out.println("DingDingTest-SsoToken");
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String SSOSecret="VqjyRTwIpSsihXBbDg3fsCASoayXrVgn1Wlz84Oghyc_I8XGhHbAT-ZKVhHMQZPS";
		String sstoken=DingUtil.getSsoToken(corpid, SSOSecret);
		NoteResult nr=new NoteResult();
		nr.setData(sstoken);
		return nr;
	}
	//钉钉测试-获取部门List
	@RequestMapping("/table/dingdingTest-deptList.do")
	@ResponseBody
	public synchronized  NoteResult testDingDingDeptList(HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
		DingToken dt= dingTokenDao.findDingToken();
		long dingTokenTimeMills=Long.parseLong(dt.getDingToken_time());
		long timeStamp = System.currentTimeMillis();
		String token="";
		if(timeStamp-dingTokenTimeMills<7000*1000){
			token=dt.getDingToken_body();
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
		List<HashMap<String, String>>deptEmpleList=DingUtil.getdeptem(token,"技术部");
		NoteResult nr=new NoteResult();
		nr.setData(deptEmpleList);
		nr.setStatus(0);
		return nr;
	}
	//钉钉测试-获取部门List
	@RequestMapping("/workTask/getDeptList.do")
	@ResponseBody
	public NoteResult testDingDingDept(HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println("DingDingTest-deptList");
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
		String token=DingUtil.getAccessToken(corpid, corpsecret);
		System.out.println("115:"+token);
		List<Department>deptNameList=DingUtil.getdeptem(token);
		System.out.println(deptNameList);
		NoteResult nr=new NoteResult();
		nr.setData(deptNameList);
		nr.setStatus(0);
		return nr;
	}
	private String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
	private String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
	private String url = "http://116.205.12.44:8011/Cases/nowTask_Ding_pc.html";
	private String nonceStr = "nonceStr";
	//获取签名
	@RequestMapping("/ding/getSignPC.do")
	@ResponseBody
	public synchronized NoteResult getSign(HttpServletResponse res) throws UnsupportedEncodingException, OApiException{
		System.out.println(2333+112);
		res.setHeader("Access-Control-Allow-Origin", "*");
		//查表获取到时间
		DingToken dt=dingTokenDao.findDingToken();
		String dingTokenTime=dt.getDingToken_time();
		long dingTokenTimeMills=Long.parseLong(dingTokenTime);
		long timeStamp = System.currentTimeMillis();
		String token="";
		if(timeStamp-dingTokenTimeMills<7000*1000){
			token=dt.getDingToken_body();
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
}
