package widgets.charts

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLCanvasElement
import threejs._


class Label(s: String) extends Object3D {

  def init(): Unit = {

    val x = document.createElement( "canvas" ).asInstanceOf[HTMLCanvasElement]

    val (texw, texh) = (2,2)
    val xc = x.getContext("2d")
    x.width = texw * 100
    x.height = texh * 100
    xc.fillStyle = "#FFFFFF"
//    xc.strokeStyle = "#FF00FF"
    xc.font = "100pt arial bold"
    xc.textAlign = "start"
    xc.textBaseline = "middle"
    xc.fillText(s, 2, x.height/2)

//    document.body.appendChild(x)
    val texture = new CanvasTexture(x)
//    val smaterial = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = s"rgb(64,255, 128)") )
//    smaterial.metalness = 0.8f
//    smaterial.roughness = 0.3f
//    //    material.emissive = new Color("#1f4fef")
//    smaterial.emissive = new Color(s"rgb(64,255, 128)")

    val material = new MeshBasicMaterial( threejs.MeshBasicMaterialParameters(
        color = "#6A0",
        map = texture
      ))
    material.side = Material.DoubleSide

    val mesh = new Mesh( new PlaneGeometry( texw, texh ), material )
//    val mesh = new Mesh( new BoxGeometry( 200, 200, 200, 1,1,1 ), material )
    this.add(mesh)
  }

  init()
}
