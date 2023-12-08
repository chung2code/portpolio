package songjaeuk.carrot.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import songjaeuk.carrot.common.TimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User  {

    @Id
    private String Id;
    private String password;
    private String name;
    private String phone;
    private String nickname;

    private String role;
    private String email;


    public void update(String name,String email,String nickname,String phone){
        this.name = name;
       this.email = email;
       this.nickname = nickname;
       this.phone = phone;
    }


    // OAuth2 Added
    private String provider;
    private String providerId;

    //profile이미지추가
    private String profileimage;
}
