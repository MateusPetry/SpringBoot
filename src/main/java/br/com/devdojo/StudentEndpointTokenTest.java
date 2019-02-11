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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
public class StudentEndpointTokenTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @MockBean
    private StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc;
    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @Before
    public void configProtectedHeaders(){
        String str = "{\"username\":\"Lula\",\"password\":\"devdojo\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login",str,String.class).getHeaders();
        this.protectedHeader = new HttpEntity<>(headers);
    }
        @Before
    public void configAdminHeaders(){
        String str = "{\"username\":\"Lula\",\"password\":\"devdojo\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login",str,String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }
            @Before
    public void configWrongHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","11111");
        this.wrongHeader = new HttpEntity<>(headers);
    }
    
    @Before
    public void setup() {
        Student student = new Student(1L, "Andre Almenara", "andrealmenara@gmail.com");
        BDDMockito.when(studentRepository.findOne(student.getId())).thenReturn(student);
    }

    @Test
    public void listStudentsWhenTokenIsIncorrectShouldReturnsStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/",GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getStudentsByIdWhenTokenIsIncorrectShouldReturnsStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/1",GET, wrongHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listStudentsWhenTokenIsCorrectShouldReturnsStatusCode200() {
        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/1",GET,protectedHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentsByIdTokenIsCorrectShouldReturnsStatusCode200() {
        ResponseEntity<Student> response = restTemplate.exchange("/v1/protected/students/1",GET, protectedHeader, Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentsByIdWhenTokenIsCorrectAndStudentDoesNotExistShouldReturnsStatusCode404() {
        ResponseEntity<Student> response = restTemplate.exchange("/v1/protected/students/-1",GET, protectedHeader, Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

 @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {
        BDDMockito.doNothing().when(studentRepository).delete(1L);
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/1",DELETE,adminHeader, String.class);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    //@WithMockUser(username= "xx", password="xx", roles={"USER","ADMIN"})
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() throws Exception {
        String token = adminHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studentRepository).delete(1L);
        //ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}",HttpMethod.DELETE, null, String.class, -1L);
        //Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);       
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}", -1L).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {
        String token = protectedHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studentRepository).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}", 1L).header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() throws Exception{
        Student student = new Student(3L, null, "chespirito@gmail.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<String> response = restTemplate.exchange("/v1/admin/students/",POST, new HttpEntity<>(student,adminHeader.getHeaders()),String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("fieldMessage","O campo nome do estudante é obrigatório!");
    }
        @Test
    public void createShouldPersistDataAndReturnStatusCode201() throws Exception{
        Student student = new Student(3L, "El Chavo del 8", "chespirito@gmail.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<Student> response = restTemplate.exchange("/v1/admin/students/",POST, new HttpEntity<>(student,adminHeader.getHeaders()),Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }
}
