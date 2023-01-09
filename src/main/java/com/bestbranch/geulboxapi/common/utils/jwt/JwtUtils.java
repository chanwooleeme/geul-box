package com.bestbranch.geulboxapi.common.utils.jwt;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface JwtUtils {
    Claims getClaimsBy(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;
}
