package my.education.iexcloudapidemo.restclient;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Component
public class RestClientInvoker {

    @Setter private RestClientCommand<?> restClientCommand;

    public CompletableFuture<?> executeAsyncRequest() {
        return restClientCommand.executeAsync();
    }
}
