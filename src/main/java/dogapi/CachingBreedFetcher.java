package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private BreedFetcher fetcher;
    private int callsMade = 0;
    private Map<String, List<String>> cache = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException {
        if (cache.containsKey(breed)) {
            return cache.get(breed);  // Already cached, return immediately
        } else {
            // Not in cache, so fetch it
            callsMade++;
            List<String> result = this.fetcher.getSubBreeds(breed);
            cache.put(breed, result);
            return result;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}