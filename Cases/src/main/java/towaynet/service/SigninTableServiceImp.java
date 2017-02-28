package towaynet.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import towaynet.dao.RequestTableDao;
import towaynet.dao.SigninTableDao;
import towaynet.dao.WorkTaskDao;
import towaynet.entity.SigninTable;
import towaynet.util.DingUtil;
import towaynet.util.NoteResult;

@Service("signinService")//扫描
@Transactional
public class SigninTableServiceImp implements SigninTableService{
	@Resource
	private SigninTableDao signinTableDao;
	@Resource
	private WorkTaskDao workTaskDao;
	@Resource
	private RequestTableDao requestDao;
	
	public NoteResult signinTableList(String engineerId) {
		List<SigninTable> list=signinTableDao.signinTableList(engineerId);
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询签到签退表异常");
			return nr;
		}
		if(list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查签到表指定数据无结果");
			return nr;
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("查询签到表成功");
		return nr;
	}
	public NoteResult signinTableListAll() {
		List<SigninTable> list=signinTableDao.signinTableListAll();
		NoteResult nr=new NoteResult();
		if(list==null){
			nr.setStatus(1);
			nr.setMsg("查询签到签退表所有数据异常");
			return nr;
		}
		if(list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查签到表所有数据无结果");
			return nr;
		}
		nr.setStatus(0);
		nr.setData(list);
		nr.setMsg("查询签到表所有数据成功");
		return nr;
	}

	public NoteResult signinIn(String userid,String cid,String signin_id,String map,String image,String name,String workTaskType,String workTaskName) throws Exception {
		//存图片
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(image);//转码得到图片数据
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		BufferedImage bi1 = ImageIO.read(bais);
		Date d=new Date();
		int j=(int)Math.random()*10;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String path="e://Case_pic_in/"+name+sdf.format(d)+j+"签到.png";
		File w2 = new File(path);
		ImageIO.write(bi1, "png", w2);
		NoteResult nr=new NoteResult();
		//发起会话
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
		String token=DingUtil.getAccessToken(corpid, corpsecret);
		String urlName="https://oapi.dingtalk.com/message/send_to_conversation?access_token="+token;
		//拼一个任务完成字符串
		//获取时间
		SimpleDateFormat dateFormat=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		String taskTxt=workTaskType+","+workTaskName+"签到时间为："+dateFormat.format(d);
		String s=	DingUtil.huihua(urlName, userid, cid, "{'content':'"+taskTxt+"'}");
		JSONObject jsStr = JSONObject.fromObject(s);
		Integer errcode=(Integer)jsStr.get("errcode");
		if(0!=errcode){
			nr.setMsg("发起会话失败");
			nr.setStatus(3);
			return nr;
		}
		HashMap<String, Object>hashmap=new HashMap<String, Object>();
		hashmap.put("signin_id", signin_id);
		hashmap.put("status", 1);
		hashmap.put("path",path);
		//存入地址
		hashmap.put("map", map);
		Timestamp ts=new Timestamp(System.currentTimeMillis());
		hashmap.put("start_time", ts);
		
		
		//更新请求表
		//获取workTaskid
		String workTaskId=signinTableDao.findWrokTaskId(signin_id);
		String requestId=workTaskDao.findRequestIdByWorkID(workTaskId);
		HashMap<String, Object>requestMap=new HashMap<String, Object>();
		requestMap.put("request_status", 4);
		requestMap.put("request_id", requestId);
		requestMap.put("request_real_startTime", ts);
		int requestUpdateNum=requestDao.updateRequestRealStartTime(requestMap);
		
		//是否成功跟新请求表
		if(requestUpdateNum<=0){
			nr.setStatus(4);
			nr.setMsg("更新Case请求表状态失败");
			return nr;
		}
		HashMap<String, Object>workTaskMap=new HashMap<String, Object>();
		workTaskMap.put("workTask_id", workTaskId);
		//当执行签到时
		workTaskMap.put("status", 1);
		int k=workTaskDao.workTaskUpdate(workTaskMap);
		if(k<0){
			nr.setMsg("更新工单完成状态异常");
			nr.setStatus(1);
			return nr;
		}
		if(k==0){
			nr.setStatus(1);
			nr.setMsg("更新工单状态失败");
			return nr;
		}
		//最后更新签到签退表
		int i=signinTableDao.signinInUpdate(hashmap);
		if(i<0){
			nr.setStatus(1);
			nr.setMsg("签到更新异常");
			return nr;
		}
		if(i==0){
			nr.setMsg("签到更新失败");
			nr.setStatus(1);
			return  nr;
		}
		nr.setData(i);
		nr.setMsg("签到更新成功");
		nr.setStatus(0);
		return nr;
	}
	
