jQuery(function ($) {

    var $editeur_text = $('#editor2');

    function showErrorAlert(reason, detail) {
        var msg = '';
        if (reason === 'unsupported-file-type') {
            msg = "Unsupported format " + detail;
        }
        else {
            //console.log("error uploading file", reason, detail);
        }
        $('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>' +
            '<strong>File upload error</strong> ' + msg + ' </div>').prependTo('#alerts');
    }

    if ($editeur_text.length) {
        $editeur_text.ace_wysiwyg({
            toolbar_place: function (toolbar) {
                return $(this).closest('.widget-box')
                    .find('.widget-header').prepend(toolbar)
                    .find('.wysiwyg-toolbar').addClass('inline');
            },
            toolbar:
                [
                    'font',
                    null,
                    'fontSize',
                    null,
                    {name: 'bold', className: 'btn-info', title: 'gras'},
                    {name: 'italic', className: 'btn-info'},
                    {name: 'strikethrough', className: 'btn-info', title: 'barré'},
                    {name: 'underline', className: 'btn-info', title: 'souligné'},
                    null,
                    {name: 'insertunorderedlist', className: 'btn-success', title: 'liste à puce'},
                    {name: 'insertorderedlist', className: 'btn-success', title: 'liste ordonnée'},
                    {name: 'outdent', className: 'btn-purple', title: 'desindenter'},
                    {name: 'indent', className: 'btn-purple', title: 'indenter'},
                    null,
                    {name: 'justifyleft', className: 'btn-primary', title: 'aligner à gauche'},
                    {name: 'justifycenter', className: 'btn-primary', title: 'centrer'},
                    {name: 'justifyright', className: 'btn-primary', title: 'aligner à droite'},
                    {name: 'justifyfull', className: 'btn-inverse', title: 'justifié'},
                    null,
                    {name: 'createLink', className: 'btn-pink', title: 'creer un lien'},
                    {name: 'unlink', className: 'btn-pink', title: 'supprimer un lien'},
                    null,
                    null,
                    'foreColor',
                    null,
                    {name: 'undo', className: 'btn-grey', title: 'annuler'},
                    {name: 'redo', className: 'btn-grey', title: 'refaire'}
                ],
            speech_button: false
        });
    }
    var $creer_document_form = $("#creer_document");
    if ($creer_document_form.length) {
        $('#status').change(function () {
            if (this.value === '2') {
                bootbox.prompt({
                    title: 'Veuillez entrer les adresses emails des utilisateurs',
                    placeholder: 'utilisateur1@email.com,utilisateur2@email.com,...',
                    callback: function (result) {
                        if (result === null) {

                        } else {
                            console.log(result);
                            var users = result.split(',');
                            $tags_col = $('#utilisateurs_emails .tags-col');
                            $tags_col.html("");
                            $tags = $('<div>', {class: 'tags'});
                            for (var i = 0; i < users.length; i++) {
                                console.log(/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,3})$/.test(users[i]));
                                if (/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,3})$/.test(users[i])) {
                                    $tag_container = $('<div>');
                                    $button_close = $('<button>', {type: 'button', class: 'close'});
                                    $button_close.html('x');
                                    $button_close.on('click', function () {
                                        $(this).parent().parent().remove();
                                        if ($tags.children().length == 0) {
                                            $tags.remove()
                                            $('#utilisateurs_emails').slideUp();
                                            $('#status').val('public');
                                        }
                                    });
                                    $span_tag = $('<span>', {class: 'tag'}).html(users[i]);
                                    $span_tag.append($button_close);
                                    $tag_container.append($('<input>', {
                                        type: 'hidden',
                                        name: 'utilisateurs[]'
                                    }).val(users[i]));
                                    $tag_container.append($span_tag);
                                    $tags.append($tag_container);
                                } else {
                                    $('.bootbox-form').append($('<p>', {class: 'alert alert-danger'}).html('Veuillez entrer des emails valides!\n Example1@domaine.com,Example2@domaine.com,Example3@domaine.fr... ').css('margin-top', '10px'))
                                    return false;
                                }
                            }
                            $tags_col.append($tags);
                            $('#utilisateurs_emails').slideDown();


                        }
                    }
                });

            }

        });

        $creer_document_form.find('.reset').on('click', function () {
            $('#utilisateurs_emails').slideUp();
            $('.inline.tags-col').children().remove();
        })
    }
    var $modifier_document = $('#modifier_document');
    if ($modifier_document.length) {
        $modifier_document.on('submit', function (event) {
            event.preventDefault();
            var $contenu = $('<textarea>', {name: 'contenu'});
            $contenu.css('display', 'none');
            $contenu.html($('#editor2').html());
            $(this).append($contenu);
            $(this)[0].submit();
        });
    }
    if ($('#dynamic-table').length) {
        $('#dynamic-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": false,
        });

//        $('#dynamic-table').DataTable( {
//            paging: true
//        } );
    }
    var $view_doc = $(".view-doc");
    if($view_doc.length) {
        $view_doc.on('click', function () {
            var $element = $("#" + $(this).data('doc-id'));
            if ($element.length) {
                bootbox.dialog({
                    title: $("#titre"+$(this).data('doc-id')).html(),
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
    }
})
;