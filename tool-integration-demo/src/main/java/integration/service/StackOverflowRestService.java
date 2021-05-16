package integration.service;

import com.intellij.openapi.diagnostic.Logger;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import integration.domain.StackOverflowQuestion;
import kotlin.text.Charsets;
import net.minidev.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public final class StackOverflowRestService
{
    private static final Logger LOG = Logger.getInstance(StackOverflowRestService.class);

    private StackOverflowRestService()
    {
    }

    public static List<StackOverflowQuestion> getStackOverflowPosts(String tag, String title) throws IOException, URISyntaxException
    {
        String stackBaseUrl = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=%s&intitle=%s&site=stackoverflow&filter=!5RCI6pSlo)ARWwn9kj2xie(vx";

        String searchUrl = String.format(stackBaseUrl, URLEncoder.encode(tag, Charsets.UTF_8), URLEncoder.encode(title, Charsets.UTF_8));

        HttpURLConnection connection = (HttpURLConnection) new URI(searchUrl).toURL().openConnection();
        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream()))))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                responseBuilder.append(line);
            }
        }
        catch (IOException e)
        {
            LOG.error("Err something went wrong!", e.getMessage());
            // In case demo fails!
            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(StackOverflowRestService.class.getResourceAsStream("/response.json"))))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    responseBuilder.append(line).append(System.lineSeparator());
                }
            }
        }

        DocumentContext parse = JsonPath.parse(responseBuilder.toString());
        List<Map<String, Object>> items = parse.read("items");
        List<StackOverflowQuestion> questions = new ArrayList<>();
        for (Map<String, Object> eachItem : items)
        {
            StackOverflowQuestion question = new StackOverflowQuestion();
            JSONArray tags = (JSONArray) eachItem.get("tags");
            question.setTags(tags.stream().map(Object::toString).collect(Collectors.joining(", ")));
            question.setLink(eachItem.get("link").toString());
            question.setTitle(eachItem.get("title").toString());
            question.setAnswerCount(Integer.valueOf(eachItem.get("answer_count").toString()));
            String creationDateString = null == eachItem.get("creation_date") ? "0" : eachItem.get("creation_date").toString();
            String lastActivityDateString = null == eachItem.get("last_activity_date") ? "0" : eachItem.get("last_activity_date").toString();
            long lastActivityDate = Long.parseLong(lastActivityDateString);
            question.setLastActivity(Date.from(Instant.ofEpochSecond(lastActivityDate)));
            long creationDate = Long.parseLong(creationDateString);
            question.setCreationDate(Date.from(Instant.ofEpochSecond(creationDate)));
            questions.add(question);
        }
        return questions;
    }
}
