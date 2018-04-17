jQuery(function ($) {

    var $editeur_text = $('#editor2');
    var checkEventOnTags = false;
    var openedConversation = -1;
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
    function getEmailsAddAndEditDocs(){
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
                            if(!$('#erreur_mail').length)
                            $('.bootbox-form').append($('<p>', {class: 'alert alert-danger',id:'erreur_mail'}).html('Veuillez entrer des emails valides!\n Example1@domaine.com Example2@domaine.com Example3@domaine.fr ... ').css('margin-top', '10px'))
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
                var $tags  = $('.tags');
                $tags.html("");

                getEmailsAddAndEditDocs();
                if(!checkEventOnTags){
                    $tags.on('click',function () {
                        getEmailsAddAndEditDocs();
                    });
                    checkEventOnTags = true;
                }
            }else{
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

    function construirChatHistory(data){

        //entete
        var $entete = $('.chat-header');
        $entete.html("");
        var $recepteurImage = $('<img>',{class: 'contact-img',src:data[1], alt: 'avatar'});
        var $chatAbout = $('<div>',{class: 'chat-about'});
        var $chatWith = $('<div>',{class: 'chat-with'});
        $chatWith.html(data[0]);
        $chatAbout.append($chatWith);
        $entete.append($recepteurImage);
        $entete.append($chatAbout);

        var $chatHistory = $('.chat-history');
        $chatHistory.html("");
        var $chatContainer = $('<ul>');
        for(var i=2;i<data.length;i++) {
            //emetteur
            if(data[i].emetteur == 'moi') {
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
            }else {
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

    function contruireContactRow(data){
        var $peopleList = $('.people-list .list');
        var $contactRow = $('<li>', {class: 'clearfix contact-row'});
        $contactRow.data('contact-id',data.id);
        $contactRow.on('click',function () {
            var contactId = $(this).data('contact-id');
            console.log(contactId);
            $.ajax({
                method:'GET',
                url: '/afficher-messages',
                data: {contactId: contactId},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    $('.chat-message').data('contact-id',contactId);
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
    if($contactRow.length){
        $contactRow.on('click',function () {
            var contactId = $(this).data('contact-id');
            console.log(contactId);
            $.ajax({
                method:'GET',
                url: '/afficher-messages',
                data: {contactId: contactId},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    $('.chat-message').data('contact-id',contactId);
                    openedConversation = contactId;
                    construirChatHistory(data);

                }
            });
        });
        $('#envoyer-message').on('click',function () {
            var $message = $('#message-a-envoyer');
            console.log('hi');
            $.ajax({
                method:'POST',
                url: '/envoyer-messages',
                data: {contactId: $('.chat-message').data('contact-id'),messageTxt: $message.val()},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    if(data[0]!='erreur') {
                        construirChatHistory(data);
                        $message.val("");
                    }


                }
            });
        });
        $('#message-a-envoyer').on('focus',function () {
            var rowId = $('.chat-message').data('contact-id');
            $('.contact-row').each(function(){
                if($(this).data('contact-id')==rowId){
                    $(this).find('.status').remove();
                }
            });
        });
        $('#rechercher-contact').on('input',function(){
            var inputValue=$(this).val().toLowerCase();
            $('.contact-row').each(function(){
                if($(this).find('.name').html().trim().toLowerCase().match('^'+inputValue)){
                    $(this).css('display','block');
                }else{
                    $(this).css('display','none');
                }
            });
        });
        setInterval(function () {
            $.ajax({
                method:'GET',
                url: '/afficher-messages-socket',
                data:{chatActuelle:$('.chat-message').data('contact-id')},
                // dataType: 'json',
                success: function (data,success) {
                    if(success=='success' && data.length && data[0]!='vide') {
                        console.log(data);
                        console.log(data instanceof Array);
                        $('#notif')[0].play();
                        var $peopleList = $('.people-list .list');
                        $peopleList.html("");
                        if(data[0] instanceof Array) {
                            for (var i = 0; i < data[0].length; i++) {
                                contruireContactRow(data[0][i]);
                            }
                        }else{
                            contruireContactRow(data);
                        }
                        if(data[1].length != 0){
                            construirChatHistory(data[1]);
                        }
                    }
                }
            });
        },3000);
            $('.chat-history').scrollTop($('.chat-history').prop("scrollHeight"));

    }


})
;