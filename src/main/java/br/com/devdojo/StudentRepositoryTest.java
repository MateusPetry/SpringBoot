/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devdojo;

import br.com.devdojo.awesome.model.Student;
import br.com.devdojo.awesome.repository.StudentRepository;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
/**
 *
 * @author mateus
 */

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void createShouldPersistData(){
        Student student = new Student("Jailson","jailsonmendes@estudante.sc.senai.br");
        this.studentRepository.save(student);
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo("Jailson");
        assertThat(student.getEmail()).isEqualTo("jailsonmendes@estudante.sc.senai.br");
    }
    
    @Test
    public void deleteShouldRemoveData(){
                Student student = new Student("Jailson","jailsonmendes@estudante.sc.senai.br");
                this.studentRepository.save(student);
                studentRepository.delete(student);
                assertThat(studentRepository.findOne(student.getId())).isNull();
    }
    @Test
    public void updateShouldChangeAndPersistData(){
                Student student = new Student("Jailson","jailsonmendes@estudante.sc.senai.br");
                this.studentRepository.save(student);
                student.setName("Jailson222");
                student.setEmail("jailson222mendes@estudante.sc.senai.br");
                student = this.studentRepository.save(student);
                student = this.studentRepository.findOne(student.getId());
                assertThat(student.getName()).isEqualTo("Jailson222");
                assertThat(student.getEmail()).isEqualTo("jailson222mendes@estudante.sc.senai.br");
    }
    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase(){
                Student student = new Student("Jailson","jailsonmendes@estudante.sc.senai.br");
                Student student2 = new Student("jailson","jailson222mendes@estudante.sc.senai.br");
                this.studentRepository.save(student);
                this.studentRepository.save(student2);
                List<Student> studentList = studentRepository.findByNameIgnoreCaseContaining("jailson");
                assertThat(studentList.size()).isEqualTo(2);
    }
    
    @Test
    public void createWhenNameIsNullShouldThrowConstraintViolationExcpetion(){
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("O campo nome do estudante é obrigatório!");
        this.studentRepository.save(new Student());
    }
    
    @Test
    public void createWhenEmailIsNullShouldThrowConstraintViolationExcpetion(){
        thrown.expect(ConstraintViolationException.class);
        Student student = new Student();
        student.setName("Jailson");
        this.studentRepository.save(student);
    }
    
    @Test
    public void createWhenEmailIsNotValidShouldThrowConstraintViolationExcpetion(){
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Digite um email válido!");
        Student student = new Student();
        student.setName("Jailson");
        student.setName("Jailson");
        this.studentRepository.save(student);
    }
}
