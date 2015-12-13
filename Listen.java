import javafx.scene.text.Text;

public class Listen extends Thread {

    Text title;
    int temp = 0;

    Listen(Text t) {
        title = t;
    }

    @Override
    public void run() {
        int counter = 0;
        while (true) {
            temp++;
            if (temp % 100000000 == 0) {
                counter++;
                title.setText("Listen Test: " + counter);
                temp = 0;
            }
        }
    }

    public void init(Text title) {
        Thread t = new Listen(title);
        t.start();
        System.out.println("Listen Starts");
    }
}