	public NoteResult signinOut(String userid,String cid,String workTask_id,String signin_id,String map,String image,String name,String workTaskType,String workTaskName) throws Exception {
		NoteResult nr=new NoteResult();
		//存图片
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(image);//转码得到图片数据
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		BufferedImage bi1 = ImageIO.read(bais);
		Date d=new Date();
		int o=(int)Math.random()*10;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String path="e://Case_pic_out/"+name+sdf.format(d)+o+"签退.png";
		File w2 = new File(path);
		ImageIO.write(bi1, "png", w2);
		//时间轴
		Timestamp ts=new Timestamp(System.currentTimeMillis());
		//更新工单位完成状态 当签退的是最后u一个签退 
		if(!"".equals(workTask_id)){
			HashMap<String, Object>workTaskMap=new HashMap<String, Object>();
			workTaskMap.put("workTask_id", workTask_id);
			//但最后一个签到签退完成
			workTaskMap.put("status", 6);
			
			//更新请求表
			String request_id=workTaskDao.findRequestIdByWorkID(workTask_id);
			HashMap<String, Object>requestMap=new HashMap<String, Object>();
			requestMap.put("request_id",request_id);
			requestMap.put("request_status", 4);
			requestMap.put("request_real_finshTime", ts);
			int x=requestDao.updateRequestRealFinshTime(requestMap);
			if(x<0){
				nr.setMsg("更新工单完成状态异常");
				nr.setStatus(1);
				return nr;
			}
			if(x==0){
				nr.setStatus(1);
				nr.setMsg("更新工单状态失败");
				return nr;
			}
			int j=workTaskDao.workTaskUpdate(workTaskMap);
			if(j<0){
				nr.setMsg("更新工单完成状态异常");
				nr.setStatus(1);
				return nr;
			}
			if(j==0){
				nr.setStatus(1);
				nr.setMsg("更新工单状态失败");
				return nr;
			}
		}
		//发起会话
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
		String token=DingUtil.getAccessToken(corpid, corpsecret);
		String urlName="https://oapi.dingtalk.com/message/send_to_conversation?access_token="+token;
		//拼一个任务完成字符串
		//时间格式
		SimpleDateFormat dateFormat=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		String taskTxt=workTaskType+","+workTaskName+"签退时间为："+dateFormat.format(d);
		String s=	DingUtil.huihua(urlName, userid, cid, "{'content':'"+taskTxt+"'}");
		JSONObject jsStr = JSONObject.fromObject(s);
		Integer errcode=(Integer)jsStr.get("errcode");
		System.out.println("stimp:151:"+errcode);
		if(0!=errcode){
			nr.setMsg("发起会话失败");
			nr.setStatus(3);
			return nr;
		}
		//更新signin表的签退信息
		HashMap<String, Object>hashmap=new HashMap<String, Object>();
		hashmap.put("signin_id", signin_id);
		hashmap.put("status", 2);
		hashmap.put("map", map);
		hashmap.put("path", path);
		hashmap.put("finsh_time", ts);
		int i=signinTableDao.signinOutUpdate(hashmap);
		
		if(i<0){
			nr.setStatus(1);
			nr.setMsg("签退更新异常");
			return nr;
		}
		if(i==0){
			nr.setMsg("签退更新失败");
			nr.setStatus(1);
			return  nr;
		}
		nr.setData(i);
		nr.setMsg("签退更新成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult signinTableWhoFinsh(String engineerId) {
		List<SigninTable>list=signinTableDao.signinTabWhoFinsh(engineerId);
		NoteResult nr=new NoteResult();
		if(list==null||list.size()==0){
			nr.setStatus(1);
			nr.setMsg("查无结果");;
		}
		nr.setData(list);
		nr.setStatus(0);
		nr.setMsg("查询指定工程师下的所有完成任务成功");
		return nr;
	}
	public NoteResult signinTableListAllLT2() {
		NoteResult nr=new NoteResult();
		List<SigninTable>list=signinTableDao.signinTableListAllLT2();
		if(list==null||list.size()==0){
			nr.setMsg("查询指定签到表失败");
			nr.setStatus(1);
			return  nr;
		}
		nr.setData(list);
		nr.setMsg("查询指定签到表成功");
		nr.setStatus(0);
		return nr;
	}
	public NoteResult findSigninByWID(String workTask_id) {
		NoteResult nr=new NoteResult();
		List<SigninTable>list=signinTableDao.findSigninByWID(workTask_id);
		if(list==null||list.size()==0){
			nr.setMsg("查询指定签到表失败");
			nr.setStatus(1);
			return  nr;
		}
		nr.setData(list);
		nr.setMsg("查询指定签到表成功");
		nr.setStatus(0);
		return nr;
	}
}
