package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;

public class GraphGUI extends JPanel {

    private GameGUI g;

    public GraphGUI(GameGUI g) {

        setPreferredSize(new Dimension(500, 500));

        this.g = g;

        setBackground(new Color(0xfaf8ef));

    }

}
