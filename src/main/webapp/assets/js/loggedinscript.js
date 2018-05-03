jQuery(function ($) {
    var $peopleList = $('#people-list');
    var positionStart = 10;
    var chatClient = new WebSocket("ws://" + location.host + "/chat/" + $("#main-container").data('utilisateur'));
    var MAX_MSG = 10;

    function correctDate(val) {
        return val < 10 ? "0" + val : val;
    }

    var chatWindows = [];

    function buildChatRow(data) {
        if (data.sender === 'moi') {

            var $msgContainerSent = $('<div class="row msg_container base_sent">');
            var $col4 = $('<div class="chat-col col-md-10 col-xs-10">');
            var $msgSent = $('<div class="messages msg_sent">');
            var $p = $('<p>');
            $p.html(data.message);
            var $time = $('<time>');
            $time.html(data.sender + " " + data.sentDate);

            $msgSent.append($p);
            $msgSent.append($time);
            var $col5 = $('<div class="chat-col col-md-2 col-xs-2 avatar chat-avatar">');
            var $img = $('<img>', {class: "chat-img img-responsive img-chat", src: data.senderImg});
            var $a = $('<a href="/profil">');
            $a.append($img);
            $col5.append($a);

            $col4.append($msgSent);
            $msgContainerSent.append($col4);
            $msgContainerSent.append($col5);
            return $msgContainerSent;
        } else {
            var $msgContainerReceive = $('<div class="row msg_container base_receive">');
            var $col6 = $('<div class="chat-col col-md-2 col-xs-2 avatar chat-avatar">');
            var $img = $('<img>', {class: " chat-img img-responsive img-chat", src: data.senderImg});
            var $a = $('<a href="/profil/' + data.senderId + '">');
            $a.append($img);
            $col6.append($img);
            var $col7 = $('<div class="chat-col col-md-10 col-xs-10">');
            var $msgReceive = $('<div class="messages msg_receive">');
            var $p2 = $('<p>');
            $p2.html(data.message);
            var $time2 = $('<time >');
            $time2.html(data.sender + " " + data.sentDate);
            $msgReceive.append($p2);
            $msgReceive.append($time2);
            $col7.append($msgReceive);
            $msgContainerReceive.append($col6);
            $msgContainerReceive.append($col7);
            return $msgContainerReceive;
        }
    }

    function bindCHatRowToClick($element) {
        if ($peopleList.length) {
            $element.click(function (e) {
                var idContact = $(this).data('contact-id');
                var found = false;
                $('.chat-window').each(function () {
                    if ($(this).data('chat-id') == idContact) {
                        // $(this).find('.panel-body.msg_container_base').slideUp();
                        found = true;
                        return;
                    }
                });
                if (found) return;
                $.ajax({
                        method: 'get',
                        url: '/conversation',
                        data: {idContact: idContact, start: 0, end: MAX_MSG},
                        dataType: 'json',
                        success: function (data) {
                            if (data.length) {
                                if (chatWindows.length > 0 && $.inArray(data[0].toString(), chatWindows) != -1)
                                    return;
                                chatWindows.push(data[0].toString());
                                var $chatWindow = $('<div class="row chat-window col-sm-3 col-md-3" style="margin-left:10px;">');
                                var $col1 = $('<div class="chat-col col-xs-12 col-md-12">');
                                var $chatPanel = $('<div class="chat-panel panel panel-default">');
                                // panel heading begin
                                var $chatHeading = $('<div class="panel-heading top-bar chat-top-bar">');
                                var $col2 = $('<div class="chat-col col-md-8 col-xs-8">');
                                var $panelTitle = $('<h3 class="panel-title">');
                                var $glyphIconComment = $('<span class="glyphicon glyphicon-comment">');
                                // $glyphIconComment.html(data[0]);
                                $panelTitle.append($glyphIconComment);
                                $panelTitle.append(data[1]);
                                $col2.append($panelTitle);

                                var $col3 = $('<div class="chat-col col-md-4 col-xs-4" style="text-align: right;">');
                                var $minimChat = $('<a href="#" class="minim-chat-window">');
                                var $minimCHatIcon = $('<span id="minim_chat_window" class="glyphicon glyphicon-minus icon_minim">');
                                var $closeChat = $('<a href="#" class="close-chat-window">');
                                var $closeChatIcon = $('<span class="glyphicon glyphicon-remove icon_close" data-id="chat_window_1">');
                                $minimChat.append($minimCHatIcon);
                                $closeChat.append($closeChatIcon);
                                $minimChat.click(function () {
                                    $(this).parent().parent().parent().parent().find('.panel-body.msg_container_base').slideToggle();

                                });
                                $closeChat.click(function () {
                                    var $chatwindow = $(this).parent().parent().parent().parent().parent();
                                    chatWindows = $.grep(chatWindows, function (value) {
                                        return value.toString() != $chatWindow.data('chat-id').toString();
                                    });
                                    console.log(chatWindows);
                                    positionStart -= $chatPanel.width();
                                    $chatwindow.remove();
                                    if (positionStart < 10) positionStart = 10;

                                });
                                $col3.append($minimChat);
                                $col3.append($closeChat);
                                $chatHeading.append($col2);
                                $chatHeading.append($col3);
                                // panel heading end
                                var $panelBody = $('<div class="panel-body msg_container_base" id="contact-' + data[0] + '">');
                                for (var i = 3; i < data.length; i++) {

                                    $panelBody.append(buildChatRow(JSON.parse(data[i])));
                                }
                                $panelBody.data("pag-end", MAX_MSG);
                                $panelBody.scroll(function () {
                                    var $thisPanel = $(this);
                                    var previousScrollHeight = $thisPanel.prop("scrollHeight");

                                    var pos = $panelBody.scrollTop();

                                    if (pos == 0) {
                                        console.log('here');
                                        var $loader = $('<div class="loader">');
                                        $loader.css('width', '100%')
                                        var $loaderImg = $('<img>', {src: "https://www.ourshopee.com/img/loader.gif"});
                                        $loaderImg.css({
                                            height: "45px",
                                            width: "45px",
                                            margin: "auto",
                                            display: "block"
                                        });
                                        $loader.append($loaderImg);
                                        console.log($loader.html());
                                        $thisPanel.prepend($loader);
                                        var pagStart = (parseInt($panelBody.data("pag-end")) + 1);
                                        var pagEnd = (parseInt($panelBody.data("pag-end")) + MAX_MSG);
                                        $panelBody.data("pag-end", pagEnd);
                                        $.ajax({
                                            type: 'get',
                                            url: '/conversation',
                                            data: {
                                                idContact: $thisPanel.parent().parent().parent().data('chatId'),
                                                start: pagStart,
                                                end: pagEnd
                                            },
                                            dataType: 'json',
                                            cache: false,
                                            success: function (data) {
                                                $loader.remove();
                                                for (var i = data.length - 1; i >= 3; i--) {
                                                    $thisPanel.prepend(buildChatRow(JSON.parse(data[i])));
                                                }
                                                $thisPanel.scrollTop($thisPanel.prop("scrollHeight") - previousScrollHeight);
                                            }
                                        });
                                    }
                                });
                                var $panelFooter = $('<div class="panel-footer">');
                                var $inputGroup = $('<div class="input-group">');
                                var $messageInput = $('<input id="btn-input" type="text" class="form-control input-sm chat_input" placeholder="Entrez votre message ...">');
                                var $inputGrpBtn = $('<span class="input-group-btn">');
                                var $btnSend = $('<button class="btn btn-primary btn-sm" >');
                                $btnSend.html('Envoyer')
                                $btnSend.data('recepteur', data[0]);
                                $btnSend.click(function () {
                                    var today = new Date();
                                    var fullDate = correctDate(today.getDate()) + "/" + correctDate(today.getMonth() + 1) + "/" + correctDate(today.getFullYear()) + "  " + correctDate(today.getHours()) + ":" + correctDate(today.getMinutes());
                                    var $textInput = $(this).parent().parent().find('input');
                                    if (!$textInput.val().length) return;
                                    var messageObject = {
                                        message: $textInput.val(),
                                        senderId: $("#main-container").data('utilisateur').toString(),
                                        receiver: $(this).data('recepteur').toString(),
                                        senderImg: $('.nav-utilisateur-photo').attr('src'),
                                        sender: "moi",
                                        sentDate: fullDate
                                    };
                                    $textInput.val("");
                                    $('#contact-' + messageObject.receiver).append(buildChatRow(messageObject));
                                    chatClient.send(JSON.stringify(messageObject));
                                    $panelBody.scrollTop($panelBody.prop("scrollHeight"));

                                });
                                $inputGrpBtn.append($btnSend);
                                $inputGroup.append($messageInput);
                                $inputGroup.append($inputGrpBtn);
                                $panelFooter.append($inputGroup);

                                $chatPanel.append($chatHeading);
                                $chatPanel.append($panelBody);
                                $chatPanel.append($panelFooter);
                                $col1.append($chatPanel);
                                $chatWindow.append($col1);
                                $('body').append($chatWindow);
                                $chatWindow.css('right', positionStart);
                                $chatWindow.data('chat-id', data[0]);
                                positionStart += $chatWindow.width();

                                $panelBody.scrollTop($panelBody.prop("scrollHeight"));

                            }
                        }
                    }
                );
            })
            ;
        }
    }

    function contruireContactRow(data, triggerClick) {
        var $peopleList = $('.people-list .list');
        var $contactRow = $('<li>', {class: 'clearfix contact-row'});
        bindCHatRowToClick($contactRow);
        $contactRow.data('contact-id', data.senderId);
        var $contactImg = $('<img>', {
            class: 'contact-img',
            src: data.senderImg,
            alt: 'image de' + data.sender
        });
        var $about = $('<div>', {class: 'about'});
        var $contactName = $('<div>', {class: 'name'});
        $contactName.append(data.sender);
        var $status = $('<div>', {class: 'status green'});
        // var $statusIcone = $('<i>', {class: 'fa fa-circle online'});
        // $status.append($statusIcone);
        $about.append($contactName);
        $about.append($status);
        $contactRow.append($contactImg);
        $contactRow.append($about);
        $peopleList.append($contactRow);
        if (triggerClick) {
            $contactRow.trigger('click');
        }
    }


    $.ajax({
        method: 'get',
        url: '/contacts',
        dataType: 'json',
        success: function (data, status) {
            for (var i = 0; i < data.length; i++) {
                contruireContactRow(JSON.parse(data[i]), false)
            }
        }
    });
    $('.tabbable #send_message').click(function (e) {
        e.preventDefault();
        var trouve = false;
        var uId = $(this).data('u-id');
        $('.contact-row').each(function () {
            if ($(this).data('contact-id') === uId) {
                trouve = true;
                return;
            }
        });
        if (trouve) return;
        var url = $(this).attr('href');
        $.ajax({
            method: 'get',
            url: url,
            dataType: 'json',
            success: function (data, status) {
                contruireContactRow(data, true);
            }
        });
    });
    chatClient.onmessage = function (evt) {
        var msg = JSON.parse(evt.data);
        var trouve = false;
        var uId = msg.senderId;
        $('.contact-row').each(function () {
            if ($(this).data('contact-id') == uId) {
                $(this).trigger('click');
                trouve = true;
                return;
            }
        });
        if (!trouve) {
            contruireContactRow(msg, true);
            setTimeout(function () {
                var $panelBody = $('#contact-' + msg.senderId);
                $panelBody.append(buildChatRow(msg));
                $panelBody.scrollTop($panelBody.prop("scrollHeight"));
            }, 500);


        }
        var notif = new Audio("/assets/sons/definite.mp3");
        notif.play();
    };

    /**
     * webSocket Initialisation
     */

});