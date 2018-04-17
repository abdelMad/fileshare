<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>

<div class="">
    <div id="user-profile-2" class="user-profile">
        <div class="tabbable">

            <div class="tab-content no-border padding-24">
                <div id="home" class="tab-pane in active">
                    <div class="row">
                        <div class="col-xs-12 col-sm-3 center">
                            <c:set var="image" scope="page" value="/assets/images/people.png" />
                            <c:if test="${not empty profilUtilisateur.image}">
                                <c:set var="image" scope="page" value="${profilUtilisateur.image}" />
                            </c:if>
                            <span class="profile-picture">
                                <img class="editable img-responsive"
                                     alt="image de ${profilUtilisateur.nom}"
                                     id="avatar2" src="${image}">
                            </span>

                            <div class="space space-4"></div>

                            <a href="/messages?utilisateur=${profilUtilisateur.id}" id="send_message" class="btn btn-sm btn-block btn-primary">
                                <i class="ace-icon fa fa-envelope-o bigger-110"></i>
                                <span class="bigger-110">Envoyer un message</span>
                            </a>
                        </div><!-- /.col -->

                        <div class="col-xs-12 col-sm-9">
                            <h4 class="blue">
                                <span class="middle">${profilUtilisateur.nom} ${profilUtilisateur.prenom}</span>


                            </h4>

                            <div class="profile-user-info">
                                <div class="profile-info-row">
                                    <div class="profile-info-name"> Nom</div>

                                    <div class="profile-info-value">
                                        <span>${profilUtilisateur.nom}</span>
                                    </div>
                                </div>

                                <div class="profile-info-row">
                                    <div class="profile-info-name"> Prénom</div>

                                    <div class="profile-info-value">
                                        <span>${profilUtilisateur.prenom}</span>
                                    </div>
                                </div>

                                <div class="profile-info-row">
                                    <div class="profile-info-name"> Email</div>

                                    <div class="profile-info-value">
                                        <span>${profilUtilisateur.email}</span>
                                    </div>
                                </div>


                                <div class="profile-info-row">
                                    <div class="profile-info-name"> A rejoigner le</div>

                                    <div class="profile-info-value">
                                        <span><fmt:formatDate pattern="dd-MM-yyyy"
                                                              value="${profilUtilisateur.registerDate}"/> </span>
                                    </div>
                                </div>


                            </div>

                            <div class="hr hr-8 dotted"></div>


                        </div><!-- /.col -->
                    </div><!-- /.row -->

                    <div class="space-20"></div>
                    <c:if test="${not empty profilUtilisateur.description}">
                        <div class="row">
                            <div class="col-xs-12 ">
                                <div class="widget-box transparent">
                                    <div class="widget-header widget-header-small">
                                        <h4 class="widget-title smaller">
                                            <i class="ace-icon fa fa-check-square-o bigger-110"></i>
                                            A propos de moi
                                        </h4>
                                    </div>

                                    <div class="widget-body">
                                        <div class="widget-main">
                                            <p>
                                                    ${profilUtilisateur.description}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </c:if>
                </div><!-- /#home -->
            </div>
        </div>
    </div>
</div>
<%@include file="parts/footer.jsp" %>