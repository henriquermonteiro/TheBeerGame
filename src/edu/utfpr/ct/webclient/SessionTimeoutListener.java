package edu.utfpr.ct.webclient;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

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
