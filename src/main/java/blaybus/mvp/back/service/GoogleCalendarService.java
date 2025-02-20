package blaybus.mvp.back.service;

import blaybus.mvp.back.dto.request.GoogleMeetDTO;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

//  Google calendar api 호출하는 service
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    // Global instance of the JSON factory.
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    // Directory to store authorization tokens for this application.
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    @Value("${google.credentials.file}")
    private static String credentialsFilePath;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);



    // credential 생성
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = GoogleCalendarService.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("hwangrock.com").setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        //returns an authorized Credential object.
        System.out.println("Credentials saved to" + TOKENS_DIRECTORY_PATH);
        return credential;
    }

    public GoogleMeetDTO createGoogleMeet(String email, LocalDateTime localStartTime, LocalDateTime localEndTime) throws IOException, GeneralSecurityException {

        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();


        ZonedDateTime zonedStartTime = localStartTime.atZone(ZoneId.systemDefault());
        DateTime startDateTime = new DateTime(zonedStartTime.toInstant().toEpochMilli());
        ZonedDateTime zonedEndTime = localEndTime.atZone(ZoneId.systemDefault());
        DateTime endDateTime = new DateTime(zonedEndTime.toInstant().toEpochMilli());


        // 캘린더 일정 생성
        Event event = new Event();
//                .setSummary(calendarStoreDto.getSummary()) // 일정 이름
//                .setDescription(calendarStoreDto.getDescription()); // 일정 설명


        //시작일
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Seoul");;
        event.setStart(start);
        //종료일
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Seoul");
        event.setEnd(end);


        //google meet 정보
        ConferenceData confData = new ConferenceData();
        CreateConferenceRequest confReq = new CreateConferenceRequest();
        ConferenceSolutionKey confkey = new ConferenceSolutionKey();
        confkey.setType("hangoutsMeet");
        confReq.setConferenceSolutionKey(confkey);
        confReq.setRequestId(UUID.randomUUID().toString()); // 없을때 만들어지지 않음
        confData.setCreateRequest(confReq);
        event.setConferenceData(confData);

//이벤트 실행
        Event confirmedEvent = service.events().insert(email, event).setConferenceDataVersion(1).setMaxAttendees(2).execute();
        System.out.println("일정 url: "+ confirmedEvent.getHtmlLink());
        log.info("일정 url: {}", confirmedEvent.getHtmlLink());
        String eventId = confirmedEvent.getId();
        String meetLink = "https://meet.google.com/" + confirmedEvent.getConferenceData().getConferenceId();
        log.info("구글 미트 링크: {}", meetLink);

        GoogleMeetDTO googleMeetDTO = GoogleMeetDTO.builder()
                .eventId(eventId)
                .meetLink(meetLink)
                .build();

        return googleMeetDTO;

    }

    public void deleteByEventId(String email, String eventId) throws IOException, GeneralSecurityException {

        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
//이벤트 실행
        service.events().delete(email, eventId).execute();
    }
}
