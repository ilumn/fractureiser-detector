# fractureiser-detector
### (If you have any suggestions issues or concerns please open an issue or add a pull request with your own solution, I will be improving this frequently)
#### Detects the malware embeded in recent comrpomised CurseForge mods (currently reffered to as fractureiser) (WARNING: only the python script supports full directory scanning)
----
For easy of use or run preference I have included Java, Python, and Javascript versions of the detector. I recommend Python but it is up to you.

#### to run the python script use the following command:
recursive scan allows the scanning of a full directory
```py3 fractureiser-detector.py <jar_file_or_directory> [--recursive]``` (windows)
or
```python3 fractureiser-detector.py <jar_file_or_directory> [--recursive]``` (linux)
followed by the absolute file path to the jar when you are prompted, or the path to the directory if you are doing a recursive scan.

#### to run the java program use the following command:
```javac fractureiserdetector.java && java fractureiserdetector.java```
followed by the absolute file path to the jar when you are prompted.

----
## Current Features:
- recursive directory scanning to see which files in your mods folder are malicious (python only)
- full decompiling
- configureable search parameters

## To Do
- add recursive scanning to java version
- add progress bar to recursive scanning mode
- (anything else you suggest)
