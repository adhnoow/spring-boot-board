package com.example.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "table_user")
public class User {
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true) // username(id) 중복 방지
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true) // 같은 email로 가입 방지
	private String email;

	@Column(nullable = false, unique = true) // 아이디 대신 닉네임으로 활동할 거기 때문에 중복 방지
	private String nickname;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role; // ROLE_ADMIN, ROLE_MANAGER, ROLE_USER

	@Column(nullable = false)
	@CreationTimestamp // User가 회원가입(save) 한 시간을 자동 저장
	private Date createDate;

	public User(long id, String username, String password, String email, Role role, Date createDate) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.createDate = createDate;
	}
}
