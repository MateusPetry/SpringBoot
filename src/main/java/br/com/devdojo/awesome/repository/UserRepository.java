/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devdojo.awesome.repository;

import br.com.devdojo.awesome.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author mateus
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long>{
    User findByUsername(String username);
 
}
