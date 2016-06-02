package core.gui

import java.awt._
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.AffineTransform
import javax.swing._

import core.models.{Body, Vector2D}
import core.universe.{Simulation, UniverseConstants}


class GUI(simulation: Simulation,
          universeRadius: Double = UniverseConstants.UNIVERSE_RADIUS,
          drawSize: Int = 500) extends JFrame {

  private var simulate: Boolean = false

  private var slider: JSlider = null
  private var showDistance: JCheckBox = null
  private var showLines: JCheckBox = null
  private var showVelocity: JCheckBox = null

  super.setTitle("N-body Simulation")
  super.frameInit()

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  buildFrameContents()
  pack()
  setVisible(true)

  /**
    * Build frame content
    */
  private def buildFrameContents() {
    val pane: Container = getContentPane

    val pf: PaintFrame = new PaintFrame
    pane.add(pf, BorderLayout.SOUTH)

    val options: JPanel = new JPanel(new BorderLayout)

    val bStart: JButton = new JButton("Start")
    options.add(bStart, BorderLayout.WEST)
    bStart.addActionListener(new ActionListener {
      def actionPerformed(action: ActionEvent) = {
        simulate = true
      }
    })

    val sliderPanel: JPanel = new JPanel(new BorderLayout)

    slider = new JSlider(SwingConstants.HORIZONTAL, 0, 50, 0)
    slider.setMinorTickSpacing(2)
    slider.setMajorTickSpacing(10)
    slider.setPaintTicks(true)
    slider.setPaintLabels(true)

    slider.setLabelTable(slider.createStandardLabels(10))

    sliderPanel.add(new JLabel("Time units"), BorderLayout.NORTH)
    sliderPanel.add(slider, BorderLayout.SOUTH)
    options.add(sliderPanel, BorderLayout.CENTER)

    val checkboxes: JPanel = new JPanel(new GridLayout(2, 2))
    showDistance = new JCheckBox("Show distance to universe center")
    checkboxes.add(showDistance)
    showLines = new JCheckBox("Show lines from center to bodies")
    checkboxes.add(showLines)
    showVelocity = new JCheckBox("Show scaled velocity vector")
    checkboxes.add(showVelocity)
    options.add(checkboxes, BorderLayout.SOUTH)

    pane.add(options, BorderLayout.CENTER)

    val bStop: JButton = new JButton("Stop")
    options.add(bStop, BorderLayout.EAST)
    bStop.addActionListener(new ActionListener {
      def actionPerformed(action: ActionEvent) = {
        simulate = false
      }
    })
  }

  private class PaintFrame extends JPanel {

    setPreferredSize(new Dimension(drawSize, drawSize))
    val scale = drawSize / (universeRadius * 2)
    val normalizedUniverseRadius = universeRadius.toInt * scale.round.toInt

    /**
      * Simulate N-Body and repaint the gui with new positions of the bodies.
      */
    override def paint(g: Graphics) {
      g.setColor(Color.BLACK)
      g.fillRect(0, 0, normalizedUniverseRadius * 2, normalizedUniverseRadius * 2)

      g.setColor(Color.WHITE)
      g.drawOval(0, 0, normalizedUniverseRadius * 2, normalizedUniverseRadius * 2)

      val bodies = simulation.bodies
      for (b <- bodies) {
        g.setColor(b.color)
        val x: Int = (b.position.x * scale + normalizedUniverseRadius).toInt
        val y: Int = (b.position.y * scale + normalizedUniverseRadius).toInt
        g.drawOval(x - 1, y - 1, 2, 2)

        if (showVelocity.isSelected) drawVelocities(Vector2D(x, y), b, g)
        if (showDistance.isSelected) drawDistances(g, x, y)
        if (showLines.isSelected) drawLines(g, x, y)
      }

      try {
        Thread.sleep(slider.getValue)
      }
      catch {
        case e: InterruptedException => {
          e.printStackTrace
        }
      }

      if (simulate)
        simulation.simulateOneStep()

      repaint()
    }

    def drawVelocities(pos: Vector2D, body: Body, g: Graphics) = {
      val vx: Int = (pos.x + (body.velocity.x / 1E3)).toInt
      val vy: Int = (pos.y + (body.velocity.y / 1E3)).toInt

      g.drawLine(pos.x.toInt, pos.y.toInt, vx, vy)

      val tx: AffineTransform = new AffineTransform

      val arrowHead: Polygon = new Polygon
      arrowHead.addPoint(0, 3)
      arrowHead.addPoint(-3, -3)
      arrowHead.addPoint(3, -3)

      tx.setToIdentity
      tx.translate(vx, vy)

      val angle: Double = Math.atan2(vy - pos.y, vx - pos.x)
      tx.rotate((angle - Math.PI / 2d))

      val g2d: Graphics2D = g.create.asInstanceOf[Graphics2D]
      g2d.setTransform(tx)
      g2d.fill(arrowHead)
      g2d.dispose
    }

    def drawLines(g: Graphics, x: Int, y: Int) = {
      g.drawLine(normalizedUniverseRadius, normalizedUniverseRadius, x, y)
    }

    def drawDistances(g: Graphics, x: Int, y: Int) = {
      val distance: Int = Math.abs(Math.sqrt(Math.pow(y - normalizedUniverseRadius, 2) + Math.pow(x - normalizedUniverseRadius, 2))).round.toInt
      if (distance < normalizedUniverseRadius)
        g.drawString(distance + "", x + 2, y - 2)
      else {
        val vx: Double = x - normalizedUniverseRadius
        val vy: Double = y - normalizedUniverseRadius
        val nx: Double = vx / distance
        val ny: Double = vy / distance
        val kx: Double = nx * (normalizedUniverseRadius - normalizedUniverseRadius / 15)
        val ky: Double = ny * (normalizedUniverseRadius - normalizedUniverseRadius / 15)
        val cx: Int = (kx + normalizedUniverseRadius).toInt
        val cy: Int = (ky + normalizedUniverseRadius).toInt
        g.drawString(distance + "", cx + 2, cy - 2)
      }
    }
  }
}
