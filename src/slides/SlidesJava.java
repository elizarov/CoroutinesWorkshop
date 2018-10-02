package slides;

import java.util.concurrent.CompletableFuture;

public class SlidesJava {
    CompletableFuture<Image> loadImageAsync(String name) {
        return null;
    }

    Image combineImages(Image image1, Image image2) {
        return null;
    }

    CompletableFuture<Image> loadAndCombineAsync0(String name1, String name2) {
        CompletableFuture<Image> future1 = loadImageAsync(name1);
        CompletableFuture<Image> future2 = loadImageAsync(name2);
        return future1.thenCompose(image1 ->
            future2.thenCompose(image2 ->
                CompletableFuture.supplyAsync(() ->
                    combineImages(image1, image2))));
    }

    CompletableFuture<Image> loadAndCombineAsync(String name1, String name2) {
        CompletableFuture<Image> future1 = loadImageAsync(name1);
        CompletableFuture<Image> future2 = loadImageAsync(name2);
        return future1.thenCombine(future2, this::combineImages);
    }

}
