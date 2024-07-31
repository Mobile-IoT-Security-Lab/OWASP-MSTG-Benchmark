import os

def rename_files_in_directory(directory):
    # Lista dei file nella directory specificata
    for filename in os.listdir(directory):
        # Controlla se il nome del file inizia con "MASTG-TEST"
        if filename.startswith("APKHunt_MASTG-TEST"):
            # Crea il nuovo nome del file sostituendo "MASTG-TEST" con "sebastian"
            new_filename = "APKHUNT" + filename[18:]
            # Costruisce i percorsi completi dei file
            old_file_path = os.path.join(directory, filename)
            new_file_path = os.path.join(directory, new_filename)
            # Rinomina il file
            os.rename(old_file_path, new_file_path)
            print(f'File renamed: {filename} -> {new_filename}')
        else:
            print(f'File skipped: {filename} (does not start with "MASTG-TEST")')

# Esempio di utilizzo: specifica la directory contenente i file da rinominare
directory_path = '/home/talos/Desktop/result Owasp/risultato_apkhunt'
rename_files_in_directory(directory_path)
