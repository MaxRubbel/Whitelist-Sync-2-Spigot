package pw.twpi.whitelistsync.json;

import com.google.gson.*;
import pw.twpi.whitelistsync.WhitelistSync;
import pw.twpi.whitelistsync.models.WhitelistedPlayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Class to read json data from the server's whitelist.json file
 * @author Richard Nader, Jr. <rmnader@svsu.edu>
 */
public class WhitelistedPlayersFileUtilities {

    private static JsonParser parser = new JsonParser();

    // Get Arraylist of whitelisted players on server.
    public static ArrayList<WhitelistedPlayer> getWhitelistedPlayers() {
        ArrayList<WhitelistedPlayer> users = new ArrayList<>();

        // Get Json data
        getWhitelistedPlayersFromFile().forEach((user) -> {
            String uuid = ((JsonObject) user).get("uuid").getAsString();

            JsonElement nameElement = ((JsonObject) user).get("name");
            String name = "";
            if(nameElement != null) {
                name = nameElement.getAsString();
            }

            // Create DTO
            WhitelistedPlayer whitelistedPlayer = new WhitelistedPlayer();
            whitelistedPlayer.setUuid(uuid);
            whitelistedPlayer.setName(name);
            whitelistedPlayer.setWhitelisted(true);


            users.add(whitelistedPlayer);
        });

        return users;
    }

    private static JsonArray getWhitelistedPlayersFromFile() {
        JsonArray whitelist = null;
        try {
            // Read data as Json array from server directory
            FileReader fileReader = new FileReader(WhitelistSync.SERVER_FILEPATH + "/whitelist.json");
            whitelist = (JsonArray) parser.parse(fileReader);

            fileReader.close();
            // WhitelistSync.LOGGER.debug("getWhitelistedPlayersFromFile returned an array of " + whitelist.size() + " entries.");
        } catch (FileNotFoundException e) {
            WhitelistSync.LOGGER.severe("whitelist.json file not found.");
            e.printStackTrace();
        } catch (JsonParseException e) {
            WhitelistSync.LOGGER.severe("whitelist.json parse error.");
            e.printStackTrace();
        } catch (IOException e) {
            WhitelistSync.LOGGER.severe("whitelist.json read error.");
            e.printStackTrace();
        }

        return whitelist;
    }

}
