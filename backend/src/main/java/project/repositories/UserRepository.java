package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public User findById(int id);
    public Optional<User> findByLogin(String login);
    public Optional<User> findByLoginAndPassword(String login,String password);

//    public User findOneByName(String username);
}
