package widgets.charts

import threejs.{BoxGeometry, BufferGeometry, Color, Group, Material, Mesh, MeshBasicMaterial, MeshStandardMaterial, Object3D, Vector3}
import objects3d.{Component, Update}

import scala.language.postfixOps

class Chart(rows: Int, cols: Int) extends Group() with Update {

  // TODO components and update should likely be in some base trait
  var components = Seq.empty[Component]

  def update: Unit = {
    for (c <- components)
      c.update()
  }

  // Proper Chart properties
  var data: Array[Array[Int]] = Array.ofDim(rows, cols)
  var chartBlocks: Seq[Mesh] = Seq()

  // TODO perhaps it'd be better to have a constructor to take data
  def setData(newData: Array[Array[Int]]): Unit = {
    for(x <- newData.indices)
      for(y <- newData(x).indices)
        addChartBlock(x, y, newData(x)(y))
  }

  private def addChartBlock(x: Int, y: Int, chartValue: Int): Unit = {

    println(s"addChartBlock: x: $x y: $y value: ${chartValue}")
    val geometry = new BoxGeometry(1, chartValue,1,1,1,1)
//    val material = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = "#433F81") )
    val material = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = s"rgb(${x % 256}, ${y % 256}, 128)") )
    material.metalness = 0.8f
    material.roughness = 0.3f
//    material.emissive = new Color("#1f4fef")
    material.emissive = new Color(s"rgb(${x % 256}, ${y % 256}, 128)")
    val mesh = new Mesh(geometry, material)
    mesh.position.x = this.position.x + x
    mesh.position.y = this.position.y + (chartValue/2)
    mesh.position.z = this.position.z - y // Note y becoming -z
    this.add(mesh)
    this.chartBlocks = this.chartBlocks :+ mesh
  }
}

object Chart {

  def apply(rows: Int, cols: Int): Chart = {
    val ret = new Chart(rows, cols)
//    ret.components = ret.components :+ new MoveInCircleComponent(ret) :+ new RotateComponent(ret)
    ret
  }

  // TODO drop rows, cols from here
  def apply(rows: Int, cols: Int, x: Float, y: Float, z: Float /* , label, blockColor, series*/): Chart = {
    val ret = Chart(rows, cols)
    ret.position.x = x
    ret.position.y = y
    ret.position.z = z
    ret
  }
}
