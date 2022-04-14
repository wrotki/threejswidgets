package widgets.charts

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLCanvasElement
import threejs._


class Label extends Object3D {

  def init(): Unit = {

    val x = document.createElement( "canvas" ).asInstanceOf[HTMLCanvasElement]

    val xc = x.getContext("2d")
    x.width = 200
    x.height = 200
    xc.fillStyle = "#FFFFFF"
//    xc.strokeStyle = "#FF00FF"
    xc.font = "20pt arial bold"
//    xc.textAlign = "center"
//    xc.textBaseline = "middle"
//    xc.canvas.width = 200
//    xc.canvas.height = 200
//    xc.fillRect(0, 0, xc.canvas.width, xc.canvas.height)
    xc.fillText("It's a me, label", 20, 20)

    document.body.appendChild(x)


    val texture = new CanvasTexture(x)

    val smaterial = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = s"rgb(64,255, 128)") )
    smaterial.metalness = 0.8f
    smaterial.roughness = 0.3f
    //    material.emissive = new Color("#1f4fef")
    smaterial.emissive = new Color(s"rgb(64,255, 128)")

    val material = new MeshBasicMaterial( threejs.MeshBasicMaterialParameters(
        color = "#6A0"
      ,
        map = texture
      ))

    val mesh = new Mesh( new PlaneGeometry( 200, 200 ), material )
//    val mesh = new Mesh( new BoxGeometry( 200, 200, 200, 1,1,1 ), material )
    this.add(mesh)
  }

  init()
}
