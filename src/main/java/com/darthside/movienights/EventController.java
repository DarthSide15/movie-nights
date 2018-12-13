package com.darthside.movienights;

import com.google.api.services.calendar.model.Event;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class EventController {

    private CalendarQuickstart calendarQuickstart = new CalendarQuickstart();

    @RequestMapping("/events")
    public List<Event> listEvents() throws IOException, GeneralSecurityException {
        return calendarQuickstart.getEvents();
    }
}
