<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/head.jsp"%>

<div class="page-header">
    <h2 class="blue">
        Mes documents
    </h2>
</div>
<div>
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
        <tr>
            <td>
               <c:out value="${doc.intitule}"></c:out>
            </td>
            <td><c:out value="${doc.datePublixation}"></c:out></td>
            <td><c:out value="${doc.dateDerniereModif}"></c:out></td>
            <td><span class="label label-sm label-default"><c:out value="${doc.tag}"></c:out></span></td>
            <td class="hidden-480"><c:out value="${doc.dernierEditeur.email}"></c:out></td>

            <td class="hidden-480">
                <span class="label label-sm label-default"><c:out value="${doc.status}"></c:out></span>
            </td>

            <td>
                <div class="hidden-sm hidden-xs action-buttons">
                    <a class="blue view-doc" data-doc-id="<c:out value="${doc.id}"></c:out>" href="#">
                        <i class="ace-icon fa fa-search-plus bigger-130"></i>
                    </a>
                    <div class="wysiwyg-editor hidden" id="<c:out value="${doc.id}"></c:out>"   contenteditable="true">
                        <c:out value="${doc.dernierContenu}" escapeXml="false"></c:out>
                    </div>

                    <a class="green" href="/telecharger-document?id=<c:out value="${doc.id}"></c:out>">
                        <i class="ace-icon fa fa-cloud-download bigger-130"></i>
                    </a>

                    <a class="blue" href="/modifier-document?id=<c:out value="${doc.id}"></c:out>">
                        <i class="ace-icon fa fa-pencil bigger-130"></i>
                    </a>

                    <a class="red" href="/supprimer-document?id=<c:out value="${doc.id}"></c:out>">
                        <i class="ace-icon fa fa-trash-o bigger-130"></i>
                    </a>
                </div>
            </td>
        </tr>
    </c:forEach>
    </c:if>


        </tbody>
    </table>
</div>
<%@include file="parts/footer.jsp"%>
<script type="text/javascript">
    jQuery(function($) {
        //initiate dataTables plugin
            $('#dynamic-table').dataTable( {
                "bPaginate": true,
                "bLengthChange": false,
                "bFilter": false,
                "bInfo": false,
                } );

//        $('#dynamic-table').DataTable( {
//            paging: true
//        } );

        $(".view-doc").on('click', function() {
            var $element = $("#" + $(this).data('doc-id'));
            console.log('hihi');
            if ($element.length) {
                bootbox.dialog({
                    message: $element.html(),
                    buttons:
                        {

                            "button":
                                {
                                    "label": "Fermer",
                                    "className": "btn-sm"
                                }
                        }
                });
            }
        });


    })
</script>

