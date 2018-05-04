jQuery(function ($) {
    var $editeur_text = $('#document-text');
    var checkEventOnTags = false;
    var openedConversation = -1;

    function loading() {
        var opts = {
            lines: 9, // The number of lines to draw
            length: 38, // The length of each line
            width: 17, // The line thickness
            radius: 45, // The radius of the inner circle
            scale: 1, // Scales overall size of the spinner
            corners: 1, // Corner roundness (0..1)
            color: '#ffffff', // CSS color or array of colors
            fadeColor: 'transparent', // CSS color or array of colors
            opacity: 0.25, // Opacity of the lines
            rotate: 0, // The rotation offset
            direction: 1, // 1: clockwise, -1: counterclockwise
            speed: 1, // Rounds per second
            trail: 60, // Afterglow percentage
            fps: 20, // Frames per second when using setTimeout() as a fallback in IE 9
            zIndex: 2e9, // The z-index (defaults to 2000000000)
            className: 'spinner', // The CSS class to assign to the spinner
            top: '50%', // Top position relative to parent
            left: '50%', // Left position relative to parent
            shadow: 'none', // Box-shadow for the lines
            position: 'absolute' // Element positioning
        };

        return new Spinner(opts).spin(document.body);
    }

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

    var $favoris = $('#modif-favoris');
    if ($favoris.length) {
        var firstState = $favoris.attr('class');
        $favoris.hover(function () {
            if ($favoris.attr('class') == 'star-on-png favoris') {
                $(this).attr('class', 'star-off-png favoris')
            } else {
                $(this).attr('class', 'star-on-png favoris')
            }
        }, function () {
            $(this).attr('class', firstState);
        });
        $favoris.click(function () {
            var status = $(this).data('est-favoris');
            var idDoc = $('#document-text').data('document');

            var spinner = loading();
            $.ajax({
                method: 'POST',
                url: '/modifier-favoris',
                data: {idDoc: idDoc},
                dataType: 'json',
                success: function (data) {
                    spinner.stop();
                    console.log(data[0]);

                    if (data.length && data[0] != 'false') {
                        if (data[0] == 'on') {
                            firstState = 'star-on-png favoris';
                            console.log('im here in on clause')
                            $.gritter.add({
                                title: 'Notification',
                                text: 'Le document est ajouté au favoris',
                                class_name: 'gritter-success'
                            });
                        }
                        else {
                            console.log('im here in off clause')
                            $.gritter.add({
                                title: 'Notification',
                                text: 'Le document est bien retiré des favoris',
                                class_name: 'gritter-success'
                            });
                            firstState = 'star-off-png favoris';
                        }

                    }
                }
            });
        });
    }
    /**
     * end Creation et modification document
     */
    if ($('#dynamic-table').length) {
        $('#dynamic-table').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": true,
            "bInfo": false,
            "columnDefs": [
                {"targets": [1, 2, 3, 4, 5, 6], "searchable": false}
            ],
            "language": {
                "search": "Filtrer: ",
                "paginate": {
                    "previous": "précédant",
                    "next": "suivant",
                },
                "emptyTable": "Vous n' avez aucun document",
                "zeroRecords": "Aucun document qui porte cet intitulé"
            }
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


    /**
     *
     *  end partie chat
     */
    /**
     * Partie modification document partagé
     */
    if ($editeur_text.length) {
        var idDoc = $editeur_text.data('document');
        var docClient = new WebSocket("ws://" + location.host + "/document-modif/" + idDoc + "/" + $('#main-container').data('utilisateur'));
        console.log(idDoc);
        docClient.onmessage = function (evt) {

            var doc = JSON.parse(evt.data);
            if (Array.isArray(doc) && doc[0] == 'users') {
                var nbEditeurs = doc.length - 1;
                var title = (nbEditeurs <= 1 ? ' éditeur' : ' éditeurs');
                $('.editors .title').html('<p class="lead">Actuellement ' + nbEditeurs + title + ' en ligne</p>');

                var $listUsers = $('.list-users');
                $listUsers.html('');
                for (var i = 1; i < doc.length; i++) {
                    var docObject = JSON.parse(doc[i]);
                    var $span = $('<span class="label label-sm label-success">');
                    if ($('#main-container').data('utilisateur').toString() == docObject.senderId.toString()) {
                        var $a = $('<a>', {href: '/profil'});
                        $span.html("moi");

                    } else {
                        var $a = $('<a>', {href: '/profil/' + docObject.senderId});
                        $span.html(docObject.sender);
                    }

                    $a.append($span);
                    $listUsers.append($a);
                }


            }
            else
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
    /**
     * gestion historique
     */
    var $rollBack = $('.rollback');
    if ($rollBack.length) {
        $rollBack.on('click', function (e) {
            e.preventDefault();
            var idH = $(this).data('history');
            bootbox.confirm("Etes vous sûr de vouloir revenir à cette version?", function (result) {
                if (result) {
                    $.ajax({
                        url: location.href,
                        method: 'post',
                        data: {idH: idH},
                        dataType: 'json',
                        success: function (data) {
                            if (data.length && data[0] == 'true') {
                                $.gritter.add({
                                    title: '',
                                    text: 'le retour en arriere est effectué avec succès!',
                                    class_name: 'gritter-success'
                                });
                                location.reload();
                            } else if (data[0] == 'false') {
                                $.gritter.add({
                                    title: 'This is a warning notification',
                                    text: 'Une erreur est survenue veuillez réessayer',
                                    class_name: 'gritter-error'
                                });
                            }
                        }
                    });
                }
            });

        })
    }

})
;