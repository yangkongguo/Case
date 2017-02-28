package towaynet.entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{
	private String u_uid;
	private String u_name;
	private String u_password;
	private String u_phone;
	//直系上级id
	private String l_uid;
	private String email;
	//部门id
	private String d_id;
	//
	private Integer d_lev;
	
	public Integer getD_lev() {
		return d_lev;
	}
	public void setD_lev(Integer d_lev) {
		this.d_lev = d_lev;
	}
	public String getU_uid() {
		return u_uid;
	}
	public void setU_uid(String u_uid) {
		this.u_uid = u_uid;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_password() {
		return u_password;
	}
	public void setU_password(String u_password) {
		this.u_password = u_password;
	}
	public String getU_phone() {
		return u_phone;
	}
	public void setU_phone(String u_phone) {
		this.u_phone = u_phone;
	}
	public String getL_uid() {
		return l_uid;
	}
	public void setL_uid(String l_uid) {
		this.l_uid = l_uid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getD_id() {
		return d_id;
	}
	public void setD_id(String d_id) {
		this.d_id = d_id;
	}
	
	
}
