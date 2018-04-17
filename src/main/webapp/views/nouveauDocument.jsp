<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp" %>
<div class="row nouveau-document">
    <div class="col-sm-12">
        <h2 class="header blue">Nouveau Document</h2>
        <form class="form-horizontal" id="creer_document" action="" method="post">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="Intitule"> Titre du document </label>

                <div class="col-xs-10 col-sm-5">
                    <input type="text" name="intitule" id="Intitule" placeholder="" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="description"> Description </label>

                <div class="col-xs-10 col-sm-5">
                    <textarea id="description" name="description" class="autosize-transition form-control" rows="5"></textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="tags"> Tags </label>

                <div class="col-xs-10 col-sm-5">
                    <input type="text" name="tags" id="tags" placeholder="#java #jsp #jstl ..."
                           class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right" for="status"> Qui peut voir ce fichier? </label>

                <div class="col-xs-10 col-sm-5">
                    <select class="form-control" name="status" id="status">
                        <option value="0" selected>Public</option>
                        <option value="1">just moi</option>
                        <option value="2">des utilisateurs</option>
                    </select>
                </div>
            </div>
            <div class="form-group" style="display: none" id="utilisateurs_emails">
                <label class="col-sm-3 control-label no-padding-right">Les utilisateurs</label>
                <div class="col-sm-9">
                    <div class="inline tags-col">
                        <div class="tags">

                        </div>

                    </div>
                </div>
            </div>
            <div class="clearfix form-actions">
                <div class="col-md-offset-3 col-md-9">
                    <button class="btn btn-success" type="submit">
                        <i class="ace-icon fa fa-floppy-o bigger-125"></i>
                        Creer et editer
                    </button>

                    &nbsp; &nbsp; &nbsp;
                    <button class="btn reset"  type="reset">
                        <i class="ace-icon fa fa-undo bigger-110"></i>
                        Reset
                    </button>
                </div>
            </div>
        </form>
        <%--<div class="widget-box widget-color-green">--%>
            <%--<div class="widget-header widget-header-small center"></div>--%>

            <%--<div class="widget-body">--%>
                <%--<div class="widget-main no-padding">--%>
                    <%--<div class="wysiwyg-editor" id="editor2"></div>--%>
                <%--</div>--%>

                <%--<div class="widget-toolbox padding-4 clearfix">--%>


                    <%--<div class="btn-group pull-right">--%>
                        <%--<button class="btn btn-sm btn-success btn-white btn-round">--%>
                            <%--<i class="ace-icon fa fa-floppy-o bigger-125"></i>--%>
                            <%--Sauvegarder--%>
                        <%--</button>--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    </div>

</div>
<%@include file="parts/footer.jsp" %>