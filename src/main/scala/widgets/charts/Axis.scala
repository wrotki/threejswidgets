package widgets.charts

import objects3d.{Component, Update}
import threejs.{BufferAttribute, BufferGeometry, Group, Line, LineBasicMaterial, LineMaterialParameters, Vector3}

import scala.scalajs.js.typedarray.Float32Array

// https://github.com/mrdoob/three.js/blob/master/examples/misc_exporter_gltf.html
// THREE.Line Strip
// THREE.Line Loop

class Axis(dimensions: Vector3) extends Group() with Update {
  // TODO components and update should likely be in some base trait
  var components = Seq.empty[Component]

  def update: Unit = {
    for (c <- components)
      c.update()
  }

  def init(): Unit = {
    val geometry = new BufferGeometry()
    val numPoints = 2
    val radius = 10
    val positions = new Float32Array(numPoints * 3)
    for (p <- 0 until numPoints) {
      positions(p * 3) = 0
      positions(p * 3 + 1) = p * radius
      positions(p * 3 + 2) = 0
    }
    geometry.setAttribute( "position", new BufferAttribute( positions, 3 ) )
    val obj = new Line( geometry, new LineBasicMaterial( LineMaterialParameters("#ffff00") ) )
    this.add(obj)
  }

  init()
}
