package towaynet.entity;

import java.sql.Timestamp;

public class RequestTable {
	//请求ID
	private String request_id ;
	//请求名称
	private String request_name ;
	//请求地点
	private String request_map ;
	//请求客户电话
	private String request_guest_name ;
	//请求客户电话
	private String request_guest_tel ;
	//派工请求描述
	private String request_desc ;
	//请求开始时间
	private Timestamp request_start_time ;
	//请求结束时间
	private Timestamp request_finsh_time ;
	//请求状态码
	private Integer request_status ;
	//请求的类型
	private Integer request_type;
	//提出请求的人 ID
	private String request_sponsor_id ;
	//提出人名
	private String request_sponsor_name ;
	//客户单位名称
	private String request_guest_company;
	//case号
	private String request_caseNum;
	//请求执行者
	private String request_excute_engineerName;
	//请求执行者ID
	private String request_engineerId;
	//请求真正开始时间
	private Timestamp request_real_startTime;
    //请求真正结束时间
	private Timestamp request_real_finshTime; 
	
	
	public String getRequest_caseNum() {
		return request_caseNum;
	}
	public void setRequest_caseNum(String request_caseNum) {
		this.request_caseNum = request_caseNum;
	}
	//getset
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getRequest_name() {
		return request_name;
	}
	public void setRequest_name(String request_name) {
		this.request_name = request_name;
	}
	public String getRequest_map() {
		return request_map;
	}
	public void setRequest_map(String request_map) {
		this.request_map = request_map;
	}
	
	
	public String getRequest_guest_tel() {
		return request_guest_tel;
	}
	public void setRequest_guest_tel(String request_guest_tel) {
		this.request_guest_tel = request_guest_tel;
	}
	public String getRequest_desc() {
		return request_desc;
	}
	public void setRequest_desc(String request_desc) {
		this.request_desc = request_desc;
	}
	public Timestamp getRequest_start_time() {
		return request_start_time;
	}
	public void setRequest_start_time(Timestamp request_start_time) {
		this.request_start_time = request_start_time;
	}
	public Timestamp getRequest_finsh_time() {
		return request_finsh_time;
	}
	public void setRequest_finsh_time(Timestamp request_finsh_time) {
		this.request_finsh_time = request_finsh_time;
	}
	public Integer getRequest_status() {
		return request_status;
	}
	public void setRequest_status(Integer request_status) {
		this.request_status = request_status;
	}
	public String getRequest_sponsor_id() {
		return request_sponsor_id;
	}
	public void setRequest_sponsor_id(String request_sponsor_id) {
		this.request_sponsor_id = request_sponsor_id;
	}
	public Integer getRequest_type() {
		return request_type;
	}
	public void setRequest_type(Integer request_type) {
		this.request_type = request_type;
	}
	public String getRequest_guest_name() {
		return request_guest_name;
	}
	public void setRequest_guest_name(String request_guest_name) {
		this.request_guest_name = request_guest_name;
	}
	public String getRequest_sponsor_name() {
		return request_sponsor_name;
	}
	public void setRequest_sponsor_name(String request_sponsor_name) {
		this.request_sponsor_name = request_sponsor_name;
	}
	public String getRequest_guest_company() {
		return request_guest_company;
	}
	public void setRequest_guest_company(String request_guest_company) {
		this.request_guest_company = request_guest_company;
	}
	public String getRequest_excute_engineerName() {
		return request_excute_engineerName;
	}
	public void setRequest_excute_engineerName(String request_excute_engineerName) {
		this.request_excute_engineerName = request_excute_engineerName;
	}
	public Timestamp getRequest_real_startTime() {
		return request_real_startTime;
	}
	public void setRequest_real_startTime(Timestamp request_real_startTime) {
		this.request_real_startTime = request_real_startTime;
	}
	public Timestamp getRequest_real_finshTime() {
		return request_real_finshTime;
	}
	public void setRequest_real_finshTime(Timestamp request_real_finshTime) {
		this.request_real_finshTime = request_real_finshTime;
	}
	public String getRequest_engineerId() {
		return request_engineerId;
	}
	public void setRequest_engineerId(String request_engineerId) {
		this.request_engineerId = request_engineerId;
	}

	
	
	
	
}
