package pl.niekoniecznie.p2e.workflow;

public interface Command<T> {

    T execute();
}
