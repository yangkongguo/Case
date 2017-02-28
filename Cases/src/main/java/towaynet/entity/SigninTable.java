package towaynet.entity;

import java.sql.Timestamp;

public class SigninTable {
	private String signin_id ;
    private String signin_map;
    private Timestamp signin_start_time ;
    private Timestamp signin_finsh_time;
    private Integer signin_status ;
    private String signin_engineer_id ;
    private String workTask_id;
    private String signin_in_image_path;
    private String signin_out_image_path;
	public String getSignin_id() {
		return signin_id;
	}
	public void setSignin_id(String signin_id) {
		this.signin_id = signin_id;
	}
	public String getSignin_map() {
		return signin_map;
	}
	public void setSignin_map(String signin_map) {
		this.signin_map = signin_map;
	}
	
	public Timestamp getSignin_start_time() {
		return signin_start_time;
	}
	public void setSignin_start_time(Timestamp signin_start_time) {
		this.signin_start_time = signin_start_time;
	}
	public Timestamp getSignin_finsh_time() {
		return signin_finsh_time;
	}
	public void setSignin_finsh_time(Timestamp signin_finsh_time) {
		this.signin_finsh_time = signin_finsh_time;
	}
	public Integer getSignin_status() {
		return signin_status;
	}
	public void setSignin_status(Integer signin_status) {
		this.signin_status = signin_status;
	}
	public String getSignin_engineer_id() {
		return signin_engineer_id;
	}
	public void setSignin_engineer_id(String signin_engineer_id) {
		this.signin_engineer_id = signin_engineer_id;
	}
	public String getWorkTask_id() {
		return workTask_id;
	}
	public void setWorkTask_id(String workTask_id) {
		this.workTask_id = workTask_id;
	}
	public String getSignin_in_image_path() {
		return signin_in_image_path;
	}
	public void setSignin_in_image_path(String signin_in_image_path) {
		this.signin_in_image_path = signin_in_image_path;
	}
	public String getSignin_out_image_path() {
		return signin_out_image_path;
	}
	public void setSignin_out_image_path(String signin_out_image_path) {
		this.signin_out_image_path = signin_out_image_path;
	}
    
}
