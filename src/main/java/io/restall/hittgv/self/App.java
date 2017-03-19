package io.restall.hittgv.self;

import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.Tuple;
import javaslang.collection.Map;
import javaslang.collection.Stream;
import javaslang.jackson.datatype.JavaslangModule;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.getenv;
import static spark.Spark.port;
import static spark.Spark.post;

public class App {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaslangModule());

        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        port(Integer.parseInt(getenv().get("PORT")));

        post("/", (req, res) -> {
            Body body = objectMapper.readValue(req.bodyAsBytes(), Body.class);

            Document doc = Jsoup.parse(body.getHtml(), body.getUrl());

            Elements elements = doc.select("a[href]");

            String host = new URL(body.getUrl()).getHost();



            Map<Boolean, Integer> result = elements.stream().collect(Stream.collector())
                    .map(element -> element.attr("abs:href"))
                    .filter(urlValidator::isValid)
                    .map(url -> {
                        try{
                            return new URL(url);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(String.format("%s is malformed", url));
                        }
                    })
                    .map(URL::getHost)
                    .groupBy(host::equals)
                    .map((a,b) -> Tuple.of(a, b.size()));

            return objectMapper.writeValueAsString(new Response(result));
        });

    }

}
