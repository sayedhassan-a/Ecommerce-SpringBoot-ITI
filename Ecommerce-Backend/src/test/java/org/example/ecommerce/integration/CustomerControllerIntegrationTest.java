package org.example.ecommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.system.StatusCode;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration Test for Customer API Endpoints")
@Tag("integration")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce2",
        "spring.datasource.username=root",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver"
})
public class CustomerControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    private CustomerRepository customerRepository;

    String token;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(post("/customers/login")
                .with(httpBasic("remembrall.longbottom@example.com", "securePassword123")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content);
        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token"); // Don't forget to add "Bearer " as prefix.
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllCustomersSuccess() throws Exception {
        this.mockMvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Customers retrieved successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)));
    }

    // This is NOT mocking the repository, but actually using the real repository to save the customer
    @Test
    @DisplayName("Check Customer with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testCustomerSuccess() throws Exception {
        Customer a = new Customer();
        // Set everything in customer object with dummy data
        a.setFirstName("Remembrall2");
        a.setId(2L);
        a.setLastName("Longbottom");
        a.setPhone("1234567890");
        a.setDateOfBirth(LocalDate.of(1990, 1, 1));
        a.setEmail("remembrall2.longbottom@example.com");
        a.setMiddleName("Neville");
        a.setPassword("securePassword123");
//        customerRepository.save(a);

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Customer created successfully"))
                .andExpect(jsonPath("$.data.firstName").value("Remembrall2"));

        this.mockMvc.perform(get("/customers").header(HttpHeaders.AUTHORIZATION, this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Customers retrieved successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)));
    }
}
