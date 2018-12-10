package com.darthside.movienights;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// We create a resource controller that will serve the greetings
// @RestController marks this class as a controller where every method returns a domain object
// instead of a view. It's like @Controller and @ResponseBody fused together
@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting (@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    /* We annotate this method to make sure that any requests to "/greeting" gets mapped to this greeting() method.
    *  Right now @RequestMapping maps all types of HTTP requests, if we wanted it to only map GET-requests,
    *  we could type @RequestMapping(method=GET)
    *  @RequestParam binds the parameter name from the URL to name in the Greeting instance,
    *  if no parameter is entered in the URL it will default to "World".
    *  The method will return a new instance of a Greeting object sending in the current value of the counter as the ID
    *  and a formatted string containing the template and the name, which is now set to whatever was sent in as a parameter.
    *  In other words, this RESTful web-service controller populates and returns a Greeting object,
    *  and the objects data will be written directly to the HTTP response as JSON.
    * */

}
