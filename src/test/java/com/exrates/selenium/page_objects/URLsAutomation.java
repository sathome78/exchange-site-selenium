package com.exrates.selenium.page_objects;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class URLsAutomation {
    private final String baseDomain = "exrates.me";
    private final String domainProd = System.getProperty("domain.prod");
    private final String domainDemo = System.getProperty("domain.demo");
    private final String domainPreprod = System.getProperty("domain.preprod");
    private final String domainStaging = System.getProperty("domain.staging");

    public Set<String> parseURLs(String file) throws Exception {
        String domain = setDomain();
        Predicate<String> urlsFilter = Pattern
                .compile("^(http|https?:\\/\\/)[a-z0-9_\\.]")
                .asPredicate();

        Path path = Paths.get(ClassLoader.getSystemResource(file).toURI());
        Stream<String> urls = Files.lines(path);

        return urls
                .filter(urlsFilter)
                .filter(url -> !url.contains("instagram") && !url.contains("void(0)") && !url.contains("old.exrates.me"))
                .filter(url -> url.contains(baseDomain) || url.contains("t.me"))
                .map(url -> url.replace(baseDomain, domain))
                .map(this::setProtocol)
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

    private String setDomain() {
        final List<String> damains = Arrays.asList(domainProd, domainDemo, domainPreprod, domainStaging);
        return damains.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(""));
    }

    private String setProtocol(String url) {
        final String https = "https";
        final String http = "http";
        if (domainPreprod != null && url.contains(domainPreprod)) {
            return url.replace(https, http);
        } else if (domainStaging != null && url.contains(domainStaging)) {
            return url.replace(https, http);
        }
        return url;
    }
}
