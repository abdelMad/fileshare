<!-- PAGE CONTENT ENDS -->
</div><!-- /.col -->
</div><!-- /.row -->
</div><!-- /.page-content -->
</div>
</div><!-- /.main-content -->


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
<%= Util.includeCssOrJs(request,"/nouveau-document|/modifier-document","/assets/js/bootstrap-wysiwyg.min.js")%>
<%= Util.includeCssOrJs(request,"/documents-partages|/mes-documents","/assets/js/jquery.dataTables.min.js")%>
<script src="/assets/js/bootbox.min.js"></script>
<!-- ace scripts -->
<script src="/assets/js/ace-elements.min.js"></script>
<script src="/assets/js/ace.min.js"></script>
<script src="/assets/js/script.js"></script>

<!-- inline scripts related to this page -->
</body>
</html>
