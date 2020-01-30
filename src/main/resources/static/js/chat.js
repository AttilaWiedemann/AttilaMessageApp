'use strict';

var stompClient = null;
function connect() {

        var socket = new SockJS('/stompwebsocket');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);

}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server


}


function onError(error) {
}

var messageInput = document.querySelector('#message');


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: "Sanyi",
            text: messageContent,
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    console.log(payload);
}

var messageForm = document.querySelector('#messageForm');

$(function() {
    console.log( "ready!" );
    connect();
});

messageForm.addEventListener('submit', sendMessage, true)