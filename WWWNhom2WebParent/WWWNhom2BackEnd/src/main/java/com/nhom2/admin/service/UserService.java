package com.nhom2.admin.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nhom2.admin.exception.UserNotFoundException;
import com.nhom2.admin.repository.RoleRepository;
import com.nhom2.admin.repository.UserRepository;
import com.nhom2.common.entity.Role;
import com.nhom2.common.entity.User;

@Service
@Transactional
public class UserService {

	public static final int USER_PER_PAGE = 4;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	public PasswordEncoder passwordEncoder;

	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}
	
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}

	public Page<User> listByPage(int pageNum, String sortField, String sortDir , String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE, sort);
		
		if(keyword != null) {
			return userRepository.findAll(keyword, pageable);
		}
		return userRepository.findAll(pageable);
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		return userRepository.save(user);
	}
	
	public User updateAccount(User userInForm) {
		User userInDB = userRepository.findById(userInForm.getId()).get();
		if(userInDB.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		
		if(userInDB.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		
		userInDB.setFirstname(userInForm.getFirstname());
		userInDB.setLastname(userInForm.getLastname());
		
		return userRepository.save(userInDB);
	}

	private void encodePassword(User user) {
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);
		if (userByEmail == null)
			return true;

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}

		return true;
	}

	public User getUserById(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (Exception e) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
	}

	public void deleteUserById(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}

		userRepository.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id, boolean endabled) {
		userRepository.updateEnableStatus(id, endabled);
	}

	public String getEmailOfAuthenticatedUser(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		String customerEmail = null;

		if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken){
			customerEmail = request.getUserPrincipal().getName();
		}

		return customerEmail;
	}

	public long getCount() {
		return userRepository.count();
	}
}
