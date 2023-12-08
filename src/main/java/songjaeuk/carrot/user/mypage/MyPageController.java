package songjaeuk.carrot.user.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import songjaeuk.carrot.post.PostService;
import songjaeuk.carrot.user.UserDto;
import songjaeuk.carrot.user.UserService;

@Controller
public class MyPageController {

    @Autowired
    private UserService userService;
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private PostService postService;

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
