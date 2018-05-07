<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>

<div class="page-header">
    <h2 class="blue">
        ${title}
    </h2>
</div>
<div>
    <table id="dynamic-table" class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
            <th>Intitule</th>
            <th>Auteur</th>
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
                <td>
                    <c:if test="${doc.auteur.id eq utilisateur.id}">
                        <a href="/profil">moi</a>
                    </c:if>
                    <c:if test="${doc.auteur.id ne utilisateur.id}">
                        <a href="/profil/${doc.auteur.id}">${doc.auteur.prenom} ${doc.auteur.nom}</a>
                    </c:if>

                </td>
                <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                    value="${doc.datePublixation}"/></td>
                <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm"
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
                        <a class="blue view-doc" title="voir le document"
                           data-doc-id="<c:out value="${doc.id}"></c:out>" href="#">
                            <i class="ace-icon fa fa-search-plus bigger-130"></i>
                        </a>
                        <div class="wysiwyg-editor hidden" id="<c:out value="${doc.id}"></c:out>"
                             contenteditable="true">
                            <c:out value="${doc.dernierContenu}" escapeXml="false"></c:out>
                        </div>

                        <a class="green telecharger" title="télecharger" href="#" data-doc="${doc.id}">
                            <i class="ace-icon fa fa-download bigger-130"></i>
                        </a>
                        <c:if test="${(not empty utilisateur and utilisateur.id eq doc.auteur.id) or (doc.readOnly eq false)}">
                            <a class="blue" title="éditer le document"
                               href="/modifier-document?id=<c:out value="${doc.id}"></c:out>">
                            <i class="ace-icon fa fa-pencil bigger-130"></i>
                        </a>
                        </c:if>
                        <c:if test="${utilisateur.id eq doc.auteur.id}">
                            <a class="red supprimer-doc" title="supprimer le document" data-doc="${doc.id}">
                                <i class="ace-icon fa fa-trash-o bigger-130"></i>
                            </a>
                        </c:if>
                        <c:if test="${title eq 'Mes Favoris'}">
                            <a class="red supprimer-favoris" title="retirer des favoris" data-doc="${doc.id}">
                                <i class="ace-icon fa fa-trash-o bigger-130"></i>
                            </a>
                        </c:if>
                    </div>
                </td>
                </tr>
            </c:forEach>
        </c:if>


        </tbody>
    </table>
</div>
<%@include file="parts/footer.jsp" %>

