package my.education.iexcloudapidemo.restclient;

import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

public interface RestClientCommand<E> {

    CompletableFuture<E> executeAsync();

}
