<!-- PAGE CONTENT ENDS -->
</div><!-- /.col -->
</div><!-- /.row -->
</div><!-- /.page-content -->
</div>
</div><!-- /.main-content -->

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
<%= Util.includeCssOrJs(request, "/nouveau-document|/modifier-document|/messages|/profil", "/assets/js/bootstrap-wysiwyg.min.js")%>
<%= Util.includeCssOrJs(request,"/documents-partages|/mes-documents","/assets/js/jquery.dataTables.min.js")%>
<script src="assets/js/bootstrap-tag.min.js"></script>
<script src="assets/js/jquery.hotkeys.min.js"></script>
<script src="/assets/js/bootbox.min.js"></script>
<!-- ace scripts -->
<%= Util.includeCssOrJs(request, "/profil", "/assets/js/jquery.gritter.min.js")%>
<%= Util.includeCssOrJs(request, "/profil", "/assets/js/bootstrap-editable.min.js")%>
<%= Util.includeCssOrJs(request, "/profil", "/assets/js/ace-editable.min.js")%>
<script src="/assets/js/ace-elements.min.js"></script>
<script src="/assets/js/ace.min.js"></script>
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
