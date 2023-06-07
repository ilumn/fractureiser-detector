const fs = require('fs');
const readline = require('readline');
const { execSync } = require('child_process');

function searchInFile(filePath, searchString) {
  const fileStream = fs.createReadStream(filePath);
  const rl = readline.createInterface({
    input: fileStream,
    crlfDelay: Infinity
  });

  let lineNumber = 1;
  let infected = false;
  rl.on('line', (line) => {
    if (line.includes(searchString)) {
      infected = true;
      console.log(`Warning: File ${filePath} is infected!`);
    }
    lineNumber++;
  });

  rl.on('close', () => {
    if (infected) {
      console.log('Please handle the infected file accordingly.');
    }
  });
}

function searchInDirectory(directory, searchString) {
  const files = fs.readdirSync(directory);
  files.forEach((file) => {
    const filePath = `${directory}/${file}`;
    const stats = fs.statSync(filePath);
    if (stats.isDirectory()) {
      searchInDirectory(filePath, searchString);
    } else if (file.endsWith('.java')) {
      searchInFile(filePath, searchString);
    }
  });
}

function main() {
  const jarFilePath = readline.question('Enter the path to the JAR file: ');
  const searchString = '85.217.144.130';

  // Extract the contents of the JAR file
  execSync(`jar xf ${jarFilePath}`);

  // Recursively search for the string in all extracted files
  searchInDirectory('.', searchString);
}

main();
