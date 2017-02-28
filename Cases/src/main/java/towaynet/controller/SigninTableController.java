package towaynet.controller;





import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;
import towaynet.service.SigninTableService;
import towaynet.util.NoteResult;

@Controller//扫描
public class SigninTableController {
	@Resource//注入
	private SigninTableService signinService;

	//指定用户的签到签退查看
	@RequestMapping("/signin/signinTableList.do")
	@ResponseBody
	public synchronized  NoteResult signintList(String engineerId,HttpServletResponse res){
		NoteResult result = 
				signinService.signinTableList(engineerId);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	//所有签到签退查看
	@RequestMapping("/signin/signinTableListAll.do")
	@ResponseBody
	public synchronized  NoteResult signintList(HttpServletResponse res){
		NoteResult result = 
				signinService.signinTableListAll();
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	
	@RequestMapping("/signin/signinIn.do")
	@ResponseBody
	public synchronized  NoteResult signinIn(String userid,String cid,String signin_id,String map,String image,String name,String workTaskType,String workTaskName,HttpServletResponse res) throws Exception{
		NoteResult result = 
				signinService.signinIn(userid,cid,signin_id,map,image,name,workTaskType,workTaskName);

		System.out.println("signController:66:"+image.length());
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	
	@RequestMapping("/signin/signinOut.do")
	@ResponseBody
	public synchronized  NoteResult signinOut(String userid,String cid,String workTask_id,String signin_id,String map,String image,String name,String workTaskType,String workTaskName,HttpServletResponse res) throws Exception{
		NoteResult result = 
				signinService.signinOut(userid,cid,workTask_id,signin_id,map,image,name,workTaskType,workTaskName);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	
	@RequestMapping("/signin/whoFinsh.do")
	@ResponseBody
	public NoteResult whoFinsh(String engineerId,HttpServletResponse res) throws Exception{
		NoteResult result = 
					signinService.signinTableWhoFinsh(engineerId);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	@RequestMapping("/signin/signinTableListAllLT2.do")
	@ResponseBody
	public synchronized  NoteResult signinTableListAllLT2(String engineerId,HttpServletResponse res) throws Exception{
		NoteResult result = 
					signinService.signinTableListAllLT2();
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	@RequestMapping("/signin/findSigninByWID.do")
	@ResponseBody
	public synchronized  NoteResult findSigninByWID(String workTask_id,HttpServletResponse res) throws Exception{
		NoteResult result = 
					signinService.findSigninByWID(workTask_id);
		res.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	@RequestMapping("/image/testImage.do")
	@ResponseBody
	public synchronized  NoteResult sendPic(String data,HttpServletResponse res) throws Exception{
		res.setHeader("Access-Control-Allow-Origin", "*");
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(data);//转码得到图片数据
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		BufferedImage bi1 = ImageIO.read(bais);
		Date d=new Date();
		int i=(int)Math.random()*10;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String num="d://"+sdf.format(d)+i+".png";
		File w2 = new File(num);
		ImageIO.write(bi1, "png", w2);
		NoteResult nr=new NoteResult();
		nr.setStatus(0);
		nr.setMsg("成功");
		return nr;
		}
	}
