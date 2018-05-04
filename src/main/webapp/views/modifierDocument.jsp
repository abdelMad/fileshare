<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>
<div class="row nouveau-document">
    <div class="col-sm-12">
        <h2 class="header blue">Modifier Document
            <c:if test="${estFavoris eq false}">
                <i class="star-off-png favoris" data-est-favoris="${estFavoris}" id="modif-favoris"
                   title="Ajouter au favoris"></i>
            </c:if>
            <c:if test="${estFavoris eq true}">
                <i class="star-on-png favoris" data-est-favoris="${estFavoris}" id="modif-favoris"
                   title="Retirer des favoris"></i>
            </c:if>
        </h2>
        <form class="form-horizontal doc-form" id="modifier_document" action="" method="post">
            <c:if test="${document.auteur.id != utilisateur.id}">
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="Intitule"> Titre du document: </label>

                    <div class="col-xs-10 col-sm-5">
                        <label class="control-label"><b>${document.intitule}</b></label>
                    </div>
                </div>
            </c:if>
            <c:if test="${document.auteur.id eq utilisateur.id}">
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="Intitule"> Titre du document </label>

                    <div class="col-xs-10 col-sm-5">
                        <input type="text" name="intitule" value="${document.intitule}" id="Intitule" placeholder=""
                               class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="description"> Description </label>

                    <div class="col-xs-10 col-sm-5">
                    <textarea id="description" name="description"
                              class="autosize-transition form-control" rows="5">${document.description}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="tags"> Tags </label>

                    <div class="col-xs-10 col-sm-5">
                        <input type="text" name="tags" value="${document.tag}" id="tags"
                               placeholder="#java #jsp #jstl ..."
                               class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right" for="status"> Qui peut voir ce
                        fichier? </label>

                    <div class="col-xs-10 col-sm-5">
                        <select class="form-control" name="status" id="status">
                            <option value="0"
                                    <c:if test="${document.status eq 0}"><c:out value="selected"></c:out></c:if>>Public
                            </option>
                            <option value="1"
                                    <c:if test="${document.status eq 1}"><c:out value="selected"></c:out></c:if> >just
                                moi
                            </option>
                            <option value="2"
                                    <c:if test="${document.status eq 2}"><c:out value="selected"></c:out></c:if> >des
                                utilisateurs
                            </option>
                        </select>
                    </div>
                </div>
                <div class="form-group"
                     <c:if test="${document.status != 2}">style="display: none"</c:if> id="utilisateurs_emails">
                    <label class="col-sm-3 control-label no-padding-right">Les utilisateurs</label>
                    <div class="col-sm-9">
                        <div class="inline tags-col">
                            <div class="tags">
                                <c:if test="${not empty document.utilisateursAvecDroit}">
                                    <c:forEach items="${document.utilisateursAvecDroit}" var="u">
                                        <div>
                                            <input hidden value="<c:out value="${u.email}"></c:out>"
                                                   name="utilisateurs[]">
                                            <span class="tag"><c:out value="${u.email}"></c:out>
                            <button type="button" class="close">×</button>
                            </span>
                                        </div>
                                    </c:forEach>
                                </c:if>

                            </div>

                        </div>
                    </div>
                </div>
                <div class="clearfix form-actions">
                    <div class="col-md-offset-3 col-md-9">
                        <input type="hidden" name="doc_id" value="${doc_id}">
                        <button class="btn btn-success" type="submit">
                            <i class="ace-icon fa fa-floppy-o bigger-125"></i>
                            Modifier
                        </button>
                    </div>
                </div>
            </c:if>
            <div class="editors well">
                <div class="title"><p class="lead">Actuellement editeurs</p></div>
                <div class="list-users">

                </div>
            </div>
            <div class="widget-box widget-color-green">
                <div class="widget-header widget-header-small center"></div>
                <div class="widget-body">
                    <div class="widget-main no-padding">
                        <div class="wysiwyg-editor" data-utilisateur="${utilisateur.id}" data-document="${document.id}"
                             id="document-text"><c:if test="${not empty document.dernierContenu}"><c:out
                                value="${document.dernierContenu}" escapeXml="false"></c:out></c:if></div>
                    </div>
                </div>
            </div>

        </form>
    </div>

</div>
<%@include file="parts/footer.jsp" %>