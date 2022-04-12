package widgets.charts

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLCanvasElement
import threejs._


class Label extends Object3D {

  def init(): Unit = {

    val x = document.createElement( "canvas" ).asInstanceOf[HTMLCanvasElement]
    val xc = x.getContext("2d")
    x.width = 250
    x.height = 250
    xc.fillStyle = "#000"
    xc.font = "50pt arial bold"
    xc.fillText("It's a me, label", 10, 100)

    val texture = new CanvasTexture(x)
    val material = new MeshBasicMaterial( threejs.MeshBasicMaterialParameters(
        map = texture
      ))

    val mesh = new Mesh( new PlaneGeometry( 200, 200 ), material )

    this.add(mesh)
  }

  init()
}
