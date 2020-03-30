import javax.swing.*;
import java.awt.*;

public class GUI {
    public static void main(String[] args){
        EventQueue.invokeLater(() ->  {
            SystemJedi systemJedi = new SystemJedi();
            systemJedi.setVisible(true);
            systemJedi.setSize(900,700);
            systemJedi.setLocationRelativeTo(null);
            systemJedi.con();
            systemJedi.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}
