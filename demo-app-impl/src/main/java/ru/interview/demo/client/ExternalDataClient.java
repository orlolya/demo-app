package ru.interview.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.interview.demo.dto.ExternalObject;

@FeignClient(name = "ExternalDataClient", url = "${app.external-service-url}")
public interface ExternalDataClient {

    @GetMapping("/data/")
    ExternalObject getExternalObject(@RequestParam("id") Long id);

}
