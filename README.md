# fractureiser-detector
### (If you have any suggestions, issues, or concerns, please open an issue or add a pull request with your own solution. I will be improving this frequently.)

#### Detects the malware embedded in recently compromised CurseForge mods (currently referred to as fractureiser). (WARNING: Only the Python script supports full directory scanning.)

----

For ease of use or personal preference, I have included Java, Python, and JavaScript(Node.js) versions of the detector. I recommend Python, but the choice is yours.

#### To run the Python script, use the following command:
Recursive scanning allows scanning of a full directory.
```python fractureiser-detector.py <jar_file_or_directory> [--recursive/--modrinth]``` (Windows)
or
```python3 fractureiser-detector.py <jar_file_or_directory> [--recursive/--modrinth]``` (Linux)

Provide the absolute file path to the JAR when prompted, or the path to the directory if you are doing a recursive scan.

#### To run the Java program, use the following command:
```javac fractureiserdetector.java && java fractureiserdetector```

Provide the absolute file path to the JAR when prompted.

#### To run the JavaScript program, use the following command:
```node fractureiser-detector.js <jar_file> [--recursive]```

----

## Current Features:
- Recursive directory scanning to identify malicious files in your mods folder (Python only)
- modrinth modpack scanning, (Python only)
- Full decompiling
- Configurable search parameters

## To Do:
- Add recursive scanning to Java version
- Add a progress bar to recursive scanning mode
- Implement any suggested improvements or additional features
