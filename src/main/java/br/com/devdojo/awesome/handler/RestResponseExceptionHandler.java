/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devdojo.awesome.handler;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
//import sun.misc.IOUtils;

/**
 * @author William Suane for DevDojo on 7/7/17.
 */
@ControllerAdvice
public class RestResponseExceptionHandler extends DefaultResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        System.out.println("Inside hasError");
        return super.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        System.out.println("Doing something with status code "+response.getStatusCode());
        System.out.println("Doing something with body "+ IOUtils.toString(response.getBody(),"UTF-8"));
//        super.handleError(response);
    }
}