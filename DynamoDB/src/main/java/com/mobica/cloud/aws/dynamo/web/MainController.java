package com.mobica.cloud.aws.dynamo.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobica.cloud.aws.dynamo.service.DynamoDBService;

@RestController
public class MainController {

  @Autowired
  private DynamoDBService service;

  @RequestMapping(value = "/id/{id}", produces = "application/json")
  public String get(@PathVariable Integer id) {
    return service.findById(id).toJSONPretty();
  }

  @RequestMapping(value = "/all", produces = "application/json")
  public String getAll() {
    return service.fetchAll().toString();
  }

  @ExceptionHandler(Exception.class)
  public String handleError(HttpServletRequest req, Exception ex) {
    return ex.getMessage();
  }
}
