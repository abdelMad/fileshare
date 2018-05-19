<%@ page import="fr.fileshare.utilities.Util" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta charset="utf-8">
    <title>Connexion - File Share</title>

    <meta name="description" content="User login page">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="/assets/font-awesome/4.2.0/css/font-awesome.min.css">

    <!-- text fonts -->
    <link rel="stylesheet" href="/assets/fonts/fonts.googleapis.com.css">

    <!-- ace styles -->
    <link rel="stylesheet" href="/assets/css/ace.min.css">

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/assets/css/ace-part2.min.css"/>
    <![endif]-->

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/assets/css/ace-ie.min.css"/>
    <![endif]-->
    <link rel="stylesheet" href="/assets/css/main.css">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lt IE 9]>
    <script src="/assets/js/html5shiv.min.js"></script>
    <script src="/assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body class="login-layout light-login">
<div class="main-container">
    <div class="main-content">
        <div class="global-alert">
            <%= Util.showGlobalAlerts() %>
        </div>

        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div class="login-container">
                    <div class="center">
                        <h1>
                            <i class="ace-icon fa fa-folder-open-o green"></i>
                            <span class="red">File</span>
                            <span class="grey" id="id-text2">Share</span>
                        </h1>
                        <%--<h4 class="blue" id="id-company-text">© File Share</h4>--%>
                    </div>

                    <div class="space-6"></div>

                    <div class="position-relative">
                        <c:set var="check" scope="page" value="${false}" />
                        <c:if test="${not empty showRecoverPwdForm}">
                            <c:if test="${showRecoverPwdForm eq true}">
                                <c:set var="check" scope="page" value="${true}" />
                            </c:if>
                        </c:if>

                        <c:if test="${check eq false}">
                        <div id="login-box" class="login-box widget-box no-border visible">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header blue lighter bigger">
                                        <i class="ace-icon fa fa-coffee green"></i>
                                        Merci d' entrer vos informations
                                    </h4>

                                    <div class="space-6"></div>

                                    <form action="/connexion" id="login-form" method="post">
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" name="cnx_email" class="form-control" placeholder="Email">
															<i class="ace-icon fa fa-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" name="cnx_mdp" class="form-control" placeholder="Mot de passe">
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                            </label>

                                            <div class="space"></div>

                                            <div class="clearfix">
                                                <input type="hidden" name="type" value="connexion">
                                                <button type="submit" class="width-40 btn btn-sm btn-primary">
                                                    <i class="ace-icon fa fa-key"></i>
                                                    <span class="bigger-110">Connexion</span>
                                                </button>
                                            </div>

                                            <div class="space-4"></div>
                                        </fieldset>
                                    </form>

                                </div><!-- /.widget-main -->

                                <div class="toolbar clearfix">
                                    <div>
                                        <a href="#" data-target="#forgot-box" class="forgot-password-link">
                                            <i class="ace-icon fa fa-arrow-left"></i>
                                            Mot de passe oublié
                                        </a>
                                    </div>

                                    <div>
                                        <a href="#" data-target="#signup-box" class="user-signup-link">
                                            Je m'enregistre
                                            <i class="ace-icon fa fa-arrow-right"></i>
                                        </a>
                                    </div>
                                </div>
                            </div><!-- /.widget-body -->
                        </div><!-- /.login-box -->

                        <div id="forgot-box" class="forgot-box widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header red lighter bigger">
                                        <i class="ace-icon fa fa-key"></i>
                                        Recupération de mot de passe
                                    </h4>

                                    <div class="space-6"></div>
                                    <p>
                                        Enter votre email pour recevoir les instructions
                                    </p>

                                    <form action="/recuperation-mot-de-passe" id="forgot-form" method="post">
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" name="email" class="form-control" placeholder="Email">
															<i class="ace-icon fa fa-envelope"></i>
														</span>
                                            </label>

                                            <div class="clearfix">
                                                <button type="submit" class="width-35 pull-right btn btn-sm btn-danger">
                                                    <i class="ace-icon fa fa-lightbulb-o"></i>
                                                    <span class="bigger-110">Envoyez</span>
                                                </button>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div><!-- /.widget-main -->

                                <div class="toolbar center">
                                    <a href="#" data-target="#login-box" class="back-to-login-link">
                                        Retour
                                        <i class="ace-icon fa fa-arrow-right"></i>
                                    </a>
                                </div>
                            </div><!-- /.widget-body -->
                        </div><!-- /.forgot-box -->

                        <div id="signup-box" class="signup-box widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header green lighter bigger">
                                        <i class="ace-icon fa fa-users blue"></i>
                                        Nouveau utilisateur
                                    </h4>

                                    <div class="space-6"></div>
                                    <p> Merci d'entrer vos informations: </p>

                                    <form action="/inscription" id="sign-up-form" method="post">
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" name="email" class="form-control" placeholder="Email">
															<i class="ace-icon fa fa-envelope"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" name="nom" class="form-control" placeholder="Nom">
															<i class="ace-icon fa fa-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" name="prenom" class="form-control" placeholder="Prenom">
															<i class="ace-icon fa fa-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="mdp_register" id="mdp" type="password"
                                                                   class="form-control" placeholder="Mot de passe">
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" name="confirm_mdp" class="form-control" placeholder="Verification mot de passe">
															<i class="ace-icon fa fa-retweet"></i>
														</span>
                                            </label>

                                            <label class="block">
                                                <input type="checkbox" name="conditions" class="ace">
                                                <span class="lbl">
															J'accepte
															<a href="/conditions-utilisations">les conditions d'utilisation</a>
														</span>
                                            </label>

                                            <div class="space-24"></div>

                                            <div class="clearfix">
                                                <button type="reset" class="width-40 pull-left btn btn-sm">
                                                    <i class="ace-icon fa fa-refresh"></i>
                                                    <span class="bigger-110">Renitialiser</span>
                                                </button>
                                                <input value="inscription" name="type" type="hidden">
                                                <button type="submit" class="width-40 pull-right btn btn-sm btn-success">
                                                    <span class="bigger-110">Inscription</span>

                                                    <i class="ace-icon fa fa-arrow-right icon-on-right"></i>
                                                </button>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>

                                <div class="toolbar center">
                                    <a href="#" data-target="#login-box" class="back-to-login-link">
                                        <i class="ace-icon fa fa-arrow-left"></i>
                                        Retour
                                    </a>
                                </div>
                            </div><!-- /.widget-body -->
                        </div><!-- /.signup-box -->
                        </c:if>
                        <c:if test="${check eq true}">

                            <div id="forgot-box" class="forgot-box widget-box no-border visible">
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <h4 class="header red lighter bigger">
                                            <i class="ace-icon fa fa-key"></i>
                                            Recupération de mot de passe
                                        </h4>

                                        <div class="space-6"></div>
                                        <p>
                                            Enter votre nouveau mot de passe
                                        </p>

                                        <form action="/recuperation-mot-de-passe" method="post">
                                            <fieldset>
                                                <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="mdp" type="password" class="form-control" placeholder="Mot de passe">
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                                </label>

                                                <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" name="confirm_mdp" class="form-control" placeholder="Verification mot de passe">
															<i class="ace-icon fa fa-retweet"></i>
														</span>
                                                </label>

                                                <div class="clearfix">
                                                    <input type="hidden" value="${token}" name="token">
                                                    <button type="submit" class="width-35 pull-right btn btn-sm btn-success">
                                                        <i class="ace-icon fa fa-lightbulb-o"></i>
                                                        <span class="bigger-110">Confirmer</span>
                                                    </button>
                                                </div>
                                            </fieldset>
                                        </form>
                                    </div><!-- /.widget-main -->

                                </div><!-- /.widget-body -->
                            </div><!-- /.forgot-box -->
                        </c:if>
                        
                    </div><!-- /.position-relative -->
                </div>
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.main-content -->
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script src="/assets/js/jquery.2.1.1.min.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="/assets/js/jquery.1.11.1.min.js"></script>
<![endif]-->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='/assets/js/jquery.min.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='assets/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='/assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>

<!-- inline scripts related to this page -->
<script src="/assets/js/jquery.validate.min.js"></script>
<script src="/assets/js/script.js"></script>

<script type="text/javascript">
    jQuery(function($) {
        $(document).on('click', '.toolbar a[data-target]', function(e) {
            e.preventDefault();
            var target = $(this).data('target');
            $('.widget-box.visible').removeClass('visible');//hide others
            $(target).addClass('visible');//show target
        });
    });</script>


</body>
</html>