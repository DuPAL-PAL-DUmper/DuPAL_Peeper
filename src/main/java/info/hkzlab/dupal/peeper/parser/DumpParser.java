package info.hkzlab.dupal.peeper.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import info.hkzlab.dupal.peeper.devices.PALSpecs;
import info.hkzlab.dupal.peeper.parser.states.*;

public class DumpParser {
    private DumpParser() {
    };

    public static String extractPALName(JSONObject root) {
        return root.getJSONObject("header").getJSONObject("PAL").getString("type");
    }

    public static PALSpecs getPALType(JSONObject root) {
        String palName = extractPALName(root);
        PALSpecs pspecs = null;

        Class<?> specsClass;
        try {
            specsClass = Class.forName("info.hkzlab.dupal.peeper.devices.PAL" + palName.toUpperCase() + "Specs");
            pspecs = (PALSpecs) specsClass.getConstructor().newInstance(new Object[] {});
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            pspecs = null;
        }

        return pspecs;
    }
    
    public static int extractIOasOutMask(JSONObject root) {
        if(!root.getJSONObject("header").getJSONObject("PAL").has("IOsAsOUT")) return 0;

        return root.getJSONObject("header").getJSONObject("PAL").getInt("IOsAsOUT");
    }

    public static SimpleState[] extractSimpleStates(JSONObject root) {
        ArrayList<SimpleState> ssList = new ArrayList<>();

        if(!root.has("states")) return null;
        JSONArray ssArray = root.getJSONArray("states");

        for(int idx = 0; idx < ssArray.length(); idx++) {
            JSONObject ss = ssArray.getJSONObject(idx);
            ssList.add(new SimpleState(ss.getInt("in"), ss.getInt("out"), ss.getInt("hiz")));
        }

        return ssList.toArray(new SimpleState[ssList.size()]);
    }

    public static OLink[] extractOLinks(JSONObject root) {
        ArrayList<OLink> olList = new ArrayList<>();

        if(!root.has("states")) return null;
        if(!root.has("oLinks")) return null;
        JSONArray olArray = root.getJSONArray("oLinks");
        JSONArray states = root.getJSONArray("states");

        Map<Integer, OutStatePins> stateMap = new HashMap<>();
        for(int idx = 0; idx < states.length(); idx++) {
            JSONObject jstate = states.getJSONObject(idx);
            stateMap.put(Integer.valueOf(jstate.getInt("hash")), new OutStatePins(jstate.getInt("out"), jstate.getInt("hiz")));
        }

        for(int idx = 0; idx < olArray.length(); idx++) {
            JSONObject ol = olArray.getJSONObject(idx);
            OutStatePins src = stateMap.get(Integer.valueOf(ol.getInt("src")));
            OutStatePins dst = stateMap.get(Integer.valueOf(ol.getInt("dst")));

            olList.add(new OLink(ol.getInt("inputs"), src, dst));
        }

        return olList.toArray(new OLink[olList.size()]);
    }

    public static RLink[] extractRLinks(JSONObject root) {
        ArrayList<RLink> rlList = new ArrayList<>();

        if(!root.has("rLinks")) return null;
        if(!root.has("states")) return null;
        JSONArray states = root.getJSONArray("states");
        JSONArray rlArray = root.getJSONArray("rLinks");

        Map<Integer, OutStatePins> stateMap = new HashMap<>();
        for(int idx = 0; idx < states.length(); idx++) {
            JSONObject jstate = states.getJSONObject(idx);
            stateMap.put(Integer.valueOf(jstate.getInt("hash")), new OutStatePins(jstate.getInt("out"), jstate.getInt("hiz")));
        }

        for(int idx = 0; idx < rlArray.length(); idx++) {
            JSONObject rl = rlArray.getJSONObject(idx);
            OutStatePins src = stateMap.get(Integer.valueOf(rl.getInt("src")));
            OutStatePins mid = stateMap.get(Integer.valueOf(rl.getInt("mid")));
            OutStatePins dst = stateMap.get(Integer.valueOf(rl.getInt("dst")));

            rlList.add(new RLink(rl.getInt("inputs"), src, mid, dst));
        }
        return rlList.toArray(new RLink[rlList.size()]);
    }
}
