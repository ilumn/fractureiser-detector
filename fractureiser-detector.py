import shutil
import os
import subprocess

PROCYON_JAR_PATH = "procyon-decompiler.jar"


def install_procyon():
    procyon_url = "https://github.com/mstrobel/procyon/releases/download/v0.6.0/procyon-decompiler-0.6.0.jar"
    subprocess.run(["curl", "-L", "-o", PROCYON_JAR_PATH, procyon_url], check=True)


def decompile_jar(jar_file, output_dir):
    subprocess.run(["java", "-jar", PROCYON_JAR_PATH, "-o", output_dir, jar_file], check=True)


def search_in_directory(directory, search_strings):
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
                    print(f"Warning: File {file_path} is infected!")
                    break
    return is_infected


def main():
    jar_file = input("Enter the path to the JAR file: ")
    search_strings = [
        "85.217.144.130",
        "56, 53, 46, 50, 49, 55, 46, 49, 52, 52, 46, 49, 51, 48",
        "-74.-10.78.-106.12",
        "114.-18.38.108.-100",
        "-114.-18.38.108.-100"
    ]
    is_infected = False

    # Check if Procyon decompiler is installed
    if not os.path.exists(PROCYON_JAR_PATH):
        install_procyon()

    # Create a temporary directory for decompiled files
    decompiled_dir = os.path.join(os.getcwd(), "decompiled")
    os.makedirs(decompiled_dir, exist_ok=True)

    # Decompile the JAR file using Procyon decompiler
    decompile_jar(jar_file, decompiled_dir)

    # Recursively search for the strings in the decompiled files
    is_infected = search_in_directory(decompiled_dir, search_strings)

    # Cleanup: Delete the temporary directory
    shutil.rmtree(decompiled_dir)

    if is_infected:
        print("Warning: The JAR file contains infected files.")


if __name__ == "__main__":
    main()
