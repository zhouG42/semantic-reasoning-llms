from sklearn.metrics import precision_score, recall_score, f1_score
import os

# read file 1-3 one by one and write 3 times scores (p/r/f1) to a txt file
# type = "ownership"
# model = "gpt"
# rule = "jena"
# hop = "direct"



def read_lists_from_file(file_path):
    lists = []
    try:
        with open(file_path, 'r') as file:
            for line in file:
                line_list = eval(line.strip())
                #print("**********", line_list)
                lists.append(line_list)  
        return lists
    except FileNotFoundError:
        print(f"The file {file_path} does not exist.")
        return None
    except Exception as e:
        print(f"An error occurred: {e}")
        return None

def calculate_metrics(ground_truth_path, model_output_path):
    ground_truth_lists = read_lists_from_file(ground_truth_path)
    model_output_lists = read_lists_from_file(model_output_path)

    if ground_truth_lists is None or model_output_lists is None:
        return

    # check if the number of lists is consistent
    if len(ground_truth_lists) != len(model_output_lists):
        print("Error: The ground truth and model output files have different numbers of lists.")
        return

    all_precision = []
    all_recall = []
    all_f1 = []

    for gt_list, model_list in zip(ground_truth_lists, model_output_lists):
        # get all unique labels for the current pair of lists
        all_labels = list(set(model_list) | set(gt_list))

        # convert lists to binary labels of 0/1
        y_true = [1 if label in gt_list else 0 for label in all_labels]
        y_pred = [1 if label in model_list else 0 for label in all_labels]

        precision = precision_score(y_true, y_pred, average='binary', zero_division=0)
        recall = recall_score(y_true, y_pred, average='binary', zero_division=0)
        f1 = f1_score(y_true, y_pred, average='binary', zero_division=0)

        all_precision.append(precision)
        all_recall.append(recall)
        all_f1.append(f1)

    # calculate average metrics
    avg_precision = sum(all_precision) / len(all_precision)
    avg_recall = sum(all_recall) / len(all_recall)
    avg_f1 = sum(all_f1) / len(all_f1)

    return avg_precision, avg_recall, avg_f1


# file path
for type in ['ownership', 'containment']:
    for hop in ['indirect']:
        for model in ['llama','gpt','qwen']:
            for step in ['0','1','2','3']:
                ground_truth_path = f"./{type}/gt_{type}/gt_{type}_{hop}_{step}.txt"
                model_output_path = f"./{type}/{model}_{type}/{model}_nl_{hop}_{step}.txt"
                print("Current file being calcluated: ",model_output_path)
                metrics = calculate_metrics(ground_truth_path, model_output_path)
                print(metrics)
                file_folder =  f"./scores/{type}/{hop}/{model}"

                if not os.path.exists(file_folder):
                    os.makedirs(file_folder)

                file_name = f"nl_{step}.txt"

                #file_name = os.path.basename(file_name)
                destination_path = os.path.join(file_folder, file_name)
                print(destination_path)
                
                mode = "a" if os.path.exists(destination_path) else "w"
                with open(destination_path, mode, encoding="utf-8") as file:
                    if mode == "a":
                        file.write("\n==================================\n")
                    file.write(str(metrics))

    #avg_precision, avg_recall, avg_f1 = metrics
    # print(f"Average Precision: {avg_precision}")
    # print(f"Average Recall: {avg_recall}")
    # print(f"Average F1 Score: {avg_f1}")


