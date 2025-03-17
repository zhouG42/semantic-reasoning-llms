from openai import OpenAI
import os

# load kg file
with open('../KG/WiSoKGV4.ttl', 'r', encoding='utf-8') as file:
    kg = file.read()
#print(kg)


client = OpenAI(
base_url = 'https://hub.nhr.fau.de:8443/llm/ollama/llama3.1-70b/v1',
api_key= "API_KEY",
)


################  Ownership rules desctiption  #####################
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

#################  Ownership rules desctiption   ####################
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


response = client.chat.completions.create(
      model="llama3.1:70b",
      #model="llama3.2:3b",
      temperature=0,
      messages=[
      {"role": "system",  "content": "You are an assistant that helps with finding device URIs via querying and reasoning over knowledge graphs."},
      {"role": "user", "content": f'''
       Given the following knowledge graph in Turtle format:
        {kg}
        and the following rule description:
        {nl_indirect_ownership}
        Perform the following queries one by one and return only the URIs of the found devices: 
        [
            1. Find all devices temporarily owned by all people
            2. Find all devices temporarily owned by research assistants
            3. Find all devices temporarily owned by people in offices
            4. Find all lights temporarily owned by all people
            5. Find all devices temporarily owned by research assistants in offices
            6. Find all lights temporarily owned by research assistants
            7. Find all lights temporarily owned by people in offices
            8. Find all lights temporarily owned by research assistants in offices

        ]
       **Output Rules:**
       - Return only a list of short URIs (without prefix) corresponding to the found devices.
       - Do NOT include SPARQL queries, intermediate reasoning steps, or explanations.
       - If no devices are found, return an empty list (`[]`).
        '''
    }
      ]
)
print(response.choices[0].message.content) 

file_path = "llama_nl_indirect.txt"
content = response.choices[0].message.content
mode = "a" if os.path.exists(file_path) else "w"

with open(file_path, mode, encoding="utf-8") as file:
    if mode == "a":
        file.write("\n==================================\n")
    file.write(content)

"""
prompt test:
        and the following rule description: 
        {jena_direct_containment}


        and the following rule description: 
        {nl_direct_ownership}
# CoT: Do not include SPARQL queries. Think step by step.
# Do not include SPARQL queries, intermediate reasoning steps, intermediate reasoning steps, or explanations in the result.
Your answer must only contain the final results in the format:
        - person: URI, device: URI.  
        Do not include SPARQL queries. Think step by step.
"""
