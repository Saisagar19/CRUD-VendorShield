// Under com.vendorshield.dto.response.AuthResponse.java
package com.vendorshield.dto.response;

public record AuthResponse(
    String accessToken,
    String tokenType,
    String username,
    String role
) {}