jQuery(function ($) {

    var $editeur_text = $('#editor2');
    var checkEventOnTags = false;
    var openedConversation = -1;
    var last_gritter;
    var checkValue = false;

    function showNotif(title, text, type, clearGritter) {
        if (clearGritter) {
            if (last_gritter) $.gritter.remove(last_gritter);
        }
        last_gritter = $.gritter.add({
            title: title,
            text: text,
            class_name: 'gritter-' + type + ' gritter-right'
        });
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

    if ($creer_document_form.length || $modifier_document.length) {
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
        })
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

    function construirChatHistory(data) {

        //entete
        var $entete = $('.chat-header');
        $entete.html("");
        var $recepteurImage = $('<img>', {class: 'contact-img', src: data[1], alt: 'avatar'});
        var $chatAbout = $('<div>', {class: 'chat-about'});
        var $chatWith = $('<div>', {class: 'chat-with'});
        $chatWith.html(data[0]);
        $chatAbout.append($chatWith);
        $entete.append($recepteurImage);
        $entete.append($chatAbout);

        var $chatHistory = $('.chat-history');
        $chatHistory.html("");
        var $chatContainer = $('<ul>');
        for (var i = 2; i < data.length; i++) {
            //emetteur
            if (data[i].emetteur == 'moi') {
                var $emetteurMarkup;
                $emetteurMarkup = $('<li>', {class: 'clearfix'});
                var $emMsgData = $('<div>', {class: 'message-data align-right'});
                var $emMsgDataTime = $('<span>', {class: 'message-data-time'});
                $emMsgDataTime.html(data[i].date);
                var $emMsgDataName = $('<span>', {class: 'message-data-name'});
                $emMsgDataName.html(' Moi');
                var $iconeMe = $('<i>', {class: "fa fa-circle me"});
                $emMsgData.append($emMsgDataTime);
                $emMsgData.append($emMsgDataName);
                $emMsgData.append($iconeMe);
                $emetteurMarkup.append($emMsgData);
                var $emMessage = $('<div>', {class: 'message other-message float-right'});
                $emMessage.html(data[i].txt);
                $emetteurMarkup.append($emMessage);
                $chatContainer.append($emetteurMarkup);
            } else {
                //recepteur

                var $recepteurMarkup;
                $recepteurMarkup = $('<li>');
                var $recMsgData = $('<div>', {class: 'message-data'});
                var $recMsgDataName = $('<span>', {class: 'message-data-name'});
                var $recIcone = $('<i>', {class: "fa fa-circle online"});
                $recMsgData.append($recIcone);
                $recMsgDataName.html(data[i].emetteur);
                var $recMsgDataTime = $('<span>', {class: 'message-data-time'});
                $recMsgDataTime.html(data[i].date);
                $recMsgData.append($recMsgDataName);
                $recMsgData.append($recMsgDataTime);
                var $recmessage = $('<div>', {class: 'message my-message'});
                $recmessage.html(data[i].txt);
                $recepteurMarkup.append($recMsgData);
                $recepteurMarkup.append($recmessage);
                $chatContainer.append($recepteurMarkup);
            }
            $chatHistory.append($chatContainer);
            $chatHistory.scrollTop($chatHistory.prop("scrollHeight"));
        }
    }

    function contruireContactRow(data) {
        var $peopleList = $('.people-list .list');
        var $contactRow = $('<li>', {class: 'clearfix contact-row'});
        $contactRow.data('contact-id', data.id);
        $contactRow.on('click', function () {
            var contactId = $(this).data('contact-id');
            console.log(contactId);
            $.ajax({
                method: 'GET',
                url: '/afficher-messages',
                data: {contactId: contactId},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    $('.chat-message').data('contact-id', contactId);
                    openedConversation = contactId;
                    construirChatHistory(data);

                }
            });
        });
        var $contactImg = $('<img>', {
            class: 'contact-img',
            src: data.image,
            alt: 'image de' + data.nomComplet
        });
        var $about = $('<div>', {class: 'about'});
        var $contactName = $('<div>', {class: 'name'});
        $contactName.append(data.nomComplet);
        var $status = $('<div>', {class: 'status green'});
        // var $statusIcone = $('<i>', {class: 'fa fa-circle online'});
        // $status.append($statusIcone);
        if (data.aEnvoyeMsg == 'true')
            $status.html('Nouveau Message');
        $about.append($contactName);
        $about.append($status);
        $contactRow.append($contactImg);
        $contactRow.append($about);
        $peopleList.append($contactRow)
    }

    var $contactRow = $('.contact-row');
    if ($contactRow.length) {
        $contactRow.on('click', function () {
            var contactId = $(this).data('contact-id');
            console.log(contactId);
            $.ajax({
                method: 'GET',
                url: '/afficher-messages',
                data: {contactId: contactId},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    $('.chat-message').data('contact-id', contactId);
                    openedConversation = contactId;
                    construirChatHistory(data);

                }
            });
        });
        $('#envoyer-message').on('click', function () {
            var $message = $('#message-a-envoyer');
            console.log('hi');
            $.ajax({
                method: 'POST',
                url: '/envoyer-messages',
                data: {contactId: $('.chat-message').data('contact-id'), messageTxt: $message.val()},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    if (data[0] != 'erreur') {
                        construirChatHistory(data);
                        $message.val("");
                    }


                }
            });
        });
        $('#message-a-envoyer').on('focus', function () {
            var rowId = $('.chat-message').data('contact-id');
            $('.contact-row').each(function () {
                if ($(this).data('contact-id') == rowId) {
                    $(this).find('.status').remove();
                }
            });
        });
        $('#rechercher-contact').on('input', function () {
            var inputValue = $(this).val().toLowerCase();
            $('.contact-row').each(function () {
                if ($(this).find('.name').html().trim().toLowerCase().match('^' + inputValue)) {
                    $(this).css('display', 'block');
                } else {
                    $(this).css('display', 'none');
                }
            });
        });
        setInterval(function () {
            $.ajax({
                method: 'GET',
                url: '/afficher-messages-socket',
                data: {chatActuelle: $('.chat-message').data('contact-id')},
                // dataType: 'json',
                success: function (data, success) {
                    if (success == 'success' && data.length && data[0] != 'vide') {
                        console.log(data);
                        console.log(data instanceof Array);
                        $('#notif')[0].play();
                        var $peopleList = $('.people-list .list');
                        $peopleList.html("");
                        if (data[0] instanceof Array) {
                            for (var i = 0; i < data[0].length; i++) {
                                contruireContactRow(data[0][i]);
                            }
                        } else {
                            contruireContactRow(data);
                        }
                        if (data[1].length != 0) {
                            construirChatHistory(data[1]);
                        }
                    }
                }
            });
        }, 3000);
        $('.chat-history').scrollTop($('.chat-history').prop("scrollHeight"));

    }

    //editables on first profile page
    $.fn.editable.defaults.mode = 'inline';
    $.fn.editableform.loading = "<div class='editableform-loading'><i class='ace-icon fa fa-spinner fa-spin fa-2x light-blue'></i></div>";
    $.fn.editableform.buttons = '<button type="submit" class="btn btn-info editable-submit"><i class="ace-icon fa fa-check"></i></button>' +
        '<button type="button" class="btn editable-cancel"><i class="ace-icon fa fa-times"></i></button>';
    $.fn.editable.defaults.ajaxOptions = {type: 'POST'};

    //editables
    var check = false;

    function submitValue(name, newValue) {
        check = false;
        $.ajax({
            method: 'POST',
            url: '/modifier-profil',
            data: {key: name, value: newValue},
            // contentType: false,
            // cache: false,
            // processData:false,
            success: function (data) {
                console.log(data);

                if (data.trim() == 'true') {
                    check = true;
                }
            }
        });
    }

    //text editable
    $('#nom')
        .editable({
            type: 'text',
            name: 'nom',
            emptytext: '',
            success: function (data, newValue) {
                submitValue('nom', newValue);
                setTimeout(function () {
                    console.log(check);
                    if (check) {
                        showNotif('Modification reussie', 'La modification de votre est effectué avec succès!', 'success', true);
                        $('.navbar-container .utilisateur-info').html('<small>Bienvenu, </small>' + newValue);
                    }
                }, 400);

            },
            validate: function (value) {
                if ($.trim(value) == '') {
                    return 'This field is required';
                }
            }
        });

    $('#prenom')
        .editable({
            type: 'text',
            name: 'prenom',
            emptytext: '',
            success: function (data, newValue) {
                submitValue('prenom', newValue);
                setTimeout(function () {
                    console.log(check);
                    if (check) {
                        showNotif('Modification reussie', 'La modification de votre prenom est effectué avec succès!', 'success', true);
                    }
                }, 400);
            },
            validate: function (value) {
                if ($.trim(value) == '') {
                    return 'This field is required';
                }
            }
        });
    var $emailInput = $('#email');
    var $emailProfil = $emailInput.html();
    var $emailParent = $emailInput.parent();

    console.log($emailProfil);

    function bindEmail(emailInput) {
        emailInput
            .editable({
                type: 'text',
                name: 'email',
                emptytext: '',
                success: function (data, newValue) {
                    bootbox.prompt({
                        title: "Veuillez entrer votre mot de passe",
                        inputType: "password",
                        callback: function (result) {
                            if (result === null) {

                            } else
                                submitValue('verify', result);
                            console.log(newValue);
                            setTimeout(function () {
                                console.log('Im here');
                                if (check) {
                                    submitValue('email', newValue);
                                    setTimeout(function () {
                                        console.log(check);
                                        if (check) {
                                            showNotif('Modification reussie', 'La modification de votre l\'email est effectué avec succès!', 'success', true);
                                            showNotif('Email Validation', 'Un email de validation vient d\'être envoyé', 'info', false);
                                            $emailParent.html('');
                                            var $thisEmail = emailInput;
                                            $thisEmail.html(newValue);
                                            $emailParent.append($thisEmail);
                                            bindEmail($thisEmail);
                                        } else {
                                            showNotif('Modification echouée', 'L\'email que vous venez d\'entrer existe dèja', 'error', true);

                                        }
                                    }, 1400);
                                } else {
                                    showNotif('Mot de passe incorrect', 'Le mot de passe que vous venez d\'entrer est invalide', 'error', true);
                                    $emailParent.html('');
                                    var $thisEmail = emailInput;
                                    $emailParent.append($thisEmail);
                                    bindEmail($thisEmail);

                                }
                            }, 400);

                        }
                    });
                    return false;
                }
                , validate: function (value) {
                    if ($.trim(value) == '') {
                        return 'This field is required';
                    }
                }
            });
    }

    bindEmail($emailInput);

    $('#about').editable({
        mode: 'inline',
        type: 'wysiwyg',
        name: 'description',
        emptytext: '',
        wysiwyg: {
            //css : {'max-width':'300px'}
        },
        success: function (response, newValue) {
            console.log(newValue.length);
            submitValue('description', newValue);
            setTimeout(function () {
                console.log(check);
                if (check) {
                    showNotif('Modification reussie', 'La modification de votre description est effectué avec succès!', 'success', true);
                }
            }, 400);

        }
    });
    cpt = 0;

    // *** editable avatar *** //
    try {//ie8 throws some harmless exceptions, so let's catch'em

        //first let's add a fake appendChild method for Image element for browsers that have a problem with this
        //because editable plugin calls appendChild, and it causes errors on IE at unpredicted points
        try {
            document.createElement('IMG').appendChild(document.createElement('B'));
        } catch (e) {
            Image.prototype.appendChild = function (el) {
            }
        }

        var srcImage = "";
        $('#imageProfile').editable({
            type: 'image',
            name: 'imageProfile',
            id: 'imageProfile',
            value: null,
            image: {
                //specify ace file input plugin's options here
                btn_choose: 'Changer Image',
                droppable: true,
                maxSize: 2100000,//~100Kb

                //and a few extra ones here
                name: 'imageProfile',//put the field name here as well, will be used inside the custom plugin
                on_error: function (error_type) {//on_error function will be called when the selected file has a problem
                    console.log(error_type);
                    if (last_gritter) $.gritter.remove(last_gritter);
                    if (error_type == 1) {//file format error
                        last_gritter = $.gritter.add({
                            title: 'File is not an image!',
                            text: 'Please choose a jpg|gif|png image!',
                            class_name: 'gritter-error gritter-center'
                        });
                    } else if (error_type == 2) {//file size rror
                        last_gritter = $.gritter.add({
                            title: 'File too big!',
                            text: 'Image size should not exceed 100Kb!',
                            class_name: 'gritter-error gritter-center'
                        });
                    }
                    else {//other error
                    }
                },
                on_success: function () {
                    $.gritter.removeAll();
                    // console.log(newValue);

                }
            }
            ,
            url: function (params) {
                console.log(params);
                // ***UPDATE AVATAR HERE*** //
                //for a working upload example you can replace the contents of this function with
                //examples/profile-avatar-update.js
                var deferred = new $.Deferred

                var value = $('#imageProfile').next().find('input[type=hidden]:eq(0)').val();
                if (!value || value.length == 0) {
                    deferred.resolve();
                    return deferred.promise();
                }
                $.ajax({
                    url: '/modifier-image-profil',
                    data: new FormData(document.getElementsByClassName('editableform')[0]),
                    contentType: false,
                    processData: false,
                    cache: false,
                    enctype: 'multipart/form-data',
                    type: "POST",
                    success: function (data) {
                        if (data.trim().length && data.trim() != 'false') {
                            // check = true ;
                            srcImage = data;
                            console.log(check);
                        }
                    }
                });

                //dummy upload
                setTimeout(function () {
                    //for browsers that have a thumbnail of selected
                    console.log(srcImage);
                    if (srcImage.length) {
                        $('#imageProfile').attr('src', srcImage);
                        $('.nav-utilisateur-photo').attr('src', srcImage);
                        // submitValue()
                        deferred.resolve({'status': 'OK'});

                        showNotif('Modification reussie', 'La modification de votre Image de profil est effectué avec succès!', 'success', true);
                    } else {
                        showNotif('Modification echouée', 'La modification est echouée! Veuillez reéssayer', 'error', true);

                    }

                }, 400)

                return deferred.promise();
                // ***END OF UPDATE AVATAR HERE*** //
            },

            success: function (response, newValue) {

            }
        })
    } catch (e) {
    }


})
;