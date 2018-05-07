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
        <div class="well">
            <p>Le contenu du document s'enregistre automatiquement! amusez vous bien</p>
        </div>
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
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-10">
                        <div class="checkbox">
                            <label>
                                <input name="lectureS" type="checkbox" class="ace"
                                       <c:if test="${document.readOnly eq true}">checked</c:if> >
                                <span class="lbl"> Mode lecture seule</span>
                            </label>
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
            <div class="row well">
                <div class="col-sm-8 mb-20">
                    <div class="editors ">
                        <div class="title"><p class="lead">Actuellement editeurs</p></div>
                        <div class="list-users">

                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <button class="btn btn-primary" id="show-conversation">Conversation de ce groupe</button>
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
<div class="row chat-window col-xs-6 col-sm-5 col-md-3" data-msg-type="groupe" id="conversation-group"
     data-doc="${document.id}" style="display:none;margin-left: 10px; right: 10px;">
    <div class="chat-col col-xs-12 col-md-12">
        <div class="chat-panel panel panel-default">
            <div class="panel-heading top-bar chat-top-bar">
                <div class="chat-col col-md-8 col-xs-8"><h3 class="panel-title"><span
                        class="glyphicon glyphicon-comment"></span>Groupe de ${document.intitule}</h3></div>
                <div class="chat-col col-md-4 col-xs-4" style="text-align: right;"><a href="#"
                                                                                      class="minim-chat-window"><span
                        id="minim_chat_window" class="glyphicon glyphicon-minus icon_minim"></span></a><a href="#"
                                                                                                          class="close-chat-window"><span
                        class="glyphicon glyphicon-remove icon_close" data-id="chat_window_1"></span></a></div>
            </div>
            <div class="panel-body msg_container_base" id="contact-1">
                <c:if test="${not empty grpMessages}">
                    <c:forEach var="m" items="${grpMessages}">
                        <c:set var="image" scope="page" value="/assets/images/people.png"/>
                        <c:if test="${not empty m.emetteur.image}">
                            <c:set var="image" scope="page" value="${m.emetteur.image}"/>
                        </c:if>
                        <c:if test="${m.emetteur.id ne utilisateur.id}">
                            <div class="row msg_container base_receive">
                                <div class="chat-col col-md-2 col-xs-2 avatar chat-avatar"><a
                                        href="/profil/${m.emetteur.id}"><img
                                        class=" chat-img img-responsive img-chat" src="${image}"></a></div>
                                <div class="chat-col col-md-10 col-xs-10">
                                    <div class="messages msg_receive"><p>${m.text}</p>
                                        <time>${m.emetteur.nom} <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                                                                value="${m.date}"/></time>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${m.emetteur.id eq utilisateur.id}">
                            <div class="row msg_container base_sent">
                                <div class="chat-col col-md-10 col-xs-10">
                                    <div class="messages msg_sent"><p>${m.text}</p>
                                        <time>moi <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                                                  value="${m.date}"/></time>
                                    </div>
                                </div>
                                <div class="chat-col col-md-2 col-xs-2 avatar chat-avatar"><a href="/profil"><img
                                        class="chat-img img-responsive img-chat"
                                        src="${image}"></a></div>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:if>
            </div>

            <div class="panel-footer">
                <div class="input-group"><input id="btn-input" type="text" class="form-control input-sm chat_input"
                                                placeholder="Entrez votre message ..."><span class="input-group-btn"><button
                        class="btn btn-primary btn-sm" id="btn-send-grp">Envoyer</button></span></div>
            </div>
        </div>
    </div>
</div>

<%@include file="parts/footer.jsp" %>
