import re


# keep only the best F1 result out of three runs
def keep_best(input_file, output_file):
    highest_row = None
    highest_value = float('-inf')
    
    with open(input_file, 'r') as file:
        for line in file:
            if re.match(r"\(.*\)", line.strip()): 
                values = eval(line.strip())  
                if values[2] > highest_value:
                    highest_value = values[2]

    with open(output_file, 'w') as out:
        out.write(str(highest_value))


for type in ["ownership","containment"]:
    for model in ["llama", "qwen", "gpt"]:
        for rule in ['no','nl','jena']:
            for hop in ['direct','indirect']:
                input_filename = f"scores_{type}/three_runs/scores_{model}_{rule}_{hop}.txt"
                print("Current input file: ", input_filename)
                output_filename = f"./scores_{type}/best/scores_{model}_{rule}_{hop}.txt"
                print("Current output file: ", output_filename)
                keep_best(input_filename, output_filename) 





