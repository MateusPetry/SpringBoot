/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mateus
 */
package br.com.devdojo.awesome.model;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity implements Serializable{
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    protected Long id;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
       
        return id != null ? id.equals(that.id) : that.id == null;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
}
