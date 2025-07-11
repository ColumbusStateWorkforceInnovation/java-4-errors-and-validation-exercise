package edu.cscc.jpaexercise.jpaexercise.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.jpaexercise.jpaexercise.models.User;
import edu.cscc.jpaexercise.jpaexercise.models.UserAddress;
import edu.cscc.jpaexercise.jpaexercise.repositories.UserAddressesRepository;
import edu.cscc.jpaexercise.jpaexercise.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.test.properties"
)
class UserAddressesControllerTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserAddressesRepository userAddressesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        userAddressesRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("It can retrieve a user by address")
    public void itCanRetrieveAUserByAddress() throws Exception {
        User user = usersRepository.save(new User("Jim", "Kirkbride"));
        UserAddress userAddress = new UserAddress(user, "123 Main St", "Columbus", "OH", "43215");
        user.setUserAddresses(List.of(userAddress));
        userAddressesRepository.save(userAddress);

        mockMvc.perform(MockMvcRequestBuilders.get("/user-addresses/{id}/user", userAddress.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(user)));
    }
}