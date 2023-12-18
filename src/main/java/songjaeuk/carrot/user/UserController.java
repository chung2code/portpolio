package songjaeuk.carrot.user;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import songjaeuk.carrot.config.auth.PrincipalDetails;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login_get() {
        log.info("GET /login");
        return "user/login";  // 로그인 폼을 제공하는 뷰 이름 반환
    }
    @GetMapping("/join")
    public void join_get() {
        log.info("GET /join");
    }

    @PostMapping("/join")
    public String join_post(@Valid UserDto dto, BindingResult bindingResult, Model model) {
        log.info("POST user/join " + dto);

        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "user/join";
        }

        // 회원 가입 처리
        User user = userService.joinMember(dto,model);

        // 회원 가입 실패 시
        if (user == null) {
            // TODO: 실패 메시지 설정 또는 추가적인 에러 핸들링 수행
            return "user/join";
        }

        log.info(String.valueOf(user));  // 로그에 저장된 사용자 정보 출력

      return "redirect:/user/login";
    }







    //----------------------------------------------------------------
    //메일인증
    //----------------------------------------------------------------
    @GetMapping(value="/auth/email/{id}")
    public @ResponseBody void email_auth(@PathVariable String username, HttpServletRequest request)
    {
        log.info("GET /user/auth/email.." + username);

        //메일설정
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("anthdny7@gamil.com");
        mailSender.setPassword("wmgj xbbl jtds nnaa");

        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        mailSender.setJavaMailProperties(props);

        //난수값생성
        String tmpPassword = (int)(Math.random()*10000000)+""; //

        //본문내용 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(username);
        message.setSubject("[WEB_TEST]이메일코드발송");
        message.setText(tmpPassword);

        //발송
        mailSender.send(message);

        //세션에 Code저장
        HttpSession session = request.getSession();
        session.setAttribute("email_auth_code",tmpPassword);

    }


    @GetMapping("/auth/confirm/{code}")
    public @ResponseBody String email_auth_confirm(@PathVariable String code, HttpServletRequest request)
    {
        System.out.println("GET /user/auth/confirm " + code);
        HttpSession session = request.getSession();
        String auth_code = (String)session.getAttribute("email_auth_code");
        if(auth_code!=null)
        {
            if(auth_code.equals(code)){
                session.setAttribute("is_email_auth",true);
                return "success";
            }else{
                session.setAttribute("is_email_auth",false);
                return "failure";
            }

        }

        return "failure";
    }



    @PostMapping("/passwordConfirm")
    public ResponseEntity<String> passowrdConfirm(@RequestBody JSONObject oldPassword, Authentication authentication){
        System.out.println("POST /user/passwordConfirm"+oldPassword.get("oldPassword"));
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
       UserDto userDto = principalDetails.getUser();

        Boolean isCorrect = passwordEncoder.matches(oldPassword.get("oldPassword").toString(),userDto.getPassword());

        if (isCorrect){
            System.out.println("패스워드 일치");
            return  new ResponseEntity("패스워드 확인 완료", HttpStatus.OK);
        }
        return new ResponseEntity("패스워드 불일치",HttpStatus.UNAUTHORIZED);
    }

// 아이디 중복검사
@PostMapping("/checkID")
@ResponseBody
public CheckIdResponse checkId(@RequestBody CheckIdRequest request) {
    boolean exists = userRepository.existsById(request.getId());
    return new CheckIdResponse(!exists);
}

    public static class CheckIdRequest {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class CheckIdResponse {
        private boolean success;

        public CheckIdResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }






    //프로필이미지
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping(value = "profileimage/upload")
    public @ResponseBody String profileImageUpload(MultipartFile[] file, Authentication authentication) throws IOException {
        log.info("POST /user/profileimage/upload file : " +file);

        //저장위치 /resources/static/images/계정명폴더/파일명
        //폴더 경로 확인
        Resource resource = resourceLoader.getResource("classpath:static/images/user");
        File getfiles = resource.getFile();
        String absolutePath = getfiles.getAbsolutePath();
        System.out.println("정적 자원 경로"+ absolutePath);

        //접속 유저명 받기

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserDto userDto = principalDetails.getUser();
        String username = userDto.getNickname();

        //저장 폴더 지정
        String uploadPath = absolutePath + File.separator + username;
        File dir = new File(uploadPath);
        if(!dir.exists()){
            dir.mkdirs();
        }else {
            //기존 파일제거
            File[] files = dir.listFiles();
            for(File rmfile : files){
                rmfile.delete();
            }
        }

        System.out.println("--------------------");
        System.out.println("FILE NAME : " + file[0].getOriginalFilename());
        System.out.println("FILE SIZE : " + file[0].getSize() + " Byte");
        System.out.println("--------------------");

        //파일명 추출
        String filename = file[0].getOriginalFilename();
        //파일객체생성
        File fileobj = new File(uploadPath,filename);
        //업로드
        file[0].transferTo(fileobj);


        //Authentication에 변경정보 넣기
        // http://localhost:8080/images/user/+username/+filename

        userDto.setProfileimage("http//localhost:8080/images/user"+username+"/"+filename);
        principalDetails.setUser(userDto);

        //DB에 넣기

        userService.updateProfile(userDto);

        return "ok";

    }
}
