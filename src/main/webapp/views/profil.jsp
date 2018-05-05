<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>
<div class="page-header">
    <h2>
        Mon Profile

    </h2>
</div>
<div id="user-profile-1" class="user-profile row">
    <div class="col-xs-12 col-sm-3 center">
        <div>
            <c:set var="image" scope="page" value="/assets/images/people.png"/>
            <c:if test="${not empty utilisateur.image}">
                <c:set var="image" scope="page" value="${utilisateur.image}"/>
            </c:if>
            <span class="profile-picture">
                <img id="imageProfile" class="editable img-responsive" alt="${utilisateur.nom} image"
                     src="${image}"/>
            </span>
        </div>

        <div class="space-6"></div>

        <div class="hr hr12 dotted"></div>

        <div class="clearfix">
            <div class="grid2">
                <span class="bigger-175 blue">25</span>

                <br/>
                Followers
            </div>

            <div class="grid2">
                <span class="bigger-175 blue">12</span>

                <br/>
                Following
            </div>
        </div>

        <div class="hr hr16 dotted"></div>
    </div>

    <div class="col-xs-12 col-sm-9">

        <div class="space-12"></div>
        <form method="post" action="/modifier-profil">
            <div class="profile-user-info profile-user-info-striped">
                <div class="profile-info-row">
                    <div class="profile-info-name"> Nom</div>

                    <div class="profile-info-value">
                        <span class="editable" data-name="nom" id="nom">${utilisateur.nom}</span>
                    </div>
                </div>
                <div class="profile-info-row">
                    <div class="profile-info-name"> Prenom</div>

                    <div class="profile-info-value">
                        <span class="editable" data-name="prenom" id="prenom">${utilisateur.prenom}</span>
                    </div>
                </div>
                <div class="profile-info-row">
                    <div class="profile-info-name"> Email</div>

                    <div class="profile-info-value">
                        <span class="editable" data-name="email" id="email">${utilisateur.email}</span>
                    </div>
                </div>
                <div class="profile-info-row">
                    <div class="profile-info-name"> Date creation du compte</div>

                    <div class="profile-info-value">
                    <span class="editable"><fmt:formatDate pattern="dd-MM-yyyy hh:mm"
                                                           value="${utilisateur.registerDate}"></fmt:formatDate></span>
                    </div>
                </div>


                <div class="profile-info-row">
                    <div class="profile-info-name">Description</div>

                    <div class="profile-info-value">
                        <span class="editable" data-name="description" id="about">${utilisateur.description}</span>
                    </div>
                </div>
            </div>
        </form>
        <div class="space-20"></div>


        <div class="space-6"></div>

    </div>
</div>
<%@include file="parts/footer.jsp" %>