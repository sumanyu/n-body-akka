package core.gui;

import core.models.Body;
import core.universe.SystemState;
import core.universe.UniverseConstants;

import javax.swing.*;
import java.awt.*;

public class JavaGUI extends JFrame {

    private SystemState systemState;

    public JavaGUI(SystemState systemState, double universeRadius) {
        super("N-body Simulation");
        this.systemState = systemState;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();
        PaintFrame pf = new PaintFrame(universeRadius, 500, systemState);
        pane.add(pf, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private class PaintFrame extends JPanel {

        private SystemState systemState;
        private int universeRadius;

        private double scale;

        /**
         *
         * Initialize frame with universe radius and size of the window.
         *
         * @param universeRadius
         * @param drawSize
         * @param systemState
         */
        public PaintFrame(double universeRadius, int drawSize, SystemState systemState) {
            setPreferredSize(new Dimension(drawSize, drawSize));
            scale = drawSize/(universeRadius*2);
            this.universeRadius = (int) Math.round(universeRadius*scale);
            this.systemState = systemState;
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
            for(Body b : systemState.getJavaBodies()) {
                g.setColor(b.color());
                int x = (int) Math.round(b.position().x()*scale+universeRadius);
                int y = (int) Math.round(b.position().y()*scale+universeRadius);
                int height = (int) Math.max(1, 20 * b.mass() / UniverseConstants.SOLAR_MASS());
                int width = height;
                g.drawOval(x, y, width, height);
            }

            try {
                Thread.sleep(10);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            repaint();
        }
    }

}
