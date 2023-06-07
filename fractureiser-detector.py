import os
import subprocess

def search_in_file(file_path, search_string):
    with open(file_path, 'r') as file:
        for line_number, line in enumerate(file, 1):
            if search_string in line:
                print(f'Warning: File {file_path} is infected!')

def search_in_directory(directory, search_string):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                search_in_file(file_path, search_string)

def main():
    jar_file_path = input('Enter the path to the JAR file: ')
    search_string = '85.217.144.130'

    # Extract the contents of the JAR file
    subprocess.run(['jar', 'xf', jar_file_path])

    # Recursively search for the string in all extracted files
    search_in_directory('.', search_string)

if __name__ == '__main__':
    main()
