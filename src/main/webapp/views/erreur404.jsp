<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp"%>
<div class="error-container">
    <div class="well">
        <h2 class="grey lighter smaller">
											<span class="blue bigger-125">
												<i class="ace-icon fa fa-sitemap"></i>
												404
											</span>
            Page Notn trouvée
        </h2>

        <hr>
        <h3 class="lighter smaller">Nous avons regardé partout mais nous n'avons pas pu le trouver!</h3>

        <div>


            <div class="space"></div>
            <h4 class="smaller">Essayez l'une des solutions suivantes:</h4>

            <ul class="list-unstyled spaced inline bigger-110 margin-15">
                <li>
                    <i class="ace-icon fa fa-hand-o-right blue"></i>
                    Re-vérifier l'URL pour les fautes de frappe
                </li>
                <li>
                    <i class="ace-icon fa fa-hand-o-right blue"></i>
                    Dis nous à propos de cela
                </li>
            </ul>
        </div>

        <hr>
        <div class="space"></div>

        <div class="center">
            <a href="javascript:history.back()" class="btn btn-grey">
                <i class="ace-icon fa fa-arrow-left"></i>
                Retour
            </a>

            <a href="/" class="btn btn-primary">
                <i class="ace-icon fa fa-tachometer"></i>
                Accueil
            </a>
        </div>
    </div>
</div>

<%@include file="parts/footer.jsp"%>