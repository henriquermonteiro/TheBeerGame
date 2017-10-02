package edu.utfpr.ct.webclient;

import edu.utfpr.ct.gamecontroller.Table;
import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.gamecontroller.Controller;
import edu.utfpr.ct.interfaces.IControllerHost;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import edu.utfpr.ct.interfaces.IControllerPlayer;
import edu.utfpr.ct.util.IPUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.catalina.Context;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11AprProtocol;
import org.apache.coyote.http11.Http11NioProtocol;

public class ActionService {

    private Tomcat server;
    private IControllerPlayer controler;
    private IControllerHost controlerHost;
    private Thread listenner;

//    private static boolean stopService = false;
    private static ActionService service;

    public static ActionService getService() {
        if (service == null) {

            try {
                service = new ActionService();
            } catch (ServletException | LifecycleException | IOException ex) {
            }
        }

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

    private ActionService() throws ServletException, LifecycleException, IOException {
        this.controler = Controller.getController();
        this.controlerHost = Controller.getController();
        service = this;

        File root = getRootFolder();
        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
        Tomcat tomcat = new Tomcat();
        server = tomcat;

        Path tempPath = Files.createTempDirectory("tomcat-base-dir");
        tomcat.setBaseDir(tempPath.toString());

        String portT = System.getenv("PORT");

        Integer port = null;

        try {
            port = Integer.parseInt(portT);
        } catch (NumberFormatException ex) {
            if (IPUtils.availablePort(80)) {
                port = 80;
            } else {
                if (IPUtils.availablePort(8080)) {
                    port = 8080;
                } else {
                    for (int k = 1; k < 10; k++) {
                        if (IPUtils.availablePort(8080 + k)) {
                            port = 8080 + k;
                            break;
                        }
                    }

                    if (port == null) {
                        port = 0;
                    }
                }
            }
        }

        tomcat.setPort(port);

        File webContentFolder = new File(root.getAbsolutePath(), "src/edu/utfpr/ct/webclient/webapp/");
        if (!webContentFolder.exists()) {
            webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
        }
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
        ctx.setSessionTimeout(1);
        System.out.println("Timeout: " + ctx.getSessionTimeout());
        ctx.addApplicationListener(SessionTimeoutListener.class.getName());
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

        listenner = new Thread() {
            @Override
            public void run() {
//                if(stopService){
//                    System.out.println("Stopping");
//                    return;
//                }
                server.getServer().await();
            }

        };

        listenner.start();
//        server.getServer().await();
    }

    public void stopService() {
        try {
//            stopService = true;
//            listenner.interrupt();
            server.stop();
        } catch (LifecycleException ex) {
        }
    }

    public boolean checkIn(String playerID) {
        return controler.checkIn(playerID);
    }

    public Game[] listAvailableRooms() {
        return controler.listAvailableGameRooms();
    }

    public boolean enterGameRoom(String gameName, String playerID, String password) {
        return controler.enterGameRoom(gameName, playerID, password);
    }

    public int postMove(String gameName, String playerID, Integer move) {
        return controler.postMove(gameName, playerID, move);
    }

    public EngineData updateData(String gameName, String playerName) {
        return controler.getGameData(gameName, playerName);
    }

    public Table getTableData(String gameName) {
        return controler.getTable(gameName);
    }

    public Boolean gameHasFinished(String gameName) {
        Integer ret = controler.getGameState(gameName);

        if (ret == 1 || ret == 2) {
            return false;
        }

        return (ret == -1 ? true : null);
    }

    public boolean isNameAvailable(String player) {
        return controler.isNameAvailable(player);
    }

    public boolean logout(String player) {
        return controler.logout(player);
    }

    public int getPort() {
        for (Service s : server.getServer().findServices()) {
            for (Connector c : s.findConnectors()) {
                ProtocolHandler pH = c.getProtocolHandler();
                if (pH instanceof Http11AprProtocol || pH instanceof Http11AprProtocol || pH instanceof Http11NioProtocol) {
                    return c.getLocalPort();
                }
            }
        }

        return -1;
    }
    
    public Game getGameInfo(String gameName){
        return controlerHost.getGame(gameName);
    }
    
    public Game getReportInfo(String gameName){
        return controlerHost.getReport(gameName);
    }
}
