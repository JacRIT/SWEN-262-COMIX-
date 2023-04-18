package Api;
import java.util.Map;

public class ComixAPIResponse<T>  {

    /**
     * The response of the API call
     */
    private T response;

    /**
     * A map of reasons why the call failed 
     * EX : <"copyDoesNotExist" : true>
     */
    private Map<String, Boolean> status;

    public ComixAPIResponse(T response, Map<String, Boolean> status) {
        this.response = response;
        this.status = status;
    }
    public T getResponse() {
        return this.response;
    }
    public Map<String, Boolean> getStatus() {
        return this.status;
    }
}
