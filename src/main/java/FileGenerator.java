

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileGenerator {



    public static void main(String[] args) {

        new FileGenerator();

    }

    public FileGenerator() {



        try {

            File f = new File("sessions.txt");
            List<String> current = Files.readAllLines(f.toPath());
            List<String> fixed = new ArrayList<>();
            for (String s : current) {

                String username = s.split(":")[0];
                String UUID = getUUID(username);
                fixed.add(s + ":" + UUID);

            }

            FileWriter fw = new FileWriter(f);
            for (String s : fixed) {

                fw.write(s + System.getProperty("line.separator"));

            }
            fw.flush();
            fw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private String getUUID(String name) {

        try {

            URL url = new URL("https://playerdb.co/api/player/minecraft/"+name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.connect();
            if (connection.getInputStream() == null) {
                System.out.println("ERROR GETTING UUID " + name);
                return null;
            }
            InputStream is = connection.getInputStream();
            JSONParser jp = new JSONParser();
            String obj = jp.parse(new InputStreamReader(is, "UTF-8")).toString(); //converting Object to String
            JSONObject jsonObject = new JSONObject(obj);
            JSONObject target = jsonObject.getJSONObject("data").getJSONObject("player");
            String strUUID = target.getString("id");
            return strUUID;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
