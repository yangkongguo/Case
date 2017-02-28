package towaynet.entity;

public class Department{
	private boolean createDeptGroup; 
	private String name; 
	private int id; 
	private boolean autoAddUser;
	public boolean isCreateDeptGroup() {
		return createDeptGroup;
	}
	public void setCreateDeptGroup(boolean createDeptGroup) {
		this.createDeptGroup = createDeptGroup;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isAutoAddUser() {
		return autoAddUser;
	}
	public void setAutoAddUser(boolean autoAddUser) {
		this.autoAddUser = autoAddUser;
	}
	@Override
	public String toString() {
		return "Department [createDeptGroup=" + createDeptGroup + ", name=" + name + ", id=" + id + ", autoAddUser="
				+ autoAddUser + "]";
	}
	
}