import re


# calculate the average of three runs
def keep_average(input_file, output_file):
    total = [0, 0, 0]
    count = 0
    
    with open(input_file, 'r') as file:
        for line in file:
            if re.match(r"\(.*\)", line.strip()):  
                values = eval(line.strip())  
                total = [total[i] + values[i] for i in range(3)]
                count += 1
    if count == 0:
        return None
    average_row = tuple(val / count for val in total)
    with open(output_file, 'w') as out:
        out.write(f"{average_row}\n")



for type in ["ownership","containment"]:
    for model in ["llama", "qwen", "gpt"]:
        for rule in ['no','nl','jena']:
            for hop in ['direct','indirect']:
                input_filename = f"scores_{type}/three_runs/scores_{model}_{rule}_{hop}.txt"
                print("Current input file: ", input_filename)
                output_filename = f"./scores_{type}/average/scores_{model}_{rule}_{hop}.txt"
                print("Current output file: ", output_filename)
                keep_average(input_filename, output_filename) 




