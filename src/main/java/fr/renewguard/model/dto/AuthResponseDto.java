package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponseDto {

@JsonProperty("access_token") private String accessToken;

@JsonProperty("token_type") private String tokenType;

@JsonProperty("expires_in") private int expiresIn;

@JsonProperty("username") private String username;

@JsonProperty("site_name") private String siteName;

@JsonProperty("role") private String role;

public String getAccessToken() { return accessToken; }

public String getTokenType() { return tokenType; }

public int getExpiresIn() { return expiresIn; }

public String getUsername() { return username; }

public String getSiteName() { return siteName; }

public String getRole() { return role; }

}
