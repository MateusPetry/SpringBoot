/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devdojo.awesome.error;

/**
 *
 * @author mateus
 */
public class CustomErrorType {
    private String errorMessage;

    public CustomErrorType(String errorMessage) {
    this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
}
