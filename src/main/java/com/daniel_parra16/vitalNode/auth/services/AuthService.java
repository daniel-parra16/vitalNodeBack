package com.daniel_parra16.vitalNode.auth.services;

import java.util.Map;

import com.daniel_parra16.vitalNode.auth.dtos.LoginRequest;
import com.daniel_parra16.vitalNode.auth.dtos.LoginResponse;
import com.daniel_parra16.vitalNode.auth.dtos.RefreshTokenRequest;
import com.daniel_parra16.vitalNode.auth.dtos.RegistroRequest;

public interface AuthService {

    public Map<String, String> registro(RegistroRequest request);

    public LoginResponse login(LoginRequest request);

    public LoginResponse renovarToken(RefreshTokenRequest request);

    public void logout(RefreshTokenRequest request);
}
