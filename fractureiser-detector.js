const fs = require('fs');
const { execSync } = require('child_process');
const https = require('https');
const path = require('path');
const zlib = require('zlib');

const PROCYON_JAR_PATH = 'procyon-decompiler.jar';
const detections = [];

function installProcyon() {
    const procyonUrl = 'https://github.com/mstrobel/procyon/releases/download/v0.6.0/procyon-decompiler-0.6.0.jar';
    execSync(`curl -L ${procyonUrl} -o ${PROCYON_JAR_PATH}`);
}

function decompileJar(jarFile, outputDir) {
    execSync(`java -jar ${PROCYON_JAR_PATH} -o ${outputDir} ${jarFile}`);
}

function searchInDirectory(directory, searchStrings) {
    let isInfected = false;
    const files = fs.readdirSync(directory);
    files.forEach((file) => {
        const filePath = path.join(directory, file);
        const stats = fs.statSync(filePath);
        if (stats.isDirectory()) {
            isInfected |= searchInDirectory(filePath, searchStrings);
        } else if (stats.isFile() && file.endsWith('.jar')) {
            isInfected |= analyzeJar(filePath, searchStrings);
        }
    });
    return isInfected;
}

function searchInDecDirectory(directory, searchStrings) {
    let isInfected = false;
    const files = fs.readdirSync(directory);
    files.forEach((file) => {
        const filePath = path.join(directory, file);
        const stats = fs.statSync(filePath);
        if (stats.isFile() && file.endsWith('.java')) {
            isInfected |= searchInFile(filePath, searchStrings);
        } else if (stats.isDirectory()) { // Recursively search in subdirectories
            isInfected |= searchInDecDirectory(filePath, searchStrings);
        }
    });
    return isInfected;
}

function searchInFile(filePath, searchStrings) {
    let isInfected = false;
    const lines = fs.readFileSync(filePath, 'utf8').split('\n');
    lines.forEach((line) => {
        searchStrings.forEach((searchString) => {
            if (line.includes(searchString)) {
                isInfected = true;
                if (!detections.includes(`Warning: File ${filePath} is infected!`)) {
                    detections.push(`Warning: File ${filePath} is infected!`);
                    console.log(`Warning: File ${filePath} is infected!`);
                }
                return;
            }
        });
    });
    return isInfected;
}

function analyzeJar(jarFile, searchStrings) {
    let isInfected = false;
    const decompiledDir = path.join(process.cwd(), 'decompiled');
    fs.mkdirSync(decompiledDir, { recursive: true });
    try {
        decompileJar(jarFile, decompiledDir);
        isInfected = searchInDecDirectory(decompiledDir, searchStrings);
    } finally {
        fs.rmSync(decompiledDir, { recursive: true, force: true });
    }
    if (isInfected) {
        console.log(`Warning: The JAR file '${jarFile}' contains infected files.`);
    }
    return isInfected;
}

function scanJar(jarFile, searchStrings) {
    const isInfected = analyzeJar(jarFile, searchStrings);
    if (!isInfected) {
        console.log('No infected files found in the JAR file.');
    }
}

function main() {
    const args = process.argv.slice(2);
    if (args.length < 1) {
        console.log('Error: Please provide the path to the JAR file or directory.');
        return;
    }
    const jarPath = args[0];
    const recursiveMode = args.includes('--recursive');

    const searchStrings = [
        '85.217.144.130',
        '56, 53, 46, 50, 49, 55, 46, 49, 52, 52, 46, 49, 51, 48',
        '-74.-10.78.-106.12',
        '114.-18.38.108.-100',
        '-114.-18.38.108.-100',
    ];

    if (!fs.existsSync(PROCYON_JAR_PATH)) {
        installProcyon();
    }

    if (recursiveMode) {
        if (!fs.statSync(jarPath).isDirectory()) {
            console.log(
                'Error: The specified path is not a directory. If you are not using recursive mode, provide the path to a file.'
            );
            return;
        }
        const isInfected = searchInDirectory(jarPath, searchStrings);
        if (!isInfected) {
            console.log('No infected files found in the JAR files.');
        }
    } else {
        if (!fs.statSync(jarPath).isFile()) {
            console.log('Error: The specified path is not a file.');
            return;
        }
        scanJar(jarPath, searchStrings);
    }

    // detections.forEach((detection) => {
    //     console.log(detection);
    // });
}

main();