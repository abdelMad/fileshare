<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>
<div class="row nouveau-document">
    <div class="col-sm-12">
        <h2 class="header blue">Nouveau Document</h2>
        <form class="form-horizontal" id="modifier_document" action="" method="post">
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
                    <input type="text" name="tags" value="${document.tag}" id="tags" placeholder="#java #jsp #jstl ..."
                           class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="status"> Qui peut voir ce fichier? </label>

                <div class="col-xs-10 col-sm-5">
                    <select class="form-control" name="status" id="status">
                        <option value="0"
                        <c:if test="${document.status eq 0}"><c:out value="selected"></c:out></c:if>>Public</option>
                        <option value="1"
                        <c:if test="${document.status eq 1}"><c:out value="selected"></c:out></c:if> >just moi</option>
                        <option value="2"
                        <c:if test="${document.status eq 2}"><c:out value="selected"></c:out></c:if> >des utilisateurs</option>
                    </select>
                </div>
            </div>
            <div class="form-group" <c:if test="${document.status != 2}"><c:out value="style=\"display: none\""></c:out></c:if> id="utilisateurs_emails">
                <label class="col-sm-3 control-label no-padding-right">Les utilisateurs</label>
                <div class="col-sm-9">
                    <div class="inline tags-col">
                        <div class="tags">
                            <c:if test="${not empty document.utilisateursAvecDroit}">
                                <c:forEach items="${document.utilisateursAvecDroit}" var="u">
                                    <div>
                                        <input hidden value="<c:out value="${u.email}"></c:out>" name="utilisateurs[]">
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

            <div class="widget-box widget-color-green">
                <div class="widget-header widget-header-small center"></div>
                <div class="widget-body">
                    <div class="widget-main no-padding">
                        <div class="wysiwyg-editor" id="editor2"><c:if test="${not empty document.contenu}"><c:out value="${document.contenu}" escapeXml="false"></c:out></c:if></div>
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
        </form>
    </div>

</div>
<%@include file="parts/footer.jsp" %>