package walter.teamcowboy.types;

import java.util.Collections;
import java.util.Map;

/**
 * Representation of a TeamCowboy team object. http://api.teamcowboy.com/v1/docs/#id.xafee-p95rkk
 * <p>
 * TODO: add color swatches. TODO: add options TODO: add userProfileInfo
 */
public class Team {
    private int teamId;
    private String name;
    private String shortName;
    private String timeZoneId;
    private String city;
    private String stateProvince;
    private String stateProvinceAbbrev;
    private String country;
    private String countryIso3;
    private String postalCode;
    private String locationDisplayShort;
    private User managerUser; // TODO: figure out email name differences.
    private Map<String, String> images = Collections.EMPTY_MAP;
    private MetaInfo meta;

    public String getLocationDisplayShort() {
        return locationDisplayShort;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public String getCity() {
        return city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getStateProvinceAbbrev() {
        return stateProvinceAbbrev;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryIso3() {
        return countryIso3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public User getManagerUser() {
        return managerUser;
    }

    public Map<String, String> getImages() {
        return images;
    }

    private static class MetaInfo {
        // TODO: make a type?
        private Map<String, String> teamMemberType;
        private boolean isHiddenByUser;
        private boolean showOnDashboard;

        public Map<String, String> getTeamMemberType() {
            return teamMemberType;
        }

        public boolean isHiddenByUser() {
            return isHiddenByUser;
        }

        public boolean isShowOnDashboard() {
            return showOnDashboard;
        }
    }

    @Override
    public String toString() {
        return name + " - " + locationDisplayShort;

    }
}
