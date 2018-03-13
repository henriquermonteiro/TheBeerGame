<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="java.util.Locale" %>
<%@page import="org.json.simple.*" %>
<%@page import="org.json.simple.parser.*" %>
<%@page import="edu.utfpr.ct.localization.*" %>
<%@page import="edu.utfpr.ct.util.ReplacerUtils" %>

<%
    if(request.getParameter("lang") != null){
        session.setAttribute("PREF-LANG", request.getParameter("lang"));
    }
    
    String lang = Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry();
    if(session.getAttribute("PREF-LANG") != null){ 
        lang = (String) session.getAttribute("PREF-LANG");
    }
    
    Localize localize = ClientLocalizationManager.getInstance().getClientFor(lang);
    
    String name = (String) session.getAttribute("USER-ID");
%>
<jsp:include page="resources/head_begin.jsp"/>
<script src="resources/dialog-polyfill.js"></script>
<link rel="stylesheet" type="text/css" href="resources/dialog-polyfill.css"/>
<link rel="stylesheet" type="text/css" href="game.css"/>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <script type="text/javascript">
            function showSlideshow(){
                
                var el = document.getElementById("dialog");
                
                el.showModal();
            }
            
            function closeSlideshow() {
                var el = document.getElementById("dialog");

                el.close();
                
                el = document.getElementById("vid-tutorial");
                
                if(el !== null){
                    el.pause();
                }
            }
        </script>
        <jsp:include page="resources/head_finish.jsp"/>
        <jsp:include page="resources/body_begin.jsp">
            <jsp:param name="source" value="slideshow.jsp"/>
        </jsp:include>
                <div class="center-content">
                    <div class="mdl-card mdl-shadow--2dp">
                        <button onclick="showSlideshow()">Show Things</button>
                    </div>
                </div>
        
                <dialog id="dialog" class="mdl-dialog">
                    <div class="mdl-dialog__content">
                        <div class="w3-content">
                            <% 
                                File f = new File("resources"+File.separator+"webapp"+File.separator+"videos"+File.separator+"mov.mp4");
                                boolean video_flag = false;
                                if(f.exists() && !f.isDirectory()){
                                    video_flag = true;
                            %>
                            <div class="mySlide" style="width: 100%; height: 100%; text-align: center;">
                                <video id="vid-tutorial" width="800" height="600" controls>
                                    <source src="videos/mov.mp4" type="video/mp4">
                                </video>
                            </div>
                            <%
                                }
                                
                                String tutorial = "";

                                f = new File("resources"+File.separator+"lang"+File.separator+"web"+File.separator+lang+"_tutorial.tut");
                                if(f.exists() && !f.isDirectory()){
                                    FileInputStream fis = null;

                                    try {
                                        fis = new FileInputStream(f);
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

                                        String s;
                                        while ((s = reader.readLine()) != null) {
                                            tutorial = tutorial.concat(s);
                                        }

                                    } catch (IOException ex) {
                                        tutorial = "";
                                    } finally {
                                        if (fis != null) {
                                            try {
                                                fis.close();
                                            } catch (IOException ex) {
                                            }
                                        }
                                    }
                                }
                            %>
                            <%=ReplacerUtils.tutorialReplace(tutorial)%>
                        </div>
                    </div>
                <div class="mdl-dialog__actions">
                    <div id="slideshowcontrol" class="w3-center w3-section" style="width: 100%;">
                        <button class="w3-button w3-light-grey" onclick="plusDivs(-1)">&#10094; Prev</button>
                        <% if(video_flag) { %>
                        <button class="w3-button demo" onclick="currentDiv(1)"><i class="material-icons" style="font-size: 16px;">videocam</i></button>
                        <% } %>
                        <button id="slide-next" class="w3-button w3-light-grey" onclick="plusDivs(1)">Next &#10095;</button>
                    </div>
                    
                    <button id="dlg_button" type="button" class="mdl-button" onclick="closeSlideshow()"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TUTO_BUTTON)) %></button>
                </div>
            </dialog>
            <script>
                dialogPolyfill.registerDialog(document.getElementById("dialog"));
                
                var slideIndex = 1;
                buildSlideshow();

                function plusDivs(n) {
                  showDivs(slideIndex += n);
                }

                function currentDiv(n) {
                  showDivs(slideIndex = n);
                }
                
                function buildSlideshow(){
                    var action_div = document.getElementById("slideshowcontrol");
                    var slides = document.getElementsByName("text_tutorial");
                    var next = document.getElementById("slide-next");
                    
                    var i;
                    for(i = 0; i < slides.length; i++){
                        var but = document.createElement("button");
                        but.className = "w3-button demo";
                        but.innerHTML = i+1;
                        but.onclick = function() { currentDiv( Number(this.innerHTML) + 1 ); };
                        
                        action_div.insertBefore(but, next);
                    }
                    
                    showDivs(slideIndex);
                }

                function showDivs(n) {
                  var i;
                  var x = document.getElementsByClassName("mySlide");
                  var dots = document.getElementsByClassName("demo");
                  if (n > x.length) {slideIndex = 1;}    
                  if (n < 1) {slideIndex = x.length;}
                  for (i = 0; i < x.length; i++) {
                     x[i].style.display = "none";  
                  }
                  for (i = 0; i < dots.length; i++) {
                     dots[i].className = dots[i].className.replace(" w3-red", "");
                  }
                  x[slideIndex-1].style.display = "block";  
                  dots[slideIndex-1].className += " w3-red";
                  
                  document.getElementById("dlg_button").disabled = true;
                  <% if(video_flag){%>
                  if(slideIndex === 1) {document.getElementById("dlg_button").disabled = false;};        
                  <%}%>
                  if(slideIndex === x.length) {document.getElementById("dlg_button").disabled = false;};
                }
            </script>
        <jsp:include page="resources/body_finish.jsp" />
