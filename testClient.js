"use strict";
const net = require('net');

const socket = net.connect({
    port: 23487,
    host: 'localhost'
}, () => {
    console.log("Connection established!");

    socket.write("getOccuresBy\nBauchschmerzen\n");
});

let answer = "";

socket.on('data', data => {
    answer += data;
});

socket.on('end', () => {
    console.log("Answer: " + answer);
    console.log("Connection closed!");
});
