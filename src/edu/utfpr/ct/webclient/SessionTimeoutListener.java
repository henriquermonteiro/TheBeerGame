/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.webclient;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author henrique
 */
@WebListener
public class SessionTimeoutListener implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        hse.getSession().setMaxInactiveInterval(5 * 60);
//        hse.getSession().setMaxInactiveInterval(5);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        Object user = hse.getSession().getAttribute("USER-ID");
        
        if(user != null && user instanceof String){
            Object obj = hse.getSession().getServletContext().getAttribute("action-service");

            if (obj instanceof ActionService) {
                ((ActionService) obj).logout((String)user);
//                System.out.println("killed " + user);
            }
        }
    }
    
}
