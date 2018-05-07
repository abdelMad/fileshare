<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page import="fr.fileshare.utilities.Util" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>${title} - File Share</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/assets/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/assets/font-awesome/4.2.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="/assets/css/jquery.gritter.min.css"/>

    <!-- page specific plugin styles -->
    <%= Util.includeCssOrJs(request, "/mes-documents|/documents-favoris", "/assets/css/ui.jqgrid.min.css")%>
    <link rel="stylesheet" href="/assets/css/bootstrap-editable.min.css"/>

    <!-- text fonts -->
    <link rel="stylesheet" href="/assets/fonts/fonts.googleapis.com.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="/assets/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/assets/css/ace-part2.min.css" class="ace-main-stylesheet" />
    <![endif]-->
    <link rel="stylesheet" href="/assets/css/main.css" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/assets/css/ace-ie.min.css" />
    <![endif]-->


    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="/assets/js/html5shiv.min.js"></script>
    <script src="/assets/js/respond.min.js"></script>
    <![endif]-->
</head>

<body class="no-skin">
    <%@include file="navbar.jsp"%>
    <div class="main-container" id="main-container"
         <c:if test="${not empty utilisateur}">data-utilisateur="${utilisateur.id}"</c:if>>
    <c:if test="${not empty utilisateur}">
        <%@include file="sidebar.jsp"%>
    </c:if>

        <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <c:if test="${not empty utilisateur}">
                    <%@include file="chatSideBar.jsp" %>
                </c:if>
                <div class="row">
                    <div class="col-md-12">
                        <div class="global-alert">
                            <%= Util.showGlobalAlerts() %>
                        </div>
