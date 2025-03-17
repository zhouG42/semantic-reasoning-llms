import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class DefineRules {
    public static void main(String[] args) {
        try {
        // load KG data
        String inputFileName = "../data/WiSoKG.ttl";

        Model model = ModelFactory.createDefaultModel();
        FileManager.get().readModel(model, inputFileName);

        // define reasoning rules
        String rules =

            // Rule 1_0: Find all personal devices
            "[personDeviceRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                    -> (?person <http://example.com/ontology#ownsDevice> ?device)] " +


            // Rule 1_1: Find all personal devices owned by research assistants
            "[RAPersonalDeviceRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                       (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                        -> (?person <http://example.com/ontology#RAownsPersonalDevice> ?device)] " +

            // Rule 1_1: Find all personal lights
            "[personalLightRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                    (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                     -> (?person <http://example.com/ontology#ownsPersonalLight> ?device)] " +

            // Rule 1_1: Find all the personal devices owned by external PhD students
            "[PhDPersonalDeviceRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                        (?person <https://schema.org/jobTitle> 'External PhD Student') " +
            "                         -> (?person <http://example.com/ontology#PhDownsPersonalDevice> ?device)] " +

            // Rule 1_1: Find all personal speakers
            "[personalSpeakerRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                      (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Speaker>) " +
            "                       -> (?person <http://example.com/ontology#ownsPersonalSpeaker> ?device)] " +

            // Rule 1_1: Find all personal kettles
            "[personalKettleRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                     (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Kettle>) " +
            "                      -> (?person <http://example.com/ontology#ownsPersonalKettle> ?device)] " +

            // Rule 1_1: Find all personal LED strips
            "[personalLEDStripRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                       (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/LedStrip>) " +
            "                        -> (?person <http://example.com/ontology#ownsPersonalLEDStrip> ?device)] " +

            // Rule 1_1: Find all personal radios
            "[personalRadioRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                    (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Radio>) " +
            "                     -> (?person <http://example.com/ontology#ownsPersonalRadio> ?device)] " +


            // Rule 1_2: Find all personal lights owned by research assistants
            "[RAPersonalLightRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                      (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                      (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                       -> (?person <http://example.com/ontology#RAownsPersonalLight> ?device)] " +

            // Rule 1_2: Find all personal speakers owned by research assistants
            "[RAPersonalSpeakerRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                        (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                        (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Speaker>) " +
            "                         -> (?person <http://example.com/ontology#RAownsPersonalSpeaker> ?device)] " +

            // Rule 1_2: Find all personal kettles owned by research assistants
            "[RAPersonalKettleRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                       (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                       (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Kettle>) " +
            "                        -> (?person <http://example.com/ontology#RAownsPersonalKettle> ?device)] " +

            // Rule 1_2: Find all personal LED strips owned by research assistants
            "[RAPersonalLEDStripRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                         (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                         (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/LedStrip>) " +
            "                          -> (?person <http://example.com/ontology#RAownsPersonalLEDStrip> ?device)] " +

            // Rule 1_2: Find all personal lights owned by external PhD students
            "[PhDPersonalLightRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                       (?person <https://schema.org/jobTitle> 'External PhD Student') " +
            "                       (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                        -> (?person <http://example.com/ontology#PhDownsPersonalLight> ?device)] " +

            // Rule 1_2: Find all personal radios owned by external PhD students
            "[PhDPersonalRadioRule: (?person <http://example.com/ontology#ownsElement> ?device) " +
            "                       (?person <https://schema.org/jobTitle> 'External PhD Student') " +
            "                       (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Radio>) " +
            "                        -> (?person <http://example.com/ontology#PhDownsPersonalRadio> ?device)] " +



            // Rule 2_0: Find all the devices temporarily owned by people
            "[tempPersonDeviceRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "                       (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                        -> (?person <http://example.com/ontology#ownsTemp> ?device)] " +


            // Rule 2_1: Find all devices temporarily owned by research assistants
            "[RADeviceRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "               (?place <https://w3id.org/bot#containsElement> ?device) " +
            "               (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                -> (?person <http://example.com/ontology#RAOwnsDevice> ?device)] " +

            // Rule 2_1: Find all devices temporarily owned by people in all offices
            "[personOfficeDeviceRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "                         (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                         (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                          -> (?person <http://example.com/ontology#ownsOfficeDevice> ?device)] " +

            // Rule 2_1: Find all the lights temporarily owned by all people
            "[personLightRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "                  (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                  (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                   -> (?person <http://example.com/ontology#ownsLight> ?device)] " +


            // Rule 2_2: Find all devices in offices temporarily owned by research assistants
            "[RAOfficeDeviceRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "                     (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                     (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                     (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                      -> (?person <http://example.com/ontology#RAOwnsOfficeDevice> ?device)] " +

            // Rule 2_2: Find all lights temporarily owned by all research assistants
            "[RALightRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "              (?place <https://w3id.org/bot#containsElement> ?device) " +
            "              (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "              (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "               -> (?person <http://example.com/ontology#RAOwnsLight> ?device)] " +

            // Rule 2_2: Find all lights temporarily owned by all people in offices
            "[personOfficeLightRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "                        (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                        (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                        (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                         -> (?person <http://example.com/ontology#ownsOfficeLight> ?device)] " +


            // Rule 2_3: Find all lights temporarily owned by research assistants in all offices
            "[RAOfficeLightRule: (?person <http://example.com/ontology#currentLocation> ?place) " +
            "                    (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                    (?person <https://schema.org/jobTitle> 'Research Assistant') " +
            "                    (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                    (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                     -> (?person <http://example.com/ontology#RAOwnsOfficeLight> ?device)] " +



            // Rule 1_0: Find all the devices in all rooms
            "[roomDeviceRule: (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                  -> (?place <http://example.com/ontology#containsElement> ?device)] " +


            // Rule 1_1: Find all the devices in all offices
            "[officeDeviceRule: (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                   (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                    -> (?place <http://example.com/ontology#OfficeContainsElement> ?device)] " +

            // Rule 1_1: Find all the lights in all rooms
            "[roomLightRule: (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                 -> (?place <http://example.com/ontology#RoomContainsLight> ?device)] " +


            // Rule 1_2: Find all the lights in all offices
            "[officeLightRule: (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                  (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                  (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                   -> (?place <http://example.com/ontology#OfficeContainsLight> ?device)] " +


            // Rule 2_0: Find all devices in all storeys
            "[storeyDeviceRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                   (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                    -> (?storey <http://example.com/ontology#containsDevice> ?device)] " +


            // Rule 2_1: Find all devices in storey 4
            "[storey4DeviceRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                    (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                    (?storey <http://www.w3.org/2000/01/rdf-schema#label> 'WiSo 4th Floor') " +
            "                     -> (?storey <http://example.com/ontology#storey4ContainsDevice> ?device)] " +

            // Rule 2_1: Find all devices in all offices in all storeys
            "[storeyOfficeDeviceRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                         (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                         (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                          -> (?storey <http://example.com/ontology#containsOfficeDevice> ?device)] " +

            // Rule 2_1: Find all lights in all storeys
            "[storeyLightRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                  (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                  (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                   -> (?storey <http://example.com/ontology#containsLight> ?device)] " +


            // Rule 2_2: Find all devices contained in offices in storey 4
            "[storey4OfficeDeviceRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                          (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                          (?storey <http://www.w3.org/2000/01/rdf-schema#label> 'WiSo 4th Floor') " +
            "                          (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                           -> (?storey <http://example.com/ontology#storey4ContainsOfficeDevice> ?device)] " +

            // Rule 2_2: Find all lights contained in storey 4
            "[storey4LightRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                   (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                   (?storey <http://www.w3.org/2000/01/rdf-schema#label> 'WiSo 4th Floor') " +
            "                   (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                    -> (?storey <http://example.com/ontology#storey4ContainsLight> ?device)] " +

            // Rule 2_2: Find all the lights in all offices in all storeys
            "[storeyOfficeLightRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                        (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                        (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                        (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                         -> (?storey <http://example.com/ontology#containsOfficeLight> ?device)] " +


            // Rule 2_3: Find all lights in all offices in storey 4
            "[storey4OfficeLightRule: (?storey <https://w3id.org/bot#hasSpace> ?place) " +
            "                         (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                         (?storey <http://www.w3.org/2000/01/rdf-schema#label> 'WiSo 4th Floor') " +
            "                         (?place <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/ontology#Office>) " +
            "                         (?device <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/data/devices/Light>) " +
            "                          -> (?storey <http://example.com/ontology#storey4ContainsOfficeLight> ?device)] "+



            // Rule unavailable: Find all the devices owned by people with unavailable current location
            "[unavailPersonDeviceRule:  (?person <http://example.com/ontology#currentLocation> 'Unavailable') " +
            "                           (?person <https://schema.org/workLocation> ?place) " +
            "                           (?place <https://w3id.org/bot#containsElement> ?device) " +
            "                            -> (?person <http://example.com/ontology#ownsUnavail> ?device)] " ;



        // create a reasoner with the combined rules
            Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
            InfModel infModel = ModelFactory.createInfModel(reasoner, model);

            // SPARQL queries for each rule type
            String[] queries = {
                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsDevice ?device }",


                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAownsPersonalDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsPersonalLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:PhDownsPersonalDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsPersonalSpeaker ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsPersonalKettle ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsPersonalLEDStrip ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsPersonalRadio ?device }",


                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAownsPersonalLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAownsPersonalSpeaker ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAownsPersonalKettle ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAownsPersonalLEDStrip ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:PhDownsPersonalLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:PhDownsPersonalRadio ?device }",



                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsTemp ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAOwnsDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsOfficeDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAOwnsOfficeDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAOwnsLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsOfficeLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:RAOwnsOfficeLight ?device }",



                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?place ex:containsElement ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?place ex:OfficeContainsElement ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?place ex:RoomContainsLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?place ex:OfficeContainsLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:containsDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:storey4ContainsDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:containsOfficeDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:containsLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:storey4ContainsOfficeDevice ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:storey4ContainsLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:containsOfficeLight ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?storey ex:storey4ContainsOfficeLight ?device }",



                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device WHERE { ?person ex:ownsUnavail ?device }",

                "PREFIX ex: <http://example.com/ontology#> " +
                "SELECT ?device1 ?device2 ?device3 WHERE { {?person1 ex:ownsDevice ?device1} UNION {?person2 ex:ownsTemp ?device2} UNION {?person3 ex:ownsUnavail ?device3} }"

                };



        // rule descriptions for print out to txt file
        String[] ruleTypes = {
            "Rule 1_0: Find all personal devices ",
            "Rule 1_1: Find all personal devices owned by research assistants",
            "Rule 1_1: Find all personal lights ",
            "Rule 1_1: Find all personal devices owned by external PhD students",
            "Rule 1_1: Find all personal speakers ",
            "Rule 1_1: Find all personal kettles ",
            "Rule 1_1: Find all personal LED strips ",
            "Rule 1_1: Find all personal Radios ",
            "Rule 1_2: Find all personal lights owned by research assistants ",
            "Rule 1_2: Find all personal speakers owned by research assistants ",
            "Rule 1_2: Find all personal kettles owned by research assistants ",
            "Rule 1_2: Find all personal LED strips owned by research assistants ",
            "Rule 1_2: Find all personal lights owned by external PhD students ",
            "Rule 1_2: Find all personal radios owned by external PhD students ",


            "Rule 2_0: Find all the devices temporarily owned by people",
            "Rule 2_1: Find all devices temporarily owned by research assistants ",
            "Rule 2_1: Find all devices temporarily owned by people in all offices ",
            "Rule 2_1: Find all the lights temporarily owned by all people ",
            "Rule 2_2: Find all devices in offices temporarily owned by research assistants ",
            "Rule 2_2: Find all lights temporarily owned by all research assistants ",
            "Rule 2_2: Find all lights temporarily owned by all people in offices ",
            "Rule 2_3: Find all lights temporarily owned by research assistants in all offices ",


            "Rule 1_0: Find all the devices in all rooms ",
            "Rule 1_1: Find all the devices in all offices ",
            "Rule 1_1: Find all the lights in all rooms ",
            "Rule 1_2: Find all the lights in all offices ",
            "Rule 2_0: Find all devices in all storeys ",
            "Rule 2_1: Find all devices in storey 4 ",
            "Rule 2_1: Find all devices in all offices in all storeys ",
            "Rule 2_1: Find all lights in all storeys ",
            "Rule 2_2: Find all devices contained in offices in storey 4 ",
            "Rule 2_2: Find all lights contained in storey 4 ",
            "Rule 2_2: Find all the lights in all offices in all storeys ",
            "Rule 2_3: Find all lights in all offices in storey 4",


            "Unavailable Rule: Find all the devices owned by people with unavailable current location",
            "Ownership Rule: Find all devices owned by people (Nested If)"
        };



         // write output to txt file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("all_inferred_output.txt"))) {
                for (int i = 0; i < queries.length ; i++) {
                    writer.write("==== " + ruleTypes[i] + " ====" + System.lineSeparator());

                    Query query = QueryFactory.create(queries[i]);
                    try (QueryExecution qe = QueryExecutionFactory.create(query, infModel)) {
                        ResultSet results = qe.execSelect();

                        while (results.hasNext()) {
                            QuerySolution solution = results.nextSolution();
                            writer.write(solution.toString() + System.lineSeparator());
                        }
                    }
                    writer.write(System.lineSeparator());
                }
            }

            System.out.println("Inferred results written to all_inferred_output.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

