<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>
<div class="page-header">
    <h2 class="blue">
        Accueil
    </h2>
</div>
<form action="/" method="get" class="form-inline search-form-home">
    <div class="center">
        <input type="text" name="intitule" value="${param.intitule}" class="input-large" placeholder="Intitule fichier">
        <input type="text" name="tags" value="${param.tags}" class="input-large" placeholder="Tags">

        <button type="submit" class="btn btn-info btn-sm">
            <i class="ace-icon fa fa-search bigger-110"></i>Rechercher
        </button>
    </div>
</form>
<div id="timeline-1">
    <div class="row">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1">
            <c:if test="${empty docs}">
                <div class="text-center">
                    Aucun document disponible pour l'instant
                </div>
            </c:if>
            <c:if test="${not empty docs}">
                <div class="timeline-container">

                    <c:forEach items="${docs}" var="doc">
                        <div class="timeline-items">
                            <div class="timeline-item clearfix">
                                <div class="timeline-info">
                                    <c:set var="image" scope="page" value="/assets/images/people.png"/>
                                    <c:if test="${not empty doc.auteur.image}">
                                        <c:set var="image" scope="page" value="${doc.auteur.image}"/>
                                    </c:if>
                                    <a href="/profil<c:if test="${doc.auteur.id ne utilisateur.id}"> /${doc.auteur.id} </c:if>">
                                        <img alt="image de ${doc.auteur.nom}" src="${image}"/></a>
                                </div>

                                <div class="widget-box transparent">
                                    <div class="widget-header widget-header-small">
                                        <h5 class="widget-title smaller">
                                            <c:if test="${doc.auteur.id eq utilisateur.id}">
                                                <a href="/profil" class="red">Vous</a>
                                            </c:if>
                                            <c:if test="${doc.auteur.id ne utilisateur.id}">
                                                <a href="/profil/${doc.auteur.id}" class="blue"><c:out
                                                        value="${doc.auteur.nom}"></c:out></a>
                                            </c:if>
                                            <span class="grey">a publié un nouveau document: </span>
                                        </h5>
                                        <span id="titre${doc.id}">${doc.intitule}</span>
                                        <span class="widget-toolbar no-border">
																	<i class="ace-icon fa fa-clock-o bigger-110"></i>
                                            Le <fmt:formatDate pattern="dd-MM-yyyy"
                                                               value="${doc.datePublixation}"/> à <fmt:formatDate
                                                pattern="HH:mm"
                                                value="${doc.datePublixation}"/>
																</span>


                                    </div>

                                    <div class="widget-body">
                                        <div class="widget-main">
                                            <c:out value="${doc.description}"></c:out>


                                            <div class="space-6"></div>
                                            <c:if test="${not empty doc.tag}">
                                                <c:set var="tags" value="${fn:split(doc.tag, ' ')}"/>
                                                <c:forEach items="${tags}" var="tg">
                                                    <a href="/?tags=${tg}"><span
                                                            class="label label-sm label-error"><c:out
                                                            value="${tg}"></c:out></span></a>
                                                </c:forEach>
                                            </c:if>
                                            <div class="widget-toolbox clearfix">
                                                <div class="pull-left">
                                                    <i class="ace-icon fa fa-hand-o-right grey bigger-125"></i>
                                                    <a href="#" class="bigger-110  view-doc"
                                                       data-doc-id="<c:out value="${doc.id}"></c:out>">Voir le
                                                        document</a>
                                                    <div class="wysiwyg-editor hidden"
                                                         id="<c:out value="${doc.id}"></c:out>"
                                                         contenteditable="true">
                                                        <c:out value="${doc.dernierContenu}" escapeXml="false"></c:out>
                                                    </div>
                                                </div>

                                                <div class="pull-right action-buttons">
                                                    <a class="telecharger" title="télecharger le document"
                                                       data-doc="${doc.id}">
                                                        <i class="ace-icon fa fa-download green bigger-130"></i>
                                                    </a>
                                                    <c:if test="${(not empty utilisateur and utilisateur.id eq doc.auteur.id) or (doc.readOnly eq false)}">
                                                        <a href="/modifier-document?id=${doc.id}"
                                                           title="modifier le document">
                                                            <i class="ace-icon fa fa-pencil blue bigger-125"></i>
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </c:forEach>
                </div>
                <!-- /.timeline-container -->
            </c:if>

        </div>
    </div>
</div>

<%@include file="parts/footer.jsp" %>
<script>
    $(window).scroll(function () {
        if ($(window).scrollTop() == $(document).height() - $(window).height()) {

        }
    });
</script>
