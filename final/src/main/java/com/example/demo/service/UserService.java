package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.User;

public interface UserService {
	void saveUser(User user);

	User findUserById(Long id);

	void deleteUserById(Long id);

	// 관리자가 유저를 관리할 때 필요함
	Page<User> findUserList(Pageable pageble);

	List<User> findAllUserList();

}
