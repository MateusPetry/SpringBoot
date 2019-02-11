/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devdojo.awesome.repository;

import br.com.devdojo.awesome.model.Student;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author mateus
 */
public interface StudentRepository extends PagingAndSortingRepository<Student, Long>{
    List<Student> findByNameIgnoreCaseContaining(String name);
}
