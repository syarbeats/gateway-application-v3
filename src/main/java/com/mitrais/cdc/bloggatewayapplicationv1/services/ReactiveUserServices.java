package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ReactiveUserServices {

    private UserRepository userRepository;

    public ReactiveUserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Single<APIResponse> userRegistration(User user){
        return Single.create(userRegistration -> {
            user.setEnabled(false);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRegistration.onSuccess(new APIResponse(true, "User has been registered successfully", userRepository.save(user)));
        });

    }

    public Single<APIResponse> updateUserData(User user){
        return Single.create(updateUser -> {
            User datauser = userRepository.findByUsername(user.getUsername());
            if(!user.getPassword().isEmpty() && user.getPassword() != null){
                datauser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }

            if(!user.getEmail().isEmpty() && user.getEmail() != null){
                datauser.setEmail(user.getEmail());
            }

            datauser.setEnabled(user.isEnabled());
            updateUser.onSuccess(new APIResponse(true, "Update user data has been updated sucessfully", userRepository.save(datauser)));
        });
    }

    public Single<APIResponse> resetPassword(String username, String password){
        return Single.create(resetPassword -> {
            User userdata = userRepository.findByUsername(username);
            userdata.setPassword(new BCryptPasswordEncoder().encode(password));
            resetPassword.onSuccess(new APIResponse(true, "Update user password has been updated successfully", userRepository.save(userdata)));
        });
    }

    public Single<APIResponse> deleteUserByUsername(String username){
        return Single.create(deleteUserByUsername -> {
            User userdata = userRepository.findByUsername(username);
            userRepository.delete(userdata);
            deleteUserByUsername.onSuccess(new APIResponse(true, "Delete user data has been executed successfully", userdata));
        });
    }

    public Single<APIResponse> findUserById(int id) {
        return Single.create(findUserById -> {
            Optional<User> userOptional = userRepository.findById(id);
            if(userOptional.isPresent()){
                findUserById.onSuccess(new APIResponse(true, "User data was found", userOptional.get()));
            }
            else {
                findUserById.onSuccess(new APIResponse(false, "User data was not found", null));
            }
        });

    }

    public Single<APIResponse> findUserByUsername(String username){
        return Single.create(findUserByUsername -> {
            User user = userRepository.findByUsername(username);
            if(user != null){
                findUserByUsername.onSuccess(new APIResponse(true, "User data was found", user));
            }else {
                findUserByUsername.onSuccess(new APIResponse(false, "User data was not found", null));
            }
        });

    }

    public Single<User> findUserByEmail(String email){
        return Single.create(findUserByEmail -> {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if(userOptional.isPresent()){
                findUserByEmail.onSuccess(userOptional.get());
            }else {
                findUserByEmail.onError(new UsernameNotFoundException("User data was not found"));
            }
        });

    }

    public Single<APIResponse> getAllUsers(){
        return Single.create(getAllUsers -> {
            getAllUsers.onSuccess(new APIResponse(true, "User data was found", userRepository.findAll()));
        });

    }

    /**
     * This method will be used to activate
     * user, change enabled field to true.
     *
     * @param username
     * @return will return activated user data
     */
    public Single<APIResponse> activateUser(String username){
       return Single.create(activateUser -> {
           User user = userRepository.findByUsername(username);
           user.setEnabled(true);
           User userdata = userRepository.save(user);
           activateUser.onSuccess(new APIResponse(true, "User has been activated", userdata));
       });
    }

}

