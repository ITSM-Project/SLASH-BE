// package project.slash.security.auth;
//
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import project.slash.user.model.User;
//
// import java.util.Collection;
// import java.util.Collections;
//
// public class CustomUserDetails implements UserDetails {
//
// 	private final User user;
//
// 	public CustomUserDetails(User user) {
// 		this.user = user;
// 	}
//
// 	@Override
// 	public Collection<? extends GrantedAuthority> getAuthorities() {
// 		// 사용자의 역할을 SimpleGrantedAuthority로 변환
// 		return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
// 	}
//
// 	@Override
// 	public String getPassword() {
// 		return user.getPassword();
// 	}
//
// 	@Override
// 	public String getUsername() {
// 		return user.getId();  // user_id를 username으로 사용
// 	}
//
// 	@Override
// 	public boolean isAccountNonExpired() {
// 		return true;
// 	}
//
// 	@Override
// 	public boolean isAccountNonLocked() {
// 		return true;
// 	}
//
// 	@Override
// 	public boolean isCredentialsNonExpired() {
// 		return true;
// 	}
//
// 	@Override
// 	public boolean isEnabled() {
// 		return true;
// 	}
//
// 	public String getName() {
// 		return user.getName();
// 	}
//
// 	public String getEmail() {
// 		return user.getEmail();
// 	}
//
// 	public String getPhoneNum() {
// 		return user.getPhoneNum();
// 	}
// }
