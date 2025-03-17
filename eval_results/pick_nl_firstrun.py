import os
import shutil


# copy files to a new folder (nl & first run)
def copy_file_to_folder(file_path, destination_folder):
    if not os.path.isfile(file_path):
        print("Error: File does not exist.")
        return
    
    if not os.path.exists(destination_folder):
        os.makedirs(destination_folder)
    
    file_name = os.path.basename(file_path)
    destination_path = os.path.join(destination_folder, file_name)
    
    shutil.copy2(file_path, destination_path)
    print(f"File copied to {destination_path}")


hop = "direct"
type = "ownership" 
model = "llama"
rule = "nl"
run = "1"


for model in ['llama', 'qwen', 'gpt']:
    for type in ['ownership','containment']:
        for hop in ['direct','indirect']:
            filename = f"../three_runs/LLM_outputs/{model}/{type}/{model}_{rule}_{hop}_{run}.txt" 
            folderpath = f"./triple_counts/{model}/{type}/{hop}"
            copy_file_to_folder(filename, folderpath)

