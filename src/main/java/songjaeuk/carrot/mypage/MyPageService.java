package songjaeuk.carrot.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import songjaeuk.carrot.user.User;
import songjaeuk.carrot.user.UserDto;
import songjaeuk.carrot.user.UserRepository;
@Service
public class MyPageService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //회원정보 수정
    @Transactional
    public void updateUser(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다. id="+userDto.getId()));
        user.update(userDto.getName(),userDto.getEmail(),userDto.getPhone(),userDto.getRole());
    }
    //비밀번호 변경
    @Transactional
    public void changePassword(String username,String newPassword){
        User user = userRepository.findById(username)
                .orElseThrow(()->new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id="+username));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    //회원정보 삭제
    @Transactional
    public void deleteUser(String username){
        User user = userRepository.findById(username)
                .orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다. id="+username));
        userRepository.delete(user);
    }
    //관심상품
    //구매물품몰록
    //판매 물품목록

}
