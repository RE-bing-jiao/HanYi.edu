package edu.HanYi.service;

import edu.HanYi.dto.request.UserCreateRequest;
import edu.HanYi.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Integer id);
    UserResponse updateUser(Integer id, UserCreateRequest request);
    void deleteUser(Integer id);
}