public class Toy {
    private static int seq = 1;
    int id;
    String name;
    int frequency = 80;

    Toy(String name, int frequency) {
        this.frequency = frequency;
        this.name = name;
        this.id = seq++;
    }

    @Override
    public String toString() {
        return String.format("Toy: {id=%d; name=%s; freq=%d}", this.id, this.name, this.frequency);
    }
}
