package mcc.timer.scripts;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mcc.MCC;
import mcc.timer.Timer;
import mcc.timer.scripts.trigger.ScriptTrigger;
import mcc.timer.scripts.trigger.TimeTrigger;
import mcc.yml.Constants;

public class ScriptManager {
    
    private static Map<String, Map<String, List<Script>>> scripts;

    static {
        scripts = new HashMap<>();

        File scriptDir = new File(new File("").getAbsolutePath() + "/" + Constants.FOLDER_CONFIGS + "/scripts");
        if (!scriptDir.exists()) {
            scriptDir.mkdirs();
        }

        try {
            File[] files = scriptDir.listFiles();
            for (File scriptFile : files) {
                if (!scriptFile.isFile()) continue;

                JsonObject json = JsonParser.parseReader(new FileReader(scriptFile)).getAsJsonObject();
                Script script = parse(json);

                Map<String, List<Script>> componentScripts = scripts.getOrDefault(script.getComponent(), new HashMap<>());
                List<Script> stateScripts = componentScripts.getOrDefault(script.getState(), new ArrayList<>());
                stateScripts.add(script);
                componentScripts.put(script.getState(), stateScripts);
                scripts.put(script.getComponent(), componentScripts);

                System.out.println("Loaded script: " + script.getName());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            MCC.markStaticLoadError();
        }
    }

    public static void tick(String component, String state, Timer timer, long now) {
        Map<String, List<Script>> componentScripts = scripts.get(component);
        if (componentScripts == null) return;

        List<Script> stateScripts = componentScripts.get(state);
        if (stateScripts == null) return;

        for (Script script : stateScripts) {
            script.tick(timer, now);
        }
    }

    public static Script parse(JsonObject scriptJson) {
        String name = scriptJson.get("name").getAsString();
        String component = scriptJson.get("component").getAsString();
        String state = scriptJson.get("state").getAsString();
        
        Script script = new Script(name, component, state);

        JsonObject events = scriptJson.get("events").getAsJsonObject();
        for (String eventKey : events.keySet()) {
            JsonObject event = events.get(eventKey).getAsJsonObject();
            String type = event.get("type").getAsString();
            JsonArray values = event.get("values").getAsJsonArray();

            String[] valuesArray = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                valuesArray[i] = values.get(i).getAsString();
            }

            ScriptTrigger trigger = parseTrigger(eventKey);
            ScriptAction action = parseAction(type);

            ScriptEvent scriptEvent = new ScriptEvent(trigger, action, valuesArray);
            script.addEvent(scriptEvent);
        }

        return script;
    }

    private static ScriptAction parseAction(String type) {
        if (type.equalsIgnoreCase("chatbox")) {
            return ScriptAction.CHATBOX;
        } else {
            return ScriptAction.ERROR;
        }
    }

    private static ScriptTrigger parseTrigger(String value) {
        if (value.matches("^\\d+s$")) {
            return new TimeTrigger(TimeUnit.SECONDS, Integer.parseInt(value.substring(0, value.length() - 1)));
        } else if (value.equalsIgnoreCase("init")) {
            return ScriptTrigger.ALWAYS;
        } else {
            System.err.println("Failed to parse trigger: " + value);
            return ScriptTrigger.NEVER;
        }
    }
}
