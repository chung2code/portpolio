package songjaeuk.carrot.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

//    @Modifying
//    @Query("update Post p set p.count = p.count + 1 where p.id=:id")
//    int updateCount(Long id);


}
