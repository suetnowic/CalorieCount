package com.viktorsuetnov.caloriecounting.controller;

import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.payload.request.LoginRequest;
import com.viktorsuetnov.caloriecounting.payload.request.SignupRequest;
import com.viktorsuetnov.caloriecounting.payload.response.JwtTokenSuccessResponse;
import com.viktorsuetnov.caloriecounting.payload.response.MessageResponse;
import com.viktorsuetnov.caloriecounting.security.jwt.JwtTokenProvider;
import com.viktorsuetnov.caloriecounting.service.UserService;
import com.viktorsuetnov.caloriecounting.validations.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.viktorsuetnov.caloriecounting.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    private final ResponseErrorValidator responseErrorValidator;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(ResponseErrorValidator responseErrorValidator, UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.responseErrorValidator = responseErrorValidator;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser (@Valid @RequestBody SignupRequest request,
                                                BindingResult bindingResult) {
        final ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        userService.createUser(request);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest request,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.getUserByEmail(request.getUsername());

        if (user.isEnabled()) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtTokenSuccessResponse(true, jwt));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account not activated");
    }
}
