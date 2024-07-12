/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * La classe ChatGPTIntegration permette di interagire con l'API di OpenAI ChatGPT.
 */
public class ChatGPTIntegration {

    private static final String API_KEY = "sk-proj-9IJ8jqaQJQklLVbc1FaAT3BlbkFJhAjRuuNeuLITSaJPswQ2";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * Invoca l'API di ChatGPT con un ruolo e un messaggio specifico.
     *
     * @param role il ruolo del sistema (es. "system", "user", "assistant")
     * @param chat il messaggio dell'utente
     * @return la risposta di ChatGPT
     * @throws Exception in caso di errore nella richiesta HTTP
     */
    public static String chatGPTApi (String role, String chat) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        // Costruisci il corpo della richiesta
        Map<String, Object> message = new HashMap<>();
        message.put("role", "system");
        message.put("content", role);

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", chat);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", new Object[]{message, userMessage});
        requestBody.put("max_tokens", 110);

        String jsonRequestBody = gson.toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Estrai il contenuto della risposta di ChatGPT
            String responseBody = response.body();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            String content = jsonObject
                    .getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();

            // Stampa solo la risposta di ChatGPT
            return (content.trim());
    }
}
