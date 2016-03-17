package pl.niekoniecznie.p2e;

import pl.niekoniecznie.p2e.parser.ParseResult;
import pl.niekoniecznie.polar.model.Model.SessionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntrySet {

    Map<String, Session.Builder> sessions = new HashMap<>();

    public List<Session> build() {
        List<Session> result = new ArrayList<>();

        for (Session.Builder builder : sessions.values()) {
            result.add(builder.build());
        }

        return result;
    }

    public void add(ParseResult item) {
        if (item == null) {
            return;
        }

        if (!sessions.containsKey(item.sessionKey)) {
            sessions.put(item.sessionKey, new Session.Builder());
        }

        Session.Builder builder = sessions.get(item.sessionKey);

        if (SessionData.class.isInstance(item.data)) {
            builder.data(SessionData.class.cast(item.data));
        }
    }
}
