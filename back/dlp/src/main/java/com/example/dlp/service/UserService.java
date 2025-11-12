package com.example.dlp.service;

import com.example.dlp.model.Role;
import com.example.dlp.model.User;
import com.example.dlp.model.UserInfoDTO;
import com.example.dlp.model.emailDTO.VerificationData;
import com.example.dlp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repo;

    private final Map<String, VerificationData> verificationMap = new ConcurrentHashMap<>();
    private final EmailService emailService;
    public UserService(PasswordEncoder passwordEncoder, UserRepository repo, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
        this.emailService = emailService;
    }

    public List<User> findAll() { return repo.findAll(); }
    public User findById(Long id) { return repo.findById(id).orElseThrow(); }
    public User save(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repo.save(u);
    }
    public User update(Long id, User u) { u.setId(id); return repo.save(u); }
    public void delete(Long id) { repo.deleteById(id); }
    //поиск по логину
    public User findByLogin(String login) { return repo.findByLogin(login).orElseThrow(); }
    public Optional<UserInfoDTO> getUserInfoIfCredentialsValid(String login, String password) {
        Optional<User> user = repo.findByLogin(login);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            Role role = user.get().getRole();
            return Optional.of(new UserInfoDTO(
                    user.get().getFullName(),
                    role.getName(),
                    role.getId()));
        }
        return Optional.empty();
    }
    //запрос кода на email
    public void sendVerificationCode(String login, String newPassword) {
        User user = repo.findByLogin(login).orElseThrow();
        String code = String.format("%06d", new Random().nextInt(999999));
        long expiration = System.currentTimeMillis() + 5 * 60 * 1000; // 5 минут
        verificationMap.put(login, new VerificationData(code, passwordEncoder.encode(newPassword), expiration));
        emailService.sendCode(login, code); // login = email
    }

    //запрос изменения пароля
    public boolean confirmPasswordChange(String login, String code) {
        VerificationData data = verificationMap.get(login);
        if (data == null || System.currentTimeMillis() > data.getExpiresAt()) return false;
        if (!data.getCode().equals(code)) return false;
        User user = repo.findByLogin(login).orElseThrow();
        user.setPassword(data.getEncodedPassword());
        repo.save(user);
        verificationMap.remove(login);
        return true;
    }

}