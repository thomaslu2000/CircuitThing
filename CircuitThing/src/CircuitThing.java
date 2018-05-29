import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;


class CircuitThing {
    public CircuitThing() {
    	EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Circuit Thing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                CircuitPanel panel=new CircuitPanel();
                //panel.addKeyListener(panel);
                panel.addMouseListener(panel);
                panel.setFocusable(true);
                frame.add(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    public static void main(String[] args) {
        new CircuitThing();
    }
}