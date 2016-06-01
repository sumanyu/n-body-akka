package core;

import core.models.Body;
import core.universe.Simulation;

import javax.swing.*;
import java.awt.*;

public class JavaGUI extends JFrame {

    private Simulation simulation;

    public JavaGUI(Simulation simulation, double universeRadius) {
        super("N-body Simulation");
        this.simulation = simulation;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();
        PaintFrame pf = new PaintFrame(universeRadius, 500, simulation);
        pane.add(pf, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private class PaintFrame extends JPanel {

        private Simulation sim;
        private int universeRadius;

        private double scale;

        /**
         *
         * Initialize frame with universe radius and size of the window.
         *
         * @param universeRadius
         * @param drawSize
         * @param sim
         */
        public PaintFrame(double universeRadius, int drawSize, Simulation sim) {
            setPreferredSize(new Dimension(drawSize, drawSize));
            scale = drawSize/(universeRadius*2);
            this.universeRadius = (int) Math.round(universeRadius*scale);
            this.sim = sim;
        }

        /**
         *
         * Simulate N-Body and repaint the gui with new positions of the bodies.
         *
         */
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, universeRadius*2, universeRadius*2);
            g.setColor(Color.WHITE);
            g.drawOval(0, 0, universeRadius*2, universeRadius*2);
            for(Body b : sim.getJavaBodies()) {
                g.setColor(b.color());
                int x = (int) Math.round(
                        b.position().x()*scale+universeRadius);
                int y = (int) Math.round(
                        b.position().y()*scale+universeRadius);
                g.drawOval(x, y, 2, 2);
            }
            simulation.simulate();
            repaint();
        }
    }

}
