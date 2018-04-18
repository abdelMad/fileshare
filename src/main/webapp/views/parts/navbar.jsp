<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="navbar" class="navbar navbar-default">
    <div class="navbar-container" id="navbar-container">
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
            <span class="sr-only">Toggle sidebar</span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>
        </button>

        <div class="navbar-header pull-left">
            <a href="/" class="navbar-brand">
                <small>
                    <i class="fa fa-folder-open-o"></i>
                    File Share
                </small>
            </a>
        </div>

        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <c:if test="${not empty utilisateur}">
                <ul class="nav ace-nav">
                    <c:set var="image" scope="page" value="/assets/images/people.png"/>
                    <c:if test="${not empty utilisateur.image}">
                        <c:set var="image" scope="page" value="${utilisateur.image}"/>
                    </c:if>
                    <li class="light-blue">
                        <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                            <img class="nav-utilisateur-photo" src="${image}" alt="${utilisateur.nom}"/>
                            <span class="utilisateur-info">
									<small>Bienvenu,</small>
									${utilisateur.nom}
								</span>

                            <i class="ace-icon fa fa-caret-down"></i>
                        </a>

                        <ul class="utilisateur-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">

                            <li>
                                <a href="/profil">
                                    <i class="ace-icon fa fa-user"></i>
                                    Profile
                                </a>
                            </li>

                            <li class="divider"></li>

                            <li>
                                <a href="/deconnexion">
                                    <i class="ace-icon fa fa-power-off"></i>
                                    Déconnexion
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </c:if>
            <c:if test="${empty utilisateur}">
                <ul class="nav navbar-nav user-menu">
                    <li class="light-blue"><a href="/connexion"><i class="fa ace-icon fa-arrow-circle-o-down"></i>Connexion</a>
                    </li>
                </ul>
            </c:if>
        </div>
    </div><!-- /.navbar-container -->
</div>
