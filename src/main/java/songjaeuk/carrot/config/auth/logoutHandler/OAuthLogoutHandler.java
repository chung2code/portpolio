package songjaeuk.carrot.config.auth.logoutHandler;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;
import songjaeuk.carrot.config.auth.PrincipalDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OAuthLogoutHandler implements LogoutHandler {


    private String naverClientId ="myCONIrrsJHFIPcgl9OQ";


    private String naverClientSecret="ngJQs03WkY";


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        System.out.println("authentication " + authentication);
        System.out.println("authentication.getPrincipal() " + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("provider : " + principalDetails.getUser().getProvider());

        String provider = principalDetails.getUser().getProvider();
        String accessToken = ((PrincipalDetails) authentication.getPrincipal()).getAccessToken();

        if(provider!=null&& StringUtils.contains(provider,"kakao"))
        {
            System.out.println("GET /th/kakao/logout");

            //URL
            String url = "https://kapi.kakao.com/v1/user/logout";
            //Header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            headers.add("Authorization", "Bearer "+accessToken);

            //Parameter
            //header + parameter
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

            //Request_Case1
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> rs =  rt.exchange(url, HttpMethod.POST,entity,String.class);
            System.out.println(rs);
            System.out.println(rs.getBody());

        }
        else if(provider!=null&&StringUtils.contains(provider,"google"))
        {
            String url ="https://accounts.google.com/o/oauth2/revoke?token="+accessToken;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url,HttpMethod.GET,null,String.class);

            HttpSession session = request.getSession(false);

            if(session!=null)
                session.invalidate();

        }
        else if(provider!=null&&StringUtils.contains(provider,"naver"))
        {


            System.out.println("[LOGOUT_HANDLER] naverClientId : " + naverClientId);
            System.out.println("[LOGOUT_HANDLER] naverClientSecret : " + naverClientSecret);

            System.out.println("[LOGOUT_HANDLER] authentication  : "  + authentication);
            System.out.println("[LOGOUT_HANDLER] authentication getPrincipal()  : "  + (PrincipalDetails)authentication.getPrincipal());
            System.out.println("[LOGOUT_HANDLER] getAccessToken()  : "  + ((PrincipalDetails) authentication.getPrincipal()).getAccessToken());



            // 네이버 API 로그아웃을 위한 URL 생성
            String logoutUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id="
                    + naverClientId + "&client_secret=" + naverClientSecret + "&access_token=" + accessToken+"&service_provider=NAVER";



            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(logoutUrl, HttpMethod.GET, entity, String.class);

            System.out.println("NAVER LOGOUT Success!");
            //기존 세션 제거
            HttpSession session = request.getSession(false);
            if(session!=null)
                session.invalidate();


        }


    }
}
