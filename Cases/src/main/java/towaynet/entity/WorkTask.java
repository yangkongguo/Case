package towaynet.entity;

import java.sql.Timestamp;

public class WorkTask {
	private String workTask_id ;
	private String workTask_num;
	private String workTask_name ;
	private String workTask_map ;
	private String workTask_guest_name ;
	private String workTask_guest_tel ;
	private String workTask_desc ;
	private Timestamp workTask_start_time ;
	private Timestamp workTask_finsh_time ;
	private Integer workTask_status ;
	private Integer workTask_type;
	private String workTask_engineer_name ;
	private String workTask_engineer_id;
	private String workTask_sponsor_name;
	private String workTask_engineer_remark;
	private String request_id;
	
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getWorkTask_id() {
		return workTask_id;
	}
	public void setWorkTask_id(String workTask_id) {
		this.workTask_id = workTask_id;
	}
	public String getWorkTask_num() {
		return workTask_num;
	}
	public void setWorkTask_num(String workTask_num) {
		this.workTask_num = workTask_num;
	}
	public String getWorkTask_name() {
		return workTask_name;
	}
	public void setWorkTask_name(String workTask_name) {
		this.workTask_name = workTask_name;
	}
	public String getWorkTask_map() {
		return workTask_map;
	}
	public void setWorkTask_map(String workTask_map) {
		this.workTask_map = workTask_map;
	}
	
	public String getWorkTask_guest_tel() {
		return workTask_guest_tel;
	}
	public void setWorkTask_guest_tel(String workTask_guest_tel) {
		this.workTask_guest_tel = workTask_guest_tel;
	}
	public String getWorkTask_desc() {
		return workTask_desc;
	}
	public void setWorkTask_desc(String workTask_desc) {
		this.workTask_desc = workTask_desc;
	}
	public Timestamp getWorkTask_start_time() {
		return workTask_start_time;
	}
	public void setWorkTask_start_time(Timestamp workTask_start_time) {
		this.workTask_start_time = workTask_start_time;
	}
	public Timestamp getWorkTask_finsh_time() {
		return workTask_finsh_time;
	}
	public void setWorkTask_finsh_time(Timestamp workTask_finsh_time) {
		this.workTask_finsh_time = workTask_finsh_time;
	}
	public Integer getWorkTask_status() {
		return workTask_status;
	}
	public void setWorkTask_status(Integer workTask_status) {
		this.workTask_status = workTask_status;
	}
	public String getWorkTask_engineer_name() {
		return workTask_engineer_name;
	}
	public void setWorkTask_engineer_name(String workTask_engineer_name) {
		this.workTask_engineer_name = workTask_engineer_name;
	}
	public Integer getWorkTask_type() {
		return workTask_type;
	}
	public void setWorkTask_type(Integer workTask_type) {
		this.workTask_type = workTask_type;
	}
	public String getWorkTask_guest_name() {
		return workTask_guest_name;
	}
	public void setWorkTask_guest_name(String workTask_guest_name) {
		this.workTask_guest_name = workTask_guest_name;
	}
	public String getWorkTask_engineer_id() {
		return workTask_engineer_id;
	}
	public void setWorkTask_engineer_id(String workTask_engineer_id) {
		this.workTask_engineer_id = workTask_engineer_id;
	}
	public String getWorkTask_sponsor_name() {
		return workTask_sponsor_name;
	}
	public void setWorkTask_sponsor_name(String workTask_sponsor_name) {
		this.workTask_sponsor_name = workTask_sponsor_name;
	}
	public String getWorkTask_engineer_remark() {
		return workTask_engineer_remark;
	}
	public void setWorkTask_engineer_remark(String workTask_engineer_remark) {
		this.workTask_engineer_remark = workTask_engineer_remark;
	}
	
	
}
