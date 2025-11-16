package com.kadri.user.service;

import com.kadri.user.model.User;
import com.kadri.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User create(User user){
        user.setId(null);
        return repository.save(user);
    }

    public User getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found " + id));
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public User update(Long id, User updatedUser){
        User existingUser = getById(id);
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFullName(updatedUser.getFullName());

        return repository.save(existingUser);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public User getByUsername(String username){
        return repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }
}
