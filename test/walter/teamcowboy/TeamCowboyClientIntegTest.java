package walter.teamcowboy;

import static org.junit.Assert.assertNotNull;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import walter.teamcowboy.types.AuthUser;
import walter.teamcowboy.types.Team;
import walter.teamcowboy.types.User;

import java.util.List;
import java.util.Map;

public class TeamCowboyClientIntegTest {
    private TeamCowboyClient client;

    @Before
    public void setUp() {
        client = new TeamCowboyClient();
    }

    @Test
    public void testMakeTestRequest() {
        Map<String, Object> testResponse = client.makeTestRequest();
        System.out.println(testResponse);

        assertNotNull(testResponse);
    }

    @Test
    public void testMakeTestPostRequest() {
        Map<String, Object> testResponse = client.makeTestPostRequest();
        System.out.println(testResponse);
        assertNotNull(testResponse);
    }

    @Test
    public void testSignin() {
        AuthUser authUser = client.login(IntegTestData.USER_NAME, IntegTestData.PASSWORD);
        Assert.assertNotNull(authUser);

        System.out.println(ToStringBuilder.reflectionToString(authUser));
    }

    @Test
    public void testGetUserDetail() {
        User user = client.getUserDetails(IntegTestData.AUTH_USER);
        Assert.assertNotNull(user);

        System.out.println(ToStringBuilder.reflectionToString(user));
    }

    @Test
    public void testGetTeams() {
        List<Team> teams = client.getTeams(IntegTestData.AUTH_USER);
        Iterables.transform(teams, new Function<Team, Void>() {
            @Override
            public Void apply(Team team) {
                System.out.println(ToStringBuilder.reflectionToString(team));
                return null;
            }
        });

        Assert.assertNotNull(teams);
        Assert.assertNotEquals(0, teams.size());
    }
}