package oceanebelle.parser.engine;

public interface Translator<T> {
    /**
     * Helper method of translating a sentence into an event.
     * This method should be as lightweight as possible
     * @param sentence the raw sentence
     * @return event type
     */
    T translate(String sentence);
}
