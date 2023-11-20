package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.VKNewsDateDTO;
import ru.verpul.DTO.VKNewsFilterDTO;
import ru.verpul.DTO.VKNewsPostDTO;
import ru.verpul.service.VKService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/vk/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class VKController {

    private final VKService vkService;

    @GetMapping("/token")
    public boolean haveToken() {
        return vkService.haveVKToken();
    }

    @GetMapping("/newsfeed")
    public List<VKNewsPostDTO> getNewsfeed(@RequestParam String beginDate,
                                           @RequestParam String beginTime) {

        return vkService.loadNewsfeed(beginDate, beginTime);
    }

    @GetMapping("/date")
    public VKNewsDateDTO getLastNewsCheckDate() {
        return vkService.getLastNewsCheckDate();
    }

    @GetMapping("/filter")
    public String getVKNewsFilter() {
        return vkService.getVKNewsFilter();
    }

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveVKNewsFilter(@RequestBody VKNewsFilterDTO filter) {
        vkService.saveVKNewsFilter(filter);
    }
}
