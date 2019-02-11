/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mateus
 */
package br.com.devdojo;

import br.com.devdojo.awesome.model.Student;
import br.com.devdojo.awesome.repository.StudentRepository;
import static java.util.Arrays.asList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
public class StudentEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @MockBean
    private StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config {

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().basicAuthorization("Admir", "devdojo");
        }
    }

    @Before
    public void setup() {
        Student student = new Student(1L, "Andre Almenara", "andrealmenara@gmail.com");
        BDDMockito.when(studentRepository.findOne(student.getId())).thenReturn(student);
    }

    @Test
    public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnsStatusCode401() {
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void getStudentsByIdWhenUsernameAndPasswordAreIncorrectShouldReturnsStatusCode401() {
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/1", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnsStatusCode200() {
        List<Student> students = asList(new Student(1L, "Andre Almenara", "andrealmenara@gmail.com"),
                new Student(2L, "Paulo Guina", "paulo@guina.com"));
        BDDMockito.when(studentRepository.findAll()).thenReturn(students);
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentsByIdWhenUsernameAndPasswordAreCorrectShouldReturnsStatusCode200() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, 1L);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentsByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnsStatusCode404() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, -1L);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {

        BDDMockito.doNothing().when(studentRepository).delete(1L);
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @WithMockUser(username= "xx", password="xx", roles={"USER","ADMIN"})
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() throws Exception {
        BDDMockito.doNothing().when(studentRepository).delete(1L);
        //ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}",HttpMethod.DELETE, null, String.class, -1L);
        //Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);       
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}", -1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
        @Test
    @WithMockUser(username= "xx", password="xx", roles={"USER"})
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {
        BDDMockito.doNothing().when(studentRepository).delete(1L);
        //ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}",HttpMethod.DELETE, null, String.class, -1L);
        //Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);       
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}", -1L))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() throws Exception{
        Student student = new Student(3L, null, "chespirito@gmail.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/admin/students/",student,String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("fieldMessage","O campo nome do estudante é obrigatório!");
    }
        @Test
    public void createShouldPersistDataAndReturnStatusCode201() throws Exception{
        Student student = new Student(3L, "El Chavo del 8", "chespirito@gmail.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<Student> response = restTemplate.postForEntity("/v1/admin/students/",student,Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }
}
