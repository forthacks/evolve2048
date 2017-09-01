package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class NetworkPanel extends JPanel {

    DecimalFormat df = new DecimalFormat("0.000");

    public NetworkPanel() {

        setPreferredSize(new Dimension(1000, 250));

    }

    public void paint(Graphics g2) {

        super.paint(g2);

        Graphics2D g = (Graphics2D) g2;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        if (Main.evolution != null && Main.evolution.bestplayers.size() > 0) {

            g.setFont(g.getFont().deriveFont(14f));

            int y = 30;
            int maxx = 0;

            for (Layer l : Main.evolution.bestplayers.get(Main.evolution.bestplayers.size() - 1).layers) {

                int x = 20;

                for (Neuron n : l.neurons) {

                    g.setFont(g.getFont().deriveFont(Font.BOLD));
                    g.setColor(new Color(0x3E86A0));
                    if (n instanceof AndNeuron) {
                        g.drawString("AND", x + 27, y);
                    } else if (n instanceof MinNeuron) {
                        g.drawString("MIN", x + 27, y);
                    } else if (n instanceof InputNeuron) {
                        g.drawString("IPT", x + 27, y);
                    }

                    g.setColor(new Color(0x000000));
                    g.drawString(df.format(n._value), x+23, y + 20);

                    g.setFont(g.getFont().deriveFont(Font.PLAIN));
                    g.drawString(df.format(n.up) + ", " + df.format(n._up), x, y + 40);
                    g.drawString(df.format(n.left) + ", " + df.format(n._left), x, y + 60);
                    g.drawString(df.format(n.down) + ", " + df.format(n._down), x, y + 80);
                    g.drawString(df.format(n.right) + ", " + df.format(n._right), x, y + 100);

                    if (!(n instanceof InputNeuron)) {

                        int size = n.parents.size();

                        if (size >= 2) {

                            g.setColor(new Color(0x3E86A0));
                            g.drawLine(x + 50, y - 15, 100 * n.parents.get(size - 2) + 75, y - 95);
                            g.drawLine(x + 50, y - 15, 100 * n.parents.get(size - 1) + 75, y - 95);

                        }

                    }

                    x += 100;

                    if (x > maxx)
                        maxx = x;

                }

                y += 200;

            }

            setPreferredSize(new Dimension(maxx + 50, y + 20));

        }

    }

}
