package ru.verpul.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.YoutubeChannelDTO;
import ru.verpul.feign.TGBotFeign;
import ru.verpul.mapper.YoutubeChannelMapper;
import ru.verpul.model.YoutubeChannel;
import ru.verpul.repository.YoutubeChannelRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class YoutubeService {

    @Value("${youtube.api-key}")
    private String apiKey;

    @Value("${youtube.channel-id}")
    private String channelId;

    private static final String APPLICATION_NAME = "Youtube channels new videos checker";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SNIPPET = Collections.singletonList("snippet");

    private final YoutubeChannelRepository youtubeChannelRepository;
    private final YoutubeChannelMapper youtubeChannelMapper;
    private final TGBotFeign tgBotFeign;
    private YouTube youtubeService;

    private YouTube getService() throws GeneralSecurityException, IOException {
        if (this.youtubeService == null) {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            this.youtubeService =  new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }

        return this.youtubeService;
    }

    public List<YoutubeChannelDTO> getSubscriptions() {
        List<YoutubeChannelDTO> channels = new ArrayList<>();

        try {
            YouTube youtubeService = getService();
            YouTube.Subscriptions.List request = youtubeService.subscriptions()
                    .list(SNIPPET)
                    .setKey(apiKey)
                    .setChannelId(channelId)
                    .setMaxResults(50L);

            SubscriptionListResponse response = request.execute();

            channels = response.getItems().stream().
                    map(channel -> YoutubeChannelDTO.builder()
                            .channelId(channel.getSnippet().getResourceId().getChannelId())
                            .channelName(channel.getSnippet().getTitle())
                            .thumbnailUrl(channel.getSnippet().getThumbnails().getDefault().getUrl())
                            .build())
                    .collect(Collectors.toList());
        } catch (GeneralSecurityException | IOException e) {
            log.error("Ошибка при загрузке youtube каналов", e);
        }

        return channels;
    }

    @Transactional
    public void setChannelPreferences(YoutubeChannelDTO channel) {
        Optional<YoutubeChannel> foundChannel = youtubeChannelRepository.findByChannelId(channel.getChannelId());

        if (foundChannel.isPresent()) {
            youtubeChannelRepository.delete(foundChannel.get());
        } else {
            channel.setCheckNewVideo(true);
            youtubeChannelRepository.save(youtubeChannelMapper.youtubeChannelDTOToYoutubeChannel(channel));
        }
    }

    public List<YoutubeChannelDTO> getSavedChannels() {
        return youtubeChannelRepository.findAll().stream()
                .map(youtubeChannelMapper::youtubeChannelToYoutubeChannelDTO)
                .collect(Collectors.toList());
    }

    public void checkNewVideos() {
        List<YoutubeChannel> savedChannels = youtubeChannelRepository.findAll();
        LocalDateTime current = LocalDateTime.now();

        if (!savedChannels.isEmpty()) {
            try {
                YouTube youtubeService = getService();
                YouTube.Search.List request = youtubeService.search()
                        .list(SNIPPET)
                        .setKey(apiKey)
                        .setOrder("date")
                        .setMaxResults(1L);

                for (YoutubeChannel youtubeChannel : savedChannels) {
                    SearchListResponse response = request
                            .setChannelId(youtubeChannel.getChannelId())
                            .execute();

                    SearchResult lastVideo = response.getItems().get(0);
                    String publishedAt = lastVideo.getSnippet().getPublishedAt().toString();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    LocalDateTime publishedAtAsJavaDateTime = LocalDateTime.parse(publishedAt, formatter);

                    if (publishedAtAsJavaDateTime.isAfter(current.minusHours(12))) {
                        String messageForTG = makeMessageForTG(lastVideo);
                        tgBotFeign.sendTGNotification(messageForTG);
                    }
                }
            } catch (GeneralSecurityException | IOException e) {
                log.error("Ошибка при загрузке youtube видео с канала", e);
            }
        }
    }

    private String makeMessageForTG(SearchResult lastVideo) {
        String channelTitle = lastVideo.getSnippet().getChannelTitle();
        String title = lastVideo.getSnippet().getTitle();

        return "На канале " + channelTitle + " вышло видео:\n" + title;
    }
}
