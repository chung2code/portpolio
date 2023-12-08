package songjaeuk.carrot.config.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import songjaeuk.carrot.config.auth.provider.GoogleUserInfo;
import songjaeuk.carrot.config.auth.provider.KakaoUserInfo;
import songjaeuk.carrot.config.auth.provider.NaverUserInfo;
import songjaeuk.carrot.config.auth.provider.OAuth2UserInfo;
import songjaeuk.carrot.user.User;
import songjaeuk.carrot.user.UserDto;
import songjaeuk.carrot.user.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalDetailsOAuth2Service extends DefaultOAuth2UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    public PrincipalDetailsOAuth2Service(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2User oauth2User =   super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        System.out.println("auth2User.getAttributes() : " +oauth2User.getAttributes());
        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao"))
        {
            System.out.println("[] 카카오 로그인");
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo((Map<String, Object>) oauth2User.getAttributes().get("properties"));
            kakaoUserInfo.setId(userRequest.getClientRegistration().getClientId());
            oAuth2UserInfo = kakaoUserInfo;
            //oAuth2UserInfo = new KakaoUserInfo((Map<String, Object>) oauth2User.getAttributes().get("properties"));
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("google"))
        {
            System.out.println("[] 구글 로그인");
            oAuth2UserInfo  = new GoogleUserInfo(oauth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver"))
        {
            System.out.println("[] 네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
        }

        //OAuth2UserInfo 확인
        String provider  = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String Id = provider+"_"+providerId+"@example.com";    //  /
        String password = passwordEncoder.encode("1234");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        //DB 저장
        Optional<User> optional =   userRepository.findById(Id);
        if(optional.isEmpty()) {
            User user = User.builder()
                    .Id(Id)
                    .password(password)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
            System.out.println("[OAUTH] "+provider +" 최초 로그인 요청!");
        }else{
            System.out.println("[OAUTH] 기존 계정"+optional.get().getId() +"으로 로그인!");
        }



        //AccessToken 정보를 Authentication에 저장하기
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setAttributes(oauth2User.getAttributes());
        UserDto dto = new UserDto();
        dto.setId(Id);
        dto.setPassword(password);
        dto.setRole(role);
        //OAUTH2 LOGOUT
        dto.setProvider(provider);
        dto.setProviderId(providerId);
        principalDetails.setUser(dto);
        principalDetails.setAccessToken(userRequest.getAccessToken().getTokenValue());

        return principalDetails;

    }





    @Override
    public UserDetails loadUserByUsername(String Id) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findById(Id);

        if(user.isEmpty())
            return null;

        UserDto dto = new UserDto();
        dto.setId(user.get().getId());
        dto.setPassword(user.get().getPassword());
        dto.setRole(user.get().getRole());
        dto.setPhone(user.get().getPhone());
        dto.setNickname(user.get().getNickname());


        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setUser(dto);

        return principalDetails;
    }
}