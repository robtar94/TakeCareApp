package project.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import project.DTO.PersonalDataDTO;
import project.DTO.PersonalProfileDTO;
import project.DTO.UserDTO;
import project.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    public UserServiceImpl userServiceImpl;

    public PersonalProfileDTO generatePersonalProfileDTO(){
        UserDTO userDTO = new UserDTO();
        PersonalDataDTO personalDataDTO = new PersonalDataDTO();

        personalDataDTO.setName("   ");
        personalDataDTO.setSurname("   ");
        personalDataDTO.setEmail("   ");
        return new PersonalProfileDTO(personalDataDTO,userDTO);
    }

    @Test
    public void checkIfCorrectTest(){
        PersonalProfileDTO personalProfileDTO = generatePersonalProfileDTO();
        UserServiceImpl userService = new UserServiceImpl();
        boolean response = userService.checkIfCorrect(personalProfileDTO,false);

        Assert.assertTrue(response==false);
    }
}
