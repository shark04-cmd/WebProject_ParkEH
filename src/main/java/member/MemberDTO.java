package member;

import java.sql.Date;

public class MemberDTO {
	private String id;
	private String pass;
	private String newPass; // 비밀번호 수정 시 사용할 임시 필드
	private String name;
	private Date regidate;

	// 기본 생성자
	public MemberDTO() {
	}

	// Getter 및 Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegidate() {
		return regidate;
	}

	public void setRegidate(Date regidate) {
		this.regidate = regidate;
	}
}