package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // return statement included so that the starter code can compile and run.
        try {
            // Construct the API URL for the given breed
            String url = "https://dog.ceo/api/breed/" + breed + "/list";

            // Create and execute the request
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            // Check if the HTTP request was successful
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException("Breed not found: " + breed);
            }
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            JSONArray subBreedsArray = json.getJSONArray("message");
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < subBreedsArray.length(); i++) {
                subBreeds.add(subBreedsArray.getString(i));
            }

            return subBreeds;
        }
        catch (IOException e) {
            // Network or IO errors are reported as BreedNotFoundException
            throw new BreedNotFoundException("Failed to fetch sub-breeds for breed: " + breed);
        } catch (Exception e) {
            // Any other errors (JSON parsing, etc.) are also reported as BreedNotFoundException
            throw new BreedNotFoundException("Error processing sub-breeds for breed: " + breed);
        }

    }
}