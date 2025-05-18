package com.example.soundcloudbe.service;

import com.example.soundcloudbe.entity.User;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.model.dto.AuthenticationResponse;
import com.example.soundcloudbe.model.dto.IntrospectResponse;
import com.example.soundcloudbe.model.dto.VerifyOTPResponse;
import com.example.soundcloudbe.model.request.*;
import com.example.soundcloudbe.repository.UserRepository;
import com.example.soundcloudbe.validator.EmailValidator;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    private long REFRESH_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!authenticated) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }
        String token = generateToken(user);
        return AuthenticationResponse.builder().token(token)
                .authenticated(true)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getRefreshToken(), true);
        User user = userRepository.findByEmail(signedJWT.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        boolean valid = true;
        try {
            verifyToken(request.getToken(), false);
        } catch (AppException e) {
            valid = false;
        }
        return IntrospectResponse.builder().valid(valid).build();
    }

    public void register(CreateUserRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new AppException(ErrorCode.RESOURCE_EXISTED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User newUser = new User();
        BeanUtils.copyProperties(request, newUser);
        newUser.setCreatedAt(new Date());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(newUser);
    }

    public void resetPassword(ResetPasswordRequest request){
        if (!request.getNewPassword().equalsIgnoreCase(request.getConfirmPassword())){
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_INVALID);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordRequest request) throws MessagingException {
        if (!EmailValidator.isValidEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_INVALID);
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        String otp = generateOTP();
        user.setVerificationCode(otp);
        userRepository.save(user);
        sendOTPEmail(user.getEmail(), user.getVerificationCode());
    }

    public VerifyOTPResponse verifyOTP(VerifyOTPRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        if (!user.getVerificationCode().equals(request.getOtp())){
            throw new AppException(ErrorCode.OTP_INVALID);
        }
        user.setVerificationCode(null);
        userRepository.save(user);

        String token = generateToken(user);

        return VerifyOTPResponse.builder()
                .email(user.getEmail())
                .token(token)
                .build();
    }

    private void sendOTPEmail(String to, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Password Reset OTP");

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Your OTP is:</p>"
                + "<p><b>" + otp + "</b></p>"
                + "<p>Please use this OTP to reset your password. It will expire in 5 minutes.</p>";

        helper.setText(content, true);

        mailSender.send(message);
    }

    private String generateOTP() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            str.append(secureRandom.nextInt(10));
        }
        return str.toString();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date((Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli())))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }


}
