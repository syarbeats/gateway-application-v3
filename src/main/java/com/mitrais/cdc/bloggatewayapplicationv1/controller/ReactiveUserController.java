package com.mitrais.cdc.bloggatewayapplicationv1.controller;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.NewPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResetPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResponseWrapper;
import com.mitrais.cdc.bloggatewayapplicationv1.services.ReactiveUserServices;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.EmailUtility;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.Utility;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReactiveUserController extends CrossOriginController{

    private ReactiveUserServices reactiveUserServices;
    private EmailUtility emailUtility;

    public ReactiveUserController(ReactiveUserServices reactiveUserServices, EmailUtility emailUtility) {
        this.reactiveUserServices = reactiveUserServices;
        this.emailUtility = emailUtility;
    }

    @PostMapping("/register-reactive")
    public Single<ResponseEntity<ResponseWrapper>> userRegister(@RequestBody User user){

        return reactiveUserServices.userRegistration(user)
                .subscribeOn(Schedulers.io())
                .map(userdata -> ResponseEntity.ok(new Utility("Check your email to activate your account", userdata).getResponseData()));
    }

    @PatchMapping("/update/user-reactive")
    public Single<ResponseEntity<ResponseWrapper>> updateUserData(@RequestBody User user){
        return reactiveUserServices.updateUserData(user)
                .subscribeOn(Schedulers.io())
                .map(updatedata -> ResponseEntity.ok(new Utility("Update User Data", updatedata).getResponseData()));
    }

    @DeleteMapping("/delete/user-reactive/{username}")
    public Single<ResponseEntity<ResponseWrapper>> deleteUserDataByUsername(@PathVariable("username") String username){
        return reactiveUserServices.deleteUserByUsername(username)
                .subscribeOn(Schedulers.io())
                .map(deletedata -> ResponseEntity.ok(new Utility("Delete User Data", deletedata).getResponseData()));
    }

    @GetMapping("/find/user-reactive/{id}")
    public Single<ResponseEntity<ResponseWrapper>> findUserDataById(@PathVariable("id") int id){
        return reactiveUserServices.findUserById(id)
                .subscribeOn(Schedulers.io())
                .map(finddata -> ResponseEntity.ok(new Utility("Find User Data", finddata).getResponseData()));
    }

    @GetMapping("/find-user-by-username-reactive/{username}")
    public Single<ResponseEntity<ResponseWrapper>> findUserByUsername(@PathVariable("username") String username){
        return reactiveUserServices.findUserByUsername(username)
                .subscribeOn(Schedulers.io())
                .map(findUser -> ResponseEntity.ok(new Utility("Find User Data By Username", findUser).getResponseData()));
    }

    @GetMapping("/all-users-reactive")
    public Single<ResponseEntity<ResponseWrapper>> getAllUsers(){
        return reactiveUserServices.getAllUsers()
                .subscribeOn(Schedulers.io())
                .map(getuser -> ResponseEntity.ok(new Utility("Find User Data", getuser).getResponseData()));
    }

    @PostMapping("/resetpassword-reactive")
    public Single<ResponseEntity<ResponseWrapper>> resetPasswordRequest(@RequestBody ResetPasswordPayload request){
        return reactiveUserServices.resetPasswordRequest(request)
                .subscribeOn(Schedulers.io())
                .map(reset -> ResponseEntity.ok(new Utility("Check your email to reset your password", reset).getResponseData()));


    }

    @PostMapping("/reset-reactive")
    public Single<ResponseEntity<ResponseWrapper>> resetPassword(@RequestBody NewPasswordPayload password){
        return reactiveUserServices.resetPassword(new String(Base64.decodeBase64(password.getId().getBytes())), password.getPassword())
                .subscribeOn(Schedulers.io())
                .map(reset -> ResponseEntity.ok(new Utility("Change password process have done successfully", reset).getResponseData()));
    }

    @GetMapping("/activate-reactive")
    public Single<ResponseEntity<ResponseWrapper>> activateUser(@RequestParam("id") String id){
        byte[] decodedUsername = Base64.decodeBase64(id.getBytes());
        String username = new String(decodedUsername);
        return reactiveUserServices.activateUser(username)
                .subscribeOn(Schedulers.io())
                .map(activate ->  ResponseEntity.ok(new Utility("Your account has been activated", username).getResponseData()));

    }
}
