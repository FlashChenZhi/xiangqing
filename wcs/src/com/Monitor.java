package com;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by van on 2018/1/3.
 */
public class Monitor implements Runnable {

    JFrame frame = new JFrame() {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawLine(280, 40, 280, 580);

            g.drawLine(520, 40, 520, 580);

            g.drawLine(800, 40, 800, 580);

        }
    };
    Panel panel4 = new Panel();
    Panel panel3 = new Panel();
    Panel panel2 = new Panel();
    Panel panel1 = new Panel();

    Label srm3 = new Label("0");
    Label srm2 = new Label("0");
    Label srm1 = new Label("0");

    Label scar1 = new Label("0");
    Label scar2 = new Label("0");
    Label scar3 = new Label("0");

    public static void main(String[] args) throws Exception {
        Monitor monitor = new Monitor();
        monitor.initPanel4();
        monitor.initPanel3();
        monitor.initPanel2();
        monitor.initPanel1();
        monitor.initSrm();
    }

    private void initSrm() {
        srm3.setSize(20, 40);
        srm2.setSize(20, 40);
        srm1.setSize(20, 40);
        srm3.setLocation(270, 500);
        srm2.setLocation(510, 500);
        srm1.setLocation(790, 500);
        srm1.setBackground(Color.ORANGE);
        srm2.setBackground(Color.ORANGE);
        srm3.setBackground(Color.ORANGE);

        scar1.setSize(20, 20);
        scar2.setSize(20, 20);
        scar3.setSize(20, 20);
        scar1.setLocation(270, 500);
        scar2.setLocation(510, 500);
        scar3.setLocation(790, 500);
        scar1.setBackground(Color.YELLOW);
        scar2.setBackground(Color.YELLOW);
        scar3.setBackground(Color.YELLOW);
        frame.add(srm3);
        frame.add(srm2);
        frame.add(srm1);
        frame.add(scar1);
        frame.add(scar2);
        frame.add(scar3);

        frame.repaint();
    }

    private Color mfColor = new Color(255, 0, 0);

    private float[] dash1 = {5.0f};

    private BasicStroke s = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

    private Rectangle2D mfRect = new Rectangle2D.Float();

    public void initPanel4() throws Exception {

        frame.setSize(1280, 768);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("欧普照明状态监控");
        frame.setLayout(null);
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");


        panel4.setSize(240, 600);
        panel4.setLocation(20, 20);
        panel4.setLayout(null);
        frame.add(panel4);

        for (int bay = 1; bay <= 26; bay++) {
            for (int bank = 1; bank <= 12; bank++) {
                Label label = new Label("X") {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);

                        Graphics2D g2d = (Graphics2D) g;
                        //设置边框颜色
                        g2d.setColor(mfColor);
                        //设置边框范围
                        mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                        //设置边框类型
                        g2d.setStroke(s);

                        g2d.draw(mfRect);

                    }
                };

                label.setSize(20, 20);
                label.setLocation(20 * (bank - 1), 20 * (bay - 1));
                panel4.add(label);
                panel4.repaint();
            }
        }
        Label label12 = new Label("#") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };
        label12.setSize(20, 20);
        label12.setLocation(220, 520);
        panel4.add(label12);

        Label label11 = new Label("==") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };
        label11.setSize(20, 50);
        label11.setLocation(220, 540);
        panel4.add(label11);
        panel4.repaint();

    }


    public void initPanel3() {

        panel3.setSize(200, 600);
        panel3.setLocation(300, 20);
        panel3.setLayout(null);
        frame.add(panel3);

        for (int bay = 1; bay <= 26; bay++) {
            for (int bank = 1; bank <= 10; bank++) {
                Label label = new Label("X") {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);

                        Graphics2D g2d = (Graphics2D) g;
                        //设置边框颜色
                        g2d.setColor(mfColor);
                        //设置边框范围
                        mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                        //设置边框类型
                        g2d.setStroke(s);

                        g2d.draw(mfRect);

                    }
                };

                label.setSize(20, 20);
                label.setLocation(20 * (bank - 1), 20 * (bay - 1));
                panel3.add(label);
                panel3.repaint();

            }
        }

        Label label09 = new Label("==") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };

        label09.setSize(20, 70);
        label09.setLocation(0, 520);

        Label label08 = new Label("#") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };
        label08.setSize(20, 20);
        label08.setLocation(180, 520);
        panel3.add(label08);

        Label label07 = new Label("==") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };
        label07.setSize(20, 50);
        label07.setLocation(180, 540);
        panel3.add(label07);
        panel3.add(label09);
        panel3.repaint();


    }

    public void initPanel2() {
        panel2.setSize(240, 600);
        panel2.setLocation(540, 20);
        panel2.setLayout(null);
        frame.add(panel2);

        for (int bay = 1; bay <= 26; bay++) {
            for (int bank = 1; bank <= 12; bank++) {
                Label label = new Label("X") {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);

                        Graphics2D g2d = (Graphics2D) g;
                        //设置边框颜色
                        g2d.setColor(mfColor);
                        //设置边框范围
                        mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                        //设置边框类型
                        g2d.setStroke(s);

                        g2d.draw(mfRect);

                    }
                };

                label.setSize(20, 20);
                label.setLocation(20 * (bank - 1), 20 * (bay - 1));
                panel2.add(label);
                panel2.repaint();
            }
        }

        Label label05 = new Label("==") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };


        label05.setSize(20, 70);
        label05.setLocation(0, 520);

        panel2.add(label05);


        Label label08 = new Label("#") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };
        label08.setSize(20, 20);
        label08.setLocation(220, 520);
        panel2.add(label08);

        Label label07 = new Label("==") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };
        label07.setSize(20, 50);
        label07.setLocation(220, 540);
        panel2.add(label07);
        panel2.repaint();

    }

    public void initPanel1() {

        panel1.setSize(120, 600);
        panel1.setLocation(820, 20);
        panel1.setLayout(null);
        frame.add(panel1);

        for (int bay = 1; bay <= 26; bay++) {
            for (int bank = 1; bank <= 6; bank++) {
                Label label = new Label("X") {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);

                        Graphics2D g2d = (Graphics2D) g;
                        //设置边框颜色
                        g2d.setColor(mfColor);
                        //设置边框范围
                        mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                        //设置边框类型
                        g2d.setStroke(s);

                        g2d.draw(mfRect);

                    }
                };

                label.setSize(20, 20);
                label.setLocation(20 * (bank - 1), 20 * (bay - 1));
                panel1.add(label);
                panel1.repaint();

            }
        }

        Label label09 = new Label("==") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;
                //设置边框颜色
                g2d.setColor(mfColor);
                //设置边框范围
                mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
                //设置边框类型
                g2d.setStroke(s);

                g2d.draw(mfRect);

            }
        };

        label09.setSize(20, 70);
        label09.setLocation(0, 520);
        panel1.add(label09);
        panel1.repaint();
    }


    @Override
    public void run() {
        while (true) {
            panel4.repaint();
            panel3.repaint();
            panel2.repaint();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
