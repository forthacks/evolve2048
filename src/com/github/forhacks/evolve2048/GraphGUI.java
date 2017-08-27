package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;

public class GraphGUI extends JPanel {

    public GraphGUI() {

        setPreferredSize(new Dimension(500, 500));

        setBackground(new Color(0xfaf8ef));

    }

    @Override
    public void paint(Graphics g2) {

        if (Main.evolution == null || Main.evolution.scores.size() == 0) {
            return;
        }

        Graphics2D g = (Graphics2D) g2;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        int prevx = 0;
        int prevy = 0;

        g.setColor(Color.GRAY);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        g.setStroke(new BasicStroke(1));

        for (int i = 0; i <= Main.evolution.maxscore * (3.8 / 3); i += Main.evolution.maxscore * (3.8 / 3) * 0.2) {

            String s = Integer.toString(i);

            int y = 440 - (int) (((double) i / Main.evolution.maxscore) * 300.0);
            int x = 70 - fm.stringWidth(s);

            g.drawLine(40, y, 460, y);
            g.drawString(s, x, y - 3);

        }

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);

        for (int i = 0; i < Main.evolution.scores.size(); i++) {

            int score = Main.evolution.scores.get(i);

            int x = 80 + (int) (((double) i / (Main.evolution.scores.size() - 1)) * 370.0);
            int y = 450 - (int) (((double) score / Main.evolution.maxscore) * 300.0);

            g.fillOval(x, y, 4, 4);

            if (i > 0) {
                g.drawLine(prevx + 2, prevy + 2, x + 2,y + 2);
            }

            prevx = x;
            prevy = y;

        }

    }

}
