"use strict";
const net = require('net');

const path = 'owl.socket';

const socket = net.connect(path, () => {
    console.log("Connection established!");

    socket.write("getIndividualTypes\nM300400\n");
});

let answer = "";

socket.on('data', data => {
    answer += data;
});

socket.on('end', () => {
    console.log("Answer: " + answer);
    console.log("Connection closed!");
});
