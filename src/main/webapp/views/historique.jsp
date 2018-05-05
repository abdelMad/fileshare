<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>
<div class="page-header">
    <h2 class="blue">
        ${title}
    </h2>
</div>
<c:if test="${empty historiques}">
    <div id="docs">
        <table id="dynamic-table" class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th>Intitule</th>
                <th>
                    <i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
                    Date Publication
                </th>
                <th>
                    <i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
                    Dernière modification
                </th>
                <th>Tags</th>
                <th class="hidden-480">Dernier Editeur</th>


                <th class="hidden-480">Status</th>

                <th></th>
            </tr>
            </thead>

            <tbody>
            <c:if test="${not empty documents}">
                <c:forEach items="${documents}" var="doc">
                    <td id="titre${doc.id}">
                        <c:out value="${doc.intitule}"></c:out>
                    </td>
                    <td><fmt:formatDate pattern="dd-MM-yyyy HH:mm"
                                        value="${doc.datePublixation}"/></td>
                    <td><fmt:formatDate pattern="dd-MM-yyyy HH:mm"
                                        value="${doc.dateDerniereModif}"/></td>
                    <c:if test="${not empty doc.tag}">
                        <c:set var="tags" value="${fn:split(doc.tag, ' ')}"/>
                        <td>
                            <c:forEach items="${tags}" var="tg">
                                <a href="/?tags=${tg}"><span class="label label-sm label-success"><c:out
                                        value="${tg}"></c:out></span></a>

                            </c:forEach>
                        </td>
                    </c:if>
                    <c:if test="${empty doc.tag}">
                        <td></td>
                    </c:if>
                    <td class="hidden-480">
                        <c:if test="${doc.dernierEditeur.id eq utilisateur.id}">
                            <a class="blue" href="/profil">moi</a>
                        </c:if>
                        <c:if test="${doc.dernierEditeur.id ne utilisateur.id}">
                            <a class="blue" href="/profil/${doc.dernierEditeur.id}">${doc.dernierEditeur.nom}</a>
                        </c:if>
                    </td>

                    <td class="hidden-480">
                        <c:if test="${doc.status eq 0}"><span class="label label-sm label-default">Public</span></c:if>
                        <c:if test="${doc.status eq 1}"><span class="label label-sm label-default">Privé</span></c:if>
                        <c:if test="${doc.status eq 2}">Partagé avec:
                            <c:forEach items="${doc.utilisateursAvecDroit}" var="ut">

                                <c:if test="${ut.id eq utilisateur.id}">
                    <span class="label label-sm label-primary"><a class="white"
                                                                  href="/profil">${ut.id eq utilisateur.id? "moi":ut.nom}</a></span>
                                </c:if>
                                <c:if test="${ut.id ne utilisateur.id}">
                    <span class="label label-sm label-primary"><a class="white"
                                                                  href="/profil/${ut.id}">${ut.id eq utilisateur.id? "moi":ut.nom}</a></span>
                                </c:if>

                            </c:forEach>
                        </c:if>

                    </td>

                    <td>
                        <div class="hidden-sm hidden-xs action-buttons">
                            <a class="blue view-doc" data-doc-id="<c:out value="${doc.id}"></c:out>" href="#">
                                <i class="ace-icon fa fa-search-plus bigger-130"></i>
                            </a>
                            <div class="wysiwyg-editor hidden" id="<c:out value="${doc.id}"></c:out>"
                                 contenteditable="true">
                                <c:out value="${doc.dernierContenu}" escapeXml="false"></c:out>
                            </div>
                            <a class="blue" href="/modifier-document?id=<c:out value="${doc.id}"></c:out>">
                                <i class="ace-icon fa fa-pencil bigger-130"></i>
                            </a>
                            <a class="green" href="/historique/<c:out value="${doc.id}"></c:out>">
                                <i class="ace-icon fa fa-check bigger-130"></i>
                            </a>
                        </div>
                    </td>
                    </tr>
                </c:forEach>
            </c:if>


            </tbody>
        </table>

    </div>
</c:if>
<c:if test="${not empty historiques}">
    <div id="timeline" class="">
        <div class="row">
            <div class="col-xs-12 col-sm-10 col-sm-offset-1">
                <div class="timeline-container timeline-style2">
                    <div class="timeline-items">
                        <c:forEach items="${historiques}" var="h">
                            <div class="timeline-item clearfix <c:if test="${not empty h.version and not empty h.document.version and h.version eq h.document.version}">well</c:if>">
                                <div class="timeline-info">
                                <span class="timeline-date"><fmt:formatDate pattern="dd-MM-yyyy HH:mm"
                                                                            value="${h.dateModif}"/></span>
                                    <i class="timeline-indicator btn btn-info no-hover"
                                       <c:if test="${not empty h.version and not empty h.document.version and h.version eq h.document.version}">style="background-color: green !important;" </c:if>></i>
                                </div>

                                <div class="widget-box transparent">
                                    <div class="widget-body">
                                        <div class="widget-main no-padding">
                                        <span class="bigger-110">
                                            <c:if test="${utilisateur.id eq h.editeur.id}">
                                                <a href="/profil" class="red bolder">Moi</a>

                                            </c:if>
                                            <c:if test="${utilisateur.id ne h.editeur.id}">
                                                <a href="/profil/${h.editeur.id}"
                                                   class="purple bolder">${h.editeur.prenom} ${h.editeur.nom}</a>
                                            </c:if>
                                        </span>
                                            <a title="revenir à cette version" data-history="${h.id}"
                                               class="btn btn-warning btn-sm rollback">
                                                <i class="ace-icon fa fa-reply icon-only"></i>
                                            </a>
                                            <br>
                                            <span class="red bolder">${h.version}</span>

                                            <br>
                                            <i class="ace-icon fa fa-hand-o-right grey bigger-125"></i>
                                            <a href="#" class="view-doc" data-doc-id="<c:out value="${h.id}"></c:out>">Voir
                                                cette version …</a>
                                            <div class="wysiwyg-editor hidden" id="<c:out value="${h.id}"></c:out>"
                                                 contenteditable="true">
                                                <c:out value="${h.contenu}" escapeXml="false"></c:out>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                    </div><!-- /.timeline-items -->
                </div><!-- /.timeline-container -->

            </div>
        </div>
    </div>
</c:if>
<%@include file="parts/footer.jsp" %>