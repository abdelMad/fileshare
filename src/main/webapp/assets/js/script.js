jQuery(function ($) {
    var $editeur_text = $('#document-text');
    var checkEventOnTags = false;
    var openedConversation = -1;

    /**
     * validation functions
     */
    function errorPlacement(error, element) {
        // Add the `help-block` class to the error element
        error.addClass("help-block");

        if (element.prop("type") === "checkbox") {
            error.insertAfter(element.parent("label"));
        } else {
            error.insertAfter(element);
        }
    }

    function highlight(element, errorClass, validClass) {
        $(element).parents(".form-group").addClass("has-error").removeClass("has-success");
    }

    jQuery.validator.addMethod("tags", function (value, element) {
        var checked = true;
        if (value.length) {
            var tags = new RegExp('^#[\\w\\d]+$|^#[\\w\\d]+( #[\\w\\d]+)*$');
            checked = tags.test(value);
        }
        return checked;
    }, jQuery.validator.format("Veuillez entrer un tag valide (#example1 #exampl2 ...)"));

    function unhighlight(element, errorClass, validClass) {
        $(element).parents(".form-group").addClass("has-success").removeClass("has-error");
    }

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

    /**
     * Creation et modification document
     */
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
    var $modifier_document = $('#modifier_document');
    if ($creer_document_form.length || $modifier_document.length) {
        $('.doc-form').validate({
            rules: {
                intitule: {
                    required: true
                },
                tags: {
                    tags: true
                }
            },
            messages: {
                intitule: "Veuillez saisir l'intitulé du document",
            },
            errorElement: "em",
            errorPlacement: errorPlacement,
            highlight: highlight,
            unhighlight: unhighlight
        });
        $('#status').change(function () {
            if (this.value === '2') {
                var $tags = $('.tags');
                $tags.html("");

                getEmailsAddAndEditDocs();
                if (!checkEventOnTags) {
                    $tags.on('click', function () {
                        getEmailsAddAndEditDocs();
                    });
                    checkEventOnTags = true;
                }
            } else {
                $('#utilisateurs_emails').slideUp();
                $('.inline.tags-col').children().remove();
            }

        });
        $creer_document_form.find('.reset').on('click', function () {
            $('#utilisateurs_emails').slideUp();
            $('.inline.tags-col').children().remove();
        });

        var $tags = $('#tags');
        if ($tags.length && $tags.val().length) {

        }
    }

    if ($modifier_document.length) {

        $modifier_document.on('submit', function (event) {
            event.preventDefault();
            var $contenu = $('<textarea>', {name: 'contenu'});
            $contenu.css('display', 'none');
            $contenu.html($('#document-text').html());
            $(this).append($contenu);
            if ($modifier_document.valid())
                $(this)[0].submit();
        });
    }

    function getEmailsAddAndEditDocs() {
        bootbox.prompt({
            title: 'Veuillez entrer les adresses emails des utilisateurs',
            placeholder: 'utilisateur1@email.com utilisateur2@email.com ...',
            callback: function (result) {
                if (result === null) {
                    $('#status').val(0);
                } else {
                    var users = result.split(' ');
                    $tags = $('#utilisateurs_emails .tags');

                    for (var i = 0; i < users.length; i++) {
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
                            if (!$('#erreur_mail').length)
                                $('.bootbox-form').append($('<p>', {
                                    class: 'alert alert-danger',
                                    id: 'erreur_mail'
                                }).html('Veuillez entrer des emails valides!\n Example1@domaine.com Example2@domaine.com Example3@domaine.fr ... ').css('margin-top', '10px'))
                            return false;
                        }
                    }
                    $('#utilisateurs_emails').slideDown();


                }
            }
        });
    }

    $('.favoris').hover(function () {
        $(this).attr('class', 'star-on-png favoris')
    }, function () {
        $(this).attr('class', 'star-off-png favoris')
    });
    $('#ajouter-favoris').click(function () {
        if ($(this).data('status') === undefined || $(this).data('status') == 'true')
            $(this).data('status', 'false');
        else if ($(this).data('status') === undefined || $(this).data('status') == 'false')
            $(this).data('status', 'true');

        console.log($(this).data('status'))
        // $.ajax({
        //     method: 'POST',
        //     url: '/ajouter-au-favoris',
        //     data: {docId: docId,status: status},
        //     dataType: 'json',
        //     success: function (data) {
        //         if(data.length && data[0]!=='error'){
        //             $('#ajouter-favoris').attr('class','star-off-png favoris');
        //         }
        //     }
        // });
    });
    /**
     * end Creation et modification document
     */
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
    if ($view_doc.length) {
        $view_doc.on('click', function () {
            var $element = $("#" + $(this).data('doc-id'));
            if ($element.length) {
                bootbox.dialog({
                    title: $("#titre" + $(this).data('doc-id')).html(),
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

    /**
     *
     *  partie chat
     */


    $('.minim-chat-window').click(function () {
        $('.panel-body.msg_container_base').slideToggle(100);
    });
    $('.close-chat-window').click(function () {
        $(this).parent().parent().parent().parent().parent().remove();
    });

    /**
     *
     *  end partie chat
     */
    /**
     * Partie modification document partagé
     */
    if ($editeur_text.length) {
        var idDoc = $editeur_text.data('document');
        var docClient = new WebSocket("ws://" + location.host + "/document-modif/" + idDoc);
        console.log(idDoc);
        docClient.onmessage = function (evt) {

            var doc = JSON.parse(evt.data);
            console.log(doc);
            $editeur_text.html(doc.txt);


        };
        $editeur_text.wysiwyg('document').keyup(function (e) {
            var doc = {
                idDoc: idDoc.toString(),
                idU: $editeur_text.data('utilisateur').toString(),
                txt: $editeur_text.html()
            };
            var jsonDoc = JSON.stringify(doc);
            console.log(jsonDoc);
            docClient.send(jsonDoc);
        });

    }


})
;