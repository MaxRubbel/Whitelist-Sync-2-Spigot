package pw.twpi.whitelistsync2.json;

import net.minecraft.util.com.google.gson.*;
import pw.twpi.whitelistsync2.WhitelistSync2;
import pw.twpi.whitelistsync2.models.OppedPlayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * Class to read json data from the server's ops.json file
 * @author Richard Nader, Jr. <rmnader@svsu.edu>
 */
public class OppedPlayersFileUtilities {
    
    private static JsonParser parser = new JsonParser();

    // Get Arraylist of opped players on server.
    public static ArrayList<OppedPlayer> getOppedPlayers() {
        ArrayList<OppedPlayer> users = new ArrayList<>();

        // Get Json data
        getOppedPlayersFromFile().forEach((user) -> {
            String uuid = ((JsonObject) user).get("uuid").getAsString();

            JsonElement nameElement = ((JsonObject) user).get("name");
            String name = "";
            if(nameElement != null) {
                name = nameElement.getAsString();
            }

            // Create DTO
            OppedPlayer oppedPlayer = new OppedPlayer();
            oppedPlayer.setUuid(uuid);
            oppedPlayer.setName(name);
            oppedPlayer.setIsOp(true);

            
            users.add(oppedPlayer);
        });

        return users;
    }

    private static JsonArray getOppedPlayersFromFile() {
        JsonArray oplist = null;
        try {
            // Read data as Json array from server directory
            oplist = (JsonArray) parser.parse(new FileReader(WhitelistSync2.SERVER_FILEPATH + "/ops.json"));
            
            // WhitelistSync2.LOGGER.debug("getOppedPlayersFromFile returned an array of " + oplist.size() + " entries.");
        } catch (FileNotFoundException e) {
            WhitelistSync2.LOGGER.severe("ops.json file not found.");
            e.printStackTrace();
        } catch (JsonParseException e) {
            WhitelistSync2.LOGGER.severe("ops.json parse error.");
            e.printStackTrace();
        }
        
        return oplist;
    }
    
}
