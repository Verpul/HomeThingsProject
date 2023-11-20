package ru.verpul.service;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.newsfeed.NewsfeedItemType;
import com.vk.api.sdk.objects.newsfeed.responses.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verpul.DTO.VKNewsDateDTO;
import ru.verpul.DTO.VKNewsFilterDTO;
import ru.verpul.DTO.VKNewsPostDTO;
import ru.verpul.component.VKClientComponent;
import ru.verpul.mapper.VKNewsDateMapper;
import ru.verpul.mapper.VKNewsFilterMapper;
import ru.verpul.model.VKNewsDate;
import ru.verpul.model.VKNewsFilter;
import ru.verpul.model.VKToken;
import ru.verpul.repository.VKNewsDateRepository;
import ru.verpul.repository.VKNewsFilterRepository;
import ru.verpul.repository.VKTokenRepository;
import ru.verpul.util.VKUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VKService {

    private final VKTokenRepository vkTokenRepository;
    private final VKClientComponent vkClientComponent;
    private final VKNewsDateRepository vkNewsDateRepository;
    private final VKNewsDateMapper vkNewsDateMapper;
    private final VKNewsFilterRepository vkNewsFilterRepository;
    private final VKNewsFilterMapper vkNewsFilterMapper;

    public boolean haveVKToken() {
        return vkTokenRepository.getVKToken() != null;
    }

    @Transactional
    public List<VKNewsPostDTO> loadNewsfeed(String beginDate, String beginTime) {
        int epochtime = VKUtil.getDateTimeAsEpoch(beginDate, beginTime);
        VKToken token = vkTokenRepository.getVKToken();
        UserActor userActor = new UserActor(token.getUserId(), token.getAccessToken());

        try {
            GenericResponse genericResponse = vkClientComponent.getVkApiClient().newsfeed()
                    .get(userActor)
                    .filters(NewsfeedItemType.POST)
                    .count(100)
                    .startTime(epochtime)
                    .execute();

            saveNewsCheckDate();

            List<VKNewsFilter> savedFilters = vkNewsFilterRepository.findAll();

            return genericResponse.getItems().stream()
                    .filter(post -> post.getOneOf0().getMarkedAsAds().getValue().equals("0"))
                    .map(post -> {
                        VKNewsPostDTO newsPost = new VKNewsPostDTO();
                        long sourceId = post.getOneOf0().getSourceId();
                        GroupFull postGroup = genericResponse.getGroups().stream()
                                .filter(group -> group.getId().equals(Math.abs(sourceId)))
                                .findFirst()
                                .get();

                        newsPost.setPostId(post.getOneOf0().getId());
                        newsPost.setGroupName(postGroup.getName());
                        newsPost.setGroupScreenName(postGroup.getScreenName());
                        newsPost.setSourceId(sourceId);
                        String text = post.getOneOf0().getText();
                        newsPost.setText(text.isEmpty() ? "Видео" : text);
                        newsPost.setNumberOfComments(post.getOneOf0().getComments().getCount());
                        newsPost.setPostDate(VKUtil.getEpochAsDateTime(post.getOneOf0().getDate()));

                        for (VKNewsFilter filter : savedFilters) {
                            if (text.contains(filter.getFilter())) {
                                newsPost.setHidden(true);
                            }
                        }

                        return newsPost;
                    })
                    .collect(Collectors.toList());
        } catch (ClientException | ApiException e) {
            log.error("Не удалось загрузить новостную ленту", e);
        }

        return null;
    }

    private void saveNewsCheckDate() {
        Optional<VKNewsDate> savedDate = vkNewsDateRepository.getNewsDate();

        VKNewsDate datesToSave = savedDate.orElseGet(VKNewsDate::new);
        datesToSave.setBeginDate(LocalDate.now());
        datesToSave.setBeginTime(LocalTime.now());

        vkNewsDateRepository.save(datesToSave);
    }

    public VKNewsDateDTO getLastNewsCheckDate() {
        Optional<VKNewsDate> savedDate = vkNewsDateRepository.getNewsDate();

        return vkNewsDateMapper.vkNewsDateToVkNewsDateDTO(savedDate.orElse(null));
    }

    public String getVKNewsFilter() {
        return vkNewsFilterRepository.findAll().stream()
                .map(vkNewsFilterMapper::VKNewsFilterToVKNewsFilterDTO)
                .map(VKNewsFilterDTO::getFilter)
                .collect(Collectors.joining("\n"));
    }

    @Transactional
    public void saveVKNewsFilter(VKNewsFilterDTO filter) {
        Set<VKNewsFilter> filtersToSave = Arrays.stream(filter.getFilter().split("\n"))
                .filter(filterValue -> !filterValue.isBlank())
                .map(filterValue -> {
                    VKNewsFilter filterToSave = new VKNewsFilter();
                    filterToSave.setFilter(filterValue);

                    return filterToSave;
                })
                .collect(Collectors.toSet());

        vkNewsFilterRepository.deleteAll();
        vkNewsFilterRepository.saveAll(filtersToSave);
    }
}
