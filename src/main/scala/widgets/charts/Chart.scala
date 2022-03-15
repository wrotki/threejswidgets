package widgets.charts

import objects3d.{Component, Update}
import threejs.utils.BufferGeometryUtils
import threejs.{BufferGeometry, LineLoop, _}

import scala.scalajs.js
import js.JSConverters._
import scala.collection.mutable
import scala.language.postfixOps
import scala.scalajs.js.typedarray.Float32Array

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
    var maxx = newData.indices.max
    var maxy = 0
    var maxz = 0
    for(x <- newData.indices)
      for(y <- newData(x).indices) {
        addChartBlock(x, y, newData(x)(y))
        maxy = math.max(maxy, y)
        maxz = math.max(maxz, newData(x)(y).toInt)
      }

    var geometries: js.Array[BufferGeometry] = (chartBlocks map { _.geometry } ).toJSArray

    val newMergedGeometry = BufferGeometryUtils.mergeBufferGeometries(geometries, true)
    mergedGeometry = newMergedGeometry
    val chartMaterial = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = s"rgb(64,255, 128)") )
    chartMaterial.metalness = 0.8f
    chartMaterial.roughness = 0.3f
    //    material.emissive = new Color("#1f4fef")
    chartMaterial.emissive = new Color(s"rgb(64,255, 128)")
    val chartMesh = new Mesh(mergedGeometry, chartMaterial)
    chartMesh.position.x = this.position.x
    chartMesh.position.y = this.position.y
    chartMesh.position.z = this.position.z
    this.add(chartMesh) // TODO uncomment

    val axisX = new Axis(new Vector3(maxx,0,0))
    val axisY = new Axis(new Vector3(0,maxz,0))
    val axisZ = new Axis(new Vector3(0,0,maxy))
    axisX.position.set(this.position.x, this.position.y, this.position.z)
    axisY.position.set(this.position.x, this.position.y, this.position.z)
    axisZ.position.set(this.position.x, this.position.y, this.position.z)
    this.add(axisX)
    this.add(axisY)
    this.add(axisZ)
    debugaxis = axisX

//    addDebugLoop()
//    addDebugTetrahedron()
//    addDebugTwoBoxes()
  }

  private def addChartBlock(x: Int, y: Int, chartValue: Float): Unit = {

//    println(s"addChartBlock: x: $x y: $y value: ${chartValue}")
    val geometry = new BoxGeometry(1, chartValue,1,1,1,1)
//    val attributesArray = new Float32Array(Seq(this.position.x + x, this.position.y + (chartValue/2), this.position.z + y).toJSArray)
//    geometry.setAttribute("position", new BufferAttribute(attributesArray, 3))
    geometry.translate(x,(chartValue/2), y)

//    val material = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = "#433F81") )
    val material = new MeshStandardMaterial( threejs.MeshStandardMaterialParameters(color = s"rgb(${x % 256}, ${y % 256}, 128)") )
    material.metalness = 0.8f
    material.roughness = 0.3f
//    material.emissive = new Color("#1f4fef")
    material.emissive = new Color(s"rgb(${x % 256}, ${y % 256}, 128)")
    val mesh = new Mesh(geometry, material)
//    mesh.position.x = this.position.x + x
//    mesh.position.y = this.position.y + (chartValue/2)
//    mesh.position.z = this.position.z + y // Note y becoming z

//    this.add(mesh) // TODO render mergedGeometry instead

    this.chartBlocks = this.chartBlocks :+ mesh
  }

  private def addDebugLoop(): Unit = {
    val geometry = new BufferGeometry()
    val numPoints = 5
    val radius = 70
    var positions = new Float32Array( numPoints * 3 )

    for ( i <- 0 until numPoints ) {
      val s = i * Math.PI * 2 / numPoints
      positions( i * 3 ) = radius * Math.sin( s ).toFloat
      positions( i * 3 + 1 ) = radius * Math.cos( s ).toFloat
      positions( i * 3 + 2 ) = 0f

    }

    geometry.setAttribute( "position", new BufferAttribute( positions, 3 ) )
    val obj = new LineLoop( geometry, new LineBasicMaterial( LineMaterialParameters("#ffff00")))
    obj.position.set( 0, 0, - 200 );

    this.add( obj );
  }

  private def addDebugTetrahedron(): Unit = {
    val material = new MeshBasicMaterial( MeshBasicMaterialParameters(
      "#ff8000"
    ))

    val obj = new Mesh( new TetrahedronGeometry( 15, 0 ), material )
    obj.position.set( -10, 0, -20 )
//    obj.name = 'Tetrahedron';
    this.add( obj )
  }

  private def addDebugTwoBoxes(): Unit = {
    val material = new MeshBasicMaterial( MeshBasicMaterialParameters(
      "#ff8000"
    ))

    val bg1 = new BoxGeometry(1, 1,1,1,1,1)
//    val aa1 = new Float32Array(Seq(-10f, 0f, -10f).toJSArray)
//    bg1.setAttribute("position", new BufferAttribute(aa1, 3))
    bg1.translate(-10f,-10f,0f)

    val bg2 = new BoxGeometry(2, 2,2,1,1,1)
//    val aa2 = new Float32Array(Seq(10f, 0f, -10f).toJSArray)
//    bg2.setAttribute("position", new BufferAttribute(aa2, 3))
    bg2.translate(10f,10f,0f)

    var geometries: js.Array[BufferGeometry] = (Seq(bg1,bg2) map { _.asInstanceOf[BufferGeometry] }).toJSArray

    val newMergedGeometry = BufferGeometryUtils.mergeBufferGeometries(geometries, true)

    val obj = new Mesh( newMergedGeometry, material )
    obj.position.set( 0, 0, -10 )
//    obj.name = 'TwoBoxes';
    this.add( obj )

    val o1 = new Mesh( bg1, material )
//    o1.position.set( -10, 10, -30 )
//    this.add( o1 )
    val o2 = new Mesh( bg2, material )
//    o2.position.set( 10, 10, -30 )
//    this.add( o2 )
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
