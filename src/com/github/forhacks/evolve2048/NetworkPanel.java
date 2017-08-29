package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;

public class NetworkPanel extends JPanel {

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

            for (Layer l : Main.evolution.bestplayers.get(Main.evolution.bestplayers.size() - 1).layers) {

                int x = 20;

                for (Neuron n : l.neurons) {

                    if (n instanceof AndNeuron) {
                        g.drawString("AND", x, y);
                    } else if (n instanceof MaxNeuron) {
                        g.drawString("MAX", x, y);
                    } else if (n instanceof WeightNeuron) {
                        g.drawString("WHT", x, y);
                    } else if (n instanceof InputNeuron) {
                        g.drawString("IPT", x, y);
                    }
                    g.drawString((n._value + "00000").substring(0, 5), x, y + 20);
                    g.drawString((n.up + "00000").substring(0, 5) + ", " + (n._up + "00000").substring(0, 5), x, y + 40);
                    g.drawString((n.left + "00000").substring(0, 5) + ", " + (n._left + "00000").substring(0, 5), x, y + 60);
                    g.drawString((n.down + "00000").substring(0, 5) + ", " + (n._down + "00000").substring(0, 5), x, y + 80);
                    g.drawString((n.right + "00000").substring(0, 5) + ", " + (n._right + "00000").substring(0, 5), x, y + 100);

                    if (!(n instanceof InputNeuron)) {

                        int size = n.parents.size();

                        if (size >= 2) {

                            g.drawLine(x + 50, y - 12, 20 + 100 * n.parents.get(size - 2) + 50, y - 100 + 2);
                            g.drawLine(x + 50, y - 12, 20 + 100 * n.parents.get(size - 1) + 50, y - 100 + 2);

                        }

                    }

                    x += 100;

                }

                y += 200;

            }

            setPreferredSize(new Dimension(1000, y + 20));

        }

    }

}
