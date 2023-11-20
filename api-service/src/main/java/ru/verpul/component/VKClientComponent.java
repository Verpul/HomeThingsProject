package ru.verpul.component;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class VKClientComponent {

    private final VkApiClient vkApiClient;

    public VKClientComponent() {
        TransportClient transportClient = new HttpTransportClient();
        vkApiClient = new VkApiClient(transportClient);
    }
}
