/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devdojo.awesome.model;
import javax.persistence.Entity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author mateus
 */
@Entity
public class Student extends AbstractEntity{
    @NotEmpty(message = "O campo nome do estudante é obrigatório")
    private String name;
    @NotEmpty
    @Email(message = "Digite um email válido!")
    private String email;

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public Student(Long id,String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Student() {
    }
    
    
    @Override
    public String toString() {
        return "Student{" + "name=" + name + ", email=" + email + '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    
    
}
