from openai import OpenAI
import os

# API configuration
api_key = 'API_KEY' # Replace with your API key
base_url = "https://chat-ai.academiccloud.de/v1"
#model = "meta-llama-3.1-8b-instruct" 
#model = "deepseek-r1-distill-llama-70b" 
#model = "mistral-large-instruct"
#model = "llama-3.3-70b-instruct"

model = "qwen2.5-72b-instruct"  # best accracy and fastest inference time

"""
meta-llama-3.1-8b-instruct xx
internvl2.5-8b xx
deepseek-r1 xx
deepseek-r1-distill-llama-70b ## working okay, too much middle steps
llama-3.3-70b-instruct # working quite good but slower
llama-3.1-nemotron-70b-instruct xx
llama-3.1-sauerkrautlm-70b-instruct xx
mistral-large-instruct # good and fast
qwen2.5-vl-72b-instruct xx vision
qwen2.5-72b-instruct # working best
qwen2.5-coder-32b-instruct xx
codestral-22b  xx
"""

with open('../KG/WiSoKGV4.ttl', 'r', encoding='utf-8') as file:
    kg = file.read() 


#####################################
nl_direct_ownership = "If there exists a direct link in the knowledge graph between a person and a device, then the person owns the device as a personal device."
nl_indirect_ownership = """If there exists a link in the knowledge graph between a person and a place indicating the current location of the person and the place contains devices, 
                            then the person temporarily owns the devices."""

jena_direct_ownership = """
                        [personalOwnedDeviceRule: (?person <http://example.com/ontology#ownsDevice> ?device) 
                                       -> (?person <http://example.com/ontology#ownsPersonalDevice> ?device)] 
                         """

jena_indirect_ownership = """
                        [temporarilyOwnedDeviceRule: (?person <http://example.com/ontology#currentLocation> ?place)  
                                    (?place <https://w3id.org/bot#containsElement> ?device) 
                                   -> (?person <http://example.com/ontology#ownsTemporarily> ?device)] 
                        """

#####################################
nl_direct_containment =  "If there exists a direct link in the knowledge graph between a room and a device, then the room contains the device"
nl_indirect_containment = "If there exists a link in the knowledge graph between a storey and a room, and the room contains devices, then the storey contains the devices."

jena_direct_containment = """
            [roomContainsDeviceRule: (?place <https://w3id.org/bot#containsElement> ?device) 
                              -> (?place <http://example.com/ontology#containsElement> ?device)] 
"""
jena_indirect_containment = """
        [storeyContainsDeviceRule: (?storey <https://w3id.org/bot#hasSpace> ?place) 
                           (?place <https://w3id.org/bot#containsElement> ?device) 
                               -> (?storey <http://example.com/ontology#containsDevice> ?device)] 

"""
#####################################



# Start OpenAI client
client = OpenAI(
    api_key = "27c024c52611178dc0018455fba78d43",
    base_url = base_url
)
  
# Get response
chat_completion = client.chat.completions.create(
        messages=[
      {"role": "system",  "content": "You are an assistant that helps with finding device URIs via querying and reasoning over knowledge graphs."},
      {"role": "user", "content": f'''
       Given the following knowledge graph in Turtle format:
      {kg}      
      and the following rule description:
      {jena_direct_ownership}
        Perform the following queries one by one and return only the URIs of the found devices:  
        [    
            8. Find all personal radios
            9. Find all personal lights owned by research assistants
            10. Find all personal speakers owned by research assistants
            11. Find all personal kettles owned by research assistants
            12. Find all personal LED strips owned by research assistants
            13. Find all personal lights owned by external phd students
            14. Find all personal radios owned by external phd students

        ]
       **Output Rules:**
       - Return only a list of short URIs (without prefix) corresponding to the found devices.
       - Do NOT include SPARQL queries, intermediate reasoning steps, or explanations.
       - If no devices are found, return an empty list (`[]`).
        '''
    }
            
            ],
        model= model,
    )
  
# Print full response as JSON
#print(chat_completion) # You can extract the response text from the JSON object
print(chat_completion.choices[0].message.content) 

file_path = "qwen_jena_direct.txt"
content = chat_completion.choices[0].message.content
mode = "a" if os.path.exists(file_path) else "w"

with open(file_path, mode, encoding="utf-8") as file:
    if mode == "a":
        file.write("\n==================================\n")
    file.write(content)



"""
      and the following rule definitions:
      {jena_direct_containment}
name":"Meta Llama 3.1 8B Instruct","
""object":"model","id":"meta-llama-3.1-8b-instruct",
"created":1741272148,"status":"ready","input":["text"]},
{"output":["text"],"owned_by":"chat-ai",
"name":"InternVL2.5 8B MPO","object":"model","id":"internvl2.5-8b","created":1741272148,"status":"ready","input":["text","image"]},{"output":["text","thought"],"owned_by":"chat-ai","name":"DeepSeek R1","object":"model","id":"deepseek-r1","created":1741272148,"status":"ready","input":["text"]},{"output":["text","thought"],"owned_by":"chat-ai","name":"DeepSeek R1 Distill Llama 70B","object":"model","id":"deepseek-r1-distill-llama-70b","created":1741272148,"status":"ready","input":["text"]},{"output":["text"],"owned_by":"chat-ai","name":"Meta Llama 3.3 70B Instruct","object":"model","id":"llama-3.3-70b-instruct","created":1741272148,"status":"loading","input":["text"]},{"output":["text"],"owned_by":"chat-ai","name":"Llama 3.1 Nemotron 70B Instruct","object":"model","id":"llama-3.1-nemotron-70b-instruct","created":1741272148,"status":"ready","input":["text"]},{"output":["text"],"owned_by":"chat-ai","name":"Llama 3.1 SauerkrautLM 70B Instruct","object":"model","id":"llama-3.1-sauerkrautlm-70b-instruct","created":1741272148,"status":"ready","input":["text"]},{"output":["text"],"owned_by":"chat-ai","name":"Mistral Large Instruct","object":"model","id":"mistral-large-instruct","created":1741272148,"status":"ready","input":["text"]},{"output":["text"],"owned_by":"chat-ai","name":"Qwen 2.5 VL 72B Instruct","object":"model","id":"qwen2.5-vl-72b-instruct","created":1741272148,"status":"ready","input":["text","image","video"]},{"output":["text"],"owned_by":"chat-ai","name":"Qwen 2.5 72B Instruct","object":"model","id":"qwen2.5-72b-instruct","created":1741272148,"status":"ready","input":["text"]},{"output":["text"],"owned_by":"chat-ai","name":"Qwen 2.5 Coder 32B Instruct","object":"model","id":"qwen2.5-coder-32b-instruct","created":1741272148,"status":"ready","input":["text"]},{"output":["text"],"owned_by":"chat-ai","name":"Codestral 22B","object":"model","id":"codestral-22b","created":1741272148,"status":"ready","input":["text"]}],"object":"list"}

        4. If there exists a link called "hasSpace" connecting a storey to a room to the room contains devices, then the storey contains the devices.

        [
            1: Find all the devices in all rooms ,
            2: Find all the devices in all offices,
            3. Find all the lights in all rooms,
            4. Find all the lights in all offices,
            5. Find all devices in all storeys,
            6. Find all devices in storey 4,
            7. Find all devices in all offices in all storeys,
            8. Find all lights in all storeys,
            9. Find all devices contained in offices in storey 4,
            10. Find all lights contained in storey 4,
            11. Find all the lights in all offices in all storeys,
            12. Find all lights in all offices in storey 4
            ]

"""


