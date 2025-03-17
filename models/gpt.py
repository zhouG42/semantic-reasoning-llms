from transformers import pipeline
from openai import OpenAI
import os


client = OpenAI(api_key="API_KEY")


# load kg file
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

messages = [
    {"role": "system", "content": "You are an assistant that helps with finding device URIs via querying and reasoning over knowledge graphs."},
    {"role": "user", "content": f'''
       Given the following knowledge graph in Turtle format:
       {kg}   
        and the following rule description: 
        {jena_indirect_containment}
    Perform the following queries one by one and return only the URIs of the found devices: 
    [
            1. Find all devices in all storeys
            2. Find all devices in storey 4
            3. Find all devices in offices in all storeys
            4. Find all lights in all storeys
            5. Find all devices contained in offices in storey 4
            6. Find all lights contained in storey 4
            7. Find all lights in offices in all storeys
            8. Find all lights in offices in storey 4
    ]

    **Output Rules:**
    - Return only a list of short URIs (without prefix) corresponding to the found devices.
    - Do NOT include SPARQL queries, intermediate reasoning steps, or explanations.
    - If no devices are found, return an empty list (`[]`).
    '''
    }
]

response = client.chat.completions.create(#model="gpt-3.5-turbo",
#model="gpt-4",
#model="o1-mini",
model = "gpt-4o",          # options: "gpt-3.5-turbo", "gpt-4" (most expensive),  "gpt-4o", "gpt-4o-mini", ["o1-preview","o1-mini","o1-preview-2024-09-12"(for reasoning, not available in api call)]
messages=messages,
max_tokens=10000,          
temperature=0)


print("LLM Output:", response.choices[0].message.content.strip())

file_path = "gpt_jena_indirect.txt"
content = response.choices[0].message.content
mode = "a" if os.path.exists(file_path) else "w"

with open(file_path, mode, encoding="utf-8") as file:
    if mode == "a":
        file.write("\n==================================\n")
    file.write(content)



"""
        and the following rule description: 
        {jena_indirect_ownership}
First try: without any rule information in the definition
Second try: give rule in NL and Jena syntax
Third try: prompt to LLM to generate Jena rules instead find the final devices
"""
