public class Main {

    public static void main(String[] args) throws InterruptedException {

        int count = 100; // количество игрушек
        Controller controller = Controller.run(count);

        while (controller.getListCount() > 0) {
            System.out.println("count=" + controller.getListCount());
            Thread.sleep(2000);
        }
    }

}
