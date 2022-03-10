package widgets.charts

import objects3d.{Component, Update}
import threejs.utils.BufferGeometryUtils
import threejs.{BufferGeometry, _}

import scala.scalajs.js
import js.JSConverters._
import scala.collection.mutable
import scala.language.postfixOps

class Chart(rows: Int, cols: Int) extends Group() with Update {

  // TODO components and update should likely be in some base trait
  var components = Seq.empty[Component]
  var debugaxis: Axis = _
  var mergedGeometry: BufferGeometry = _

  // Proper Chart properties
  var data: Array[Array[Float]] = Array.ofDim(rows, cols)
  var chartBlocks: Seq[Mesh] = Seq()

  def update: Unit = {
    for (c <- components)
      c.update()
  }

  // TODO perhaps it'd be better to have a constructor to take data
  def setData(newData: Array[Array[Float]]): Unit = {
    for(x <- newData.indices)
      for(y <- newData(x).indices)
        addChartBlock(x, y, newData(x)(y))

    var geometries: js.Array[BufferGeometry] = (chartBlocks map { _.geometry } ).toJSArray

    mergedGeometry = BufferGeometryUtils.mergeBufferGeometries(geometries, false)
    val chartMaterial = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = s"rgb(64,255, 128)") )
    chartMaterial.metalness = 0.8f
    chartMaterial.roughness = 0.3f
    //    material.emissive = new Color("#1f4fef")
    chartMaterial.emissive = new Color(s"rgb(64,255, 128)")
    val chartMesh = new Mesh(mergedGeometry, chartMaterial)

    this.add(chartMesh) // TODO uncomment

    val axisX = new Axis(new Vector3(1,0,0))
    val axisY = new Axis(new Vector3(0,1,0))
    val axisZ = new Axis(new Vector3(0,0,1))
    axisX.position.set(this.position.x, this.position.y, this.position.z)
    axisY.position.set(this.position.x, this.position.y, this.position.z)
    axisZ.position.set(this.position.x, this.position.y, this.position.z)
    this.add(axisX)
    this.add(axisY)
    this.add(axisZ)
    debugaxis = axisX
  }

  private def addChartBlock(x: Int, y: Int, chartValue: Float): Unit = {

//    println(s"addChartBlock: x: $x y: $y value: ${chartValue}")
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
    mesh.position.z = this.position.z + y // Note y becoming z

//    this.add(mesh) // TODO render mergedGeometry instead

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
