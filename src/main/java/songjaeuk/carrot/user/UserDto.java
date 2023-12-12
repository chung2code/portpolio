package songjaeuk.carrot.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {


    @NotBlank(message = "Id을 입력하세요")

    private String Id;


    @NotBlank(message = "password를 입력하세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @NotBlank(message = "password를 다시 입력하세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String passwordcheck;
    private String name;
    private String email;
    private String nickname;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    private String role;


    //OAUTH2
    private String provider;
    private String providerId;

    public static User dtoToEntity(UserDto dto){
        User user = User.builder()
                .Id(dto.getId())
                .password(dto.getPassword())
                .role(dto.getRole())
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
        return user;

    }




    //profile image add
    private String profileimage;
}
