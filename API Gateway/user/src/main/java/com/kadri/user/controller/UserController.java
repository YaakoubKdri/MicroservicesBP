package com.kadri.user.controller;

import com.kadri.user.dto.UserRequest;
import com.kadri.user.dto.UserResponse;
import com.kadri.user.mapper.UserMapper;
import com.kadri.user.model.User;
import com.kadri.user.service.UserService;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    public UserResponse create(@RequestBody UserRequest request){
        return mapper.toResponse(service.create(mapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id){
        return mapper.toResponse(service.getById(id));
    }

    @GetMapping
    public List<UserResponse> getAll(){
        return service.getAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody UserRequest request){
        return mapper.toResponse(service.update(id, mapper.toEntity(request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
