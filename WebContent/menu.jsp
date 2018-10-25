    <%@page import="util.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<header id="Header">
    <%
    User user = (User) session.getAttribute("user");    
    String username = user!=null? user.getUserName()+",您好":"尚未登入";
    %>
    <style>
    
    .loginUser{
       position: fixed ;
       right:25px;
       top:5px;       
       color: white;
       z-index: 9;
    }
</style>  
        <div class="container">            
            <div style="margin-left:-15px;margin-right:-15px">                    
                <a class="loginUser" id="username" href="login.jsp"><%=username%></a> 
                <nav class="navbar navbar-default megamenu" role="navigation">                    
                    <div class="container" style="padding-left: 0px;padding-right: 0px;">                        
                        <div class="navbar-header">                            
                            <button type="button" class="navbar-toggle pull-left" data-toggle="collapse" data-target="#offcanvas-left"><span class="sr-only">Toggle navigation</span><i class="icon-menu"></i></button>
                            <div class="Logo">
                                <!--<a title="警察刑事紀錄證明書" href="index.jsp"><img id='top_img' alt="警察刑事紀錄證明書" src="assets/img/logo4.png"  width="300"  height="70"></a>-->
                                <a title="警察刑事紀錄證明書" href="index.jsp"><img id='top_img' alt="無限創意表演團" src="assets/img/logo4.png"  width="80"  height="80"></a>
                            </div>                            
                            <div class="NAME">
                                無限票務系統                                
                            </div>
                            
                            <!--   <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#offcanvas-right"><span class="sr-only">Toggle navigation</span><i class="icon-down-open"></i></button>-->
                        </div>
                        
                        <div class="collapse navbar-collapse Topmenu" id="offcanvas-left" role="navigation" style="padding-left: 0px;padding-right: 0px;">
                            <div id="MENU" class="menu">
                                <ul>
<!--                                    <li>
                                        <a  id="first_menu" target="_self" href="login.jsp">重新登入</a>
-->                                    
                                    <li>
                                        <a href="index.jsp">說明</a>
                                    </li>
                                    <li>
                                        <a href="forAudience.jsp">觀眾索票</a>
                                    </li>
                                    <li>
                                        <a href="requestTicket.jsp">索票登錄(簡易)</a>
                                    </li>
                                    <li>
                                        <a href="requestTicketMod.jsp">索票登錄</a>
                                    </li>
                                    <li>
                                        <a href="reply.jsp">票根輸入</a>
                                    </li>
                                    <li>
                                        <a href="queryTickStatus.jsp">出席狀況</a>
                                    </li>
                                    <li>
                                        <a href="replyComment.jsp">回條輸入</a>
                                    </li>
                                    
<!--                                    <li>
                                        <a href="apply.jsp">(未)申辦作業</a>
                                    </li>-->
<!--                                    <li>
                                        <a href="query.jsp">出席狀況</a>
                                    </li>-->
                                    
                                    
<!--                                    <li>
                                        <a>統計資料<b class=""></b></a>
                                        <ul>
                                            <li>
                                                <a href="queryTickStatus.jsp">索票狀況</a>
                                            </li>
                                            <li>
                                                <a href="query.jsp">(未)查詢作業</a>
                                            </li>
                                                                                      
                                        </ul>
                                    </li>-->
                                </ul>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
        </div>
    </header>
    