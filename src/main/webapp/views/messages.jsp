<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>

<div class="row">
    <div class="col-sm-5">
        <div class="people-list" id="people-list">
            <div class="search">
                <input type="text" placeholder="search" id="rechercher-contact"/>
                <i class="fa fa-search"></i>
            </div>
            <ul class="list">
                <c:if test="${not empty contacts}">
                    <c:forEach items="${contacts}" var="contact">
                        <li class="clearfix contact-row" data-contact-id="${contact.id}">
                            <c:set var="image" scope="page" value="/assets/images/people.png"/>
                            <c:if test="${not empty contact.image}">
                                <c:set var="image" scope="page" value="${contact.image}"/>
                            </c:if>
                            <img class="contact-img" src="${image}"
                                 alt="image de ${contact.nom}"/>
                            <div class="about">
                                <div class="name">${contact.prenom} ${contact.nom}</div>

                                <%--<div class="status">--%>
                                    <%--<i class="fa fa-circle online"></i> online--%>
                                <%--</div>--%>
                            </div>
                        </li>
                    </c:forEach>
                </c:if>


            </ul>
        </div>
    </div>
    <div class="col-sm-7">
        <div class="chat ">
            <c:if test="${not empty recepteur}">
                <div class="chat-header clearfix">
                    <c:set var="recepteurImage" scope="page" value="/assets/images/people.png"/>
                    <c:if test="${not empty recepteur.image}">
                        <c:set var="recepteurImage" scope="page" value="${recepteur.image}"/>
                    </c:if>
                    <a href="/profil/${recepteur.id}"><img class="contact-img" src="${recepteurImage}" alt="avatar"/></a>

                    <div class="chat-about">
                        <div class="chat-with"><a href="/profil/${recepteur.id}">${recepteur.prenom} ${recepteur.nom}</a></div>
                    </div>
                </div>
                <!-- end chat-header -->

                <div class="chat-history">
                    <ul>
                        <c:if test="${not empty messages}">
                            <c:forEach items="${messages}" var="msg">
                                <c:if test="${utilisateur.id eq msg.emetteur.id}">
                                    <li class="clearfix">
                                        <div class="message-data align-right">
                                            <span class="message-data-time"><fmt:formatDate pattern="dd-MM-yyyy hh:mm"
                                                                                            value="${msg.date}"/> </span>
                                            <span class="message-data-name"> Moi</span><i class="fa fa-circle me"></i>

                                        </div>
                                        <div class="message other-message float-right">
                                            <c:out value="${msg.text}"></c:out>
                                        </div>
                                    </li>
                                </c:if>
                                <c:if test="${utilisateur.id ne msg.emetteur.id}">
                                    <li>
                                        <div class="message-data">
                                        <span class="message-data-name"><i
                                                class="fa fa-circle online"></i><c:out
                                                value="${msg.emetteur.nom}"></c:out></span>
                                            <span class="message-data-time"><fmt:formatDate pattern="dd-MM-yyyy hh:mm"
                                                                                            value="${msg.date}"/></span>
                                        </div>
                                        <div class="message my-message">
                                            <c:out value="${msg.text}"></c:out>
                                        </div>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </ul>

                </div>
                <!-- end chat-history -->
            </c:if>
            <div class="chat-message clearfix" data-contact-id="${recepteur.id}">
                <label for="message-a-envoyer" class="sr-only">Message à envoyer</label>
                <textarea name="message-a-envoyer" id="message-a-envoyer" placeholder="Tapez votre Message"
                          rows="3"></textarea>
                <button id="envoyer-message" class="btn btn-info">Envoyer</button>

            </div> <!-- end chat-message -->

        </div> <!-- end chat -->
    </div>
</div>
<!-- end container -->


<%@include file="parts/footer.jsp" %>
