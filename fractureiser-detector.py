import os
import argparse
import subprocess
import shutil

PROCYON_JAR_PATH = "procyon-decompiler.jar"
detections = []

def install_procyon():
    procyon_url = "https://github.com/mstrobel/procyon/releases/download/v0.6.0/procyon-decompiler-0.6.0.jar"
    subprocess.run(["curl", "-L", "-o", PROCYON_JAR_PATH, procyon_url], check=True)


def decompile_jar(jar_file, output_dir):
    subprocess.run(["java", "-jar", PROCYON_JAR_PATH, "-o", output_dir, jar_file], check=True)


def search_in_directory(directory, search_strings):
    is_infected = False
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(".jar"):
                file_path = os.path.join(root, file)
                is_infected |= analyze_jar(file_path, search_strings)
    return is_infected

def search_in_dec_directory(directory, search_strings):
    is_infected = False
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)
                is_infected |= search_in_file(file_path, search_strings)
    return is_infected
    
def search_in_file(file_path, search_strings):
    is_infected = False
    with open(file_path, "r") as file:
        for line in file:
            for search_string in search_strings:
                if search_string in line:
                    is_infected = True
                    detections.append(f"Warning: File {file_path} is infected!")
                    print(f"Warning: File {file_path} is infected!")
                    break
    return is_infected

def analyze_jar(jar_file, search_strings):
    is_infected = False

    # Create a temporary directory for decompiled files
    decompiled_dir = os.path.join(os.getcwd(), "decompiled")
    os.makedirs(decompiled_dir, exist_ok=True)

    try:
        # Decompile the JAR file using Procyon decompiler
        decompile_jar(jar_file, decompiled_dir)

        # Recursively search for the strings in the decompiled files
        is_infected = search_in_dec_directory(decompiled_dir, search_strings)
    finally:
        # Cleanup: Delete the temporary directory
        shutil.rmtree(decompiled_dir)

    if is_infected:
        print(f"Warning: The JAR file '{jar_file}' contains infected files.")

    return is_infected


def scan_jar(jar_file, search_strings):
    is_infected = analyze_jar(jar_file, search_strings)

    if not is_infected:
        print("No infected files found in the JAR file.")


def main():
    parser = argparse.ArgumentParser(description="JAR File Scanner")
    parser.add_argument("file", help="JAR file to scan")
    parser.add_argument(
        "--recursive",
        action="store_true",
        help="Scan JAR files in the directory recursively",
    )

    args = parser.parse_args()
    jar_path = args.file
    recursive_mode = args.recursive

    search_strings = [
        "85.217.144.130",
        "56, 53, 46, 50, 49, 55, 46, 49, 52, 52, 46, 49, 51, 48",
        "-74.-10.78.-106.12",
        "114.-18.38.108.-100",
        "-114.-18.38.108.-100",
    ]

    # Check if Procyon decompiler is installed
    if not os.path.exists(PROCYON_JAR_PATH):
        install_procyon()

    if recursive_mode:
        if not os.path.isdir(jar_path):
            print("Error: The specified path is not a directory. if you arent using recursive mode provide the path to a file")
            return

        is_infected = search_in_directory(jar_path, search_strings)

        if not is_infected:
            print("No infected files found in the JAR files.")
    else:
        if not os.path.isfile(jar_path):
            print("Error: The specified path is not a file.")
            return

        scan_jar(jar_path, search_strings)

    for detection in detections:
        print(detection)

if __name__ == "__main__":
    main()
    input("press any key to continue")
