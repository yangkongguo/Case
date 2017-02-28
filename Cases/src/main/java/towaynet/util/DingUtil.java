package towaynet.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;


import towaynet.entity.Department;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DingUtil {
	//设置连接
	public static BufferedReader getConnection(String urlNameString){
		BufferedReader in = null;
		try {
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			((HttpURLConnection) connection).setRequestMethod("GET");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(),"utf-8"));
		}catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		return in;

	}
	//post的请求
	public static BufferedReader getConnectionPost(String urlNameString,String userId,String fromDate,String toDate){
		BufferedReader in = null;
		//PrintWriter out=null;
		try {
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpURLConnection connection= (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-Type", "application/json");
			//connection.addRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);// 使用 URL 连接进行输出   
			connection.setDoInput(true);// 使用 URL 连接进行输入   
			connection.setUseCaches(false);// 忽略缓存   
			connection.setRequestMethod("POST");// 设置URL请求方法
			// 建立实际的连接
			connection.connect();
			// 获取URLConnection对象对应的输出流
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());  
			JSONObject obj = new JSONObject();
			obj.put("userId", userId);
			obj.put("workDateFrom", fromDate);
			obj.put("workDateto", toDate);
			out.write(obj.toString().getBytes("UTF-8"));//这样可以处理中文乱码问题 
			// flush输出流的缓冲
			out.flush();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(),"utf-8"));
		}catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}finally{

		}
		return in;

	}
	//获取整个JSON中的指定数据
	public static String getDingJSON(BufferedReader in,String sign){
		String result=""; 
		String line="";
		try {
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject obj = JSONObject.fromObject(result);
		String data=(String) obj.get(sign);
		return data;
	}
	//获取整个JSON
	public static String getDingJSON(BufferedReader in){
		String result=""; 
		String line="";
		try {
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	//获取到token——access
	public synchronized static String  getAccessToken(String corpid,String corpseret){
		String urlNameString="https://oapi.dingtalk.com/gettoken?corpid="+corpid+"&corpsecret="+corpseret;
		BufferedReader in=getConnection(urlNameString);
		String accessToken=getDingJSON(in,"access_token");
		return accessToken;
	}
	//获取到SsoToken
	public static String getSsoToken(String corpid,String corpsecret){
		String urlNameString="https://oapi.dingtalk.com/sso/gettoken?corpid="+corpid+"&corpsecret="+corpsecret;
		BufferedReader in=getConnection(urlNameString);
		String SsoToken=getDingJSON(in,"access_token");
		return SsoToken;
	}

	//获取到deptIn 查看部门信息 查看指定部门下属的所有信息  获取指定的部门下所有员工信息
	public static List<HashMap<String, String>> getdeptem(String token,String depteName){
		if(depteName==null||depteName.equals("")){
			return null;
		}
		String urlNameString="https://oapi.dingtalk.com/department/list?access_token="+token;
		BufferedReader in=getConnection(urlNameString);
		//
		String deptJson=getDingJSON(in);
		JSONObject obj = JSONObject.fromObject(deptJson);
		JSONArray array= (JSONArray) obj.get("department");
		List<Department> list=(List) JSONArray.toCollection(array,Department.class);
		//获取指定的部门ID
		int id=0;
		for (Department deptement : list) {
			if(depteName.equals(deptement.getName())){
				id=deptement.getId();
				break;
			}
		}
		urlNameString="https://oapi.dingtalk.com/user/list?access_token="+token+"&department_id="+id;
		in=getConnection(urlNameString);
		String duser=getDingJSON(in);
		//获取指定的员工信息
		obj = JSONObject.fromObject(duser);
		//获取指定部门下的所有用户信息
		array= (JSONArray) obj.get("userlist");
		List<HashMap<String, String>>mapList=new ArrayList<HashMap<String,String>>();
		for (int i=0;i<array.size();i++) {
			JSONObject jobe=(JSONObject) array.get(i);
			HashMap< String, String >map=new HashMap<String, String>();
			map.put("name", jobe.getString("name"));
			map.put("userid", jobe.getString("userid"));
			mapList.add(map);
		}
		return mapList;
	}
	//获取所有部门信息
	public static List<Department> getdeptem(String token){
		String urlNameString="https://oapi.dingtalk.com/department/list?access_token="+token;
		BufferedReader in=getConnection(urlNameString);
		//
		String deptJson=getDingJSON(in);

		JSONObject obj = JSONObject.fromObject(deptJson);
		JSONArray array= (JSONArray) obj.get("department");
		List<Department> list=(List) JSONArray.toCollection(array,Department.class);
		return list;
	}
	//获取ticket
	public synchronized static String getTicket(String token){
		String urlNameString="https://oapi.dingtalk.com/get_jsapi_ticket?access_token="+token;
		BufferedReader in=getConnection(urlNameString);
		//获取到json里面的指定数据
		String ticket=getDingJSON(in,"ticket");
		return ticket;
	}
	//获取用户ID
	public static String getUserId(String code) {
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
		String token=getAccessToken(corpid, corpsecret);
		String url="https://oapi.dingtalk.com/user/getuserinfo?access_token="+token+"&code="+code;
		BufferedReader in=getConnection(url);
		//获取到json里面的指定数据
		//String errcode=getDingJSON(in,"errcode");
		String userid=getDingJSON(in,"userid");
		return userid;
	}
	//获取用户信息
	public static HashMap<String, String> getUserInfo(String userId) {
		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
		String token=getAccessToken(corpid, corpsecret);
		String url="https://oapi.dingtalk.com/user/get?access_token="+token+"&userid="+userId;
		BufferedReader in=getConnection(url);
		//获取到json里面的指定数据
		String result=""; 
		String line="";
		try {
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject obj = JSONObject.fromObject(result);
		String name=(String) obj.getString("name");
		String dep=obj.getString("department");
		HashMap<String, String>map=new HashMap<String, String>();
		map.put("dep", dep);
		map.put("name", name);
		return map;
	}
	//获取签名
	public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws OApiException, UnsupportedEncodingException {
		String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
		+ "&url=" + url;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return bytesToHex(sha1.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new OApiResultException(e.getMessage());
		}
	}
	
	//签名编码
	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	//获取考勤数据
	public static void appadd(String urlName,String todate,String fromdate,String userId) { 

		try { 
			//创建连接 
			URL url = new URL(urlName); 
			HttpURLConnection connection = (HttpURLConnection) url 
					.openConnection(); 
			connection.setDoOutput(true); 
			connection.setDoInput(true); 
			connection.setRequestMethod("POST"); 
			connection.setUseCaches(false); 
			connection.setInstanceFollowRedirects(true); 
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Content-Type", 
					"application/json"); 

			connection.connect(); 

			//POST请求 
			DataOutputStream out = new DataOutputStream( 
					connection.getOutputStream()); 
			JSONObject obj = new JSONObject(); 
			obj.element("userId", userId); 
			obj.element("workDateFrom", fromdate); 
			obj.element("workDateTo", todate); 

			out.writeBytes(obj.toString()); 
			out.flush(); 
			out.close(); 

			//读取响应 
			BufferedReader reader = new BufferedReader(new InputStreamReader( 
					connection.getInputStream())); 
			String lines; 
			StringBuffer sb = new StringBuffer(""); 
			while ((lines = reader.readLine()) != null) { 
				lines = new String(lines.getBytes(), "utf-8"); 
				sb.append(lines); 
			} 
			reader.close(); 
			// 断开连接 
			connection.disconnect(); 
		} catch (MalformedURLException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (UnsupportedEncodingException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (IOException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 

	} 
	//发起会话功能
	public static String huihua(String urlName,String userId,String cid,String text) { 
		try { 
			//创建连接 
			URL url = new URL(urlName); 
			HttpURLConnection connection = (HttpURLConnection) url 
					.openConnection(); 
			connection.setDoOutput(true); 
			connection.setDoInput(true); 
			connection.setRequestMethod("POST"); 
			connection.setUseCaches(false); 
			connection.setInstanceFollowRedirects(true); 
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("Content-Type", 
					"application/json"); 
			connection.setRequestProperty("Accept-Charset", "utf-8");
			connection.setRequestProperty("contentType", "utf-8");
			connection.connect(); 

			//POST请求 
			DataOutputStream out = new DataOutputStream( 
					connection.getOutputStream()); 
			JSONObject obj = new JSONObject(); 
			obj.element("sender", userId); 
			obj.element("cid", cid); 
			obj.element("msgtype", "text"); 
			obj.element("text", text);
			//URLEncoder.encode(jsStr, "utf-8");
			out.write(obj.toString().getBytes("utf-8"));
			out.flush(); 
			out.close(); 

			//读取响应 
			BufferedReader reader = new BufferedReader(new InputStreamReader( 
					connection.getInputStream())); 
			String lines; 
			StringBuffer sb = new StringBuffer(""); 
			while ((lines = reader.readLine()) != null) { 
				lines = new String(lines.getBytes(), "utf-8"); 
				sb.append(lines); 
			} 
			reader.close(); 
			// 断开连接 
			connection.disconnect(); 
			String ss=sb.toString();
			return ss;
		} catch (MalformedURLException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (UnsupportedEncodingException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (IOException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
			return null;
	} 
	//main方法测试
	public static void main(String[] args) throws IOException {
		for(int i=0;i<10;i++){
			System.out.println("i"+i);
			if(i==5){
				return;
			}
		}
		System.out.println("Test");
//		//		System.out.println(token);
//		//		String urlName="https://oapi.dingtalk.com/attendance/list?access_token="+token;
//		//		appadd(urlName, "2016-12-28 16:00:00", "2016-12-25 08:00:00", "06164161313317");
//		Properties prop = new Properties();     
//		InputStream is=null;
//		OutputStream oFile=null;
//		try{
//			//读取属性文件a.properties
//			is=new BufferedInputStream(new FileInputStream("../flushNum.proierties")); 
//			//BufferedInputStream bis=new BufferedInputStream(is);
//			System.out.println(is);
//			prop.load(is);     ///加载属性列表re
//			String i=prop.getProperty("num");
//			System.out.println(i);
//			//bis.close();
//			///保存属性到b.properties文件
//			oFile = new FileOutputStream("flushNum.proierties",true);//true表示追加打开
//			System.out.println(oFile);
//			prop.setProperty("nall", "10086");
//			String d=prop.getProperty("nall");
//			System.out.println(d);
//			prop.store(oFile, "Update '" + "ddd" + "' value");  
//			System.out.println("ok");
//		}
//		catch(Exception e){
//			System.out.println(e);
//		}finally{
//			if(is!=null){
//			is.close();
//			}
//			if(oFile!=null){
//				oFile.close();
//				}
//		}
		//获取所有的部门信息
//		String corpid="dingdda4cfb59500c8f635c2f4657eb6378f";
//		String corpsecret="S3BgsmqGuFv7-9_9Mr20Me59DcnVX9pH05HJGZDLavli6MiMq803onk6RBdXzU_f";
//		String token=getAccessToken(corpid, corpsecret);
//		List<Department>list=getdeptem(token);
//		System.out.println(list);
//		String dId="23518071";
//		List<HashMap<String, String>>listMap=getdeptem(token, "运营部");
//		System.out.println(listMap);
	
	} 
}
