package walter.teamcowboy.types;

import java.util.Map;

/**
 * Representation of an event object
 * <p/>
 * http://api.teamcowboy.com/v1/docs/#id.ldpemf-r24f6j
 * <p/>
 * TODO: add result info?
 */
public class Event {
    private int eventId;
    private Team team;
    private int seasonId;
    private String seasonName;
    // game, doubleheader, postseason, match, meet, tournament, jamboree, race, regatta, ride, bye, practice,
    // scrimmage, pickup, meeting, other
    private String eventType;
    private String eventTypeDisplay;
    // active, postponed, canceled, forfeited
    private String status;
    private String statusDisplay;

    private String personNounSingular;
    private String personNounPlural;

    private String title;
    private String titleFull;
    // home, away, NULL
    // TODO: create an enum
    private String homeAway;

    // TODO: add an object for this.
    private Map<String, Object> dateTimeInfo;

    // TODO: add a class for Location
    // http://api.teamcowboy.com/v1/docs/#kix.g3al2c-eoc9mv
    private Map<String, Object> location;

    public int getEventId() {
        return eventId;
    }

    public Team getTeam() {
        return team;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventTypeDisplay() {
        return eventTypeDisplay;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public String getPersonNounSingular() {
        return personNounSingular;
    }

    public String getPersonNounPlural() {
        return personNounPlural;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleFull() {
        return titleFull;
    }

    public String getHomeAway() {
        return homeAway;
    }

    public Map<String, Object> getDateTimeInfo() {
        return dateTimeInfo;
    }

    public Map<String, Object> getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return dateTimeInfo.get("startDateTimeLocalDisplay") + " - " + titleFull + " @ " + location.get("name");
    }

    class Team {
        private int teamId;
        private String teamName;

        public int getTeamId() {
            return teamId;
        }

        public String getTeamName() {
            return teamName;
        }
    }
}
