#!/usr/bin/js

"use strict";

const input = process.argv[2];

if (!input) {
    console.log("Missing input file argument");
    process.exit(1);
}

const fs = require("fs");
const lineReader = require("readline").createInterface({
    input: fs.createReadStream(input)
});


let started = false;
let level;
let cols;
let rows;
let parsed = [];

lineReader.on("line", function (line) {
    if (line === "*************************************") {
        if (started) {
            finishLevel();
        } else {
            started = true;
        }
    } else if (line.indexOf("Maze:") === 0) {
        level = line.substring(6);
    } else if (line.indexOf("Size X:") === 0) {
        cols = parseInt(line.substring(8));
    } else if (line.indexOf("Size Y:") === 0) {
        rows = parseInt(line.substring(8));
    } else if (line.indexOf("X") > -1) {
        parsed.push(line);
    }
});

function finishLevel() {
    fs.writeFile(`level_${level}.txt`, parsed.join("\n"));
    parsed = [];
}
