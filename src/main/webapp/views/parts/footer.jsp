<%@ page import="fr.fileshare.utilities.Util" %>
<%@ page import="fr.fileshare.dao.UtilisateurHandler" %>
</div><!-- /.col -->
<c:if test="${not empty utilisateur}">
	<%@include file="chatSideBar.jsp" %>
</c:if>
<!-- PAGE CONTENT ENDS -->
</div><!-- /.row -->
</div><!-- /.page-content -->
</div>
</div><!-- /.main-content -->
<div class="chat-windows-container">

</div>

<div class="footer">
	<div class="footer-inner">
		<c:set var = "now" value = "<%= new java.util.Date()%>" />
		<div class="footer-content">
						<span class="bigger-120">
							<span class="blue bolder">File</span>
							Share &copy; <fmt:formatDate pattern = "yyyy"
														 value = "${now}" />
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
    window.jQuery || document.write("<script src='assets/js/jquery.min.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
window.jQuery || document.write("<script src='assets/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>
<script src="/assets/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<%= Util.includeCssOrJs(request,"/nouveau-document|/modifier-document","/assets/js/jquery-ui.custom.min.js")%>
<%= Util.includeCssOrJs(request,"/nouveau-document|/modifier-document","/assets/js/jquery.ui.touch-punch.min.js")%>
<%= Util.includeCssOrJs(request,"/nouveau-document|/modifier-document","/assets/js/jquery.hotkeys.min.js")%>
<%= Util.includeCssOrJs(request,"/nouveau-document|/modifier-document|/messages","/assets/js/bootstrap-wysiwyg.min.js")%>
<%= Util.includeCssOrJs(request, "/documents-favoris|/mes-documents", "/assets/js/jquery.dataTables.min.js")%>
<script src="/assets/js/bootstrap-tag.min.js"></script>
<script src="/assets/js/bootbox.min.js"></script>
<script src="/assets/js/jquery.validate.min.js"></script>
<!-- ace scripts -->
<script src="/assets/js/ace-elements.min.js"></script>
<script src="/assets/js/ace.min.js"></script>
<% if (UtilisateurHandler.isLoggedIn(request)) {%>
<script src="/assets/js/loggedinscript.js"></script>
<%}%>
<script src="/assets/js/script.js"></script>
<audio id="notif" class="hidden">
	<source src="/assets/sons/notif.wav" type="audio/wav">
	Your browser does not support the audio element.
</audio>
<%--<embed src="/assets/sons/notif.wav" autostart="false" width="0" height="0" id="sound1"--%>
<%--enablejavascript="true"/>--%>
<!-- inline scripts related to this page -->
</body>
</html>
