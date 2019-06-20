package com.exrates.selenium.page_objects;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExratesAllURLsForUnauthorizedUsers {

    public Set<String> parseURLs(String file) throws Exception {
        Predicate<String> urlsFilter = Pattern
                .compile("^(http|https?:\\/\\/)[a-z0-9_\\.]")
                .asPredicate();

        Path path = Paths.get(ClassLoader.getSystemResource(file).toURI());
        Stream<String> lineStream = Files.lines(path);
        return lineStream
                .filter(urlsFilter)
                .filter(url -> url.contains("https://") && !url.contains("linkedin.com") && !url.contains("linkedin.com") && !url.contains("void(0)"))
                .filter(url -> url.contains("exrates") || url.contains("t.me"))
                .collect(Collectors.toSet());
    }

    public int getResponseCode(String url) {
        int statusCode = 0;
        try {
            statusCode = Request
                    .Get(url)
                    .connectTimeout(10000)
                    .execute()
                    .returnResponse()
                    .getStatusLine()
                    .getStatusCode();
        } catch (IOException e) {
            // skip
        }
        return statusCode;
    }

    public Map<Integer, List<String>> sortURLsByResponseCode(String file) throws Exception {
        return parseURLs(file)
                .stream()
                .collect(Collectors.groupingBy(this::getResponseCode));
    }
}
