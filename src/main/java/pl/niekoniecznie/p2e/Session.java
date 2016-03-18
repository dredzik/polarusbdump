package pl.niekoniecznie.p2e;

import pl.niekoniecznie.polar.model.Model.SessionData;

public class Session {
    public static class Builder {
        public Builder add(SessionData data) {
            System.out.println("udało się?");
            return this;
        }
    }
}
