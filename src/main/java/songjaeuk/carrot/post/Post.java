package songjaeuk.carrot.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import songjaeuk.carrot.common.TimeEntity;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String title;
    private String details;
    private String place;
    private String price;

    private Long count;

    @ElementCollection
    private List<String> files;

}
