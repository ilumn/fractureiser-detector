# fractureiser-detector
#### Detects the malware embeded in recent comrpomised CurseForge mods (currently reffered to as fractureiser) (WARNING: javascript file is broken rn use the others)
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


