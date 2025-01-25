import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConverterAPI {
    public static void main(String[] args) throws IOException {
        // Create a HashMap for currency codes
        HashMap<Integer, String> currencyCodes = new HashMap<>();
        // Add currency codes
        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "CAD");
        currencyCodes.put(3, "EUR");
        currencyCodes.put(4, "HKD");
        currencyCodes.put(5, "INR");

        String fromCode, toCode;
        double amount;
        Scanner sc = new Scanner(System.in);
        System.out.println("WELCOME TO THE CURRENCY CONVERTER!");

        System.out.println("Currency converting from?");
        System.out.println("1:USD (US Dollar)\t 2:CAD (Canadian Dollar)\t 3:EUR (Euro)\t 4:HKD (Hong Kong Dollar)\t 5:INR (Indian Rupee)");
        fromCode = currencyCodes.get(sc.nextInt());
        sc.nextLine();  // Consume newline

        System.out.println("Currency converting to?");
        System.out.println("1:USD (US Dollar)\t 2:CAD (Canadian Dollar)\t 3:EUR (Euro)\t 4:HKD (Hong Kong Dollar)\t 5:INR (Indian Rupee)");
        toCode = currencyCodes.get(sc.nextInt());
        sc.nextLine();  // Consume newline

        System.out.println("Amount you wish to convert:");
        amount = sc.nextFloat();

        // Call the method to get the conversion rate and perform the conversion
        sendHttpGETRequest(fromCode, toCode, amount);
        System.out.println("Thanks for using this Currency converter!");
    }

    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) {
        // Replace with your actual API key
        String apiKey = "d0ac12465cbf10cf25c12213";  // Your actual API key here

        // Construct the API URL
        String urlString = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + fromCode + "?symbols=" + toCode;

        try {
            // Making the HTTP request
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            // Get the response code
            int responseCode = httpURLConnection.getResponseCode();

            // Read the response
            StringBuilder response = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response JSON
                JSONObject obj = new JSONObject(response.toString());

                // Check if the conversion rates object exists and if the rate for the desired currency is available
                if (obj.has("conversion_rates") && obj.getJSONObject("conversion_rates").has(toCode)) {
                    double exchange = obj.getJSONObject("conversion_rates").getDouble(toCode);
                    // Print the conversion result
                    System.out.println(amount + " " + fromCode + " = " + (amount * exchange) + " " + toCode);
                } else {
                    System.out.println("Error: Conversion rate not found for " + toCode);
                }
            } else {
                System.out.println("GET request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("Error making the request: " + e.getMessage());
        }
    }
}
