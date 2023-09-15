import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Threads extends JFrame {
    private static JSlider slider;
    private static Thread thread1;
    private static Thread thread2;
    private static Integer semaphore = 0;

    public Threads() {
        setTitle("Lab_1");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        setVisible(true);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        add(slider);

        SpinnerModel model1 = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner prioritySpinner1 = new JSpinner(model1);

        SpinnerModel model2 = new SpinnerNumberModel(5, 1, 10, 1);
        JSpinner prioritySpinner2 = new JSpinner(model2);

        JPanel SPanel = new JPanel();
        SPanel.add(prioritySpinner1);
        SPanel.add(prioritySpinner2);
        add(SPanel);

        JPanel BPanel = new JPanel();
        JButton startButton = new JButton("Пуск");
        JButton stopButton = new JButton("СТОП");
        JButton startButton1 = new JButton("ПУСК 1");
        JButton startButton2 = new JButton("ПУСК 2");
        JButton stopButton1 = new JButton("СТОП 1");
        JButton stopButton2 = new JButton("СТОП 2");

        BPanel.setLayout(new GridLayout(3, 2, 10, 10));

        BPanel.add(startButton);
        BPanel.add(stopButton);
        BPanel.add(startButton1);
        BPanel.add(startButton2);
        BPanel.add(stopButton1);
        BPanel.add(stopButton2);
        add(BPanel);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int priority1 = (int) prioritySpinner1.getValue();
                int priority2 = (int) prioritySpinner2.getValue();

                thread1 = new Thread(new SliderUpdater(slider, priority1, 10, "Потік 1"));
                thread2 = new Thread(new SliderUpdater(slider, priority2, 90, "Потік 2"));

                thread1.start();
                thread2.start();

                startButton.setEnabled(false);
                startButton1.setVisible(false);;
                startButton2.setVisible(false);
                stopButton1.setVisible(false);
                stopButton2.setVisible(false);
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (thread1 != null) {
                    thread1.interrupt();
                    thread1 = null;
                }
                if (thread2 != null) {
                    thread2.interrupt();
                    thread2 = null;
                }

                startButton.setEnabled(true);
                startButton1.setVisible(true);;
                startButton2.setVisible(true);
                stopButton1.setVisible(true);
                stopButton2.setVisible(true);
            }
        });

        startButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (semaphore == 0) {
                    semaphore = 1;
                    thread1 = new Thread(new SliderUpdater(slider, Thread.MIN_PRIORITY, 10, "Потік 1"));
                    thread1.start();
                    startButton1.setEnabled(false);
                    stopButton2.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Зайнято: " + thread2.getName());
                }

                prioritySpinner1.setVisible(false);
                prioritySpinner2.setVisible(false);
                startButton.setVisible(false);
                stopButton.setVisible(false);
            }
        });

        startButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (semaphore == 0) {
                    semaphore = 2;
                    thread2 = new Thread(new SliderUpdater(slider, Thread.MAX_PRIORITY, 90, "Потік 2"));
                    thread2.start();
                    startButton2.setEnabled(false);
                    stopButton1.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Зайнято: " + thread1.getName());
                }

                prioritySpinner1.setVisible(false);
                prioritySpinner2.setVisible(false);
                startButton.setVisible(false);
                stopButton.setVisible(false);
            }
        });

        stopButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (thread1 != null) {
                    thread1.interrupt();
                    thread1 = null;
                    semaphore = 0;

                    prioritySpinner1.setVisible(true);
                    prioritySpinner2.setVisible(true);
                    startButton1.setEnabled(true);
                    startButton.setVisible(true);
                    stopButton.setVisible(true);
                    stopButton2.setEnabled(true);
                }
            }
        });

        stopButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (thread2 != null) {
                    thread2.interrupt();
                    thread2 = null;
                    semaphore = 0;

                    prioritySpinner1.setVisible(true);
                    prioritySpinner2.setVisible(true);
                    startButton2.setEnabled(true);
                    startButton.setVisible(true);
                    stopButton.setVisible(true);
                    stopButton1.setEnabled(true);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                closeWindow();
                System.exit(0);
            }
        });
    }
    private void closeWindow() {
        if (thread1 != null) {
            thread1.interrupt();
            thread1 = null;
        }
        if (thread2 != null) {
            thread2.interrupt();
            thread2 = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Threads();
            }
        });
    }

    private static class SliderUpdater implements Runnable {
        private final JSlider slider;
        private final int priority;
        private final int value;
        private final String name;

        public SliderUpdater(JSlider slider, int priority, int value, String name) {
            this.slider = slider;
            this.priority = priority;
            this.value = value;
            this.name = name;
        }

        public void run() {
            Thread.currentThread().setName(name);
            while (!Thread.currentThread().isInterrupted()) {
                int position = slider.getValue();
                if (position < value) {
                    position++;
                } else if (position > value) {
                    position--;
                }
                slider.setValue(position);

                try {
                    Thread.sleep(1000 / priority);
                } catch (InterruptedException exception) {
                    System.out.println(name + " зупинений");
                    break;
                }
            }
        }
    }
}