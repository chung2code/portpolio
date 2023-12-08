package songjaeuk.carrot.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User joinMember(UserDto dto, Model model) {
        // 패스워드 일치 여부 확인
        if (!dto.getPassword().equals(dto.getPasswordcheck())) {
            model.addAttribute("repassword", "패스워드가 일치하지 않습니다");
            return null;
        }

        dto.setRole("ROLE_USER");
        dto.setProfileimage("/images/basic.webp");
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = UserDto.dtoToEntity(dto);

        // DB에 사용자 정보 저장 후 결과 검증
        User savedUser = userRepository.save(user);

        if (savedUser == null || savedUser.getId() == null) {
            // TODO: 로그 출력 또는 예외 처리 등 필요한 작업 수행
            return null;
        }

        return savedUser;  // 성공적으로 저장된 사용자 정보 반환
    }
// 유저정보 수정
    @Transactional
    public void updateUser(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다. id="+userDto.getId()));
        user.update(userDto.getName(),userDto.getEmail(),userDto.getPhone(),userDto.getRole());
    }
//  유정정보 삭제
    @Transactional
    public void deleteUser(String username){
        User user = userRepository.findById(username)
                .orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다. id="+username));
        userRepository.delete(user);
    }


    //프로필업데이트
    public void updateProfile(UserDto dto){
        User user = UserDto.dtoToEntity(dto);

        userRepository.save(user);
    }
}
