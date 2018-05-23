<%@ page import="fr.fileshare.utilities.Util" %>
<%@ page import="fr.fileshare.dao.UtilisateurHandler" %>
</div><!-- /.col -->

<!-- PAGE CONTENT ENDS -->
</div><!-- /.row -->
</div><!-- /.page-content -->
</div>
</div><!-- /.main-content -->
<div class="chat-windows-container">

</div>

<div class="footer">
    <div class="footer-inner">
        <c:set var="now" value="<%= new java.util.Date()%>"/>
        <div class="footer-content <c:if test="${not empty utilisateur}">col-md-8</c:if>">
						<span class="bigger-120">
							<span class="blue bolder">File</span>
							Share &copy; <fmt:formatDate pattern="yyyy"
														 value="${now}"/>
						</span>

            &nbsp; &nbsp;
            <span class="action-buttons">
							<a href="#">
								<i class="ace-icon fa fa-twitter-square light-blue bigger-150"></i>
							</a>

							<a href="#">
								<i class="ace-icon fa fa-facebook-square text-primary bigger-150"></i>
							</a>

							<a href="#">
								<i class="ace-icon fa fa-rss-square orange bigger-150"></i>
							</a>
						</span>
        </div>
    </div>
</div>

<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
    <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>

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
    window.jQuery || document.write("<script src='assets/js/jquery.min.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
window.jQuery || document.write("<script src='assets/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="/assets/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<%= Util.includeCssOrJs(request, "/nouveau-document|/modifier-document", "/assets/js/jquery-ui.custom.min.js")%>
<%= Util.includeCssOrJs(request, "/nouveau-document|/modifier-document", "/assets/js/jquery.ui.touch-punch.min.js")%>
<%= Util.includeCssOrJs(request, "/nouveau-document|/modifier-document", "/assets/js/jquery.hotkeys.min.js")%>
<%= Util.includeCssOrJs(request, "/nouveau-document|/modifier-document|/messages|/profil", "/assets/js/bootstrap-wysiwyg.min.js")%>
<%= Util.includeCssOrJs(request, "/documents-favoris|/mes-documents|/historique", "/assets/js/jquery.dataTables.min.js")%>
<%= Util.includeCssOrJs(request, "/documents-favoris|/mes-documents|/historique", "/assets/js/jquery.dataTables.bootstrap.min.js")%>
<%= Util.includeCssOrJs(request, "/documents-favoris|/mes-documents|/historique", "/assets/js/dataTables.tableTools.min.js")%>
<%= Util.includeCssOrJs(request, "/documents-favoris|/mes-documents|/historique", "/assets/js/dataTables.colVis.min.js")%>


<script src="/assets/js/bootstrap-tag.min.js"></script>
<script src="/assets/js/jquery.gritter.min.js"></script>
<script src="/assets/js/spin.min.js"></script>

<script src="/assets/js/bootbox.min.js"></script>
<script src="/assets/js/jquery.validate.min.js"></script>
<!-- ace scripts -->
<%= Util.includeCssOrJs(request, "/profil", "/assets/js/bootstrap-editable.min.js")%>
<%= Util.includeCssOrJs(request, "/profil", "/assets/js/ace-editable.min.js")%>
<script src="/assets/js/ace-elements.min.js"></script>
<script src="/assets/js/ace.min.js"></script>

<script src="/assets/js/script.js"></script>
<c:if test="${not empty utilisateur}">
    <script src="/assets/js/loggedinscript.js"></script>
</c:if>
</body>
</html>
