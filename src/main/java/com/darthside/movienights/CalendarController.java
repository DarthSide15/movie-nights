package com.darthside.movienights;
import com.darthside.movienights.database.Token;
import com.darthside.movienights.database.TokenTable;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
public class CalendarController {

    @Autowired
    TokenTable tokenTable;

    // Needed to handle both standard and whole-day events
    private static <T> T firstNonNull(T... params) {
        for (T param : params)
            if (param != null)
                return param;
        return null;
    }

    private String convertTimeToString(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    @RequestMapping(value = "/periods", method = RequestMethod.GET)
    public List<Period> getFreePeriods() {

        List<Token> tokens = tokenTable.findAll();
        List<Event> allEvents = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        // Gets events from all available users and stores them in 'allEvents'
        for (Token t: tokens) {
            GoogleCredential cred;

            // Refresh access tokens if expired
            if( t.getExpiresAt() < currentTime ) {
                cred = GoogleController.getRefreshedCredentials(t.getRefreshToken());
                t.setAccessToken(cred.getAccessToken());
                t.setExpiresAt(System.currentTimeMillis() + 3600*1000);
                tokenTable.save(t);
            } else {
                cred = new GoogleCredential().setAccessToken(t.getAccessToken());
            }

            Calendar calendar =
                    new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), cred)
                            .setApplicationName("Movie Nights")
                            .build();

            // Gets the next 10 events from the primary calendar
            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = null;
            try {
                events = calendar.events().list("primary")
                        .setMaxResults(10)
                        .setTimeMin(now)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Event> items = events.getItems();
            allEvents.addAll(items);
        }

        List<Period> periods = new ArrayList<>();

        // Sort 'allEvents' List in chronological order
        for (Event event : allEvents) {
            DateTime start = firstNonNull(event.getStart().getDateTime(), event.getStart().getDate());
            System.out.printf("%s (%s)\n", event.getSummary(), start);
        }
        allEvents.sort(Comparator.comparing(event -> firstNonNull(event.getStart().getDateTime(), event.getStart().getDate()).getValue()));

        // Calculate period Now to start of first event
        Period nowToNext = new Period(
                new DateTime(System.currentTimeMillis()),
                firstNonNull(allEvents.get(0).getStart().getDateTime(), allEvents.get(0).getStart().getDate())
        );
        periods.add(nowToNext);

        // Calculate periods from end time of previous event to start time of next event
        for (int i = 0; i < allEvents.size() - 1 ; i++) {

            // TODO: Handle overlapping events
            // If end time of current event occurs AFTER start time of next event,
            // Do not store this period
            // if (eventA.getEnd() > eventA.getStart())
            //      continue;

            // The issue here is that we don't know how to compare the values

            Period periodBetweenEvents = new Period(firstNonNull(allEvents.get(i).getEnd().getDateTime(), allEvents.get(i).getEnd().getDate()),
                    firstNonNull(allEvents.get(i + 1).getStart().getDateTime(), allEvents.get(i + 1).getStart().getDate()));
            periods.add(periodBetweenEvents);

        }
        System.out.println(periods);

        // Calculate period from end time of last event in the list plus 31 days
        Period lastEventPlusAMonth = new Period(
                firstNonNull(allEvents.get(allEvents.size() -1).getEnd().getDateTime(), allEvents.get(allEvents.size() - 1).getEnd().getDate()),
                new DateTime(  firstNonNull(allEvents.get(allEvents.size() -1).getEnd().getDateTime(), allEvents.get(allEvents.size() - 1).getEnd().getDate()).getValue()
                        + 31*24*60*60*1000L)
        );
        periods.add(lastEventPlusAMonth);

        System.out.println("Size: " + periods.size());
        System.out.println("Available periods: \n" );
        for ( Period period : periods) {
            System.out.println("Start: " + convertTimeToString(period.getStart().getValue()));
            System.out.println("End: " + convertTimeToString(period.getEnd().getValue()));
            System.out.println();
        }

        return periods;
    }
}
