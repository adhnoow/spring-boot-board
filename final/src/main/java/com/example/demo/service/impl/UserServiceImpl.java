package com.example.demo.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public User findUserById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override // JPA-delete method(delete)
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public Page<User> findUserList(Pageable pageble) {
		pageble = PageRequest.of(pageble.getPageNumber() <= 0 ? 0 : pageble.getPageNumber() - 1, pageble.getPageSize(),
				pageble.getSort());
		return userRepository.findAll(pageble);
	}

	@Override
	public List<User> findAllUserList() {
		return userRepository.findAll();
	}

}
