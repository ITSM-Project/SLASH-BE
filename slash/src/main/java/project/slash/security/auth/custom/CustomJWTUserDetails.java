package project.slash.security.auth.custom;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomJWTUserDetails implements UserDetails {

	private final String userId;

	public CustomJWTUserDetails(String userId, String password,
		Collection<? extends GrantedAuthority> authorities
	) {
		this.userId = userId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return "";
	}
}
