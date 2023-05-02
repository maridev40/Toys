import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Controller {

    public static Controller run(int count) {
        Controller controller = new Controller();
        controller.addRanomToys(count);
        return controller;
    }

    private List<Toy> list = new ArrayList<>();
    private Queue<Toy> queue = new LinkedList<>();

    private int index = 0;

    public Controller() {
        Thread backgroundSatietyManagement = new Thread(() -> {
            while (true) {

                Toy toy = getPrizeToy();
                if (toy != null) {
                    moveToyToQueue(toy);
                    moveToyToFile();
                }

                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        backgroundSatietyManagement.setDaemon(true);
        backgroundSatietyManagement.start();
    }

    public void addToy(Toy toy) {
        this.list.add(toy);
    }

    public void addRanomToys(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int freq = random.nextInt(1, 100);
            addToy(new Toy("Toy" + i, freq));
        }
    }

    public Toy findToy(int freq) {
        int tmp = list.size();
        while (tmp > 0) {
            if (list.get(tmp - 1).frequency > 70) {
                return list.get(tmp - 1);
            }
            tmp--;
        }
        return null;
    }

    public Toy getPrizeToy() {
        Toy toy = null;
        while (list.size() > 0) {
            // поиск 30% - 100% в 7 из 10 случаев 
            if (index < 7) {
                toy = findToy((10 - index) * 10);
                if (toy != null) {
                    break;
                }
            }
            // поиск 30% - 10% в 2 из 10 случаев
            if (index < 9) {
                toy = findToy((10 - index) * 10);
                if (toy != null) {
                    break;
                }
            }
            // поиск 10% - 0% в 1 из 10 случаев
            if (index < 10) {
                toy = findToy((10 - index) * 10);
                if (toy != null) {
                    break;
                }
            }
            index = 0; // обнулить случаи
            toy = list.get(0);
            break;
        }

        index++; // нарастить случаи
        return toy;
    }

    public void moveToyToQueue(Toy toy) {
        queue.add(toy);
        list.remove(toy);
    }

    public void moveToyToFile() {
        if (queue.size() > 0) {
            Toy toy = queue.poll();

            Path file = Path.of("log.txt");
            try (BufferedOutputStream stream = new BufferedOutputStream(
                    Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
                Charset utf8 = StandardCharsets.UTF_8;
                byte[] buffer = toy.toString().getBytes(utf8);
                // stream.write(buffer, 0, buffer.length);
                stream.write(buffer);
                stream.write("\n".getBytes(utf8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public int getListCount() {
        return list.size();
    }

}
