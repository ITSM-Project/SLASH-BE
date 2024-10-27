// package project.slash.security.auth;
//
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
//
// import project.slash.user.model.User;
// import project.slash.user.repository.UserRepository;
//
// @Service
// public class CustomUserDetailService implements UserDetailsService {
//
// 	private final UserRepository userRepository;
//
// 	public CustomUserDetailService(UserRepository userRepository) {
// 		this.userRepository = userRepository;
// 	}
//
// 	@Override
// 	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
// 		// 데이터베이스에서 사용자 조회
// 		User user = userRepository.findById(id)
// 			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
// 		return new CustomUserDetails(user);
// 	}
// }
