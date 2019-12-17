package pl.furmanj.face_recognition;




import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.furmanj.face_recognition.model.FaceObject;
import pl.furmanj.face_recognition.model.ImageURL;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

@Controller
public class FaceAPIClient {

    private static final String FACE_BASE_API_URL ="https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect?";

    @Value("${Ocp-Apim-Subscription-Key}")
    private String ocpApimSubscriptionKey;

    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ImageURL> imageURLHttpEntity = getHttpEntity("https://s6.ifotos.pl/img/jkjpg_qaraaer.jpg");

        ResponseEntity<FaceObject[]> exchange = restTemplate.exchange(getApiUrl(), HttpMethod.POST, imageURLHttpEntity, FaceObject[].class);
        Stream.of(exchange.getBody()).forEach(System.out::println);
    }

    private HttpEntity<ImageURL> getHttpEntity(String url) {
        ImageURL imageURL = new ImageURL(url);
        HttpHeaders httpHeaders = getHttpHeaders();
        return new HttpEntity<>(imageURL, httpHeaders);
    }

    private URI getApiUrl() {

        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(FACE_BASE_API_URL);
            uriBuilder.addParameter("returnFaceId", "true");
            uriBuilder.addParameter("returnFaceLandmarks", "false");
            uriBuilder.addParameter("returnFaceAttributes", "age,gender");
            uriBuilder.addParameter("recognitionModel", "recognition_01");
            uriBuilder.addParameter("returnRecognitionModel", "false");
            uriBuilder.addParameter("detectionModel", "detection_01");
            uri = uriBuilder.build();


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;

    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Ocp-Apim-Subscription-Key", ocpApimSubscriptionKey);
        return httpHeaders;
    }
}
