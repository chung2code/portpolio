package songjaeuk.carrot.mypage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import songjaeuk.carrot.config.auth.PrincipalDetails;
import songjaeuk.carrot.post.PostService;
import songjaeuk.carrot.user.UserDto;
import songjaeuk.carrot.user.UserService;

@Controller
@Slf4j
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private UserService userService;
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private PostService postService;

    @GetMapping("/home")
    public String mypage(Authentication authentication, Model model)
    {
        log.info("GET /user/mypage/mypageHome..");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserDto userDto = principalDetails.getUser();


        model.addAttribute("userDto",principalDetails.getUser());
        return "mypage/mypageHome";
    }

    //회원정보 수정
    @PutMapping("/users/{username}")
    public ResponseEntity updateUser(@PathVariable String username, @RequestBody UserDto userDto){
        userService.updateUser(userDto);
        return ResponseEntity.ok().build();
    }

    //비밀번호변경
    @PutMapping("/{username}/password")
    public ResponseEntity<Void>changePassword(@PathVariable String username,@RequestBody PasswordChangeRequest passwordChangeRequest){
        myPageService.changePassword(username,passwordChangeRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }
    //회원정보삭제
    @DeleteMapping("/users/{username}")
    public ResponseEntity deleteUser(@PathVariable String username){
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    //관심목록
    //구매물품
    //판매물품
}
