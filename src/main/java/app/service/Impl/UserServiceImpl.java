package app.service.Impl;

import app.model.entity.UserEntity;
import app.model.entity.UserRoleEntity;
import app.model.entity.enums.UserRole;
import app.model.service.UserRegistrationServiceModel;
import app.repository.UserRepository;
import app.repository.UserRoleRepository;
import app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static app.constant.ConstantMessages.USER_ROLE_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final FlowerParadiseUserService flowerParadiseUserService;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRoleRepository userRoleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           FlowerParadiseUserService flowerParadiseUserService,
                           PasswordEncoder encoder) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.flowerParadiseUserService = flowerParadiseUserService;
        this.encoder = encoder;
    }


    @Override
    public void seedUsers() {
        if (userRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);

            userRoleRepository.saveAll(List.of(adminRole, userRole));

            UserEntity admin = new UserEntity().setUsername("admin").setFullname("Admin Adminov")
                    .setPassword(passwordEncoder.encode("topsecret"));
            UserEntity user = new UserEntity().setUsername("user").setFullname("Bai Ivan")
                    .setPassword(passwordEncoder.encode("topsecret"));
            admin.setRoles(List.of(adminRole, userRole));
            user.setRoles(List.of(userRole));

            userRepository.saveAll(List.of(admin, user));
        }
    }

    @Override
    public void registerAndLoginUser(UserRegistrationServiceModel serviceModel) {
        UserEntity newUser = modelMapper.map(serviceModel, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(serviceModel.getPassword()));

        UserRoleEntity userRole = userRoleRepository.findByRole(UserRole.USER)
                .orElseThrow(() -> new IllegalStateException(USER_ROLE_NOT_FOUND));

        newUser.addRole(userRole);

        newUser = userRepository.save(newUser);

        UserDetails principal = flowerParadiseUserService.loadUserByUsername(newUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal,
                newUser.getPassword(), principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public boolean userNameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity findByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);

    }
}
