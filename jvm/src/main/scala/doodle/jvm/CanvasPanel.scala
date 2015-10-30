package doodle
package jvm

import doodle.core.{Color, Line, RGBA, Stroke => DoodleStroke, Vec}
import doodle.backend.{Canvas, Key}

import java.util.concurrent.ConcurrentLinkedQueue
import java.awt.{Color => AwtColor, BasicStroke, Dimension, Graphics, Graphics2D, RenderingHints, Rectangle, Shape}
import java.awt.geom.Path2D
import java.awt.event.{ActionListener, ActionEvent, KeyListener, KeyEvent}
import javax.swing.{JPanel, SwingUtilities, Timer}

import scala.collection.mutable.Queue

class CanvasPanel extends JPanel {
  import CanvasPanel._

  // Drawing must be done on the Swing thread, while calls to the Canvas
  // attached to this panel may be done by other threads. We solve this issue by
  // having the canvas communicate with the panel via a concurrent queue.
  val queue = new ConcurrentLinkedQueue[Op]()
  val canvas = new Java2DCanvas(this)

  var currentTimer: Timer = null

  /** Release any resources held by this panel */
  def close(): Unit = {
    if(currentTimer != null)
      currentTimer.stop()
  }

  override def paintComponent(graphics: Graphics): Unit = {
    val context = graphics.asInstanceOf[Graphics2D]
    context.setRenderingHints(new RenderingHints(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                              ))
    var center = Vec(getWidth/2, getHeight/2)

    var currentStroke: DoodleStroke = null
    var currentFill: Color = null
    var currentPath: Path2D = null

    retrieveOps()
    operations.foreach {
      case SetSize(width, height) =>
        setPreferredSize(new Dimension(width + 40, height + 40))
        SwingUtilities.windowForComponent(this).pack()

      case SetOrigin(x, y) =>
        center = Vec(getWidth/2, getHeight/2) + Vec(x, y)

      case Clear(color) =>
        val oldColor = context.getColor()
        context.setColor(awtColor(color))
        context.fillRect(0, 0, getWidth, getHeight)
        context.setColor(oldColor)

      case SetStroke(stroke) => 
        currentStroke = stroke

      case SetFill(color) =>
        currentFill = color

      case Stroke() =>
        if(currentStroke != null && currentPath != null) {
          val width = currentStroke.width.toFloat
          val cap = currentStroke.cap match {
            case Line.Cap.Butt => BasicStroke.CAP_BUTT
            case Line.Cap.Round => BasicStroke.CAP_ROUND
            case Line.Cap.Square => BasicStroke.CAP_SQUARE
          }
          val join = currentStroke.join match {
            case Line.Join.Bevel => BasicStroke.JOIN_BEVEL
            case Line.Join.Miter => BasicStroke.JOIN_MITER
            case Line.Join.Round => BasicStroke.JOIN_ROUND
          }
          val stroke = new BasicStroke(width, cap, join)
          val color = awtColor(currentStroke.color)

          context.setStroke(stroke)
          context.setPaint(color)

          context.draw(currentPath)
        }

      case Fill() =>
        if(currentFill != null && currentPath != null) {
          context.setPaint(awtColor(currentFill))
          context.fill(currentPath)
        }

      case BeginPath() =>
        currentPath = new Path2D.Double()

      case MoveTo(x, y) =>
        currentPath.moveTo(center.x + x, center.y - y)

      case LineTo(x, y) =>
        currentPath.lineTo(center.x + x, center.y - y)

      case BezierCurveTo(cp1x, cp1y, cp2x, cp2y, endX, endY) =>
        currentPath.curveTo(
          center.x + cp1x , center.y - cp1y,
          center.x + cp2x , center.y - cp2y,
          center.x + endX , center.y - endY
        )

      case EndPath() =>
        currentPath.closePath()

      case SetAnimationFrameCallback(callback) =>
        if(currentTimer != null) {
          currentTimer.stop()
        }
        val MsPerFrame = 17 // 17 ms per frame at 60 fps
        val listener = new ActionListener() {
          def actionPerformed(evt: ActionEvent): Unit =
            callback(evt.getWhen.toDouble)
        }
        currentTimer = new Timer(MsPerFrame, listener)
        currentTimer.setRepeats(true)
        currentTimer.start()

      case SetKeyDownCallback(callback) =>
        val listener = new KeyListener() {
          def keyPressed(evt: KeyEvent): Unit = {
            callback(KeyCode.toKey(evt.getKeyCode()))
          }

          def keyReleased(evt: KeyEvent): Unit =
            ()

          def keyTyped(evt: KeyEvent): Unit =
            ()
        }
        this.addKeyListener(listener)
    }

    // We don't want to keep re-adding callbacks everytime we get a new
    // operation, so remove operations that are not idempotent.
    stripNonIdempotentOperations()
  }
  // The Ops we have pulled off the queue
  private val operations = new Queue[Op]() 

  private def retrieveOps(): Unit = {
    var op = queue.poll()
    while(op != null) {
      op match {
        case Clear(_) =>
          // For efficiency, drop all preceding operations
          operations.clear()
        case _ =>
          ()
      }
      operations += op
      op = queue.poll()
    }
  }

  /** Remove non-idempotent operations from the queue. Idempotent operations are
    * those we can repeat without an observable effect. */
  private def stripNonIdempotentOperations(): Unit = {
    def idempotent(op: Op): Boolean = {
      op match {
        case SetAnimationFrameCallback(_) => false
        case SetKeyDownCallback(_) => false
        case _ => true
      }
    }
    val safe = operations.filter(idempotent)
    operations.clear()
    operations ++= safe
  }

  private def awtColor(color: Color): AwtColor = {
    val RGBA(r, g, b, a) = color.toRGBA
    new AwtColor(r.get, g.get, b.get, a.toUnsignedByte.get)
  }
}

object CanvasPanel {
  sealed trait Op
  final case class SetOrigin(x: Int, y: Int) extends Op
  final case class SetSize(width: Int, height: Int) extends Op
  final case class Clear(color: Color) extends Op
  final case class SetStroke(stroke: DoodleStroke) extends Op
  final case class SetFill(color: Color) extends Op
  final case class Stroke() extends Op
  final case class Fill() extends Op
  final case class BeginPath() extends Op
  final case class MoveTo(x: Double, y: Double) extends Op
  final case class LineTo(x: Double, y: Double) extends Op
  final case class BezierCurveTo(cp1x: Double, cp1y: Double, cp2x: Double, cp2y: Double, endX: Double, endY: Double) extends Op
  final case class EndPath() extends Op
  final case class SetAnimationFrameCallback(callbacl: Double => Unit) extends Op
  final case class SetKeyDownCallback(callback: Key => Unit) extends Op
}
