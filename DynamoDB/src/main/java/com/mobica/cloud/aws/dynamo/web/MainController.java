package com.mobica.cloud.aws.dynamo.web;

import com.mobica.cloud.aws.dynamo.service.DynamoDBService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/items")
public class MainController {

    private final DynamoDBService service;

    public MainController(DynamoDBService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces= "application/json")
    public String get(@PathVariable Long id) {
        return service.findById(id).toJSONPretty();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String getAll() {
        return service.fetchAll();
    }

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception ex) {
        return ex.getMessage();
    }
}
