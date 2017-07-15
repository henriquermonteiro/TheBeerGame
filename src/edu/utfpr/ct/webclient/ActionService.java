package edu.utfpr.ct.webclient;

import edu.utfpr.ct.datamodel.Game;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import test.mock.ControllerPlayerMock;
import edu.utfpr.ct.interfaces.IControllerPlayer;
import edu.utfpr.ct.interfaces.IFunction;
import org.apache.catalina.Context;

public class ActionService {
    private Tomcat server;
    private IControllerPlayer controler;
    
    private static ActionService service;

    public static ActionService getService() {
        return service;
    }

    
    private static File getRootFolder() {
        try {
            File root;
            String runningJarPath = ActionService.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            System.out.println("application resolved root folder: " + root.getAbsolutePath());
            return root;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) throws Exception {
        new ActionService(new ControllerPlayerMock());
//        File root = getRootFolder();
//        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
//        Tomcat tomcat = new Tomcat();
//        Path tempPath = Files.createTempDirectory("tomcat-base-dir");
//        tomcat.setBaseDir(tempPath.toString());
//        
//        //The port that we should run on can be set into an environment variable
//        //Look for that variable and default to 8080 if it isn't there.
//        String webPort = System.getenv("PORT");
//        if (webPort == null || webPort.isEmpty()) {
//            webPort = "8081";
//        }
//
//        tomcat.setPort(Integer.valueOf(webPort));
//        File webContentFolder = new File(root.getAbsolutePath(), "src/edu/utfpr/ct/webclient/webapp/");
//        if (!webContentFolder.exists()) {
//            webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
//        }
//        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
//        //Set execution independent of current thread context classloader (compatibility with exec:java mojo)
//        ctx.setParentClassLoader(ActionService.class.getClassLoader());
//
//        //Disable TLD scanning by default
//        if (System.getProperty(Constants.SKIP_JARS_PROPERTY) == null && System.getProperty(Constants.SKIP_JARS_PROPERTY) == null) {
//            System.out.println("disabling TLD scanning");
//            StandardJarScanFilter jarScanFilter = (StandardJarScanFilter) ctx.getJarScanner().getJarScanFilter();
//            jarScanFilter.setTldSkip("*");
//        }
//
//        System.out.println("configuring app with basedir: " + webContentFolder.getAbsolutePath());
//
//        // Declare an alternative location for your "WEB-INF/classes" dir
//        // Servlet 3.0 annotation will work
//        File additionWebInfClassesFolder = new File(root.getAbsolutePath(), "build/classes");
//        WebResourceRoot resources = new StandardRoot(ctx);
//
//        WebResourceSet resourceSet;
//        if (additionWebInfClassesFolder.exists()) {
//            resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClassesFolder.getAbsolutePath(), "/");
//            System.out.println("loading WEB-INF resources from as '" + additionWebInfClassesFolder.getAbsolutePath() + "'");
//        } else {
//            resourceSet = new EmptyResourceSet(resources);
//        }
//        resources.addPreResources(resourceSet);
//        ctx.setResources(resources);
//
//        tomcat.start();
//        tomcat.getServer().await();
    }

    public ActionService(IControllerPlayer controler) throws ServletException, LifecycleException, IOException {
        this.controler = controler;
        service = this;
        
        File root = getRootFolder();
        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
        Tomcat tomcat = new Tomcat();
        server = tomcat;
        
        Path tempPath = Files.createTempDirectory("tomcat-base-dir");
        tomcat.setBaseDir(tempPath.toString());
        
        String portT = System.getenv("PORT");
        
        Integer port;
        
        try{
            port = Integer.parseInt(portT);
        }catch(NumberFormatException ex){
            port = 8081;
        }
        
        tomcat.setPort(port);
        
        File webContentFolder = new File(root.getAbsolutePath(), "src/edu/utfpr/ct/webclient/webapp/");
        if (!webContentFolder.exists()) {
            webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
        }
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
        ctx.setDelegate(true);
        //Set execution independent of current thread context classloader (compatibility with exec:java mojo)
        ctx.setParentClassLoader(ActionService.class.getClassLoader());
        
        //Disable TLD scanning by default
        if (System.getProperty(Constants.SKIP_JARS_PROPERTY) == null && System.getProperty(Constants.SKIP_JARS_PROPERTY) == null) {
            System.out.println("disabling TLD scanning");
            StandardJarScanFilter jarScanFilter = (StandardJarScanFilter) ctx.getJarScanner().getJarScanFilter();
            jarScanFilter.setTldSkip("*");
        }

        System.out.println("configuring app with basedir: " + webContentFolder.getAbsolutePath());
        
        ctx.getServletContext().setAttribute("action-service", this);
        
        File additionWebInfClassesFolder = new File(root.getAbsolutePath(), "build/classes");
        WebResourceRoot resources = new StandardRoot(ctx);

        WebResourceSet resourceSet;
        
//        File f = new File("TheBeerGame.jar");
//        String path = f.getAbsolutePath();
//        System.out.println(path);
        
//        resourceSet = new JarResourceSet(resources, "/WEB-INF/classes", path, "/");
        
        
        if (additionWebInfClassesFolder.exists()) {
            resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClassesFolder.getAbsolutePath(), "/");
            System.out.println("loading WEB-INF resources from as '" + additionWebInfClassesFolder.getAbsolutePath() + "'");
        } else {
            resourceSet = new EmptyResourceSet(resources);
        }
        resources.addPreResources(resourceSet);
        ctx.setResources(resources);
        
//        tomcat.addServlet("/checkin", "BeerGameServletCheckin", new CheckinServlet(this));

        server.start();
        System.out.println("Wee");
        server.getServer().await();
    }
    
    public String checkIn(String playerID){
        return controler.checkIn(playerID);
    }
    
    public Game[] listAvailableRooms(String playerName){
        return controler.listAvailableGameRooms(playerName);
    }
    
    public boolean enterGameRoom(String gameName, String playerID, String password){
        return controler.enterGameRoom(gameName, playerID, password);
    }
    
    public int postMove(String gameName, IFunction function, String playerID, Integer move){
        return controler.postMove(gameName, function, playerID, move);
    }
    
    public Game updateData(String gameName, String playerName){
        return controler.getGameData(gameName, playerName);
    }
    
    public Boolean gameHasFinished(String gameName){
        Integer ret = controler.getGameState(gameName);
        return (ret == 1? false : (ret == 8? true: null));
    }
}
