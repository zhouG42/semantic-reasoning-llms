import re
import matplotlib.pyplot as plt


llama_f1 = []  
qwen_f1 = []  
gpt_f1 = []  

hop = "indirect"
#type = "containment" 
type = "ownership" 

# llama
for rule in ['no','nl','jena']:
    filename = f"./scores_{type}/best/scores_llama_{rule}_{hop}.txt"
    with open(filename, "r") as file: 
        for line in file:
            best_f1_scores = float(line)
            llama_f1.append(float(best_f1_scores))

#qwen
for rule in ['no','nl','jena']:
    filename = f"./scores_{type}/best/scores_qwen_{rule}_{hop}.txt"
    with open(filename, "r") as file: 
        for line in file:
            best_f1_scores = float(line)
            qwen_f1.append(float(best_f1_scores))

# gpt
for rule in ['no','nl','jena']:
    filename = f"./scores_{type}/best/scores_gpt_{rule}_{hop}.txt"
    with open(filename, "r") as file: 
        for line in file:
            best_f1_scores = float(line)
            gpt_f1.append(float(best_f1_scores))

print("llama",llama_f1)
print("qwen", qwen_f1)
print("gpt",gpt_f1)

# Ownership F1 
hops = ['no', 'nl', 'jena']

if type == "containment":

    plt.figure(figsize=(10, 6))
    plt.plot(hops, llama_f1, marker='o', label='llama')
    plt.plot(hops, qwen_f1, marker='s', label='qwen')
    plt.plot(hops, gpt_f1, marker='^', label='gpt')

    plt.xlabel('context', fontsize=12)
    plt.ylabel('F1 Score', fontsize=12)
    plt.title('F1 Score vs context for direct Containment', fontsize=14)
    plt.xticks(hops)  
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend(title='Containment', fontsize=10)

    plt.tight_layout()
    plt.show()

else:

    plt.figure(figsize=(10, 6))
    plt.plot(hops, llama_f1, marker='o', label='llama')
    plt.plot(hops, qwen_f1, marker='s', label='qwen')
    plt.plot(hops, gpt_f1, marker='^', label='gpt')

    plt.xlabel('context', fontsize=12)
    plt.ylabel('F1 Score', fontsize=12)
    plt.title('F1 Score vs context for direct Ownership', fontsize=14)
    plt.xticks(hops) 
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend(title='Ownership', fontsize=10)

    plt.tight_layout()
    plt.show()
    

