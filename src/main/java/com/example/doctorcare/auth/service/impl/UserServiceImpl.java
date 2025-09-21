// package com.example.doctorcare.auth.service.impl;

// import java.time.LocalDateTime;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// import org.springframework.security.crypto.password.PasswordEncoder;

// import com.example.doctorcare.application.exception.EmailException;
// import com.example.doctorcare.application.service.SessionService;
// import com.example.doctorcare.auth.exception.PasswordRegisterException;
// import com.example.doctorcare.auth.exception.UserNotFoundException;
// import com.example.doctorcare.auth.security.common.CommonService;
// import com.example.doctorcare.auth.security.jwt.JwtUtils;
// import com.example.doctorcare.auth.service.UserService;
// import com.example.doctorcare.domain.system.user.User;
// import com.example.doctorcare.infrastructure.common.utils.Const.ACTIVE;
// import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER;
// import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER_ERROR;
// import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER_NOT_FOUND;
// import com.example.doctorcare.model.dto.request.SignupRequest;
// import com.example.doctorcare.model.dto.request.UserUpdateRequest;
// import com.example.doctorcare.model.dto.response.UserDtoPatientResponse;
// import com.example.doctorcare.model.dto.response.UserDtoResponse;
// import com.example.doctorcare.model.entity.RoleEntity;
// import com.example.doctorcare.model.mapper.UserMapper;
// import com.example.doctorcare.repository.UserRepository;

// import jakarta.transaction.Transactional;
// import lombok.AccessLevel;
// import lombok.RequiredArgsConstructor;
// import lombok.experimental.FieldDefaults;

// @RequiredArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// public class UserServiceImpl implements UserService {

//     UserRepository userRepository;
//     PasswordEncoder encoder;
//     UserMapper userMapper;
//     CommonService commonService;

//     @Override
//     public void create(User entity) {
//         userRepository.save(entity);
//     }

//     /**
//      * Creates a new user and saves it to the repository.
//      *
//      * @param signUpRequest the signup request containing user details
//      * @param role          the role to be assigned to the user
//      * @return the created User
//      * @throws EmailException            if the email already exists
//      * @throws PasswordRegisterException if the password does not meet security
//      *                                   requirements
//      * 
//      *                                   Admin can create user with role Doctor so
//      *                                   this method must be return User
//      * 
//      */
//     @Override
//     @Transactional
//     public User createUser(SignupRequest signUpRequest, RoleEntity role) {

//         // Check if the email already exists
//         if (this.emailExist(signUpRequest.getEmail())) {
//             throw new EmailException(MESSENGER_ERROR.USER_EXIST);
//         }

//         if (commonService.passworRegisterdErrors(signUpRequest.getPassword()) != 0) {
//             throw new PasswordRegisterException(MESSENGER_ERROR.PASSOWRD_REGISTER_ERROR);
//         } // Validate the password

//         Set<RoleEntity> roles = new HashSet<>();
//         roles.add(role); // Assign roles to the user

//         // User user = User.builder()
//         //         // Create a new User using the builder pattern
//         //         .email(signUpRequest.getEmail())
//         //         .name(signUpRequest.getFullName())
//         //         .genderl(signUpRequest.getGender())
//         //         .password(encoder.encode(signUpRequest.getPassword()))
//         //         .phone(signUpRequest.getPhone())
//         //         .address(signUpRequest.getAddress())
//         //         .dateOfbirth(signUpRequest.getDateOfbirth())
//         //         .active(0)
//         //         .roles(roles)
//         //         .build();
//         // this.create(user);

//         // Save the user entity to the repository
//         // return user;
//     }

//     @Override
//     public UserDtoResponse getInfo(String email) {
//         User entity = this.findByEmail(email);
//         return userMapper.toDto(entity, MESSENGER.USER_INFO);
//     }

//     @Override
//     public UserDtoPatientResponse getUserInfo(String email) {
//         User entity = this.findByEmail(email);
//         return userMapper.toDtoInfo(entity);
//     }

//     @Override
//     public User findById(Integer id) {
//         Optional<User> resutl = userRepository.findById(id);
//         return resutl.orElseThrow(
//                 () -> new UserNotFoundException(MESSENGER_NOT_FOUND.USER_NOT_FOUND_ID + ": (" + id + ")."));
//     }

//     @Override
//     public User update(User entity) {
//         return userRepository.save(entity);
//     }

//     @Override
//     public void delete(User entity) {
//         userRepository.delete(entity);
//     }

//     @Override
//     public List<User> findAll() {
//         return userRepository.findAll();
//     }

//     @Override
//     public User findByEmail(String email) {
//         Optional<User> result = userRepository.findUserByEmail(email);
//         return result.orElseThrow(() -> new UserNotFoundException(MESSENGER_NOT_FOUND.USER_NOT_FOUND_EMAIL));
//     }

//     @Override
//     public boolean emailExist(String email) {
//         Optional<User> result = userRepository.findUserByEmail(email);
//         return result.isPresent();
//     }

//     private User mapForSaving(UserUpdateRequest userRequest) {
//         return User.builder()
//                 .email(userRequest.getEmail())
//                 .name(userRequest.getName())
//                 .address(userRequest.getAddress())
//                 .phone(userRequest.getPhone())
//                 .genderl(userRequest.getGender())
//                 .dateOfbirth(userRequest.getDateOfbirth())
//                 .updatedAt(LocalDateTime.now())
//                 .build();
//     }

//     @Override
//     public UserDtoResponse updateUser(String email, UserUpdateRequest request) {
//         User user = this.findByEmail(email);
//         user.setName(request.getName());
//         user.setAddress(request.getAddress());
//         user.setPhone(request.getPhone());
//         user.setGenderl(request.getGender());
//         user.setDateOfbirth(request.getDateOfbirth());
//         user.setUpdatedAt(LocalDateTime.now());
//         return userMapper.toDto(userRepository.saveAndFlush(user), MESSENGER.UPDATE_INFO);
//     }

//     @Override
//     public User update(User entity, UserUpdateRequest request) {
//         entity.setName(request.getName());
//         entity.setAddress(request.getAddress());
//         entity.setPhone(request.getPhone());
//         entity.setGenderl(request.getGender());
//         entity.setDateOfbirth(request.getDateOfbirth());
//         entity.setUpdatedAt(LocalDateTime.now());
//         return userRepository.saveAndFlush(entity);
//     }

//     @Override
//     public String activeAccount(User userLocker) {
//         String result = null;
//         if (userLocker.getActive() == ACTIVE.NONE) {
//             userLocker.setActive(ACTIVE.NONE);
//             result = MESSENGER.LOCKED_SUCCESS;
//         } else if (userLocker.getActive() == ACTIVE.ACCEPT) {
//             userLocker.setActive(ACTIVE.ACCEPT);
//             result = MESSENGER.UNLOCK_SUCCESS;
//         } else {
//             result = MESSENGER_ERROR.UNIDENTIFIED;
//         }
//         return result;
//     }
// }
